package com.kova7ev.youtrack.service;

import java.util.List;

import com.kova7ev.youtrack.model.Project;

public interface ProjectService {

	Project findById(int id);
	
	Project findByName(String name);
	
	List<Project> findAll();	
	
	boolean isProjectNameUnique(Integer id, String name);
	
	void saveProject(Project project);
	
	void updateProject(Project project);
	
	void deleteProjectByName(String name);
}