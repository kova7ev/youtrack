package com.kova7ev.youtrack.dao;

import java.util.List;

import com.kova7ev.youtrack.model.Project;

public interface ProjectDao {

	List<Project> findAll();
	
	Project findById(int id);
	
	Project findByName(String name);
	
	void save(Project project);
	
	void deleteByName(String name);
}