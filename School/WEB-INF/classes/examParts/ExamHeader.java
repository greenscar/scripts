package examParts;
import java.util.*;
import java.sql.*;
import logging.*;
import db.*;
import daos.*;
public class ExamHeader{
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//
    
    //********************** PROPERTY VARS ***********************//
    private Secretary log;
    private DBUtil dbUtil;
    private int examNum;
    private String examName;    
    private ExamCatDAO categoryDAO;
    // The only reason creatorEmpNum is held here is the DB
    private int creatorEmpNum;
    private int takerEmpNum;
    private int finalGrade;
    private java.util.Date dateTaken;
    private java.util.Date dateCreated;
    private java.util.Date dateLastMod;
    private boolean isActive = false;
    private boolean displayAfterTaking;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** QUERY STRINGS **********************//
    protected static String insertStmtStr;
    protected static String updateStmtStr;
    protected static String deleteStmtStr;
    protected static String selectStmtStr;
    protected static String selectViaTakeNumStmtStr;
    protected String selectListStmtStr;
    protected String selectViaExamNumStmtStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    protected PreparedStatement insertStmt;
    protected PreparedStatement updateStmt;
    protected PreparedStatement deleteStmt;
    protected PreparedStatement selectStmt;
    protected PreparedStatement selectViaTakeNumStmt;
    protected PreparedStatement selectListStmt;
    protected PreparedStatement selectViaExamNumStmt;
    //***************** END PREPARED STATEMENTS ******************//
    
