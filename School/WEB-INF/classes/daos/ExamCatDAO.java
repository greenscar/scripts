package daos;
import java.beans.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import db.*;
import vos.ExamCatVO;
import logging.*;
public class ExamCatDAO {
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//
    //********************** PROPERTY VARS ***********************//
    private String code;
    private String name;
    private Secretary log;
    // Prepared query strings
    private String selectNameViaCodeStmtStr;
    private String selectCodeViaNameStmtStr;
    private String selectAllStmtStr;
    // Prepared Statements
    private PreparedStatement selectNameViaCodeStmt;
    private PreparedStatement selectCodeViaNameStmt;
    private PreparedStatement selectAllStmt;
    DBUtil dbutil;
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public ExamCatDAO() {
        log = new Secretary();
        dbutil = new DBUtil();
        createPreparedStatements();
    }
    public ExamCatDAO(String catCode) {
        log = new Secretary();
        dbutil = new DBUtil();
        createPreparedStatements();
        this.code = catCode;
        this.loadNameViaCode();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setName(String cat){
        this.name = cat;
    }
    public void setCode(String id){
        log.write("ExamCatDAO.setCode("+id+")");
        this.code = id;
    }
    public String getName(){
        return this.name;
    }
    public String getCode(){
        return this.code;
    }
    public ExamCatVO getExamCatVO(){
        log.write("ExamCatDAO.getExamCatVO()");
        ExamCatVO vo = new ExamCatVO(this.code, this.name);
        return vo;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //*************************** DB FXNS *************************//
    /*
     * Load the category name from the EXAM_CATEGORY table based on
     *  this object's current code
     */
    public void loadNameViaCode(){
        log.write("ExamCatDAO.loadNameViaCode()");
        try{
            selectNameViaCodeStmt.setString(1, this.code);
            ResultSet rs = selectNameViaCodeStmt.executeQuery();
            if(rs.next()){
                this.name = rs.getString(1);
            }
            else{
                this.name = "UNDEFINED";
            }
        }catch(SQLException e){
            log.write("SQLException in ExamCat.loadNameViaCode() = " + e.toString());
        }
    }   
    public void loadCode(){
        log.write("ExamCat.loadCode()");
        try{
            selectCodeViaNameStmt.setString(1, this.name);
            ResultSet rs = selectNameViaCodeStmt.executeQuery();
            if(rs.next()){
                this.code = rs.getString(1);
                log.write("this.code = " + this.code);
            }
            else{
                this.code = "UNDEFINED";
                log.write("this.code = UNDEFINED");
            }
        }catch(SQLException e){
            log.write("SQLException in ExamCat.loadCode() = " + e.toString());
        }   
    }
    /*
     * Prepare all SQL statements for calling
     * This method is called in this instances constructor.
     */
    public void createPreparedStatements(){
        log.write("ExamCat.createPreparedStatements()");
        try{
            selectAllStmtStr = "SELECT * FROM EXAM_CATEGORY";
            selectAllStmt = dbutil.createPreparedStatement(selectAllStmtStr);
            
            selectNameViaCodeStmtStr = "SELECT category_name FROM EXAM_CATEGORY "
                            + " WHERE category_code = ?";
            selectNameViaCodeStmt = dbutil.createPreparedStatement(selectNameViaCodeStmtStr);
            
            selectCodeViaNameStmtStr = "SELECT category_code FROM EXAM_CATEGORY "
                                     + " WHERE category_name = ?";
            selectCodeViaNameStmt = dbutil.createPreparedStatement(selectCodeViaNameStmtStr);
        }catch(Exception e){
            log.write("Exception in ExamCat.createPreparedStatements(): " 
                    + e.toString());
        }
    }
    
    /*
     * Load the name from the EXAM_CATEGORY table for the provided categoryCode.
     * Assign this category code and the returned category name to this object.
     */
    public void loadViaCode(String catCode){
        log.write("ExamCat.loadViaCode("+catCode+")");
        try{
            selectNameViaCodeStmt.setString(1, catCode);
            ResultSet rs = selectNameViaCodeStmt.executeQuery();
            if(rs.next()){
                this.code = catCode;
                this.name = rs.getString(1);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamCat.loadDAOViaCode() = " + e.toString());
        }
        
    }
    /*
     * getCatList() queries the DB for all exam categories.
     * It then creates a Collection of ExamCatVO objects,
     *    one for each exam name.
     * This Collection is returned.
     */
    public Collection getCatList(){
        log.write("ExamCat.getExamNameList()");
        Vector v = new Vector();
        ExamCatVO temp = null;
        try{
            ResultSet rs = selectAllStmt.executeQuery();
            while(rs.next()){
                temp = new ExamCatVO();
                temp.setCode(rs.getString(1));
                temp.setName(rs.getString(2));
                v.add(temp);
            }
        }catch(SQLException e){
            log.write("SQLException in ExamCat.getExamNameList() = " + e.toString());
        }
        return v;
    }
    //************************* END DB FXNS ***********************//
    
}
