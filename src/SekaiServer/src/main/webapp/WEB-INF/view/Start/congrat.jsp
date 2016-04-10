<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="row">
	<div id="breadcrumb" class="col-md-12">
	</div>
</div>
<div class="row">
	<div class="col-xs-12 text-center page-404">
	<h2>Congratulation, you have finished the basic Settings</h2>
		<form class="form-inline" role="form">
			<h4>You can now access the interface of Sekai</h4>
			<div class="text-center center-block" style="margin-top:1%;"><button type="button" id="ok" class="btn btn-primary center-block">OK</button></div>
		</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
	});
	$('html').animate({scrollTop: 0},'slow');});
	$.post('/Start/finish');
	$("button#ok").click(function(){
		window.location = "/";
	})
</script>
