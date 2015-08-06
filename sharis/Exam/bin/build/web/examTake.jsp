<%@page errorPage="error.jsp"%>
<%@page contentType="text/html"%>
<%@page import="beans.*"%>
<jsp:useBean id="BeanUserSession" scope="session" class="beans.BeanStudent" />
<%!
    BeanSecurity security;
%>
<%
String fxnsFile = "js_exam_take_fxns.js";
security = new BeanSecurity();
if((!security.userRoleNumIsValid(request, session, 10))){
    %><jsp:forward page="login"/><%
}
if((request.getParameter("examNum") == null) && (request.getParameter("toDo") == null))
{
    String title = "Please select an exam.";
    %>
    <%@ include file="header.inc" %>
    <!--
        DISPLAY LIST OF EXAMS FOR USER TO SELECT WHICH TO TAKE
    -->
    <table class="question">
    <tr>
        <th>
            <a href="takeExam?orderBy=exam_num" class="table_header">Exam Number</a>
        </th>
        <th>
            <a href="takeExam?orderBy=exam_name" class="table_header">Exam Name</a>
        </th>
        <th>
            <a href="takeExam?orderBy=point_value" class="table_header">Point Value</a>
        </th>
    </tr>
    <%
        //Load list of active exams.
        while(BeanUserSession.examListHasMore())
        {
            %>
            <jsp:setProperty name="BeanUserSession" property="nextExamVO" value="true" />
            <tr>
                <td>
                    <a href="takeExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&toDo=start">
                        <jsp:getProperty name="BeanUserSession" property="examNum" />
                    </a>
                </td>
                <td>
                    <a href="takeExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&toDo=start">
                        <jsp:getProperty name="BeanUserSession" property="examName" />
                    </a>
                </td>
                <td>
                    <a href="modifyExam?examNum=<jsp:getProperty name="BeanUserSession" property="examNum"/>&act=verify">
                        <jsp:getProperty name="BeanUserSession" property="examPointValueTotal" />
                    </a>
                </td>
            </tr>
            <%
        }
    %>
    </table>
<%@ include file="footerStudent.inc" %>
    <%
}
else if((request.getParameter("toDo").equals("Submit Exam")) && (request.getParameter("cont") == null))
{
    int examNum = (new Integer((String)(request.getParameter("examNum")))).intValue();
    if(BeanUserSession.getExamDisplayAfterTaking())
    {
        String title = "Exam " + examNum;
        %>
        <%@ include file="header.inc" %>
        <!--
            DISPLAY GRADED EXAM
        -->
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
                            <jsp:getProperty name="BeanUserSession" property="fullName"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Employee ID:</td>
                        <td>
                            <jsp:getProperty name="BeanUserSession" property="empNum"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Today's Date:</td>
                        <td>
                            <%=new java.util.Date()%>
                        </td>
                    </tr>
                    <%
                    if(BeanUserSession.getExamIsSelfGrading())
                    {
                    %>
                    <tr>
                        <td>Point Value:</td>
                        <td>
                            <jsp:getProperty name="BeanUserSession" property="examPointValueFinalGrade"/> / <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                        </td>
                    </tr>
                    <%
                    }
                    %>
                </td>
            </tr>
        </table>
        <br><br>
        <jsp:getProperty name="BeanUserSession" property="examBodyGraded"/>
         <%@ include file="footerStudent.inc" %>
        <%
    }//if(BeanUserSession.getExamDisplayAfterTaking())n")
    else{
        String title = "Exam " + examNum + " completed";
        %>
        <%@ include file="header.inc" %>
        <h1 align="center">Your exam has been processed.</h1>
         <%@ include file="footerStudent.inc" %>
        <%
    }//END else
}//END else if(request.getParameter("toDo").equalsIgnoreCase("processTaken"))
else
{
    int examNum = 0;
    if(request.getParameter("examNum") == null && request.getAttribute("examNum") != null)
    {
        examNum = ((Integer)(request.getAttribute("examNum"))).intValue();
    }
    else if(request.getParameter("examNum") != null && request.getAttribute("examNum") == null)
    {
        examNum = (new Integer((String)(request.getParameter("examNum")))).intValue();
    }
    String title = "Exam " + examNum;
    %>
    <%@ include file="header.inc" %>
    <!--
        DISPLAY SECTION OF EXAM TO TAKE
    -->
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
                        <jsp:getProperty name="BeanUserSession" property="fullName"/>
                    </td>
                </tr>
                <tr>
                    <td>Employee ID:</td>
                    <td>
                        <jsp:getProperty name="BeanUserSession" property="empNum"/>
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
                        <jsp:getProperty name="BeanUserSession" property="examPointValueTotal"/>
                    </td>
                </tr>
            </td>
        </tr>
    </table>
    <form name="takeExamForm" method="POST" action="takeExam" onsubmit="return checkForErrors(this)">
    <input type="hidden" name="examNum" value="<%=examNum%>">
    <br><br>
    <jsp:getProperty name="BeanUserSession" property="examSectionToTake"/>

    <%
    if(!BeanUserSession.examOnFirstSection())
    {%>
        <!--
            DISPLAY FIRST & PREV BUTTONS
        -->
        <input class="goTo" type="submit" name="toDo" value="<< Save / First">
        <input class="goTo" type="submit" name="toDo" value="< Save / Prev">
    <%
    }
    if(!BeanUserSession.examOnLastSection())
    {%>
        <!--
            DISPLAY NEXT & LAST BUTTONS
        -->
        <input class="goTo" type="submit" name="toDo" value="Save / Next >">
        <input class="goTo" type="submit" name="toDo" value="Save / Last >>">
    <%
    }%>
    <!--
        DISPLAY SUBMIT EXAM BUTTON
    -->
    <br><br>
    <input class="submitExam" type="submit" name="toDo" value="Submit Exam" onclick="return confirm('Are you sure you are ready to submit this exam?');">
    </form>
    <script language="JavaScript" type="text/javascript">
        if (document.getElementsByTagName) 
        {
            var inputElements = document.getElementsByTagName("input");
            for (i=0; inputElements[i]; i++) 
            {
                if (inputElements[i].className && (inputElements[i].className.indexOf("disableAutoComplete") != -1)) 
                {
                    inputElements[i].setAttribute("autocomplete","off");
                }//if current input element has the disableAutoComplete class set.
            }//loop thru input elements
        }//basic DOM-happiness-check
    </script>
    </body>
    </html>
    <%
} //END else if(request.getParameter("toDo") == null)
%>