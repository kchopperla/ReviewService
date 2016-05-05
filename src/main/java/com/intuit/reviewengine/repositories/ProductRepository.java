package com.intuit.reviewengine.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.intuit.reviewengine.models.Product;

/**
 * Product repository which is the main layer between service layer and the database.
 * @author kchopperla
 *
 */
@RepositoryRestResource(exported=false)
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{
	
}