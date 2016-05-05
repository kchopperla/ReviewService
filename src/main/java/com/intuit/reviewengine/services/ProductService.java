package com.intuit.reviewengine.services;

import java.util.List;

import com.intuit.reviewengine.resources.ProductResource;

public interface ProductService {
	public ProductResource getProductById(Integer id, Integer profileId) throws Exception;
	
	public List<ProductResource> getAllProducts(Integer profileId) throws Exception;
}