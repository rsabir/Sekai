$("#submit").click(function(e) {
	if ($("input#password").val()==""){
		showError("#password");
		return;
	}
	$.post("SetUser",{
		username : $("input#username").val(),
		password:$("input#password").val(),
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
	