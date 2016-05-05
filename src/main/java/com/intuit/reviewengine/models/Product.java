package com.intuit.reviewengine.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Product Entity that interacts with Product table in the database.
 * @author kchopperla
 *
 */
@Entity
@Table(name = "product", catalog = "intuit", uniqueConstraints = {@UniqueConstraint(columnNames = "ID") })
public class Product {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "DISPLAY_NAME", nullable = false)
	private String displayName;
	
	@Column(name = "DESCRIPTION",  nullable = false)
	private String description;
	
	@Fetch(value = FetchMode.JOIN)
	@OneToMany(mappedBy = "product")
	private Set<Review> reviews;
	
	
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

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Product() {
		
	}
	
	public String toString() {
		return "Product [id=" + id + ", displayName=" + displayName
				+ ", description=" + description + "]";
	}
}