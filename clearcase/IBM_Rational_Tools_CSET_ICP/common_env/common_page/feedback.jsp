<%@ page session="false" import="java.util.*,java.io.*,javax.mail.*,java.net.*,javax.mail.internet.*,javax.activation.*" %>

<%!
private static String getParam(HttpServletRequest request, String param) {
  if (request.getParameter(param) == null)
    return "";
  else
    return request.getParameter(param).trim();
}

private static String getAttr(HttpSession session, String param) {
  if (session.getAttribute(param) == null)
    return "";
  else
    return (String)session.getAttribute(param);
}

// booleans to control what this page does

boolean email = false;
boolean error = false;
boolean success = false;

if (request.getParameter("email") != null && request.getParameter("email").equals("true")) {
	email = true;
} else { 
	email = false;
}

if (request.getParameter("success") != null && request.getParameter("success").equals("true")) {
	success = true;
} else { 
	success = false;
}

String QUESTIONNAIREID = getParam(request, "QUESTIONNAIREID");
String QUESTIONNAIRENAME = getParam(request, "QUESTIONNAIRENAME");
String RESPONSEID = getParam(request, "RESPONSEID");
String ID65000020 = getParam(request, "ID65000020");
String ID68000020 = getParam(request, "ID68000020");
String ID69000020 = getParam(request, "ID69000020");
String ID70000020 = getParam(request, "ID70000020");
String ID71000020 = getParam(request, "ID71000020");
String ID72000020 = getParam(request, "ID72000020");
String ID73000020 = getParam(request, "ID73000020");
String ID74000020 = getParam(request, "ID74000020");
String ID75000020 = getParam(request, "ID75000020");
String ID76000020 = getParam(request, "ID76000020");
String ID77000020 = getParam(request, "ID77000020");
String ID78000020 = getParam(request, "ID78000020");
String ID79000020 = getParam(request, "ID79000020");
String ID81000020 = getParam(request, "ID81000020");
String ID82000020 = getParam(request, "ID82000020");
String ID83000020 = getParam(request, "ID83000020");
String ID84000020 = getParam(request, "ID84000020");
String ID85000020 = getParam(request, "ID85000020");
String ID11900020 = getParam(request, "ID11900020");
String ID86000020 = getParam(request, "ID86000020");
String ID87000020 = getParam(request, "ID87000020");
String ID88000020 = getParam(request, "ID88000020");
String ID90000020 = getParam(request, "ID90000020");
String ID91000020 = getParam(request, "ID91000020");
String ID12100020 = getParam(request, "ID12100020");
String ID92000020 = getParam(request, "ID92000020");
String ID94000020 = getParam(request, "ID94000020");
String ID95000020 = getParam(request, "ID95000020");
String ID96000020 = getParam(request, "ID96000020");
String ID97000020 = getParam(request, "ID97000020");
String ID98000020 = getParam(request, "ID98000020");
String ID10000020 = getParam(request, "ID10000020");
String ID10100020 = getParam(request, "ID10100020");
String ID10200020 = getParam(request, "ID10200020");
String ID10300020 = getParam(request, "ID10300020");
String ID10400020 = getParam(request, "ID10400020");
String ID10500020 = getParam(request, "ID10500020");
String ID10600020 = getParam(request, "ID10600020");

// if the URL is empty, fill it with the referring page
if (url.equals("") && request.getHeader("referer") != null)
{
   url = request.getHeader("referer");
}

