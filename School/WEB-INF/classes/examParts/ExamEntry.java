package examParts;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.lang.StringBuffer;
import logging.*;
import daos.*;
import db.*;
/*
 * ExamEntry is an abstract class of all entrys in an exam
 *    and will be extended by Header.class and Question.class
 * Created on April 24, 2003, 1:55 PM
 */
public abstract class ExamEntry{
    //*********************** STATIC VARS ************************//
    //examLoc is where in the exam this entry will show up.
    //Each ExamEntry will have a unique examLoc.
    protected static int CHOICE_TD_WIDTH = 580;
    protected static int QUESTION_TD_WIDTH = CHOICE_TD_WIDTH;
    protected static int Q_NUM_TD_WIDTH = 3; //50;
    protected static int RADIO_TD_WIDTH = Q_NUM_TD_WIDTH;
    protected static int QUESTION_TEXT_FIELD_WIDTH = 80;
    protected static int CHOICE_TEXT_FIELD_WIDTH = 20;
    protected static int ESSAY_TEXT_AREA_COLS = QUESTION_TEXT_FIELD_WIDTH - 10;
    protected static int ESSAY_TEXT_AREA_ROWS = 10;
    protected static int LETTER_TD_WIDTH = RADIO_TD_WIDTH; //3;
    protected static int BLANK_WIDTH = 20;
    //protected static int ESSAY_TEXT_AREA_ROWS = 15;
    //protected static int ESSAY_TEXT_AREA_COLS = 65;
    protected static char[] LETTER = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String entryTypeCode;
    private String entryTypeName;
    private String entryTypeClassName;
    private String className;
    protected int examNum;
    protected int examLoc;
    protected Secretary log;
    protected DBUtil dbUtil;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** QUERY STRINGS **********************//
    private static String insertEEStmtStr;
    private static String updateEEStmtStr;
    private static String deleteEEStmtStr;
    private static String selectEEStmtStr;
    private static String selectTypeViaCodeStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement insertEEStmt;
    private PreparedStatement updateEEStmt;
    private PreparedStatement deleteEEStmt;
    private PreparedStatement selectEEStmt;
    private PreparedStatement selectTypeViaCodeStmt;
    //***************** END PREPARED STATEMENTS ******************//
    
