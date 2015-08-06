

package servlets;

import java.io.*;
import java.net.*;
import logging.Secretary;
import javax.servlet.*;
import javax.servlet.http.*;
import beans.BeanUser;

public class ExamDisplayServlet extends dbConnectionBroker.HttpServletJXGB 
{

    private java.sql.Connection connWeb = null;
    private java.sql.Connection connPS = null;

    public void init(ServletConfig config) throws ServletException {
      java.util.HashMap whereTo = new java.util.HashMap();
      whereTo.put("WEB", "WEB");
      whereTo.put("PS", "PS");
      super.init(config, whereTo);
    }
    public void destroy() {
        super.destroy();
    }

    /** 
    * This processRequest handles submits from ExamDelete.jsp.
    * 
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        Secretary.startFxn("exam", "ExamDisplayServlet.processRequest(request, response)");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        Integer examNum;
        String with = request.getParameter("w");
        String goTo = "/index.jsp";
        BeanUser user = (BeanUser)(session.getAttribute("BeanUserSession"));
        //Secretary.write("exam", "with = " + with);
        connWeb = this.getConnection("WEB");
        connPS = this.getConnection("PS");
        if((request.getParameter("examNum") == null) && (request.getParameter("takeNum") == null))
        {
            // AN EXAM HAS NOT BEEN SELECTED. LOAD A LIST OF EXAMS FOR THE USER
            // TO SELECT FROM.
            //Secretary.write("exam", "examNum == null");
            Secretary.write("exam", "Display a list of exams");
            String orderBy = request.getParameter("orderBy");
            if(with.equals("solutions"))
            {
                // LOAD ALL EXAMS
                user.examListLoadActiveAll(connWeb, orderBy);
                goTo = "/examDisplayWithSolutions.jsp";
            }
            else if(with.equals("grades"))
            {
                // LOAD ALL GRADED EXAMS
                user.examListLoadViaGraded(connWeb, connPS, true, orderBy); 
                goTo = "/examDisplayGraded.jsp";
            }
        }
        else
        {
            if(request.getParameter("examNum") != null)
            {
                // Display exam with solutions
                examNum = new Integer(request.getParameter("examNum"));
                Secretary.write("exam", "Display exam # " + examNum + " with solutions.");
                // LOAD ONE EXAM WITH SOLUTIONS
                user.setExamFromDB(connWeb, connPS, examNum.intValue());
                goTo = "/examDisplayWithSolutions.jsp";
            }
            else if(request.getParameter("takeNum") != null)
            {
                // Display exam graded
                Integer takeNum = new Integer(request.getParameter("takeNum"));
                Secretary.write("exam", "Display exam take # " + takeNum + " graded.");
                user.setExamGradedFromDB(connWeb, connPS, takeNum.intValue());
                goTo = "/examDisplayGraded.jsp";
            }
        }
        this.freeConnection(connWeb, "WEB");
        this.freeConnection(connPS, "PS");
        rd = getServletConfig().getServletContext().getRequestDispatcher(goTo);
        Secretary.endFxn("exam", "ExamDisplayServlet.processRequest(request, response)");
        //this.destroy();
        rd.forward(request, response);
    }

    /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    processRequest(request, response);
    }

    /** Returns a short description of the servlet.
    */
    public String getServletInfo() {
    return "Short description";
    }

}
