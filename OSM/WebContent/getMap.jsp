<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="server.Server" %>
	<%@ page import="utils.ConfigUtils" %>
	<%@ page import="constants.Urls" %>
	<%@ page import="java.util.ArrayList" %>
    <%
	String configString = ConfigUtils.getConfig(Urls.CONFIGSERVER);
	ArrayList<ArrayList<Object>> configList = ConfigUtils.parse(configString);
	Server.refresh(configList);
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		 <link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.css" />
		 <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
		 <link href="css/jquery-ui.min.css" rel='stylesheet' type='text/css'/>
		 <link href="css/jquery-ui.structure.min.css" rel='stylesheet' type='text/css'/>
		 <link href="css/jquery-ui.theme.min.css" rel='stylesheet' type='text/css'/>
		 <link href='css/font-awesome.min.css' rel='stylesheet' type='text/css'>
		 <style type="text/css">
		 	#map { height: 80vh; }
		 	h1{
		 		color:white;
		 		text-align:center;
		 		font-family: 'Open Sans', sans-serif;
		 		background:black;
		 		padding:1%;
				-moz-border-radius: 5px;
		 		-webkit-border-radius: 5px;
				border-radius: 5px;
		 	}
		 	#search{
		 		width: 300px;
				background: white;
				position: absolute;
				left:5px;
				bottom: 5px;
				height: 40px;
				border: 1px solid #D9D9D9;
		 	}
		 	#mapContainer{
		 		position:relative;
		 	}
		 	.container_icon{
			 	position: absolute;
				right: 0;
				height:100%;
				background:black;
				color:white;
				top:0;
				width: 15%;
				cursor:pointer;
		 	}
		 	.container_icon:hover{
		 		opacity:0.85;
		 	}
		 	.container_icon:ACTIVE{
		 		opacity:0.9;
		 	}
		 	i.fa.fa-search{
			 	position: absolute;
				top: 50%;
				transform: translate(-50%,-50%);
				left: 50%;
		 	}
		 	#input_client{
				position: absolute;
				top: 0px;
				left: 0px;
				height: 20px;
				width: 85%;
				padding: 10px 0px 10px 5px;
				border: 0px none;
				background: inherit;	
		 	}
		 </style>
		<title>The Map</title>
	</head>
	<body>
	<h1>The Users In The Server </h1>
	<div id="mapContainer"><div id="map"></div></div>
	<script src="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.js"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	<script>
	var minLat = <% out.print(Server.getMinLat());%>
	var minLgn = <% out.print(Server.getMinLgn());%>
	var maxLat = <% out.print(Server.getMaxLat());%>
	var maxLgn = <% out.print(Server.getMaxLgn());%>
	
	var varGlobClient = [];
	var all=1;
	
	
	var southWest = L.latLng(minLat,minLgn ),
    northEast = L.latLng(maxLat,maxLgn),
    bounds = L.latLngBounds( southWest,northEast);
	var map = L.map('map',{
		maxBounds : bounds,
		center: [(minLat+maxLat)/2, (minLgn+maxLgn)/2],
	    zoom: 15,
	    minZoom:13
	});
	
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ', {
		maxZoom: 18,
		id: 'mapbox.streets'
	}).addTo(map);
	var markers = [];
	function setMarkers(all,varGlobClient){
		$.post("GetClients",{
			all:all,
			client:varGlobClient
		},function(data){
			for (var i =0 ; i< markers.length ; i++){
				map.removeLayer(markers[i]);
			}
			markers = [];
			if (data.code==0){
				$.each(data.clients,function(index,client){
					markers.push( L.marker([client.lat,client.lgn]).bindPopup(client.id).addTo(map) );
				});					
			}	
		},"json").fail(function() {
		    alert( "error" );
		  });
		
		 
	}
	setMarkers(all,varGlobClient);
	
	$(document).ready(function(){
		
		$("#mapContainer").append('<div id="search">'+
		'<div class="container_icon"><i class="fa fa-search"></i></div>'+
		'<input placeholder="Search for client" id="input_client"/>'+
		'</div>');
		var timer = setInterval(function(){
			setMarkers(all,varGlobClient);
		}, 10000);
		
		$("#input_client").autocomplete({
	        source: function(request,response){
	        	$.get("AutocompleteClient",function(data){
	        		response(data);
	        	},"json");
	        },
	        minLength: 2,
	        select: function(event, ui) {
	       		all =0;
	       		varGlobClient = ui.item.value;
	       		setMarkers(all,varGlobClient);
	        },
	        position: { 
	        	my : "left bottom",
	        	at: "left top" 
	        },
	        html: true, // optional (jquery.ui.autocomplete.html.js required)

		    // optional (if other layers overlap autocomplete list)
	        open: function(event, ui) {
	            $(".ui-autocomplete").css("z-index", 1000);
	        }
	    }).keypress(function(e) {
	        if(e.which == 13) {
	        	if ( $(e.target).val()=="" ){
	        		all = 1;
	        	}else{
		        	all = 0;
		       		varGlobClient = $(e.target).val();
	        	}
	       		setMarkers(all,varGlobClient);
	        }
	    });
		
		$(".container_icon").click(function(){
			var e = jQuery.Event("keypress");
			e.which = 13; // # Some key code value
			$("#input_client").trigger(e);
		});
	});

// 	L.marker([51.5, -0.09]).addTo(map)
// 		.bindPopup("<b>Hello world!</b><br />I am a popup.").openPopup();

// 	L.circle([51.508, -0.11], 500, {
// 		color: 'red',
// 		fillColor: '#f03',
// 		fillOpacity: 0.5
// 	}).addTo(map).bindPopup("I am a circle.");

 	L.polygon([
 		[minLat,minLgn ],
 		[minLat,maxLgn ],
 		[maxLat,maxLgn],
 		[maxLat,minLgn ]
		
 	],{
 		 fillOpacity: 0
 	}).addTo(map);


	var popup = L.popup();

	function onMapClick(e) {
		popup
			.setLatLng(e.latlng)
			.setContent("You clicked the map at " + e.latlng.toString())
 			.openOn(map);
 	}

 	map.on('click', onMapClick);

	</script>
	</body>
</html>