if (email)
{
   // email code, put stuff in session

   try {
 
       // hack so that this code will work on swigy or 106
 
       String host = "swg3ws018.southbury.usf.ibm.com";
       String server = new String(HttpUtils.getRequestURL(request));

       if (server != null && server.indexOf("136") < 0)
       		host = "relay.us.ibm.com";

       Properties props = new Properties();
       props.put("mail.smtp.host", host);
       Session _session = Session.getDefaultInstance(props, null);
       Message msg = new MimeMessage(_session);

       StringBuffer messageBuffer = new StringBuffer();

 
       messageBuffer.append("QUESTIONNAIREID = " + QUESTIONNAIREID + "\n"); 
       messageBuffer.append("QUESTIONNAIRENAME = " + QUESTIONNAIRENAME + "\n"); 	   
       messageBuffer.append("RESPONSEID = " + RESPONSEID + "\n"); 	   
       messageBuffer.append("ID65000020 = " + ID65000020 + "\n"); 	   
       messageBuffer.append("ID68000020 = " + ID68000020 + "\n");  	   
	   messageBuffer.append("ID69000020 = " + ID69000020 + "\n");  	   
	   messageBuffer.append("ID70000020 = " + ID70000020 + "\n"); 
	   messageBuffer.append("ID71000020 = " + ID71000020 + "\n"); 
	   messageBuffer.append("ID72000020 = " + ID72000020 + "\n"); 
	   messageBuffer.append("ID73000020 = " + ID73000020 + "\n"); 
	   messageBuffer.append("ID74000020 = " + ID74000020 + "\n"); 
	   messageBuffer.append("ID75000020 = " + ID75000020 + "\n"); 
	   messageBuffer.append("ID76000020 = " + ID76000020 + "\n"); 
	   messageBuffer.append("ID77000020 = " + ID77000020 + "\n"); 
	   messageBuffer.append("ID78000020 = " + ID78000020 + "\n"); 
	   messageBuffer.append("ID79000020 = " + ID79000020 + "\n"); 
	   messageBuffer.append("ID81000020 = " + ID81000020 + "\n"); 	   	   	   	   	   	   	   	   		      
	   messageBuffer.append("ID82000020 = " + ID82000020 + "\n");
	   messageBuffer.append("ID83000020 = " + ID83000020 + "\n");
	   messageBuffer.append("ID84000020 = " + ID84000020 + "\n");
	   messageBuffer.append("ID85000020 = " + ID85000020 + "\n");
	   messageBuffer.append("ID11900020 = " + ID11900020 + "\n");
	   messageBuffer.append("ID86000020 = " + ID86000020 + "\n");
	   messageBuffer.append("ID87000020 = " + ID87000020 + "\n");
	   messageBuffer.append("ID88000020 = " + ID88000020 + "\n");
	   messageBuffer.append("ID90000020 = " + ID90000020 + "\n");	  	   	   	   	   	   	   	   	   
	   messageBuffer.append("ID91000020 = " + ID91000020 + "\n");
	   messageBuffer.append("ID12100020 = " + ID12100020 + "\n");		   
	   messageBuffer.append("ID92000020 = " + ID92000020 + "\n");		   
	   messageBuffer.append("ID94000020 = " + ID94000020 + "\n");	
	   messageBuffer.append("ID95000020 = " + ID95000020 + "\n");	
	   messageBuffer.append("ID96000020 = " + ID96000020 + "\n");	
	   messageBuffer.append("ID97000020 = " + ID97000020 + "\n");	
	   messageBuffer.append("ID98000020 = " + ID98000020 + "\n");	
	   messageBuffer.append("ID10000020 = " + ID10000020 + "\n");		   	   	   	   	   	   
	   messageBuffer.append("ID10100020 = " + ID10100020 + "\n");	
	   messageBuffer.append("ID10200020 = " + ID10200020 + "\n");	
	   messageBuffer.append("ID10300020 = " + ID10300020 + "\n");	
	   messageBuffer.append("ID10400020 = " + ID10400020 + "\n");	
	   messageBuffer.append("ID10500020 = " + ID10500020 + "\n");	
	   messageBuffer.append("ID10600020 = " + ID10600020 + "\n");		   	   	   	   	   	   
 
       msg.setText(messageBuffer.toString());
       msg.setFrom(new InternetAddress("pdbarley@us.ibm.com"));
       msg.setSubject("Rational WBT Evaluation ");


       msg.setRecipient(Message.RecipientType.TO, new InternetAddress("pdbarley@us.ibm.com"));
       Transport.send(msg);
 
   } 
 
   // redirect to feedback and flag success as true
   response.sendRedirect("feedback.jsp?success=true&url=" + URLEncoder.encode(url));
   return; // return to stop the JSP from processing this request

} // end if submittal email attempted

