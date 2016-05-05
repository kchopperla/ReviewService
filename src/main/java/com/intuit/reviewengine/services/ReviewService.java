package com.intuit.reviewengine.services;

import com.intuit.reviewengine.resources.ReviewResource;

public interface ReviewService {
	
	public ReviewResource save(ReviewResource reviewResource) throws Exception;
	
	public void delete(Integer reviewResourceId) throws Exception;
	
	public ReviewResource findReviewById(Integer id) throws Exception;
}