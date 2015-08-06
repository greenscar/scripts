var courseDes = " ";
var courseVer = "1.0";
var courseRelDate = "21 June 2005";
var courseTitle = "CSET.PL - Description and Application";
var preReqs = "";
var product = "";
var productVer = "";
var feedBackAddress ="dbellagio@us.ibm.com";
var ext_labs = false;
var umlGuide = false;
var rupLite = false;
var ruProduction = false;
var glossary = false;
var faqs = false;
var evalLinkNo = "2";
var evalIndex = "10";
function courseIndex(modNo, secNo, pageClass ,path, url, modTitle, secTitle, pageTitle){
	this.modNo = modNo
	this.secNo = secNo
	this.pageClass = pageClass
	this.path = path
	this.url = url
	this.modTitle = modTitle
	this.secTitle = secTitle
	this.pageTitle = pageTitle
}
lpage = new Array(10);
var i=0;
lpage[i] = new courseIndex("start","0","0","0","","","","");
i++, lpage[i] = new courseIndex("0", "0", "0", "../common_env/common_page/","welcome.html","About this Intellectual Capital","Welcome","Welcome to an IBM Rational IC Package");
i++, lpage[i] = new courseIndex("0","1","0","module00/","m00_s01_01.html","About this Intellectual Capital","Package Overview","Package Overview");
i++, lpage[i] = new courseIndex("0","2","0","module00/","m00_s01_02.html","About this Intellectual Capital","Author","Author");
i++, lpage[i] = new courseIndex("0","3","0","module00/","m00_s01_03.html","About this Intellectual Capital","General Considerations","General Considerations");

i++, lpage[i] = new courseIndex("1","0","0","module01/","m01_s00_01.html","CSET.PL Basics","What is the Problem?","What is the Problem?");
i++, lpage[i] = new courseIndex("1","1","0","module01/","m01_s01_01.html","CSET.PL Basics","CSET.PL Redo Example","CSET.PL Redo Example");
i++, lpage[i] = new courseIndex("1","2","0","module01/","m01_s01_02.html","CSET.PL Basics","CSET.PL Undo Example","CSET.PL Undo Example");
i++, lpage[i] = new courseIndex("1","3","0","module01/","m01_s01_03.html","CSET.PL Basics","Comments on CSET.PL Usage","Comments on CSET.PL Usage");

i++, lpage[i] = new courseIndex("2","0","0","module02/","m02_s00_01.html","CSET.PL Quick Reference","CSET.PL","CSET.PL");

i++, lpage[i] = new courseIndex("3","0","0","module03/","m03_s00_01.html","Enhancement List and Change History","Enhancement List","Enhancement List");
i++, lpage[i] = new courseIndex("3","1","0","module03/","m03_s00_02.html","Enhancement List and Change History","Change History","Change History");

i++, lpage[i] = new courseIndex("80","0","0","module80/","m80_s00_01.html","Exams and Labs","CSET.PL Lab","CSET.PL Lab");

// END OF COURSE DATA - DO NOT ALTER ANYTHING BELOW THIS LINE
//*************************************************************************************************************************************

i++, lpage[i] = new courseIndex("90","1","1","../common_env/common_page/","conclusion.html","Conclusion","The Next Step","Congratulations");
i++, lpage[i] = new courseIndex("end","0","0","0","","","","");
i++, lpage[i] = new courseIndex("95","1","2","../common_env/common_page/","wbt_help.html","Using Rational\'s WBT","","Instructions and Technical Requirements for Using Rational\'s WBT Programs");
i++, lpage[i] = new courseIndex("99","1","3","resources/","resources.html","Resources","","Additional Resources and Links");
i++, lpage[i] = new courseIndex("99","1","3","../common_env/common_page/","bookmark.html","Bookmarks","","Bookmarked Pages for this Course");
if (parent.navBar.wbt_domain=="ibm"){
i++, lpage[i] = new courseIndex("99","1","3","../common_env/common_page/","contact.html","Contact Us","","Contact Rational University Form");
i++, lpage[i] = new courseIndex("99","1","3","../common_env/common_page/","contact_response.htm","Contact Us","","Contact Rational University Form Thank You");
}
i++, lpage[i] = new courseIndex("99","1","3","../common_env/common_page/","coursemap.html","Course map","","Course site map");
if (glossary){
i++, lpage[i] = new courseIndex("99","1","3","resources/","glossary.html","Glossary","","Glossary");
}
if (faqs){
i++, lpage[i] = new courseIndex("99","1","3","resources/","faqs.html","Faqs","","Frequently Asked Questions");
}
if (umlGuide){
i++, lpage[i] = new courseIndex("99","1","3", topic_link_1_path ,"uml_index.html","UML Guide","","UML 2.0 Quickstart Guide");
}
if (rupLite){
i++, lpage[i] = new courseIndex("99","1","3","../common_content/rup_lite/","rup.html target=&#34;_blank&#34;","RUP Lite","","RUP Lite");
}
if (parent.navBar.wbt_domain=="ibm"){
i++, lpage[i] = new courseIndex("99", "1", "3", "resources/","feedback.html","Feedback","","Course Evaluation Survey");
i++, lpage[i] = new courseIndex("99", "1", "3", "resources/","feedback_response.htm","Feedback","","Course Evaluation Survey Thank You");
}
i++, lpage[i] = new courseIndex("end","0","0","0","","","","");