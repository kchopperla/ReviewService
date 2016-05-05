package com.intuit.reviewengine.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.models.Credentials;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.services.ProfileService;


/**
 * This controller is mainly for REST operations with login management.
 * 
 * @author kchopperla
 *
 */
@RestController
@RequestMapping("/v1/login")
@Api(description = "login", tags = {"/v1/login"})
public class LoginManagementController {

	
	private static final Logger logger = LoggerFactory.getLogger(LoginManagementController.class);

	@Autowired
	ProfileService profileService;
	
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation("It authenticates the user.")
	public HttpEntity<ProfileResource> authenticate(HttpServletRequest request,HttpServletResponse response,
			@RequestBody Credentials credentials) throws Exception{
		ProfileResource profileResource=null;
		try {
			if(credentials==null){
				throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST, "400.bad.request");
			}else {
				profileResource = profileService.authenticate(credentials.getUsername(),credentials.getPassword());
				if(profileResource==null){
					logger.error("INVALID_CREDENTIALS:USERNAME:"+credentials.getUsername());
					throw new IntuitReviewEngineException(HttpStatus.UNAUTHORIZED, "401.unauthorized.error");
				}else {
					response.setHeader(ControllerConstants.SERVICE_SESSION_ID,profileResource.getServiceSessionId());
					ControllerUtils.addCookie(response, ControllerConstants.SERVICE_SESSION_ID, profileResource.getServiceSessionId(), -1, false, ".localhost", "/");
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				response.setContentLength(0);
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}
}