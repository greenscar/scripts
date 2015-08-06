package examParts;
import javax.servlet.http.*;
import java.sql.*;
public abstract class Question extends ExamEntry{
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//

    //********************** PROPERTY VARS ***********************//
    protected int questionNum;
    protected int takeNum;
    protected String insertTakeStmtStr;
    protected PreparedStatement insertTakeStmt;
    //*********************** CONSTRUCTORS ***********************//
    public Question() {
        super();
        this.createPreparedAnswerStmts();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public int getQuestionNum(){
        return questionNum;
    }
    public void setQuestionNum(int num){
        log.write("Question.setQuestionNum("+num+")");
        this.questionNum = num;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public abstract void processForm(HttpServletRequest request, int currentExamLoc, int currentQuestNum);
    public abstract void fetchEmpAnswer(HttpServletRequest request, int takeNum);
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    //******************** END HTML DISPLAY FXNS ******************//
    
    //*************************** DB FXNS *************************//
    public abstract void insertTakeToDB(int takeNum);
    protected abstract void createPreparedStatements();
    protected void createPreparedAnswerStmts(){
        insertTakeStmtStr = "INSERT INTO EMP_ANSWER(take_num, exam_loc, "
                + "quest_loc, answer_entered, correct) VALUES "
                + "(?, ?, ?, ?, ?);";
        try{
            insertTakeStmt = dbUtil.createPreparedStatement(insertTakeStmtStr);
        }catch(java.lang.Exception e){
                log.write("java.lang.Exception in "
                    + "Question.createPreparedAnswerStmts() : " 
                    + e.toString());
        } 
    }
    //************************* END DB FXNS ***********************//
    public int modQuestionNum(int change){
        log.write("Question.modQuestionNum("+change+")");
        questionNum += change;
        return questionNum;
    }    
    public abstract void loadTakenFromDB(int eNum, int eLoc, int tNum);
}
