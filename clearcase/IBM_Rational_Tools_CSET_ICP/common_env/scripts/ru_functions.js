var mainCourseTitle;
var mainCourseTitleN4 = "";
var nextUrl;
var prevUrl;
var returnUrl;
var breadCrumb;
var Crumb;
var Crumbsup;
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
var umlterm = "";
var currentClass;
var button_sep = "<img src='../common_env/images/button_sep.gif' width='15' height='18' align='absmiddle' border='0'>";
var button_sep2 = "<img src='../common_env/images/button_sep2.gif' width='3' height='18' align='absmiddle' border='0'>";
var previous_live = "<img src='../common_env/images/nav_prev.gif' width='11' height='18' align='absmiddle' border='0'>";
var previous_null = "<img src='../common_env/images/nav_prev_null.gif' width='11' height='18' align='absmiddle' border='0'>";
var next_live = "<img src='../common_env/images/nav_next.gif' width='11' height='18' align='absmiddle' border='0'>";
var next_null = "<img src='../common_env/images/nav_next_null.gif' width='11' height='18' align='absmiddle' border='0'>";;
var return_live = "<img src='../common_env/images/nav_return.gif' width='16' height='18' align='absmiddle' border='0'>";
var next_button;
var prev_button;


	parent.topicMenu.location.href = baseLink + "../common_env/tmenu.html";
	parent.navFooter.location.href = baseLink + "../common_env/footer.html";	

//replace the default ratAppl variable with the product and product version information if it has been filled out
// in the course_nav.js file

if (product != ""){
	ratAppl = "<span class='bold'>" + product + "</span>";
	if (productVer != ""){
		ratAppl = "<span class='bold'>" +product + ", " + productVer+ "</span>";
	}
}

function umlTerm(umlterm){
	parent.navBar.umlterm = umlterm;
	parent.mainContent.location.href = topic_link_1_alt;
}

mainCourseTitle = "<br>" + parent.navBar.courseTitle;

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

function doNothing(){
//mothing happens here
}

function firstInit(){
   setTimeout("doNothing()", 5000); 
	navInit();
   setTimeout("doNothing()", 3000); 
	pageChanged();
}

function pageChanged(){

// this function is called by a Flash movie from the topic menu html file. The flash movie runs continually calling this function to see if the 
// page in the mainContent frame has changed. The purpose of this flash and function was to eliminate the javascript function call to navInit() from the
// individual content html files
	if (parent.mainContent){
		if (parent.mainContent.location.href.indexOf(actPage) > 0){
			// Do Nothing - the page in the mainContent has not changed so there is no need to run the navInit function to update the topic menu
		} else {
			// run NavInit - The page in the mainContent frame has changed - Run the navigation init function to update the topic menu, crumbar, page title, and other navigation links
			parent.navBar.navInit();
		}
	}
  setTimeout("pageChanged()", 750); 
}


//Establish the menu bar

var coursemap = "";
if (button_1.length > 1){
	coursemap = button_1_link + button_1 + "</a>";
	}
	
var menuBar = "";	
if (glossary){
	menuBar += button_2_link + button_2 + "</a>";
	}
if (button_3.length > 1){
	if (glossary){	
		menuBar += button_sep + button_3_link + button_3 + "</a>";
	} else {
		menuBar += button_3_link + button_3 + "</a>";		
	}
}
if (button_4.length > 1){
	menuBar += button_sep + button_4_link + button_4 + "</a>";
	}
if (button_5.length > 1){
	menuBar += button_sep + button_5_link + button_5 + "</a>";
	}
if (button_6.length > 1){
	menuBar += button_sep + button_6_link + button_6 + "</a>";
	}
if (button_7.length > 1){
	menuBar += button_sep + button_7_link + button_7 + "</a>";
	}
if (faqs== true){
	menuBar += button_sep + button_8_link + button_8 + "</a>";
	}

// End Establish the menu bar


//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
// NAV INITIALIZATION FUNCTION
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************

