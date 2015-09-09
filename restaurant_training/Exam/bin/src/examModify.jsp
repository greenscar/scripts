<!--
FILE: examModify.jsp
FUNCTION: This file displays all screens needed for modification of an exam.
          Any form processing that will be done during the modification is 
             passed to ExamModifyServlet.class
-->
<%@page errorPage="error.jsp"%>
<%@page contentType="text/html"%>
<%@page import="beans.*"%>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanAdmin" />
<%!
   BeanSecurity security;
   String title;
%>
<%
String fxnsFile = "js_exam_mod_fxns.js";
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%
}
if((session.getAttribute("examNumToMod") != null) && (BeanUserSession.getExamNum() == 0)){
   session.removeAttribute("examNumToMod");
}
%>
<%
/********************************************************************************
 * First visit to screen. An exam to mod has NOT been selected.
 *******************************************************************************/
if(request.getParameter("act") == null){
   title = "Please select an exam.";
%>
<%@ include file="header.inc" %>
    <table class="question">
    <tr><th colspan='5'>Please select an exam to modify</th></tr>
    <tr>
        <th>
            <a href="modifyExam?orderBy=exam_num" class="table_header">Exam Number</a>
        </th>
        <th>
            <a href="modifyExam?orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
        <th>
            <a href="modifyExam?orderBy=point_value" class="table_header">Point Value</a>
        </th>
        <th>
            <a href="modifyExam?orderBy=can_retake" class="table_header">Can Retake</a>
        </th>
    </tr>
    <%
        //Load list of active exams.
        while(BeanUserSession.examListHasMore()){
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="modifyExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examNum" />
                    </a>
                </td>
                <td>
                    <a href="modifyExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examName" />
                    </a>
                </td>
                <td>
                    <a href="modifyExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examPointValueTotal" />
                    </a>
                </td>
                <td>
                    <a href="modifyExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examCanRetake" />
                    </a>
                </td>
            </tr>
        <%
        }
    %>
        </table>
        <P ALIGN=CENTER>
            <INPUT TYPE="BUTTON" VALUE="Cancel" onClick="window.location='menuAdmin.jsp'">
        <P>
    <%
    }
/********************************************************************************
 * Second visit to screen. Ensure this is the exam they wish to mod.
 *******************************************************************************/
    else if(request.getParameter("act").equals("verify")){
        Integer eNum = new Integer((String)(request.getParameter("examNum")));
        //int examNum = ((Integer)(session.getAttribute("examNum"))).intValue();
        int examNum = eNum.intValue();
        title = "Is this the exam you wish to modify?";
%>      
<%@ include file="header.inc" %>
      <h1 align="center">Are you sure you want to modify this exam?</h1>
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
                    <jsp:getProperty name="BeanUserSession" property="examCreatorFullName"/>
                </td>
            </tr>
            <tr>
                <td>Date Created:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
                </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
                <td>Display After Taking:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
                </td>
            </tr>
            <tr>
               <td>User Can Retake</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
          </td>
         </tr>
        </table>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyWithSolutions"/>
        <P ALIGN=CENTER>
            <INPUT TYPE="BUTTON" VALUE="Yes" onClick="window.location='modifyExam?verify=TRUE&act=modExam'">
            <INPUT TYPE="BUTTON" VALUE="No" onClick="window.location='modifyExam?verify=FALSE'">
        <P>
    <%}
/********************************************************************************
 * Third or greater visit to screen. Display exam to mod.
 *******************************************************************************/
    else if((request.getParameter("act").equals("modExam")) || (request.getAttribute("act") == "modExam")){
    title = "Do your modifications.";
%>      
<%@ include file="header.inc" %>
      <FORM name="mod_form_header" method="POST" action="modifyExam?act=modHeader">
        <table class=header onMouseOver="this.className='header_red_bold'" onMouseOut="this.className='header_black'" onclick="submit();">
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
                    <jsp:getProperty name="BeanUserSession" property="examCreatorFullName"/>
                </td>
            </tr>
            <tr>
                <td>Date Created:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
                </td>
            </tr>
            <tr>
                <td>Date Last Modified:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateLastMod"/>
                </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
                <td>Display After Taking:</td>
                <td>
                   <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
                </td>
            </tr>
            <tr>
               <td>User Can Retake</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
          </td>
         </tr>
        </table>
        </form>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyToMod"/>
        <P ALIGN=CENTER>
            <INPUT TYPE="BUTTON" VALUE="Finished" onClick="window.location='modifyExam?finished=end&act=end'">
        <P>
        <%
    }

