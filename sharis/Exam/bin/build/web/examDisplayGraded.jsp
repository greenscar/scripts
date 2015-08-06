<%@page errorPage="error.jsp"%>
<%@page contentType="text/html"%>
<%@page import="beans.*"%>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanUser"/>
<%
System.out.println(BeanUserSession.getClass().toString());
    beans.BeanSecurity security;
    security = new beans.BeanSecurity();
if(!(security.userRoleNumIsValid(request, session, 10))){
%><jsp:forward page="login"/><%
}
String title = "Please select an exam.";
String fxnsFile = "";
%>
<%@ include file="header.inc" %>
    <%if(request.getParameter("takeNum") == null)
    {
    %>
    <table class="question">
    <tr>
        <th width="10%">
            <a href="displayExam?w=grades&orderBy=take_num" class="table_header">Take #</a>
        </th>
        <th width="10%">
            <a href="displayExam?w=grades&orderBy=date_submitted" class="table_header">Date Taken</a>
        </th>
        <th width="25%">
            <a href="displayExam?w=grades&orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
        <th width="15%">
            <a href="displayExam?w=grades&orderBy=taker" class="table_header">Taker</a>
        </th>
        <th width="10%">
            <a href="displayExam?w=grades&orderBy=final_grade" class="table_header">Grade</a>
        </th>
        <th width="30%">
            <a href="displayExam?w=grades&orderBy=graders_comment" class="table_header">Graders Comment</a>
        </th>
    </tr>
    <%
        //Load list of active exams.
        while(BeanUserSession.examListHasMore()){
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="displayExam?w=grades&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examTakeNum" />
                    </a>
                </td>
                <td>
                    <a href="displayExam?w=grades&toDo=dispToGrade&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examDateTaken" />
                    </a>
                </td>
                <td>
                    <a href="displayExam?w=grades&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examName" />
                    </a>
                </td>
                <td>
                    <a href="displayExam?w=grades&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examTakerFullName" />
                    </a>
                </td>
                <td align="center">
                    <a href="displayExam?w=grades&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                    </a>
                </td>
                <td align="center">
                    <a href="displayExam?w=grades&takeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examGradersComment" />
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
        int takeNum = (new Integer(request.getParameter("takeNum"))).intValue();
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
                <td>Name:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examTakerFullName"/>
                </td>
            </tr>
            <tr>
                <td>Employee ID:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examTakerEmpNum"/>
                </td>
            </tr>
            <tr>
                <td>Taken On:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examDateTaken"/>
                </td>
            </tr>
            <tr>
               <td>Extra Points:</td>
               <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointsExtra"/>
                </td>
            </tr>
            <tr>
                <td>Final Grade:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
                <td>Grade %:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examGrade"/>
                </td>
            </tr>
            <tr>
               <td>Student Can Retake:</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
          </td>
         </tr>
        </table>
<%if((BeanUserSession.getExamGradersComment() != null) && !(BeanUserSession.getExamGradersComment().equalsIgnoreCase(""))){%>
        <h3 class="gradersComment"><jsp:getProperty name="BeanUserSession" property="examGradersComment"/></h3>
<%}%>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyGraded"/>
    <%}%>
<%if(BeanUserSession.getRoleNum() == 100){%>
<%@ include file="footerAdmin.inc" %>
<%}
  else{%>
<%@ include file="footerStudent.inc" %>
<%
}
%>
