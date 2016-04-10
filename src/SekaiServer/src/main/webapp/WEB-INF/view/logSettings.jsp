<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<meta charset="UTF-8">
<title>Log Settings</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=yes">
	
<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Open+Sans'>

<style>

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
<sec:csrfMetaTags />
</head>
<body>

<div class="row">
	<div id="breadcrumb" class="col-md-12">
		<ol class="breadcrumb">
			<li><a href="index.html">Index</a></li>
			<li><a href="#">Logs</a></li>
			<li><a href="#">Settings</a></li>
		</ol>
	</div>
</div>

	<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="box">
			<div class="box-header">
				<div class="box-name">
					<i class="fa fa-cog"></i>
					<span>Log Settings</span>
				</div>
				<div class="no-move"></div>
			</div>
				<div class="box-content">
		<h4 class="page-header">Level Of Logs</h4>
				<form class="form-horizontal logs" role="form">
				<div class="row form-group">
					<div class="col-sm-12">
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="TRACE" > TRACE
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="DEBUG"> DEBUG
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="INFO"> INFO
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="WARN"> WARN
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="ERROR"> ERROR
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
						<div class="radio-inline">
							<label>
								<input type="radio" name="radio-inline" id="OFF"> OFF
								<i class="fa fa-circle-o"></i>
							</label>
						</div>
				</div>
				</div>
				<div class="text-center center-block" style="margin-top:1%;"><button id="submit" type="button" class="btn btn-primary center-block">Save</button></div>
				<div id="success">Well saved</div>
				</form>	
			</div>



		</form>
	</div>
	

	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	
	<script>
		window.levelLogs = "${level}";
	</script>	
 	<script src="<c:url value="/resources/js/logSettings.js" />"></script>
</body>
</html>