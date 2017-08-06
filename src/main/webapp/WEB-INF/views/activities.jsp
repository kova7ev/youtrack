<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Activities List</title>
<link href="<c:url value='/static/css/bootstrap.css' />"
	rel="stylesheet"></link>
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
	<div class="generic-container">
		<%@include file="authheader.jsp"%>
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<span class="lead">List of Activities </span>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Project</th>
						<th>User</th>
						<th>Comment</th>
						<th>Date</th>
						<th>Hours</th>
						<sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
							<th width="100"></th>
						</sec:authorize>
						<sec:authorize access="hasRole('ADMIN')">
							<th width="100"></th>
						</sec:authorize>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${activities}" var="activity">
						<tr>
							<td>${activity.activityProject.name}</td>
							<td>${activity.activityUser.ssoId}</td>
							<td>${activity.comment}</td>
							<td><fmt:formatDate value="${activity.date}"
									pattern="yyyy-MM-dd" /></td>
							<td>${activity.hours}</td>
							<td><a
								href="<c:url value='/edit-activity-${activity.id}' />"
								class="btn btn-success custom-width">edit</a></td>
							<td><a
								href="<c:url value='/delete-activity-${activity.id}' />"
								class="btn btn-danger custom-width">delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="well">
			<a href="<c:url value='/newactivity' />">Add New Activity</a>
		</div>
	</div>
</body>
</html>