<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<meta charset="UTF-8">
<title>Settings</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=yes">
	
<sec:csrfMetaTags />

<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Open+Sans'>

<style>
div.app {
	position: absolute;
	-ms-transform: translate(-50%, -50%); /* IE 9 */
	-webkit-transform: translate(-50%, -50%); /* Chrome, Safari, Opera */
	transform: translate(-50%, -50%);
	width: 70%;
	padding: 10px;
	background: rgba(0, 0, 0, 0.6);
	color: white;
	top: 50%;
	left: 50%;
	height: 100px;
	text-align: center;
	line-height: center;
}

div.app.active .text {
	line-height: 30px;
	color: white;
	font-weight: normal;
}

#contentLog{
	height:80vh;
	overflow:auto;
	font-size:14px;
	padding-left:20px;
	padding-right:20px;
	margin:5px;
	-moz-box-shadow: inset 1px 1px 30px 0px #c0c0c0;
	-webkit-box-shadow: inset 1px 1px 30px 0px #c0c0c0;
	-o-box-shadow: inset 1px 1px 30px 0px #c0c0c0;
	box-shadow: inset 1px 1px 30px 0px #c0c0c0;
	filter:progid:DXImageTransform.Microsoft.Shadow(color=#c0c0c0, Direction=134, Strength=30);
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
	border-radius: 10px;
}
.loading{
	postion:absolute;
	top:50%;
	left:50%;
	transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);/* IE 9 */
    -webkit-transform: translate(-50%,-50%);/* Safari */
}
html{
	font-size:12px;
}
</style>

</head>
<body>
<div class="row">
	<div id="breadcrumb" class="col-md-12">
		<ol class="breadcrumb">
			<li><a href="#">Log</a></li>
			<li><a href="#">HTTP Logs</a></li>
		</ol>
	</div>
</div>
<div class="row">
		<div class="box">
			<div class="box-header">
				<div class="box-name">
				<i class="fa fa-list-ul"></i>
					<span>HTTP Logs</span>
				</div>
				<div class="box-icons">
					<a class="refresh-log">
						<i class="fa fa-refresh"></i>
					</a>
					<a class="expand-link">
						<i class="fa fa-expand"></i>
					</a>
				</div>
				<div class="no-move"></div>
			</div>
			<div class="box-content no-padding" >
			<div id="contentLog">
				<div class="loading"><i class="fa fa-refresh fa-spin"></i></div>
			</div>
			</div>
</div>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
 	<script>
	 	String.prototype.replaceAll = function(search, replacement) {
	 	    var target = this;
	 	    return target.replace(new RegExp(search, 'g'), replacement);
	 	};
 		function getLog(){
 			$.get("getHttpLogFile",function(data){
 	 			data = data.replaceAll("\n", '<br/>');
 	 			$("#contentLog").html(data);
 	 			$(".loading").hide();
 	 		},"html");
 		}
 		$(".refresh-log").click(function(){
 			$(".loading").show();
 			getLog();
 		});
 		getLog();
 		</script>
</body>
</html>