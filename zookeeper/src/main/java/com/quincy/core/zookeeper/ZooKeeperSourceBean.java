package com.quincy.core.zookeeper;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

import com.quincy.core.zookeeper.impl.ZooKeeperSourceImpl;

public class ZooKeeperSourceBean implements FactoryBean<ZooKeeperSource>, InitializingBean {
	private String url;
	private int timeout;
	private Watcher watcher;
	private GenericObjectPoolConfig poolCfg;
	private AbandonedConfig abandonedCfg;
	private boolean singleton = true;
	@Nullable
	private ZooKeeperSource singletonInstance;

	public ZooKeeperSourceBean(String url, int timeout, Watcher watcher, GenericObjectPoolConfig poolCfg, AbandonedConfig abandonedCfg) {
		this.url = url;
		this.timeout = timeout;
		this.watcher = watcher;
		this.poolCfg = poolCfg;
		this.abandonedCfg = abandonedCfg;
	}

	@Override
	public ZooKeeperSource getObject() throws Exception {
		return this.singletonInstance;
	}

	@Override
	public Class<ZooKeeperSource> getObjectType() {
		return ZooKeeperSource.class;
	}

	public final void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	@Override
	public final boolean isSingleton() {
		return this.singleton;
	}

	@Override
	public void afterPropertiesSet() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.singleton)
			this.singletonInstance = create();
	}

	private ZooKeeperSource create() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		PoolableZooKeeperFactory f = new PoolableZooKeeperFactory(url, timeout, watcher);
		return new ZooKeeperSourceImpl(f, poolCfg, abandonedCfg);
	}
}