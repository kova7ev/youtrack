package com.kova7ev.youtrack.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kova7ev.youtrack.model.User;
import com.kova7ev.youtrack.service.UserService;

/**
 * A converter class used in views to map id's to actual userProfile objects.
 */
@Component
public class UserToActivityUserConverter implements Converter<Object, User>{

	static final Logger logger = LoggerFactory.getLogger(UserToActivityUserConverter.class);
	
	@Autowired
	UserService userService;

	/**
	 * Gets Project by Id
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	public User convert(Object element) {
		Integer id = Integer.parseInt((String)element);
		User user = userService.findById(id);
		logger.info("User : {}", user);
		return user;
	}
	
}