function navInit(){

	// MAIN Navigation Initialization Loop ********************************************************************
	
	// Start NavInit loop from 1 to the total number of lpage array entries (number of pages)
	// note that the lpage array element 0 (zero) is a null element used to test for the start of the array.
	// this makes it easyt to determine the first page by seeing if the array element -1 modNo is set to "start"
	// at which time the previous buttons are set to null.
	
	for (i=1; i<(lpage.length-1); i++){
		
		//  Setting of next/previous null buttons  ********************************************************************
		
		// The array contains additional support pages that are not part of the course definition pages.
		// The variable arrayPages is set to the actual course learning pages by locating the first array elements
		// with the modNo attribute set to "end". Subtracting 1 compensates for this null "end" elements in the page count
		if (lpage[i].modNo == "end"){ arrayPages = (i-1)}
		
		//  if the page url of the page in the mainContent frame match the url of this array elements, then we have located
		// the array element that represents the loaded page - then is the array element before this element has the modNo attribute
		// set to "start" we know that the loaded page is the first page in the course, therfore, we set the previous buttons to null.
		if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i-1].modNo == "start")){

		prev_button = button_sep2 + previous_null + "<span class='deact'>" + button_previous + "</span>";
		next_button = "<a href='#' onClick='parent.navBar.ru_nextPage();return false;'>" + button_next + next_live + "</a>";
		
		// Otherwise, once we have located the array element that represents the loaded page, we test to see if the next array element
		// modNo attribute is set to "end", if so, there are no course pages after this page and we set the next buttons to null
		} else if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i+1].modNo == "end")){

		prev_button = button_sep2 + "<a href='#' onClick='parent.navBar.ru_prevPage();return false;'>" + previous_live + button_previous + "</a>";
		next_button = "<span class='deact'>" + button_next + "</span>" + next_null;
		
		// Otherwise, we check the make sure that the loaded page is not a support page (i.e. modNo attribute is less
		// than "92"), and, if that is the case, we set the next/previous buttons to their normal active state.
		// i.e. what ever page is currently loaded in the mainContent frame, it is not the start or end page, nor is it
		// an environment support page. See below for what happens to the next/previous buttons is the loaded page IS a
		// support page (i.e. modNo attribute set to "92" or higher.
		} else if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i].modNo < "92")){

		prev_button = button_sep2 + "<a href='#' onClick='parent.navBar.ru_prevPage();return false;'>" + previous_live + button_previous + "</a>";
		next_button = "<a href='#' onClick='parent.navBar.ru_nextPage();return false;'>" + button_next + next_live + "</a>";

		}
		// END OF Setting of next/previous null buttons  ********************************************************************

		// MAIN NAVIGATION INITIALIATION COMMANDS for non-Pop-up pages ***********************************
		
		// If this array element url matches the currently loaded page in the mainContent frame AND the page IS NOT 
		// a pop-up page (i.e. pageClass does not equal "P"), then run the commands in this section
		
		if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i].pageClass != "P")){

			// Set the Active Page variable "actPage" to the url (filenname + html) attribute of this array element, ex. "m01_s01_01.html"
			// Set the current page variable "currentPage" to the array element url PLUS the array element path, ex. "module01/m01_s01_01.html"
			// Set the current page variable "currentPath" to the array element path attribute, ex. "module01/"

			actPage = lpage[i].url;	
			currentPage = lpage[i].path + lpage[i].url;
			currentPath = lpage[i].path;
			currentClass = "0";
			
			// Set Return URL and Course Started variables ***************
			
			// If the currently loaded page IS NOT a support page, set the returnUrl variable to the currentPage variable.
			// The returnUrl variable is applied to the "return to course" button that replaces the next/previous buttons
			// when the currently loaded page IS a support page (i.e. modNo equal to or greater than "92"

			if (lpage[i].modNo < "92"){
				
				returnUrl = currentPage;
				
				// Is the currently loaded page is not the first course page "Welcome", set the capture last view
				// variable to true and the started variable to "1" (to indicate that the student has started the course
			 	// by navigating beyond the opening welcome page
				// These variables are used in the cookie functions/commands that capture and store information about
				// the last course page that was viewed in the browser the last time the course was opened. These function
				// are part of the system that gives the student the option of returning to the last viewed learning page
				
				if (lpage[i].url != "welcome.html"){
					captureLV = true;
					started = "1";
				}
				
			}
			// END OF Set Return URL and Course Started variables ***************			

			// Set "Return to Course" button for support pages ******************	

			// If the currently loaded page (mainContent) IS an environment support page, change the next/previous buttons
			// to the "Return to Course" button
			
			if (lpage[i].modNo > "92"){
				
			prev_button = button_sep2 + "<a href='#' onClick='parent.navBar.ru_returnPage();return false;'>" + return_live + button_return + "</a>";
			next_button = "  ";			
			}
			
			// END OF Set "Return to Course" button for support pages ******************				
	
			// Set page data variables from the array element attributes *************	
			// note: some of these variable will be used on the paging routine for the breadcrumb bar below
			moduleNo = lpage[i].modNo;
			sectionNo = lpage[i].secNo;
			moduleTitle = lpage[i].modTitle;
			sectionTitle = lpage[i].secTitle;
			pageTitle = "<span class='pagetitle'>" + lpage[i].pageTitle + "</span>";
			lastPageTitle = lpage[i].pageTitle;
			// END OF Set page data variables from the array element attributes *************		
	
			// Set nextUrl for the standard NEXT button *************************************
			
			// We need to set the nextUrl variable to the path and file url of the next learning page.
			// To do that, we need to determine that the next page element in the array IS NOT for a Pop-up
			// page and also NOT the "end" marker.
			
			if ((lpage[i+1].pageClass != "P") && (lpage[i+1].modNo != "end")){
				
				// If the next page element is not a pop-up page definition and not the "END" marker,
				// set the nextUrl variabel to the path and file name from the repestive attributes of the
				// next array element:
				
				nextUrl = lpage[i+1].path + lpage[i+1].url;

			// Else, if the next array element is a definition for a pop-up page, we need to search ahead in
			// the array for the next array element that IS NOT a definition for a pop-up page. Do to this,
			// we start another loop starting at the index number of the current main navigation loop PLUS 2,
			// Therefore skipping over the next array element to the second next array element.
								
			} else if (lpage[i+1].pageClass == "P"){
				
				// Secondary array loop starting from current index PLUS 2
				
				for (j=i+2; j <= j+30; j++){
					
					// Having skipped over the next array element to the second next array element, we need
					// to determine is the second next element is a pop-up page definition OR the "END" marker.
					// since the wbt environment allows pop-up pages to appear in linear sequence in the array,
					// we need to continue this array until we find the next normal linear learning page
					// We do this for 30 array elements, which, in essence, defines a limitation of 30 pop-up
					// pages in a row in the array
					
					if ((lpage[j].pageClass != "P") && (lpage[j].modNo != "end")){
						
						// Once we find the next normal linear learning page, set that page path and url as the 
						// nextUrl variable for the nedxt button, then break out of this secondary loop
						
						nextUrl = lpage[j].path + lpage[j].url;
						break;
					}
				}
				// End of secondary loop to skip over a pop-up page for the nextUrl variable				
			}
			
			// If the next array element IS the "END" marker (and therefore not a "P" pageClass), set the
			// nextUrl to the currently loaded page. Normally, the next button will be null (and therefore, unclickable)
			// by this time, so this setting is a catchall setting so the variable has something in it
			
			else {
				nextUrl = lpage[i].path + lpage[i].url;
			}
	
			// END OF Set nextUrl for the standard NEXT button *************************************	
	
			// Set prevUrl for the standard PREVIOUS button *************************************			
			
			// See the explanation above for setting the nextUrl for the NEXT button. This set of commands
			// works exactly the same, except in reverse in order to set the proper path and url name
			// into the prevUrl for the PREVIOUS button
			
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
			else {
				prevUrl = lpage[i].path + lpage[i].url;
			}
			// END OF Set prevUrl for the standard PREVIOUS button *************************************	
	
		}
		// END OF MAIN NAVIGATION INITIALIATION COMMANDS for non-Pop-up pages ***********************************		


		// Navigation for POP-UP Pages

		if ((parent.mainContent.location.href.indexOf(lpage[i].url) > 0) && (lpage[i].pageClass == "P")){

			actPage = lpage[i].url;	
			currentPage = lpage[i].path + lpage[i].url;
			currentPath = lpage[i].path;
			currentClass = "P";
	
			prev_button = button_sep2 + "<a href='#' onClick='parent.navBar.ru_returnPage();return false;'>" + return_live + button_return + "</a>";
			next_button = "  ";			

			moduleNo = lpage[i].modNo;
			sectionNo = lpage[i].secNo;
			moduleTitle = lpage[i].modTitle;
			sectionTitle = lpage[i].secTitle;
			pageTitle = "<span class='pagetitle'>" + lpage[i].pageTitle + "</span>";
		}
	}
	// END OF MAIN Navigation Initialization Loop ***************************************************************

	// SET PAGE Numbering for the currently lesson (section) ********************************************************

	// set the page number (of this page) to 0 (zero)
	// Set the number of pages for this lesson (section) to 0 (zero)
	pageNo = 0;
	pageTotal = 0;
	
	// Start a secondary loop through the array to set the page number of this page (within the lesson)
		
	for (j=1; j<(lpage.length-1); j++){
		
		// during the operation of this loop, we are going to count the total number of pages that
		// have modNo and secNo attributes that match the moduleNo and sectionNo variables that were set
		// in the "MAIN NAVIGATION INITIALIATION COMMANDS for non-Pop-up pages" above. We're also not going
		// top count any pop-up page definitions that fall within this module and section.
		
		if ((lpage[j].secNo == sectionNo) && (lpage[j].modNo == moduleNo) && (lpage[j].pageClass != "P")){
			pageTotal = pageTotal + 1;
			
			// while we're counting up the total number of pages in this section (of this module), we're checking
			// to see if the url attribute matches the currently loaded page (in mainContent frame) - if so,
			// set the page number of this page to the page Total as is stands right now. Obviously, this condition
			// should only occur (become true) once while going through the loop.
			// the loop will continue (adding to the page total) as long as the module and section numbers match.
			if ((lpage[j].path + lpage[j].url) == currentPage){
				pageNo = pageTotal;
			}
		}
	}
	
	
		
	if (moduleNo < "80"){
		moduleCount = "";
		//moduleCount = "Module " + moduleNo + ": ";
	} else {
		moduleCount = "";
	}

	if ((moduleNo < "90") && (currentClass != "P")){
		Crumb = "<span class='breadcrumb'>" + moduleCount + moduleTitle + "<img src='../common_env/images/diamond.gif' width='20' height='12' align='absmiddle'>" + sectionTitle + ", &nbsp;&nbsp;&nbsp;<span class='pagecount'>Page " + pageNo + " of " + pageTotal + "</span>";
		Crumbsup = " ";
	} else if ((moduleNo < "90") && (currentClass == "P")){
		Crumbsup = "<span class='breadcrumbsupp'>Supplemental Content <img src='../common_env/images/diamond.gif' width='20' height='12' align='absmiddle'> To resume course, click RETURN: <img src='../common_media/images/yellow-right.gif' width='50' height='12' align='absmiddle'></span>";
		Crumb = " ";
	} else if (moduleNo == "90"){
		Crumb = "<span class='breadcrumb'>" +  moduleTitle  + "</span>";
		Crumbsup = " ";
	} else {
		Crumbsup = "<span class='breadcrumbsupp'>" + moduleTitle + "<img src='../common_env/images/diamond.gif' width='20' height='12' align='absmiddle'> To resume course, click RETURN: <img src='../common_media/images/yellow-right.gif' width='50' height='12' align='absmiddle'></span>";
		Crumb = " ";
	}	


	if (captureLV){
		captureLV = false;
		parent.navBar.getCookie();
		parent.navBar.parseCookie();
		parent.navBar.updateCookie();
	}
	
	
		topicMenu = "<table width='1' border='0' cellpadding='0' cellspacing='0'>";
		topicMenu += "<tr><td>";


		for (i=1; i<=arrayPages; i++){
	
			if (((lpage[i].secNo == "0") && (lpage[i].pageClass != "1")) || ((lpage[i].secNo == "1") && ( lpage[i].modNo == "90"))){
				topicMenu += "<table width='170' border='0' cellpadding='0' cellspacing='0'>";
				menuStyle = "class='tree'";
				bullet = "<img src='images/navbullet-grey.gif' width='12' height='12' border='0'>";
				menuShim = "<img src='../common_media/images/0shim.gif' alt=''  width='1' height='16'>";

				linkContent = "<a href='" +  baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'>" + lpage[i].modTitle + "</a>";
				
				topicMenu += "<tr>";
				topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>";
				topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='150' height='1'></td></tr>";
				topicMenu += "<tr>";

				if (lpage[i].modNo <= 79){
					topicMenu += "<td align='left' valign='top'><span class='mod'>" + lpage[i].modNo + "</span></td>";
				} else {
					topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
				}
				
				//topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";


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
					topicMenu += "<table width='170' border='0' cellpadding='0' cellspacing='0'>";
					topicMenu += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>";
					topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='12' height='1'></td>";
					topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='138' height='1'></td></tr>";
					topicMenu += "<tr><td align='left'>" + menuShim + "</td>";
					topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
					topicMenu += "<td align='left'>" + linkContent + "</td></tr>";
					topicMenu += "</table>";
					norepeat = lpage[i].secNo
				}
			}
		}
	menuShim = "<img src='../common_media/images/0shim.gif' alt=''  width='1' height='16'>";
	topicMenu += "<table width='170' border='0' cellpadding='0' cellspacing='0'>";
	topicMenu += "<tr>";
	topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>";
	topicMenu += "<td><img src='../common_media/images/0shim.gif' alt=''  width='150' height='1'></td></tr>";


	if ((parent.navBar.umlGuide) || (parent.navBar.rupLite)){
	topicMenu += "<tr><td colspan='2'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='15'></td></tr>";
	topicMenu += "<tr><td align='left' colspan='2' class='h3'>" + topic_option_title + "</a></td></tr>";		
	topicMenu += "<tr><td colspan='2'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";
	topicMenu += "<tr><td colspan='2'><img src='../common_env/images/menu_rat.gif' alt=''  width='175' height='5'></td></tr>";
	topicMenu += "<tr><td colspan='2'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='5'></td></tr>";


}
		
	if (parent.navBar.umlGuide){	
	topicMenu += "<tr>";
	topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
	topicMenu += "<td align='left'>" + topic_link_1 + topic_option_1 + "</a></td></tr>";
	}
	if (parent.navBar.rupLite){	
	topicMenu += "<tr>";
	topicMenu += "<td align='left' valign='top'>" + bullet + "</td>";
	topicMenu += "<td align='left'>" + topic_link_2 + topic_option_2 + "</a></td></tr>";
	}	
	
	if ((parent.navBar.umlGuide) || (parent.navBar.rupLite)){
	topicMenu += "<tr><td colspan='2'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='10'></td></tr>";
	}

	topicMenu += "<tr><td colspan='2'><img src='../common_media/images/0shim.gif' alt=''  width='1' height='10'></td></tr>";
	topicMenu += "<tr><td align='left' colspan='2' class='copyright'>" + topic_copy + "</a></td></tr>";		


	
	topicMenu += "</table>";
	topicMenu += "</td></tr></table>";	

