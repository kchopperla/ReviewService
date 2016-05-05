package com.intuit.reviewengine.resources;

import java.util.HashSet;
import java.util.Set;

public class ProductResource {
	
	private Integer id;
	private String displayName;
	private String description;
	private Set<ReviewResource> reviews = new HashSet<ReviewResource>();
	private double totalRating;
	
	public Set<ReviewResource> getReviews() {
		return reviews;
	}
	public double getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(double totalRating) {
		this.totalRating = totalRating;
	}
	public void setReviews(Set<ReviewResource> reviews) {
		this.reviews = reviews;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}