    //*********************** CONSTRUCTORS ***********************//
    public ExamEntry(){
        dbUtil = new DBUtil();
        log = new Secretary();
        this.createPreparedEEStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public String getEntryTypeCode(){
        log.write("ExamEntry.getEntryTypeCode()");
        return this.entryTypeCode;
    }
    public String getEntryTypeName(){
        log.write("ExamEntry.getEntryTypeName()");
        return this.entryTypeName;
    }
    public String getClassName(){
        log.write("ExamEntry.getClassName()");
        return this.className;
    }    
    public int getExamLoc(){
        log.write("ExamEntry.getExamLoc()");
        return examLoc;
    }
    public void setEntryTypeCode(String qTypeID){
        log.write("ExamEntry.setEntryTypeCode(" + qTypeID + ")");
        this.entryTypeCode = qTypeID;
    }
    public void setExamLoc(int loc){
        log.write("ExamEntry.setExamLoc("+loc+")");
        //this.oldExamLoc = examLoc;
        this.examLoc = loc;
    }
    public int modExamLoc(int change){
        log.write("ExamEntry.modExamLoc("+change+")");
        //this.oldExamLoc = examLoc;
        examLoc += examLoc;
        return examLoc;
    }
    public void setExamNum(int num){
        log.write("ExamEntry.setExamNum("+num+")");
       // this.oldExamNum = examNum;
        this.examNum = num;
    }
    public int getExamNum(){
        log.write("ExamEntry.getExamNum()");
        return this.examNum;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public abstract void processForm(HttpServletRequest request, int currentExamLoc, int currentQuestNum);
    protected static String replaceSubstring(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public abstract String displayForm(int currentQNum);
    public abstract String displayToTake();
    public abstract String displayToMod();
    public abstract String displayToView();
    public abstract String displayGraded();
    //******************* END HTML DISPLAY FXNS *******************//
    
    
    //*************************** DB FXNS *************************//
    protected abstract void createPreparedStatements();
    public abstract void insertToDB();
    public abstract void loadFromDB(int examNum, int qNum) throws SQLException;
    public abstract void loadTakenFromDB(int eNum, int eLoc, int tNum);
    public abstract void updateDB();
    public abstract void deleteFromDB();
    
    private void createPreparedEEStatements(){
        log.write("ExamEntry.createPreparedEEStatements()");
        try{
            insertEEStmtStr = "INSERT INTO EXAM_ENTRY "
                   + "(exam_num, exam_loc, question_num, entry_type_code) "
                   + "VALUES (?, ?, ?, ?)";
            this.insertEEStmt = dbUtil.createPreparedStatement(insertEEStmtStr);
            
            updateEEStmtStr = "UPDATE EXAM_ENTRY SET "
                    + "question_num=?, entry_type_code=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateEEStmt = dbUtil.createPreparedStatement(updateEEStmtStr);
            
            deleteEEStmtStr = "DELETE FROM EXAM_ENTRY "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteEEStmt = dbUtil.createPreparedStatement(deleteEEStmtStr);
            
            selectEEStmtStr = "SELECT * FROM EXAM_ENTRY "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.selectEEStmt = dbUtil.createPreparedStatement(selectEEStmtStr);
            
            selectTypeViaCodeStr = "SELECT entry_type_name, class_name FROM "
                    + " ENTRY_TYPE WHERE entry_type_code = ?";
            this.selectTypeViaCodeStmt = dbUtil.createPreparedStatement(selectTypeViaCodeStr);
        }catch(Exception e){
            log.write("Exception in ExamHeader.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToEXAM_ENTRY(int qNum){
        log.write("ExamEntry.insertToEXAM_ENTRY("+qNum+")");
        try{
            insertEEStmt.setInt(1, this.examNum);
            insertEEStmt.setInt(2, this.examLoc);
            insertEEStmt.setInt(3, qNum);
            insertEEStmt.setString(4, this.entryTypeCode);
            insertEEStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamEntry.insertToEXAM_ENTRY(int qNum): " + e.toString());
        }catch(Exception e){
            log.write("Exception in ExamEntry.insertToEXAM_ENTRY(int qNum): " + e.toString());
        }
    }
    public void updateInEXAM_ENTRY(int qNum){
        log.write("ExamEntry.updateInEXAM_ENTRY("+qNum+")");
        try{
            updateEEStmt.setInt(1, qNum);
            updateEEStmt.setString(2, this.entryTypeCode);
            updateEEStmt.setInt(3, this.examNum);
            updateEEStmt.setInt(4, this.examLoc);
            updateEEStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamEntry.updateInEXAM_ENTRY(int qNum): " + e.toString());
        }catch(Exception e){
            log.write("Exception in ExamEntry.updateInEXAM_ENTRY(int qNum): " + e.toString());
        }
    }
    public void deleteFromEXAM_ENTRY(){
        log.write("ExamEntry.deleteFromEXAM_ENTRY()");
        try{
            deleteEEStmt.setInt(1, this.examNum);
            deleteEEStmt.setInt(2, this.examLoc);
            updateEEStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamEntry.deleteFromEXAM_ENTRY(int qNum): " + e.toString());
        }catch(Exception e){
            log.write("Exception in ExamEntry.deleteFromEXAM_ENTRY(int qNum): " + e.toString());
        }
    }
    public void selectFromEXAM_ENTRY(){
    }
    
    public void loadEntryTypeViaCode(String code){
        log.write("ExamEntry.loadEntryTypeViaCode("+code+")");
        try{
            selectTypeViaCodeStmt.setString(1, code);
            ResultSet rs = selectTypeViaCodeStmt.executeQuery();
            rs.next();
            this.entryTypeCode = code;
            this.entryTypeName = rs.getString(1);
            this.entryTypeClassName = rs.getString(2);
        }catch(SQLException e){
            log.write("SQLException in ExamEntry.loadEntryTypeViaCode(" + code + "): " + e.toString());
        }
        log.write("ExamEntry.loadEntryTypeViaCode("+code+") COMPLETE");
    }
    //************************* END DB FXNS ***********************//
}