// UPDATE THE INFORMATION OBJECTS 
	if (parent.topicMenu.document.getElementById("tmenu")){	
		parent.topicMenu.document.getElementById("tmenu").innerHTML = topicMenu;
	}
	if (parent.navBar.document.getElementById("topNavBack")){
		parent.navBar.document.getElementById("topNavBack").innerHTML = prev_button;	
	}
	if (parent.navBar.document.getElementById("topNavFwd")){
		parent.navBar.document.getElementById("topNavFwd").innerHTML = next_button;	
	}
	
	if (parent.navFooter.document.getElementById("botNavBack")){
		parent.navFooter.document.getElementById("botNavBack").innerHTML = prev_button;
	}
	if (parent.navFooter.document.getElementById("botNavFwd")){
		parent.navFooter.document.getElementById("botNavFwd").innerHTML = next_button;	
	}
	if (parent.navBar.document.getElementById("crumb")){
		parent.navBar.document.getElementById("crumb").innerHTML = Crumb;		
	}
	if (parent.navBar.document.getElementById("crumbsupp")){
		parent.navBar.document.getElementById("crumbsupp").innerHTML = Crumbsup;
	}
	if (parent.navBar.document.getElementById("pagetitle")){
		parent.navBar.document.getElementById("pagetitle").innerHTML = pageTitle;
	}

