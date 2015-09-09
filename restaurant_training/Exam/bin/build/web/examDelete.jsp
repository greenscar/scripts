<!--
FILE: examDelete.jsp
FUNCTION: This file displays all screens needed for modification of an exam.
          Any form processing that will be done during the modification is 
             passed to examDeleteServlet.class
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
String fxnsFile = "";
//beanAdmin = (BeanAdmin)(session.getAttribute(beans.BeanUser.SESSION));
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%
}if(request.getParameter("act") != null){
   System.out.println("act = " + request.getParameter("act"));
}
/********************************************************************************
 * First visit to screen. An exam to mod has NOT been selected.
 *******************************************************************************/
if(request.getParameter("act") == null){
   title = "Which exam would you like to delete?";
%>
<%@ include file="header.inc" %>
    <table class="question">
    <tr><th colspan='3'>Please select an exam to delete</th></tr>
    <tr>
        <th>
            <a href="deleteExam?orderBy=exam_num" class="table_header">Exam Number</a>
        </th>
        <th>
            <a href="deleteExam?orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
    </tr>
    <%
        //Load list of active exams.
        while(BeanUserSession.examListHasMore()){
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="deleteExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examNum" />
                    </a>
                </td>
                <td>
                    <a href="deleteExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examName" />
                    </a>
                </td>
            </tr>
        <%
        }
            
    %>
        </table>
    <%
    }
/********************************************************************************
 * User has selected an exam to delete.
 *******************************************************************************/
    else{ //else (request.getParameter("act") == null)
      Integer eNum = new Integer((String)(request.getParameter("examNum")));
      //int examNum = ((Integer)(session.getAttribute("examNum"))).intValue();
      int examNum = eNum.intValue();
      if(request.getParameter("act").equals("verify")){
        title = "Are you sure you want to delete this exam? (This is permanent.)";
      }
      else{ //User has already deleted the exam.
        title = "This exam has been deleted.";
      }
%>      
<%@ include file="header.inc" %>
      <%if(request.getParameter("act").equals("verify")){%>
        <h1 align="center">Are you sure you want to delete this exam?</h1>
      <%}
      else{ //User has already deleted the exam.%>
        <h1 align="center">This exam has been deleted.</h1>
      <%}%>
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
                <td>Display After Taking:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDisplayAfterTakingString"/>
                </td>
            </tr>
            <tr>
               <td>Student Can Retake:</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
          </td>
         </tr>
        </table>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyWithSolutions"/>
      <%if(request.getParameter("act").equals("verify")){%>
          <FORM name="verify_delete" method="POST" action="deleteExam">
          <input type="hidden" name="examNum" value="<%=examNum%>">
          <input type="hidden" name="verify" value="TRUE">
          <input type="hidden" name="act" value="del">
          <P ALIGN=CENTER>
                <INPUT TYPE="SUBMIT" VALUE="Yes" action="SUBMIT">
                <INPUT TYPE="BUTTON" VALUE="No" onClick="window.location='deleteExam?verify=FALSE'">
          <P>
          </FORM>
      <%}
      else{ //User has already deleted the exam.%>
        <P ALIGN=CENTER>
            <INPUT TYPE="BUTTON" VALUE="Finished" onClick="window.location='menuAdmin.jsp'">
        <P>
      <%}
    } // END else (request.getParameter("act") == null)
    %>
    <%@ include file="footerAdmin.inc" %>
