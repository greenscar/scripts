package beans;
import examParts.*;
import java.util.*;
import java.sql.*;
import logging.*;
import db.*;
import vos.*;
import daos.*;
public class GodBean extends UserBean{
    //*********************** STATIC VARS ************************//
    public final static String SESSION = "god";
    public final static String EXAM = "exam";
    //********************* END STATIC VARS **********************//
    private Secretary log;
    //********************** PROPERTY VARS ***********************//
    private ExamBean exam;
    private ExamVO examVO;
    private DBUtil dbUtil;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** CONSTRUCTORS ***********************//
    public GodBean() {
        super();
        dbUtil = new DBUtil();
        log.write("GodBean() constructor");
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    //******************** END PROCESS FUNCTIONS ******************//
    //********************** HTML DISPLAY FXNS ********************//
    //******************* END HTML DISPLAY FXNS *******************//
    //************************* DB METHODS ***********************//
    public String getListToDisplayWithSol(){
        log.write("GodBean.getListOfActiveExams()");
        ExamDAO ed = new ExamDAO();
        Object exams[] = ed.getAllActive().toArray();
        log.write("ed.getAllActive complete.");
        ExamVO eVO = null;
        String temp = null;
        String toReturn = "<TABLE ALIGN=CENTER BORDER=5 CELLPADDING=4>\n"
                + "<TR>\n"
                + "<TH>NAME</TH>"
                + "<TH>CREATOR</TH>"
                + "<TH>CATEGORY</TH>"
                + "<TH>CREATION DATE</TH>"
                + "</TR>\n";
        toReturn += "<TR>";
        for(int i=0; i < exams.length; i++){
            eVO = (ExamVO)(exams[i]);
            temp = "<TR>\n"
                     + "<TD><a href=\"AdminServlet?goTo=" + ExamBean.DISPLAY_W_SOLUTIONS + "&examNum=" + eVO.getExamNum() + "\">" + eVO.getExamName() + "</a></TD>"
                     + "<TD><a href=\"AdminServlet?goTo=" + ExamBean.DISPLAY_W_SOLUTIONS + "&examNum=" + eVO.getExamNum() + "\">" + eVO.getCreatorName() + "</a></TD>"
                     + "<TD><a href=\"AdminServlet?goTo=" + ExamBean.DISPLAY_W_SOLUTIONS + "&examNum=" + eVO.getExamNum() + "\">" + eVO.getCatName() + "</a></TD>"
                     + "<TD><a href=\"AdminServlet?goTo=" + ExamBean.DISPLAY_W_SOLUTIONS + "&examNum=" + eVO.getExamNum() + "\">" + eVO.getDateCreated() + "</a></TD>"
                     + "</TR>\n";
            toReturn = toReturn.concat(temp);
        }
        temp = "</TR></TABLE>\n";
        toReturn = toReturn.concat(temp);
        return toReturn;
    }
    //*********************** END DB METHODS *********************//
    public void insertExamToDB(){
        log.write("GodBean.insertExamToDB()");
    }
}
