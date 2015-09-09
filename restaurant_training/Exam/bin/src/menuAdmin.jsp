<%logging.Secretary.startFxn("exam", "menuAdmin.jsp");%>
<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%!
    BeanSecurity security;
    BeanUser user;
    String fxnsFile;
%>
<%-- Retreive the AdminBean from the HttpSession Object--%>
<%
fxnsFile = "";
user = (BeanUser)session.getAttribute(BeanUser.SESSION);
session.removeAttribute("thisEntryType");
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%}
String title = user.getFullName() + ", Please select an option."; %>
<%@ include file="header.inc" %>
<a href="createExam?s=0">Create an Exam</a><BR>
<a href="deleteExam">Delete an Exam</a><BR>
<a href="modifyExam">Modify an Exam</a><BR>
<a href="displayExam?w=solutions">View an Exam</a><BR>
<a href="gradeExam">Grade an exam</a><br>
<a href="displayExam?w=grades">View Graded Exam</a><BR>

<!--
<a href="examCreate.jsp">Create an Exam</a><BR>
<a href="examDelete.jsp">Delete an Exam</a><BR>
<a href="examModify.jsp">Modify an existing exam</a><BR>
<a href="examGrade.jsp">Grade an exam</a><br>
<a href="examDisplayWithSolutions.jsp">View an Exam With Solutions</a><BR>
<a href="examDisplayGraded.jsp">View a Taken Exam</a><BR>
-->
<%@ include file="footerAdmin.inc" %>
<%logging.Secretary.endFxn("exam", "menuAdmin.jsp");%>