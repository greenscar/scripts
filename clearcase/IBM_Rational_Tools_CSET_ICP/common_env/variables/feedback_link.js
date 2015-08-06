// if this is an RU Production course, set the feedback menu bar link to open the feedback form
// otherwise, set the feedback button link to a simple mailto link using the address
// specified in the course parameters. this is only used in the main environment window
// so test to make sure this is the main window and not a pop-up window, since the nav_links.js
// file is also read into the pop-up page window.
if (parent.navBar){
	if ((parent.navBar.ruProduction) && (parent.navBar.wbt_domain == "ibm")){
		button_4_link = "<a href='../common_env/common_page/contact.html' target='mainContent'>";
	} else {
		button_4_link = "<a href='mailto:" + parent.navBar.feedBackAddress + "'>";
	}
}
