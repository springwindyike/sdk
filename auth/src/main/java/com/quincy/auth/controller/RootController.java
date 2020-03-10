package com.quincy.auth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quincy.auth.AuthConstants;
import com.quincy.auth.annotation.LoginRequired;
import com.quincy.auth.o.DSession;
import com.quincy.auth.service.AuthorizationService;

@Controller
@RequestMapping("")
public class RootController {
	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping("")
	public String root(HttpServletRequest request) throws Exception {
		DSession session = authorizationService.getSession(request);
		String uri = session==null?"/auth/signin":AuthConstants.URI_INDEX;
		return "redirect:"+uri;
	}

	@LoginRequired
	@GetMapping(value = AuthConstants.URI_INDEX)
	public String index() {
		return AuthConstants.URI_INDEX;
	}

	@GetMapping(value = "/static/**")
	public void handleStatic() {}
}