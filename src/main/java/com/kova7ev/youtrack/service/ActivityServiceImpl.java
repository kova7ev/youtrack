package com.kova7ev.youtrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kova7ev.youtrack.dao.ActivityDao;
import com.kova7ev.youtrack.model.Activity;

@Service("activityService")
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	ActivityDao dao;

	public Activity findById(int id) {
		return dao.findById(id);
	}

	public List<Activity> findAll() {
		return dao.findAll();
	}

	public void saveActivity(Activity activity) {
		dao.save(activity);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate
	 * update explicitly. Just fetch the entity from db and update it with proper
	 * values within transaction. It will be updated in db once transaction ends.
	 */
	public void updateActivity(Activity activity) {
		Activity entity = dao.findById(activity.getId());
		if (entity != null) {
			entity.setComment(activity.getComment());
			entity.setDate(activity.getDate());
			entity.setHours(activity.getHours());
		}
	}

	public void deleteActivityById(int id) {
		dao.deleteById(id);
	}
}