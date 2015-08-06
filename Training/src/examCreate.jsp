<%@page errorPage="error.jsp" %>
<%@page contentType="text/html" %>
<%@page import="beans.*" %>
<%@page import="java.util.Date" %>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanAdmin" />
<%!
    BeanSecurity security;
    String title;
    String fxnsFile;
%>
<%
/***************************************************************************
 * SECURITY CHECK
 **************************************************************************/
if (security == null)
    security = new BeanSecurity();
fxnsFile = "js_exam_create_fxns.js";
if((!security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%
}
/***************************************************************************
 * END SECURITY CHECK
 **************************************************************************/
int step = new Integer(request.getParameter("s")).intValue();
if(step == 0)
{
   /*************************************************************************
    * GET HEADER INFO
    ************************************************************************/
   title = "Please Enter your Exam Information.";
   %>
<%@include file="header.inc"%>
   <form name="getHeaderInfo" method="POST" action="createExam" onsubmit="return validateHeaderData(this)">
   <input type="hidden" name="s" value="<%=++step%>">
    <table class="header">
        <tr>
            <th>Please enter your exam information.</th>
            </td>
        </tr>
        <tr>
            <td>
            <table width="400"  border="0" cellpadding="1" cellspacing="1"> 
                <TR> 
                    <TD WIDTH="145">Exam Name:</TD>
                    <TD WIDTH="151"> 
                        <INPUT TYPE="text" NAME="examName" SIZE="40" maxlength="100">
                    </TD>
                </TR>
                <TR> 
                    <TD WIDTH="145">Creator Name:</TD>
                    <TD WIDTH="151"> 
                       <%= BeanUserSession.getFullName()%>
                    </TD>
                </TR>
                <TR> 
                    <TD WIDTH="145">Display After Taking:</TD>
                    <TD WIDTH="151"> 
                        <input type="radio" name="displayAfterTaking" value="true" CHECKED>Yes 
                        <input type="radio" name="displayAfterTaking" value="false">No
                    </TD>
                </TR>
                <TR> 
                    <TD WIDTH="145">Student Can Retake:</TD>
                    <TD WIDTH="151"> 
                        <input type="radio" name="canRetake" value="true">Yes 
                        <input type="radio" name="canRetake" value="false" CHECKED>No
                    </TD>
                </TR>
                <TR>
                    <TD COLSPAN="2" ALIGN="CENTER">
                        <INPUT TYPE="submit" NAME="Submit" VALUE="Submit">
                        <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
                        <input type="button" name="Cancel" VALUE="Cancel" onclick="history.go(-1);">
                    </TD>
                </TR>
            </table>
        </tr>
    </table>
    <INPUT TYPE="hidden" NAME="creatorsEmpNum" VALUE="<%= BeanUserSession.getEmpNum()%>">
    </form>
    </div>
    </body>
</html>
<%
    /*************************************************************************
    * END GET HEADER INFO
    ************************************************************************/
} // END if(request.getParameter("s") == 0)
else if(step == 1)
{
   /*************************************************************************
    * DISPLAY HEADER INFO
    * GET FIRST ENTRY TYPE
    ************************************************************************/
   String title = "Please select your first entry type.";
   %>
<%@include file="header.inc"%>
   <form name="getEntry" method="POST" action="createExam" onsubmit="return ensureEntryTypeSelected(this)">
   <input type="hidden" name="s" value="<%=++step%>">
   <table class="header">
      <tr>
         <td>
            <tr>
               <th colspan=2>
                  <jsp:getProperty name="BeanUserSession" property="examName"/>
               </th>
            </tr>
            <tr>
               <td>Creator:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="fullName"/>
               </td>
            </tr>
            <tr>
               <td>Date Created:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
               </td>
            </tr>
            <tr>
               <td>Display After Taking:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
               </td>
            </tr>
            <tr>
               <td>Student Can Retake:</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
         </td>
      </tr>
   </table>
   <br><br>
      <!--Get the form for the next entry type. -->
      <table class="header">
      <tr><th align="center">Please select your first entry type</th></tr>
      <%
      BeanUserSession.entryTypeListReset();
      while(BeanUserSession.entryTypeListHasMore()){
      %>
         <tr>
            <td align="left">
               <jsp:setProperty name="BeanUserSession" property="nextEntryType" value="true" /> 
               <input type="radio" name="nextEntryType" value="<jsp:getProperty name="BeanUserSession" property="anEntryTypeCode"/>"><jsp:getProperty name="BeanUserSession" property="anEntryTypeName"/><br>
            </td>
         </tr>
      <% 
      }
      %>
      </table>
      <br><br>
      <p ALIGN="CENTER">
         <INPUT TYPE="submit" NAME="Submit" VALUE="Submit">
         <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
      </p>
    </form>
    </div>
   <%
   /*************************************************************************
    * END DISPLAY HEADER INFO
    * END GET FIRST ENTRY TYPE
    ************************************************************************/
}// END else if(step == 1)
else if(step > 1)
{
   /*************************************************************************
    * DISPLAY EXAM
    * DISPLAY ENTRY FORM
    * DISPLAY ENTRY TYPE FORM
    ************************************************************************/    
   String title = "Enter the information.";
   %>
<%@include file="header.inc"%>
   <form name="getEntry" method="POST" action="createExam" onsubmit="return validateEntryCreate(this)">
   <input type="hidden" name="s" value="<%=++step%>">
   <table class="header">
      <tr>
         <td>
            <tr>
               <th colspan=2>
                  <jsp:getProperty name="BeanUserSession" property="examName"/>
               </th>
            </tr>
            <tr>
               <td>Creator:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="fullName"/>
               </td>
            </tr>
            <tr>
               <td>Date Created:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
               </td>
            </tr>
            <tr>
               <td>Display After Taking:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
               </td>
            </tr>
            <tr>
               <td>Student Can Retake:</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetake"/></td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
         </td>
      </tr>
   </table>
   <br><br>
   <jsp:getProperty name="BeanUserSession" property="examBodyWithSolutionsForDevel"/>
   <br><br>
      <!--Get the form for the entry to be entered. -->
      <jsp:getProperty name="BeanUserSession" property="lastEntryForm" />
      <input type="hidden" name="thisEntryType" value="<%=request.getParameter("nextEntryType")%>">
      <br>
      <!--Get the form for the next entry type. -->
      <table class="header">
      <tr><th align="center">Please select your next entry type</th></tr>
      <%
      BeanUserSession.entryTypeListReset();
      while(BeanUserSession.entryTypeListHasMore()){
      %>
         <tr>
            <td align="left">
               <jsp:setProperty name="BeanUserSession" property="nextEntryType" value="true" /> 
               <input type="radio" name="nextEntryType" value="<jsp:getProperty name="BeanUserSession" property="anEntryTypeCode"/>"><jsp:getProperty name="BeanUserSession" property="anEntryTypeName"/><br>
            </td>
         </tr>
      <% 
      }
      %>
         <tr>
            <td align="left">
               <input type="radio" name="nextEntryType" value="end">END OF EXAM<br>
            </td>
         </tr>
      </table>
      <br><br>
      <p ALIGN="CENTER">
         <INPUT TYPE="submit" NAME="Submit" VALUE="Submit">
         <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
      </p>
    </form>
    </div>
    </body>
</html>
<%
   /*************************************************************************
    * END DISPLAY EXAM
    * END DISPLAY ENTRY FORM
    * END DISPLAY ENTRY TYPE FORM
    ************************************************************************/
}
%>