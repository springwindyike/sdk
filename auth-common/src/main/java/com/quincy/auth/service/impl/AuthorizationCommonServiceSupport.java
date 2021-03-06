package com.quincy.auth.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.quincy.auth.o.XSession;
import com.quincy.auth.service.AuthorizationCommonService;

public abstract class AuthorizationCommonServiceSupport implements AuthorizationCommonService {
	protected abstract Object getUserObject(HttpServletRequest request) throws Exception;

	public XSession getSession(HttpServletRequest request) throws Exception {
		Object obj = this.getUserObject(request);
		return obj==null?null:(XSession)obj;
	}
}