var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ready(function() {
	
	$("#"+window.levelLogs.toUpperCase()).prop("checked",true);
	
	$("#submit").click(function(e) {
		window.levelLogs = $("form.logs input:checked").attr("id");
		$.ajax({
			  type: 'POST',
			  url: "SetLogs",
			  data: { level : window.levelLogs },
			  success: function(data){
					if (data.code==0){
						$("#success").text("Well saved").show();
					}
			  },
			  dataType:"json",
			  beforeSend:function(xhr){
					xhr.setRequestHeader(header, token);
			  }
		});
	});  
});

//The comment bellow is useful to debug the script in Firefox and Chrome Browser 
//(the script is dynamically loaded) : 
//@ sourceURL=logSettings.js
	