var ruwin = null;
var screenGood = -1;
var acrobatVersion = -1;
var flashVersion = -1;
var ru_width = -1;
var ru_height = -1;

var spaceLine="<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='3'></td></tr>";
spaceLine += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='4' bgcolor='#AAAAAA'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='1'></td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td></tr>";
spaceLine += "<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='3'></td></tr>";

//*************************************************************************************
// Check Screen Resolution
function checkScreen(){
	if (screen.width > 1){
		ru_width=screen.width;
		ru_height=screen.height;
	}
	return;
}

//**************************************************************************************************8
// Adobe Acrobat Detection
var acrobatVersion = 0;
function getAcrobatVersion() {
	var agent = navigator.userAgent.toLowerCase(); 
	
	// NS3+, Opera3+, IE5+ Mac (support plugin array):  check for Acrobat plugin in plugin array
	if (navigator.plugins != null && navigator.plugins.length > 0) {
      for (i=0; i < navigator.plugins.length; i++ ) {
         var plugin = navigator.plugins[i];
         if (plugin.name.indexOf("Adobe Acrobat") > -1) {
            acrobatVersion = parseFloat(plugin.description.substring(30));
         }
      }
	}
   
	// IE4+ Win32:  attempt to create an ActiveX object using VBScript
	else if (agent.indexOf("msie") != -1 && parseInt(navigator.appVersion) >= 4 && agent.indexOf("win")!=-1 && 

agent.indexOf("16bit")==-1) {
	   document.write('<scr' + 'ipt language="VBScript"\> \n');
	   document.write('on error resume next \n');
		document.write('dim obAcrobat \n');
		document.write('set obAcrobat = CreateObject("PDF.PdfCtrl.5") \n');
		document.write('if IsObject(obAcrobat) then \n');
		document.write('acrobatVersion = 5 \n');
		document.write('else set obAcrobat = CreateObject("PDF.PdfCtrl.1") end if \n');
		document.write('if acrobatVersion < 5 and IsObject(obAcrobat) then \n');
		document.write('acrobatVersion = 4 \n');
		document.write('end if');
		document.write('</scr' + 'ipt\> \n');
  }

	// Can't detect in all other cases
	else {
		acrobatVersion = -1;
	}

	return acrobatVersion;
}


//******************************************************************************************************
// Dtect Flash

var flashVersion = 0;
function getFlashVersion() {
	var agent = navigator.userAgent.toLowerCase(); 
	
   // NS3 needs flashVersion to be a local variable
   if (agent.indexOf("mozilla/3") != -1 && agent.indexOf("msie") == -1) {
      flashVersion = 0;
   }
   
	// NS3+, Opera3+, IE5+ Mac (support plugin array):  check for Flash plugin in plugin array
	if (navigator.plugins != null && navigator.plugins.length > 0) {
		var flashPlugin = navigator.plugins['Shockwave Flash'];
		if (typeof flashPlugin == 'object') { 
			if (flashPlugin.description.indexOf('7.') != -1) flashVersion = 7;
			else if (flashPlugin.description.indexOf('6.') != -1) flashVersion = 6;
			else if (flashPlugin.description.indexOf('5.') != -1) flashVersion = 5;
			else if (flashPlugin.description.indexOf('4.') != -1) flashVersion = 4;
			else if (flashPlugin.description.indexOf('3.') != -1) flashVersion = 3;
		}
	}

	// IE4+ Win32:  attempt to create an ActiveX object using VBScript
	else if (agent.indexOf("msie") != -1 && parseInt(navigator.appVersion) >= 4 && agent.indexOf("win")!=-1 && agent.indexOf("16bit")==-1) {
	   document.write('<scr' + 'ipt language="VBScript"\> \n');
		document.write('on error resume next \n');
		document.write('dim obFlash \n');
		document.write('set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.7") \n');
		document.write('if IsObject(obFlash) then \n');
		document.write('flashVersion = 7 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.6") end if \n');
		document.write('if flashVersion < 7 and IsObject(obFlash) then \n');
		document.write('flashVersion = 6 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.5") end if \n');
		document.write('if flashVersion < 6 and IsObject(obFlash) then \n');
		document.write('flashVersion = 5 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.4") end if \n');
		document.write('if flashVersion < 5 and IsObject(obFlash) then \n');
		document.write('flashVersion = 4 \n');
		document.write('else set obFlash = CreateObject("ShockwaveFlash.ShockwaveFlash.3") end if \n');
		document.write('if flashVersion < 4 and IsObject(obFlash) then \n');
		document.write('flashVersion = 3 \n');
		document.write('end if');
		document.write('</scr' + 'ipt\> \n');
  }
		
	// WebTV 2.5 supports flash 3
	else if (agent.indexOf("webtv/2.5") != -1) flashVersion = 3;

	// older WebTV supports flash 2
	else if (agent.indexOf("webtv") != -1) flashVersion = 2;

	// Can't detect in all other cases
	else {
		flashVersion = -1;
	}

	return flashVersion;
}

