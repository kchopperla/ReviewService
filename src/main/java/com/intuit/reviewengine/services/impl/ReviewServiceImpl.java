package com.intuit.reviewengine.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intuit.reviewengine.Utils.ServiceUtils;
import com.intuit.reviewengine.exceptions.IntuitReviewEngineException;
import com.intuit.reviewengine.models.Review;
import com.intuit.reviewengine.repositories.ReviewRepository;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	ReviewRepository reviewRepository;
	
	public void setReviewRepository(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public ReviewResource save(ReviewResource reviewResource) throws Exception {
		if(reviewResource==null){
			throw new IllegalArgumentException("Resource cannot be null");
		}
		
		Review reviewEntity = new Review();
		if(reviewResource.getId()==null){
			
			ServiceUtils.copyReviewResourceToEntity(reviewResource, reviewEntity, false);
		}else {
			reviewEntity = reviewRepository.findOne(reviewResource.getId());
			
			if(reviewResource.getProductId()!=reviewEntity.getProduct().getId()){
				throw new IntuitReviewEngineException(HttpStatus.BAD_REQUEST,"400.bad.request");
			}
			ServiceUtils.copyReviewResourceToEntity(reviewResource, reviewEntity, true);
		}
		
		reviewEntity = reviewRepository.save(reviewEntity);
		ServiceUtils.copyReviewEntityToResource(reviewEntity, reviewResource);
		return reviewResource;
	}

	@Transactional
	public void delete(Integer reviewResourceId) throws Exception {
		if(reviewResourceId==null){
			throw new IllegalArgumentException("Resource id cannot be null");
		}
		Review review = reviewRepository.findOne(reviewResourceId);
		reviewRepository.delete(review);
	}
	
	public ReviewResource findReviewById(Integer id) throws Exception{
		if(id==null){
			throw new IllegalArgumentException("Resource id cannot be null");
		}
		Review review = reviewRepository.findOne(id);
		ReviewResource reviewResource = new ReviewResource();
		ServiceUtils.copyReviewEntityToResource(review, reviewResource);
		return reviewResource;
	}
}