package com.intuit.reviewengine.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.intuit.reviewengine.models.Review;

@RepositoryRestResource(exported=false)
public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer>{
	
}