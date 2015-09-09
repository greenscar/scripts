/***********************************************************************
 * Module:  EssayVO.java
 * Author:  jsandlin
 * Purpose: Defines the Class EssayVO
 ***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;
public class EssayVO extends QuestionVO
{
    private String QUESTION_FIELD = "EssayQ";
    private String SOLUTION_FIELD = "EssayS";
    private String question;
    private String solution;
    private String empAnswer;
    private boolean empCorrect;
    
    public void logValues()
    {
        super.logValues();
        log.write("exam", "    question = " + this.question);
        log.write("exam", "    solution = " + this.solution);
        if(empAnswer != null)
        {
            log.write("exam", "    empAnswer = " + empAnswer);
        }
        log.write("exam", "--------------------------------");
    }
    /** @param currentQNum */
    public String displayForm()
    {
        //log.write("exam", "EssayVO.displayForm()");
        String form = "<TABLE class=entry>\n\t<tr>\n"
            + "\t<td colspan=\"2\">Point Value: <input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\"></td>\n\t</tr>\n\t<tr>\n"
            + "\t<td width=\"" + Q_NUM_TD_WIDTH + "\"><P>" 
            + this.getQuestionNum() + ")</P></td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\"> \n"
            + "   \t\t<textarea class=\"header\" name=\"" + QUESTION_FIELD 
            + "\" size=\"" + Q_TEXT_FIELD_WIDTH + "\"></textarea>\n"
            + "\t</td>\n\t</tr>\n\t\t<td></td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\"> \n"
            + "\t\t<TEXTAREA CLASS=\"essay\" name=\"" + SOLUTION_FIELD 
            + "\" ></textarea>\n"
            + "\t</td>\n\t</tr>\n</table>\n";
        return form;
    }

    public String displayToMod()
    {
        //log.write("exam", "EssayVO.displayToMod()");
        String tempQ = replaceSubString(question, "\"", "&quot;");
        tempQ = replaceSubString(tempQ, "<br>", "\n");
        String tempS = replaceSubString(solution, "\"", "&quot;");
        tempS = replaceSubString(tempS, "<br>", "\n");
        tempS = replaceSubString(tempS, "<BR>", "\n");
        String form = "<TABLE class=entry>\n\t<tr>\n"
            + "\t\t<td colspan=\"2\">Point Value: <input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\"" + this.getPointValuePerAnswer() + "\"></td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<P>" 
            + this.getQuestionNum() + ")</P>\n\t\t</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t<textarea class=\"description\" name=\"" + QUESTION_FIELD +  "\">"
            + tempQ + "</textarea>\n"
            + "\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td width=\""
            + Q_NUM_TD_WIDTH + "\">&nbsp</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t<TEXTAREA name=\"" + SOLUTION_FIELD +  "\""
            + "  CLASS=\"essay\">"+ tempS +"</textarea>\n"
            + "\t\t</td>\n\t</tr>\n</table>\n";
        return form;
    }
    public String displayToGrade() {
        //log.write("exam", "EssayVO.displayToGrade()");
        String solToDisp = "na";
        if (this.solution.trim().length() == 0)
            solToDisp = "No solution entered.";
        else
            solToDisp = solution;
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        String display = "<TABLE class=entry>\n\t<tr>\n"
            + "\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>" + this.getPointValuePerAnswer() 
            + "</b>\n\t\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
            + "\t\t<td valign=\"top\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t<B>"
            + this.empAnswer + "</B>\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
            + "\t\t<td valign=\"top\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t<B STYLE=\"color:green\">"
            + solToDisp + "</B>\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\" "
            + " value=\"" + this.getPointValueEarnedTotal() + "\" onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\">\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td>\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        return display;
    }
    
    public String displayGraded()
    {
        //log.write("exam", "EssayVO.displayGraded()");
        String display = "<TABLE class=entry>\n<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">"
            + this.getQuestionNum() + ")</td>\n\t\t<td width=\""+QUESTION_TD_WIDTH+"\">" 
            + this.question + "</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" style=\"color: green;\">";
        display += this.pointValueEarnedTotal + "/" + this.pointValueTotal;
        display += "</td>\n"
            + " \t\t<td valign=\"top\" width=\""+QUESTION_TD_WIDTH+"\"><B>"
            + this.empAnswer + "</B></td>\n\t</tr>\n";
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"2\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n";
        }
        display += "</table>\n";
        return display;
    }

    public String displayToTake()
    {
        //log.write("exam", "EssayVO.displayToTake()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td colspan=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n"
            + "\t\t<td valign=\"top\">\n\t\t\t<TEXTAREA "
            + " CLASS=\"essay\" name=\""
            + this.getEntryTypeCode() + "_" + this.getQuestionNum()
            + "\">";
        if(this.empAnswer != null)
            display += replaceSubString(this.empAnswer, "<BR>", "\n");
        display += "</TEXTAREA>\n\t\t</td>\n\t</tr>\n</table>\n";
        return display;
    }

    public String displayToView()
    {
        //log.write("exam", "EssayVO.displayForm()");
        String solToDisp = "na";
        if (this.solution.trim().length() == 0)
            solToDisp = "No solution entered.";
        else
            solToDisp = solution;
        return "<TABLE class=entry>\n\t<tr>\n"
            + "\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>" + this.getPointValuePerAnswer() 
            + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<P>" 
            + this.getQuestionNum() + ")</P>\n\t\t</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t" + this.question + "\n"
            + "\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>\n\t\t\t&nbsp\n\t\t</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t<B>" + solToDisp + "</B>\n"
            + "\t\t</td>\n\t</tr>\n</table>\n";
    }
    public String displayToSelect()
    {
        //log.write("exam", "EssayVO.displayForm()");
        String solToDisp = "";
        if (this.solution.trim().length() > 0)
            solToDisp = solution;
        return "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n"
            + "\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>" + this.getPointValuePerAnswer() 
            + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<P>" 
            + this.getQuestionNum() + ")</P>\n\t\t</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t" + this.question + "\n"
            + "\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>\n\t\t\t&nbsp\n\t\t</td>\n\t\t<td width=\""
            + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t<B>" + solToDisp + "</B>\n"
            + "\t\t</td>\n\t</tr>\n</table>\n";
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "Essay.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        String newQuestion = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        this.question = replaceSubString(newQuestion, "\n", "<BR>");
        this.solution = replaceSubString(newSolution, "\n", "<BR>");
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "Essay.processCreateForm(request, " + time + ")");
    }
    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "Essay.processModForm(request)");
        String newQuestion = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        this.question = replaceSubString(newQuestion, "\n", "<BR>");
        this.solution = replaceSubString(newSolution, "\n", "<BR>");
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "Essay.processModForm(request)");
    }
    
    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        //log.write("exam", "EssayVO.processEmpAnswerForm()");
        String answ = (String)(request.getParameter(this.getEntryTypeCode() + "_" + this.getQuestionNum()));
        this.empAnswer = replaceSubString(answ, "\n", "<BR>");
    }

    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //log.write("exam", "EssayVO.processGradingForm()");
        String paramName;
        Boolean thisCorrect;
        paramName = "comment_" + this.getExamLoc();
        String comment = "";
        //if(request.getParameter(paramName) != null)
            comment = replaceSubString(((String)(request.getParameter(paramName))), "\n", "<BR>");
        comment = replaceSubString(comment, " ", "&nbsp;");
        this.setGradersComment(comment);
        paramName = "points_earned_" + this.getExamLoc();
        Integer pointsEarned = new Integer((String)(request.getParameter(paramName)));
        this.setPointValueEarnedTotal(pointsEarned.intValue());
        if(this.getPointValueEarnedTotal() > 0)
            this.setEmpCorrect(true);
        else
            this.setEmpCorrect(false);
    }
    
    public String getQuestion()
    {
        return question;
    }
    public void setQuestion(String newQuestion)
    {
        question = newQuestion;
    }
    
    public String getSolution()
    {
        return solution;
    }
    public void setSolution(String newSolution)
    {
        solution = newSolution;
    }

    public String getEmpAnswer()
    {
        return empAnswer;
    }    
    public void setEmpAnswer(String newEmpAnswer)
    {
        empAnswer = newEmpAnswer;
    }
    public void setEmpCorrect(boolean c)
    {
        this.empCorrect = c;
    }
    public boolean getEmpCorrect()
    {
        return this.empCorrect;
    }
    
    public boolean getAnswerEntered()
    {
        if(this.getEmpAnswer() == null)
            return false;
        else
            return true;
    }
    
    public EssayVO()
    {
        super();
        this.solution = "";
        this.question = "";
        this.setNumSolutions(1);
        //this.setEntryTypeCode("es");
    }

}