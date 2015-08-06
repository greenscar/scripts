package examParts;
import java.sql.*;
import java.util.*;
import db.*;
public final class TrueFalse extends Question{
    //*********************** STATIC VARS ************************//
    final static public String QUESTION_FIELD = "newTrueFalseQ";
    final static public String SOLUTION_FIELD = "newTrueFalseS";
    final static public String ENTRY_TYPE_CODE = "tf";
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String question;
    private boolean solution;
    private int dbSolution;
    private boolean empAnswer;
    protected boolean empCorrect;
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
    public TrueFalse (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        this.createPreparedStatements();
    }
    public TrueFalse(String quest, boolean answ) {
        super();
        this.question = quest;
        this.solution = answ;
        if(answ) dbSolution = 1;
        else dbSolution = 0;
        this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setQuestion(String newQ){
        log.write("TrueFalse.setQuestion("+newQ+")");
        this.question = newQ;
    }
    public String getQuestion(){
        log.write("TrueFalse.getQuestion()");
        return this.question;
    }
    public void setSolution(boolean newS){
        log.write("TrueFalse.setSolution("+newS+")");
        this.solution = newS;
    }
    public boolean getSolution(){
        log.write("TrueFalse.getSolution()");
        return this.solution;
    }
    public void setEmpAnswer(boolean newA){
        log.write("TrueFalse.setEmpAnswer("+newA+")");
        this.empAnswer = newA;
    }
    public boolean getEmpAnswer(){
        log.write("TrueFalse.getEmpAnswer()");
        return this.empAnswer;
    }
    public void setEmpCorrect(boolean c){
        log.write("TrueFalse.setEmpCorrect("+c+")");
        this.empCorrect = c;
    }
    public boolean getEmpCorrect(){
        log.write("TrueFalse.setEmpCorrect()");
        return this.empCorrect;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum){
        log.write("TrueFalse.processForm(request, "+location+", "+qNum+")");
        String newQuestion = (String)request.getParameter(QUESTION_FIELD);
        Boolean x = new Boolean((String)request.getParameter(SOLUTION_FIELD));
        boolean newSolution = x.booleanValue();
        this.setExamLoc(location);
        this.setQuestionNum(qNum);
        this.setQuestion(newQuestion);
        this.setSolution(newSolution);
        if(this.solution) dbSolution = 1;
        else dbSolution = 0;
    }
    
    public void fetchEmpAnswer(javax.servlet.http.HttpServletRequest request, int takeNum) {
        log.write("TrueFalse.fetchEmpAnswer");
        Boolean x = new Boolean((String)request.getParameter(this.questionNum + "_" + ENTRY_TYPE_CODE));
        this.empAnswer = x.booleanValue();
        this.takeNum = takeNum;
    }
    
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum) {
        log.write("TrueFalse.displayForm()");
        String form;
        form = "<TABLE CLASS=QUESTION>\n"
             +  " \t\t<TD WIDTH=" + RADIO_TD_WIDTH + ">" 
             + currentQNum + ")</TD>\n"
             +  " \t\t<TD WIDTH=" + CHOICE_TD_WIDTH + "> \n            "
             +  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp\n"
             +  "\t\t\t<INPUT TYPE=text NAME=" 
             + QUESTION_FIELD + " SIZE=" + QUESTION_TEXT_FIELD_WIDTH + ">\n"
             +  " \t\t</TD>\n  \t</TR>\n  \t<TR>\n"
             +  " \t\t<TD WIDTH=" + RADIO_TD_WIDTH + ">Answer:</TD>\n"
             +  " \t\t<TD WIDTH=" + CHOICE_TD_WIDTH + ">\n            "
             +  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TRUE:\n            "
             +  "<INPUT TYPE=radio NAME=" 
             + SOLUTION_FIELD + " VALUE=true>\n            "
             +  "&nbsp;&nbsp;&nbsp;FALSE:\n            "
             +  "<INPUT TYPE=radio NAME=" 
             + SOLUTION_FIELD + " VALUE=false>\n         "
             +  "</TD>\n \t\t<TD COLSPAN=2>&nbsp;</TD>\n"
             +  "  \t</TR>\n"
             + "\t</TABLE>\n";	
        return form;
    }
    public String displayGraded() {
        String answer;
        log.write("TrueFalse.displayToView()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                    + "\t\t<TR>\n"
                    + "\t\t\t<TD WIDTH=\""+ Q_NUM_TD_WIDTH
                    + "\" VALIGN=\"top\">" + this.questionNum
                    + ")</TD>\n\t\t\t<TD colspan=\"2\">"
                    + this.question + "</TD>\n"
                    + "\t\t</TR>\n"
                    + "\t\t<TR>\n\t\t\t<TD WIDTH=\"" + Q_NUM_TD_WIDTH + "\">"
                    + "<IMG SRC=\"";
        if(this.empCorrect)
            display += "./images/check_mark.gif";
        else
            display += "./images/x_mark.gif";
        display += "\"></TD>\n<TD COLSPAN=\"" + Q_NUM_TD_WIDTH 
                + "\"><VALIGN=\"TOP\">";
        if(this.empAnswer)
            display += "<B>TRUE</B>";
        else
            display += "<B>FALSE</B>";
        display += "</TD>\n\t\t</TR>\n"
                + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToMod() {
        log.write("TrueFalse.displayToView()");
        return null;
    }
    public String displayToTake() {
        log.write("TrueFalse.displayToTake()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                    + "\t\t<TR>\n            "
                    + "<TD WIDTH=\""+Q_NUM_TD_WIDTH+"\" VALIGN=\"top\">" + this.questionNum
                    + ")</TD>\n\t\t\t<TD colspan=\"2\">"
                    + this.question + "</TD>\n"
                    + "\t\t</TR>\n"
                    + "\t\t<TR>\n\t\t\t<TD WIDTH=\""+Q_NUM_TD_WIDTH+"\"></TD>\n"
                    + "\t\t\t<TD WIDTH=\"5%\">"
                    + "<INPUT TYPE=\"radio\" NAME=\"" 
                    + this.questionNum + "_" + ENTRY_TYPE_CODE
                    + "\" VALUE=\"true\">TRUE"
                    + "</TD>\n\t\t\t<TD VALIGN=\"top\">"
                    + "<INPUT TYPE=\"radio\" NAME=\"" 
                    + this.questionNum + "_" + ENTRY_TYPE_CODE
                    + "\" VALUE=\"false\">FALSE"
                    + "</TD>\n\t\t</TR>\n"
                    + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("TrueFalse.displayToView()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                    + "\t\t<TR>\n"
                    + "\t\t\t<TD WIDTH=\""+Q_NUM_TD_WIDTH+"\" VALIGN=\"top\">" + this.questionNum
                    + ")</TD>\n\t\t\t<TD colspan=\"2\">"
                    + this.question + "</TD>\n"
                    + "\t\t</TR>\n"
                    + "\t\t<TR>\n\t\t\t<TD WIDTH=\""+Q_NUM_TD_WIDTH+"\"></TD>\n"
                    + "<TD COLSPAN=\""+Q_NUM_TD_WIDTH+"\"><VALIGN=\"TOP\">";
        if(this.solution)
            display += "<B>TRUE</B>";
        else
            display += "<B>FALSE</B>";
        display += "</TD>\n\t\t</TR>\n"
                + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    private String getSolutionToDisplay(){
        Boolean x = new Boolean(this.solution);
        return(x.toString().toUpperCase());
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //************************* DB METHODS ***********************//
    protected void createPreparedStatements(){
        log.write("TrueFalse.createPreparedStatements()");
        try{
            insertStmtStr = "INSERT INTO TRUE_FALSE "
                   + "(exam_num, exam_loc, question, solution) VALUES ("
                   + "?, ?, ?, ?)";
            this.insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            updateStmtStr = "UPDATE TRUE_FALSE SET "
                    + "question=?, solution=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM TRUE_FALSE "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
            
            selectStmtStr = "SELECT question, solution FROM TRUE_FALSE "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
        }catch(Exception e){
            log.write("Exception in TrueFalse.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB() {
        log.write("TrueFalse.insertToDB()");
        try{
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.question);
            insertStmt.setInt(4, this.dbSolution);
            insertStmt.executeUpdate();
        }catch(Exception e){
            log.write("Exception in TrueFalse.insertToDB()" 
                + " : " + e.toString());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("TrueFalse.loadFromDB("+eNum+", "+eLoc+")");
        selectStmt.setInt(1, eNum);
        selectStmt.setInt(2, eLoc);
        ResultSet rs = selectStmt.executeQuery();
        if(rs.next()){
            this.setExamNum(eNum);
            this.setExamLoc(eLoc);
            String q = rs.getString(1);
            log.write("q = " + q);
            this.setQuestion(q);
            int solution = rs.getInt(2);
            if(solution == 0) this.setSolution(false);
            else this.setSolution(true);
        }
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    public void insertTakeToDB(int takeNum){
        log.write("TrueFalse.insertTakeToDB()");
        try{
            this.insertTakeStmt.setInt(1, takeNum);
            this.insertTakeStmt.setInt(2, this.examLoc);
            this.insertTakeStmt.setInt(3, 0);
            if(this.empAnswer)
                this.insertTakeStmt.setInt(4, 1);
            else
                this.insertTakeStmt.setInt(4, 0);
            if(this.empAnswer == this.solution)
                this.insertTakeStmt.setInt(5, 1);
            else
                this.insertTakeStmt.setInt(5, 0);
            this.insertTakeStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in TrueFalse.insertTakeToDB() : " + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        log.write("TrueFalse.loadTakenFromDB("+eNum+", "+eLoc+", "+tNum+")");
        String selectTakenStr = "SELECT tf.question, tf.solution, "
                               + "ea.answer_entered, ea.correct FROM "
                               + "TRUE_FALSE tf, EMP_ANSWER ea, EXAM_TAKE et "
                               + "WHERE et.take_num = ? "
                               + "AND tf.exam_loc = ? "
                               + "AND tf.exam_num = et.exam_num "
                               + "AND et.take_num = ea.take_num "
                               + "AND ea.exam_loc = tf.exam_loc "; 
        try{
            PreparedStatement selectTakenStmt = 
                        dbUtil.createPreparedStatement(selectTakenStr);
            selectTakenStmt.setInt(1, tNum);
            selectTakenStmt.setInt(2, eLoc);
            ResultSet rs = selectTakenStmt.executeQuery();
            if(rs.next()){
                this.setExamNum(eNum);
                this.setExamLoc(eLoc);
                String q = rs.getString(1);
                this.setQuestion(q);
                int solution = rs.getInt(2);
                if(solution == 0) this.setSolution(false);
                else this.setSolution(true);
                int empAnswer = rs.getInt(3);
                if(empAnswer == 0) this.setEmpAnswer(false);
                else this.setEmpAnswer(true);
                int correct = rs.getInt(4);
                if(correct == 0) this.setEmpCorrect(false);
                else this.setEmpCorrect(true);
            }
        }catch(SQLException e){
            log.write("SQLException in TrueFalse.loadTakenFromDB() : " 
                    + e.getMessage());
        }catch(Exception e){
            log.write("Exception in TrueFalse.loadTakenFromDB()" 
                + " : " + e.getMessage());
        }
    }
    
    //************************* END DB FXNS ***********************//
}
