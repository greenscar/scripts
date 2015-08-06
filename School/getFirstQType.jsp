<%@page contentType="text/html"%>
<% String title = "Please select your first entry type."; %>
<%@ page import="beans.*" errorPage="error.jsp" %>

<jsp:useBean id="exam" scope="session" class="beans.ExamBean" />
<jsp:setProperty name="exam" property="creator" value="<%= (AdminBean)session.getAttribute(AdminBean.SESSION) %>" />
<%--<jsp:setProperty name="exam" property="*"/> --%>
<%@ include file="header.inc" %>
<jsp:getProperty name="exam" property="headerToDisplay" />
<form name="getFirstQType" method="POST" action="admin">
    <h3>Please select your first question type.</h3>
    <jsp:getProperty name="exam" property="nextEntryType" />
    <br><br><br>
    <INPUT TYPE="hidden" NAME="toDo" VALUE="<%=ExamBean.GET_FIRST_Q %>">
    <input type="submit" name="submit" value="Continue">
</form>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END getFirstQType.jsp");
%>