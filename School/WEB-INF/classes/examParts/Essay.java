package examParts;
import java.util.*;
import java.sql.*;
import db.*;

public final class Essay extends Question {
    //*********************** STATIC VARS ************************//
    final static public String QUESTION_FIELD = "newEssayQ";
    final static public String SOLUTION_FIELD = "newEssayS";
    final static public String ENTRY_TYPE_CODE = "es";
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String question;
    private String solution;
    private String empAnswer;
    //******************** END PROPERTY VARS *********************//
    //*********************** QUERY STRINGS **********************//
    private static String insertStmtStr;
    private static String updateStmtStr;
    private static String deleteStmtStr;
    private static String selectStmtStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement selectStmt;
    //***************** END PREPARED STATEMENTS ******************//
    
    //*********************** CONSTRUCTORS ***********************//
    public Essay (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        this.createPreparedStatements();
    }
    public Essay(String quest, String answ) {
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        this.question = quest;
        this.solution = answ;
        this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setQuestion(String newQ){
        this.question = newQ;
    }
    public String getQuestion(){
        return this.question;
    }
    public void setSolution(String newS){
        this.solution = newS;
    }
    public String getSolution(){
        return this.solution;
    }
    public void setEmpAnswer(String newA){
        this.empAnswer = newA;
    }
    public String getEmpAnswer(){
        return this.empAnswer;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum){
        log.write("Essay.processForm(request, "+location+", "+qNum+")");
        String newQuestion = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        this.setExamLoc(location);
        this.setQuestionNum(qNum);
        this.setQuestion(replaceSubstring(newQuestion, "\n", "<BR>"));
        this.setSolution(replaceSubstring(newSolution, "\n", "<BR>"));
    }
    public void fetchEmpAnswer(javax.servlet.http.HttpServletRequest request, int takeNum) {
        log.write("Essay.fetchEmpAnswer()");
        String answ = (String)(request.getParameter(this.questionNum + "_" + ENTRY_TYPE_CODE));
        this.empAnswer = replaceSubstring(answ, "\n", "<BR>");
        this.takeNum = takeNum;
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum) {
        log.write("Essay.displayForm()");
        String form = "   <TABLE CLASS=QUESTION>\n      <TR>\n"
                + "         <TD WIDTH=\"" + Q_NUM_TD_WIDTH + "\"><P>" 
                + currentQNum + ")</P></TD>\n         <TD WIDTH=\""
                + QUESTION_TD_WIDTH + "\"> \n            \n"
                + "            <INPUT TYPE=\"text\" NAME=\"" + QUESTION_FIELD 
                + "\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH + "\">\n"
                + "         </TD>\n      </TR>\n         <TD WIDTH=\""
                + Q_NUM_TD_WIDTH + "\">Solution:</TD>\n         <TD WIDTH=\""
                + QUESTION_TD_WIDTH + "\"> \n            \n"
                + "            <TEXTAREA NAME=\"" + SOLUTION_FIELD 
                + "\" cols=\"" + ESSAY_TEXT_AREA_COLS + "\" rows=\"" 
                + ESSAY_TEXT_AREA_ROWS + "\"></textarea>\n"
                + "         </TD>\n      </TR>\n"
                + "      <TD COLSPAN=\"2\"></TD>\n"
                + "      </TR>\n   </TABLE>\n";
        return form;
    }
    public String displayGraded() {
        log.write("Essay.displayGraded()");
        log.write("Essay.displayToView()");
        String display = "   <TABLE CLASS=QUESTION>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n         <TD colspan=\"" + LETTER_TD_WIDTH + "\">" 
            + this.question + "</TD>\n      </TR>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n"
            + "          <TD VALIGN=\"top\"><B>"
            + this.empAnswer + "</B></TD>\n      </TR>\n"
            + "   </TABLE>\n   <BR>\n";
        return display;
    }
    public String displayToMod() {
        log.write("Essay.displayToMod()");
        return null;
    }
    public String displayToTake() {
        log.write("Essay.displayToTake()");
        String display = "   <TABLE CLASS=QUESTION>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n         <TD colspan=\"" + LETTER_TD_WIDTH + "\">" 
            + this.question + "</TD>\n      </TR>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n"
            + "          <TD VALIGN=\"top\"><TEXTAREA "
            + "COLS=\"" + ESSAY_TEXT_AREA_COLS + "\" "
            + "ROWS=\"" + ESSAY_TEXT_AREA_ROWS + "\" "
            + "NAME=\""
            + this.questionNum + "_" + ENTRY_TYPE_CODE
            +"\"></TEXTAREA></TD>\n      </TR>\n"
            + "   </TABLE>\n   <BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("Essay.displayToView()");
        String display = "   <TABLE CLASS=QUESTION>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n         <TD colspan=\"" + LETTER_TD_WIDTH + "\">" 
            + this.question + "</TD>\n      </TR>\n"
            + "      <TR>\n         <TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n"
            + "          <TD VALIGN=\"top\"><B>"
            + this.solution + "</B></TD>\n      </TR>\n"
            + "   </TABLE>\n   <BR>\n";
        return display;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //************************* DB METHODS ***********************//
    protected void createPreparedStatements(){
        log.write("Essay.createPreparedStatements()");
        try{
            insertStmtStr = "INSERT INTO ESSAY "
                   + "(exam_num, exam_loc, question, solution) VALUES ("
                   + "?, ?, ?, ?)";
            this.insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            updateStmtStr = "UPDATE ESSAY SET "
                    + "question=?, solution=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM ESSAY "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
            
            selectStmtStr = "SELECT question, solution FROM ESSAY "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
            
        }catch(Exception e){
            log.write("Exception in Essay.createPreparedStatements()" 
                + " : " + e.getMessage());
        }
    }
    public void insertToDB() {
        log.write("Essay.insertToDB()");
        try{
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.question);
            insertStmt.setString(4, this.solution);
            insertStmt.executeUpdate();
        }catch(Exception e){
            log.write("Exception in Essay.insertToDB()" 
                + " : " + e.getMessage());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("Essay.loadFromDB("+eNum+", "+eLoc+")");
        selectStmt.setInt(1, eNum);
        selectStmt.setInt(2, eLoc);
        ResultSet rs = selectStmt.executeQuery();
        if(rs.next()){
            this.setExamNum(eNum);
            this.setExamLoc(eLoc);
            this.setQuestion(rs.getString(1));
            this.setSolution(rs.getString(2));
        }
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    public void insertTakeToDB(int takeNum){
        log.write("Essay.insertTakeToDB()");
        try{
            this.insertTakeStmt.setInt(1, takeNum);
            this.insertTakeStmt.setInt(2, this.examLoc);
            insertTakeStmt.setInt(3, 0);
            insertTakeStmt.setString(4, this.empAnswer);
            //this.insertTakeStmt.setInt(5, 0);
            this.insertTakeStmt.setNull(5, java.sql.Types.BIT);
            this.insertTakeStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in Essay.insertTakeToDB() : " + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        String selectTakenStmtStr;
        PreparedStatement selectTakenStmt;
        try{
            selectTakenStmtStr = "SELECT e.question, e.solution, " 
                               + "ea.answer_entered FROM "
                               + "ESSAY e, EMP_ANSWER ea, EXAM_TAKE et "
                               + "WHERE et.take_num = ? "
                               + "AND e.exam_num = et.exam_num "
                               + "AND et.take_num = ea.take_num ";
            selectTakenStmt = dbUtil.createPreparedStatement(selectTakenStmtStr);
            selectTakenStmt.setInt(1, tNum);
            ResultSet rs = selectTakenStmt.executeQuery();
            if(rs.next()){
                this.setExamNum(eNum);
                this.setExamLoc(eLoc);
                this.setQuestion(rs.getString(1));
                this.setSolution(rs.getString(2));
                this.setEmpAnswer(rs.getString(3));
            }
        }catch(SQLException e){
            log.write("SQLException in Essay.loadTakenFromDB() : " + e.getMessage());
        }catch(Exception e){
            log.write("Exception in Essay.loadTakenFromDB() : " + e.getMessage());
        }    
    }
    
    //************************* END DB FXNS ***********************//
    
}