    //*********************** CONSTRUCTORS ***********************//
    public ExamHeader() {
        log.write("ExamHeader constructor");
        dbUtil = new DBUtil();
        log = new Secretary();
        this.categoryDAO = new ExamCatDAO();
        this.createPreparedStatements();
        log.write("END ExamHeader constructor");
    }
    public ExamHeader(int eNum, String examName, String catCode, int creator, 
                        java.util.Date date, boolean isActive, boolean disp){
        log.write("ExamHeader(" + eNum + ", " + examName + ", " + catCode + ", " 
                + creator + ", " + date + ", " + isActive + ", " 
                + disp + ") constructor");
        dbUtil = new DBUtil();
        log = new Secretary();
        this.examNum = eNum;
        log.write("ExamHeader.examNum = " + this.examNum);
        this.examName = examName;
        this.categoryDAO = new ExamCatDAO(catCode);
        this.creatorEmpNum = creator;
        this.dateCreated = date;
        this.dateLastMod = date;
        this.isActive = isActive;
        this.displayAfterTaking = disp;
        this.createPreparedStatements();
    }
    public ExamHeader(int eNum){
        dbUtil = new DBUtil();
        log = new Secretary();
        this.examNum = eNum;
        this.createPreparedStatements();
        this.categoryDAO = new ExamCatDAO();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    //********************* setXXX *********************//
    public void setExamNum(int x){
        log.write("ExamHeader.setExamNum("+x+")");
        this.examNum = x;
    }
    public void setExamName(String n){
        log.write("ExamHeader.setExamName("+n+")");
        this.examName = n;
    }
    public void setCategoryName(String name){
        this.categoryDAO.setName(name);
    }
    public void setCategory(String code, String name){
        log.write("ExamHeader.setCategory("+code+", "+name+")");
        this.categoryDAO.setCode(code);
        this.categoryDAO.setName(name);
    }
    public void setCategoryViaCode(String id){
        log.write("ExamHeader.setCategoryViaCode("+id+")");
        this.categoryDAO.setCode(id);
        this.categoryDAO.loadNameViaCode();
        
    }
    public void setCategoryViaName(String n){
        log.write("ExamHeader.setCategoryName("+n+")");
        this.categoryDAO.setName(n);
        this.categoryDAO.getCode();
    }
    /*
    public void setCategory(ExamCatVO v){
        log.write("ExamHeader.setCategory(ExamCatVO)");
        this.categoryDAO = v;
    }
     */
    public void setCreatorEmpNum(int num){
        log.write("ExamHeader.setCreatorEmpNum("+num+")");
        this.creatorEmpNum = num;
    }
    public void setDateCreated(java.util.Date d){
        log.write("ExamHeader.setDateCreated("+d+")");
        this.dateCreated = d;
    }
    public void setDateTaken(java.util.Date d){
        log.write("ExamHeader.setDateTaken("+d+")");
        this.dateTaken = d;
    }
    public void setDateLastMod(java.util.Date d){
        log.write("ExamHeader.setDateLastMod("+d+")");
        this.dateLastMod = d;
    }
    public void setIsActive(boolean x){
        log.write("ExamHeader.setIsActive("+x+")");
        this.isActive = x;
    }
    public void setDisplayAfterTaking(boolean x){
        log.write("ExamHeader.setDisplayAfterTaking("+x+")");
        this.displayAfterTaking = x;
    }
    public void setTakerEmpNum(int num){
        log.write("ExamHeader.setTakerEmpNum("+num+")");
        this.takerEmpNum = num;
    }
    public void setFinalGrade(int x){
        this.finalGrade = x;
    }
    //******************* END setXXX *******************//
    
    //********************* getXXX *********************//
    public int getTakerEmpNum(){
        return this.takerEmpNum;
    }
    public int getFinalGrade(){
        return this.finalGrade;
    }
    public int getExamNum(){
        return this.examNum;
    }
    public String getName(){
        return this.examName;
    }
    public java.util.Date getDateLastMod(){
        return this.dateLastMod;
    }
    public boolean getDisplayAfterTaking(){
        return this.displayAfterTaking;
    }
    public boolean getIsActive(){
        return this.isActive;
    }
    public java.util.Date getDateCreated(){
        return this.dateCreated;
    }
    public java.util.Date getDateTaken(){
        return this.dateTaken;
    }
    public String getCategoryCode(){
        return this.categoryDAO.getCode();
    }
    public String getCategoryName(){
        return this.categoryDAO.getName();
    }
    public int getCreatorEmpNum(){
        return this.creatorEmpNum;
    }
    //******************* END getXXX *******************//
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayToView(String creatorName){
        log.write("ExamHeader.displayToView()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Creator:</TD>";
        temp += "<TD>" + creatorName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Category:</TD>";
        temp += "<TD>" + this.getCategoryName() + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Date Created:</TD>";
        temp += "<TD>" + this.dateCreated + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Display After Taking:</TD>";
        temp += "<TD>" + this.displayAfterTaking + "</TD>";
        temp += "</TR>";
        temp += "</TABLE>";
        return temp;
    }
    public String displayToMod(String creatorName, String catName){
        log.write("ExamHeader.displayToMod()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Creator:</TD>";
        temp += "<TD>" + creatorName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Category:</TD>";
        temp += "<TD>" + catName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Date Created:</TD>";
        temp += "<TD>" + this.dateCreated + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Display After Taking:</TD>";
        temp += "<TD>" + this.displayAfterTaking + "</TD>";
        temp += "</TR>";
        temp += "</TABLE>";
        return temp;
    }
    public String displayToTake(String fullName, int empNum){
        log.write("ExamHeader.displayToView()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Name:</TD>";
        temp += "<TD>" + fullName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Employee ID:</TD>";
        temp += "<TD>" + empNum + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Today's Date:</TD>";
        temp += "<TD>" + new java.util.Date() + "</TD>";
        temp += "</TR>\n";
        temp += "</TABLE>";
        return temp;
    }
    public String displayGraded(String takersName){
        log.write("ExamHeader.displayGraded()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Name:</TD>";
        temp += "<TD>" + takersName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Employee ID:</TD>";
        temp += "<TD>" + this.takerEmpNum + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Date Taken:</TD>";
        temp += "<TD>" + this.dateTaken + "</TD>";
        temp += "</TR>\n";
        temp += "</TABLE>";
        return temp;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //************************* DB METHODS ***********************//
    public void createPreparedStatements() {
        log.write("ExamHeader.createPreparedStatements()");
        try{
            insertStmtStr = "INSERT INTO EXAM (exam_num, exam_name, "
                    + "category_code, creator_emp_num, date_created, date_last_mod, "
                    + "is_active, display_after_taking)"
                    + "VALUES(?,?,?,?,?,?,?,?)";
            this.insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            selectStmtStr = "SELECT * FROM EXAM WHERE exam_num = ?";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
            
            selectViaExamNumStmtStr = "SELECT "
                    + " e.exam_num, e.exam_name, e.creator_emp_num,"
                    + " e.date_created, e.date_last_mod, e.is_active, "
                    + " e.display_after_taking, ec.category_name, e.category_code "
                    + "FROM EXAM e, EXAM_CATEGORY ec WHERE "
                    + "e.exam_num = ? AND ec.category_code = e.category_code";
            selectViaExamNumStmt = dbUtil.createPreparedStatement(selectViaExamNumStmtStr);
            
            selectViaTakeNumStmtStr = "SELECT "
                    + "e.exam_num, e.exam_name, ec.category_code, "
                    + "ec.category_name, et.date_taken, et.emp_num, "
                    + "et.final_grade "
                    + "FROM EXAM e, EXAM_CATEGORY ec, EXAM_TAKE et WHERE "
                    + "et.take_num = ? AND ec.category_code = e.category_code "
                    + "AND et.exam_num = e.exam_num";
            selectViaTakeNumStmt = dbUtil.createPreparedStatement(selectViaTakeNumStmtStr);
            
            selectListStmtStr = "SELECT * FROM EXAM";
            this.selectListStmt = dbUtil.createPreparedStatement(selectListStmtStr);
            
            updateStmtStr = "UPDATE EXAM SET exam_name=?, "
                    + "category_code=?, creator_emp_num=?, date_created=?, "
                    + "date_last_mod=?, is_active=?, display_after_taking=? "
                    + "WHERE exam_num = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM EXAM WHERE exam_num = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
        }catch(Exception e){
            log.write("Exception in ExamHeader.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB() {
        log.write("ExamHeader.insertToDB()");
        try{
            insertStmt.setInt(1, this.examNum);
            insertStmt.setString(2, this.examName);
            insertStmt.setString(3, this.categoryDAO.getCode());
            insertStmt.setInt(4, this.creatorEmpNum);
            insertStmt.setFloat(5, dbUtil.getFloatFromDate(this.dateCreated));
            insertStmt.setFloat(6, dbUtil.getFloatFromDate(this.dateLastMod));
            if(this.isActive)
                insertStmt.setInt(7, 1);
            else
                insertStmt.setInt(7, 0);
            insertStmt.setBoolean(8, this.displayAfterTaking);
            insertStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeader.insertToDB(): " + e.toString());
        }
    }
    public void loadFromDBViaTakeNum(int tNum){
        log.write("ExamHeader.loadFromDBViaTakeNum(" + tNum + ")");
        Float temp;
        // Load the exam Header
        try{
            selectViaTakeNumStmt.setInt(1, tNum);
            ResultSet rs = selectViaTakeNumStmt.executeQuery();
            if(!(rs.next())) log.write("ERROR IN rs.next()");
            this.setExamNum(rs.getInt(1));
            this.setExamName(rs.getString(2));
            this.setCategory(rs.getString(3), rs.getString(4));
            this.setDateTaken(dbUtil.getDateFromFloat(rs.getFloat(5)));
            this.setTakerEmpNum(rs.getInt(6));
            this.setFinalGrade(rs.getInt(7));
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.loadFromDBViaExamNum() = " + e.toString());
        }
        // End Load Exam Header.
        log.write("ExamHeader.loadFromDBViaExamNum("+examNum+") completed.");
    }
    public void loadFromDBViaExamNum(int examNum){
        log.write("ExamHeader.loadFromDBViaExamNum(" + examNum + ")");
        Float temp;
        // Load the exam Header
        try{
            selectViaExamNumStmt.setInt(1, examNum);
            ResultSet rs = selectViaExamNumStmt.executeQuery();
            if(!(rs.next())){
                log.write("ERROR IN rs.next()");
            }
            this.setExamNum(rs.getInt(1));
            this.setExamName(rs.getString(2));
            this.setCreatorEmpNum(rs.getInt(3));
            this.setDateCreated(dbUtil.getDateFromFloat(rs.getFloat(4)));
            this.setDateLastMod(dbUtil.getDateFromFloat(rs.getFloat(5)));
            if(rs.getInt(6) == 0) this.setIsActive(false);
            else this.setIsActive(true);
            if(rs.getInt(7) == 0) this.setDisplayAfterTaking(false);
            else this.setDisplayAfterTaking(true);
            this.setCategoryViaCode(rs.getString("category_code"));
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.loadFromDBViaExamNum() = " + e.toString());
        }
        // End Load Exam Header.
        log.write("ExamHeader.loadFromDBViaExamNum("+examNum+") completed.");
    }
    public void updateDB() {
        log.write("ExamHeader.updateDB()");
        try{
            updateStmt.setString(1, this.examName);
            updateStmt.setString(2, this.categoryDAO.getCode());
            updateStmt.setString(3, String.valueOf(this.creatorEmpNum));
            updateStmt.setString(4, String.valueOf(this.dateCreated));
            updateStmt.setString(5, String.valueOf(this.dateLastMod));
            updateStmt.setString(6, String.valueOf(this.isActive));
            updateStmt.setString(7, String.valueOf(this.displayAfterTaking));
            updateStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeader.updateDB(): " + e.toString());
        }
    }
    public void deleteFromDB() {
        log.write("ExamHeader.deleteFromDB()");
        try{
            deleteStmt.setString(1, String.valueOf(this.examNum));
            deleteStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeader.deleteFromDB(): " + e.toString());
        }
    }
    
    // Get a Collection list of all Exam Headers (all exams) in the DB
    public Collection getList() {
        Vector v = new Vector();
        ExamHeader eh = null;
        Float temp;
        try{
            ResultSet rs = selectListStmt.executeQuery();
            while(rs.next()){
                eh = new ExamHeader();
                eh.setExamNum(rs.getInt(1));
                eh.setExamName(rs.getString(2));
                eh.setCategoryViaCode(rs.getString(3));
                eh.setCreatorEmpNum(rs.getInt(4));
                eh.setDateCreated(dbUtil.getDateFromFloat(rs.getFloat(5)));
                eh.setDateLastMod(dbUtil.getDateFromFloat(rs.getFloat(6)));
                eh.setIsActive(rs.getBoolean(7));
                eh.setDisplayAfterTaking(rs.getBoolean(8));
                v.add(eh);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamHeader.getList(): " + e.toString());
        }
        return v;
    }
    //*********************** END DB METHODS *********************//
}

    