package examParts;
import java.sql.*;
import java.util.*;
import db.*;
public class Matching extends Question {
    
    //*********************** STATIC VARS ************************//
    final static public String DESCRIPTION_FIELD = "newMatchingDescription";
    final static public String[] QUESTION_FIELDS = new String[20];
    final static public String[] CHOICE_FIELDS = new String[20];
    final static public String[] SOLUTION_FIELDS = new String[20];
    final static private int MAX_NUM_QUESTIONS = 20;
    final static private int MAX_NUM_CHOICES = 20;
    final static private int NUM_ROWS_PER_COLUMN = 4;
    final static public String ENTRY_TYPE_CODE = "ma";
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
    public Matching (){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        solutions = new Vector();
        questions = new Vector();
        choices = new Vector();
        log.write("Matching CONSTRUCTOR");
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
        log.write("Matching.setDescription("+s+")");
        this.description = s;
    }
    public String getDescription(){
        log.write("Matching.getDescription()");
        return this.description;
    }
    public void setQuestions(Vector newQ){
        log.write("Matching.setQuestions("+newQ.toString()+")");
        this.questions = newQ;
    }
    public Vector getQuestions(){
        log.write("Matching.getQuestions()");
        return this.questions;
    }
    public void setChoices(Vector newC){
        log.write("Matching.setChoices("+newC.toString()+")");
        this.choices = newC;
    }
    public Vector getChoices(){
        log.write("Matching.getChoices()");
        return this.choices;
    }
    public void setSolutions(Vector newS){
        log.write("Matching.setSolutions("+newS.toString()+")");
        this.solutions = newS;
    }
    public Vector getSolutions(){
        log.write("Matching.getSolutions()");
        return this.solutions;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    public void addQuestion(String ques, int cNumOfSol){
        log.write("Matching.addQuestion("+ques+", "+cNumOfSol+")");
        this.questions.add(ques);
        this.solutions.add(choices.elementAt(cNumOfSol));
    }
    public void addChoice(String choice, int cNum){
        log.write("Matching.addChoice("+choice+", "+cNum+")");
        this.choices.add(cNum, choice);
    }
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum){
        log.write("Matching.processform(request, "+location+", "+qNum+")");
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
                    //this.solutions.add(choices.elementAt(arrLoc));
            }
        }
        this.setExamLoc(location);
        this.setQuestionNum(qNum);
    }
    
    public void fetchEmpAnswer(javax.servlet.http.HttpServletRequest request, int takeNum) {
        log.write("Matching.fetchEmpAnswer()");
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
        log.write("Matching.displayform()");
        String form = "\t<TABLE CLASS=QUESTION>\n"
                 + "\t\t<TR>\n\t\t\t"
                 + "</TR>\n\t\t"
                 + "<TR>\n\t\t\t"
                 + "<TD WIDTH=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">" 
                 + currentQNum + ")</TD>\n\t\t\t<TD COLSPAN=\"2\">\n\t\t\t\t"
                 + "<INPUT TYPE=\"TEXT\" SIZE=\"" + QUESTION_TEXT_FIELD_WIDTH
                 + "\" NAME=\"" + DESCRIPTION_FIELD + "\"></TD>\n\t\t</TR>";
        for(int x=0; x < MAX_NUM_QUESTIONS || x < MAX_NUM_CHOICES; x++){
            form += "\n\t\t<TR>\n\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH 
                 + "\" VALIGN=\"top\">\n\t\t\t\t";
            if(MAX_NUM_QUESTIONS > x){
		form += "<INPUT TYPE=\"TEXT\" SIZE=\"1\" NAME=\"" 
                     + SOLUTION_FIELDS[x] + "\">";
            }
            form += "\n\t\t\t</TD>\n\t\t\t<TD VALIGN=\"TOP\" HALIGN=\"LEFT\">";
            if(MAX_NUM_CHOICES > x){
		form += "\n\t\t\t\t<INPUT TYPE=\"TEXT\" NAME=\""
                     + QUESTION_FIELDS[x] + "\" SIZE=\"50%\">\n\t\t\t";
            }
            form += "</TD>\n\t\t\t<TD ALIGN=\"RIGHT\">\n\t\t\t\t";
            if(MAX_NUM_CHOICES > x){
		form += LETTER[x] + ") ";
		form += "<INPUT TYPE=\"TEXT\" NAME=\"" + CHOICE_FIELDS[x] + "\">";
            }
            form += "\n\t\t\t</TD>\n\t\t</TR>\n";
		
	}		
	form += "\t</TABLE>\n\t\t<BR>\n";
        return form;
    }
    public String displayGraded() {
        log.write("Matching.displayToView()");
        String display = "";
        display = "\t<TABLE CLASS=QUESTION>\n"
             + "<TR>\n\t\t\t"
	     + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">" 
             + this.questionNum + ")</TD>\n\t\t\t<TD COLSPAN=\"3\">\n\t\t\t\t"
             + this.description + "</TD>\n\t\t</TR>";
        for(int x=0; x < questions.size() || x < choices.size(); x++){
            display += "\n\t\t<TR>\n\t\t\t"
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\">";
            if(((Boolean)(empCorrect.elementAt(x))).booleanValue())
                display += "<IMG SRC=\"./images/check_mark.gif\">";
            else
                display += "<IMG SRC=\"./images/x_mark.gif\">";
            display += "</TD>\n\t\t\t"
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
                    + "\n\t\t\t\t";
            // Print Solution
            if(solutions.size() > x){
		display += "<B><U> " + (Character)empAnswers.elementAt(x) + " </U></B>";
            }
            display += "\n\t\t\t</TD>\n\t\t\t<TD VALIGN=\"TOP\" HALIGN=\"LEFT\">";
            // Print question
            if(questions.size() > x){
		display += (String)questions.elementAt(x);
            }
            display += "</TD>\n\t\t\t<TD ALIGN=\"LEFT\">\n\t\t\t\t";
            // Print choice
            if(choices.size() > x){
                display += LETTER[x] + ") "
                        + (String)choices.elementAt(x);
            }
            display += "\n\t\t\t</TD>\n\t\t</TR>\n";
		
	}		
	display += "\t</TABLE>\n<BR>\n";
        return display;
    }
    public String displayToMod() {return null;}
    public String displayToTake() {
        log.write("Matching.displayToTake()");
        String display;
        int numQs = this.questions.size();
        int numCs = this.choices.size();
        display = "\t<TABLE CLASS=QUESTION>\n"
             + "\t\t<TR>\n\t\t\t"
	     + "</TR>\n\t\t"
             + "<TR>\n\t\t\t"
	     + "<TD WIDTH=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">" 
             + this.questionNum + ")</TD>\n\t\t\t<TD COLSPAN=\"2\">\n\t\t\t\t"
             + this.description + "</TD>\n\t\t</TR>";
        for(int x=0; x < numQs || x < numCs; x++){
            display += "\n\t\t<TR>\n\t\t\t<TD WIDTH=\"" + LETTER_TD_WIDTH 
                 + "\" VALIGN=\"top\">\n\t\t\t\t";
            // Print solution box
            if(numQs > x){
		display += "<INPUT TYPE=\"TEXT\" SIZE=\"1\" NAME=\"" 
                    + this.questionNum + "_" + ENTRY_TYPE_CODE + "_" 
                    + SOLUTION_FIELDS[x] + "\">";
            }
            display += "\n\t\t\t</TD>\n\t\t\t<TD VALIGN=\"TOP\" HALIGN=\"LEFT\">";
            // Print question
            if(numQs > x){
		display += (String)questions.elementAt(x);
            }
            display += "</TD>\n\t\t\t<TD ALIGN=\"RIGHT\">\n\t\t\t\t";
            // Print choice
            if(numCs > x){
                display += LETTER[x] + ") "
                        + (String)choices.elementAt(x);
            }
            display += "\n\t\t\t</TD>\n\t\t</TR>\n";
            display += "\n\t\t\t</TD>\n\t\t</TR>\n";
		
	}		
	display += "\t</TABLE>\n\t\t<BR>\n";
        return display;
    }
    public String displayToView() {
        log.write("Matching.displayToView()");
        String display = "";
        display = "\t<TABLE CLASS=QUESTION>\n"
             + "<TR>\n\t\t\t"
	     + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">" 
             + this.questionNum + ")</TD>\n\t\t\t<TD COLSPAN=\"3\">\n\t\t\t\t"
             + this.description + "</TD>\n\t\t</TR>";
        for(int x=0; x < questions.size() || x < choices.size(); x++){
            display += "\n\t\t<TR>\n\t\t\t"
                    // A spacer to go above the qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\"></TD>\n\t\t\t" 
                    // END spacer above qnum
                    + "<TD WIDTH=\"" + LETTER_TD_WIDTH + "\" VALIGN=\"top\">"
                    + "\n\t\t\t\t";
            // Print Solution
            if(solutions.size() > x){
		display += "<B><U> " + (Character)solutions.elementAt(x) + " </U></B>";
            }
            display += "\n\t\t\t</TD>\n\t\t\t<TD VALIGN=\"TOP\" HALIGN=\"LEFT\">";
            // Print question
            if(questions.size() > x){
		display += (String)questions.elementAt(x);
            }
            display += "</TD>\n\t\t\t<TD ALIGN=\"LEFT\">\n\t\t\t\t";
            // Print choice
            if(choices.size() > x){
                display += LETTER[x] + ") "
                        + (String)choices.elementAt(x);
            }
            display += "\n\t\t\t</TD>\n\t\t</TR>\n";
		
	}		
	display += "\t</TABLE>\n<BR>\n";
        return display;
    }
    //******************** END HTML DISPLAY FXNS ******************//
    
    //*************************** DB FXNS *************************//
    protected void createPreparedStatements() {
        log.write("Matching.createPreparedStatements()");
        try{
            /******************** INSERTS ********************/
            insertStmtStr = "INSERT INTO MATCHING "
                       + "(exam_num, exam_loc, description) "
                       + "VALUES (?, ?, ?)";
            insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            insertQuestionsStmtStr = "INSERT INTO MATCHING_QUESTION "
                       + "(exam_num, exam_loc, quest_loc, question, "
                       + "choice_loc_of_solution) VALUES (?, ?, ?, ?, ?)";
            insertQuestionsStmt = dbUtil.createPreparedStatement(insertQuestionsStmtStr);
            
            insertChoicesStmtStr =  "INSERT INTO MATCHING_CHOICE "
                       + "(exam_num, exam_loc, choice_loc, choice) "
                       + "VALUES (?, ?, ?, ?)";
            insertChoicesStmt = dbUtil.createPreparedStatement(insertChoicesStmtStr);
            /***************** END INSERTS *******************/
            /******************* SELECTS *********************/
            selectStmtStr = "SELECT m.description, mq.question, "
                    + "mq.choice_loc_of_solution, mc.choice_loc, "
                    + "mc.choice FROM "
                    + "MATCHING m, MATCHING_QUESTION mq, MATCHING_CHOICE mc "
                    + "WHERE m.exam_num = mq.exam_num "
                    + "AND mq.exam_num = mc.exam_num "
                    + "AND m.exam_loc = mq.exam_loc "
                    + "AND mq.exam_loc = mc.exam_loc "
                    + "AND m.exam_num = ? AND m.exam_loc = ? "
                    + "ORDER BY mc.choice_loc";
            selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
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
            log.write("Exception in Matching.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB() {
        log.write("Matching.insertToDB()");
        try{
            // INSERT THE DESCRIPTION
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.description);
            insertStmt.executeUpdate();
            log.write(insertStmt.toString());
            Character temp;
            // INSERT THE QUESTIONS
            for(int i=0; i < questions.size(); i++){
                //int solutionNum = choices.indexOf(solutions.elementAt(i)) + 1;
                temp = (Character)(solutions.elementAt(i));
                int solutionNum = Arrays.binarySearch(LETTER, temp.charValue()) + 1;
                insertQuestionsStmt.setInt(1, this.examNum);
                insertQuestionsStmt.setInt(2, this.examLoc);
                insertQuestionsStmt.setInt(3, i);
                if(i < questions.size())
                    insertQuestionsStmt.setString(4, (String)this.questions.elementAt(i));
                else
                    insertQuestionsStmt.setString(4, "N/A");
                insertQuestionsStmt.setInt(5, solutionNum);
                insertQuestionsStmt.executeUpdate();
                log.write(insertQuestionsStmt.toString());
            }
            // INSERT THE CHOICES
            for(int i=0; i < this.choices.size(); i++){
                insertChoicesStmt.setInt(1, this.examNum);
                insertChoicesStmt.setInt(2, this.examLoc);
                insertChoicesStmt.setInt(3, (i + 1));
                insertChoicesStmt.setString(4, (String)choices.elementAt(i));
                insertChoicesStmt.executeUpdate();
                log.write(insertChoicesStmt.toString());
            }
        }catch(SQLException e){
            log.write("SQLException in Matching.insertToDB(): " + e.toString());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("Matching.loadFromDB("+eNum+", "+eLoc+")");
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
        //dbUtil.dumpData(rs, log);
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
        log.write("questions = " + questions.toString());
        log.write("choices = " + choices.toString());
        log.write("solutions = " + solutions.toString());
         
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    public void insertTakeToDB(int takeNum){
        log.write("Matching.insertTakeToDB()");
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
            log.write("SQLException in Matching.insertTakeToDB() : " + e.getMessage());
        }  
    }
    
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        log.write("Matching.loadTakenFromDB("+eNum+", "+eLoc+", "+tNum+")");
        //String qs[] = new String[MAX_NUM_QUESTIONS];
        questions = new Vector();
        solutions = new Vector();
        empAnswers = new Vector();
        Hashtable choicesTemp = new Hashtable();
        Hashtable empCorrectTemp = new Hashtable();
        this.description = "";
        this.setExamNum(eNum);
        this.setExamLoc(eLoc);
        String selectTakenStr = "SELECT ma.description, maq.question, "
              + "maq.choice_loc_of_solution, mac.choice_loc, mac.choice, "
              + "ea.answer_entered, ea.correct FROM (EMP_ANSWER ea INNER JOIN "
              + "EXAM_TAKE et ON ea.take_num = et.take_num) "
              + "INNER JOIN (MATCHING ma INNER JOIN "
              + "(MATCHING_CHOICE mac INNER JOIN "
              + "MATCHING_QUESTION maq ON (mac.choice_loc = "
              + "maq.choice_loc_of_solution) AND (mac.exam_loc = "
              + "maq.exam_loc) AND (mac.exam_num = maq.exam_num)) "
              + "ON (ma.exam_loc = mac.exam_loc) AND (ma.exam_num = "
              + "mac.exam_num)) ON (ma.exam_loc = ea.exam_loc) AND "
              + "(ea.quest_loc = maq.quest_loc) AND (ea.exam_loc = "
              + "mac.exam_loc) AND (et.exam_num = ma.exam_num) "
              + "WHERE (((et.take_num)=?) AND ((ea.exam_loc)=?))"
              + "ORDER BY maq.quest_loc;";
        
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
