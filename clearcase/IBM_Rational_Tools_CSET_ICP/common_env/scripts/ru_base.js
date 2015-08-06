var urlLink=unescape(parent.location.href);
var indexStart=urlLink.indexOf("ru_startcourse.html");
var baseLink=urlLink.substring(0,indexStart);
	if (urlLink.indexOf("ibm.com") > -1){
		wbt_domain="ibm";
	} else {
		wbt_domain="foreign";
	}
