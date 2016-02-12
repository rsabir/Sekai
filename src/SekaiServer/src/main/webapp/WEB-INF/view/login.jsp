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

<link href="<c:url value="/resources/css/setConfig.css" />" rel="stylesheet">
<style>
div.app{
	width: 70%;
	padding: 10px;
	background: rgba(0, 0, 0, 0.6);
	color: white;
	height: 100px;
	text-align: center;
	line-height: center;
}
div.cont form, div.app{ 
	position: absolute;
	left: 50%;
	bottom:0px;
} 
div.cont form .form{
position: initial;
bottom: 0px;
}
div.app.active .text {
	line-height: 30px;
	color: white;
	font-weight: normal;
}

.msg,.error {
    font-size: 12px;
    margin-bottom: 14px;
    color: #FFF;
    background: #314EF3;
    padding: 2%;
}
.error{
	background: #C52C2C;
}
</style>

</head>
<body>


	<div class="cont">

		<form method="post" action="<c:url value='/j_spring_security_check' />">

			<div class="form">
				<div class="text">Login</div>
				
				<c:if test="${not empty error}">
					<div class="error">${error}</div>
				</c:if>
				<c:if test="${not empty msg}">
					<div class="msg">${msg}</div>
				</c:if>
				
				<div class="forceColor"></div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="username" name="username" type="text" class="input"
						placeholder="username" />
				</div>
				<div class="topbar">
					<input id="password" name="password" type="password" class="input"
						placeholder="Passoword" />
				</div>
				 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				
				<button type="submit" class="submit_" id="submit">Login</button>
			</div>



		</form>
	</div>
	<div class="app">
		<div class="text">
			The new settings were well saved<br />(optionnal)Please restart the
			server
		</div>
	</div>
	</div>

	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
 	<script src="<c:url value="/resources/js/settings.js" />"></script>
</body>
</html>