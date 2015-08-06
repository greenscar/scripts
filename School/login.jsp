<%
logging.Secretary.write("------ login.jsp");
%>
<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%
if(session.getAttribute(AdminBean.SESSION) != null){ 
    logging.Secretary.write("forwarding to AdminBean");
%>
    <jsp:forward page="admin"/> 
<%}
else if(session.getAttribute(StudentBean.SESSION) != null){ 
    logging.Secretary.write("forwarding to StudentBean");
%>
    <jsp:forward page="student"/> 
<%}
String title = "Please log in."; %>
<%@ include file="header.inc" %>
<%    
    if(session.getAttribute("numTries") != null){
        Integer  x = (Integer)(session.getAttribute("numTries"));
        int numTries = x.intValue();
        if(numTries > 0){
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
                     <TD><INPUT TYPE="text" NAME="emp_num" value="135489"></TD>
                  </TR>
                  <TR>
                     <TD ALIGN="right">Password:</TD>
                     <TD><INPUT TYPE="password" NAME="pwd" value="666"></TD>               
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
         <INPUT TYPE="hidden" NAME="toDo" VALUE="processLogin">
      </FORM>   
   </CENTER>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END login.jsp");
%>