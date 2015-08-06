/*
 * ExamVO.java
 *
 * Created on July 21, 2003, 3:33 PM
 */

package vos;

import daos.*;
import logging.*;
import java.util.*;
import examParts.*;
import java.lang.reflect.*;
import db.*;
import beans.*;
import java.sql.*;

public class ExamVO extends Object implements java.io.Serializable {
    
    //********************** PROPERTY VARS ***********************//
    private Vector entries; //a Vector of ExamEntry Objects
    private Secretary log;
    private String thisQTypeCode;
    private String nextQTypeCode;
    //private UserBean creator;
    private int currentQNum;
    private int currentExamLoc;
    private int examNum;
    private int takeNum;
    public UserDAO creator;
    public UserVO taker;
    private ExamHeaderVO header;
    private EntryTypeVO thisEntryType;
    private EntryTypeVO nextEntryType;
    //******************** END PROPERTY VARS *********************//
    
    /** Creates a new instance of ExamVO */
    public ExamVO(){
        this.creator = new UserDAO();
        this.header = new ExamHeaderVO();
        this.thisEntryType = new EntryTypeVO();
        this.nextEntryType = new EntryTypeVO();
    }
    //******************** GET & SET FUNCTIONS *******************//
    public void setCurrentExamLoc(int x){
        this.currentExamLoc = x;
    }
    public void setThisQTypeCode(String newType){
        this.thisQTypeCode = newType;
    }
    public void setNextQTypeCode(String newType){
        this.nextQTypeCode = newType;
    }
    public void setCatName(String c){
        log.write("ExamVO.setCatName("+c+")");
        this.header.setCategoryName(c);
    }
    public void setCurrentQNum(int x){
        this.currentQNum = x;
    }
    public void setCreator(UserDAO ab){
        this.creator = ab;
        this.setCreatorEmpNum(this.creator.getEmpNum());
    }
    public void setCreatorEmpNum(int num){
        log.write("ExamVO.setCreatorEmpNum("+num+")");
        this.creator.setEmpNum(num);
    }
    public void setExamNum(int newNum){
        log.write("ExamVO.setExamNum("+ newNum +")");
        this.header.setExamNum(newNum);
    }
    public void setExamName(String newExamName){
        log.write("ExamVO.setExamName("+ newExamName +")");
        this.header.setExamName(newExamName);
    }
    public void setCategory(String code, String name){
        log.write("ExamVO.setCategory("+code+", "+name+")");
        this.header.setCategory(code, name);
    }
    public void setCategorybyCode(String cat){
        this.header.setCategoryViaCode(cat);
    }
    public void setCategoryName(String n){
        this.header.setCategoryViaName(n);
    }
    public void setDateCreated(java.util.Date aDate){
        this.header.setDateCreated(aDate);
    }
    public void setDateLastMod(java.util.Date aDate){
        this.header.setDateLastMod(aDate);
    }
    public void setDateTaken(java.util.Date aDate){
        this.header.setDateTaken(aDate);
    }
    public void setIsActive(boolean e){
        this.header.setIsActive(e);
    }
    public void setDisplayAfterTaking(boolean dispAftTak) {
        this.header.setDisplayAfterTaking(dispAftTak);
    }
    public void setTakeNum(int x){
        this.takeNum = x;
    }
    public void setTaker(String fn, String ln){
        this.taker = new UserVO(fn, ln);
    }
    //******************* END setXXX *******************//
    //********************* getXXX *********************//
    public int getCurrentExamLoc(){
        return this.currentExamLoc;
    }
    public String getNextQTypeCode(){
        return this.nextQTypeCode;
    }
    public String getThisQTypeCode(){
        return this.thisQTypeCode;
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
    public String getCatName(){
        return this.header.getCategoryName();
    }
    public String getCatCode(){
        return this.header.getCategoryCode();
    }
    public java.util.Date getDateCreated(){
        return this.header.getDateCreated();
    }
    public java.util.Date getDateTaken(){
        return this.header.getDateTaken();
    }
    public boolean getDisplayAfterTaking() {
        return this.header.getDisplayAfterTaking();
    }
    public boolean isActive(){
        return this.header.getIsActive();
    }
    public int getcurrentQNum(){
        return this.currentQNum;
    }
    public int getTakeNum(){
        return this.takeNum;
    }
    //******************* END getXXX *******************//
    //****************** END GET & SET FUNCTIONS *****************//
    
}
