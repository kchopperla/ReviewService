package com.intuit.reviewengine.Utils;

import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.intuit.reviewengine.models.Product;
import com.intuit.reviewengine.models.Profile;
import com.intuit.reviewengine.models.Review;
import com.intuit.reviewengine.resources.ProductResource;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.resources.ReviewResource;

public class ServiceUtils {
	
	
	public static void copyReviewResourceToEntity(ReviewResource reviewResource, Review review, boolean isEditMode)
	throws Exception{
		if(reviewResource!=null && review!=null){
			review.setComment(reviewResource.getComment());
			if(isEditMode){
				
			}else {
				java.sql.Date sqlDate = new Date(reviewResource.getCreatedDate().getTime()) ;
				review.setCreatedDate(sqlDate);
				Product product = new Product();
				product.setId(reviewResource.getProductId());
				review.setProduct(product);
				review.setProfileId(reviewResource.getProfileId());
			}
			review.setRating(reviewResource.getRating());
		}
	}
	
	public static void copyReviewEntityToResource(Review review, ReviewResource reviewResource){
		if(reviewResource!=null && review!=null){
			reviewResource.setId(review.getId());
			reviewResource.setComment(review.getComment());
			reviewResource.setCreatedDate(review.getCreatedDate());
			reviewResource.setProfileId(review.getProfileId());
			reviewResource.setRating(review.getRating());
			reviewResource.setProductId(review.getProduct().getId());
		}
	}
	
	public static void copyProfileResourceToEntity(ProfileResource profileResource, Profile profile){
		if(profileResource!=null && profile!=null){
			profile.setId(profileResource.getId());
			profile.setUserName(profileResource.getUserName());
			profile.setPassword(profileResource.getPassword());
			profile.setServiceSessionId(profile.getServiceSessionId());
		}
	}
	
	public static void copyProfileEntityToResource(Profile profile, ProfileResource profileResource){
		if(profileResource!=null && profile!=null){
			profileResource.setId(profile.getId());
			profileResource.setUserName(profile.getUserName());
			profileResource.setPassword(profile.getPassword());
			profileResource.setServiceSessionId(profile.getServiceSessionId());
		}
	}
	
	public static void copyProductEntityToResource(Product product, ProductResource productResource, Integer profileId){
		if(product!=null){
			Double totalRating = 0.0;
			int numOfReviews=0;
			productResource.setId(product.getId());
			productResource.setDisplayName(product.getDisplayName());
			productResource.setDescription(product.getDescription());
			Set<Review> reviewEntities = product.getReviews();
			numOfReviews=reviewEntities.size();
			Set<ReviewResource> reviewResources = new HashSet<ReviewResource>();
			if(reviewEntities!=null){
				Iterator<Review> reviewEntitiesIterator = reviewEntities.iterator();
				while(reviewEntitiesIterator.hasNext()){
					ReviewResource reviewResource = new ReviewResource();
					Review reviewEntity =  reviewEntitiesIterator.next();
					copyReviewEntityToResource(reviewEntity,reviewResource);
					if(profileId!=null && reviewResource.getProfileId().equals(profileId)){
						reviewResource.setOwnsReview(true);
					}
					reviewResources.add(reviewResource);
					totalRating+=reviewEntity.getRating();
				}
			}
			productResource.setReviews(reviewResources);
			if(numOfReviews==0){
				productResource.setTotalRating(totalRating);
			}else {
				productResource.setTotalRating(totalRating/numOfReviews);
			}
		}
	}
}