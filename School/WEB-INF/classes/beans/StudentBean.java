package beans;
import examParts.*;
import java.util.*;
import java.sql.*;
import logging.*;
import db.*;
import vos.*;
import daos.*;
public class StudentBean extends UserBean{
    //*********************** STATIC VARS ************************//
    public final static String SESSION = "student";
    public final static String EXAM = "exam";
    public final static String DISPLAY_TEST_TO_TAKE = "dispToTake";
    //********************* END STATIC VARS **********************//
    private Secretary log;
    //********************** PROPERTY VARS ***********************//
    private ExamBean exam;
    //private ExamVO examVO;
    private DBUtil dbUtil;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** CONSTRUCTORS ***********************//
    public StudentBean() {
        super();
        log.write("END StudentBean() constructor");
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setExam(ExamBean eb){
        this.exam = eb;
    }
    public ExamBean getExam(){
        return this.exam;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    //******************** END PROCESS FUNCTIONS ******************//
    //********************** HTML DISPLAY FXNS ********************//
    public String getExamToTake(){
        return this.exam.getHTMLToTake(this.getFullName(), this.getEmpNum());
    }
    public String getListOfActiveTestsToTake(){
        log.write("StudentBean.getListOfActiveTestsToTake()");
        ExamDAO ed = new ExamDAO();
        Object exams[] = ed.getAllActive().toArray();
        ExamVO eVO = null;
        String temp = null;
        String toReturn = "<TABLE ALIGN=CENTER BORDER=5 CELLPADDING=4>\n"
                + "<TR>\n"
                + "<TH>EXAM NUMBER</TH>"
                + "<TH>EXAM NAME</TH>"
                + "<TH>CATEGORY</TH>"
                + "</TR>\n";
        toReturn += "<TR>";
        for(int i=0; i < exams.length; i++){
            eVO = (ExamVO)(exams[i]);
            temp = "<TR>\n"
                     + "<TD><a href=\"admin?toDo=" + ExamBean.DISPLAY_TO_TAKE + "&en=" + eVO.getExamNum() + "\">" + eVO.getExamNum() + "</a></TD>"
                     + "<TD><a href=\"admin?toDo=" + ExamBean.DISPLAY_TO_TAKE + "&en=" + eVO.getExamNum() + "\">" + eVO.getExamName() + "</a></TD>"
                     + "<TD><a href=\"admin?toDo=" + ExamBean.DISPLAY_TO_TAKE + "&en=" + eVO.getExamNum() + "\">" + eVO.getCatName() + "</a></TD>"
                 + "</TR>\n";
            toReturn = toReturn.concat(temp);
        }
        temp = "</TR></TABLE>\n";
        toReturn = toReturn.concat(temp);
        return toReturn;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    //************************* DB METHODS ***********************//
    
    //*********************** END DB METHODS *********************//
    public void insertExamToDB(){
        log.write("StudentBean.insertExamToDB()");
    }
}
