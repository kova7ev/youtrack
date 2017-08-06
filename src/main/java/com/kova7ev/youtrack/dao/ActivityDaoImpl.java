package com.kova7ev.youtrack.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kova7ev.youtrack.model.Activity;

@Repository("activityDao")
public class ActivityDaoImpl extends AbstractDao<Integer, Activity> implements ActivityDao {

	static final Logger logger = LoggerFactory.getLogger(ActivityDaoImpl.class);

	public Activity findById(int id) {
		Activity activity = getByKey(id);

		if (activity != null) {
			Hibernate.initialize(activity.getActivityUser());
			Hibernate.initialize(activity.getActivityProject());
		}

		return activity;
	}

	@SuppressWarnings("unchecked")
	public List<Activity> findAll() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("date"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);// To avoid duplicates.
		List<Activity> activities = (List<Activity>) criteria.list();

		for (Activity activity : activities) {
			Hibernate.initialize(activity.getActivityUser());
			Hibernate.initialize(activity.getActivityProject());
		}

		return activities;
	}

	public void save(Activity activity) {
		persist(activity);
	}

	public void deleteById(int id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", id));
		Activity activity = (Activity) crit.uniqueResult();
		delete(activity);
	}
}