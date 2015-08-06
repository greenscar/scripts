<%@page contentType="text/html"%>
<%@ page import="beans.*" errorPage="error.jsp" %>
<%@ page import="vos.*" %>
<%@ page import="java.util.*" %>
<%-- Declare the AdminBean --%>
<%!
    AdminBean admin;
%>
<jsp:useBean id="catBean" scope="page" class="beans.ExamCatBean" />
<% admin = (AdminBean)session.getAttribute(AdminBean.SESSION); %>
<% String title = admin.getFullName() + ", Please enter your exam information."; %>
<%@ include file="header.inc" %>
    <form name="getHeaderInfo" method="POST" action="admin">
    <input type="hidden" name="dateCreated" value="<%= new Date() %>">
    <table class="header">
        <tr>
            <th>Please enter your exam information.</th>
            </td>
        </tr>
        <tr>
            <td>
            <INPUT TYPE="hidden" NAME="question_num" VALUE = "0">
            <table width="400"  border="0" cellpadding="1" cellspacing="1"> 
                <TR> 
                    <TD WIDTH="145">Exam Name:</TD>
                    <TD WIDTH="151"> 
                        <INPUT TYPE="text" NAME="examName" SIZE="25" maxlength="30">
                    </TD>
                </TR>
                <TR> 
                    <TD WIDTH="145">Your Name:</TD>
                    <TD WIDTH="151"> 
                        <%= admin.getFullName()%>
                    </TD>
                </TR>
                <TR> 
                <TD WIDTH="145">Exam Category:</TD>
                <TD WIDTH="151"> 
                    <jsp:getProperty name="catBean" property="examCatListForm" />
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
                    <TD COLSPAN="2" ALIGN="CENTER">
                        <INPUT TYPE="submit" NAME="Submit" VALUE="Submit">
                        <INPUT TYPE="reset" NAME="Reset" VALUE="Reset">
                    </TD>
                </TR>
            </table>
        </tr>
    </table>
    <INPUT TYPE="hidden" NAME="creatorsEmpNum" VALUE="<%= admin.getEmpNum()%>">
    <INPUT TYPE="hidden" NAME="toDo" VALUE="<%=ExamBean.GET_FIRST_Q_TYPE %>">
    </form>
<%@ include file="footer.inc" %>
<%
    logging.Secretary.write("------ END getHeaderInfo.jsp");
%>