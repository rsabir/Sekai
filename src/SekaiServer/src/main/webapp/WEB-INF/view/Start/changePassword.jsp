<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Server Configuration</title>
<sec:csrfMetaTags />
<style type="text/css">
#errorMessage, #success {
	display: none;
	margin-top: 2%;
	width: 90%;
	margin-left: auto;
	margin-right: auto;
	color: red;
	text-align: center;
}

#success {
	color: green;
}
</style>
</head>
<body>

	<div class="row">
		<div id="breadcrumb" class="col-md-12">
			<ol class="breadcrumb">
				<li><a href="#">Change Password</a></li>
			</ol>
		</div>
	</div>
		<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="box">
				<div class="box-content">
				<h4 class="page-header">Change Default Password</h4>
				<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-4 control-label">Username</label>
							<div class="col-sm-4">
								<input type="text" id="username" value="${username}" class="form-control" placeholder="username" data-toggle="tooltip" data-placement="username"  title="username" disabled/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">Password</label>
							<div class="col-sm-4">
								<input  type="password" id="password" class="form-control" placeholder="Password" data-toggle="tooltip" data-placement="username"  title="username" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">Confirm Password</label>
							<div class="col-sm-4">
								<input type="password" id="confirmPassword" class="form-control" placeholder="Confirm Password" data-toggle="tooltip" data-placement="Ip of the server" title="password">
							</div>
						</div>
						<div class="text-center center-block" style="margin-top:1%;"><button type="button" id="submit" class="btn btn-primary center-block">Next</button></div>
						<div id="success">Well saved</div>
						<div id="errorMessage" class="text-center center-block" style="display:none"></div>
				</form>						
			</div>
		</div>
	</div>
	</div>
	<script type="text/javascript">
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
		});
	</script>
	<script src="<c:url value="/resources/js/Start/changePassword.js" />"></script>
</body>
</html>