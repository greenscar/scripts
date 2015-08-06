package daos;
import java.util.*;
import db.*;
import java.sql.*;
import examParts.ExamHeader;
import logging.Secretary;
public class __DONT_NEED_ExamHeaderDAO{
    //********************** PROPERTY VARS ***********************//
    private Secretary log;
    private DBUtil dbUtil;
    
    private int examNum;
    private String name;    
    private String categoryID;
    //private UserDAO creator;
    private int creatorEmpNum;
    private java.util.Date dateCreated;
    private java.util.Date dateLastMod;
    private boolean active = false;
    private boolean displayAfterTaking;
    // Prepared query strings
    private String insertStmtStr;
    private String updateStmtStr;
    private String deleteStmtStr;
    private String selectStmtStr;
    private String selectListStmtStr;
    // Prepared Statements
    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement selectStmt;
    private PreparedStatement selectListStmt;
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public __DONT_NEED_ExamHeaderDAO() {
        log = new Secretary();
        log.write("ExamHeaderDAO() constructor");
        dbUtil = new DBUtil();
        createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    //********************* setXXX *********************//
    public void setExamNum(int x){
        this.examNum = x;
    }
    public void setName(String n){
        this.name = n;
    }
    public void setCategoryID(String id){
        this.categoryID = id;
    }
    public void setCreatorEmpNum(int num){
        this.creatorEmpNum = num;
    }
    public void setDateCreated(java.util.Date d){
        this.dateCreated = d;
    }
    public void setDateLastMod(java.util.Date d){
        this.dateLastMod = d;
    }
    public void setActive(boolean x){
        this.active = x;
    }
    public void setDisplayAfterTaking(boolean x){
        this.displayAfterTaking = x;
    }
    //******************* END setXXX *******************//
    
    //********************* getXXX *********************//
    public int getExamNum(){
        return this.examNum;
    }
    public String getName(){
        return this.name;
    }
    public java.util.Date getDateLastMod(){
        return this.dateLastMod;
    }
    public boolean getDisplayAfterTaking(){
        return this.displayAfterTaking;
    }
    public boolean getActive(){
        return this.active;
    }
    public java.util.Date getDateCreated(){
        return this.dateCreated;
    }
    public String getCategoryID(){
        return this.categoryID;
    }
    public int getCreatorEmpNum(){
        return this.creatorEmpNum;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //*************************** DB FXNS *************************//
    //******************** METHODS *********************//
    public void createPreparedStatements() {
        try{
            insertStmtStr = "INSERT INTO EXAM_HEADER (examNum, name, "
                    + "categoryID, creatorEmpNum, creationDate, lastModDate, "
                    + "active, displayAfterTaking)"
                    + "VALUES(?,?,?,?,?,?,?,?)";
            this.insertStmt = dbUtil.createPreparedStatement(insertStmtStr);
            
            updateStmtStr = "UPDATE EXAM_HEADER SET name=\"?\", "
                    + "categoryID=?, creatorEmpNum=?, creationDate=?, "
                    + "lastModDate=?, active=?, displayAfterTaking=? "
                    + "WHERE examNum = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM EXAM_HEADER WHERE examNum = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
            
            selectStmtStr = "SELECT * FROM EXAM_HEADER WHERE examNum = ?";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
            
            selectListStmtStr = "SELECT * FROM EXAM_HEADER";
            this.selectListStmt = dbUtil.createPreparedStatement(selectListStmtStr);
        }catch(Exception e){
            log.write("Exception in ExamHeaderDAO.createPreparedStatements() : " 
                + e.getMessage());
        }
    }
    
    public void deleteDAO() {
        log.write("ExamHeaderDAO.deleteDAO()");
        try{
            deleteStmt.setString(1, String.valueOf(this.examNum));
            deleteStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeaderDAO.deleteDAO(): " + e);
        }
    }
    
    public void insertDAO() {
        log.write("ExamHeaderDAO.insertDAO()");
        try{
            insertStmt.setString(1, String.valueOf(this.getExamNum()));
            insertStmt.setString(2, this.getName());
            insertStmt.setString(3, this.getCategoryID());
            insertStmt.setString(4, String.valueOf(this.getCreatorEmpNum()));
            insertStmt.setFloat(5, dbUtil.getFloatFromDate(this.dateCreated));
            insertStmt.setFloat(6, dbUtil.getFloatFromDate(this.dateLastMod));
            insertStmt.setString(7, String.valueOf(this.getActive()));
            insertStmt.setString(8, String.valueOf(this.getDisplayAfterTaking()));
            insertStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeaderDAO.insertDAO(): " + e);
        }
    }
    public void loadViaExamNum(int examNum){
        log.write("ExamHeaderDAO.loadViaExamNum(" + examNum + ")");
        try{
            selectStmt.setString(1, String.valueOf(examNum));
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                this.setExamNum(rs.getInt(1));
                this.setName(rs.getString(2));
                this.setCategoryID(rs.getString(3));
                this.setCreatorEmpNum(rs.getInt(4));
                this.setDateCreated(dbUtil.getDateFromFloat(rs.getFloat(5)));
                this.setDateLastMod(dbUtil.getDateFromFloat(rs.getFloat(6)));
                this.setActive(rs.getBoolean(7));
                this.setDisplayAfterTaking(rs.getBoolean(8));
            }
        }catch(SQLException e){
            log.write("SQLException in ExamHeaderDAO.loadViaExamNum(): " + e);
        }
    }
    
    public void updateDAO() {
        log.write("ExamHeaderDAO.updateDAO()");
        try{
            updateStmt.setString(1, this.getName());
            updateStmt.setString(2, this.getCategoryID());
            updateStmt.setString(3, String.valueOf(this.creatorEmpNum));
            insertStmt.setFloat(4, dbUtil.getFloatFromDate(this.dateCreated));
            insertStmt.setFloat(5, dbUtil.getFloatFromDate(this.dateLastMod));
            updateStmt.setString(6, String.valueOf(this.active));
            updateStmt.setString(7, String.valueOf(this.displayAfterTaking));
            updateStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in ExamHeaderDAO.updateDAO(): " + e);
        }
    }
    
    // Get a Collection list of all Exam Headers (all exams) in the DB
    public Collection getList() {
        Vector v = new Vector();
        ExamHeader eh = null;
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
            log.write("SQLException in ExamHeaderDAO.getList(): " + e);
        }
        return v;
    }
    //************************* END DB FXNS ***********************//
}
