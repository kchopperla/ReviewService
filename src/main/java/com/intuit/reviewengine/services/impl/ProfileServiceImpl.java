package com.intuit.reviewengine.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intuit.reviewengine.Utils.ServiceUtils;
import com.intuit.reviewengine.models.Profile;
import com.intuit.reviewengine.repositories.ProfileRepository;
import com.intuit.reviewengine.resources.ProfileResource;
import com.intuit.reviewengine.services.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	ProfileRepository profileRepository;
	
	@Transactional
	public ProfileResource save(ProfileResource profileResource) throws Exception {
		if(profileResource==null){
			throw new IllegalArgumentException("Profile Resource cannot be null");
		}
		
		Profile profileEntity = null;
		if(profileResource.getId()==null){
			profileEntity = new Profile();
			
		}else {
			profileEntity = profileRepository.findOne(profileResource.getId());
		}
		
		ServiceUtils.copyProfileResourceToEntity(profileResource, profileEntity);
		profileEntity = profileRepository.save(profileEntity);
		ServiceUtils.copyProfileEntityToResource(profileEntity, profileResource);
		return profileResource;
	}
	
	@Transactional
	public ProfileResource authenticate(String username, String password) throws Exception {
		if(username==null || password==null){
			throw new IllegalArgumentException("credentails cannot be null");
		}
		
		Profile profileEntity = profileRepository.authenticate(username,password);
		ProfileResource profileResource = new ProfileResource();
		ServiceUtils.copyProfileEntityToResource(profileEntity, profileResource);
		return profileResource;
	}
	
	@Transactional
	public boolean validServiceSession(String serviceSessionId) throws Exception {
		if(serviceSessionId==null){
			throw new IllegalArgumentException("credentails cannot be null");
		}
		
		Profile profileEntity = profileRepository.validSessionID(serviceSessionId);
		boolean validServiceSession = false;
		if(profileEntity!=null)
		{
			validServiceSession = true;
		}
		return validServiceSession;
	}
	
	public ProfileResource getProfileBySessionId(String serviceSessionId) throws Exception {
		if(serviceSessionId==null){
			throw new IllegalArgumentException("credentails cannot be null");
		}
		
		Profile profileEntity = profileRepository.validSessionID(serviceSessionId);
		ProfileResource profileResource = new ProfileResource();
		ServiceUtils.copyProfileEntityToResource(profileEntity, profileResource);
		return profileResource;
	}

	@Transactional
	public ProfileResource getProfileById(Integer id) throws Exception {
		if(id==null){
			throw new IllegalArgumentException("Resource id cannot be null");
		}
		Profile profile = profileRepository.findOne(id);
		ProfileResource profileResource = new ProfileResource();
		ServiceUtils.copyProfileEntityToResource(profile,profileResource);
		return profileResource;
		
	}
}