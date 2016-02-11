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
<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel='stylesheet' type='text/css' />
<link href="<c:url value="/resources/css/jquery-ui.structure.min.css" />" rel='stylesheet'
	type='text/css' />
<link href="<c:url value="/resources/css/jquery-ui.theme.min.css" />" rel='stylesheet'
	type='text/css' />
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel='stylesheet' type='text/css'>
<link href="<c:url value="/resources/css/animate.min.css" />" rel='stylesheet' type='text/css' />
<style type="text/css">
#map {
	height: 80vh;
}

body {
	margin: 0;
	position: relative;
	overflow: hidden;
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

#search.extend {
	width: 300px;
}

#search {
	width: 200px;
	background: white;
	position: absolute;
	left: 5px;
	bottom: 5px;
	height: 40px;
	border: 1px solid #D9D9D9;
}

#search:hover {
	width: 300px;
	-moz-animation-duration: 1s;
	-webkit-animation-duration: 1s; /* Chrome, Safari, Opera */
	animation-duration: 1s;
	animation-name: slidein;
}

@
keyframes slidein {from { width:200px;
	
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
	height: 20px;
	width: 85%;
	padding: 10px 0px 10px 5px;
	border: 0px none;
	background: inherit;
}

.leaflet-bottom.leaflet-right {
	display: none;
}

#select {
	position: absolute;
	bottom: 5px;
	right: 5px;
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
</style>
<!-- tags for csrf protection -->
<sec:csrfMetaTags />
<title>The Map</title>
</head>
<body>
	<h1>The Users In The Server</h1>
	<div id="mapContainer">
		<div id="map"></div>
	</div>
	<script src="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.js"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'   />"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
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
	<div id="noclient">
		<div id="message">
			<div class="text">No current client available in the server</div>
		</div>
	</div>
	<div id="error">Error in settings : The url of configuration is
		not good</div>
</body>
</html>