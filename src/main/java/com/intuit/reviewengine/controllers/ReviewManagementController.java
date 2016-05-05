package com.intuit.reviewengine.controllers;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.ProfileService;
import com.intuit.reviewengine.services.ReviewService;

/**
 * This controller is the crux of the entire review engine. 
 * It performs various functions.
 * 
 * GET review function fetches the review based on id if present.
 * 
 * POST/PUT/DELETE rest calls can actually create/edit/delete a 
 * review. But, this controller adds additional security 
 * 1. looking for session token either in the cookie or the header (which means user needs to log into the system)
 * 2. Every review posted has a profile id in the system. And every profile id has a unique session token.
 *    So, this controller makes sure that there is a 2 way match while doing any modify/delete review.
 * 3. This also makes sure that we dont reveal any additional information about other profiles who posted
 *    the reviews. It only provides the username of the person who posted.
 * 4. It also makes sure that when a review is being modified, the productId, createdDate and the
 *    productId of the review arr unaffected which is very important when reviews are being edited.     
 *
 * @author kchopperla
 *
 */
@RestController
@RequestMapping("/v1/reviews")
public class ReviewManagementController {

	private static final Logger logger = LoggerFactory.getLogger(ReviewManagementController.class);

	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ProfileService profileService;

	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}

	public void setControllerUtils(ControllerUtils controllerUtils) {
		this.controllerUtils = controllerUtils;
	}

	@Autowired
	ControllerUtils controllerUtils;


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<ReviewResource> getReview(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id) throws Exception{
		ReviewResource reviewResource = null;
		try {
			reviewResource = reviewService.findReviewById(id);
			if(reviewResource==null){
				logger.error("REVIEW_RESOURCE_IS_NULL");
				throw new IntuitReviewEngineException(HttpStatus.NOT_FOUND, "404.not.found.review"); 
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}
		return new ResponseEntity(reviewResource, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public HttpEntity<ReviewResource> createReview(HttpServletRequest request,HttpServletResponse response, @RequestBody ReviewResource reviewResource) throws Exception{
		try {
			if(reviewResource==null){
				throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST, "400.bad.request"); 
			}else {

				if(controllerUtils.isValidUser(request)){
					// Set create date.
					Calendar calendar = Calendar.getInstance();
					Date createdDate =  calendar.getTime();
					reviewResource.setCreatedDate(createdDate);

					// Set the profile id by calling service_session_id
					controllerUtils.setProfileIdInReviewResource(request, reviewResource);
					
					reviewResource = reviewService.save(reviewResource);
					if(reviewResource==null){
						throw new IntuitReviewEngineException(HttpStatus.NOT_FOUND, "404.not.found.review"); 
					}
					reviewResource.setOwnsReview(true);
				}else{
					logger.error("POST_REVIEW_FAILED:"+ reviewResource);
					throw new IntuitReviewEngineException(HttpStatus.FORBIDDEN, "403.forbidden"); 
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}
		return new ResponseEntity(reviewResource, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public HttpEntity deleteReview(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) throws Exception{

		try {
			if(id==null){
				throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST, "400.bad.request"); 
			}else {
				if(controllerUtils.isUserAllowedForReviewModifications(request,id)){
					reviewService.delete(id);
				}else{
					throw new IntuitReviewEngineException(HttpStatus.FORBIDDEN, "403.forbidden"); 
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				logger.error("PUT_REVIEW_FAILED:"+ id);
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public HttpEntity<ReviewResource> editReview(HttpServletRequest request,HttpServletResponse response, @RequestBody ReviewResource reviewResource) throws Exception {
		try {
			if(reviewResource==null){
				throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST, "400.bad.request"); 
			}else {
				Integer reviewId = reviewResource.getId();
				if(reviewId==null){
					throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST, "400.bad.request"); 
				}else{
					if(controllerUtils.isUserAllowedForReviewModifications(request,reviewId)){
						// Set the profile id by calling service_session_id
						controllerUtils.setProfileIdInReviewResource(request, reviewResource);
						reviewResource = reviewService.save(reviewResource);
						if(reviewResource!=null){
							reviewResource.setOwnsReview(true);
						}
					}else{
						throw new IntuitReviewEngineException(HttpStatus.FORBIDDEN, "403.forbidden"); 
					}
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}
		return new ResponseEntity(reviewResource, HttpStatus.OK);
	}

}
