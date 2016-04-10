$("#submit").click(function(e) {
	
	if ($("input#username").val()==""){
		showError("#username");
		return;
	}
	if ($("input#port").val()==""){
		showError("#port");
	}
	if ($("input#database").val()==""){
		showError("#database");
	}
	$.post("SetDatabaseSettings",{
		username : $("input#username").val(),
		password:$("input#password").val(),
		port:$("input#port").val(),
		database:$("input#database").val()
	},function(data){
		if (data.code==0){
			$("#success").text("Well saved").show();
		}
	});
});
function showError(cssSelector){
	$("input" +cssSelector).parent().parent().addClass("has-error").find(cssSelector).change(function(){
		$("input"+cssSelector).parent().parent().removeClass("has-error");
	});
}
	