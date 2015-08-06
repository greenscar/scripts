package daos;
import java.beans.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import db.*;
import beans.*;
import vos.EntryTypeVO;
import logging.*;

public class EntryTypeDAO {
    //***************** PROPERTY VARS ******************//
    private String name;
    private String code;
    private String className;
    
    //***************** QUERY STRINGS ******************//
    private String selectStmtStr;
    private String selectCodeStmtStr;
    private String selectAllStmtStr;
    
    //************** PREPARED STATEMENTS ***************//
    private PreparedStatement selectStmt;
    private PreparedStatement selectCodeStmt;
    private PreparedStatement selectAllStmt;
    
    //***************** CLASSES VARS *******************//
    DBUtil dbutil;
    Secretary log;
    
    //****************** CONSTRUCTORS ******************//
    public EntryTypeDAO() {
        log = new Secretary();
        dbutil = new DBUtil();
        createPreparedStatements();
    }
    public EntryTypeDAO(String id, String name){
        this.code = id;
        this.name = name;
        log = new Secretary();
        dbutil = new DBUtil();
        createPreparedStatements();
    }
    public void setCode(String id){
        this.code = id;
    }
    public void setName(String id){
        this.name = id;
    }
    public void setClassName(String id){
        this.className = id;
    }
    
    public String getName(){
        return this.name;
    }
    public String getCode(){
        return this.code;
    }
    public String getClassName(){
        return this.className;
    }
    
    
    public void createPreparedStatements(){
        //log.write("EntryTypeDAO.createPreparedStatements()");
        try{
            selectAllStmtStr = "SELECT * FROM ENTRY_TYPE";
            selectAllStmt = dbutil.createPreparedStatement(selectAllStmtStr);
            
            selectStmtStr = "SELECT * FROM ENTRY_TYPE "
                + "WHERE entry_type_code = ?";
            selectStmt = dbutil.createPreparedStatement(selectStmtStr);
            
            selectCodeStmtStr = "SELECT * FROM ENTRY_TYPE "
                + "WHERE entry_type = ?";
            selectCodeStmt = dbutil.createPreparedStatement(selectCodeStmtStr);
        }catch(Exception e){
            //log.write("Exception in EntryTypeDAO.createPreparedStatements(): " e.getMessage());
        }
    }
    public void loadDAO(String code){
        log.write("EntryTypeDAO.loadDAO("+code+")");
        try{
            selectStmt.setString(1, code);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                setCode(rs.getString(1));
                setName(rs.getString(2));
                setClassName(rs.getString(3));
            }
        }catch(Exception e){
            log.write("Exception in EntryTypeDAO.loadDAO(" 
                        + code + "): " + e.getMessage());
        }
    }
    public void loadDAOFromName(String name){
        log.write("EntryTypeDAO.loadDAOFromName("+name+")");
        try{
            selectCodeStmt.setString(1, name);
            ResultSet rs = selectCodeStmt.executeQuery();
            if(rs.next()){
                setCode(rs.getString(1));
                setName(rs.getString(2));
                setClassName(rs.getString(3));
            }
        }catch(Exception e){
            //log.write("Exception in EntryTypeDAO.createPreparedStatements(): " e.getMessage());
        }
    }
    
    /*
     * getEntryTypeList() queries the DB for all question types.
     * It then creates a Collection of EntryTypeVO objects,
     *    one for each question type.
     * This Collection is returned.
     */
    public Collection getEntryTypeList(){
        log.write("EntryTypeDAO.getEntryTypeList()");
        Vector v = new Vector();
        EntryTypeVO temp = null;
        try{
            ResultSet rs = selectAllStmt.executeQuery();
            while(rs.next()){
                temp = new EntryTypeVO();
                temp.setCode(rs.getString(1));
                temp.setName(rs.getString(2));
                temp.setClassName(rs.getString(3));
                v.add(temp);
            }
        }catch(SQLException e){
            //log.write("SQLException in EntryTypeDAO.getExamNameList() = " + e.toString());
        }
        return v;
    }
}