/********************************************************************************
* User has selected to modify the header
* Display the header in a form.
********************************************************************************/
else if(request.getParameter("act").equals("modHeader")){
      title = "Do your modifications and press submit";
%>      
<%@ include file="header.inc" %>
      <FORM name="form_mod_header" method="POST" action="modifyExam?act=modHeaderProcess">
        <table class=header>
        <tr>
          <td>
            <tr>
                <th colspan=2>
                    <input type=text name="examName" class="examName" value="<jsp:getProperty name="BeanUserSession" property="examName"/>">
                </th>
            </tr>
            <tr>
                <td>Creator:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examCreatorFullName"/>
                </td>
            </tr>
            <tr>
                <td>Date Created:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
                </td>
            </tr>
            <tr>
                <td>Date Last Modified:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateLastMod"/>
                </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
                <td>Display After Taking:</td>
                <td>
                    <select NAME="displayAfter">
                        <option VALUE="true"<%
                        if(BeanUserSession.getExamDisplayAfterTaking()){
                            %> SELECTED <%
                        }%>>YES</option>
                        <option VALUE="false"<%
                        if(!BeanUserSession.getExamDisplayAfterTaking()){
                            %> SELECTED <%
                        }%>>NO</option>
                    </select>
                </td>
            </tr>
            <tr>
               <td>User Can Retake</td>
               <td>
                    <select NAME="canRetake">
                        <option VALUE="true"<%
                        if(BeanUserSession.getExamCanRetake()){
                            %> SELECTED <%
                        }%>>YES</option>
                        <option VALUE="false"<%
                        if(!BeanUserSession.getExamCanRetake()){
                            %> SELECTED <%
                        }%>>NO</option>
                    </select>
               </td>
            </tr>
          </td>
         </tr>
        </table>
        <br><br>
        <P ALIGN=CENTER>
            <INPUT TYPE="SUBMIT" VALUE="Finished">
        <P>
        </form>
        <%
}
/********************************************************************************
* User has selected an entry to modify.
* Display this entry in the form.
********************************************************************************/
else if(request.getParameter("act").equals("modEntryDisplay")){
   Integer eLoc = new Integer(request.getParameter("eLoc"));
   title = "Do your modifications and press submit";
%>      
<%@ include file="header.inc" %>
<jsp:setProperty name="BeanUserSession" property="examLocToMod" value="<%=eLoc.intValue() %>"/>
   <FORM name="form_mod_entry" method="POST" action="modifyExam?act=modEntryProcess">
   <table class="entry">
      <jsp:getProperty name="BeanUserSession" property="examEntryToMod"/>
   </table>
      <br><br>
      <P ALIGN=CENTER>
          <INPUT TYPE="SUBMIT" NAME="process" VALUE="Process Change" onClick="return validateEntryMod(this.form)">
          <INPUT TYPE="SUBMIT" NAME="delete" VALUE="Delete This Entry" onClick="return confirmDelete()">
          <INPUT TYPE="BUTTON" VALUE="Cancel" onClick="window.location='examModify.jsp?act=modExam'">
      <P>
   </FORM>
<%
}
/********************************************************************************
* User has selected to add an entry
* 1) Display the entry type form.
********************************************************************************/
else if(request.getParameter("act").equals("addEntry1")){
   title = "Select your entry type.";
   Integer eLoc = new Integer(request.getParameter("eLoc"));
   Integer qNum = new Integer(request.getParameter("qNum"));
%>      
<%@ include file="header.inc" %>
   <div align="center">
   <FORM name="form_mod_entry" method="POST" action="modifyExam?act=addEntry2" onsubmit="return ensureEntryTypeSelected(this)">
      <input type="hidden" name="eLoc" value="<%=eLoc%>">
      <input type="hidden" name="qNum" value="<%=qNum%>">
      <!--Get the form for the next entry type. -->
      <table class="header">
      <tr><th align="center">Please select your entry type</th></tr>
      <%
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
          <INPUT TYPE="BUTTON" VALUE="Cancel" onClick="window.location='examModify.jsp?act=modExam'">
      </p>
    </form>
    </div>
<%
}

/********************************************************************************
* User has selected to add an entry and has selected the entry type.
* 2) Display the entry form.
********************************************************************************/
else if(request.getParameter("act").equals("addEntry2")){
   Integer eLoc = new Integer(request.getParameter("eLoc"));
   title = "Enter the information then press submit.";
%>      
<%@ include file="header.inc" %>
<jsp:setProperty name="BeanUserSession" property="examLocToMod" value="<%=eLoc.intValue() %>"/>
   <FORM name="form_mod_entry" method="POST" action="modifyExam?act=addEntry3" onsubmit="return validateEntryMod(this)">
    <table class="entry">
      <jsp:getProperty name="BeanUserSession" property="examEntryToMod"/>
    </table>
    <br><br>
      <P ALIGN=CENTER>
          <INPUT TYPE="SUBMIT" VALUE="Process Change">
      <P>
   </FORM>
<%
}
/********************************************************************************
* User has finished modifying the exam.
********************************************************************************/
else if(request.getParameter("act").equals("end")){
   title = "Here is your exam.";
   %>
<%@include file="header.inc"%>
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
                <jsp:getProperty name="BeanUserSession" property="examCreatorFullName"/>
               </td>
            </tr>
            <tr>
               <td>Date Created:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDateCreated"/>
               </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
               <td>Display After Taking:</td>
               <td>
                  <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
               </td>
            </tr>
            <tr>
               <td>User Can Retake</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
         </td>
      </tr>
   </table>
   <br><br>
   <jsp:getProperty name="BeanUserSession" property="examBodyWithSolutions"/>
    <P ALIGN=CENTER>
        <INPUT TYPE="BUTTON" VALUE="Finished" onClick="window.location='menuAdmin.jsp'">
    <P>
   <%
   session.removeAttribute("examNumToMod");
}
%>
</body>
</html>
