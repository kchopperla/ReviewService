package com.intuit.reviewengine.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.reviewengine.Utils.ServiceUtils;
import com.intuit.reviewengine.models.Product;
import com.intuit.reviewengine.repositories.ProductRepository;
import com.intuit.reviewengine.resources.ProductResource;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.ProductService;
import com.intuit.reviewengine.services.ProfileService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProfileService profileService;
	
	@Override
	public ProductResource getProductById(Integer id, Integer profileId) throws Exception {
		if(id==null){
			throw new IllegalArgumentException("Resource cannot be null");
		}
		
		Product productEntity  = productRepository.findOne(id);
		
		ProductResource productResource = new ProductResource();
		ServiceUtils.copyProductEntityToResource(productEntity, productResource, profileId);
		setProfileNameOnReviews(productResource.getReviews());
		return productResource;
	}
	
	@Override
	public List<ProductResource> getAllProducts(Integer profileId) throws Exception{
		List<ProductResource> productResources = new ArrayList<ProductResource>();
		List<Product> productEntities = new ArrayList<Product>();
		
		Iterable<Product> productsIterable = productRepository.findAll();
		if(productsIterable!=null){
			
			Iterator<Product> productsIterator = productsIterable.iterator();
			while(productsIterator.hasNext()){
				productEntities.add(productsIterator.next());
			}
		}
		
		if(productEntities!=null){
			for(int i=0;i<=productEntities.size()-1;i++){
				ProductResource productResource = new ProductResource();
				ServiceUtils.copyProductEntityToResource(productEntities.get(i), productResource, profileId);
				setProfileNameOnReviews(productResource.getReviews());
				productResources.add(productResource);
			}
		}
		
		return productResources;
	}
	
	private void setProfileNameOnReviews(Set<ReviewResource> reviewResources) throws Exception{
		if(reviewResources!=null){
			Iterator<ReviewResource> reviewResourcesIterator = reviewResources.iterator();
			while(reviewResourcesIterator.hasNext()){
				ReviewResource reviewResource = reviewResourcesIterator.next();
				ProfileResource profileResource = profileService.getProfileById(reviewResource.getProfileId());
				if(profileResource!=null){
					reviewResource.setProfileName(profileResource.getUserName());
				}
			}
		}
	}
}