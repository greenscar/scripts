package examParts;
import java.sql.*;
import java.util.*;
import db.*;

public class WordBank extends Question{
    
    //*********************** STATIC VARS ************************//
    final static public String DESCRIPTION_FIELD = "newWordBankDescription";
    final static public String[] QUESTION_FIELDS = new String[20];
    final static public String[] CHOICE_FIELDS = new String[20];
    final static public String[] SOLUTION_FIELDS = new String[20];
    final static private int MAX_NUM_QUESTIONS = 20;
    final static private int MAX_NUM_CHOICES = 20;
    final static private int NUM_ROWS_PER_COLUMN = 4;
    final static public String ENTRY_TYPE_CODE = "wb";
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    private String description;
    private Vector questions;
    private Vector choices;
    private Vector solutions;
    private Vector empAnswers;
    private Vector empCorrect;
    //******************** END PROPERTY VARS *********************//
    //*********************** QUERY STRINGS **********************//
    private static String insertStmtStr;
    private static String insertQuestionsStmtStr;
    private static String insertChoicesStmtStr;
    
    private static String selectStmtStr;
    private static String selectQuestionsStmtStr;
    private static String selectChoicesStmtStr;
    
    private static String updateStmtStr;
    private static String updateQuestionsStmtStr;
    private static String updateChoicesStmtStr;
    
    private static String deleteStmtStr;
    private static String deleteQuestionsStmtStr;
    private static String deleteChoicesStmtStr;
        
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement insertStmt;
    private PreparedStatement insertQuestionsStmt;
    private PreparedStatement insertChoicesStmt;
    
    private PreparedStatement selectStmt;
    private PreparedStatement selectQuestionsStmt;
    private PreparedStatement selectChoicesStmt;
    
    private PreparedStatement updateStmt;
    private PreparedStatement updateQuestionsStmt;
    private PreparedStatement updateChoicesStmt;
    
    private PreparedStatement deleteStmt;
    private PreparedStatement deleteQuestionsStmt;
    private PreparedStatement deleteChoicesStmt;
    //***************** END PREPARED STATEMENTS ******************//
 
