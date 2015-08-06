<%
logging.Secretary.write("------ displayExamListToChoose.jsp");
%>
<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%if(session.getAttribute(AdminBean.SESSION) == null){ %>
    <jsp:forward page="./login.jsp"/> 
<%}%>
<jsp:useBean id="admin" scope="session" class="beans.AdminBean" />
<%String title = (String)(request.getAttribute("title"));
logging.Secretary.write("title = " + title);
%>
<%@ include file="header.inc" %>
<%if(session.getAttribute(ExamBean.SESSION) == null){%>
    <%-- Display the list of exams. --%>
    <jsp:getProperty name="admin" property="listToDisplayGraded"/>
<%}
else{
%>
    <%-- Display the selected exam with solution. --%>
    <jsp:useBean id="exam" scope="session" class="beans.ExamBean" />
    <jsp:getProperty name="exam" property="htmlGraded"/>
<%}%>
<%@ include file="footer.inc" %>
<%logging.Secretary.write("------ END displayExamListToChoose.jsp");%>
