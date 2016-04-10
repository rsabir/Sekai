<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.css" />
<link href='https://fonts.googleapis.com/css?family=Open+Sans'
	rel='stylesheet' type='text/css'>
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel='stylesheet' type='text/css'>
<link href="<c:url value="/resources/plugins/jquery-ui/jquery-ui.min.css" />" rel="stylesheet">
<link rel="stylesheet" href="<c:url value="/resources/css/leaflet.awesome-markers.css"/>">
<style type="text/css">
#map {
	height: 80vh;
}

h1 {
	color: white;
	text-align: center;
	font-family: 'Open Sans', sans-serif;
	background: black;
	padding: 1%;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}

#search_client.extend {
	width: 300px;
}

#search_client {
	width: 200px;
	background: white;
	position: absolute;
	left: 5px;
	bottom: 5px;
	height: 40px;
	border: 1px solid #D9D9D9;
}

#search_client:hover {
	width: 300px;
	-moz-animation-duration: 1s;
	-webkit-animation-duration: 1s; /* Chrome, Safari, Opera */
	animation-duration: 1s;
	animation-name: slidein;
}

@
keyframes slidein {
	from { 
		width:200px;	
	}
	to {
		width: 300px;
	}
}
#mapContainer {
	position: relative;
}

.container_icon {
	position: absolute;
	right: 0;
	height: 100%;
	background: black;
	color: white;
	top: 0;
	width: 13.5%;
	cursor: pointer;
	min-width: 39px;
	z-index: 100;
}

.container_icon:hover {
	opacity: 0.85;
}

.container_icon:ACTIVE {
	opacity: 0.9;
}

i.fa.fa-search {
	position: absolute;
	top: 50%;
	transform: translate(-50%, -50%);
	left: 50%;
}

#input_client {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 85%;
	padding: 10px 0px 10px 5px;
	border: 0px none;
	background: inherit;
}

.leaflet-bottom.leaflet-right {
	display: none;
}

 #select {
	width: 30%;
	position: absolute;
	right: 5px;
	bottom: 0px;
}
#select .form-group{
	margin-bottom:0px;
}

div#noclient {
	display: none;
	width: 100%;
	position: absolute;
	z-index: 1000;
	bottom: 0px;
}

div#noclient #message {
	background: white;
	width: 50%;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
	padding: 5px;
	-moz-box-shadow: 0px -5px 5px 0px #656565;
	-webkit-box-shadow: 0px -5px 5px 0px #656565;
	-o-box-shadow: 0px -5px 5px 0px #656565;
	box-shadow: 0px -5px 5px 0px #656565;
	filter: progid:DXImageTransform.Microsoft.Shadow(color=#656565,
		Direction=90, Strength=5);
	-webkit-border-top-left-radius: 5px;
	-webkit-border-top-right-radius: 5px;
	-moz-border-radius-topleft: 5px;
	-moz-border-radius-topright: 5px;
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
	font-family: "Open Sans", sans-serif;
}

#error {
	display: none;
	background: rgba(0, 0, 0, 0.5);
	width: 70%;
	padding-top: 10px;
	padding-bottom: 10px;
	position: fixed;
	top: 50%;
	left: 50%;
	text-align: center;
	color: white;
	transform: translate(-50%, -50%);
}
#container_nb_clients{
	position:absolute;
	display:inline-block;
	background:rgba(0,0,0,0.5);
	top:0px;
	right:0px;
	color:white;
	padding:2px;
}

  .awesome-marker i {
    margin-left: 1px;
    }
</style>
<!-- tags for csrf protection -->
<sec:csrfMetaTags />
<title>The Map</title>
</head>
<body>
<div class="row">
	<div id="breadcrumb" class="col-md-12">
		<ol class="breadcrumb">
			<li><a href="/index">Index</a></li>
			<li><a href="#">Map</a></li>
		</ol>
	</div>
</div>
<div class="row">
	<div class="col-xs-12">
		<div class="box">
			<div class="box-header">
				<div class="box-name">
					<i class="fa fa-map-marker"></i>
					<span>The current users in the server</span>
				</div>
				<div class="box-icons">
					<a class="expand-link">
						<i class="fa fa-expand"></i>
					</a>
				</div>
				<div class="no-move"></div>
			</div>
		
			<div id="mapContainer">
				<div id="map"></div>
				<div id="noclient">
					<div id="message">
						<div class="text">No current client available in the server</div>
					</div>
				</div>
				<div id="container_nb_clients"><span id="nb_clients">0</span> Clients</div>
			</div>
		</div>
	</div>
</div>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'   />"></script>
	<script src="<c:url value="/resources/plugins/jquery-ui/jquery-ui.min.js"/>"></script>
<!-- 	<script src="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.js"></script> -->
<%-- 	<script src="<c:url value='/resources/js/leaflet.awesome-markers.min.js'/>"></script> --%>
	<script type="text/javascript" src="<c:url value='/resources/js/getMap.js'/>"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
		});
		
		
	})
	</script>
	<div id="error">Error in settings : The url of configuration is
		not good</div>
</body>
</html>