// END OF UPDATING THE INFOMRATION OBJECTS	
}

//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
// END OF NAV INITIALIZATION FUNCTION
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************
//****************************************************************************************



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
var indent60 = 	"<tr><td><img src='" + shim + "' alt='' width='60' height='3'></td>";
	indent60 += "<td><img src='" + shim + "' alt='' width='430' height='3'></td>";
	indent60 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td>";
	indent60 += "<td><img src='" + shim + "' alt='' width='150' height='3'></td></tr>";	
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

			if (lpage[i].modNo < 80){
				siteMap += "<td><span class='h3'>" + lpage[i].modNo + ".0 " + lpage[i].modTitle + "</span></td>";
			} else {
				siteMap += "<td><span class='h3'>" + lpage[i].modTitle + "</span></td>";
			}
			
			siteMap += "<td>&nbsp;</td><td>&nbsp;</td></tr>";
			siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";
			siteMap += "<td colspan='3' bgcolor='#003366'><img src='" + shim + "' alt='' width='770' height='1'></td></tr>";
			siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='750' height='4'></td></tr></table>";
		}
		if ((lpage[i].secNo > 0) && (norepeat == 0) && (lpage[i].pageClass == "0")  && (lpage[i].pageClass != "1")&& (lpage[i].modNo < 90)){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent20;
			siteMap += "<tr><td>&nbsp;</td>";

			if (lpage[i].modNo < 80){
				siteMap += "<td><span class='h4'>" + lpage[i].modNo + "." +lpage[i].secNo + " " + lpage[i].secTitle + "</span></td>";
			} else {
				siteMap += "<td><span class='h4'>" + lpage[i].secTitle + "</span></td>";
			}
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
		siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent50;
		siteMap += "<tr><td>&nbsp;</td>";	
		siteMap += "<td valign='top'><a href='" + baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'><span class='h5'>" + lpage[i].pageTitle + "</span></a></td>";
		siteMap += "<td valign='top'>&nbsp;" + pageProg + "</td>";
		siteMap += "<td valign='top' align='left'>&nbsp;</span></td><tr>";
		siteMap += "<tr><td><img src='" + shim + "' alt='' width='1' height='1'></td>";			
		siteMap += "<td colspan='3'  bgcolor='#DDDDDD'><img src='" + shim + "' alt='' width='50' height='1'></td></tr>";
		siteMap += "<tr><td colspan='4'><img src='" + shim + "' alt='' width='10' height='1'></td></tr></table>";
		}
		if (lpage[i].pageClass == "P"){
			siteMap += "<table width='790' border='0' cellpadding='0' cellspacing='0'>" + indent60;
			siteMap += "<tr><td>&nbsp;</td>";
			siteMap += "<td valign='top'><a href='" + baseLink + lpage[i].path + lpage[i].url + "' target='mainContent'><span class='h6'>" + lpage[i].pageTitle + "</span></a></td>";
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
function ru_newWindowPop(loc){
	popLoc = baseLink + loc;
	parent.mainContent.location.href=popLoc;
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
	parent.mainContent.location.href = baseLink + "../common_env/common_page/welcome.html";

	firstInit();
}


// -->