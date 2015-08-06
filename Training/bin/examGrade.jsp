<%@page errorPage="error.jsp"%>
<%@page contentType="text/html"%>
<%@page import="beans.*"%>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanAdmin" />
<%
    BeanSecurity security;
    security = new BeanSecurity();
if(!(security.userRoleNumIsValid(request, session, 100))){
%><jsp:forward page="login"/><%
}
String title = "Please select an exam.";
String fxnsFile = "js_grade_fxns.js";
%>
<%@ include file="header.inc" %>
    <%if(request.getParameter("examTakeNum") == null){
    %>
<!--************************************************************************** 
    Display a list of taken exams, which can be graded.
**************************************************************************-->
    <table class="question">
    <tr>
        <th width="10%">
            <a href="gradeExam?orderBy=take_num" class="table_header">Take #</a>
        </th>
        <th width="10%">
            <a href="gradeExam?orderBy=date_submitted" class="table_header">Date Taken</a>
        </th>
        <th width="25%">
            <a href="gradeExam?orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
        <th width="15%">
            <a href="gradeExam?orderBy=taker" class="table_header">Taker</a>
        </th>
        <th width="10%">
            <a href="gradeExam?orderBy=final_grade" class="table_header">Grade</a>
        </th>
        <th width="30%">
            <a href="gradeExam?orderBy=graders_comment" class="table_header">Graders Comment</a>
        </th>
    </tr>
    <%
        //Load list of active exams.
        while(BeanUserSession.examListHasMore()){
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examTakeNum" />
                    </a>
                </td>
                <td>
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examDateTaken" />
                    </a>
                </td>
                <td>
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examName" />
                    </a>
                </td>
                <td>
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examTakerFullName" />
                    </a>
                </td>
                <td align="center">
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                    </a>
                </td>
                <td align="center">
                    <a href="gradeExam?toDo=dispToGrade&examTakeNum=<jsp:getProperty name="BeanUserSession" property="examTakeNum"/>">
                        <jsp:getProperty name="BeanUserSession" property="examGradersComment" />
                    </a>
                </td>
            </tr>
        <%
        }
    %>
        </table>
<!--************************************************************************** 
    END Display a list of taken exams, which can be graded.
**************************************************************************-->
    <%
    }
    else if(((String)(request.getParameter("toDo"))).compareTo("dispToGrade") == 0){
        int examTakeNum = (new Integer(request.getParameter("examTakeNum"))).intValue();
    %>
<!--************************************************************************** 
    Display the selected exam to grade
**************************************************************************-->
<form name="gradeExam" method="POST" action="gradeExam" onsubmit="javascript:return checkGrading(this)">
<input type="hidden" name="toDo" value="verify">
<input type="hidden" name="examTakeNum" value="<%=examTakeNum%>">
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
                <td>Final Grade:</td>
                <td>
                    <input size="3" type="text" name="finalGrade" readonly value="<jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/>"> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
               <td>Student Can Retake:</td>
               <td><jsp:getProperty name="BeanUserSession" property="examCanRetakeString"/></td>
            </tr>
          </td>
         </tr>
        </table>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyToGrade"/>        
        <br><br>
        <h3 align="center"><u>Overall Exam Comments</u></h3>
        <textarea class="multChoice" name="gradersComment"><jsp:getProperty name="BeanUserSession" property="examGradersComment"/></textarea>
        <p align="center">Additional Points:<input type="text" name="extraPoints" size="3"></p>
        <P ALIGN=CENTER>
          <INPUT TYPE="SUBMIT" VALUE="Submit Graded Exam.">
        <P>
</form>
<!--************************************************************************** 
    END Display the selected exam to grade
**************************************************************************-->
    <%}
    else if(((String)(request.getParameter("toDo"))).compareTo("verify") == 0){
        int examTakeNum = (new Integer(request.getParameter("examTakeNum"))).intValue();
%>
<!--************************************************************************** 
    Display the graded exam to Verify Grading
**************************************************************************-->
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
                <td>Today's Date:</td>
                <td>
                    <%=new java.util.Date()%>
                </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
               <td>Extra Points:</td>
               <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointsExtra"/>
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
        <form name="verify" method="POST" action="gradeExam">
        
<input type="hidden" name="examTakeNum" value="<%=examTakeNum%>">
            <input type="hidden" name="toDo" value="process">
            <input type="submit" name="submit" value="Process grading">
        </form>
        <form name="verify" method="POST" action="gradeExam">
<input type="hidden" name="examTakeNum" value="<%=examTakeNum%>">
            <input type="hidden" name="toDo" value="regrade">
            <input type="submit" name="submit" value="Modify your grading">
        </form>
<!--************************************************************************** 
    END Display the graded exam to Verify Grading
**************************************************************************-->
<%
    }
    else{ //request.getParameter("toDo") == "dispGraded")
%>
<!--************************************************************************** 
    Display the graded exam
**************************************************************************-->

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
                <td>Today's Date:</td>
                <td>
                    <%=new java.util.Date()%>
                </td>
            </tr>
            <tr>
                <td>Point Value:</td>
                <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                </td>
            </tr>
            <tr>
               <td>Extra Points:</td>
               <td>
                    <jsp:getProperty name="BeanUserSession" property="examPointsExtra"/>
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
<!--************************************************************************** 
    END Display the graded exam
**************************************************************************-->
<%
    }
%>
    
<%@ include file="footerAdmin.inc" %>
