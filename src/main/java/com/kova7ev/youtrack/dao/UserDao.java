package com.kova7ev.youtrack.dao;

import java.util.List;

import com.kova7ev.youtrack.model.User;


public interface UserDao {

	User findById(int id);
	
	User findBySSO(String sso);
	
	User findByName(String name);
	
	void save(User user);
	
	void deleteBySSO(String sso);
	
	List<User> findAllUsers();

}

