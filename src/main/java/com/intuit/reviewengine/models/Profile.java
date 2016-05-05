package com.intuit.reviewengine.models;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Profile entity that interacts with the Profile database.
 * @author kchopperla
 *
 */
@Entity
@Table(name = "profile", catalog = "intuit", uniqueConstraints = {@UniqueConstraint(columnNames = "ID") })
public class Profile implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "USER_NAME", nullable = false)
	private String userName;
	
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	
	@Column(name = "SERVICE_SESSION_ID", nullable = false)
	private String serviceSessionId;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceSessionId() {
		return serviceSessionId;
	}

	public void setServiceSessionId(String serviceSessionId) {
		this.serviceSessionId = serviceSessionId;
	}

	public Profile() {
		
	}
	
	public String toString() {
		return "Profile [id=" + id + ", userName=" + userName + ", password=" + password + "]";
	}
}