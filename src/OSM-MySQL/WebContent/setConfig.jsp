<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
  <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <title> Set the Config</title>
 <style>
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
 	textarea{
 		width:90%;
 		height:50vh;
 		margin-left:auto;
 		margin-right:auto;
 		display:block;
 	}
 	button{
 		background: black;
 		color:white;
 		padding:10px;
 		padding-left:30px;
 		padding-right:30px;
 		display:block;
 		margin-left:auto;
 		margin-right:auto;
 		margin-top:2%;
 	}
 	#error,#success{
 		display:none;
 		margin-top:2%;
 		width:90%;
 		margin-left:auto;
 		margin-right:auto;
 		color:red;
 		text-align:center;
 	}
 	#success{
 		color:green;
 	}
 </style> 
 </head>
<body>
  <h1>Config.json</h1>
  <textarea></textarea>
 <button type="button">SAVE</button>
 <div id="error"></div>
 <div id="success">Well saved</div> 
<!--   <script>tinymce.init({ 
 	  selector: 'textarea'
   });</script> -->
  <script>
  	$.get("/Config",function(data){
  		$("textarea").val(JSON.stringify(JSON.parse(data)),null,4);
  		//$("textarea").val(JSON.stringify(JSON.parse($("textarea").val()),null,4));
  	},"json");
  	$("textarea").change(function(){
  		$("#error").hide();
  		$("#success").hide();
  		try{
			var config = $.parseJSON($("textarea").val());
		}catch(e){
			$("#error").text(e).show();
		}
  	})
	$("button").click(function(){
		try{
			var config = $.parseJSON($("textarea").val());
			$.post("/SetConfig",{
				response:$("textarea").val()
				},function(data){
					if (data.code==0){
						$("#success").text("Well saved").show();
					}else{
						$("#error").text("An error happened, please try later").show();
					}
				},"json");
		}catch(e){
			$("#error").text(e).show();
		}
	});
  </script>
</body>
</html>