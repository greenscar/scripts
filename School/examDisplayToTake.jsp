<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%    
if(session.getAttribute(StudentBean.SESSION) == null){ %>
    <jsp:forward page="./login.jsp"/> 
<%  } 
String title;
if(request.getParameter("en") == null){
    title = "Please select an exam to take."; 
}
else{
    title = "Good luck.";
}
%>
<%@ include file="header.inc" %>
<jsp:useBean id="student" scope="session" class="beans.StudentBean" />
<%
StudentBean sb = (StudentBean)session.getAttribute(StudentBean.SESSION);
if(request.getParameter("en") == null){
    //Display a list of active tests to choose from.
    out.print(sb.getListOfActiveTestsToTake());
}
else{
%>
    <form name="takeTest" method="post" action="gradeExam">
        <jsp:getProperty name="student" property="examToTake" />
        <INPUT TYPE="hidden" NAME="toDo" VALUE="<%=ExamBean.PROCESS_TAKEN_TEST%>">
        <INPUT TYPE="submit" NAME="Submit" VALUE="Submit">
        <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
    </form>
<%
}
%>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END takeExam.jsp");
%>