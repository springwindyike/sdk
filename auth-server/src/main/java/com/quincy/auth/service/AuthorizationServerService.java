package com.quincy.auth.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.quincy.auth.o.DSession;
import com.quincy.auth.o.User;

public interface AuthorizationServerService {
//	public DSession setSession(String jsessionid, String originalJsessionid, Long userId, AuthCallback callback) throws IOException, ClassNotFoundException;
	public DSession setSession(HttpServletRequest request, String originalJsessionid, Long userId, AuthCallback callback) throws Exception;
	public void updateSession(User user) throws IOException;
	public <T extends User> void updateSession(List<T> users) throws IOException;
}