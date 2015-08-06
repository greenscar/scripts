/***********************************************************************
* Module:  QuestionVO.java
* Author:  jsandlin
* Purpose: Defines the Class QuestionVO
***********************************************************************/
package exam.vos;
import logging.Secretary;
import java.util.*;

public abstract class QuestionVO extends ExamEntryVO
{
    protected static String POINT_VALUE_FIELD = "PointValue";

    protected int pointValueTotal;
    protected int pointValuePerAnswer;
    protected int pointValueEarnedTotal;
    private int numSolutions;
    private String gradersComment;
    protected boolean selfGrading;

    //public abstract void processForm(javax.servlet.http.HttpServletRequest request, long time);
    public abstract void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request);
    public abstract void processGradingForm(javax.servlet.http.HttpServletRequest request);
    
    public void logValues(){
        super.logValues();
        log.write("exam", "    numSolutions = " + this.getNumSolutions());
        log.write("exam", "    pointValueTotal = " + this.getPointValueTotal());
        log.write("exam", "    pointValuePerAnswer = " + this.getPointValuePerAnswer());
        log.write("exam", "    getPointValueEarnedTotal = " + this.getPointValueEarnedTotal());
        if(this.gradersComment != null)
            log.write("exam", "    gradersComment = " + this.gradersComment);
    }

    public abstract boolean getAnswerEntered();
    public int getNumSolutions()
    {
        return this.numSolutions;
    }
    public int getPointValueTotal()
    {
        return this.pointValueTotal;
    }
    public int getPointValueEarnedTotal()
    {
        //Secretary.startFxn("exam", "QuestionVO.getPointValueEarnedTotal()");
        //Secretary.endFxn("exam", "QuestionVO.getPointValueEarnedTotal() => " + this.pointValueEarnedTotal);
        return this.pointValueEarnedTotal;
    }
    public int getPointValuePerAnswer()
    {
        return this.pointValuePerAnswer;
    }
    protected void computePointValueTotal()
    {
        Secretary.startFxn("exam", "QuestionVO.computePointValueTotal()");
        Secretary.write("exam", "numSolutions = " + this.getNumSolutions());
        Secretary.write("exam", "pointValuePerAnswer = " + this.pointValuePerAnswer);
        this.pointValueTotal = this.pointValuePerAnswer * this.getNumSolutions();
        Secretary.endFxn("exam", "QuestionVO.computePointValueTotal() => " + this.pointValueTotal);
    }
    public void computePointValuePerAnswer(){
        if(this.getNumSolutions() == 0) 
            this.pointValuePerAnswer = 0;
        else 
            this.pointValuePerAnswer = this.pointValueTotal / this.getNumSolutions();
    }
        
    public void setPointValueTotal(int newV)
    {
        this.pointValueTotal = newV;
        //this.pointValuePerAnswer = this.pointValueTotal / this.getNumSolutions();
    }
    public void setPointValueEarnedTotal(int newV)
    {
        this.pointValueEarnedTotal = newV;
    }
    public void setPointValuePerAnswer(int newV)
    {
        this.pointValuePerAnswer = newV;
    }
    public void setNumSolutions(int newS)
    {
        this.numSolutions = newS;
    }
    
    public void setGradersComment(String c){
        this.gradersComment = c;
    }
    public String getGradersComment(){
        if(this.gradersComment == null) 
            return "";
        else 
            return this.gradersComment;
    }
    public QuestionVO()
    {
        super();
        this.pointValuePerAnswer = 0;
        this.pointValueTotal = 0;
        this.pointValueEarnedTotal = 0;
        this.numSolutions = 0;
    }

}