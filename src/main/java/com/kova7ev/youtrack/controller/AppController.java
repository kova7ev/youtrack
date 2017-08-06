package com.kova7ev.youtrack.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.kova7ev.youtrack.model.Activity;
import com.kova7ev.youtrack.model.Project;
import com.kova7ev.youtrack.model.User;
import com.kova7ev.youtrack.model.UserProfile;
import com.kova7ev.youtrack.model.UserProfileType;
import com.kova7ev.youtrack.service.ActivityService;
import com.kova7ev.youtrack.service.ProjectService;
import com.kova7ev.youtrack.service.UserProfileService;
import com.kova7ev.youtrack.service.UserService;

@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	UserService userService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	ProjectService projectService;

	@Autowired
	ActivityService activityService;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = {"/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing
		 * custom @Unique annotation and applying it on field [sso] of Model class
		 * [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill
		 * custom errors outside the validation framework as well while still using
		 * internationalized messages.
		 * 
		 */
		if (!userService.isUserSSOUnique(user.getId(), user.getSsoId())) {
			FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId",
					new String[] { user.getSsoId() }, Locale.getDefault()));
			result.addError(ssoError);
			return "registration";
		}

		userService.saveUser(user);

		model.addAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		// return "success";
		return "registrationsuccess";
	}

	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result, ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * //Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which
		 * is a unique key to a User. if(!userService.isUserSSOUnique(user.getId(),
		 * user.getSsoId())){ FieldError ssoError =new
		 * FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new
		 * String[]{user.getSsoId()}, Locale.getDefault())); result.addError(ssoError);
		 * return "registration"; }
		 */

		userService.updateUser(user);

		model.addAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "registrationsuccess";
	}

	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}

	/**
	 * This method will list all activities.
	 */
	@RequestMapping(value = { "/activities" }, method = RequestMethod.GET)
	public String listActivities(ModelMap model) {

		User user = userService.findBySSO(getPrincipal());
		
		boolean isAdmin = user.getUserProfiles().stream().anyMatch(p -> (p.getType().equals("ADMIN") || p.getType().equals("DBA")));
		
		List<Activity> activities =  isAdmin ? activityService.findAll() : activityService.findByUser(user.getId());
		model.addAttribute("activities", activities);
		model.addAttribute("loggedinuser", getPrincipal());
		return "activities";
	}

	/**
	 * This method will provide the medium to add a new activity.
	 */
	@RequestMapping(value = { "/newactivity" }, method = RequestMethod.GET)
	public String newActivity(ModelMap model) {
		User user = userService.findBySSO(getPrincipal());
		Activity activity = new Activity();
		activity.setActivityUser(user);

		model.addAttribute("user", user);
		model.addAttribute("userprojects", user.getUserProjects());
		model.addAttribute("activity", activity);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "newactivity";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving activity in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newactivity" }, method = RequestMethod.POST)
	public String saveActivity(@Valid Activity activity, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			return "newactivity";
		}

		activityService.saveActivity(activity);

		model.addAttribute("success", "Activity: id=" + activity.getId() + ", comment= " + activity.getComment()
				+ ", hours= " + activity.getHours() + " added successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		// return "success";
		return "newactivitysuccess";
	}

	/**
	 * This method will provide the medium to update an existing activity.
	 */
	@RequestMapping(value = { "/edit-activity-{id}" }, method = RequestMethod.GET)
	public String editActivity(@PathVariable int id, ModelMap model) {
		Activity activity = activityService.findById(id);
		model.addAttribute("activity", activity);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "newactivity";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating activity in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-activity-{id}" }, method = RequestMethod.POST)
	public String updateActivity(@Valid Activity activity, BindingResult result, ModelMap model, @PathVariable int id) {

		if (result.hasErrors()) {
			return "newactivity";
		}

		activityService.updateActivity(activity);

		model.addAttribute("success", "Activity: id=" + activity.getId() + ", comment= " + activity.getComment()
				+ ", hours= " + activity.getHours() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "newactivitysuccess";
	}

	/**
	 * This method will delete an activity by it's ID value.
	 */
	@RequestMapping(value = { "/delete-activity-{id}" }, method = RequestMethod.GET)
	public String deleteActivity(@PathVariable int id) {
		activityService.deleteActivityById(id);
		return "redirect:/activities";
	}

	/**
	 * This method will list all users projects.
	 */
	@RequestMapping(value = { "/", "/myprojects" }, method = RequestMethod.GET)
	public String listUserProjects(ModelMap model) {

		Set<Project> projects = userService.findBySSO(getPrincipal()).getUserProjects();
		model.addAttribute("projects", projects);
		model.addAttribute("loggedinuser", getPrincipal());
		return "projects";
	}
	
	/**
	 * This method will list all projects.
	 */
	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String listProjects(ModelMap model) {

		List<Project> projects = projectService.findAll();
		model.addAttribute("projects", projects);
		model.addAttribute("loggedinuser", getPrincipal());
		return "projects";
	}

	/**
	 * This method will provide the medium to add a new project.
	 */
	@RequestMapping(value = { "/newproject" }, method = RequestMethod.GET)
	public String newProject(ModelMap model) {
		Project project = new Project();
		model.addAttribute("project", project);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "newproject";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving project in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newproject" }, method = RequestMethod.POST)
	public String saveProject(@Valid Project project, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			return "newproject";
		}

		/*
		 * Preferred way to achieve uniqueness of field [name] should be implementing
		 * custom @Unique annotation and applying it on field [name] of Model class
		 * [Project].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill
		 * custom errors outside the validation framework as well while still using
		 * internationalized messages.
		 * 
		 */
		if (!projectService.isProjectNameUnique(project.getId(), project.getName())) {
			FieldError nameError = new FieldError("project", "name", messageSource.getMessage("non.unique.name",
					new String[] { project.getName() }, Locale.getDefault()));
			result.addError(nameError);
			return "newproject";
		}

		projectService.saveProject(project);

		model.addAttribute("success", "Project " + project.getName() + " added successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		// return "success";
		return "newprojectsuccess";
	}

	/**
	 * This method will provide the medium to update an existing project.
	 */
	@RequestMapping(value = { "/edit-project-{name}" }, method = RequestMethod.GET)
	public String editProject(@PathVariable String name, ModelMap model) {
		Project project = projectService.findByName(name);
		model.addAttribute("project", project);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "newproject";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating project in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-project-{name}" }, method = RequestMethod.POST)
	public String updateProject(@Valid Project project, BindingResult result, ModelMap model,
			@PathVariable String name) {

		if (result.hasErrors()) {
			return "newproject";
		}

		/*
		 * //Uncomment below 'if block' if you WANT TO ALLOW UPDATING NAME in UI which
		 * is a unique key to a Project.
		 * if(!projectService.isProjectNameUnique(project.getId(), project.getName())){
		 * FieldError nameError = new FieldError("project", "name",
		 * messageSource.getMessage("non.unique.name", new String[]{ project.getName()
		 * }, Locale.getDefault())); result.addError(nameError); return "newproject"; }
		 */

		projectService.updateProject(project);

		model.addAttribute("success", "Project " + project.getName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "newprojectsuccess";
	}

	/**
	 * This method will delete an project by it's NAME value.
	 */
	@RequestMapping(value = { "/delete-project-{name}" }, method = RequestMethod.GET)
	public String deleteProject(@PathVariable String name) {
		projectService.deleteProjectByName(name);
		return "redirect:/projects";
	}

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}

	/**
	 * This method will provide Projects list to views
	 */
	@ModelAttribute("projects")
	public List<Project> initializeProjects() {
		return projectService.findAll();
	}

	/**
	 * This method will provide Users list to views
	 */
	@ModelAttribute("users")
	public List<User> initializeUsers() {
		return userService.findAllUsers();
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests. If users is already logged-in and
	 * tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		} else {
			return "redirect:/myprojects";
		}
	}

	/**
	 * This method handles logout requests. Toggle the handlers if you are
	 * RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			// new SecurityContextLogoutHandler().logout(request, response, auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	/**
	 * This method returns true if users is already authenticated [logged-in], else
	 * false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}