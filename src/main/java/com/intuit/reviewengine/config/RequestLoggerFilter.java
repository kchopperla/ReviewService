package com.intuit.reviewengine.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class RequestLoggerFilter implements Filter  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggerFilter.class);
	private static final String SESSION_ID_KEY = "SESSION_ID";
	 	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//nothing much to do.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest httpRequest = ((HttpServletRequest)request);
			HttpSession session = httpRequest.getSession();
			if (request instanceof HttpServletRequest) {
				MDC.put(SESSION_ID_KEY, session.getId());
			}
			
			Cookie[] cookies = httpRequest.getCookies();
			
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("URI:{}, method:{}, queryString:{}, cookies:{}", 
						httpRequest.getPathInfo(), 
						httpRequest.getMethod(),
						httpRequest.getQueryString(),
						httpRequest.getHeader("Cookie")
						);
			}
			
			chain.doFilter(request, response);
		}
		finally {
			MDC.remove(SESSION_ID_KEY);
		}
	}

	@Override
	public void destroy() {
		//nothing much to do.
	}
}
