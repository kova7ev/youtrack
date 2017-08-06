package com.kova7ev.youtrack.dao;

import java.util.List;

import com.kova7ev.youtrack.model.Activity;

public interface ActivityDao {

	List<Activity> findAll();
	
	List<Activity> findByUser(Integer id);
	
	Activity findById(int id);
	
	void save(Activity project);
	
	void deleteById(int id);
}