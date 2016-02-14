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
#error, #success {
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
				<li><a href="index.html">index</a></li>
				<li><a href="#">Settings</a></li>
				<li><a href="#">Database</a></li>
			</ol>
		</div>
	</div>
		<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="box">
			<div class="box-header">
				<div class="box-name">
					<i class="fa fa-cog"></i>
					<span>Database Configuration</span>
				</div>
				<div class="no-move"></div>
			</div>
				<div class="box-content">
				<h4 class="page-header">Mysql Configuration</h4>
				<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-2 control-label">Username</label>
							<div class="col-sm-4">
								<input type="text" id="username" value="${username}" class="form-control" placeholder="username" data-toggle="tooltip" data-placement="username"  title="username">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Password</label>
							<div class="col-sm-4">
								<input type="password" id="password" class="form-control" placeholder="Password" data-toggle="tooltip" data-placement="Ip of the server" title="password">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Port</label>
							<div class="col-sm-4">
								<input type="text" id="port" value="${port} "class="form-control" placeholder="Port of database" data-toggle="tooltip" data-placement="Port of the database" title="port">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">Database</label>
							<div class="col-sm-4">
								<input type="text" id="database" value="${database}" class="form-control" placeholder="The name of database" data-toggle="tooltip" data-placement="The name of database" title="the name of database">
							</div>
						</div>
						<div class="text-center center-block" style="margin-top:1%;"><button type="button" class="btn btn-primary center-block">SAVE</button></div>
						<div id="success">Well saved</div>
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
	<script src="<c:url value="/resources/js/databaseSettings.js" />"></script>
</body>
</html>