// If successful email send
else if (success)
{

%>

<html>
<!-- DW6 -->
<head>
<title>IBM Rational software online training</title>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="Description" content="IBM Rational software online training"/>
<meta name="Abstract" content="IBM Rational software online training"/>
<meta name="Keywords" content="ibm, international business machines, rational, rational sofware, ibm training, rational training, rational university, clearcase, clearquest, rup, rational uinified process, software modeling, software development, xde, rational rose, xde tester, software configuration management"/>
<meta name="Owner" content="ref@ibm.us.com"/>
<meta name="Robots" content="noindex,nofollow"/>
<meta name="Security" content="private"/>
<meta name="Source" content="IBM Rational sofware - Rational University"/>
<meta name="IBM.Country" content="US"/>
<meta name="DC.Date" scheme="iso8601" content="2004-01-30"/>
<meta name="DC.Language" scheme="rfc1766" content="en-US"/>
<meta name="DC.Rights" content="Copyright (c) 2004 by IBM Corporation"/>
<meta name="DC.Type" scheme="IBM_ContentClassTaxonomy" content="ZZ999"/>
<meta name="DC.Subject" scheme="IBM_SubjectTaxonomy" content="ZZ999"/>
<meta name="DC.Publisher" content="IBM Corporation - Rational software"/>
<meta name="IBM.Effective" scheme="W3CDTF" content="2004-01-30"/>
<meta name="IBM.Industry" scheme="IBM_IndustryTaxonomy" content="ZZ"/>
<meta http-equiv="PICS-Label" content='(PICS-1.1 "http://www.icra.org/ratingsv02.html" l gen true r (cz 1 lz 1 nz 1 oz 1 vz 1) "http://www.rsac.org/ratingsv01.html" l gen true r (n 0 s 0 v 0 l 0) "http://www.classify.org/safesurf/" l gen true r (SS~~000 1))' />

<link rel='stylesheet' href='../../common_env/styles/ru_style.css' type='text/css'>


</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<p>Thank you for taking the time to submit your evaulation!  Your comments and suggestions are essential in shaping the future of out courseware.</p>
<p>Please use the Topic menu or click the "Return to course" button to resume the course.</p>



<%
}  // end if successful email send

