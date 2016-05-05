package com.intuit.reviewengine.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class ProfileResource {
	
	private Integer id;
	private String userName;
	
	@JsonIgnore
	private String password;
	private String serviceSessionId;
	
	public String getServiceSessionId() {
		return serviceSessionId;
	}
	public void setServiceSessionId(String serviceSessionId) {
		this.serviceSessionId = serviceSessionId;
	}
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
}