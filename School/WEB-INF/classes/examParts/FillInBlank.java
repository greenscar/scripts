package examParts;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import db.*;

public class FillInBlank extends Question {
    
    //*********************** STATIC VARS ************************//
    final static public String QUESTION_FIELD = "newFillInBlankQ";
    final static public String[] SOLUTION_FIELDS = new String[26];
    final static private int MAX_NUM_BLANKS = 15;
    final static private int NUM_ROWS_PER_COLUMN = 4;
    final static public String ENTRY_TYPE_CODE = "fb";
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String question;
    private Vector solutions;
    private Vector empAnswers;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** QUERY STRINGS **********************//
    private static String insertQuestionStmtStr;
    private static String updateQuestionStmtStr;
    private static String deleteQuestionStmtStr;
    private static String selectStmtStr;
    private static String insertSolutionStmtStr;
    private static String updateSolutionStmtStr;
    private static String deleteSolutionStmtStr;
    private static String selectSolutionStmtStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement insertQuestionStmt;
    private PreparedStatement updateQuestionStmt;
    private PreparedStatement deleteQuestionStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement insertSolutionStmt;
    private PreparedStatement updateSolutionStmt;
    private PreparedStatement deleteSolutionStmt;
    private PreparedStatement selectSolutionStmt;
    //***************** END PREPARED STATEMENTS ******************//
    //*********************** CONSTRUCTORS ***********************//
    public FillInBlank (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        solutions = new Vector();
        log.write("FillInBlank CONSTRUCTOR");
        for(int x=0; x<MAX_NUM_BLANKS; x++){
            String name = "solution_" + LETTER[x];
            java.lang.reflect.Array.set(SOLUTION_FIELDS, x, name);
        }
        this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setQuestion(String newQ){
        log.write("FillInBlank.setQuestion("+newQ+")");
        this.question = newQ;
    }
    public String getQuestion(){
        log.write("FillInBlank.getQuestion()");
        return this.question;
    }
    public void setSolution(Vector newS){
        log.write("FillInBlank.setSolution("+newS.toString()+")");
        this.solutions = newS;
    }
    public void addSolution(String s){
        log.write("FillInBlank.addSolution("+s+")");
        this.solutions.add(s);
    }
    public Vector getSolutions(){
        log.write("FillInBlank.getSolutions()");
        return this.solutions;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(HttpServletRequest request, int location, int qNum){
        log.write("FillInBlank.processForm(request, "+location+", "+qNum+")");
        java.util.Enumeration e = request.getParameterNames();
        this.question = (String)request.getParameter(QUESTION_FIELD);
        String empty = "";
        while(e.hasMoreElements()){
            String name = (String)e.nextElement();
            if(name.startsWith("solution")){
                String toAdd = (String)request.getParameter(name);
                if(toAdd.compareTo(empty) != 0){
                    this.solutions.add(toAdd);
                    log.write("this.solutions.add("+toAdd+")");
                }
            }
        }
        this.setExamLoc(location);
        log.write("setQuestionNum("+qNum+")");
        this.setQuestionNum(qNum);
    }
    
    public void fetchEmpAnswer(HttpServletRequest request, int takeNum) {
        log.write("FillInBlank.fetchEmpAnswer()");
        this.takeNum = takeNum;
        String paramName;
        empAnswers = new Vector();
        for(int x=0; x < this.solutions.size(); x++){
            paramName = this.questionNum + "_" + ENTRY_TYPE_CODE 
                        + "_" + SOLUTION_FIELDS[x];
            String answ = (String)(request.getParameter(paramName));
            this.empAnswers.add(answ);
        }
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum) {
        log.write("FillInBlank.displayForm()");
        String form= "\t<TABLE CLASS=QUESTION>\n"
                   + "\t\t<TR>\n\t\t\t<TD WIDTH=\""
                   + Q_NUM_TD_WIDTH + "\">" + currentQNum + ")</TD>\n"
                   + "\t\t\t<TD WIDTH=\"" + QUESTION_TEXT_FIELD_WIDTH 
                   + "\" COLSPAN=\"3\">\n" 
                   + "    \t\t\t<INPUT TYPE=\"text\" NAME=\""
                   + QUESTION_FIELD + "\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH 
                   + "\">\n\t\t\t</TD>\n\t\t</TR>\n";

        // Print MAX_NUM_BLANKS blanks in rows of 3.
        for(int x=0; x < MAX_NUM_BLANKS; x++){
            if(x==0){
                form += "\t\t\t<TD WIDTH=\""  
                     + Q_NUM_TD_WIDTH + "\">Solution:</TD>\n";
            }
            else{
                form += "\t\t\t<TD WIDTH=\"" 
                     + Q_NUM_TD_WIDTH + "\"></TD>\n";
            }
            for(int y=0;(y<3 &&  x<MAX_NUM_BLANKS);y++){
                form += "\t\t\t<TD WIDTH=\"" + CHOICE_TD_WIDTH + "\">\n"
                     //+ "\n\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp\n"
                     + "    \t\t\t<INPUT TYPE=\"text\" NAME=\""
                     + SOLUTION_FIELDS[x] 
                     + "\" SIZE=\"" + BLANK_WIDTH 
                     + "\">\n\t\t\t</TD>\n";
                x++;
            }
            form += "\t\t</TR>\n";
        }
        form += "\t\t</TR>\n\t</TABLE>\n";
        return form;
    }
    public String displayGraded() {
        log.write("FillInBlank.displayToView()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                + "\t\t<TR>\n\t\t\t<TD WIDTH=\"" 
                + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
                + this.questionNum + ")</TD>\n\t\t\t"
                + "<TD colspan=\"2\">" + this.question + "</TD>\n"
                + "\t\t</TR>\n";
        int colNum = 0;
        int size = solutions.size();
        int numCols = 3;
        int x = 0;
        display += "\t\t<TR>\n"
                +  "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n"
                +  "\t\t\t<TD>"
                + "<P STYLE=\"color:green\">"
                + "Employee's Answers</P></TD>\n"
                +  "\t\t\t<TD>"
                + "<P STYLE=\"color:blue\">"
                + "Solutions</P></TD\n"
                +  "\t\t</TR>\n";
        while(x < size){
            // Add the column of dead space on the left.
            display += "\t\t<TR>\n"
                    +  "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n";
            // Add the actual Solutions
            while((size > (NUM_ROWS_PER_COLUMN * colNum)) 
                    && (x < size) && (colNum <= numCols)){
                display += "\t\t\t"
                        + "<TD VALIGN=\"top\"><B><U>&nbsp "
                        + ((String)empAnswers.elementAt(x))
                        + "&nbsp</U></B></TD>\n";
                display += "\t\t\t"
                        + "<TD VALIGN=\"top\">"
                        + ((String)solutions.elementAt(x))
                        + "</TD>\n";
                x++;
                colNum++;
            }
            display += "\t\t</TR>\n";
            colNum = 0;
            display += "</TR>";
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToMod() {return null;}
    public String displayToTake() {
        log.write("FillInBlank.displayToTake()");
        int size = this.solutions.size();
        int colNum = 0;
        int numCols = 3;
        int x = 0;
        int colSpan = size / NUM_ROWS_PER_COLUMN + 1;
        log.write(size + " / " + NUM_ROWS_PER_COLUMN + " + 1 = " + colSpan);
        //log.write("colSpan = " + colSpan);
        String display= "\t<TABLE CLASS=QUESTION>\n"
                   + "\t\t<TR>\n\t\t\t<TD WIDTH=\""
                   + Q_NUM_TD_WIDTH + "\">" + this.questionNum + ")</TD>\n"
                   + "\t\t\t<TD WIDTH=\"" + QUESTION_TD_WIDTH 
                   + "\" COLSPAN=\"" + colSpan + "\">\n" 
                   + "\t\t\t\t" + this.question
                   + "\n\t\t\t</TD>\n\t\t</TR>\n";

        // Print MAX_NUM_BLANKS blanks in rows of 3.
        while(x < size){
            // Add the column of dead space on the left.
            display += "\t\t<TR>\n"
                    +  "\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">&nbsp</TD>\n";
            // Add the actual Solutions
            while((size > (NUM_ROWS_PER_COLUMN * colNum)) 
                    && (x < size) && (colNum <= numCols)){
                display += "\t\t\t<TD VALIGN=\"top\">"
                        + "<INPUT TYPE=\"text\" NAME=\""
                        + this.questionNum + "_" + ENTRY_TYPE_CODE + "_" 
                        + SOLUTION_FIELDS[x] + "\" SIZE=\"" + BLANK_WIDTH 
                        + "\"></TD>\n";
                x++;
                colNum++;
            }
            display += "\t\t</TR>\n";
            colNum = 0;
            //display += "</TR>";
            
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("FillInBlank.displayToView()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                + "\t\t<TR>\n\t\t\t<TD WIDTH=\"" 
                + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
                + this.questionNum + ")</TD>\n\t\t\t"
                + "<TD colspan=\"3\">" + this.question + "</TD>\n"
                + "\t\t</TR>\n";
        int colNum = 0;
        int size = solutions.size();
        int numCols = 3;
        int x = 0;
        while(x < size){
            // Add the column of dead space on the left.
            display += "\t\t<TR>\n\t\t\t"
                    +  "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n";
            // Add the actual Solutions
            while((size > (NUM_ROWS_PER_COLUMN * colNum)) 
                    && (x < size) && (colNum <= numCols)){
                display += " \t\t\t"
                        + "<TD VALIGN=\"top\"><B><U>&nbsp "
                        + ((String)solutions.elementAt(x))
                        + " &nbsp</U></B></TD>\n";
                x++;
                colNum++;
            }
            display += "\t\t</TR>\n";
            colNum = 0;
            display += "</TR>";
        }
        display += "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //*************************** DB FXNS *************************//
    protected void createPreparedStatements() {
        log.write("FillInBlank.createPreparedStatements()");
        try{
            insertQuestionStmtStr = "INSERT INTO FILL_IN_BLANK "
                       + "(exam_num, exam_loc, question) "
                       + "VALUES (?, ?, ?)";
            insertQuestionStmt = 
                    dbUtil.createPreparedStatement(insertQuestionStmtStr);
            
            insertSolutionStmtStr = "INSERT INTO FILL_IN_BLANK_SOLUTION "
                       + "(exam_num, exam_loc, quest_loc, solution) "
                       + "VALUES (?, ?, ?, ?)";
            insertSolutionStmt = 
                    dbUtil.createPreparedStatement(insertSolutionStmtStr);
            
            selectStmtStr = "SELECT q.question, s.solution FROM "
                + "FILL_IN_BLANK q, FILL_IN_BLANK_SOLUTION s "
                + "WHERE q.exam_num = ? AND q.exam_loc = ? "
                + "AND s.exam_num = ? AND s.exam_loc = ? "
                + "ORDER BY s.quest_loc";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
            
            updateQuestionStmtStr = "UPDATE FILL_IN_BLANK SET question=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateQuestionStmt = 
                    dbUtil.createPreparedStatement(updateQuestionStmtStr);
            
            deleteQuestionStmtStr = "DELETE FROM FILL_IN_BLANK "
                + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteQuestionStmt = 
                    dbUtil.createPreparedStatement(deleteQuestionStmtStr);
            
        }catch(Exception e){
            log.write("Exception in FillInBlank.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    
    public void insertToDB() {
        log.write("FillInBlank.insertToDB()");
        try{
            // INSERT THE QUESTION
            insertQuestionStmt.setInt(1, this.examNum);
            insertQuestionStmt.setInt(2, this.examLoc);
            insertQuestionStmt.setString(3, this.question);
            insertQuestionStmt.executeUpdate();
            // INSERT THE SolutionS
            for(int i=0; i < this.solutions.size(); i++){
                insertSolutionStmt.setInt(1, this.examNum);
                insertSolutionStmt.setInt(2, this.examLoc);
                String temp = (String)this.solutions.elementAt(i);
                insertSolutionStmt.setInt(3, i);
                insertSolutionStmt.setString(4, temp);
                insertSolutionStmt.executeUpdate();
            }
        }catch(SQLException e){
            log.write("SQLException in FillInBlank.insertToDB(): " 
                    + e.toString());
        }
        
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("FillInBlank.loadFromDB("+eNum+", "+eLoc+")");
        this.solutions.clear();
        this.question = "";
        this.setExamNum(eNum);
        this.setExamLoc(eLoc);
        selectStmt.setInt(1, eNum);
        selectStmt.setInt(2, eLoc);
        selectStmt.setInt(3, eNum);
        selectStmt.setInt(4, eLoc);
        ResultSet rs = selectStmt.executeQuery();
        while(rs.next()){
            if(this.question == ""){
                this.setQuestion(rs.getString(1));
                log.write("Question = " + this.question);
            }
            this.addSolution(rs.getString(2));
        }
    }
    public void deleteFromDB() {
    }
    public void updateDB() {
    }
    
    public void insertTakeToDB(int takeNum) {
        log.write("FillInBlank.insertTakeToDB()");
        try{
            this.insertTakeStmt.setInt(1, takeNum);
            this.insertTakeStmt.setInt(2, this.examLoc);
            this.insertTakeStmt.setNull(5, java.sql.Types.BIT);
            for(int x=0; x < this.empAnswers.size(); x++){
                this.insertTakeStmt.setInt(3, x);
                this.insertTakeStmt.setString(4, (String)(empAnswers.elementAt(x)));
                this.insertTakeStmt.executeUpdate();
            }
        }catch(SQLException e){
            log.write("SQLException in FillInBlank.insertTakeToDB() : " + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        log.write("FillInBlank.loadTakenFromDB("+eNum+", "+eLoc+", "+tNum+")");
        this.solutions.clear();
        this.question = "";
        this.setExamNum(eNum);
        this.setExamLoc(eLoc);
        this.empAnswers = new Vector();
        String selectTakenStr = "SELECT fbq.question, fbs.solution, "
                               + "ea.answer_entered FROM "
                               + "FILL_IN_BLANK fbq, FILL_IN_BLANK_SOLUTION fbs, "
                               + "EMP_ANSWER ea, EXAM_TAKE et "
                               + "WHERE et.take_num = ? "
                               + "AND fbq.exam_loc = ? "
                               + "AND fbq.exam_num = et.exam_num "
                               + "AND et.take_num = ea.take_num "
                               + "AND ea.exam_loc = fbq.exam_loc "
                               + "AND fbq.exam_num = fbs.exam_num "
                               + "AND fbq.exam_loc = fbs.exam_loc "
                               + "AND ea.quest_loc = fbs.quest_loc";
        try{
            PreparedStatement selectTakenStmt = 
                        dbUtil.createPreparedStatement(selectTakenStr);
            selectTakenStmt.setInt(1, tNum);
            selectTakenStmt.setInt(2, eLoc);
            ResultSet rs = selectTakenStmt.executeQuery();
            while(rs.next()){
                if(this.question == ""){
                    this.setQuestion(rs.getString(1));
                }
                this.addSolution(rs.getString(2));
                this.empAnswers.add(rs.getString(3));
            }
        }catch(SQLException e){
            log.write("SQLException in FillInBlank.loadTakenFromDB() : " 
                    + e.getMessage());
        }catch(Exception e){
            log.write("Exception in FillInBlank.loadTakenFromDB()" 
                + " : " + e.getMessage());
        }
    }
    
    //************************* END DB FXNS ***********************//
    
}