// initial load - email not submitted or sent, so show the main email-it page
else
{
%>


<html>
<!-- DW6 -->
<head>
<title>IBM Rational software online training</title>
<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>
<meta name="Description" content="IBM Rational software online training"/>
<meta name="Abstract" content="IBM Rational software online training"/>
<meta name="Keywords" content="ibm, international business machines, rational, rational sofware, ibm training, rational training, rational university, clearcase, clearquest, rup, rational uinified process, software modeling, software development, xde, rational rose, xde tester, software configuration management"/>
<meta name="Owner" content="ref@ibm.us.com"/>
<meta name="Robots" content="noindex,nofollow"/>
<meta name="Security" content="private"/>
<meta name="Source" content="IBM Rational sofware - Rational University"/>
<meta name="IBM.Country" content="US"/>
<meta name="DC.Date" scheme="iso8601" content="2004-01-30"/>
<meta name="DC.Language" scheme="rfc1766" content="en-US"/>
<meta name="DC.Rights" content="Copyright (c) 2004 by IBM Corporation"/>
<meta name="DC.Type" scheme="IBM_ContentClassTaxonomy" content="ZZ999"/>
<meta name="DC.Subject" scheme="IBM_SubjectTaxonomy" content="ZZ999"/>
<meta name="DC.Publisher" content="IBM Corporation - Rational software"/>
<meta name="IBM.Effective" scheme="W3CDTF" content="2004-01-30"/>
<meta name="IBM.Industry" scheme="IBM_IndustryTaxonomy" content="ZZ"/>
<meta http-equiv="PICS-Label" content='(PICS-1.1 "http://www.icra.org/ratingsv02.html" l gen true r (cz 1 lz 1 nz 1 oz 1 vz 1) "http://www.rsac.org/ratingsv01.html" l gen true r (n 0 s 0 v 0 l 0) "http://www.classify.org/safesurf/" l gen true r (SS~~000 1))' />

<link rel='stylesheet' href='../../common_env/styles/ru_style.css' type='text/css'>


</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="javascript">
function sendPressed() {

    document.feedbackForm.email.value=true;
    document.feedbackForm.submit();
    return false;
}

//-->
</script>

	<table width="800" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><img src="../../common_media/images/0shim.gif" alt="" width="30" height="10"></td>
          <td><img src="../../common_media/images/0shim.gif" alt="" width="740" height="1"></td>
          <td><img src="../../common_media/images/0shim.gif" alt="" width="30" height="1"></td>
        </tr>
        <tr>
          <td><img src="../../common_media/images/0shim.gif" alt="" width="30" height="30"></td>
          <td><p><img src="../../common_media/images/rational_mosaic.jpg" width="598" height="21"></p>
            <p>Course Evaluation for Course "<strong>
                <script language="javascript">document.write(parent.navBar.courseDes)</script>
          ,</strong> <!-- #BeginLibraryItem "/Library/course_title.lbi" -->
            <script language="javascript">document.write("<span class='titleblack'>" + parent.navBar.courseTitle + "</span>");</script>
                <!-- #EndLibraryItem -->"</p></td>
          <td></td>
        </tr>	
	 	<tr>
          <td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="30" height="10"></td>

        </tr>	
		<form action="feedback.jsp"  method="post" name="feedbackForm">
		<input type="hidden" name="QUESTIONNAIREID" value="ID10000020108145312">
		<input type="hidden" name="QUESTIONNAIRENAME" value="WBT Course Evaluation Survey">
		<input type="hidden" name="RESPONSEID" value="ID61152820041305661">
		<script language="javascript">document.write("<input type='hidden' name='ID65000020' value=" + parent.navBar.evalIndex + ">")</script>
        <tr>
          <td><img src="../../common_media/images/0shim.gif" alt="" width="30" height="30"></td>
          <td valign="top"><table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="640" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="50" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="50" height="1"></td>
            </tr> <tr>
              <td colspan="3" class="h3">
Student Preparation:
			</td>
            </tr>

<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="3" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="3" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
	            <tr>
              <td>&nbsp;</td>
              <td align="center">Yes</td>
              <td align="center">No</td>	

            </tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="5"></td></tr>			
			
            <tr>
              <td>Did you read the Course Description before enrolling?&nbsp;&nbsp;</td>
              <td align="center"><input type="radio" name="ID68000020" value="1"></input></td>
              <td align="center"><input type="radio" name="ID68000020" value="2"></input></td>

            </tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			

            <tr>
              <td>Do you meet the requirements (prerequisities) for this course?&nbsp;&nbsp;</td>
              <td align="center"><input type="radio" name="ID69000020" value="1"></input></td>
              <td align="center"><input type="radio" name="ID69000020" value="2"></input></td>

            </tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>Have you taken a Rational University web-based training course
              before 
              this training?&nbsp;&nbsp;</td>
              <td align="center"><input type="radio" name="ID70000020" value="1"></input></td>
              <td align="center"><input type="radio" name="ID70000020" value="2"></input></td>
            </tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>Have you taken a Rational University instructor-led training
              course before 
              this training?&nbsp;&nbsp;</td>
              <td align="center"><input type="radio" name="ID71000020" value="1"></input></td>
              <td align="center"><input type="radio" name="ID71000020" value="2"></input></td>
            </tr>
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="3"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
          </table></td>
          <td></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td valign="top">
		  <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="440" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="300" height="1"></td>
            </tr>
            <tr>
              <td class="h3" colspan="2">
Student Details:</td>
            </tr>

<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
			
            <tr>
              <td>What is your primary job responsibility?&nbsp;&nbsp;</td>
              <td><input type="text" name="ID72000020"  value="" size="30" maxlength="60"></input></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>How many years have you been in your current
              position?&nbsp;&nbsp;</td>
              <td><select name="ID73000020" size="1"><option value="5">&lt; 2 years</option>
<option value="4">3-5 years</option>
<option value="3">6-10 years</option>
<option value="2">11-15 years</option>
<option value="1">Over 16 years</option>

</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>How many people are employed at your company?&nbsp;&nbsp;</td>
              <td><select name="ID74000020" size="1"><option value="5">1-250 people</option>
<option value="4">251-1000 people</option>
<option value="3">1001-5000 people</option>
<option value="2">5001-10000 people</option>
<option value="1">&gt; 10001 people</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
          </table></td>
          <td></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td valign="top">
		  <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="440" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="300" height="1"></td>
            </tr>
            <tr><td colspan="2" class="h3">
Learning Details:
			</td></tr>

<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
			
            <tr>
              <td>What is the method of learning that you consider the easiest for you?</td>
              <td><select name="ID75000020" size="1"><option value="5">Working in small groups</option>
<option value="4">Interacting w/computer</option>
<option value="3">Repetition</option>
<option value="2">Discussion</option>

<option value="1">Q &amp; A</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>What type of learning format do you prefer?</td>
              <td><select name="ID76000020" size="1"><option value="5">Classroom based</option>
<option value="4">Web based</option>
<option value="3">Self-paced</option>
<option value="2">Live online</option>
<option value="1">Tutorial</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>What is the maximum length of a good web-based training module?</td>
              <td><select name="ID77000020" size="1"><option value="3">10 minutes</option>
<option value="2">30 minutes</option>
<option value="1">1 hour</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>How long should you have access to complete a web-based training course?</td>
              <td><select name="ID78000020" size="1"><option value="5">30 days</option>
<option value="4">60 days</option>
<option value="3">90 days</option>
<option value="2">180 days</option>

<option value="1">360 days</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>What is the maximum time you can be away from your job to attend a classroom based training course?</td>
              <td><select name="ID79000020" size="1"><option value="5">1 day</option>
<option value="4">2 days</option>
<option value="3">3 days</option>
<option value="2">4 days</option>
<option value="1">5 days</option>
</select></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			

          </table>		  
		  
		  </td>
          <td></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td valign="top">
		  		  <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="440" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
            </tr>
            <tr>
              <td colspan="6" class="h3">
General Course Content:</td>
            </tr>

<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="6" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="6" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr>
              <td>&nbsp;</td>
              <td align="center" valign="middle" class="copyright">Strongly<br>
            Agree</td>
              <td align="center" valign="middle" class="copyright">Agree</td>
              <td align="center" valign="middle" class="copyright">Indifferent</td>
              <td align="center" valign="middle" class="copyright">Disagree</td>
              <td align="center" valign="middle" class="copyright">Strongly<br>
            Disagree</td>			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			

<tr>
              <td>The text-based portion of this course was clearly worded.</td>
              <td align="center"><input type="radio" name="ID81000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID81000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID81000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID81000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID81000020" value="1"></input></td>			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
	<tr>
              <td>I need information at a greater depth than is presented in this course.</td>
              <td align="center"><input type="radio" name="ID82000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID82000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID82000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID82000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID82000020" value="1"></input></td>			  			  
			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I can successfully apply what I have learned in this course.</td>
              <td align="center"><input type="radio" name="ID83000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID83000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID83000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID83000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID83000020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I will use the course materials as a reference in the future.</td>
              <td align="center"><input type="radio" name="ID84000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID84000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID84000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID84000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID84000020" value="1"></input></td>			  			  
	  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>The course was too long.</td>
              <td align="center"><input type="radio" name="ID85000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID85000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID85000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID85000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID85000020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>The exercises were helpful.</td>
              <td align="center"><input type="radio" name="ID11900020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID11900020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID11900020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID11900020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID11900020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I had enough time to complete the lab exercises (if any).</td>
              <td align="center"><input type="radio" name="ID86000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID86000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID86000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID86000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID86000020" value="1"></input></td>			  			  
			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>The graphics and self checks were clear.</td>
              <td align="center"><input type="radio" name="ID87000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID87000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID87000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID87000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID87000020" value="1"></input></td>			  			  
			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>The scenarios helped clarify the course materials.</td>
              <td align="center"><input type="radio" name="ID88000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID88000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID88000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID88000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID88000020" value="1"></input></td>			  			  
  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I would like to see more detailed text-based explanations.</td>
              <td align="center"><input type="radio" name="ID90000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID90000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID90000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID90000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID90000020" value="1"></input></td>			  			  
			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I completed all the modules in the course.</td>
              <td align="center"><input type="radio" name="ID91000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID91000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID91000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID91000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID91000020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			

<tr>
              <td>The course objectives were clear.</td>
              <td align="center"><input type="radio" name="ID12100020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID12100020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID12100020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID12100020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID12100020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
          </table>		  </td>
          <td></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td valign="top">
		   <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="100" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="640" height="1"></td>
            <tr>
              <td valign="top">Comments:</td>
              <td align="left"><textarea name="ID92000020" style="width: 600" rows="4"  cols="200"></textarea></td>
            </tr>
			<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
			<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
			<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
          </table></td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td valign="top">
<table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="440" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="60" height="1"></td>
            </tr>
            <tr>
              <td colspan="6" class="h3">
			  
			  Overall Satisfaction:
			  </td>
            </tr>

<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="6" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="6" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr>
              <td>&nbsp;</td>
              <td align="center" valign="middle" class="copyright">Strongly<br>
            Agree</td>
              <td align="center" valign="middle" class="copyright">Agree</td>
              <td align="center" valign="middle" class="copyright">Indifferent</td>
              <td align="center" valign="middle" class="copyright">Disagree</td>
              <td align="center" valign="middle" class="copyright">Strongly<br>
            Disagree</td>			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			

<tr>
              <td>I will recommend this course to others.</td>
              <td align="center"><input type="radio" name="ID94000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID94000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID94000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID94000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID94000020" value="1"></input></td>			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
	<tr>
              <td>This course held my interest.</td>
              <td align="center"><input type="radio" name="ID95000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID95000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID95000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID95000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID95000020" value="1"></input></td>			  			  
			  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I will be taking web-based training from Rational University in the future.</td>
              <td align="center"><input type="radio" name="ID96000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID96000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID96000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID96000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID96000020" value="1"></input></td>			  			  
		  			  
            </tr>
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr>
              <td>I will be taking instructor-led training from Rational University in the future.</td>
              <td align="center"><input type="radio" name="ID97000020" value="5"></input></td>
              <td align="center"><input type="radio" name="ID97000020" value="4"></input></td>
              <td align="center"><input type="radio" name="ID97000020" value="3"></input></td>
              <td align="center"><input type="radio" name="ID97000020" value="2"></input></td>
              <td align="center"><input type="radio" name="ID97000020" value="1"></input></td>			  			  
	  			  
            </tr>

<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="6"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
<tr>
           
          </table>
		  </td>
          <td></td>
        </tr>	
       <tr>
          <td>&nbsp;</td>
          <td valign="top">
		   <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="100" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="640" height="1"></td>
            <tr>
              <td valign="top">Comments:</td>
              <td align="left"><textarea name="ID98000020" style="width: 600" rows="4"  cols="200"></textarea></td>
            </tr>
			<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
			<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
			<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
          </table></td>
          <td>&nbsp;</td>
        </tr>
       <tr>
         <td>&nbsp;</td>
         <td valign="top">
		 		  <table width="740" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="440" height="1"></td>
              <td><img src="../../common_media/images/0shim.gif" alt="" width="300" height="1"></td>
            </tr>
            <tr>
              <td colspan="2" class="h3">
			  Optional Student Contact Information:
			  </td>
            </tr>

<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="2"></td></tr>
<tr><td colspan="2" bgcolor="#000000"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="3"></td></tr>
			
            <tr>
              <td>May we contact you about your experience with Rational University?</td>
              <td><select name="ID10000020" size="1"><option value="2">Yes</option>
				<option value="1">No</option>
			  </select></td>
             </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>Name:</td>
              <td><input type="text" name="ID10100020"  value="" size="40" maxlength="60"></input></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
             <tr>
              <td>Company:</td>
              <td><input type="text" name="ID10200020"  value="" size="40" maxlength="60"></input></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>Address:</td>
              <td><input type="text" name="ID10300020"  value="" size="40" maxlength="60"></input></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
            <tr>
              <td>Email:</td>
              <td><input type="text" name="ID10400020"  value="" size="40" maxlength="60"></input></td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			

            <tr>
              <td>Phone:</td>
              <td><input type="text" name="ID10500020"  value="" size="40" maxlength="60"></input>
              </td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>			
             <tr>
              <td>Fax:</td>
              <td><input type="text" name="ID10500020"  value="" size="40" maxlength="60"></input>
              </td>
            </tr>
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2" bgcolor="#CCCCCC"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="1"></td></tr>			
<tr><td colspan="2"><img src="../../common_media/images/0shim.gif" alt="" width="540" height="13"></td></tr>
          </table></td>
         <td>&nbsp;</td>
       </tr>
       <tr>
         <td>&nbsp;</td>
         <td valign="top">
		            <a href='javascript:sendPressed()'><img src="../../common_media/images/submit.gif" width="120" height="21" border="0"/></a>
          </td>
         <td>&nbsp;</td>
       </tr>
       <tr>
         <td><img src="../../common_media/images/0shim.gif" alt="" width="30" height="30"></td>
         <td valign="top">&nbsp;</td>
         <td>&nbsp;</td>
       </tr>						
	</form>


      </table>
<%
}  
%>

</body>
</html>
