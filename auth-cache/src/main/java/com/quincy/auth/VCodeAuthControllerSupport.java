package com.quincy.auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.quincy.auth.controller.AuthorizationControllerSupport;
import com.quincy.sdk.RedisProcessor;
import com.quincy.sdk.Result;
import com.quincy.sdk.annotation.JedisInjector;
import com.quincy.sdk.annotation.VCodeRequired;

import redis.clients.jedis.Jedis;

public abstract class VCodeAuthControllerSupport extends AuthorizationControllerSupport {
	@Autowired
	private RedisProcessor redisProcessor;
	@Resource(name = "loginFailuresHolderKey")
	private String loginFailuresHolderKey;
	private final static int FAILURES_THRESHOLD_FOR_VCODE = 3;
	/**
	 * 密码登录
	 */
	@JedisInjector
	@PostMapping("/signin/pwd")
	public ModelAndView doLogin(HttpServletRequest request, 
			@RequestParam(required = false, value = "username")String username, 
			@RequestParam(required = false, value = "password")String password, 
			@RequestParam(required = false, value = "vcode")String vcode, 
			@RequestParam(required = false, value = AuthConstants.PARAM_BACK_TO)String _backTo, 
			Jedis jedis) throws Exception {
		Result result = null;
		String _failures = jedis.hget(loginFailuresHolderKey, username);
		int failures = _failures==null?0:Integer.parseInt(_failures);
		if(failures<FAILURES_THRESHOLD_FOR_VCODE) {
			result = doPwdLogin(request, username, password, failures, jedis);
		} else {
			result = redisProcessor.validateVCode(request);
			if(result.getStatus()==1)
				result = doPwdLogin(request, username, password, failures, jedis);
		}
		if(result.getStatus()==1)
			jedis.hdel(loginFailuresHolderKey, username);
		ModelAndView mv = createModelAndView(request, result, _backTo);
		return mv;
	}

	private Result doPwdLogin(HttpServletRequest request, String username, String password, long failures, Jedis jedis) throws Exception {
		Result result = doPwdLogin(request, username, password);
		if(result.getStatus()==AuthConstants.LOGIN_STATUS_PWD_INCORRECT) {
			jedis.hincrBy(loginFailuresHolderKey, username, 1);
			if(failures+1>=FAILURES_THRESHOLD_FOR_VCODE)
				result.setStatus(AuthConstants.LOGIN_STATUS_PWD_INCORRECT-1);
		}
		return result;
	}
	/**
	 * 验证码登录
	 */
	@VCodeRequired
	@PostMapping("/signin/vcode")
	public ModelAndView doLogin(HttpServletRequest request, 
			@RequestParam(required = false, value = "username")String _username, 
			@RequestParam(required = false, value = AuthConstants.PARAM_BACK_TO)String _backTo) throws Exception {
		Result result = this.login(request, _username, null);
		return this.createModelAndView(request, result, _backTo);
	}
}