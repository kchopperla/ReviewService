package com.intuit.reviewengine.services;

import com.intuit.reviewengine.models.Profile;
import com.intuit.reviewengine.resources.ProfileResource;

public interface ProfileService {
	
	public ProfileResource save(ProfileResource profileResource) throws Exception;
	
	public ProfileResource getProfileById(Integer id) throws Exception;
	
	public ProfileResource authenticate(String username, String password) throws Exception;
	
	public boolean validServiceSession(String serviceSessionId) throws Exception;
	
	public ProfileResource getProfileBySessionId(String serviceSessionId) throws Exception;
}