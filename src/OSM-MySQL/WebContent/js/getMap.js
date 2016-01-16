	var minLat;
	var minLgn;
	var maxLat;
	var maxLgn;
	var map;
	var polyline;
	var varGlobClient = [];
	var optionSelected=0;	
	var all=1;
	var divNoClient = $("div#noclient");
	var markers = [];
	var listLatlgn = [];
	var count = 0;
	
	var TIME_REFRESH=1000;
	
	////////////////////////////////////////////////////Functions/////////////////////////////////////////////////////////
	function setMarkers(all,varGlobClient){
		if (window.competition==false || window.competition==undefined){
			window.competition = true;
			if (polyline!=undefined && optionSelected == 0){
				map.removeLayer(polyline);
				polyline = undefined;
			}
			
			// Hack that is very important when it comes to reduce the time of refresh
			if (count<20){
				var marker = markers[markers.length-1];
				if (marker!=undefined){
					for (var j=0 ; j<marker.length ; j++ )
						map.removeLayer(marker[j]);
					count++;
				}
			}else if (count==20){
				$.each(markers,function(index,marker){
					for (var j=0 ; j<marker.length ; j++ )
						map.removeLayer(marker[j]);
				});
				markers=[];
				count=0;
			}
			
			if (optionSelected==0){
					$.post("GetClients",{
						all:all,
						client:varGlobClient
					},function(data){
						if (data.code==0){
							if (data.clients.length==0 && !divNoClient.hasClass("animated") ){	
								divNoClient.addClass('animated fadeInUp').show();
								setTimeout(function(){
									setTimeout(function(){
										divNoClient.removeClass('animated').hide();
									},10000);
									divNoClient.removeClass('fadeInUp').addClass('animated fadeOutDown').show()
									.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',function(){
										divNoClient.removeClass('fadeOutDown').hide();
									});
								}, 3000);
							}
							else{
								var tmpMarker = [];	
								$.each(data.clients,function(index,client){						
									tmpMarker.push( L.marker([client.lat,client.lgn]).bindPopup(client.id).addTo(map) );
								});
								markers.push(tmpMarker);
							}
						}else if (!divNoClient.hasClass("animated") ){
							divNoClient.addClass('animated fadeInUp').show();
							setTimeout(function(){
								setTimeout(function(){
									divNoClient.removeClass('animated').hide();
								},10000);
								divNoClient.removeClass('fadeInUp').addClass('animated fadeOutDown').show()
								.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend',function(){
									divNoClient.removeClass('fadeOutDown').hide();
								});
							}, 3000);
						}
					},"json").fail(function(){
						//TODO SHOW ERROR;
					});
					
			}else if (polyline == undefined){
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
				//    alert( "error" );
				  });
			}
			window.competition = false;
		}
	}
	
	function set_all(activated){
		if (activated==1){
			all = 1;
			$("option#ajd,option#hier").prop('disabled',true);
		}else{
			all=0;
			$("option#ajd,option#hier").prop('disabled',false);
		}
	}
	
	function onMapClick(e) {
		popup
			.setLatLng(e.latlng)
			.setContent("You clicked the map at " + e.latlng.toString())
 			.openOn(map);
 	}
	
	///////////////////////////////////////////////////////Code//////////////////////////////////////////////////
	$.get("/GetLatLgn",function(data){
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
			L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IjZjNmRjNzk3ZmE2MTcwOTEwMGY0MzU3YjUzOWFmNWZhIn0.Y8bhBaUMqFiPrDRW9hieoQ', {
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
			var popup = L.popup();
		}else{
			$("#error").show();
		}
	},"json");
	

	
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
		setMarkers(all,varGlobClient);
		var timer = setInterval(function(){
			setMarkers(all,varGlobClient);
		}, TIME_REFRESH);
		
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
	

