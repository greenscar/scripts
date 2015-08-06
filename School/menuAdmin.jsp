<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%!
    AdminBean admin;
%>
<%-- Retreive the AdminBean from the HttpSession Object--%>
<%
admin = (AdminBean)session.getAttribute(AdminBean.SESSION);
String title = admin.getFullName() + ", what would you like to do?"; %>
<%@ include file="header.inc" %>
<%-- <%@ page import="examParts.*" errorPage = "error.jsp" %> --%>
<form method="post" action="login">
    <input type="submit" name="toDo" value="<%= ExamBean.GET_HEADER %>"><BR>
    <input type="submit" name="toDo" value="<%= ExamBean.DISPLAY_W_SOLUTIONS %>"><BR>
    <input type="submit" name="toDo" value="<%= ExamBean.DISPLAY_GRADED %>">
</form>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END menuAdmin.jsp");
%>