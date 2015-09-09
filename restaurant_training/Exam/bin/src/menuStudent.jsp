<%
logging.Secretary.startFxn("exam", "menuStudent.jsp");
%>
<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%!
    BeanSecurity security;
    BeanStudent user;
    String fxnsFile;
%>
<%-- Retreive the AdminBean from the HttpSession Object--%>
<%
fxnsFile = "";
user = (BeanStudent)session.getAttribute(BeanUser.SESSION);
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 10))){
%><jsp:forward page="login"/><%}
String title = user.getFullName() + ", Please select an option."; %>
<%@ include file="header.inc" %>
<a href="takeExam">Take an Exam</a><BR>
<%
    if(user.savedExamExists())
    {
        %>
        <a href="takeExam?toDo=cont">Continue an Exam</a><BR>
        <%
    }
%>
<a href="displayExam?w=grades">View a Taken Exam</a><BR>
<%@ include file="footerStudent.inc" %>
<%
logging.Secretary.endFxn("exam", "menuStudent.jsp");
%>