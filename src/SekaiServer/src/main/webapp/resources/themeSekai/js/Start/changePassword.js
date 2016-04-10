var listOfInput = "#password"+", "+"#confirmPassword";
var ERROR_MESSAGE_PASSWORDS_DIFFERENT = "The both password given aren't the same";
$("#submit").click(function(e) {
	if ($("input#password").val()==""){
		showError("#password");
		return;
	}
	if ($("input#confirmPassword").val()==""){
		showError("#confirmPassword");
		return;
	}
	if ($("input#password").val()!=$("input#confirmPassword").val()){
		showErrorMessage(ERROR_MESSAGE_PASSWORDS_DIFFERENT);
	}
	$.post("/Start/ChangePassword",{
		password : $("input#password").val(),
		confirmPassword : $("input#confirmPassword").val(),
		},function(data){
		if (data.code==0){
			$("#success").text("Well saved").show();
			$("#second").attr("href","/Start/BasicConfig").addClass("ajax-link").removeClass("disabled").click();
		}
	});
});
function showError(cssSelector){
	$("input" +cssSelector).parent().parent().addClass("has-error").find(cssSelector).change(function(){
		$("input"+cssSelector).parent().parent().removeClass("has-error");
	});
}
function showErrorMessage(message){
	$("#errorMessage").text(message).show();
	$(listOfInput).keydown(function(){
		$("#errorMessage").hide();
	});
}
// The comment bellow is useful to debug the script in Firefox and Chrome Browser 
// (the script is dynamically loaded) : 
//@ sourceURL=changePassword.js
	