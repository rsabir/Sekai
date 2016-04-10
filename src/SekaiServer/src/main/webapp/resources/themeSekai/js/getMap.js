$(document).ready(function(){
	var minLat;
	var minLgn;
	var maxLat;
	var maxLgn;
	var map;
	var polyline;
	var popup;
	var timer;
	var varGlobClient = [];
	var listOfClients = [];
	var listOfClientsId = [];
	var isFollowChecked=true;
	var all=1;
	var divNoClient = $("div#noclient");
	var divNbClients = $("span#nb_clients");
	var markers = [];
	var listLatlgn = [];
	var count = 0;
	var lastTimeNoClientShowed;
	var colorTab = ['red', 'darkred', 'orange', 'green', 'darkgreen', 'blue', 'purple', 'darkpuple', 'cadetblue'];
	var colorPolylineTab = ['#F92525', '#A71C1C', 'orange', 'green', '#265126', 'blue', 'purple', '#4D2A4D', '#9FC5EA'];
	var colorIndexCounter=0;
	
	var TIME_REFRESH=1000;
	var TIME_BETWEEN_NO_CLIENT = 15000;
	
	//////////////////////////////////////////////////// Constants ///////////////////////////////////////////
	var TEXT_PLEASE_SELECT_CLIENT = "Please select Client"
	var TEXT_SELECT_CLIENT = "SELECT A CLIENT";
	var TEXT_SEARCH_CLIENT = "Search For Client";
	var TEXT_DATE = "date";
	var ID_INPUT_DATE= "#input_date";
	var ID_SEARCH_CLIENT = "#input_client";
	//////////////////////////////////////////////////// Objects /////////////////////////////////////////////////////////
	function Client(id,gpsClient){
		this.id = id;
		this.firstMarker = null;
		this.lastMarker = new Marker(gpsClient,
				L.marker([gpsClient.lat,gpsClient.lon], {icon: getColoredMarker('blue')}).bindPopup(id).addTo(map));
		this.path = [];
		this.color=getClientColor();
		this.colorPolyline = colorPolylineTab[colorIndexCounter-1];
	}
	function GPS(lat,lon){
		this.lat = lat;
		this.lon = lon;
	}
	function Marker(gpsMarker,markerLeaflet){
		this.gps = gpsMarker;
		this.marker = markerLeaflet;
	}
	////////////////////////////////////////////////////Functions/////////////////////////////////////////////////////////
	function getClientColor(){
		if (colorIndexCounter == colorTab.length)
			colorIndexCounter=0;
		colorIndexCounter++;
		return colorTab[colorIndexCounter-1];
	}
	function getColoredMarker(color){
		return L.AwesomeMarkers.icon({
		    icon: 'circle',
		    prefix:'fa',
		    iconColor:'white',
		    markerColor: color
		  });
	}
	function getClients(){
		return setInterval(function(){
			setMarkers(all,varGlobClient);
		}, TIME_REFRESH);
	}
	function getTodayString(){
		 var today = new Date();
		    var dd = today.getDate();
		    var mm = today.getMonth()+1; //January is 0!

		    var yyyy = today.getFullYear();
		    if(dd<10){
		        dd='0'+dd
		    } 
		    if(mm<10){
		        mm='0'+mm
		    } 
		    var today = dd+'/'+mm+'/'+yyyy;
		    return today;
	}
	function addClient(idClient,gpsClient){
			var isAdded = false;
			listOfClients.forEach(function(clientObject,index,array){
				if (clientObject.id==idClient){
					var lastGpsMarker = clientObject.lastMarker.gps;
					if (lastGpsMarker.lat != gpsClient.lat || lastGpsMarker.lon != gpsClient.lon){
						if (isFollowChecked == true){
							if (clientObject.firstMarker==null){
								clientObject.firstMarker = new Marker(lastGpsMarker,
										L.marker([lastGpsMarker.lat,lastGpsMarker.lon],{icon: getColoredMarker(clientObject.color)}).bindPopup(idClient).addTo(map));
							}
							clientObject.path.push(L.polyline([L.latLng(lastGpsMarker.lat,lastGpsMarker.lon),
							                                   L.latLng(gpsClient.lat,gpsClient.lon)],
									{color: clientObject.colorPolyline}).addTo(map));
						}
						map.removeLayer(clientObject.lastMarker.marker);
						clientObject.lastMarker.gps = gpsClient;
						clientObject.lastMarker.marker = L.marker([gpsClient.lat,gpsClient.lon], {icon: getColoredMarker('blue')}).bindPopup(idClient).addTo(map);
						array[index] = clientObject;
					}
					isAdded=true;
				}
			});
			if (!isAdded){
				var client = new Client(idClient,gpsClient);
				listOfClients.push(client);
			}
	}
	function deleteClient(idClient){
		listOfClients.forEach(function(clientObject,index,array){
			if (clientObject.id==idClient){
				if (clientObject.firstMarker!=null){
					map.removeLayer(clientObject.firstMarker.marker);
					clientObject.path.forEach(function(polyline,index,array){
						map.removeLayer(polyline);
					});
				}
				map.removeLayer(clientObject.lastMarker.marker);
				array.splice(index, 1);
			}
		});
		var index = listOfClientsId.indexOf(idClient);
		if (index > -1) {
			listOfClientsId.splice(index, 1);
		}
	}
	function disableFollow(){
		listOfClients.forEach(function(clientObject,index,array){
			if (clientObject.firstMarker!=null){
				map.removeLayer(clientObject.firstMarker.marker);
				clientObject.path.forEach(function(polyline,index,array){
					map.removeLayer(polyline);
				});
				clientObject.path = [];
				clientObject.firstMarker = null;
				array[index] = clientObject;
			}
		});
	}
	function deleteAllAbsentClients(newClientsIdList){
		listOfClientsId.sort();
		newClientsIdList.sort();
		var j=0;
		for (var i=0; i<listOfClientsId.length ; i++){
			while(newClientsIdList[j]!=undefined && listOfClientsId[i]>newClientsIdList[j]){
				j++;
			}
			if (listOfClientsId[i]!=newClientsIdList[j]){
				deleteClient(listOfClientsId[i]);
			}else{
				j++;
			}
		}
	}
	function setMarkers(all,varGlobClient){
			if (polyline!=undefined ){
				map.removeLayer(polyline);
				polyline = undefined;
			}
					
					$.post("GetClients",{
						all:all,
						client:varGlobClient
					},function(data){
						if (data.code==0){
							var newClientsIdList=[];
							divNbClients.text(data.clients.length);
							if (data.clients.length==0 && !divNoClient.hasClass("animated") ){	
								/*if (lastTimeNoClientShowed==undefined || (new Date()).getTime()-lastTimeNoClientShowed>TIME_BETWEEN_NO_CLIENT){
									divNoClient.addClass('animated fadeInUp').show();
									lastTimeNoClientShowed = new Date().getTime();
									setTimeout(function(){
										divNoClient.hide().removeClass("animated");
//										setTimeout(function(){
//											divNoClient.removeClass('').hide();
//										},1000);
//										divNoClient.removeClass('fadeInUp').removeClass("animated").addClass('animated fadeOutDown').show()
//										.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',function(){
//											divNoClient.removeClass('animated fadeOutDown').hide();
//										});
									}, TIME_BETWEEN_NO_CLIENT/2);
								}*/
								
							}else{
								var tmpMarker = [];	
								$.each(data.clients,function(index,client){						
									newClientsIdList.push(client.id);
									addClient(client.id, new GPS(client.lat,client.lgn));
								});
							}
							deleteAllAbsentClients(newClientsIdList);
							listOfClientsId=newClientsIdList;
						}
					},"json").fail(function(){
						//TODO SHOW ERROR;
					});
					
			}
	function getHistory(date){
		$.post("GetHistory",{
			client : varGlobClient,
			date: date
		},function(data){
			listLatlgn = [];
			$.each(data.history,function(index,entry){
				listLatlgn.push(L.latLng(entry.lat,entry.lon));
			});	
			if (data.history.length!=0)
				divNbClients.text(1);
			else{
				divNbClients.text(0);
			}
			polyline = L.polyline(listLatlgn, {color: 'red'}).addTo(map);
		},"json").fail(function() {
		// Todo show some error
		  });
	}
	function set_all(activated){
		if (activated==1){
			all = 1;
			$(ID_INPUT_DATE).prop('disabled',true);
		}else{
			all=0;
			$(ID_INPUT_DATE).prop('disabled',false);
		}
	}
	
	function onMapClick(e) {
		popup
			.setLatLng(e.latlng)
			.setContent("Lat,Lon: " + e.latlng.toString())
 			.openOn(map);
 	}
	
	///////////////////////////////////////////////////////Code//////////////////////////////////////////////////
	$.get("GetLatLgn",function(data){
		if (data.code==0){
			minLat = data.minLat;
			minLgn = data.minLgn;
			maxLat = data.maxLat;
			maxLgn = data.maxLgn;
			var southWest = L.latLng(minLat,minLgn ),
		    northEast = L.latLng(maxLat,maxLgn),
		    bounds = L.latLngBounds( southWest,northEast);
			map = L.map('map',{
				maxBounds : bounds,
				center: [(minLat+maxLat)/2, (minLgn+maxLgn)/2],
			    zoom: 15,
			    minZoom:1
			});
			map.fitBounds(bounds);
			L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
				maxZoom: 18,
				id: 'mapbox.streets'
			}).addTo(map);
			map.on('click', onMapClick);
		 	L.polygon([
		 		[minLat,minLgn ],
		 		[minLat,maxLgn ],
		 		[maxLat,maxLgn],
		 		[maxLat,minLgn ]
				
		 	],{
		 		 fillOpacity: 0
		 	}).addTo(map);
			popup = L.popup();
		}else{
			$("#error").show();
		}
	},"json");
	

	
	$(document).ready(function(){
		
		divNoClient = $("div#noclient");
		
		$("#mapContainer").append('<div id="search_client" class="">'+
		'<div class="container_icon"><i class="fa fa-search"></i></div>'+
		'<input placeholder="'+TEXT_SEARCH_CLIENT+'" id="input_client"/>'+
		'</div>');
//		$("#mapContainer").append(
//
//			    '<FORM id="select">'+
//			    '<SELECT name="nom" size="1">'+
//			    '<OPTION id="sans">sans trajet</option>'+
//			    "<OPTION id=\"ajd\" disabled>aujourd'hui</option>"+
//			    '<OPTION id="hier" disabled>hier</option>'+
//			    '</SELECT>'+
//			    '</FORM>');
		$("#mapContainer").append('<form id="select" class="form-horizontal" role="form"><div class="form-group has-feedback"><div class="col-sm-7">'+
		'<input type="text" id="input_date" class="form-control" placeholder="Date"  disabled>'+
		'<span class="fa fa-calendar txt-danger form-control-feedback"></span>'+
		'</div>'+
		'<div class="col-sm-5">'+
		'<div class="checkbox" id="follow">'+
			'<label>'+
				'<input type="checkbox" checked> Follow'+
					'<i class="fa fa-square-o small"></i>'+
			'</label>'+
		'</div>'+
		'</div></div></form>');
		$('#input_date').datepicker({
			setDate: new Date(),
			dateFormat: 'dd/mm/yy' 
		});
		
		setMarkers(all,varGlobClient);
		timer = getClients();
		$(ID_SEARCH_CLIENT).autocomplete({
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
	            $(ID_SEARCH_CLIENT).addClass("extend");
	        },
	        close:function(event,ui){
	        	 $(ID_SEARCH_CLIENT).removeClass("extend");
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
	    }).click(function(e){
	    	$(e.target).attr('placeholder',TEXT_SEARCH_CLIENT);
	    	$(ID_INPUT_DATE).attr('placeholder',TEXT_DATE);
	    });
		
		$(".container_icon").click(function(){
			var e = jQuery.Event("keypress");
			e.which = 13; // # Some key code value
			$(ID_SEARCH_CLIENT).trigger(e);
		});
		var is_first = true;
		$("#follow").click(function(e){
			if (!is_first){
				var n = $( "input:checked" ).length;
				if (n>0){
					//input is checked
					isFollowChecked = true;
				}else{
					isFollowChecked = false;
					disableFollow();
				}
			}
			is_first = !is_first;
		});


		$(ID_INPUT_DATE).change(function(e){
			clearInterval(timer);
			if (polyline!=undefined){
				map.removeLayer(polyline);
				polyline = undefined;
			}
			var JqueryElement = $(e.target);
			var date = JqueryElement.val();
			var today = getTodayString();
			if (date == today || date == ""){
				timer = getClients();
			}else{
				getHistory(date);
			}
			
		});
		$(ID_INPUT_DATE+"+span").click(function(e){
			if ($(ID_INPUT_DATE).prop('disabled')){
				$(ID_INPUT_DATE).attr('placeholder',TEXT_PLEASE_SELECT_CLIENT);
				$(ID_SEARCH_CLIENT).attr('placeholder',TEXT_SELECT_CLIENT);
			}
		});
		
		window.li.click(function(){
			clearInterval(timer);
			$(ID_INPUT_DATE).datepicker({
				setDate: new Date(),
				dateFormat: 'dd/mm/yy' 
			});
		});
			
		
	});
	
});
