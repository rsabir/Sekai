
<%@ page session="false"%>

<html>
<head>
<meta charset="UTF-8">
<title>Settings</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=yes">


<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Open+Sans'>

<link rel="stylesheet" href="css/setConfig.css">
<style>
	div.app{
		position:absolute;
		-ms-transform: translate(-50%,-50%); /* IE 9 */
    	-webkit-transform: translate(-50%,-50%); /* Chrome, Safari, Opera */
    	transform: translate(-50%,-50%);
		width:70%;
		padding:10px;
		background:rgba(0,0,0,0.6);
		color:white;
		top: 50%;
		left: 50%;
		height: 100px;
		text-align: center;
		line-height: center;
	}
	div.app.active .text{
		line-height:30px;
		color:white;
		font-weight:normal;
	}
</style>

</head>
<body>


	<div class="cont">


		<form method="post" action="SendGPS">

			<div class="form">
				<div class="text">Config.json</div>
				<div class="forceColor"></div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="config" type="text" class="input" placeholder="Url of Server Config.json" />
				</div>
				<div class="text">Mysql</div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="username" type="text" class="input" placeholder="Username" />
				</div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="password" type="password" class="input" placeholder="Password" />
				</div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="port" type="text" class="input" placeholder="Port" />
				</div>
				<div class="topbar">
					<div class="spanColor"></div>
					<input id="database" type="text" class="input" placeholder="Database" />
				</div>
				<button type="button" class="submit_" id="submit">Save</button>
			</div>



		</form>
	</div>
	<div class="app">
		<div class="text">The new settings were well saved<br/>(optionnal)Please restart the server </div>
      </div>
	</div>
	
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="js/index.js"></script>
</body>
</html>