<%@page errorPage="error.jsp"%>
<%@page contentType="text/html"%>
<%@page import="beans.*"%>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanAdmin" />
<%!
    BeanSecurity security;
%>
<%
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%
}
String title = "Please select an exam.";
String fxnsFile = "";
%>
<%@ include file="header.inc" %>
    <%if(request.getParameter("examNum") == null){
    System.out.println("examNum == null");
    %>
    <table class="question">
    <tr>
        <th>
            <a href="displayExam?w=solutions&orderBy=exam_num" class="table_header">Exam Number</a>
        </th>
        <th>
            <a href="displayExam?w=solutions&orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
        <th>
            <a href="modifyExam?orderBy=point_value" class="table_header">Point Value</a>
        </th>
        <th>
            <a href="modifyExam?orderBy=can_retake" class="table_header">Can Retake</a>
        </th>
    </tr>
    <%
        while(BeanUserSession.examListHasMore()){
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="displayExam?w=solutions&examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examNum" />
                    </a>
                </td>
                <td>
                    <a href="displayExam?w=solutions&examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>">
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
    <%
    }
    else{
        Integer num = new Integer(request.getParameter("examNum"));
        System.out.println("examNum = " + num);
        %>    
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
               <td>Student Can Retake</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
          </td>
         </tr>
        </table>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyWithSolutions"/>
    <%}%>
<%@ include file="footerAdmin.inc" %>
