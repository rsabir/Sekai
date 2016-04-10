<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Basic Configuration</title>
		<meta name="description" content="description">
		<meta name="author" content="DevOOPS">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="<c:url value="/resources/plugins/bootstrap/bootstrap.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/plugins/jquery-ui/jquery-ui.min.css" />" rel="stylesheet">
		<link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
		<link href='http://fonts.googleapis.com/css?family=Righteous' rel='stylesheet' type='text/css'>
		<link href="<c:url value="/resources/plugins/fancybox/jquery.fancybox.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/plugins/fullcalendar/fullcalendar.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/plugins/xcharts/xcharts.min.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/plugins/select2/select2.css" />" rel="stylesheet">
		<link href="<c:url value="/resources/css/devoops-style.css" />" rel="stylesheet">
		<link rel="stylesheet"
			href="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.css" />	
		<link href="<c:url value="/resources/css/animate.min.css" />" rel='stylesheet' type='text/css' />
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
				<script src="http://getbootstrap.com/docs-assets/js/html5shiv.js"></script>
				<script src="http://getbootstrap.com/docs-assets/js/respond.min.js"></script>
		<![endif]-->
		<sec:csrfMetaTags />
		<style type="text/css">
		.nav.main-menu > li > a.disabled{
			color: #405F7A;
		}
		</style>
	</head>
<body>
<!--Start Header-->
<div id="screensaver">
	<canvas id="canvas"></canvas>
	<i class="fa fa-lock" id="screen_unlock"></i>
</div>
<div id="modalbox">
	<div class="devoops-modal">
		<div class="devoops-modal-header">
			<div class="modal-header-name">
				<span>Basic table</span>
			</div>
			<div class="box-icons">
				<a class="close-link">
					<i class="fa fa-times"></i>
				</a>
			</div>
		</div>
		<div class="devoops-modal-inner">
		</div>
		<div class="devoops-modal-bottom">
		</div>
	</div>
</div>
<header class="navbar">
	<div class="container-fluid expanded-panel">
		<div class="row">
			<div id="logo" class="col-xs-12 col-sm-12">
				<a href="index" id="realLogo"class="col-xs-12 col-sm-12">Sekai</a>
			</div>
		</div>
	</div>
</header>
<!--End Header-->
<!--Start Container-->
<div id="main" class="container-fluid">
	<div class="row">
		<div id="sidebar-left" class="col-xs-2 col-sm-2">
			<ul class="nav main-menu">
				<li>
					<a href="/Start/ChangePassword" id="first" class="active ajax-link" disabled>
						<span class="hidden-xs">1 - Change Password</span>
					</a>
				</li>
				<li>
					<a href="#" id="second" class="disabled">
						<span class="hidden-xs">2 - Basic Configuration</span>
					</a>
				</li>
				<li>
					<a href="#" id="third" class="disabled">
						<span class="hidden-xs">3 - Config.json</span>
					</a>
				</li>
			</ul>
		</div>
		<!--Start Content-->
		<div id="content" class="col-xs-12 col-sm-10">
			<div class="preloader">
				<img src="<c:url value="/resources/img/devoops_getdata.gif"/>" class="devoops-getdata" alt="preloader"/>
			</div>
			<div id="ajax-content"></div>
		</div>
		<!--End Content-->
	</div>
</div>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<!--<script src="http://code.jquery.com/jquery.js"></script>-->
<script src="<c:url value="/resources/plugins/jquery/jquery-2.1.0.min.js"/>"></script>
<script src="<c:url value="/resources/plugins/jquery-ui/jquery-ui.min.js"/>"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<c:url value="/resources/plugins/bootstrap/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/plugins/justified-gallery/jquery.justifiedgallery.min.js"/>"></script>
<script src="<c:url value="/resources/plugins/tinymce/tinymce.min.js"/>"></script>
<!-- All functions for this theme + document.ready processing -->
<script src="<c:url value="/resources/js/devoops.js"/>"></script>

 

<script>
	window.li = $(".nav li .ajax-link");
	li.click(function(e){
		var element = $(e.target);
		$("head title").html(element.text());
	});
	$(document).ready(function () {
	    if(window.location.href.indexOf("#") > -1) {
	       window.location ="/Start/Index";
	    }
	}); 
</script>
</body>
</html>
