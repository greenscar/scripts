package examParts;
import java.sql.*;
import java.util.*;
import db.*;
    
public final class MultipleChoice extends Question {
    //*********************** STATIC VARS ************************//
    final static public String QUESTION_FIELD = "newMultChoiceQ";
    final static public String SOLUTION_FIELD = "newMultChoiceS";
    final static public String[] CHOICE_FIELD = {"choiceA", "choiceB", "choiceC", "choiceD"};  
    final static public String ENTRY_TYPE_CODE = "mc";
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String question;
    private String solution;
    private String empAnswer;
    private String[] choices;
    protected boolean empCorrect;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** QUERY STRINGS **********************//
    private static String updateStmtStr;
    private static String deleteStmtStr;
    //private static String selectStmtStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    //private PreparedStatement selectStmt;
    //***************** END PREPARED STATEMENTS ******************//
    
    //*********************** CONSTRUCTORS ***********************//
    public MultipleChoice (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        //this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setQuestion(String newQ){
        this.question = newQ;
    }
    public void setSolution(String newS){
        this.solution = newS;
    }
    public void setChoices(String[] chs){
        this.choices = chs;
    }
    public void setEmpAnswer(String a){
        this.empAnswer = a;
    }
    public String getEmpAnswer(){
        return this.empAnswer;
    }
    public String getQuestion(){
        return this.question;
    }
    public String getSolution(){
        return this.solution;
    }
    public void echoChoices(){
        log.write("CHOICES:");
        for(int x = 0; x < 4; x++){
            log.write("choices["+x+"] = " + choices[x]);
        }
    }
    public void setEmpCorrect(boolean c){
        this.empCorrect = c;
    }
    public boolean getEmpCorrect(){
        return this.empCorrect;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum){
        log.write("MultipleChoice.processForm(request, "+location+", "+qNum+")");
        String newQuestion = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        String newS = "choice" + newSolution;
        newSolution = (String)request.getParameter(newS);
        //newSolution = (String)request.getParameter(newSolution);
        String newChoices[] = {null,null,null,null};
        for(int x = 0; x < 4; x++){
            String choice = (String)request.getParameter(CHOICE_FIELD[x]);
            newChoices[x] = choice;
        }
        this.setExamLoc(location);
        this.setQuestionNum(qNum);
        this.setQuestion(newQuestion);
        this.setSolution(newSolution);
        this.setChoices(newChoices);
    }
    public void fetchEmpAnswer(javax.servlet.http.HttpServletRequest request, int takeNum) {
        log.write("MultpleChoice.fetchEmpAnswer()");
        // Fetch the character answer the employee entered.
        String temp = (String)request.getParameter(this.questionNum + "_" + ENTRY_TYPE_CODE);
        // Figure out where in the array this answer is.
        int arrLoc = Arrays.binarySearch(CHOICE_FIELD, temp);
        // Fetch the choice in this array loc
        this.empAnswer = choices[arrLoc];
        this.takeNum = takeNum;
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum){
        log.write("MultipleChoice.displayForm(" + currentQNum + ")");
        String form = "\t<TABLE CLASS=QUESTION>\n\t\t<TR>\n"
            + "\t\t\t<TD WIDTH=\"" + RADIO_TD_WIDTH + "\">" 
            + currentQNum + ")</TD>\n"
            + "\t\t\t<TD WIDTH=\"" + CHOICE_TD_WIDTH + "\"> \n            "
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp\n"
            + "   \t\t\t<INPUT TYPE=\"text\" NAME=\""
            + QUESTION_FIELD + "\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH + "\">\n"
            + "\t\t\t</TD>\n\t\t</TR>\n";
        for(int x = 0; x < 4; x++){
            form += "\t\t<TR>\n";
            if(x == 0){
                 form += "\t\t\t<TD WIDTH=\"" + RADIO_TD_WIDTH 
                 + "\">Answer:</TD>\n";
            }
            else{
                 form += "\t\t\t<TD WIDTH=\"" + RADIO_TD_WIDTH 
                 + "\">&nbsp;</TD>\n";
            }
            form += "\t\t\t<TD WIDTH=\"" + CHOICE_TD_WIDTH + "\">"
                 + "<INPUT TYPE=\"radio\" NAME=\"" + SOLUTION_FIELD
                 +  "\" VALUE=\"" + LETTER[x] + "\">\n"
                 + "            " 
                 + "<INPUT TYPE=\"text\" NAME=\"" + CHOICE_FIELD[x] 
                 + "\" SIZE=\"" + CHOICE_TEXT_FIELD_WIDTH + "\">\n"
                 + "\t\t\t</TD>\n"
                 + "\t\t</TR>\n";
        }
	form += "\t</TABLE>\n";
        return form;
    }
    public String displayGraded() {
        log.write("MultipleChoice.displayGraded()");
        String display;
        display = "\n\t<TABLE CLASS=\"question\">\n\t\t<TR>\n"
            + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n         "
            + "<TD colspan=\"" + LETTER_TD_WIDTH + "\" WIDTH=\"" + QUESTION_TD_WIDTH + "\">" 
            + this.question + "</TD>\n\t\t</TR>\n      ";
        for(int x=0; x < 4; x++){
            display += "<TR>\n"
                    + "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">";
            if(this.empAnswer.compareTo(choices[x]) == 0){
                if(this.empCorrect)
                    display += "<IMG SRC=\"./images/check_mark.gif\">";
                else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            }
            display += "</TD>\n         "
                    + "<TD VALIGN=\"top\" WIDTH=\"" + QUESTION_TD_WIDTH + "\">";
            if(this.empAnswer.compareTo(choices[x]) == 0)
                display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
            else
                display += LETTER[x] + ") " + choices[x];
            display += "</TD>\n\t\t</TR>\n      ";
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToMod() {
        log.write("MultipleChoice.displayToMod()");
        return null;
    }
    public String displayToTake() {
        log.write("MultipleChoice.displayToTake()");
        String display;
        display = "\n\t<TABLE CLASS=\"question\">\n\t\t<TR>\n"
            + "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n"
            + "\t\t\t<TD colspan=\"" + LETTER_TD_WIDTH + "\" WIDTH=\"" + QUESTION_TD_WIDTH + "\">" 
            + this.question + "</TD>\n\t\t</TR>\n      ";
        for(int x=0; x < 4; x++){
            display += "\t\t<TR>\n";
            if(x == 0){
                 display += "\t\t\t<TD WIDTH=\"" + RADIO_TD_WIDTH 
                         + "\">&nbsp</TD>\n";
            }
            else{
                 display += "\t\t\t<TD WIDTH=\"" + RADIO_TD_WIDTH 
                         + "\">&nbsp;</TD>\n";
            }
            display += "\t\t\t<TD WIDTH=\"" + CHOICE_TD_WIDTH + "\">"
                 + "<INPUT TYPE=\"radio\" NAME=\"" + this.questionNum + "_" + ENTRY_TYPE_CODE
                 +  "\" VALUE=\"" + CHOICE_FIELD[x] + "\">\n"
                 + "\t\t\t\t" 
                 + choices[x]
                 + "\t\t\t</TD>\n"
                 + "\t\t</TR>\n";
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("MultipleChoice.displayToView()");
        String display;
        display = "\n\t<TABLE CLASS=\"question\">\n\t\t<TR>\n"
            + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
            + this.questionNum + ")</TD>\n         "
            + "<TD colspan=\"" + LETTER_TD_WIDTH + "\" WIDTH=\"" + QUESTION_TD_WIDTH + "\">" 
            + this.question + "</TD>\n\t\t</TR>\n      ";
        for(int x=0; x < 4; x++){
            display += "<TR>\n"
                    + "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\"></TD>\n         "
                    + "<TD VALIGN=\"top\" WIDTH=\"" + QUESTION_TD_WIDTH + "\">";
            if(this.solution.compareTo(choices[x]) == 0)
                display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
            else
                display += LETTER[x] + ") " + choices[x];
            display += "</TD>\n\t\t</TR>\n      ";
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //************************* DB METHODS ***********************//
    protected void createPreparedStatements() {
        log.write("MultipleChoices.createPreparedStatements()");
        try{
            
            updateStmtStr = "UPDATE MULT_CHOICE SET "
                    + "question=?, choice_1=?, choice_2=?, "
                    + "choice_3=?, choice_4=?, solution=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM MULT_CHOICE "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
            
        }catch(Exception e){
            log.write("Exception in MultipleChoice.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB(){
        log.write("MultipleChoice.insertToDB()");        
        String insertStmtStr = "INSERT INTO MULT_CHOICE "
                      + "(exam_num, exam_loc, question, choice_1, "
                      + "choice_2, choice_3, choice_4, solution) VALUES "
                      + "(?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement insertStmt =
                    dbUtil.createPreparedStatement(insertStmtStr);
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.question);
            for(int x=0; x<4; x++){
                insertStmt.setString((4+x), this.choices[x]);
            }      
            insertStmt.setString(8, this.solution);
            insertStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in MultipleChoice.insertToDB(): " + e.toString());
        }catch(Exception e){
            log.write("Exception in MultipleChoice.insertToDB()" 
                + " : " + e.toString());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("MultipleChoice.loadFromDB("+eNum+", "+eLoc+")");
        String selectStmtStr = "SELECT question, choice_1, choice_2, choice_3, "
                      + "choice_4, solution FROM MULT_CHOICE "
                      + "WHERE exam_num = ? AND exam_loc = ?";
        try{
            PreparedStatement selectStmt = 
                    dbUtil.createPreparedStatement(selectStmtStr);
            selectStmt.setInt(1, eNum);
            selectStmt.setInt(2, eLoc);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                this.setExamNum(eNum);
                this.setExamLoc(eLoc);
                this.setQuestion(rs.getString(1));
                String[] theChoices = new String[4];
                for(int k=2; k < 6; k++){
                    theChoices[k-2] = rs.getString(k);
                }
                this.setChoices(theChoices);
                this.setSolution(rs.getString(6));
            }
        }catch(SQLException e){
            log.write("SQLException in MultipleChoice.loadFromDB() : " 
                    + e.getMessage());
        }catch(Exception e){
            log.write("Exception in MultipleChoice.loadFromDB() : " 
                    + e.getMessage());
        }
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    
    public void insertTakeToDB(int takeNum){
        log.write("MultipleChoice.insertTakeToDB()");
        try{
            this.insertTakeStmt.setInt(1, takeNum);
            this.insertTakeStmt.setInt(2, this.examLoc);
            this.insertTakeStmt.setInt(3, 0);
            this.insertTakeStmt.setString(4, this.empAnswer);
            if(this.empAnswer.compareTo(this.solution) == 0)
                this.insertTakeStmt.setInt(5, 1);
            else
                this.insertTakeStmt.setInt(5, 0);
            this.insertTakeStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in MultipleChoice.insertTakeToDB() : " 
                    + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        log.write("MultipleChoice.loadTakenFromDB("+eNum+", "+eLoc+", "+tNum+")");
        String selectTakenStr = "SELECT mc.question, mc.choice_1, mc.choice_2, "
                               + "mc.choice_3, mc.choice_4, mc.solution, "
                               + "ea.answer_entered, ea.correct FROM "
                               + "MULT_CHOICE mc, EMP_ANSWER ea, EXAM_TAKE et "
                               + "WHERE et.take_num = ? "
                               + "AND mc.exam_num = et.exam_num "
                               + "AND et.take_num = ea.take_num "
                               + "AND ea.exam_loc = mc.exam_loc "; 
        try{
            PreparedStatement selectTakenStmt = 
                        dbUtil.createPreparedStatement(selectTakenStr);
            selectTakenStmt.setInt(1, tNum);
            ResultSet rs = selectTakenStmt.executeQuery();
            if(rs.next()){
                this.setExamNum(eNum);
                this.setExamLoc(eLoc);
                this.setQuestion(rs.getString(1));
                String[] theChoices = new String[4];
                for(int k=2; k < 6; k++){
                    theChoices[k-2] = rs.getString(k);
                }
                this.setChoices(theChoices);
                this.setSolution(rs.getString(6));
                this.setEmpAnswer(rs.getString(7));
                if(rs.getInt(8) == 0) this.setEmpCorrect(false);
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
    
    //*********************** END DB METHODS *********************//
}
