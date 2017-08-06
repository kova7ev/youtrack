package com.kova7ev.youtrack.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kova7ev.youtrack.model.Project;
import com.kova7ev.youtrack.model.User;
import com.kova7ev.youtrack.service.ProjectService;

/**
 * A converter class used in views to map id's to actual userProject objects.
 */
@Component
public class ProjectToActivityProjectConverter implements Converter<Object, Project>{

	static final Logger logger = LoggerFactory.getLogger(ProjectToActivityProjectConverter.class);
	
	@Autowired
	ProjectService projectService;

	/**
	 * Gets Project by Id
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public Project convert(Object element) {
		Integer id = Integer.parseInt((String)element);
		Project project = projectService.findById(id);
		logger.info("Profile : {}", project);
		return project;
	}
}