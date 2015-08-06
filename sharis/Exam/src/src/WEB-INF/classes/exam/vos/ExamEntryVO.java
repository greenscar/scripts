/***********************************************************************
 * Module:  ExamEntryVO.java
 * Author:  James Sandlin
 * Purpose: ExamEntryVO is an abstract class which represents any sort
 *          of individual entry in an exam.
 *          example: SectionHeader, TrueFalse, FillInBlank
 * VARS:    examLoc
 *          questionNum
 *          takeNum
 *          examNum
 *          entryTypeCode
 *          entryTypeName  
 *          entryTypeClass
 ***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;
import exam.EntryType;
public abstract class ExamEntryVO implements tools.HTMLVars, java.io.Serializable
{
    private int examNum;
    private int examLoc;
    private long insertTime;
    private int questionNum;
    private int takeNum;
    /*
    private String ENTRY_TYPE_CODE;
    private String entryTypeName;
    private String entryTypeClass;
     */
    protected EntryType entryType;
    protected Secretary log;

    public abstract String displayForm();
    public abstract String displayToMod();
    public abstract String displayToView();
    public abstract String displayToTake();
    public abstract String displayGraded();
    public abstract String displayToGrade();
    public abstract String displayToSelect();
    
    public abstract void processModForm(javax.servlet.http.HttpServletRequest request);
    public abstract void processCreateForm(javax.servlet.http.HttpServletRequest request, long time);
    
    public void logValues(){
        log.write("exam", "--------------------------------");
        log.write("exam", "    examLoc = " + this.getExamLoc());
        log.write("exam", "    questionNum = " + this.getQuestionNum());
        log.write("exam", "    entryTypeName = " + getEntryTypeName());
        log.write("exam", "    insertTime = " + this.insertTime);
        log.write("exam", "    getInsertTime() = " + this.getInsertTime());
    }

    public void setInsertTime(long x)
    {
        this.insertTime = x;
    }
    public long getInsertTime()
    {
        return this.insertTime;
    }
    public String replaceSubString(String orig, String pattern, String replaceWith)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = orig.indexOf(pattern, s)) >= 0) {
            result.append(orig.substring(s, e));
            result.append(replaceWith);
            s = e+pattern.length();
        }
        result.append(orig.substring(s));
        return result.toString();
    }

    public int getExamLoc()
    {
        return examLoc;
    }
    public void setExamLoc(int newExamLoc)
    {
        examLoc = newExamLoc;
    }

    public int getQuestionNum()
    {
        return questionNum;
    }
    public void setQuestionNum(int newQuestionNum)
    {
        questionNum = newQuestionNum;
    }
    public void setEntryType(EntryType et)
    {
       this.entryType = et;
    }
    /*
    public void setEntryTypeCode(String newEntryTypeCode)
    {
        ENTRY_TYPE_CODE = newEntryTypeCode;
    }
    public void setEntryTypeName(String newEntryTypeName)
    {
        entryTypeName = newEntryTypeName;
    }
    public void setEntryTypeClass(String newEntryTypeClass)
    {
        entryTypeClass = newEntryTypeClass;
    }
     */
    public boolean isSelfGrading()
    {
       return this.entryType.isSelfGrading();
    }
    public String getEntryTypeName()
    {
        return this.entryType.getName();
    }
    public String getEntryTypeClass()
    {
        return this.entryType.getClassName();
    }
    public String getEntryTypeCode()
    {
        return this.entryType.getCode();
    }
    public boolean getSelfGrading()
    {
       return this.entryType.isSelfGrading();
    }
    public int getTakeNum()
    {
        return takeNum;
    }
    public void setTakeNum(int newTakeNum)
    {
        this.takeNum = newTakeNum;
    }

    public int getExamNum()
    {
        return examNum;
    }
    public void setExamNum(int newExamNum)
    {
        examNum = newExamNum;
    }
    
    public ExamEntryVO()
    {
        log = new Secretary();
    }

}