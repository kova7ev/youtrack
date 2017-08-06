<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add New Activity Form</title>
<link href="<c:url value='/static/css/bootstrap.css' />"
	rel="stylesheet"></link>
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
	<div class="generic-container">
		<%@include file="authheader.jsp"%>

		<div class="well lead">Add New Activity Form</div>
		<form:form method="POST" modelAttribute="activity"
			class="form-horizontal">
			<form:input type="hidden" path="id" id="id" />

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable" for="firstName">Comment</label>
					<div class="col-md-7">
						<form:input type="text" path="comment" id="comment"
							class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="comment" class="help-inline" />
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable" for="hours">Hours</label>
					<div class="col-md-7">
						<form:input type="text" path="hours" id="hours"
							class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="hours" class="help-inline" />
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable" for="date">Date</label>
					<div class="col-md-7">
						<form:input id="datepicker" name="date" itemLabel="date"
							path="date" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable" for="activityProject">Projects</label>
					<div class="col-md-7">
						<form:select path="activityProject" items="${projects}"
							multiple="true" itemValue="id" itemLabel="name"
							class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="activityProject" class="help-inline" />
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-3 control-lable" for="activityUser">Users</label>
					<div class="col-md-7">
						<form:select path="activityUser" items="${users}"
							multiple="true" itemValue="id" itemLabel="ssoId"
							class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="activityUser" class="help-inline" />
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-actions floatRight">
					<c:choose>
						<c:when test="${edit}">
							<input type="submit" value="Update"
								class="btn btn-primary btn-sm" /> or <a
								href="<c:url value='/activities' />">Cancel</a>
						</c:when>
						<c:otherwise>
							<input type="submit" value="Add" class="btn btn-primary btn-sm" /> or <a
								href="<c:url value='/activities' />">Cancel</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>