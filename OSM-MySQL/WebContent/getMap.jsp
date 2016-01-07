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
		 <link href="css/animate.min.css" rel='stylesheet' type='text/css'/>
		 <style type="text/css">
		 	#map { height: 80vh; }
		 	body{
		 		margin:0;
		 		position:relative;
		 	}
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
		 	#search.extend{
  				width:300px;
  			}
		 	#search{
		 		width: 200px;
				background: white;
				position: absolute;
				left:5px;
				bottom: 5px;
				height: 40px;
				border: 1px solid #D9D9D9;
		 	}
		 	#search:hover{
		 		width:300px;
		 		-moz-animation-duration: 1s; 
    			-webkit-animation-duration: 1s; /* Chrome, Safari, Opera */
    			animation-duration: 1s;
  				animation-name: slidein;
			}
			@keyframes slidein {
			  from {
			    width: 200px;
			  }
			  
			  to {
			    width: 300px;
			  }
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
				width: 13.5%;
				cursor:pointer;
				min-width: 39px;
				z-index: 100;
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
		 	.leaflet-bottom.leaflet-right{
		 		display:none;
		 	}
		 	#select{
		 		position:absolute;
		 		bottom:5px;
		 		right:5px;
		 	}
		 	div#noclient{
		 		display:none;
		 		width: 100%;
				position: absolute;
				z-index: 1000;
				bottom: 0px;
		 	}
		 	div#noclient #message{
		 		background : white;
		 		width:50%;
		 		margin-left:auto;
		 		margin-right:auto;
		 		text-align:center;
		 		padding:5px;
		 		-moz-box-shadow: 0px -5px 5px 0px #656565;
				-webkit-box-shadow: 0px -5px 5px 0px #656565;
				-o-box-shadow: 0px -5px 5px 0px #656565;
				box-shadow: 0px -5px 5px 0px #656565;
				filter:progid:DXImageTransform.Microsoft.Shadow(color=#656565, Direction=90, Strength=5);
				-webkit-border-top-left-radius: 5px;
				-webkit-border-top-right-radius: 5px;
				-moz-border-radius-topleft: 5px;
				-moz-border-radius-topright: 5px;
				border-top-left-radius: 5px;
				border-top-right-radius: 5px;
				font-family: "Open Sans",sans-serif;
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
	var optionSelected=0;	
	var all=1;
	
	var divNoClient = $("div#noclient");
	
	var southWest = L.latLng(minLat,minLgn ),
    northEast = L.latLng(maxLat,maxLgn),
    bounds = L.latLngBounds( southWest,northEast);
	var map = L.map('map',{
		maxBounds : bounds,
		center: [(minLat+maxLat)/2, (minLgn+maxLgn)/2],
	    zoom: 15,
	    minZoom:13
	});
	map.fitBounds(bounds);
	
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ', {
		maxZoom: 18,
		id: 'mapbox.streets'
	}).addTo(map);
	var markers = [];
	var listLatlgn = [];
	var polyline;
	function setMarkers(all,varGlobClient){
		if (polyline!=undefined){
			map.removeLayer(polyline);
			polyline = undefined;
		}
		for (var i =0 ; i< markers.length ; i++){
			map.removeLayer(markers[i]);
		}
		if (optionSelected==0){
			$.post("GetClients",{
				all:all,
				client:varGlobClient
			},function(data){
				markers = [];
				if (data.code==0){
					if (data.clients.length==0 && !divNoClient.hasClass("animated") ){	
						divNoClient.addClass('animated fadeInUp').show();
						setTimeout(function(){
							divNoClient.removeClass('fadeInUp').addClass('animated fadeOutDown').show()
							.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',function(){
								divNoClient.removeClass('animated fadeOutDown').hide();
							});
						}, 6000);
					}
					else
						$.each(data.clients,function(index,client){
							markers.push( L.marker([client.lat,client.lgn]).bindPopup(client.id).addTo(map) );
						});					
				}else if (!divNoClient.hasClass("animated") ){
					divNoClient.addClass('animated fadeInUp').show();
					setTimeout(function(){
						divNoClient.removeClass('fadeInUp').addClass('animated fadeOutDown').show()
						.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',function(){
							divNoClient.removeClass('animated fadeOutDown').hide();
						});
					}, 6000);
				}
			},"json").fail(function() {
			    alert( "error" );
			  });
		}else{
			$.post("GetHistory",{
				optionSelected:optionSelected,
				client : varGlobClient
			},function(data){
				
				listLatlgn = [];
				$.each(data.history,function(index,entry){
					listLatlgn.push(L.latLng(entry.lat,entry.lon));
				});	
				polyline = L.polyline(listLatlgn, {color: 'red'}).addTo(map);
			},"json").fail(function() {
			    alert( "error" );
			  });
		}
		 
	}
	setMarkers(all,varGlobClient);
	
	$(document).ready(function(){
		
		divNoClient = $("div#noclient");
		
		$("#mapContainer").append('<div id="search" class="">'+
		'<div class="container_icon"><i class="fa fa-search"></i></div>'+
		'<input placeholder="Search for client" id="input_client"/>'+
		'</div>');
		$("#mapContainer").append(

			    '<FORM id="select">'+
			    '<SELECT name="nom" size="1">'+
			    '<OPTION id="sans">sans trajet</option>'+
			    "<OPTION id=\"ajd\" disabled>aujourd'hui</option>"+
			    '<OPTION id="hier" disabled>hier</option>'+
			    '</SELECT>'+
			    '</FORM>');

		var timer = setInterval(function(){
			setMarkers(all,varGlobClient);
		}, 100);
		
		$("#input_client").autocomplete({
	        source: function(request,response){
	        	$.get("AutocompleteClient",
	        			{data:request.term},
	        			function(data){
	        				response(data);
	        	},"json");
	        },
	        minLength: 2,
	        select: function(event, ui) {
	        	set_all(0);
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
	            $("#search").addClass("extend");
	        },
	        close:function(event,ui){
	        	 $("#search").removeClass("extend");
	        }
	    }).keypress(function(e) {
	        if(e.which == 13) {
	        	if ( $(e.target).val()=="" ){
	        		set_all(1)
	        	}else{
	        		set_all(0)
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
		
		$( "#select" )
		  .change(function() {
		    var id = "";
		    $( "select option:selected" ).each(function() {
		      id = $( this )[0].id;
			  switch(id){
			  case "sans":
				  optionSelected=0;	
				  break;
			  case "ajd":
				  optionSelected=1;	
				  break;
			  case "hier":
				  optionSelected=2;	
				  break;
			  }
			  setMarkers(all,varGlobClient);
		    });
		  })
		  .trigger( "change" );
		
		
	});
	
	function set_all(activated){
		if (activated==1){
			all = 1;
			$("option#ajd,option#hier").prop('disabled',true);
		}else{
			all=0;
			$("option#ajd,option#hier").prop('disabled',false);
		}
	}

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
	<div id="noclient">
		<div id="message">
			<div class="text">No current client available in the server</div>
		</div>
	</div>
	</body>
</html>