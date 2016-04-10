<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<link href="<c:url value="/resources/css/jquery-linedtextarea.css" />" rel='stylesheet'
	type='text/css' />
<title>Set the Config</title>
<style>

textarea {
	width: 90%;
	height: 50vh;
	margin-left: auto;
	margin-right: auto;
	display: block;
}

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
				<li><a href="index.html">index</a></li>
				<li><a href="#">Settings</a></li>
				<li><a href="#">Config</a></li>
			</ol>
		</div>
	</div>
	<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="box">
			<div class="box-header">
				<div class="box-name">
					<i class="fa fa-cog"></i>
					<span>Config.json</span>
				</div>>
				<div class="no-move"></div>
			</div>
				<div class="box-content">
				<h4 class="page-header">The local Config.json</h4>
					<form class="form-horizontal" role="form">
						<div class="text-center center-block"><textarea class="center-block" style="width:100%"></textarea></div>
						<div class="text-center center-block" style="margin-top:1%;"><button type="button" class="btn btn-primary center-block">SAVE</button></div>
						<div id="error"></div>
						<div id="success">Well saved</div>
					</form>
	<!--   <script>tinymce.init({ 
 	  selector: 'textarea'
   });</script> -->
  <script type="text/javascript" src="<c:url value='/resources/js/jquery-linedtextarea.js'/>"></script>
  <script>
		
	$.get("/Config",function(data){
  		$("textarea").val(JSON.stringify(data,null,'\t'));
  	},"json");
	 $("textarea").linedtextarea();
  	$("textarea").change(function(){
  		$("#error").hide();
  		$("#success").hide();
  		try{
			var config = $.parseJSON($("textarea").val());
		}catch(e){
			$("#error").text(e).show();
		}
  	})
	$("button").click(function(){
		try{
			var config = $.parseJSON($("textarea").val());
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
					xhr.setRequestHeader(header, token);
			});
			$.post("SetConfig",{
				response:$("textarea").val()
				},function(data){
					if (data.code==0){
						$("#success").text("Well saved").show();
					}else{
						$("#error").text("An error happened, please try later").show();
					}
				},"json");
		}catch(e){
			$("#error").text(e).show();
		}
	});
  </script>
</body>
</html>