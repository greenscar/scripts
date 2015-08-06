var mainCourseTitle;
var mainCourseTitleN4 = "";
var nextUrl;
var prevUrl;
var returnUrl;
var breadCrumb;
var currentPage;
var actPage;
var moduleNo;
var sectionNo;
var moduleTitle;
var sectionTitle;
var pageTitle;
var pageNo;
var pageTotal;
var cmenu;
var lastView="";
var captureLV = false;
var started = "0";
var norepeat = 0;
var menuShim;
var linkContent;
var arrayPages;
var topicMenu;
var siteMap;
var path;
var feedback;
var currentPath;
var backGround;
var umlterm;

function umlTerm(umlterm){
	parent.navBar.umlterm = umlterm;
	parent.mainContent.location.href = baseLink +  "../common_content/uml_guide/uml_qsg.html";
}



mainCourseTitle = "<p align='right' valign='absmiddle'><span class='title'>" + parent.navBar.courseTitle + "</p>";
mainCourseTitleN4 = "<table width='1000' cellspacing='0' cellpadding='0' border='0'><tr><td><img src='shim' width='1000' height='1'></td></tr>";
mainCourseTitleN4 += "<tr><td align='right'><span class='title'>" + parent.navBar.courseTitle + "</td></tr></table>";

// Set evaluation link for first or second eval index set of number codes.
var evaluation = "";
	if (evalLinkNo == "1"){
		evaluation = evaluationLink_1;
	} else {
		evaluation = evaluationLink_2;
	}

// determine which top frame background to display (rational university of not rational university)
if (parent.navBar.ruProduction){
	backGround = "../common_env/images/ibm_ru_bg.gif";
} else {
	backGround = "../common_env/images/ibm_bg.gif";
}


function pageChanged(){
// this function is called by a Flash movie from the topic menu html file. The flash movie runs continually calling this function to see if the 
// page in the mainContent frame has changed. The purpose of this flash and function was to eliminate the javascript function call to navInit() from the
// individual content html files
	if (parent.mainContent.location.href.indexOf(actPage) > 0){
	// Do Nothing - the page in the mainContent has not changed so there is no need to run the navInit function to update the topic menu
	} else {
		// run NavInit - The page in the mainContent frame has changed - Run the navigation init function to update the topic menu, crumbar, page title, and other navigation links
		parent.navBar.navInit();
	}
}


