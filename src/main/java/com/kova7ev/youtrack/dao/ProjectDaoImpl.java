package com.kova7ev.youtrack.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kova7ev.youtrack.model.Project;

@Repository("projectDao")
public class ProjectDaoImpl extends AbstractDao<Integer, Project>implements ProjectDao{
	static final Logger logger = LoggerFactory.getLogger(ProjectDaoImpl.class);
	
	public Project findById(int id) {
		return getByKey(id);
	}
	
	public Project findByName(String name) {
		logger.info("name : {}", name);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name", name));
		Project project = (Project)crit.uniqueResult();
	
		return project;
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> findAll(){
		Criteria crit = createEntityCriteria();
		crit.addOrder(Order.asc("name"));
		return (List<Project>)crit.list();
	}
	
	public void save(Project project) {
		persist(project);
	}
	
	public void deleteByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("name", name));
		Project project = (Project)crit.uniqueResult();
		delete(project);
	}
}