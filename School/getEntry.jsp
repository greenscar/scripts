<%@page contentType="text/html"%>
<%
String x = (String)request.getParameter("submit");
if(x.compareTo("End of Exam") == 0){
%>
    <jsp:forward page="menuAdmin.jsp" />
<%
}
String title = "Enter your entry then select the next entry type."; 
%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%!
    AdminBean admin;
    ExamBean exam;
    String thisQTypeCode;
%>
<%    
    thisQTypeCode = (String)request.getParameter(ExamBean.NEXT_Q_TYPE_CODE);
    exam = (ExamBean)session.getAttribute(ExamBean.SESSION);
    if(exam == null){ %>
        <jsp:forward page="./menuAdmin.jsp"/> 
<%  } %>
<jsp:useBean id="exam" scope="session" class="beans.ExamBean" />
<jsp:setProperty name="exam" property="*" />
<jsp:setProperty name="exam" property="thisQTypeCode" value="<%= thisQTypeCode %>"/>
<%@ include file="header.inc" %>
<jsp:getProperty name="exam" property="htmlWithSolutions" />
<form name="getEntry" method="POST" action="admin" ONSUBMIT="return validateQuestion(this)">
    <br><br>
    <h4>Please enter your entry:</h4>
    <jsp:getProperty name="exam" property="nextEntryForm" />
    <h4>Please select your next entry type.</h4>
    <jsp:getProperty name="exam" property="nextEntryType" />
    <br><br><br>
    <input type="hidden" name="toDo" value="<%=ExamBean.GET_Q_AND_NEXT_Q_TYPE %>">
    <input type="hidden" name="qType" value="<%= thisQTypeCode %>">
    <input type="submit" name="submit" value="Continue">
    <input type="submit" name="submit" value="End of Exam">
</form>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END getFirstQ.jsp");
%>