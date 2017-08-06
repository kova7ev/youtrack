package com.kova7ev.youtrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kova7ev.youtrack.dao.ProjectDao;
import com.kova7ev.youtrack.model.Project;

@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectDao dao;

	public Project findById(int id) {
		return dao.findById(id);
	}

	public Project findByName(String name) {
		return dao.findByName(name);
	}

	public List<Project> findAll() {
		return dao.findAll();
	}

	public boolean isProjectNameUnique(Integer id, String name) {
		Project project = findByName(name);
		return (project == null || ((id != null) && (project.getId() == id)));
	}

	public void saveProject(Project project) {
		dao.save(project);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate
	 * update explicitly. Just fetch the entity from db and update it with proper
	 * values within transaction. It will be updated in db once transaction ends.
	 */
	public void updateProject(Project project) {
		Project entity = dao.findById(project.getId());
		if (entity != null) {
			entity.setName(project.getName());
		}
	}

	public void deleteProjectByName(String name) {
		dao.deleteByName(name);
	}
}