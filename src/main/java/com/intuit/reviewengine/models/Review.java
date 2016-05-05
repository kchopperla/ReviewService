package com.intuit.reviewengine.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This is a review entity which reflects the REVIEW table in the database. 
 * @author kchopperla
 *
 */
@Entity
@Table(name = "review", catalog = "intuit", uniqueConstraints = {@UniqueConstraint(columnNames = "ID") })
public class Review implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	
	@Column(name = "COMMENT", nullable = false)
	private String comment;
	
	@Column(name = "CREATED_DATE", nullable = false)
	private Date createdDate;
	
	@Column(name = "PROFILE_ID", nullable = false)
	private Integer profileId;
	
	@Column(name = "RATING", nullable = false)
	private Integer rating;

	@ManyToOne
	@JoinColumn(name ="PRODUCT_ID")
	private Product product;
	
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Review() {
		
	}
	
	public String toString() {
		return "Review [id=" + id + ", profileId=" + profileId
				+ ", rating=" + rating + ", createdDate="+createdDate+" ,comment="+comment+ "]";
	}
}