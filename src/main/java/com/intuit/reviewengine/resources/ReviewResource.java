package com.intuit.reviewengine.resources;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intuit.reviewengine.models.Product;

public class ReviewResource {
	
	private Integer id;
	private String comment;
	private Date createdDate;
	
	private String profileName;
	private Integer rating;
	@JsonIgnore
	private Integer profileId;
	
	private boolean ownsReview=false; 
	
	public String toString(){
		StringBuffer sb = new StringBuffer("[");
		sb.append("id:").append(id).append(" ");
		sb.append("comment:").append(comment).append(" ");
		sb.append("createdDate:").append(createdDate).append(" ");
		sb.append("profileName:").append(profileName).append(" ");
		sb.append("rating:").append(rating).append(" ");
		sb.append("]");
		return sb.toString();
	}
	
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	public boolean isOwnsReview() {
		return ownsReview;
	}
	public void setOwnsReview(boolean ownsReview) {
		this.ownsReview = ownsReview;
	}
	private Integer productId;
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
}