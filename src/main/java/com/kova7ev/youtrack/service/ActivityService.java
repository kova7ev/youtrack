package com.kova7ev.youtrack.service;

import java.util.List;

import com.kova7ev.youtrack.model.Activity;

public interface ActivityService {

	Activity findById(int id);
	
	List<Activity> findAll();	
	
	void saveActivity(Activity activity);
	
	void updateActivity(Activity activity);
	
	void deleteActivityById(int id);
}