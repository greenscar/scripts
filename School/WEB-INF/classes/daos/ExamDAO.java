package daos;
import java.beans.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import db.*;
import vos.*;
import beans.*;
import logging.*;
import examParts.*;

public class ExamDAO {
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//
    //********************** PROPERTY VARS ***********************//
    private ExamVO examVO;
    private ExamHeader header;
    private Vector entries;
    private UserDAO creator;
    private UserDAO taker;
    private Secretary log;
    private DBUtil dbUtil;
    
    private String thisEntryTypeCode;
    private String thisEntryTypeName;
    private String thisEntryTypeClassName;
    private String nextEntryTypeCode;
    private String nextEntryTypeName;
    private String nextEntryTypeClassName;
    
    //private UserBean creator;
    private int currentQNum;
    private int currentExamLoc;
    private int examNum;
    private int takeNum;
    //private AdminBean creator;
    // Prepared query strings
    private String selectAllStmtStr;
    private String selectAllActiveStmtStr;
    private String loadExamBodyStr;
    private String selectTypeViaCodeStr;
    private String insertToExamTakeStr;
    private String selectAllTakenStr;
    private String loadTakenExamBodyStr;
    //private String selectViaExamNumStmtStr;
    // Prepared Statements
    private PreparedStatement selectAllStmt;
    private PreparedStatement selectAllActiveStmt;
    private PreparedStatement loadExamBody;
    private PreparedStatement selectTypeViaCodeStmt;
    private PreparedStatement insertToExamTake;
    private PreparedStatement selectAllTaken;
    private PreparedStatement loadTakenExamBody;
    //private PreparedStatement selectViaExamNumStmt;
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public ExamDAO() {
        this.entries = new Vector();
        log = new Secretary();
        dbUtil = new DBUtil();
        createPreparedStatements();
        header = new ExamHeader();
        creator = new UserDAO();
    }
    public ExamDAO(ExamHeader h) {
        this.entries = new Vector();
        log = new Secretary();
        dbUtil = new DBUtil();
        createPreparedStatements();
        header = h;
        creator = new UserDAO();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setCurrentExamLoc(int x){
        log.write("ExamDAO.setCurrentExamLoc("+x+")");
        this.currentExamLoc = x;
    }
    public void setThisEntryTypeCode(String newType){
        log.write("ExamDAO.setThisEntryTypeCode("+newType+")");
        this.thisEntryTypeCode = newType;
    }
    public void setNextEntryTypeCode(String newType){
        log.write("ExamDAO.setNextEntryTypeCode("+newType+")");
        this.nextEntryTypeCode = newType;
    }
    public void setCurrentQNum(int x){
        log.write("ExamDAO.setCurrentQNum("+x+")");
        this.currentQNum = x;
    }
    //************* setXXX FROM ExamHeader *************//
    public void setHeader(ExamHeader h){
        this.header = h;
    }
    public void setCategoryName(String c){
        log.write("ExamBean.setCatName("+c+")");
        this.header.setCategoryViaName(c);
    }
    public void setCategoryCode(String c){
        log.write("ExamBean.setCatName("+c+")");
        this.header.setCategoryViaCode(c);
    }
    public void setCreator(UserDAO c){
        this.creator = c;
    }
    public void setCreatorViaEmpNum(int empNum){
        log.write("ExamDAO.setCreatorViaEmpNum("+empNum+")");
        this.creator.loadViaEmpNum(empNum);
    }
    public void setTakerViaEmpNum(int empNum){
        log.write("ExamDAO.setTakerViaEmpNum("+empNum+")");
        this.taker = new UserDAO();
        this.taker.loadViaEmpNum(empNum);
    }
    public void setExamNum(int newNum){
        log.write("ExamBean.setExamNum("+ newNum +")");
        this.header.setExamNum(newNum);
    }
    public void setExamName(String newExamName){
        log.write("ExamBean.setExamName("+ newExamName +")");
        this.header.setExamName(newExamName);
    }
    public void setCategoryViaCode(String cat){
        log.write("ExamBean.setCategoryViaCode("+cat+")");
        this.header.setCategoryViaCode(cat);
    }
    public void setCategoryViaName(String n){
        log.write("ExamVO.setCategoryViaName("+n+")");
        this.header.setCategoryViaName(n);
    }
    public void setDateCreated(java.util.Date aDate){
        log.write("ExamVO.setDateCreated("+aDate+")");
        this.header.setDateCreated(aDate);
    }
    public void setDateLastMod(java.util.Date aDate){
        log.write("ExamVO.setDateLastMod("+aDate+")");
        this.header.setDateLastMod(aDate);
    }
    public void setIsActive(boolean e){
        log.write("ExamVO.setIsActive("+ e +")");
        this.header.setIsActive(e);
    }
    public void setDisplayAfterTaking(boolean dispAftTak) {
        log.write("ExamVO.setDisplayAfterTaking("+ dispAftTak +")");
        this.header.setDisplayAfterTaking(dispAftTak);
    }
    public void setTakerEmpNum(int x){
        log.write("setTakerEmpNum("+x+")");
        this.taker = new UserDAO(x);
    }
    public void setTaker(UserDAO u){
        this.taker = u;
    }
    //******************* END setXXX *******************//
    //********************* getXXX *********************//
    public int getCurrentExamLoc(){
        return this.currentExamLoc;
    }
    public String getNextEntryTypeCode(){
        return this.nextEntryTypeCode;
    }
    public String getThisEntryTypeCode(){
        return this.thisEntryTypeCode;
    }
    public int getcurrentQNum(){
        return this.currentQNum;
    }
    //************* getXXX FROM ExamHeader *************//
    public int getCreatorEmpNum(){
        return this.creator.getEmpNum();
    }
    public String getCreatorName(){
        return this.creator.getFullName();
    }
    public String getExamName(){
        return this.header.getName();
    }
    public int getExamNum(){
        return this.header.getExamNum();
    }
    public String getCategoryName(){
        return this.header.getCategoryName();
    }
    public String getCategoryCode(){
        return this.header.getCategoryCode();
    }
    public java.util.Date getDateCreated(){
        return this.header.getDateCreated();
    }
    public boolean getDisplayAfterTaking() {
        return this.header.getDisplayAfterTaking();
    }
    public boolean getIsActive(){
        return this.header.getIsActive();
    }
    public int getNumEntries(){
        return this.entries.size();
    }
    public ExamEntry getEntryNum(int x){
        return ((ExamEntry)entries.elementAt(x));
    }
    public String getHeaderToDisplay(String creatorName){
        return header.displayToView(creatorName);
    }
    public String getHeaderToTake(String fullName, int empNum){
        return header.displayToTake(fullName, empNum);
    }
    public String getHeaderGraded(){
        return header.displayGraded(this.taker.getFullName());
    }
    public String getTakerName(){
        return this.taker.getFullName();
    }
    public int getTakerEmpNum(){
        return this.taker.getEmpNum();
    }
    //****************** END GET & SET FUNCTIONS *****************//
    public void addEntry(ExamEntry e){
        entries.add(e);
    }
    //*************************** DB FXNS *************************//
    public void insertHeaderToDB(){
        this.header.insertToDB();
    }
    public void createPreparedStatements(){
        log.write("ExamDAO.createPreparedStatements()");
        try{
            selectAllStmtStr = "SELECT * FROM EXAM";
            selectAllStmt = dbUtil.createPreparedStatement(selectAllStmtStr);
            
            selectAllActiveStmtStr = "SELECT "
                    + " e.exam_num, e.exam_name, e.date_created, "
                    + " e.display_after_taking, ec.category_name,"
                    + " ui.name_first, ui.name_last "
                    + "FROM EXAM e, EXAM_CATEGORY ec, USER_INFO ui WHERE "
                    + "e.is_active = 1 AND ec.category_code = e.category_code "
                    + "AND ui.emp_num = e.creator_emp_num";
            selectAllActiveStmt = dbUtil.createPreparedStatement(selectAllActiveStmtStr);
            
            selectAllTakenStr = "SELECT "
                    + " e.exam_num, e.exam_name, et.take_num, et.date_taken, "
                    + " ui.name_first, ui.name_last "
                    + " FROM EXAM e, EXAM_TAKE et, USER_INFO ui WHERE"
                    + " e.exam_num = et.exam_num AND et.emp_num = ui.emp_num"
                    + " ORDER BY et.take_num";
            selectAllTaken = dbUtil.createPreparedStatement(selectAllTakenStr);
            
            loadExamBodyStr = "SELECT ee.exam_loc, ee.question_num, "
                    + " ee.entry_type_code, et.entry_type_name, et.class_name"
                    + " FROM EXAM_ENTRY ee, ENTRY_TYPE et WHERE exam_num = ?"
                    + " AND ee.entry_type_code = et.entry_type_code"
                    + " ORDER BY ee.exam_loc";
            loadExamBody = dbUtil.createPreparedStatement(loadExamBodyStr);
            
            insertToExamTakeStr = "INSERT INTO EXAM_TAKE (take_num, exam_num, "
                    + "date_taken, emp_num) VALUES (?, ?, ?, ?)";
            insertToExamTake = dbUtil.createPreparedStatement(insertToExamTakeStr);
            
            selectTypeViaCodeStr = "SELECT entry_type_name, class_name FROM "
                    + " ENTRY_TYPE WHERE entry_type_code = ?";
            this.selectTypeViaCodeStmt = dbUtil.createPreparedStatement(selectTypeViaCodeStr);
            
        }catch(Exception e){
            log.write("Exception in ExamDAO.createPreparedStatements(): " 
                    + e.toString());
        }
    }
    public void loadFromDBViaExamNum(int eNum){
        this.examNum = eNum;
        this.entries.clear();
        log.write("ExamDAO.loadFromDBViaExamNum("+eNum+")");
        // Load Header
        this.header.loadFromDBViaExamNum(eNum);
        // Set this Creator
        this.setCreatorViaEmpNum(this.header.getCreatorEmpNum());
        // Load all questions from EXAM_ENTRY
        try{
            loadExamBody.setString(1, String.valueOf(eNum));
            ResultSet rs = loadExamBody.executeQuery();
            while(rs.next()){
                // 1) Get question data from EXAM_ENTRY & ENTRY_TYPE
                int eLoc = rs.getInt(1);
                int qNum = rs.getInt(2);
                thisEntryTypeCode = rs.getString(3);
                thisEntryTypeName = rs.getString(4);
                thisEntryTypeClassName = rs.getString(5);
                // 3) Using this entry type, create a new question object.
                Class x = Class.forName(this.thisEntryTypeClassName);
                ExamEntry o = (ExamEntry)x.newInstance();
                // 4) Tell this entry to load it's values from the DB
                o.loadFromDB(eNum, eLoc);
                if(!(o instanceof SectionHeader)){
                    ((Question)o).setQuestionNum(qNum);
                }
                // 5) Append this new entry to the ExamEntry vector
                this.entries.add(o);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.loadFromDBViaExamNum("+examNum+"): " + e);
        }catch(InstantiationException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }catch(ClassNotFoundException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }catch(IllegalAccessException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }
        
       
    }
    public void loadTakenFromDB(int eNum, int tNum){
        this.takeNum = takeNum;
        this.entries.clear();
        log.write("ExamDAO.loadTakenFromDBm("+eNum+", "+tNum+")");
        // Load Header
        this.header.loadFromDBViaTakeNum(tNum);
        // Set this Creator
        //log.write("CREATOR EMP NUM = " + this.header.getCreatorEmpNum());
        this.setTakerViaEmpNum(this.header.getTakerEmpNum());
        // Load all questions from EXAM_ENTRY
        // MODIFY THIS SO IT LOADS EMP ANSWER ALSO.
        try{
            loadExamBody.setString(1, String.valueOf(eNum));
            ResultSet rs = loadExamBody.executeQuery();
            while(rs.next()){
                // 1) Get question data from EXAM_ENTRY & ENTRY_TYPE
                int eLoc = rs.getInt(1);
                int qNum = rs.getInt(2);
                thisEntryTypeCode = rs.getString(3);
                thisEntryTypeName = rs.getString(4);
                thisEntryTypeClassName = rs.getString(5);
                // 3) Using this entry type, create a new question object.
                Class x = Class.forName(this.thisEntryTypeClassName);
                ExamEntry o = (ExamEntry)x.newInstance();
                // 4) Tell this entry to load it's values from the DB
                o.loadTakenFromDB(eNum, eLoc, tNum);
                if(!(o instanceof SectionHeader)){
                    ((Question)o).setQuestionNum(qNum);
                }
                // 5) Append this new entry to the ExamEntry vector
                this.entries.add(o);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.loadFromDBViaExamNum("+examNum+"): " + e);
        }catch(InstantiationException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }catch(ClassNotFoundException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }catch(IllegalAccessException e){
            log.write("ExamDAO.loadFromDBViaExamNum threw " + e.toString());
        }
        
    }
    public Collection getAllActive(){
        log.write("ExamDAO.getAllActive()");
        Vector v = new Vector();
        ExamVO temp = null;
        try{
            ResultSet rs = selectAllActiveStmt.executeQuery();
            while(rs.next()){
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name"));
                temp.setDateCreated(dbUtil.getDateFromFloat(rs.getFloat("date_created")));
                if(rs.getInt("display_after_taking") == 0)
                    temp.setDisplayAfterTaking(false);
                else
                    temp.setDisplayAfterTaking(true);
                temp.setCategoryName(rs.getString("category_name"));
                temp.creator.setFirstName(rs.getString("name_first"));
                temp.creator.setLastName(rs.getString("name_last"));
                temp.setIsActive(true);
                v.add(temp);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.getAllActive() = " + e.toString());
        }
        return v;
    }
    public Collection getAllTaken(){
        log.write("ExamDAO.getAllActive()");
        Vector v = new Vector();
        ExamVO temp = null;
        try{
            ResultSet rs = selectAllTaken.executeQuery();
            while(rs.next()){
                temp = new ExamVO();
                temp.setExamNum(rs.getInt("exam_num"));
                temp.setExamName(rs.getString("exam_name"));
                temp.setTakeNum(rs.getInt("take_num"));
                temp.setDateTaken(dbUtil.getDateFromFloat(rs.getFloat("date_taken")));
                temp.setTaker(rs.getString("name_first"), rs.getString("name_last"));
                temp.setIsActive(true);
                v.add(temp);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.getAllActive() = " + e.toString());
        }
        return v;
    }
    public void loadEntryTypeViaCode(String code){
        log.write("ExamDAO.loadEntryTypeViaCode("+code+")");
        try{
            selectTypeViaCodeStmt.setString(1, code);
            ResultSet rs = selectTypeViaCodeStmt.executeQuery();
            rs.next();
            this.thisEntryTypeCode = code;
            this.thisEntryTypeName = rs.getString(1);
            this.thisEntryTypeClassName = rs.getString(2);
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.loadEntryTypeViaCode(" + code + "): " + e.toString());
        }
        log.write("ExamDAO.loadEntryTypeViaCode("+code+") COMPLETE");
    }
    public static int getMaxExamNumFromDB(){
        Secretary.write("ExamDAO.getMaxExamNumFromDB");
        DBUtil dbUtil = new DBUtil();
        int x = 0;
        try{
            String select;
            PreparedStatement selectStmt;
            select = "SELECT MAX(exam_num) FROM EXAM";
            selectStmt = dbUtil.createPreparedStatement(select);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                x = rs.getInt(1);
            }
        }catch(SQLException e){
            Secretary.write("Exception in ExamBean.getMaxExamNumFromDB()" 
                + " : " + e.getMessage());
        }catch(Exception e){
            Secretary.write("Exception in ExamBean.getMaxExamNumFromDB()" 
                + " : " + e.getMessage());
        }
        return x;
    }
    public static int getMaxTakeNumFromDB(){
     Secretary.write("ExamDAO.getMaxTakeNumFromDB");
        DBUtil dbUtil = new DBUtil();
        int x = 0;
        try{
            String select;
            PreparedStatement selectStmt;
            select = "SELECT MAX(take_num) FROM EXAM_TAKE";
            selectStmt = dbUtil.createPreparedStatement(select);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                x = rs.getInt(1);
            }
        }catch(SQLException e){
            Secretary.write("Exception in ExamBean.getMaxTakeNumFromDB()" 
                + " : " + e.getMessage());
        }catch(Exception e){
            Secretary.write("Exception in ExamBean.getMaxTakeNumFromDB()" 
                + " : " + e.getMessage());
        }
        return x;
    }
    public int insertToExamTake(){
        log.write("ExamDAO.insertToExamTake()");
        int takeNum = this.getMaxTakeNumFromDB() + 1;
        try{
            this.insertToExamTake.setInt(1, takeNum);
            this.insertToExamTake.setInt(2, this.examNum);
            this.insertToExamTake.setFloat(3, dbUtil.getFloatFromDate(new java.util.Date()));
            log.write("takerEmpNum = " + this.taker.getEmpNum());
            this.insertToExamTake.setInt(4, this.taker.getEmpNum());
            this.insertToExamTake.execute();
        }catch(SQLException e){
            log.write("SQLException in ExamDAO.insertToExamTake(): " + e.toString());
        }
        return takeNum;
    }   
    public void processTaken(javax.servlet.http.HttpServletRequest request){
        javax.servlet.http.HttpSession session = request.getSession();
        StudentBean aTaker = (StudentBean)session.getAttribute(StudentBean.SESSION);
        this.taker = new UserDAO(aTaker.getEmpNum());
        // Create entry in EXAM_TAKE
        log.write("taker = " + taker.getEmpNum());
        //setTakerEmpNum(taker.getEmpNum());
        log.write("aTaker.getEmpNum() = " + aTaker.getEmpNum());
        log.write("this.getTakerEmpNum() = " +this.getTakerEmpNum());
        int takeNum = insertToExamTake();
        ExamEntry eeTemp;
        // Process entries to EMP_ANSWER
        for(int x=0; x < entries.size(); x++){
            eeTemp = this.getEntryNum(x);
            if(eeTemp instanceof Question){
                ((Question)eeTemp).fetchEmpAnswer(request, takeNum);
                ((Question)eeTemp).insertTakeToDB(takeNum);
            }
        }
    }
    //************************* END DB FXNS ***********************//
}
