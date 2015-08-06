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

public class StudentServlet extends javax.servlet.http.HttpServlet {
    Secretary log;
    public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
        log = new Secretary();
        super.init(config);
    }
    
    public void destroy() {
    }
    
    protected void processRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        log.write("-=-=-=-=-=-=-=-=-=-=-=-= StudentServlet.processRequest()=-=-=-=-=-=-=-=-=-=-=-=-");
        javax.servlet.http.HttpSession session = request.getSession(true);
        javax.servlet.RequestDispatcher rd = null;
        String toDo = request.getParameter("toDo");
        if(toDo.equals("processLogin")){
            toDo = ExamBean.DISPLAY_LIST_TO_CHOOSE;
        }
        StudentBean student = null;
        student = (StudentBean)session.getAttribute(StudentBean.SESSION);
        /*
         * CHECK SECURITY HERE AND MAKE SURE THE USER IS AN Student
         */
        if(student == null){
            //User isn't logged in. Forward to the login page.
            rd = getServletConfig().getServletContext().getRequestDispatcher("/login");
        }
        else if(toDo.equals(ExamBean.DISPLAY_TO_TAKE)){
            // Display the selected exam to take.
            // Load the selected test into memory and set it as the session.
            Integer eNum = new Integer(request.getParameter("en"));
            ExamDAO examDAO = new ExamDAO();
            examDAO.loadFromDBViaExamNum(eNum.intValue());
            student.setExam(new ExamBean(examDAO));
            rd = getServletConfig().getServletContext().getRequestDispatcher("/examDisplayToTake.jsp");
        }
        else if(toDo.equals(ExamBean.DISPLAY_LIST_TO_CHOOSE)){
            // Display list of exams for user to choose which to take
            rd = getServletConfig().getServletContext().getRequestDispatcher("/examDisplayToTake.jsp");
        }
        else if(toDo.equals(ExamBean.PROCESS_TAKEN_TEST)){
            student.getExam().processTaken(request);
            log.write("Finished processing taken exam.");
        }
        else{
            rd = getServletConfig().getServletContext().getRequestDispatcher("/login");
        }
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
