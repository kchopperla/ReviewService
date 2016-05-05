package com.intuit.reviewengine.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.controllers.ReviewManagementController;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.ProfileService;
import com.intuit.reviewengine.services.ReviewService;

public class ReviewManagementControllerUnitTest {

	@Autowired
	ReviewManagementController reviewManagementController;

	private @Mocked ProfileService profileService;

	private @Mocked ReviewService reviewService;

	private @Mocked ControllerUtils controllerUtils;

	@Before
	public void setUp() {
		reviewManagementController = new ReviewManagementController();
		reviewManagementController.setProfileService(profileService);
		reviewManagementController.setControllerUtils(controllerUtils);
		reviewManagementController.setReviewService(reviewService);
	}

	/**
	 * This test case checks if HttpResponse code is OK or not for passing an id parameter
	 * @param reviewResource
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	public void testGetReview_success(@Mocked final ReviewResource reviewResource, @Mocked final Integer id,
			@Mocked HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			reviewService.findReviewById(id);result=reviewResource;
		}};

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.getReview(request, response, id);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}


	/**
	 * This test case checks if the class throws IntuitReviewEngineException when no id is passed to fetch a review.
	 * @param reviewResource
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testGetReview_error(@Mocked ReviewResource reviewResource,
			@Mocked HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.getReview(request, response, null);
	}

	/**
	 * This test is to write/post/create a review successfully.
	 * @param reviewResource
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	public void testCreateReview_success(@Mocked final ReviewResource reviewResource, @Mocked final Integer id,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.isValidUser(request);result=true;
			controllerUtils.setProfileIdInReviewResource(request, reviewResource);
			reviewService.save(reviewResource);result=reviewResource;
		}};

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.createReview(request, response, reviewResource);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}

	/**
	 * This test case is when user tries to create a review and something fails on the backend.
	 * @param reviewResource
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testCreateReview_error(@Mocked final ReviewResource reviewResource, @Mocked final Integer id,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.isValidUser(request);result=true;
			controllerUtils.setProfileIdInReviewResource(request, reviewResource);
			reviewService.save(reviewResource);result=null;
		}};

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.createReview(request, response, reviewResource);
	}

	/**
	 * This test is to create a review without logging in ( or invalid credentials).
	 * @param reviewResource
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testCreateReview_invalid_user_error(@Mocked final ReviewResource reviewResource, @Mocked final Integer id,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.isValidUser(request);result=false;
			controllerUtils.setProfileIdInReviewResource(request, reviewResource);
			reviewService.save(reviewResource);result=null;
		}};
		try {	
			ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.createReview(request, response, reviewResource);
		}catch (Exception e){
			Assert.assertTrue(e.getMessage().equals("403.forbidden"));
			throw e;
		}
	}

	/**
	 * This test case is to verify the edit review functionality. 
	 * @param reviewResource
	 * @param reviewId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	public void testEditReview_success(@Mocked final ReviewResource reviewResource, @Mocked final Integer reviewId,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			reviewResource.getId();result=100;
			controllerUtils.isUserAllowedForReviewModifications(request,100);result = true;
			controllerUtils.setProfileIdInReviewResource(request, reviewResource);
			reviewService.save(reviewResource);result = reviewResource;
		}};

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.editReview(request, response, reviewResource);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}

	/**
	 * This test case when user tries to edit a review with bad parameters.
	 * @param request
	 * @param response
	 * @param reviewResource
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testEditReview_improper_request_error(@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response,
			@Mocked final ReviewResource reviewResource) throws Exception {

		new NonStrictExpectations() {{
			reviewResource.getId();result=null;
		}};

		try {
			ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.createReview(request, response, null);
		}catch (Exception e){
			Assert.assertTrue(e.getMessage().equals("400.bad.request"));
			throw e;
		}
	}

	/**
	 * This test case is when user tries to edit the user not owned by them.
	 * @param reviewResource
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testEditReview_invalid_user_error(@Mocked final ReviewResource reviewResource, @Mocked final Integer id,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.isValidUser(request);result=false;
			controllerUtils.setProfileIdInReviewResource(request, reviewResource);
			reviewService.save(reviewResource);result=null;
		}};
		try {
			ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.createReview(request, response, reviewResource);
		}catch (Exception e){
			Assert.assertTrue(e.getMessage().equals("403.forbidden"));
			throw e;
		}
	}

	/**
	 * This test case is to delete the review owned by the user.
	 * @param reviewService
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	public void testDeleteReview_success(@Mocked final ReviewService reviewService,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.isUserAllowedForReviewModifications(request,100);result = true;
			reviewService.delete(100);
		}};

		ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.deleteReview(request, response, 100);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}

	/**
	 * This test case when user tries to delete a review that was never written before.
	 * @param reviewService
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testDeleteReview_invalid_id_error(@Mocked final ReviewService reviewService,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {
		try {
			ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.deleteReview(request, response, null);
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().equals("400.bad.request"));
			throw e;
		}
	}


	/**
	 * This test case when user tries to delete a user that is not owned.
	 * @param reviewService
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testDeleteReview_security_error(@Mocked final ReviewService reviewService,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {
		new NonStrictExpectations() {{
			controllerUtils.isUserAllowedForReviewModifications(request,100);result = false;
		}};
		try {
			ResponseEntity<ReviewResource> result = (ResponseEntity<ReviewResource>) reviewManagementController.deleteReview(request, response, 100);
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().equals("403.forbidden"));
			throw e;
		}
	}
}