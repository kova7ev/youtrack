package com.kova7ev.youtrack.dao;

import java.util.List;

import com.kova7ev.youtrack.model.UserProfile;


public interface UserProfileDao {

	List<UserProfile> findAll();
	
	UserProfile findByType(String type);
	
	UserProfile findById(int id);
}