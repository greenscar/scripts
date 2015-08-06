<%
logging.Secretary.startFxn("exam", "login.jsp");
%>
<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<HTML>
  <HEAD>
    <TITLE>Please Log In</TITLE>
    <link rel=stylesheet type="text/css" href="templates.css">
    <script language = "javascript" type="text/javascript">
        <%@ include file="js_login_fxns.js" %>
        <%@ include file="js_misc_fxns.js" %>
    </script>
</HEAD>
  <BODY onload="setFocus('textarea', 'text')" background="#FF00FF">
    <div align="center">
<%   
    if((request.getHeader("referer") != null) && (session.getAttribute("numTries") != null))
    {
        if(((Integer)(session.getAttribute("numTries"))).intValue() > 1){
%>
<H3 align="center">You have entered an incorrect login or password. <BR>Please try again</H3>
<%
    }
   }
%>
<BR><BR><BR><BR>
   <CENTER>   
      <FORM NAME="loginForm" METHOD="post" ACTION="login" ONSUBMIT="return validateLoginForm(this)">
         <TABLE BORDER="4" CELLPADDING="4" ALIGN="CENTER" HEIGHT="115">
            <TR>
               <TD>            
               <TABLE BORDER="0" ALIGN="CENTER" HEIGHT="115">
                  <TR>                  
                     <TD ALIGN="right">Employee ID:</TD>
                     <TD><INPUT TYPE="text" NAME="emp_num" value="" class="disableAutoComplete"></TD>
                  </TR>
                  <TR>
                     <TD ALIGN="right">Password:</TD>
                     <TD><INPUT TYPE="password" NAME="pwd" value="" class="disableAutoComplete"></TD>               
                  </TR>               
                  <TR>
                     <TD ALIGN = "CENTER" colspan="2">
                        <INPUT TYPE="submit" NAME="Submit" VALUE="Login">
                        <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
                     </TD>               
                  </TR>            
               </TABLE>
               </TD>
            </TR>      
         </TABLE>   
      </FORM>   
   </CENTER>
   </DIV>
    <script language="JavaScript" type="text/javascript">
    if (document.getElementsByTagName) 
    {
        var inputElements = document.getElementsByTagName("input");
        for (i=0; inputElements[i]; i++) 
        {
            if (inputElements[i].className && (inputElements[i].className.indexOf("disableAutoComplete") != -1)) 
            {
                inputElements[i].setAttribute("autocomplete","off");
            }//if current input element has the disableAutoComplete class set.
        }//loop thru input elements
    }//basic DOM-happiness-check
    </script>
  </body>
</html>

<%
    logging.Secretary.endFxn("exam", "login.jsp");
%>