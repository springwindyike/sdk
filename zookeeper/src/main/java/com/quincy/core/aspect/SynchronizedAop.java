package com.quincy.core.aspect;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.quincy.core.zookeeper.OriginalZooKeeperFactory;
import com.quincy.sdk.annotation.Synchronized;
import com.quincy.sdk.helper.AopHelper;
import com.quincy.sdk.zookeeper.Context;

import lombok.Data;

@Aspect
@Component
public class SynchronizedAop {
	@Autowired
	private OriginalZooKeeperFactory factory;
	@Autowired
	private Context context;
	@Value("${zookeeper.distributed_lock.retries}")
	private int retries;
	@Value("${zookeeper.distributed_lock.timeout}")
	private int timeout;
	private final static String KEY = "execution";

	@Pointcut("@annotation(com.quincy.sdk.annotation.Synchronized)")
    public void pointCut() {}

	@Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Synchronized annotation = AopHelper.getAnnotation(joinPoint, Synchronized.class);
		String key = annotation.value();
		ZooKeeper zk = null;
		try {
			zk = factory.connect();
			String path = context.getSynPath()+"/"+key;
			String realPath = zk.create(path+"/"+KEY, KEY.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			String seqStr = realPath.substring(realPath.indexOf(KEY)+KEY.length(), realPath.length());
			int seq = Integer.parseInt(seqStr);
			Lock lock = new Lock();
			int retried = 0;
			while(true) {
				List<String> pathes = zk.getChildren(path, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						synchronized(lock) {
							lock.setNotified(true);
							lock.notifyAll();
						}
					}
				});
				int minSeq = -1;
				for(String p:pathes) {
					int toComparedSeq = Integer.parseInt(p.substring(KEY.length(), p.length()));
					if(minSeq==-1||toComparedSeq<minSeq)
						minSeq = toComparedSeq;
				}
				if(seq>minSeq) {//没拿到锁
					synchronized(lock) {
						lock.wait(timeout);
					}
					if(!lock.isNotified()) {
						retried++;
						if(retried>=retries)
							throw new RuntimeException("Distributed Lock Timeout!");
					}
					lock.setNotified(false);
				} else//拿到锁
					break;
			}
			return joinPoint.proceed();
		} finally {
			zk.close();
		}
	}

	@Data
	private class Lock {
		private boolean notified = false;
	}
}