function navInit(){
	for (i=1; i<(lpage.length-1); i++){
		if (lpage[i].modNo == "end"){ arrayPages = (i-1)}
		if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i-1].modNo == "start")){
		parent.topButton.location.href = baseLink + "../common_env/toplinear_prev_null.html";
		parent.botButton.location.href = baseLink + "../common_env/botlinear_prev_null.html";
		} else if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i+1].modNo == "end")){
		parent.topButton.location.href = baseLink + "../common_env/toplinear_next_null.html";
		parent.botButton.location.href = baseLink + "../common_env/botlinear_next_null.html";
		} else if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i].modNo < "92")){
		parent.topButton.location.href = baseLink + "../common_env/toplinear.html";
		parent.botButton.location.href = baseLink + "../common_env/botlinear.html";
		}
		if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i].pageClass != "P")){
			actPage = lpage[i].url;	
			currentPage = lpage[i].path + lpage[i].url;
			currentPath = lpage[i].path;
			if (lpage[i].modNo < "92"){
				returnUrl = currentPage;
				if (lpage[i].url != "welcome.html"){
					captureLV = true;
					started = "1";
				}
			}
			if (lpage[i].modNo > "92"){
				parent.topButton.location.href = baseLink + "../common_env/topreturn.html";
				parent.botButton.location.href = baseLink + "../common_env/botreturn.html";
			}
			moduleNo = lpage[i].modNo;
			sectionNo = lpage[i].secNo;
			moduleTitle = lpage[i].modTitle;
			sectionTitle = lpage[i].secTitle;
			pageTitle = "<span class='h3'>" + lpage[i].pageTitle + "</span>";
				if ((lpage[i+1].pageClass != "P") && (lpage[i+1].modNo != "end")){
				nextUrl = lpage[i+1].path + lpage[i+1].url;
				}
				else if (lpage[i+1].pageClass == "P"){
					for (j=i+2; j <= j+30; j++){
						if ((lpage[j].pageClass != "P") && (lpage[j].modNo != "end")){
							nextUrl = lpage[j].path + lpage[j].url;
							break;
						}
					}
			}
			else nextUrl = lpage[i].path + lpage[i].url;
			if ((lpage[i-1].pageClass != "P") && (lpage[i-1].modNo != "start")){
				prevUrl = lpage[i-1].path + lpage[i-1].url;
			}
			else if (lpage[i-1].pageClass == "P"){
				for (j=i-1; j >= j-30; j--){
					if ((lpage[j].pageClass != "P") && (lpage[j].modNo != "start")){
						prevUrl = lpage[j].path + lpage[j].url;
						break;
					}
				}
			}
			else prevUrl = lpage[i].path + lpage[i].url;
		}
	}
	pageNo = 0;
	pageTotal = 0;
	for (j=1; j<(lpage.length-1); j++){
		if ((lpage[j].secNo == sectionNo) && (lpage[j].modNo == moduleNo) && (lpage[j].pageClass != "P")){
			pageTotal = pageTotal + 1;
			if ((lpage[j].path + lpage[j].url) == currentPage){
				pageNo = pageTotal;
			}
		}
	}
	if (moduleNo < 90){
		Crumb = "<span class='breadcrumb'>" + moduleTitle + "<img src='../common_env/images/diamond.gif' width='20' height='12' align='absmiddle'>" + sectionTitle + ", &nbsp;&nbsp;&nbsp;</span><span class='pagecount'>Page " + pageNo + " of " + pageTotal + "</span>";
	}
	else {
		Crumb = "<span class='breadcrumb'>" + moduleTitle + "</span>";
	}
	if (document.layers){
		document.coursetitle.document.write(mainCourseTitleN4);
		document.coursetitle.document.close();
	}
	
	if (captureLV){
		captureLV = false;
		parent.navBar.getCookie();
		parent.navBar.parseCookie();
		parent.navBar.updateCookie();
	}
	
	
		topicMenu = "<table width='180' border='0' cellpadding='0' cellspacing='0' bgcolor='#99ccff'>";
		topicMenu += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='180' height='1'></td></tr>";
		topicMenu += "<tr><td>";
		for (i=1; i<=arrayPages; i++){
	
			if (((lpage[i].secNo == "0") && (lpage[i].pageClass != "1")) || ((lpage[i].secNo == "1") && ( lpage[i].modNo == "90"))){
			//if ((lpage[i].secNo == "0") || ((lpage[i].secNo == "1") && ( lpage[i].modNo == 90))){
				topicMenu += "<table width='180' border='0' cellpadding='0' cellspacing='0'>";
				menuStyle = "class='tree'";
				bullet = "<img src='images/navbullet-grey.gif' width='12' height='12' border='0'>";
				menuShim = "<img src='../common_media/images/0shim.gif' alt=''  width='10' height='16'>";
				linkContent = "<a href='" +  baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'>" + lpage[i].modTitle + "</a>";
				topicMenu += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='10' height='1'></td>";
				topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='12' height='1'></td>";
				topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='158' height='1'></td></tr>";
				topicMenu += "<tr><td align='left'>" + menuShim + "</td>";
				topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
				topicMenu += "<td align='left'>" + linkContent + "</td></tr>";
				topicMenu += "</table>";
			}
			
			// create topic menu item for 
			 			
			if ((lpage[i].pageClass != "P") && (lpage[i].pageClass != "1") && (lpage[i].modNo == moduleNo) && (lpage[i].modNo < 90)){
				if ((lpage[i].modNo == moduleNo) && (lpage[i].secNo == sectionNo)){
				menuStyle = "class='treeselected'";
				bullet = "<img src='images/navbullet-smred.gif' width='12' height='12' border='0'>";
				}
				else {
				menuStyle = "class='tree'";
				bullet = "<img src='images/navbullet-smgrey.gif' width='12' height='12' border='0'>";
				}
				if (lpage[i].secNo != norepeat){
					norepeat = 0;
				}
				if (norepeat == 0){

						if (menuStyle == "class='treeselected'"){
							linkContent = "<a href='" +  baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'><span " + menuStyle + ">" + lpage[i].secTitle + "</span></a>";
						} else {
							linkContent = "<a href='" +  baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'>" + lpage[i].secTitle + "</a>";
						}
						
					menuShim = "<img src='../common_media/images/0shim.gif' alt=''  width='20' height='16'>";
					topicMenu += "<table width='180' border='0' cellpadding='0' cellspacing='0'>";
					topicMenu += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>";
					topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='12' height='1'></td>";
					topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='148' height='1'></td></tr>";
					topicMenu += "<tr><td align='left'>" + menuShim + "</td>";
					topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
					topicMenu += "<td align='left'>" + linkContent + "</td></tr>";
					topicMenu += "</table>";
					norepeat = lpage[i].secNo
				}
			}
		}
	menuShim = "<img src='../common_media/images/0shim.gif' alt=''  width='10' height='16'>";
	topicMenu += "<table width='180' border='0' cellpadding='0' cellspacing='0'>";
	topicMenu += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='10' height='30'></td>";
	topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='12' height='1'></td>";
	topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='148' height='1'></td></tr>";


	if ((parent.navBar.umlGuide) || (parent.navBar.rupLite)){
	topicMenu += "<tr><td align='left' colspan='3' class='h3'>" + topic_link_1 + topic_option_title + "</a></td></tr>";		
	topicMenu += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='10'></td></tr>";
	}
		
	if (parent.navBar.umlGuide){	
	topicMenu += "<tr><td align='left'>" + menuShim + "</td>";
	topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
	topicMenu += "<td align='left'>" + topic_link_1 + topic_option_1 + "</a></td></tr>";
	}
	if (parent.navBar.rupLite){	
	topicMenu += "<tr><td align='left'>" + menuShim + "</td>";
	topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
	topicMenu += "<td align='left'>" + topic_link_2 + topic_option_2 + "</a></td></tr>";
	}	
	
	topicMenu += "</table>";
	topicMenu += "</td></tr></table>";	
	
	parent.crumbBar.location.reload();
	parent.topicMenu.location.reload();	
}
// End in NavInit function

