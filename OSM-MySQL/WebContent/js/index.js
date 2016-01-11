$(document).ready(function() {
  
  var animating = false,
      submitPhase1 = 1100,
      submitPhase2 = 400,
      logoutPhase1 = 800,
      $login = $(".form"),
      $app = $(".app");
  $.post("/Settings",{is_get:1},function(data){
	if (data.code==0){
		$("input#config").val(data.url_config);
		$("input#username").val(data.mysql_username);
		$("input#password").val(data.mysql_password);
		$("input#port").val(data.mysql_port);
		$("input#database").val(data.mysql_database);
	}  
  },"json");
  function ripple(elem, e) {
    $(".ripple").remove();
    var elTop = elem.offset().top,
        elLeft = elem.offset().left,
        x = e.pageX - elLeft,
        y = e.pageY - elTop;
    var $ripple = $("<div class='ripple'></div>");
    $ripple.css({top: y, left: x});
    elem.append($ripple);
  };
  
  $(document).on("click", ".submit_", function(e) {
    if (animating) return;
    animating = true;
    var that = this;
    ripple($(that), e);
    $(that).addClass("processing");
    setTimeout(function() {
    	var regex = /http:\/\/(localhost|[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3})(\:[0-9]+)(\/.*)*/;
    	if ($("input#config").val()=="" || regex.test($("input#config").val())==false ){
    		$(that).removeClass("processing");
    		$("input#config").parent().addClass("error").find("#config").change(function(){
    			$("input#config").parent().removeClass("error");
    			animating = false;
    		});
    		return;
    	}
    	if ($("input#database").val()==""){
    		$(that).removeClass("processing");
    		$("input#database").parent().addClass("error").find("#database").change(function(){
    			$("input#database").parent().removeClass("error");
    			animating = false;
    		});
    		return;
    	}
    	$.post("/Settings",{
    		is_get:0,
    		mysql_port:$("input#port").val(),
    		mysql_username:$("input#username").val(),
    		mysql_password:$("input#password").val(),
    		mysql_database:$("input#database").val(),
    		url_config:$("input#config").val()
    	},function(data){
    		if (data.code==0){
    			$(that).addClass("success");
    		      setTimeout(function() {
    		        $app.show();
    		        $app.css("top");
    		        $app.addClass("active");
    		      }, submitPhase2 - 70);
    		      setTimeout(function() {
    		        $login.hide();
    		        $login.addClass("inactive");
    		        animating = false;
    		        $(that).removeClass("success processing");
    		      }, submitPhase2);		
    		}
    	},"json");
      
    }, submitPhase1);
  });
//  
//  $(document).on("click", ".app__logout", function(e) {
//    if (animating) return;
//    $(".ripple").remove();
//    animating = true;
//    var that = this;
//    $(that).addClass("clicked");
//    setTimeout(function() {
//      $app.removeClass("active");
//      $login.show();
//      $login.css("top");
//      $login.removeClass("inactive");
//    }, logoutPhase1 - 120);
//    setTimeout(function() {
//      $app.hide();
//      animating = false;
//      $(that).removeClass("clicked");
//    }, logoutPhase1);
//  });
  
});