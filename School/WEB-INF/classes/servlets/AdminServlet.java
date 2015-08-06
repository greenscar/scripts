package servlets;

import java.beans.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import db.*;
import daos.*;
import vos.*;
import beans.*;
import logging.*;

public class AdminServlet extends javax.servlet.http.HttpServlet {
    Secretary log;
    public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
        log = new Secretary();
        super.init(config);
    }
    
    public void destroy() {
    }
    
    protected void processRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        log.write("-=-=-=-=-=-=-=-=-=-=-=-= AdminServlet.processRequest()=-=-=-=-=-=-=-=-=-=-=-=-");
        javax.servlet.http.HttpSession session = request.getSession();
        javax.servlet.RequestDispatcher rd = null;
        String toDo = request.getParameter("toDo");
        AdminBean admin = null;
        admin = (AdminBean)session.getAttribute(AdminBean.SESSION);
        /*
         * CHECK SECURITY HERE AND MAKE SURE THE USER IS AN ADMIN
         */
        if(admin == null){
            //User isn't logged in. Forward to the login page.
            rd = getServletConfig().getServletContext().getRequestDispatcher("/login");
        }
        /*******************************************************************
         *                       CREATE EXAM CASES
         *******************************************************************/
        //log.write("toDo = " + toDo);
        else if(toDo == null){
            log.write("-----------------------------Forwarding Admin to /menuAdmin.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/menuAdmin.jsp");
        }
        else if(toDo.equals(ExamBean.GET_HEADER)){
            log.write("-----------------------------Forwarding Admin to /getHeaderInfo.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/getHeaderInfo.jsp");
        }
        else if(toDo.equals(ExamBean.GET_FIRST_Q_TYPE)){
            /*
             * 1) Create the exam object
             * 2) Insert the header into the database.
             * 3) Forward the user to the page to get the first question type
             */
            String examName = (String)request.getParameter("examName");
            String catCode = (String)request.getParameter(ExamCatBean.FIELD_NAME);
            Integer creat = new Integer((String)request.getParameter("creatorsEmpNum"));
            int creator = creat.intValue();
            //Float dateC = new Float(request.getParameter("dateCreated"));
            java.util.Date dateCreated = new java.util.Date();
            //String dateCreated = (String)request.getParameter("dateCreated");
            Boolean disp = new Boolean((String)request.getParameter("displayAfterTaking"));
            boolean display = disp.booleanValue();
            // Create a new ExamBean and set it to be the session exam.
            log.write("About to create ExamBean");
            ExamBean exam = new ExamBean(examName, catCode, creator, dateCreated, true, display);
            log.write("ExamBean created.");
            session.setAttribute(ExamBean.SESSION, exam);
            exam.insertHeaderToDB();
            log.write("-----------------------------Forwarding Admin to /getFirstQType.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/getFirstQType.jsp");
        }
        else if (toDo.equals(ExamBean.GET_FIRST_Q)){
            log.write("-----------------------------Forwarding Admin to /getEntry.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/getEntry.jsp");
        }
        else if (toDo.equals(ExamBean.GET_Q_AND_NEXT_Q_TYPE)){
            /*
             * exam has a Vector of ExamEntry objects, one for each entry.
             * The last entry is an ExamEntry object with no values. This object 
             *      was created when the form for the ExamEntry was displayed.
             * Place the data entered in the last ExamEntry held by this Exam object
             * processEntry will also place the entry into the DB
             */
            // 1) Process input ExamEntry
            ExamBean exam  = (ExamBean)session.getAttribute(ExamBean.SESSION);
            exam.processEntry(request, response);
            log.write("-----------------------------Forwarding Admin to /getEntry.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/getEntry.jsp");
        }
        /*******************************************************************
         *                     END CREATE EXAM CASES
         *******************************************************************/
        /*******************************************************************
         *                DISPLAY EXAM WITH SOLUTION CASES
         *******************************************************************/
        else if (toDo.equals(ExamBean.DISPLAY_W_SOLUTIONS)){
            String title = "";
            if(request.getParameter("examNum") != null){
                //Display an exam.
                /*
                 * 1) Create an instance of ExamDAO 
                 * 2) Load this ExamDAO from the db based on the passed examNum
                 * 3) Create an ExamBean from this ExamDAO
                 * 4) Set this ExamBean to the session.
                 */
                Integer eNum = new Integer(request.getParameter("examNum"));
                ExamDAO examDAO = new ExamDAO();
                examDAO.loadFromDBViaExamNum(eNum.intValue());
                ExamBean examBean = new ExamBean(examDAO);
                session.setAttribute(examBean.SESSION, examBean);
                title = "Here is exam " + examBean.getExamName();
            }
            else{
                title = "Please Select an Exam.";
            }
            request.setAttribute("title", title);
            //Display a list of exams in DB.
            log.write("-----------------------------Forwarding Admin to /examDisplayWithSolution.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/examDisplayWithSolution.jsp");
        }
        /*******************************************************************
         *              END DISPLAY EXAM WITH SOLUTION CASES
         *******************************************************************/
        /*******************************************************************
         *                DISPLAY EXAM GRADED CASES
         *******************************************************************/
        else if(toDo.equals(ExamBean.DISPLAY_GRADED)){
            String title;
            if(request.getParameter("tN") != null){
                Integer tNum = new Integer(request.getParameter("tN"));
                Integer eNum = new Integer(request.getParameter("eN"));
                ExamDAO examDAO = new ExamDAO();
                examDAO.loadTakenFromDB(eNum.intValue(), tNum.intValue());
                ExamBean examBean = new ExamBean(examDAO);
                session.setAttribute(ExamBean.SESSION, examBean);
                session.setAttribute("examNum", eNum);
                title = "Here is exam " + examBean.getExamName();
            }
            else{
                title = "Select an exam";
                session.removeAttribute(ExamBean.SESSION);
            }
            request.setAttribute("title", title);
            log.write("-----------------------------Forwarding Admin to /examDisplayGraded.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/examDisplayGraded.jsp");
        }
        /*******************************************************************
         *              END DISPLAY EXAM GRADED CASES
         *******************************************************************/
        /*******************************************************************
         *                         CREATE STUDENT -- add this later.
         *******************************************************************/
        else if (toDo.equals(AdminBean.ENROLL_NEW_STUDENT)){
            log.write("Forwarding Admin to /enrollStudent.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/enrollStudent.jsp");
        }
        /*******************************************************************
         *                       END CREATE STUDENT
         *******************************************************************/
        /*******************************************************************
         *                    SEND USER TO ADMIN MENU
         *******************************************************************/
        else{
            
            log.write("-----------------------------Forwarding Admin to /menuAdmin.jsp");
            rd = getServletConfig().getServletContext().getRequestDispatcher("/menuAdmin.jsp");
        }
        /*******************************************************************
         *                  END SEND USER TO ADMIN MENU
         *******************************************************************/
        // Forward the user to the page set in rd
        if(rd != null){
            rd.forward(request, response);
        }
    }
    
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    public String getServletInfo() {
        return "Short description";
    }
    
}
