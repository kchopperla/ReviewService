package com.intuit.reviewengine.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.resources.ProductResource;
import com.intuit.reviewengine.services.ProductService;

/**
 * This rest controller deals with product management of the review engine.
 * It gets all the products with their reviews as part of the rest call.
 * Every product which has reviews can be seen by anonymous/logged in user.
 * 
 * But a logged in user gets additional information from the service call. This
 * is possible if they have service session id token either in their request
 * header or in their cookie.
 * 
 * When the token is present, this REST API provides the review with additional 
 * information such as if  the user owns any of the reviews returned by the response.
 * 
 * @author kchopperla
 *
 */

@RestController
@RequestMapping("/v1/products")
public class ProductManagementController {

	private static final Logger logger = LoggerFactory.getLogger(ProductManagementController.class);

	@Autowired
	ProductService productService;
	
	@Autowired
	ControllerUtils controllerUtils;
	
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public void setControllerUtils(ControllerUtils controllerUtils) {
		this.controllerUtils = controllerUtils;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<ProductResource> getProduct(HttpServletRequest request,HttpServletResponse response, @PathVariable Integer id) 
			throws Exception{
		ProductResource productResource = null;
		try {
			Integer profileId=controllerUtils.getProfileId(request);
			productResource = productService.getProductById(id, profileId);
			
			if(productResource==null || (productResource.getId()==null)){
				logger.error("PRODUCT_NOT_FOUND:"+id);
				throw new IntuitReviewEngineException(HttpStatus.NOT_FOUND, "404.not.found.product"); 
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}

		return new ResponseEntity(productResource, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public HttpEntity<List<ProductResource>> getAllProducts(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		List<ProductResource> productResources = null;
		try {
			Integer profileId=controllerUtils.getProfileId(request);
			productResources = productService.getAllProducts(profileId);
			if(productResources==null){
				throw new IntuitReviewEngineException(HttpStatus.NOT_FOUND, "404.not.found.product"); 
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			
			if(e instanceof IntuitReviewEngineException){
				throw e;
			}else {
				throw new IntuitReviewEngineException(HttpStatus.INTERNAL_SERVER_ERROR, "500.internal.server.error");
			}
		}

		return new ResponseEntity(productResources, HttpStatus.OK);
	}
	
}
