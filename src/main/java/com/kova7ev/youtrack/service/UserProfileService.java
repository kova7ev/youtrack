package com.kova7ev.youtrack.service;

import java.util.List;

import com.kova7ev.youtrack.model.UserProfile;


public interface UserProfileService {

	UserProfile findById(int id);

	UserProfile findByType(String type);
	
	List<UserProfile> findAll();
	
}