checkScreen();
getAcrobatVersion();
getFlashVersion();



document.write("</head><body leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>");
document.write("<img src='../common_env/images/ibm_ru_bg_short.gif' width='800' height='49'>"); 

document.write("<table width='800' border='0' align='left' cellpadding='0' cellspacing='0'>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='30' height='1'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='175' height='1'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='30' height='1'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='15' height='1'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='520' height='1'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='30' height='1'></td></tr>");

document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='8'></td>");
document.write("<td colspan='4' valign='top' class='h1'><img src='../common_env/images/rtl_title.gif' width='113' height='35'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>");
document.write("<td colspan='4' valign='top'><img src='../common_env/images/rtl_webbanner.gif' width='443' height='22' align='absmiddle'><img src='../common_env/images/rtl_brandmark.gif' width='132' height='22' align='absmiddle'></td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
  
document.write("<tr><td valign='bottom'>&nbsp;</td>");
document.write("<td colspan='4' valign='bottom'><span class='titleblack'>" + check_1 + " </span> ");
document.write("<p>" + check_2 + "</td>");
document.write("<td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td>");
document.write("<td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='15'></td></tr>");
  
document.write("<tr><td valign='top'>&nbsp;</td><td valign='top'>" + check_3a + "<br><span class='copyright'>" + check_3b + "</span></td>");

	if (ru_width >= 1024){	 
    document.write("<td valign='middle'><img src='../common_env/images/checkgood.gif' width='30' height='30'></td>");
	document.write("<td valign='middle'>&nbsp;</td>");
    document.write("<td valign='middle'>");
	document.write(check_3c);
	} else if ((ru_width < 1024) && (ru_width != -1)){
    document.write("<td valign='middle'><img src='../common_env/images/x_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='middle'>&nbsp;</td>");
    document.write("<td valign='middle'>");
	document.write(check_3d);
	} else {
	document.write("<td valign='middle'><img src='../common_env/images/caution_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='middle'>&nbsp;</td>");
    document.write("<td valign='middle'>");
	document.write(check_3e);
	}

document.write("</td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
document.write(spaceLine);
	
document.write("<tr><td valign='top'>&nbsp;</td><td valign='top'>" + check_4a + "<br><span class='copyright'> " + check_4b + "</span></td>");

	if (acrobatVersion >= 5){	 
    document.write("<td valign='top'><img src='../common_env/images/checkgood.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='middle'>");
	document.write(check_4c);
	} else if ((acrobatVersion < 5) && (acrobatVersion != -1)){
    document.write("<td valign='top'><img src='../common_env/images/x_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='top'>");
	document.write(check_4d);
	} else {
	document.write("<td valign='top'><img src='../common_env/images/caution_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='top'>");
	document.write(check_4e);
	}

document.write("</td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
document.write(spaceLine);

document.write("<tr><td valign='top'>&nbsp;</td><td valign='top'>" + check_5a + "<br><span class='copyright'>" + check_5b + "</span></td>");
  
	if (flashVersion >= 6){	

    document.write("<td valign='top'><img src='../common_env/images/checkgood.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='middle'>");
	document.write(check_5c);
	} else if ((flashVersion < 6) && (flashVersion != -1)){
    document.write("<td valign='top'><img src='../common_env/images/x_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='top'>");
	document.write(check_5d);
	} else {
	document.write("<td valign='top'><img src='../common_env/images/caution_icon.gif' width='30' height='30'></td>");
	document.write("<td valign='top'>&nbsp;</td>");
    document.write("<td valign='top'>");
	document.write(check_5e);
	}
	
document.write("</td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
document.write(spaceLine);

document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='15'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='4' bgcolor='#99CCFF'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='8'></td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='10'></td></tr>");
 
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='20' height='1'></td>");
document.write("<td colspan='4' valign='top' align='center'><p>" + check_6 + "</p>");
document.write("<p><a href='ru_startcourse.html'>" + check_7 + "</a>");
document.write("</p></td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='30'></td></tr>");
  
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='10'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='4' bgcolor='#99CCFF'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='8'></td><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td></tr>");
document.write("<tr><td><img src='../common_media/images/0shim.gif' alt=''  width='1' height='1'></td><td colspan='5'><img src='../common_media/images/0shim.gif' alt=''  width='500' height='15'></td></tr>");
   
document.write("</table></body>");

if ((ru_width >= 1024) && (flashVersion >= 6) && (acrobatVersion >= 5)){
	document.write("<form name='myform' method=GET action='ru_startcourse.html'>");
	document.myform.submit();

}