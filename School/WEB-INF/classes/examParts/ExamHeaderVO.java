package examParts;
import java.util.*;
import java.sql.*;
import logging.*;
import db.*;
import vos.*;
import daos.*;
public class ExamHeaderVO {
    //*********************** STATIC VARS ************************//
    //********************* END STATIC VARS **********************//
    
    //********************** PROPERTY VARS ***********************//
    private Secretary log;
    private int examNum, finalGrade;
    private String examName, categoryCode, categoryName;    
    private java.util.Date dateCreated, dateLastMod, dateTaken;
    private boolean isActive = false, displayAfterTaking;
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public ExamHeaderVO() {
        log.write("ExamHeaderVO constructor");
        log = new Secretary();
        log.write("END ExamHeader constructor");
    }
    public ExamHeaderVO(int eNum, String examName, String catCode,  
                        java.util.Date date, boolean isActive, boolean disp){
        log = new Secretary();
        this.examNum = eNum;
        this.examName = examName;
        this.categoryCode = catCode;
        this.dateCreated = date;
        this.dateLastMod = date;
        this.isActive = isActive;
        this.displayAfterTaking = disp;
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    
    public int getExamNum(){
        log.write("ExamHeaderVO.getExamNum()");
        return this.examNum;
    }
    public String getExamName(){
        log.write("ExamHeaderVO.getExamName()");
        return this.examName;
    }
    public String getCategoryName(){
        return this.categoryName;
    }
    public String getCategoryCode(){
        return this.categoryCode;
    }
    public int getFinalGrade(){
        return this.finalGrade;
    }
    public java.util.Date getDateTaken(){
        log.write("ExamHeaderVO.getDateTaken()");
        return this.dateTaken;
    }
    public java.util.Date getDateCreated(){
        log.write("ExamHeaderVO.getDateCreated()");
        return this.dateCreated;
    }
    public java.util.Date getDateLastMod(){
        log.write("ExamHeaderVO.getDateLastMod()");
        return this.dateLastMod;
    }
    public boolean getIsActive(){
        log.write("ExamHeaderVO.getIsActive()");
        return this.isActive;
    }
    public boolean getDisplayAfterTaking(){
        log.write("ExamHeaderVO.getDisplayAfterTaking()");
        return this.displayAfterTaking;
    }
    
    public void setExamNum(int x){
        log.write("ExamHeaderVO.setExamNum("+x+")");
        this.examNum = x;
    }
    public void setExamName(String n){
        log.write("ExamHeaderVO.setExamName("+n+")");
        this.examName = n;
    }
    public void setCategoryName(String name){
        this.categoryName = name;
    }
    public void setCategoryCode(String c){
        this.categoryCode = c;
    }
    public void setFinalGrade(int x){
        this.finalGrade = x;
    }
    public void setDateTaken(java.util.Date d){
        log.write("ExamHeaderVO.setDateTaken("+d+")");
        this.dateTaken = d;
    }
    public void setDateCreated(java.util.Date d){
        log.write("ExamHeaderVO.setDateCreated("+d+")");
        this.dateCreated = d;
    }
    public void setDateLastMod(java.util.Date d){
        log.write("ExamHeaderVO.setDateLastMod("+d+")");
        this.dateLastMod = d;
    }
    public void setIsActive(boolean x){
        log.write("ExamHeaderVO.setIsActive("+x+")");
        this.isActive = x;
    }
    public void setDisplayAfterTaking(boolean x){
        log.write("ExamHeaderVO.setDisplayAfterTaking("+x+")");
        this.displayAfterTaking = x;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    //********************* PROCESS FUNCTIONS ********************//
    //******************** END PROCESS FUNCTIONS ******************//
    /*
     *
     * The HTML display functions need to go into the ExamVO.
     */
    //********************** HTML DISPLAY FXNS ********************//
    public String displayToView(String creatorName){
        log.write("ExamHeaderVO.displayToView()");
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
    public String displayToMod(String creatorName){
        log.write("ExamHeaderVO.displayToMod()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Creator:</TD>";
        temp += "<TD>" + creatorName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Category:</TD>";
        temp += "<TD>" + this.catName + "</TD>";
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
    public String displayToTake(String tName, int tEmpNum){
        log.write("ExamHeaderVO.displayToView()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Name:</TD>";
        temp += "<TD>" + tName + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Employee ID:</TD>";
        temp += "<TD>" + tEmpNum + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Today's Date:</TD>";
        temp += "<TD>" + new java.util.Date() + "</TD>";
        temp += "</TR>\n";
        temp += "</TABLE>";
        return temp;
    }
    public String displayGraded(){
        log.write("ExamHeaderVO.displayGraded()");
        String temp = "<TABLE class=\"header\">";
        temp += "<TR>";
        temp += "<TH colspan=2>" + this.examName + "</TH>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Name:</TD>";
        temp += "<TD>" + this.taker.getFullName() + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Employee ID:</TD>";
        temp += "<TD>" + this.taker.getEmpNum() + "</TD>";
        temp += "</TR>\n<TR>";
        temp += "<TD>Date Taken:</TD>";
        temp += "<TD>" + this.dateTaken + "</TD>";
        temp += "</TR>\n";
        temp += "</TABLE>";
        return temp;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    
}
    
    
