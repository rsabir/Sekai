$("#submit").click(function(e) {
	var regexConfig = /^((https?:)(\/\/\/?)([\w]*(?::[\w]*)?@)?([\d\w\.-]+)(?::(\d+))?)?([\/\\\w\.()-]*)?(?:([?][^#]*)?(#.*)?)*/gmi;
	if ($("input#config").val()=="" || regexConfig.test($("input#config").val())==false ){
		$("input#config").parent().parent().addClass("has-error").find("#config").change(function(){
			$("input#config").parent().parent().removeClass("has-error");
		});
		return;
	}
	var regexIp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):\d{1,}$/;
	if ($("input#ip").val()=="" || regexIp.test($("input#ip").val())==false ){
		$("input#ip").parent().parent().addClass("has-error").find("#ip").change(function(){
			$("input#ip").parent().parent().removeClass("has-error");
		});
		return;
	}
	$.post("SetServerSettings",{
		ip : $("input#ip").val(),
		url_config:$("input#config").val()
	},function(data){
		if (data.code==0){
			$("#success").text("Well saved").show();
		}
	});
});
	