package com.intuit.reviewengine.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.intuit.reviewengine.controllers.ControllerConstants;
import com.intuit.reviewengine.controllers.ProductManagementController;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.ProfileService;
import com.intuit.reviewengine.services.ReviewService;


@Component
public class ControllerUtils {

	private static final Logger logger = LoggerFactory.getLogger(ControllerUtils.class);

	@Autowired
	ReviewService reviewService;

	@Autowired
	ProfileService profileService;

	public boolean isUserAllowedForReviewModifications(HttpServletRequest request, Integer reviewId){

		boolean success = false;
		try {
			String serviceSessionId = request.getHeader(ControllerConstants.SERVICE_SESSION_ID);
			if(StringUtils.isEmpty(serviceSessionId)){
				serviceSessionId = ControllerUtils.getCookie(request, ControllerConstants.SERVICE_SESSION_ID);
			}
			ReviewResource reviewResource = reviewService.findReviewById(reviewId);
			if(reviewResource!=null){
				Integer profileId = reviewResource.getProfileId();
				ProfileResource profileResource = profileService.getProfileById(profileId);
				if(profileResource!=null){
					String serviceSessionId_ = profileResource.getServiceSessionId();
					if(!StringUtils.isEmpty(serviceSessionId) && (serviceSessionId_).equals(serviceSessionId)){
						success = true;
					}else{
						logger.info("INVALID_USER_FOR_REVIEW_MODIFICATIONS:serviceSessionIdFromUser:"+serviceSessionId+
								" serviceSessionIdFromReviewsProfile:"+serviceSessionId_);
					}
				}

			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return success;
	}

	public boolean isValidUser(HttpServletRequest request) throws Exception{

		boolean success = false;

		String serviceSessionId = request.getHeader(ControllerConstants.SERVICE_SESSION_ID);
		if(StringUtils.isEmpty(serviceSessionId)){
			serviceSessionId = ControllerUtils.getCookie(request, ControllerConstants.SERVICE_SESSION_ID);
		}
		if(StringUtils.isEmpty(serviceSessionId)){

		}else{
			success = profileService.validServiceSession(serviceSessionId);
		}
		logger.info("VALID_USER:"+success+" serviceSessionIdFromUser:"+serviceSessionId);
		return success;
	}

	public Integer getProfileId(HttpServletRequest request) throws Exception{
		
		Integer profileId = null;
		String serviceSessionId = request.getHeader(ControllerConstants.SERVICE_SESSION_ID);
		if(StringUtils.isEmpty(serviceSessionId)){
			serviceSessionId = ControllerUtils.getCookie(request, ControllerConstants.SERVICE_SESSION_ID);
		}
		if(StringUtils.isEmpty(serviceSessionId)){
			
		}else {
			ProfileResource profileResource = profileService.getProfileBySessionId(serviceSessionId);
			if(profileResource!=null){
				profileId=profileResource.getId();
			}
		}
		return profileId;
	}
	
	public static String getCookie(HttpServletRequest request, String cookieName) { 
		Cookie[] cookies = request.getCookies(); 
		String value = ""; 
		if (cookies != null) { 
			for(int i=0, j=cookies.length; i<j; i++) { 
				if (cookieName.equals(cookies[i].getName())) { 
					value = cookies[i].getValue();
				}
			}
		}
		return value;
	}

	public static void addCookie(HttpServletResponse response, String
			cookieName, String cookieValue, int age, boolean secure, String domain,
			String path) { 
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(age); 
		cookie.setSecure(secure); 
		if (domain != null) {
			cookie.setDomain(domain); 
		}
		if (path != null) { cookie.setPath(path);
		}
		response.addCookie(cookie); 
	}
	
	public void setProfileIdInReviewResource(HttpServletRequest request,ReviewResource reviewResource) throws Exception{
		if(reviewResource!=null){
			String serviceSessionId = request.getHeader(ControllerConstants.SERVICE_SESSION_ID);
			if(StringUtils.isEmpty(serviceSessionId)){
				serviceSessionId = ControllerUtils.getCookie(request, ControllerConstants.SERVICE_SESSION_ID);
			}
			
			if(!StringUtils.isEmpty(serviceSessionId)){
				ProfileResource profileResource = profileService.getProfileBySessionId(serviceSessionId);
				reviewResource.setProfileId(profileResource.getId());
			}
			
		}
	}

}