    //*********************** CONSTRUCTORS ***********************//
    public WordBank (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        solutions = new Vector();
        questions = new Vector();
        choices = new Vector();
        for(int x=0; x<MAX_NUM_QUESTIONS; x++){
            String sName = "solution_" + LETTER[x];
            java.lang.reflect.Array.set(SOLUTION_FIELDS, x, sName);
            String qName = "question_" + LETTER[x];
            java.lang.reflect.Array.set(QUESTION_FIELDS, x, qName);
        }
        for(int x=0; x<MAX_NUM_CHOICES; x++){
            String cName = "choice_" + LETTER[x];
            java.lang.reflect.Array.set(CHOICE_FIELDS, x, cName);
        }
        this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setDescription(String s){
        this.description = s;
    }
    public String getDescription(){
        return this.description;
    }
    public void setQuestions(Vector newQ){
        this.questions = newQ;
    }
    public Vector getQuestion(){
        return this.questions;
    }
    public void setChoices(Vector newC){
        this.choices = newC;
    }
    public Vector getChoices(){
        return this.choices;
    }
    public void setSolution(Vector newS){
        this.solutions = newS;
    }
    public Vector getSolutions(){
        return this.solutions;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum){
        log.write("WordBank.processform(request, "+location+", "+qNum+")");
        java.util.Enumeration e = request.getParameterNames();
        this.setDescription((String)request.getParameter(DESCRIPTION_FIELD));
        String toAdd = "";
        String question="", solution="", choice="";
        for(int loc = 0; loc < MAX_NUM_CHOICES; loc++){
            toAdd = (String)request.getParameter("choice_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
                this.choices.add(toAdd);
        }
        for(int loc = 0; loc < MAX_NUM_QUESTIONS; loc++){
            toAdd = (String)request.getParameter("question_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
                this.questions.add(toAdd);
            toAdd = (String)request.getParameter("solution_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0){
                char answerChar = (toAdd.toUpperCase().toCharArray())[0];
                int arrLoc = Arrays.binarySearch(LETTER, answerChar);
                if(choices.size() >= arrLoc)
                    this.solutions.add(new Character(answerChar));
                //log.write("solutions.add("+temp+")");
                //this.solutions.add(new Character(temp));
                //this.solutions.add(choices.elementAt(arrLoc));
            }
        }
        this.setExamLoc(location);
        this.setQuestionNum(qNum);
    }
    public void fetchEmpAnswer(javax.servlet.http.HttpServletRequest request, int takeNum) {
        log.write("WordBank.fetchEmpAnswer()");
        this.takeNum = takeNum;
        String paramName;
        empAnswers = new Vector(this.solutions.size());
        for(int x=0; x < this.solutions.size(); x++){
            paramName = this.questionNum + "_" + ENTRY_TYPE_CODE 
                        + "_" + SOLUTION_FIELDS[x];
            char answ = ((String)(request.getParameter(paramName))).charAt(0);
            this.empAnswers.add(new Character(Character.toUpperCase(answ)));
        }
        
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum) {
        log.write("WordBank.displayform()");
        String echo = "\t<table CLASS=QUESTION>\n" 
            + "\t\t<TR><TD>\n"
            + "\t\t<TABLE WIDTH=\"700\" BORDER=\"1\">\n";
        //DISPLAY THE BOXES FOR THE CHOICES.
        for(int nc=0; nc< MAX_NUM_CHOICES; ){
            echo += "\t\t<TR>\n";
            for(int x=0; x<3; x++, nc++){
                if(nc < MAX_NUM_CHOICES){
                    echo += "\t\t\t<TD WIDTH = 187>" + LETTER[nc]
                    + ")&nbsp<INPUT TYPE=\"TEXT\" SIZE=\"" + BLANK_WIDTH + "\" NAME=\""
                    + CHOICE_FIELDS[nc] + "\"></TD>\n";
                }
                else
                    echo += "\t\t\t<TD WIDTH = 187>&nbsp</TD>\n";
            }
            echo += "\t\t</TR>\n";
        }
        echo += "\t\t</TABLE>\n"
             + "\t\t</TD></TR>\n"
             //DISPLAY THE QUESTION BLANK
             + "\t\t<TR>\n"
             + "\t\t\t<TD COLSPAN=\"3\">" + currentQNum 
             + ") <INPUT TYPE=\"TEXT\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH + "\" NAME=\"" 
             + DESCRIPTION_FIELD + "\"></TD>\n\t\t</TR>\n"
             //DISPLAY THE BOXES FOR THE QUESTIONS
             + "\t\t<TR><TD>\n"
             + "\t\t<TABLE WIDTH=\"700\" BORDER=\"1\">\n"
             + "\t\t<TR>\n";
        for(int nb=0;nb<MAX_NUM_QUESTIONS;){
            echo += "\t\t<TR>\n";
            for(int x=0;x<1; x++, nb++){
                if(nb < MAX_NUM_QUESTIONS){
                    echo += "\t\t\t<TD>\n"
                         + "\t\t\t\t<INPUT TYPE=\"TEXT\" SIZE=\"" + LETTER_TD_WIDTH + "\" NAME=\"" 
                         + SOLUTION_FIELDS[nb] + "\">\n"
                         + "\t\t\t\t<INPUT TYPE=\"TEXT\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH + "\" NAME=\"" 
                         + QUESTION_FIELDS[nb] + "\">\n"
                         + "\t\t\t</TD>\n";
                }
                else
                    echo += "\t\t\t<TD>&nbsp</TD>\n";
            }
            echo += "\t\t</TR>\n";
        }
        echo += "\t\t</TR>\n\t\t</TABLE>\n\t\t</TD></TR>\n"
             + "\t</TABLE>\n\t<BR>\n";
        return echo;
    }
    public String displayGraded() {
        log.write("WordBank.displayGraded()");
        String display = "\t<TABLE CLASS=QUESTION>\n";
        //DISPLAY THE CHOICES
        for(int nc=0;nc<choices.size();){
            display += "\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">&nbsp</TD>\n         ";
                    // END spacer above qnum
            for(int x=0;x<3; x++, nc++){
                if(nc < choices.size())
                    display += "\t\t\t<TD WIDTH = 187>&nbsp" + LETTER[nc] 
                    + ")" + (String)choices.elementAt(nc) + "</TD>\n";
                else
                    display += "\t\t\t<TD WIDTH = 187>&nbsp</TD>\n";
            }
            display += "\t\t</TR>\n";
        }
        log.write("choices done");
        display += "\t\t</TD></TR>\n"
                //DISPLAY THE DESCRIPTION
                + "\t\t<TR>\n"
                + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">" + this.questionNum+ ")</TD>\n         "
                + "\t\t\t<TD COLSPAN=\"3\">" + this.description + "</TD>\n"
                + "\t\t</TR>\n"
                //DISPLAY THE QUESTIONS
                + "\t\t<TR><TD>\n";
        log.write("description done");
        for(int nb=0;nb<questions.size();nb++){
            display += "\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">";
            if(((Boolean)(empCorrect.elementAt(nb))).booleanValue())
                display += "<IMG SRC=\"./images/check_mark.gif\">";
            else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            display += "</TD>\n         " 
                    // END spacer above qnum
                    + "\t\t\t<TD COLSPAN=2>\n<B>"
                    + "\t\t\t\t<U>&nbsp"
                    + (Character)(empAnswers.elementAt(nb))
                    + " &nbsp</U></B>"
                    //+ "</TD>\n"
                    //+ "\t\t\t\t<TD>" 
                    + (String)(questions.elementAt(nb))
                    + "\n\t\t\t</TD>\n\t\t<TD></TD>\n</TR>\n";
        }    
        log.write("questions done");
        display += "\t\t</TD></TR>\n"
                + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToMod() {return null;}
    public String displayToTake() {
        log.write("WordBank.displayToTake()");
        String display = "\t<TABLE CLASS=QUESTION>\n"
                + "\t\t<TR>\n\t\t\t<TD>\n";
        //DISPLAY THE CHOICES
        for(int nc=0;nc<choices.size();){
            display += "\t\t\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "\t\t\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n";
                    // END spacer above qnum
            for(int x=0;x<3; x++, nc++){
                if(nc < choices.size())
                    display += "\t\t\t\t\t<TD WIDTH = 187>&nbsp" + LETTER[nc] 
                    + ")" + (String)choices.elementAt(nc) + "</TD>\n";
                else
                    display += "\t\t\t\t\t<TD WIDTH = 187>&nbsp</TD>\n";
            }
            display += "\t\t\t\t</TR>\n";
        }
        log.write("choices done");
        display += "\t\t\t</TD>\n\t\t</TR>\n"
                //DISPLAY THE DESCRIPTION
                + "\t\t<TR>\n"
                + "\t\t\t<TD COLSPAN=\"3\">" + this.questionNum 
                + ")" + this.description + "</TD>\n"
                + "\t\t</TR>\n"
                //DISPLAY THE QUESTIONS
                + "\t\t<TR>\n\t\t\t<TD>\n";
        log.write("description done");
        for(int nb=0;nb<questions.size();nb++){
            display += "\t\t\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "\t\t\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n" 
                    // END spacer above qnum
                    + "\t\t\t\t\t<TD colspan=2>\n"
                    + "\t\t\t\t\t\t<INPUT TYPE=\"TEXT\" SIZE=\"1\" NAME=\"" 
                    + this.questionNum + "_" + ENTRY_TYPE_CODE + "_" 
                    + SOLUTION_FIELDS[nb] + "\">\n"
                    + "\t\t\t\t\t\t&nbsp&nbsp"
                    + (String)questions.elementAt(nb) + "\n"
                    + "\t\t\t\t\t</TD>\n"
                    + "\t\t\t\t\t<TD></TD>\n"
                    + "\t\t\t\t</TR>\n";
        }    
        log.write("questions done");
        display += "\t\t\t</TD>\n\t\t</TR>\n"
                + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("WordBank.displayToView()");
        String display = "\t<TABLE CLASS=QUESTION>\n";
        //DISPLAY THE CHOICES
        for(int nc=0;nc<choices.size();){
            display += "\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">&nbsp</TD>\n         ";
                    // END spacer above qnum
            for(int x=0;x<3; x++, nc++){
                if(nc < choices.size())
                    display += "\t\t\t<TD WIDTH = 187>&nbsp" + LETTER[nc] 
                    + ")" + (String)choices.elementAt(nc) + "</TD>\n";
                else
                    display += "\t\t\t<TD WIDTH = 187>&nbsp</TD>\n";
            }
            display += "\t\t</TR>\n";
        }
        log.write("choices done");
        display += "\t\t</TD></TR>\n"
                //DISPLAY THE DESCRIPTION
                + "\t\t<TR>\n"
                + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">" + this.questionNum+ ")</TD>\n         "
                + "\t\t\t<TD COLSPAN=\"3\">" + this.description + "</TD>\n"
                + "\t\t</TR>\n"
                //DISPLAY THE QUESTIONS
                + "\t\t<TR><TD>\n";
        log.write("description done");
        for(int nb=0;nb<questions.size();nb++){
            display += "\t\t<TR>\n"
                    // A spacer to go above the qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n         " 
                    // END spacer above qnum
                    + "\t\t\t<TD COLSPAN=2>\n<B>"
                    + "\t\t\t\t<U>&nbsp"
                    + (Character)solutions.elementAt(nb)
                    + " &nbsp</U></B>"
                    //+ "</TD>\n"
                    //+ "\t\t\t\t<TD>" 
                    + (String)questions.elementAt(nb)
                    + "\n\t\t\t</TD>\n\t\t<TD></TD>\n</TR>\n";
        }    
        log.write("questions done");
        display += "\t\t</TD></TR>\n"
                + "\t</TABLE>\n\t<BR>\n";
        return display;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //************************* DB METHODS ***********************//
    protected void createPreparedStatements() {
        log.write("WordBank.createPreparedStatements()");
        try{
            /******************** INSERTS ********************/
            insertStmtStr = "INSERT INTO WORD_BANK "
                       + "(exam_num, exam_loc, description) "
                       + "VALUES (?, ?, ?)";
            insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            insertQuestionsStmtStr = "INSERT INTO WORD_BANK_QUESTION "
                       + "(exam_num, exam_loc, quest_loc, question, choice_loc_of_solution) "
                       + " VALUES (?, ?, ?, ?, ?)";
            insertQuestionsStmt = dbUtil.createPreparedStatement(insertQuestionsStmtStr);
            
            insertChoicesStmtStr =  "INSERT INTO WORD_BANK_CHOICE "
                       + "(exam_num, exam_loc, choice_loc, choice) "
                       + "VALUES (?, ?, ?, ?)";
            insertChoicesStmt = dbUtil.createPreparedStatement(insertChoicesStmtStr);
            /***************** END INSERTS *******************/
            /******************* SELECTS *********************/
            selectStmtStr = "SELECT wb.description, wbq.question, "
                    + "wbq.choice_loc_of_solution, wbc.choice_loc, "
                    + "wbc.choice FROM "
                    + "WORD_BANK wb, WORD_BANK_QUESTION wbq, WORD_BANK_CHOICE wbc "
                    + "WHERE wb.exam_num = wbq.exam_num "
                    + "AND wbq.exam_num = wbc.exam_num "
                    + "AND wb.exam_loc = wbq.exam_loc "
                    + "AND wbq.exam_loc = wbc.exam_loc "
                    + "AND wb.exam_num = ? AND wb.exam_loc = ? "
                    + "ORDER BY wbc.choice_loc";
            selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
            /*
            selectQuestionsStmtStr = "SELECT question, choice_loc_of_solution "
                    + "FROM WORD_BANK_QUESTION "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            selectQuestionsStmt = dbUtil.createPreparedStatement(selectQuestionsStmtStr);
            
            selectChoicesStmtStr = "SELECT choice, choice_loc "
                    + "FROM WORD_BANK_CHOICE "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            selectChoicesStmt = dbUtil.createPreparedStatement(selectChoicesStmtStr);
             */
            /***************** END SELECTS *******************/
            /*
            updateQuestionsStmtStr = "UPDATE FILL_IN_BLANK SET question=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateQuestionsStmt = dbUtil.createPreparedStatement(updateQuestionsStmtStr);
            
            deleteQuestionsStmtStr = "DELETE FROM FILL_IN_BLANK "
                + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteQuestionsStmt = dbUtil.createPreparedStatement(deleteQuestionsStmtStr);
            
            selectQuestionsStmtStr = "SELECT * FROM FILL_IN_BLANK "
                + "WHERE exam_num = ? AND exam_loc = ?";
            this.selectQuestionsStmt = dbUtil.createPreparedStatement(selectQuestionsStmtStr);
             */
        }catch(Exception e){
            log.write("Exception in WordBank.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB() {
        log.write("WordBank.insertToDB()");
        try{
            // INSERT THE DESCRIPTION
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.description);
            insertStmt.executeUpdate();
            Character temp;
            // INSERT THE QUESTIONS
            for(int i=0; i < questions.size(); i++){
                //int solutionNum = choices.indexOf(solutions.elementAt(i)) + 1;
                temp = (Character)(solutions.elementAt(i));
                int solutionNum  = Arrays.binarySearch(LETTER, temp.charValue()) + 1;
                insertQuestionsStmt.setInt(1, this.examNum);
                insertQuestionsStmt.setInt(2, this.examLoc);
                insertQuestionsStmt.setInt(3, i);
                insertQuestionsStmt.setString(4, (String)this.questions.elementAt(i));
                insertQuestionsStmt.setInt(5, solutionNum);
                insertQuestionsStmt.executeUpdate();
            }
            // INSERT THE CHOICES
            for(int i=0; i < this.choices.size(); i++){
                insertChoicesStmt.setInt(1, this.examNum);
                insertChoicesStmt.setInt(2, this.examLoc);
                insertChoicesStmt.setInt(3, (i + 1));
                insertChoicesStmt.setString(4, (String)choices.elementAt(i));
                insertChoicesStmt.executeUpdate();
            }
        }catch(SQLException e){
            log.write("SQLException in WordBank.insertToDB(): " + e.toString());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("WordBank.loadFromDB("+eNum+", "+eLoc+")");
        this.solutions.clear();
        this.questions.clear();
        this.choices.clear();
        this.description = "";
        this.setExamNum(eNum);
        this.setExamLoc(eLoc);
        selectStmt.setInt(1, eNum);
        selectStmt.setInt(2, eLoc);
        ResultSet rs = selectStmt.executeQuery();
        String aChoice, aQuestion, descr;
        int cNum;
        Integer cNumOfSol;
        int cCount = 0, sCount = 0, qCount = 0;
        while(rs.next()){
            descr = rs.getString(1);
            aQuestion = rs.getString(2);
            cNumOfSol = new Integer(rs.getInt(3));
            cNum = rs.getInt(4);
            aChoice = rs.getString(5);
            if(this.description == ""){
                this.setDescription(descr);
            }
            if(!(this.choices.contains(aChoice))){
                this.choices.add(cCount, aChoice);
                cCount++;
            }
            if(!(this.questions.contains(aQuestion))){
                this.questions.add(qCount, aQuestion);
                qCount++;
                Character ans = new Character(LETTER[(cNumOfSol.intValue() - 1)]);
                solutions.add(ans);
            }
        }
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    public void insertTakeToDB(int takeNum){
        log.write("WordBank.insertTakeToDB()");
        try{
            this.insertTakeStmt.setInt(1, takeNum);
            this.insertTakeStmt.setInt(2, this.examLoc);
            Character empAnswer, correctAnswer;
            for(int x=0; x < this.empAnswers.size(); x++){
                empAnswer = (Character)(empAnswers.elementAt(x));
                correctAnswer = (Character)(solutions.elementAt(x));
                this.insertTakeStmt.setInt(3, x);
                this.insertTakeStmt.setString(4, empAnswer.toString());
                if(empAnswer.compareTo(correctAnswer) == 0)
                    this.insertTakeStmt.setInt(5, 1);
                else
                    this.insertTakeStmt.setInt(5, 0);
                this.insertTakeStmt.executeUpdate();
            }
        }catch(SQLException e){
            log.write("SQLException in WordBank.insertTakeToDB() : " + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        log.write("WordBank.loadTakenFromDB("+eNum+", "+eLoc+", "+tNum+")");
        //String qs[] = new String[MAX_NUM_QUESTIONS];
        questions = new Vector();
        solutions = new Vector();
        empAnswers = new Vector();
        Hashtable choicesTemp = new Hashtable();
        Hashtable empCorrectTemp = new Hashtable();
        this.description = "";
        this.setExamNum(eNum);
        this.setExamLoc(eLoc);
        String selectTakenStr = "SELECT wb.description, wbq.question, "
              + "wbq.choice_loc_of_solution, wbc.choice_loc, wbc.choice, "
              + "ea.answer_entered, ea.correct FROM (EMP_ANSWER ea INNER JOIN "
              + "EXAM_TAKE et ON ea.take_num = et.take_num) "
              + "INNER JOIN (WORD_BANK wb INNER JOIN "
              + "(WORD_BANK_CHOICE wbc INNER JOIN "
              + "WORD_BANK_QUESTION wbq ON (wbc.choice_loc = "
              + "wbq.choice_loc_of_solution) AND (wbc.exam_loc = "
              + "wbq.exam_loc) AND (wbc.exam_num = wbq.exam_num)) "
              + "ON (wb.exam_loc = wbc.exam_loc) AND (wb.exam_num = "
              + "wbc.exam_num)) ON (wb.exam_loc = ea.exam_loc) AND "
              + "(ea.quest_loc = wbq.quest_loc) AND (ea.exam_loc = "
              + "wbc.exam_loc) AND (et.exam_num = wb.exam_num) "
              + "WHERE (((et.take_num)=?) AND ((ea.exam_loc)=?))"
              + "ORDER BY wbq.quest_loc;";
        try{
            PreparedStatement selectTakenStmt = 
                        dbUtil.createPreparedStatement(selectTakenStr);
            selectTakenStmt.setInt(1, tNum);
            selectTakenStmt.setInt(2, eLoc);
            ResultSet rs = selectTakenStmt.executeQuery();
            String aChoice, aQuestion, descr, empAnsw;
            Integer cLoc, cLocOfSol;
            int eaCount=1;
            int correct;
            while(rs.next()){
                descr = rs.getString(1);
                aQuestion = rs.getString(2);
                cLocOfSol = new Integer(rs.getInt(3));
                cLoc = new Integer(rs.getInt(4));
                aChoice = rs.getString(5);
                empAnsw = rs.getString(6);
                correct = rs.getInt(7);
                if(this.description == ""){
                    this.setDescription(descr);
                }
                if(!(choicesTemp.containsKey(cLoc))){
                    choicesTemp.put(cLoc, aChoice);
                }
                if(!(this.questions.contains(aQuestion))){
                    this.questions.add(aQuestion);
                    Character ans = new Character(LETTER[cLocOfSol.intValue()]);
                    solutions.add(ans);
                    this.empAnswers.add(new Character((empAnsw.charAt(0))));
                    if(correct == 0) empCorrectTemp.put(new Integer(eaCount++), new Boolean(false));
                    else empCorrectTemp.put(new Integer(eaCount++), new Boolean(true));
                }
                /*
                 * Go through the choices & empCorrect Hashtables
                 *  and assign them to Vectors in the correct order.
                 */
            }
            choices = new Vector();
            empCorrect = new Vector();
            String tempS;
            for(int x=1; x<=choicesTemp.size(); x++){
                //log.write("class = " + ((Object)(choicesTemp.get(new Integer(x))).getClass()).toString());
                //log.write(x + " => " + choicesTemp.get(new Integer(x)));
                this.choices.add((String)(choicesTemp.get(new Integer(x))));
            }
            for(int x=1; x<=empCorrectTemp.size(); x++){
                this.empCorrect.add((Boolean)(empCorrectTemp.get(new Integer(x))));
            }
        }catch(SQLException e){
            log.write("SQLException in WordBank.loadTakenFromDB() : " 
                    + e.getMessage());
        }catch(Exception e){
            log.write("Exception in WordBank.loadTakenFromDB()" 
                + " : " + e.getMessage());
        }
    }
    //************************* END DB FXNS ***********************//
}
