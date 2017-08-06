package com.kova7ev.youtrack.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="ACTIVITY")
public class Activity implements Serializable{

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;	

	@NotEmpty
	@Column(name="COMMENT", nullable=false)
	private String comment;
	
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name="DATE", nullable=false)
	private Date date = new Date();
	
	@NotNull
	@Column(name="HOURS", nullable=false)
	private float hours;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_ACTIVITY_PROJECT", 
             joinColumns = { @JoinColumn(name = "PROJECT_ID") }, 
             inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") })
	private Project activityProject;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_ACTIVITY_USER", 
             joinColumns = { @JoinColumn(name = "USER_ID") }, 
             inverseJoinColumns = { @JoinColumn(name = "ACTIVITY_ID") })
	private User activityUser;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}
	
	public Project getActivityProject() {
		return activityProject;
	}

	public void setActivityProject(Project project) {
		this.activityProject = project;
	}
	
	public User getActivityUser() {
		return activityUser;
	}

	public void setActivityUser(User user) {
		this.activityUser = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Activity))
			return false;
		Activity other = (Activity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (date == null) {
			if (other.date!= null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", comment=" + comment + ", date=" + date + "]";
	}
}