var shim = baseLink + "../common_media/images/0shim.gif";
var indent20 = 	"<tr><td><img src='" + shim + "' alt='' width='20' height='3'></td>";
	indent20 += "<td><img src='" + shim + "' alt='' width='470' height='3'></td>";
	indent20 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td>";
	indent20 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td></tr>";
var indent30 = 	"<tr><td><img src='" + shim + "' alt='' width='30' height='3'></td>";
	indent30 += "<td><img src='" + shim + "' alt='' width='460' height='3'></td>";
	indent30 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td>";
	indent30 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td></tr>";
var indent40 = 	"<tr><td><img src='" + shim + "' alt='' width='40' height='3'></td>";
	indent40 += "<td><img src='" + shim + "' alt='' width='450' height='3'></td>";
	indent40 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td>";
	indent40 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td></tr>";
var indent50 = 	"<tr><td><img src='" + shim + "' alt='' width='50' height='3'></td>";
	indent50 += "<td><img src='" + shim + "' alt='' width='440' height='3'></td>";
	indent50 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td>";
	indent50 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td></tr>";
	siteMap =  "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent20;
	siteMap += "<tr><td>&nbsp;</td>";
	siteMap += "<td bgcolor='#CCCCCC'><span class='tree'>&nbsp;" + parent.navBar.map_1 + "</span></td>";
	siteMap += "<td nowrap bgcolor='#CCCCCC'><span class='tree'>&nbsp;" + parent.navBar.map_2 + "</span></td>";
	siteMap += "<td nowrap bgcolor='#CCCCCC'>&nbsp;</td></tr>";
	siteMap += "<tr><td><img src='" + shim + "' alt='' width='10' height='4'></td>";
	siteMap += "<td><img src='" + shim + "' alt='' width='10' height='4'></td>";
	siteMap += "<td><img src='" + shim + "' alt='' width='10' height='4'></td>";
	siteMap += "<td><img src='" + shim + "' alt='' width='10' height='4'></td></tr></table>";
	norepeat = 0;
	for (i=1; i<(lpage.length-1); i++){
		if ((lpage[i].secNo == 0) && (lpage[i].pageClass == "0") && (lpage[i].modNo <90)){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent20;
			siteMap += "<tr><td>&nbsp;</td>";			
			siteMap += "<td><span class='h3'>" + lpage[i].modTitle + "</span></td>";
			siteMap += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
			siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";
			siteMap += "<td colspan='3' bgcolor='#003366'><img src='" + shim + "' alt='' width='770' height='1'></td></tr>";
			siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='750' height='4'></td></tr></table>";
		}
		if ((lpage[i].secNo > 0) && (norepeat == 0) && (lpage[i].pageClass == "0")  && (lpage[i].pageClass != "1")&& (lpage[i].modNo < 90)){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent30;
			siteMap += "<tr><td>&nbsp;</td>";
			siteMap += "<td><span class='h4'>Section: " + lpage[i].secTitle + "</span></td>";
			siteMap += "<td>&nbsp;</td>";
			siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='30' height='6'></td></tr></table>";
		norepeat = lpage[i].secNo;
		}
		if ((lpage[i].pageClass != "P")  && (lpage[i].modNo < 90)){
			pageNo = 0;
			pageTotal = 0;
			for (j=1; j<(lpage.length-1); j++){
				if ((lpage[j].secNo == lpage[i].secNo) && (lpage[j].modNo == lpage[i].modNo) && (lpage[j].pageClass != "P")){
				pageTotal = pageTotal + 1;
					if (lpage[j].url == lpage[i].url){
					pageNo = pageTotal;
					}
				}
			}
		pageProg = "<span class='pagecountblack'>" + parent.navBar.map_3 + " " + pageNo + " " + parent.navBar.map_4 + " " + pageTotal + "</span>";
		siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent40;
		siteMap += "<tr><td>&nbsp;</td>";	
		siteMap += "<td valign='top'><a href='" + baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'><span class='h5'>" + lpage[i].pageTitle + "</span></a></td>";
		siteMap += "<td valign='top'>&nbsp;" + pageProg + "</td>";
		siteMap += "<td valign='top' align='left'>&nbsp;</span></td><tr>";
		siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";			
		siteMap += "<td colspan='3'  bgcolor='#DDDDDD'><img src='" + shim + "' alt='' width='50' height='1'></td></tr>";
		siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='10' height='1'></td></tr></table>";
		}
		if (lpage[i].pageClass == "P"){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent50;
			siteMap += "<tr><td>&nbsp;</td>";
			siteMap += "<td valign='top'><a href='#' onClick='parent.navBar.ru_newWindowPop(\"" + baseLink + lpage[i].path + lpage[i].url + "\")'><span class='h6'>" + lpage[i].pageTitle + "</span></a></td>";
			siteMap += "<td>&nbsp;</td>";
			siteMap += "<td valign='top' align='left'><span class='copyright'>" + parent.navBar.map_5 + "</span></td></tr>";
			siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";
			siteMap += "<td colspan='3' bgcolor='#DDDDDD'><img src='" + shim + "' alt='' width=50' height='1'></td></tr>";
			siteMap += "<tr><td colspam='4'><img src='" + shim + "' alt='' width='10' height='1'></td></tr></table>";
		}
		if (lpage[i+1].secNo == lpage[i].secNo){
			norepeat = lpage[i].secNo;
		} 
		else {norepeat = 0}	
	}
	for (i=1; i<(lpage.length-1); i++){
		if (lpage[i].modNo == 90){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent40;
			siteMap += "<tr><td>&nbsp;</td>";			
			siteMap += "<td><a href='" + baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'><span class='h5'>" + lpage[i].pageTitle + "</span></a></td>";
			siteMap += "<td>&nbsp;</td><td>&nbsp;</span></td></tr>";
			siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";			
			siteMap += "<td colspan='3' bgcolor='#DDDDDD'><img src='" + shim + "' alt='' width='50' height='1'></td></tr>";
			siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='10' height='2'></td></tr></table>";
		}
	}
	siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent20;
	siteMap += "<tr><td>&nbsp;</td>";
	siteMap += "<td><span class='h3'>" + parent.navBar.map_6 + "</span></td>";
	siteMap += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
	siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";
	siteMap += "<td colspan='3' bgcolor='#000000'><img src='" + shim + "' alt='' width='50' height='1'></td></tr>";
	siteMap += "<tr><td colspan='4'><img src='" + shim + "'alt='' width='10' height='2'></td></tr></table>";
	for (i=1; i<(lpage.length-1); i++){
		if (lpage[i].modNo > 90){
		siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent40;
		siteMap += "<tr><td>&nbsp;</td>";	
		siteMap += "<td colspan='2'><a href='" + baseLink + lpage[i].path +  lpage[i].url + "' target='mainContent'><span class='h5'>" + lpage[i].pageTitle + "</span></a></td>";
		siteMap += "<td>&nbsp;</td></tr>";
		siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";
		siteMap += "<td colspan='3'  bgcolor='#DDDDDD'><img src='" + shim + "' alt='' width='50' height='1'></td></tr>";
		siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='10' height='2'></td></tr></table>";			
		}
	}
	siteMap += "<p>";
