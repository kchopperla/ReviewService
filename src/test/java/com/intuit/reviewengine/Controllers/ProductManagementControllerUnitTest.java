package com.intuit.reviewengine.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.intuit.reviewengine.Utils.ControllerUtils;
import com.intuit.reviewengine.controllers.ProductManagementController;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.resources.ProductResource;
import com.intuit.reviewengine.services.ProductService;

public class ProductManagementControllerUnitTest {

	@Mocked ProductService productService;
	
	@Mocked ControllerUtils controllerUtils;
	
	ProductManagementController productManagementController;
	
	@Before
	public void setUp() {
		productManagementController = new ProductManagementController();
		productManagementController.setProductService(productService);
		productManagementController.setControllerUtils(controllerUtils);
	}
	
	/**
	 * This test case gets the product by id.
	 * @param productId
	 * @param productResource
	 * @param profileId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test
	public void testGetProduct_success(@Mocked final Integer productId, @Mocked final ProductResource productResource,@Mocked final Integer profileId,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.getProfileId(request);;result=profileId;
			productService.getProductById(productId, profileId);result=productResource;
		}};

		ResponseEntity<ProductResource> result = (ResponseEntity<ProductResource>) productManagementController.getProduct(request, response, productId);

		Assert.assertTrue(result.getStatusCode().equals(HttpStatus.OK));
	}

	/**
	 * This test case when the product is not in the given catalog.
	 * @param productId
	 * @param productResource
	 * @param profileId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Test(expected=IntuitReviewEngineException.class)
	public void testGetProduct_error(@Mocked final Integer productId, @Mocked final ProductResource productResource,@Mocked final Integer profileId,
			@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response) throws Exception {

		new NonStrictExpectations() {{
			controllerUtils.getProfileId(request);;result=profileId;
			productService.getProductById(productId, profileId);result=null;
		}};
		try {
		ResponseEntity<ProductResource> result = (ResponseEntity<ProductResource>) productManagementController.getProduct(request, response, productId);
		}catch(Exception e){
			Assert.assertTrue(e.getMessage().equals("404.not.found.product"));
			throw e;
		}
	}
}
