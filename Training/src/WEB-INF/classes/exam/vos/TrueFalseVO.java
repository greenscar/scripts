/***********************************************************************
* Module:  TrueFalseVO.java
* Author:  jsandlin
* Purpose: Defines the Class TrueFalseVO
***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;
public class TrueFalseVO extends QuestionVO
{
    private String QUESTION_FIELD = "TrueFalseQ";
    private String SOLUTION_FIELD = "TrueFalseS";
    private String question;
    private boolean solution;
    private Boolean empAnswer;
    private boolean empCorrect;

    public void logValues(){
        super.logValues();
        log.write("exam", "    question = " + question);
        if(solution) log.write("exam", "    solution = TRUE");
        else log.write("exam", "    solution = FALSE");
        if(this.empAnswer != null)
        {
            log.write("exam", "    empAnswer = " + empAnswer);
            log.write("exam", "    empCorrect = " + empCorrect);
        }
        log.write("exam", "--------------------------------");
    }

    public String displayForm()
    {
        //log.write("exam", "TrueFalseVO.displayForm()");
        String toReturn =  "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">Point Value: <input type=\"text\" size=\"2\" name=\"" 
                + this.POINT_VALUE_FIELD + "\"></td>\n\t</tr>\n"
                +  "\t<tr>\n\t\t<td width=" + RADIO_TD_WIDTH + ">\n\t\t\t" + this.getQuestionNum() + ")\n\t\t</td>\n"
                +  "\t\t<td width=" + QUESTION_TD_WIDTH + "> \n"
                +  "\t\t\t<input type=text name=" + QUESTION_FIELD + " size=" + Q_TEXT_FIELD_WIDTH + ">\n"
                +  "\t\t</td>\n\t</tr>\n\t<tr>\n"
                +  "\t\t<td width=" + RADIO_TD_WIDTH + ">&nbsp</td>\n"
                +  "\t\t<td width=" + QUESTION_TD_WIDTH + ">\n"
                + "TRUE:\n"
                +  "\t\t\t<input type=radio name=" + SOLUTION_FIELD + " value=true>\n"
                +  "\t\t\t&nbsp;&nbsp;&nbsp;FALSE:\n"
                +  "\t\t\t<input type=radio name=" + SOLUTION_FIELD + " value=false>\n"
                +  "\t\t</td>\n\t</tr>\n</table>\n";
        //log.write("exam", toReturn);
        return toReturn;
    }

    public String displayToMod()
    {
        //log.write("exam", "TrueFalseVO.displayToMod()");
        String form;
        form = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\"" + this.getPointValuePerAnswer() 
            + "\">\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td width=" + RADIO_TD_WIDTH 
            + ">\n\t\t\t" + this.getQuestionNum() + ")\n\t\t</td>\n"
            +  "\t\t<td width=" + QUESTION_TD_WIDTH + ">\n"
            +  "\t\t\t<textarea class=\"description\" name=\"" + QUESTION_FIELD + "\">"
            +  replaceSubString(question, "\"", "&quot;") + "</textarea>\n"
            +  "\t\t</td>\n\t</tr>\n\t<tr>\n"
            +  "\t\t<td width=" + RADIO_TD_WIDTH + ">&nbsp</td>\n"
            +  "\t\t<td width=" + QUESTION_TD_WIDTH + ">\n"
            + "\t\t\tTRUE:"
            + "<input type=radio name=\"" + SOLUTION_FIELD + "\" value=true";
        if((this.question != "") && this.solution) form += " CHECKED";
        form += ">\n\t\t\t&nbsp;&nbsp;&nbsp;\n\t\t\tFALSE:"
            +  "<input type=radio name=" + SOLUTION_FIELD + " value=false";
        if((this.question != "") && !this.solution) form += " CHECKED";
        form += ">\n"
            +  "\t\t</td>\n\t</tr>\n</table>\n";
        return form;
    }

    
    public String displayToGrade() {
        //log.write("exam", "TrueFalse.displayToGrade()");
        String answer;
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValueTotal() + "</b>\n\t\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n\t\t<tr>\n"
            + "\t\t<td width=\""+ Q_NUM_TD_WIDTH
            + "\" VALIGN=\"top\">\n\t\t\t" + this.getQuestionNum()
            + ")\n\t\t</td>\n\t\t<td>\n\t\t\t"
            + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t"
            + "<IMG SRC=\"";
        //log.write("exam", "this.empCorrect = " + this.empCorrect);
        if(this.empCorrect)
        display += "./images/check_mark.gif";
        else
        display += "./images/x_mark.gif";
        display += "\">\n\t\t</td>\n\t\t<td>\n\t\t\t";
        if(this.empAnswer != null)
        {
            if(this.empAnswer.booleanValue())
                display += "<B>TRUE</B>";
            else
                display += "<B>FALSE</B>";
        }
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        display += "\n\t\t</td>\n\t</tr>"
            + "\n\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\""
            + " value=\"" + this.getPointValueEarnedTotal() + "\""
            + " onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\">\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td>\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>\n"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        return display;
    }
    public String displayGraded()
    {
        //log.write("exam", "TrueFalse.displayGraded()");
        String answer;
        String display = "<TABLE class=entry>\n\t<tr>\n"
            + "\t\t<td width=\""+ Q_NUM_TD_WIDTH
            + "\" VALIGN=\"top\">\n\t\t\t" + this.getQuestionNum()
            + ")\n\t\t</td>\n\t\t<td width=\""+QUESTION_TD_WIDTH+"\">\n\t\t\t"
            + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t"
            + "<IMG SRC=\"";
        //log.write("exam", "this.empCorrect = " + this.empCorrect);
        if(this.empCorrect)
        display += "./images/check_mark.gif";
        else
        display += "./images/x_mark.gif";
        display += "\">\n\t\t</td>\n\t\t<td width=\""+QUESTION_TD_WIDTH+"\">\n\t\t\t";
        if(this.empAnswer.booleanValue())
        display += "<B>TRUE</B>";
        else
        display += "<B>FALSE</B>";
        display += "\n\t\t</td>\n\t</tr>";
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"2\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n\t";
        }
        display += "</table>\n";
        return display;
    }

    public String displayToTake()
    {
        //log.write("exam", "TrueFalseVO.displayToTake()");
        String display = "<TABLE class=entry>\n\t<tr>\n"
            + "\t<td width=\""+Q_NUM_TD_WIDTH+"\" VALIGN=\"top\">\n\t\t\t" + this.getQuestionNum()
            + ")</td>\n\t\t<td colspan=\"2\">\n\t\t\t"
            + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\""+Q_NUM_TD_WIDTH+"\">\n\t\t\t&nbsp\n\t\t</td>\n"
            + "\t\t<td width=\"5%\">\n\t\t\t"
            + "<input type=\"radio\" name=\"" 
            + this.getEntryTypeCode() + "_" + this.getQuestionNum()
            + "\" value=\"true\"";
        if((this.empAnswer != null) && (this.empAnswer.booleanValue()))
        {
            display += " checked";
        }
        display += ">TRUE\n\t\t"
            + "</td>\n\t\t<td VALIGN=\"top\">\n\t\t\t"
            + "<input type=\"radio\" name=\"" 
            + this.getEntryTypeCode() + "_" + this.getQuestionNum()
            + "\" value=\"false\"";
        if((this.empAnswer != null) && (!this.empAnswer.booleanValue()))
        {
            display += " checked";
        }
        display += ">FALSE\n\t\t"
            + "</td>\n\t</tr>\n</table>\n";
        return display;
    }


    public String displayToView()
    {
        //log.write("exam", "TrueFalseVO.displayToView()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\""+Q_NUM_TD_WIDTH+"\">\n\t\t\t" + this.getQuestionNum()
            + ")\n\t\t</td>\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t"
            + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\""+Q_NUM_TD_WIDTH+"\">\n\t\t\t&nbsp\n\t\t</td>\n"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
        if(this.solution)
            display += "<B>TRUE</B>";
        else
            display += "<B>FALSE</B>";
        display += "\n\t\t</td>\n\t</tr>\n</table>\n";
        return display;
    }
    public String displayToSelect()
    {
        //log.write("exam", "TrueFalseVO.displayToView()");
        String display = "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\""+Q_NUM_TD_WIDTH+"\">\n\t\t\t" + this.getQuestionNum()
            + ")\n\t\t</td>\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t"
            + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\""+Q_NUM_TD_WIDTH+"\">\n\t\t\t&nbsp\n\t\t</td>\n"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
        if(this.solution)
            display += "<B>TRUE</B>";
        else
            display += "<B>FALSE</B>";
        display += "\n\t\t</td>\n\t</tr>\n</table>\n";
        return display;
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "TrueFalse.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        this.question = (String)request.getParameter(QUESTION_FIELD);
        Boolean x = new Boolean((String)request.getParameter(SOLUTION_FIELD));
        this.solution = x.booleanValue();
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "TrueFalse.processCreateForm(request, " + time + ")");
    }

    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "TrueFalse.processModForm(request)");
        this.question = (String)request.getParameter(QUESTION_FIELD);
        Boolean x = new Boolean((String)request.getParameter(SOLUTION_FIELD));
        this.solution = x.booleanValue();
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "TrueFalse.processModForm(request)");
    }
    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //Secretary.startFxn("exam", "TrueFalseVO.processGradingForm(request)");
        String paramName;
        Boolean thisCorrect;
        paramName = "comment_" + this.getExamLoc();
        String comment = "";
        //if(request.getParameter(paramName) != null)
        //{
            comment = replaceSubString(((String)(request.getParameter(paramName))), "\n", "<BR>");
            Secretary.write("exam", "comment = " + comment);
            comment = replaceSubString(comment, " ", "&nbsp;");
        //}
        this.setGradersComment(comment);
        paramName = "points_earned_" + this.getExamLoc();
        Integer pointsEarned = new Integer((String)(request.getParameter(paramName)));
        //Secretary.write("exam", "pointsEarned = " + pointsEarned);
        this.setPointValueEarnedTotal(pointsEarned.intValue());
        //Secretary.endFxn("exam", "TrueFalseVO.processGradingForm(request)");
    }

    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        //log.write("exam", "TrueFalseVO.processEmpAnswerForm");
        Boolean x = new Boolean((String)request.getParameter(this.getEntryTypeCode() + "_" + this.getQuestionNum()));
        this.empAnswer = x;
        //log.write("exam", "this.empAnswer == " + this.empAnswer);
        //log.write("exam", "this.solution == " + this.solution);
        this.empCorrect = this.getEmpAnswerCorrect();
        if(this.empCorrect){
            this.setPointValueEarnedTotal(this.getPointValueTotal());
        }
        else{
            this.setPointValueEarnedTotal(0);
        }
        //log.write("exam", "this.empCorrect == " + this.empCorrect);
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String newQuestion)
    {
        question = newQuestion;
    }

    public boolean getSolution()
    {
        return solution;
    }

    public void setSolution(boolean newSolution)
    {
        solution = newSolution;
    }

    public boolean getEmpAnswerCorrect()
    {
        return this.empAnswer.equals(new Boolean(this.solution));
    }

    public boolean getEmpAnswer()
    {
        return this.empAnswer.booleanValue();
    }
    public void setEmpAnswer(int answer)
    {
        log.write("exam", "TrueFalseVO.setEmpAnswer("+answer+")");
        if(answer == 0) this.empAnswer = new Boolean(false);
        else this.empAnswer = new Boolean(true);
    }
    
    public void setEmpAnswer(boolean newEmpAnswer)
    {
        this.empAnswer = new Boolean(newEmpAnswer);
    }

    public boolean getEmpCorrect()
    {
        return empCorrect;
    }

    public String getSolutionToDisplay()
    {
        Boolean x = new Boolean(this.solution);
        return(x.toString().toUpperCase());
    }

    public void setSolution(int solution)
    {
        if(solution == 0) this.solution = false;
        else this.solution = true;
    }

    
    public void setEmpCorrect(int correct)
    {
        //log.write("exam", "TrueFalseVO.setEmpCorrect("+correct+")");
        if(correct < 0)
        { 
            this.empCorrect = false;
            this.setPointValueEarnedTotal(0);
        }
        else
        {
            this.empCorrect = true;
            this.setPointValueEarnedTotal(this.getPointValueTotal());
        }
    }
    
    public boolean getAnswerEntered()
    {
        if((this.empAnswer != null) && ((this.getEmpAnswer() == true) || (this.getEmpAnswer() == false)))
            return true;
        else
            return false;
    }
    
    public TrueFalseVO()
    {
        super();
        this.question = "";
        this.setNumSolutions(1);
        //this.setEntryTypeCode("tf");
    }

}