var maxBM = 10;
var noBookmark;
var bmAdd = false;
var bookmark = new Array(1);
var cookieValue;
var cookieName = parent.navBar.courseDes;
var addNoMore = false;
var canNotMark = false;
var separate = "";
var tableStart = "";
var tableEnd = "";
var displayBookmark = "";
var bmLink = "";
var bmLinkText = "";
var bmDelete = false;
separate = "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='7'></td></tr>";
separate += "<tr><td colspan='3' bgcolor='#AAAAAA'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td></tr>";
separate += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='7'></td></tr>";
tableStart = "<table width='700' border='0' cellspacing='0' cellpadding='0'><tr>";
tableStart += "<td><img src='../common_media/images/0shim.gif' alt=''  width='40'height='1'></td>";
tableStart += "<td><img src='../common_media/images/0shim.gif' alt=''  width='560' height='1'></td>";
tableStart += "<td><img src='../common_media/images/0shim.gif' alt=''  width='100' height='1'></td></tr>";
tableStart += "<tr><td align='center'><b>#</b></td><td><b>" + parent.navBar.bookmark_2a + "</b> " + parent.navBar.bookmark_2b + "</td><td>&nbsp;</td></tr>";
tableStart += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
tableStart += "<tr><td colspan='3' bgcolor='#000000'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='2'></td></tr>";
tableStart += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='10'></td></tr>";
tableEnd = "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='10'></td></tr>";
tableEnd +="<tr><td colspan='3' bgcolor='#000000'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='2'></td></tr></table>";
var exp = new Date();
var expires = exp.getTime() + (365 * 24 * 60 * 60 * 1000);
exp.setTime(expires);
function setCookie(cookieName, cookieValue){
	document.cookie = cookieName + "=" + escape(started + "+") + escape(baseLink + returnUrl + "|") + escape(cookieValue) + "; expires=" + exp.toGMTString();
	if (bmAdd)	{
		bmAdd = false;
	}
}
function getCookie(name){
	var dc = document.cookie; 
	var prefix = name + "="; 
	if (dc.length > 0){ 						
			begin = dc.indexOf(prefix); 		
			if (begin != -1){ 				
				begin += prefix.length;
				end = dc.indexOf(";", begin);
				if (end == -1) end = dc.length;
				return unescape(dc.substring(begin, end));
			}
		}
	return null;
}
function parseCookie(newBookmark){
	getCookie(courseDes);
	bookmark = new Array(1);
	bookmark[0]="";
	var value = unescape(document.cookie);
	var begin = (value.indexOf("|")+1);
	var end = value.indexOf("!");
	for (i=0; i<maxBM; i++){
		if (end == -1) break;
		  	bookmark[i] = value.substring(begin, end);
			begin = end +1;
			end = value.indexOf("!", begin);
			noBookmark = i
	}
	if (newBookmark){
		noBookmark++;
		bookmark[noBookmark] = newBookmark;
	}
}		
function updateCookie(){
	cookieValue = "";
	cookieName = courseDes;
	for (i = 0 ; i < maxBM; i++){
		if (!bookmark[i]) break;
		cookieValue += bookmark[i] + "!";
	}
	setCookie(cookieName, cookieValue);
}
function addBookmark(){
	if (parent.mainContent.location.href.indexOf("bookmark.html") > 0){
		canNotMark = true;
		viewBookmark();	
	} else {
		newBookmark = parent.mainContent.location.href;
		for (i = 1; i < lpage.length; i++){
			if (newBookmark.indexOf(lpage[i].url) > 0){
				newBookmark = lpage[i].url;
				bmAdd = true;
				break;
			}
		}
		parseCookie();
		if (bookmark[0] != ""){
			if (bookmark.length < maxBM){
				parseCookie(newBookmark);
				updateCookie();
				viewBookmark();
			} else {
				addNoMore = true
				viewBookmark();
			}
		} else {
			bookmark[0] = newBookmark;
			updateCookie();
			viewBookmark();			
		}
	} 		
}
function viewBookmark(){
	displayBookmark = "";
	parseCookie();
	if (bookmark[0] != ""){
		displayBookmark += tableStart;
		if (addNoMore){
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			displayBookmark += "<tr><td>&nbsp;</td>";
			displayBookmark += "<td align='center'><font color='#FF0000'><b>" + parent.navBar.bookmark_3a + "<br>" + parent.navBar.bookmark_3b + "</b></font></td>";
            displayBookmark += "<td>&nbsp;</td></tr>";
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			displayBookmark += "<tr><td colspan='3' bgcolor='#FF0000'><img src='../common_media/images/0shim.gif' alt=''  width='3' height='1'></td></tr>";
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			addNoMore = false;
		} else if (canNotMark){
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			displayBookmark += "<tr><td>&nbsp;</td>";
			displayBookmark += "<td align='center'><font color='#FF0000'><b>" + parent.navBar.bookmark_4 + "</b></font></td>";
            displayBookmark += "<td>&nbsp;</td></tr>";
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			displayBookmark += "<tr><td colspan='3' bgcolor='#FF0000'><img src='../common_media/images/0shim.gif' alt=''  width='3' height='1'></td></tr>";
			displayBookmark += "<tr><td colspan='3'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
			canNotMark = false;
		}
		for (i=0; i<maxBM; i++){
			if (bookmark[i]){
				for (j=1; j<lpage.length; j++){
					if (bookmark[i] == lpage[j].url){
						bmLinkText = lpage[j].modTitle + ", " + lpage[j].secTitle + ", " + lpage[j].pageTitle;
						bmLink = baseLink + lpage[j].path + lpage[j].url;
					}
				}
				displayBookmark += "<tr><td align='center' valign='top'>" + (i+1) + "</td>";
				displayBookmark += "<td valign='top'><a href='" + bmLink + "' target='mainContent'>" + bmLinkText + "</a></td>";
                displayBookmark += "<td align='center' valign='top'><a href='#' onClick='parent.navBar.deleteBM(" + i + ");'>[ " + parent.navBar.bookmark_5 + " ]</a></td></tr>";
				if (bookmark[i+1]){
					displayBookmark += separate;
				}			
			}
		}
	displayBookmark += tableEnd;
		if (!bmDelete){
			parent.mainContent.location.href = baseLink + "../common_env/common_page/bookmark.html";
		} else {
			parent.mainContent.location.href = baseLink + "../common_env/common_page/bookmark.html";
			bmDelete = false;
		}
	} else if (bookmark[0] == ""){
		displayBookmark += tableStart;
		displayBookmark += "<tr><td align='center'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='50'></td>";
		displayBookmark += "<td valign='center'>" + parent.navBar.bookmark_6 + "</td>";
        displayBookmark += "<td>&nbsp;</td></tr>";
		displayBookmark += tableEnd;
		if (!bmDelete){
			parent.mainContent.location.href = baseLink + "../common_env/common_page/bookmark.html";
		} else {
			parent.mainContent.location.href = baseLink + "../common_env/common_page/bookmark.html";
			bmDelete = false;
		}
	}
}
function deleteBM(idx){
	for (i = idx; i < maxBM; i++){
		if (bookmark[i+1]){
			bookmark[i] = bookmark[i+1];
		} else {
			bookmark[i] = null;
		}
	}
	bmAdd = false;
	bmDelete = true;
	updateCookie();
	parseCookie();
	viewBookmark();
}


