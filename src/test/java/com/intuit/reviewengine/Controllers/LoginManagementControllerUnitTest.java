package com.intuit.reviewengine.Controllers;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.controllers.ControllerConstants;
import com.intuit.reviewengine.controllers.LoginManagementController;
import com.intuit.reviewengine.controllers.ProductManagementController;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.models.Credentials;
import com.intuit.reviewengine.resources.ProductResource;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.services.ProductService;
import com.intuit.reviewengine.services.ProfileService;

public class LoginManagementControllerUnitTest {

	@Mocked ProfileService profileService;
	
	LoginManagementController loginManagementController;
	
	@Before
	public void setUp() {
		loginManagementController = new LoginManagementController();
		loginManagementController.setProfileService(profileService);
	}
	
	/**
	 * This test case is when credentials are correct.
	 * @param credentials
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @param profileResource
	 * @throws Exception
	 */
	@Test
	public void testAuthenticate_success(final @Mocked Credentials credentials,final @Mocked String username, final @Mocked String password,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response, final @Mocked ProfileResource profileResource) throws Exception {

		new NonStrictExpectations() {{
			credentials.getUsername();result=username;
			credentials.getPassword();result=password;
			profileService.authenticate(credentials.getUsername(),credentials.getPassword());result=profileResource;
		}};

		ResponseEntity<ProfileResource> result = (ResponseEntity<ProfileResource>) loginManagementController.authenticate(request, response, credentials);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}

	/**
	 * This test case is for verifying when the credentials are wrong (i.e not authorized).
	 * @param credentials
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testAuthenticate_error(final @Mocked Credentials credentials,final @Mocked String username, final @Mocked String password,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			credentials.getUsername();result=username;
			credentials.getPassword();result=password;
			profileService.authenticate(credentials.getUsername(),credentials.getPassword());result=null;
		}};
		try {
			ResponseEntity<ProfileResource> result = (ResponseEntity<ProfileResource>) loginManagementController.authenticate(request, response, credentials);
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().equals("401.unauthorized.error"));
			throw e;
		}
	}
}
