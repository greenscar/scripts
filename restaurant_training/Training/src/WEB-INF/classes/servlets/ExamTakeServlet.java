package servlets;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import beans.*;


/**
 *
 * @author  jsandlin
 * @version
 */
public class ExamTakeServlet extends dbConnectionBroker.HttpServletJXGB {
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
    
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        logging.Secretary.startFxn("exam", "ExamTakeServlet.processRequest(request, response)");
        javax.servlet.http.HttpSession session = request.getSession(false);
        javax.servlet.RequestDispatcher rd = null;
        //String goTo = "/login";
        String goTo = "/examTake.jsp";
        //if(session.getAttribute(BeanUser.SESSION) != null)
        //{
            //goTo = "/examTake.jsp";
            BeanStudent student = ((BeanStudent)(session.getAttribute(BeanUser.SESSION)));
            connWeb = this.getConnection("WEB");
            connPS = this.getConnection("PS");
            String examNumString = request.getParameter("examNum");
            String toDo = request.getParameter("toDo");
            if(examNumString == null)
            {
                if((toDo != null) &&(toDo.equals("cont")))
                {
                    // LOAD EXAM FROM SESSION
                    student.loadSavedExam();
                    //if(student.examVO )
                    Integer en = new Integer(student.getExamNum());
                    request.setAttribute("examNum", en);
                }
                // LOAD LIST OF EXAMS TO CHOOSE ONE
                student.examListLoadToTake(connWeb);
                //student.examListLoadActiveAll(connWeb, request.getParameter("orderBy"));
            }
            else
            {
                // Load selected exam 
                //connWeb = this.getConnection("WEB");
                logging.Secretary.write("exam", "toDo = '" + toDo + "'");
                if(toDo.equals("start")){
                    //logging.Secretary.write("exam", "Load the exam to be taken.");
                    Integer examNum = new Integer(examNumString);
                    student.startExamTake(connWeb, connPS, examNum.intValue());
                }
                else if(toDo.equals("<< Save / First"))
                {
                    student.setExamSectionFirst(request);
                }
                else if(toDo.equals("< Save / Prev"))
                {
                    student.setExamSectionPrev(request);
                }
                else if(toDo.equals("Save / Next >"))
                {
                    student.setExamSectionNext(request);
                }
                else if(toDo.equals("Save / Last >>"))
                {
                    student.setExamSectionLast(request);
                }
                else if(toDo.equals("Submit Exam"))
                {
                    logging.Secretary.write("exam", "INSIDE Submit Exam");
                    //logging.Secretary.write("exam", "Processing Taken Exam.");
                    if(!student.examProcessTaken(connWeb, connPS, request))
                        goTo = goTo.concat("?cont=1");
                }
                //this.freeConnection(connWeb, "WEB");
            }
        //}
        this.freeConnection(connPS, "PS");
        this.freeConnection(connWeb, "WEB");
        logging.Secretary.endFxn("exam", "ExamTakeServlet.processRequest(request, response)");
        rd = getServletConfig().getServletContext().getRequestDispatcher(goTo);
        logging.Secretary.write("exam", "Forwarding to " + goTo);
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
