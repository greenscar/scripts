document.write("<div id='coursetitle' style='position:absolute; width:600px; height:1px; z-index:1; left: 410px; top: 8px;  border: 1px none #000000;' class='title'>Course Title</div>");
document.write("<div id='purpose' style='position:absolute; width:150px; height:1px; z-index:1; left: 205px; top: 15px;  border: 1px none #000000;' class='purpose'>Site Purpose</div>");
document.write("<div id='menu' style='position:absolute; width:630px; height:1px; z-index:1; left: 205px; top: 48px;  border: 1px none #000000;' class='menu'>Menu Bar</div>");
document.write("<div id='courseMap' style='position:absolute; width:190px; height:1px; z-index:1; left: 5px; top: 72px;  border: 1px none #000000;' class='menu'>Course Map</div>");
document.write("<div id='topNavBack' style='position:absolute; width:75px; height:1px; z-index:1; left: 850px; top: 48px;  border: 1px none #000000; background-color: #000000; layer-background-color: #000000;' class='menu'>previous</div>");
document.write("<div id='topNavFwd' style='position:absolute; width:75px; height:1px; z-index:1; left: 930px; top: 48px;  border: 1px none #000000; background-color: #000000; layer-background-color: #000000;' class='menu' align='right'>next</div>");
document.write("<div id='crumb' style='position:absolute; width:640px; height:1px; z-index:1; left: 205px; top: 72px;  border: 1px none #000000;' class='breadcrumb'>Breadcrumb</div>");
document.write("<div id='crumbsupp' style='position:absolute; width:640px; height:1px; z-index:2; left: 205px; top: 72px;  border: 1px none #000000;' class='breadcrumbsupp'>Support pages</div>");
document.write("<div id='pagetitle' style='position:absolute; width:740px; height:1px; z-index:1; left: 205px; top: 98px; border: 1px none #000000;' class='pagetitle'>Page Title</div>");
document.write("<div id='topicmenu' style='position:absolute; width:190px; height:1px; z-index:1; left: 5px; top: 98px; border: 1px none #000000;' class='pagetitle'>Topic/Lesson Menu</div>");

document.write("<table></table>");

document.getElementById("coursetitle").innerHTML = mainCourseTitle;
document.getElementById("purpose").innerHTML = purpose;
document.getElementById("menu").innerHTML = menuBar;
document.getElementById("courseMap").innerHTML = coursemap;