function ru_newWindowPop(loc) {
  var browser = navigator.userAgent;
  if ((parent.mainContent.location.href.indexOf("coursemap.html") < 1) && (browser.indexOf('MSIE') > 0)) {
}
	var offsetParams = getOffsetParams(820,620,15,5);
	var wParams = "height=620,width=820," + offsetParams  + "channelmode=0,dependent=0,directories=0,fullscreen=0,location=0,menubar=0,resizable=0,scrollbars=yes,status=0,toolbar=0";
	mySubWin = window.open( loc, "mySubWin", wParams);        
	setTimeout("setOpener();",200);
}
function setOpener() {
	if  (typeof mySubWin == 'object' ) {
		if (typeof mySubWin.opener == 'object' )  {mySubWin.opener = self};
	} 
 }                               
function comments() {
	window.open(feedBack);
}
function ru_prevPage(){
	parent.mainContent.location.href = baseLink +  prevUrl;
}
function ru_nextPage(){
	parent.mainContent.location.href = baseLink +  nextUrl;
}
function ru_returnPage(){
	parent.mainContent.location.href = baseLink +  returnUrl;
}
function startCourse(){
	if (parent.navBar.getCookie(courseDes)){
		parseCookie()
		lastView = unescape(document.cookie);
		var sbegin = (lastView.indexOf("=")+1);
		var send = lastView.indexOf("+");
		started = lastView.substring(sbegin, send);
		var vbegin = (lastView.indexOf("+")+1);
		var vend = lastView.indexOf("|");
		lastView = lastView.substring(vbegin, vend);
	}
	parent.navFooter.location.href = baseLink + "../common_env/footer.html";
	parent.topicMenu.location.href = baseLink + "../common_env/tmenu.html";	
	parent.mainContent.location.href = baseLink + "../common_env/common_page/welcome.html";
}

// -->