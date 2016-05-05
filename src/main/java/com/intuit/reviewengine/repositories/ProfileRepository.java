package com.intuit.reviewengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.intuit.reviewengine.models.Profile;

@Repository
@RepositoryRestResource(exported=false)
public interface ProfileRepository extends PagingAndSortingRepository<Profile, Integer>{
	
	@Query("SELECT P FROM Profile P WHERE P.userName = :username AND P.password=:password")
	public Profile authenticate(@Param("username") String username, @Param("password") String password);
	
	@Query("SELECT P FROM Profile P WHERE P.serviceSessionId = :serviceSessionId")
	public Profile validSessionID(@Param("serviceSessionId") String serviceSessionId);
	
}