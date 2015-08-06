/***********************************************************************
* Module:  MultipleChoiceVO.java
* Author:  jsandlin
* Purpose: Defines the Class MultipleChoiceVO
***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;
    
public class MultipleChoiceVO extends QuestionVO
{
    private static String QUESTION_FIELD = "MultChoiceQ";
    private static String SOLUTION_FIELD = "MultChoiceS";
    private static String[] CHOICE_FIELD = {"choiceA", "choiceB", "choiceC", "choiceD"};
    //private static String[] CHOICE_FIELD = {"A", "B", "C", "D"};
    private String question;
    private String solution;
    private String empAnswer;
    private String[] choices;
    private boolean empCorrect;

    public void logValues()
    {
        super.logValues();
        Secretary.write("exam", "question = " + question);
        for(int x=0; x<4; x++)
        {
            Secretary.write("exam", "    " + CHOICE_FIELD[x] + " = " + choices[x]);
        }
        Secretary.write("exam", "solution = " + solution);
        if(empAnswer != null)
        {
            Secretary.write("exam", "empAnswer = " + empAnswer);
            Secretary.write("exam", "empCorrect = " + empCorrect);
        }
        Secretary.write("exam", "--------------------------------");
    }
    public String displayForm()
    {
        //Secretary.startFxn("exam", "MultipleChoiceVO.displayForm(" + this.getQuestionNum() + ")");
        String form = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">Point Value: <input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\"></td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">" 
            + this.getQuestionNum() + ")</td>\n"
            + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n"
            + "\t\t\t<textarea class=\"description\" name=\"" + QUESTION_FIELD 
            + "\">" + this.question + "</textarea></td>\n\t</tr>\n";
        for(int x = 0; x < 4; x++)
        {
            form += "\t<tr>\n"
                + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n"
                + "\t\t\t&nbsp;<input type=\"radio\" name=\"" + SOLUTION_FIELD
                +  "\" value=\"" + LETTER[x] + "\">\n\t\t</td>\n"
                + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n"
                + "\t\t\t<textarea class=\"multChoice\" name=\"" + CHOICE_FIELD[x] + "\"></textarea>\n"
                + "\t\t</td>\n"
                + "\t</tr>\n";
        }
        form += "</table>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayForm(" + this.getQuestionNum() + ")");
        return form;
    }
    public String displayToMod()
    {
        //Secretary.startFxn("MultipleChoiceVO.displayToMod()");
        String form = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">Point Value: "
            + "<input type=\"text\" size=\"1\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\"" 
            + this.getPointValuePerAnswer() + "\"></td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">" 
            + this.getQuestionNum() + ")</td>\n"
            + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n"
            + "\t\t\t<textarea class=\"description\" name=\"" + QUESTION_FIELD 
            + "\">" + this.question + "</textarea>\n"
            + "\t\t</td>\n\t</tr>\n";
        for(int x = 0; x < 4; x++)
        {
            form += "\t<tr>\n" + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n"
            + "\t\t\t&nbsp;<input type=\"radio\" name=\"" + SOLUTION_FIELD
            +  "\" value=\"" + LETTER[x] + "\"";
            if(choices[x] != null)
            {
                if(this.solution.compareTo(choices[x]) == 0)
                    form += " checked";
            }
            form += ">\n\t\t</td>\n"
            + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n"
            + "\t\t\t<textarea class=\"description\" name=\"" + CHOICE_FIELD[x] + "\">";
            if(choices[x] != null)
            {
                form += replaceSubString(choices[x], "\"", "&quot;");
            }
            form += "</textarea>\n"
            + "\t\t</td>\n"
            + "\t</tr>\n";
        }
        form += "\t</TABLE>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayToMod()");
        return form;
    }   

    public String displayToGrade() {
        //Secretary.startFxn("MultipleChoiceVO.displayToGrade()");
        String display;
        display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b>\n\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n\t"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>";
        for(int x=0; x < 4; x++)
        {
            display += "\n\t<tr>\n";
            display += "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t";
            if(this.empAnswer.compareTo(choices[x]) == 0)
            {
                if(this.empCorrect)
                    display += "<IMG SRC=\"./images/check_mark.gif\">";
                else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            }
            else
            {
                display += "&nbsp;";
            }
            display += "\n\t\t</td>\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
            if(this.empAnswer.compareTo(choices[x]) == 0)
                display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
            else
                display += LETTER[x] + ") " + choices[x];
            display += "\n\t\t</td>\n\t</tr>";
        }
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\""
            + " value=\"" + this.getPointValueEarnedTotal() + "\""
            + " onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\">\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td>\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>\n"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayToGrade()");
        return display;
    }
    

    public String displayGraded()
    {
        //Secretary.startFxn("MultipleChoiceVO.displayGraded()");
        String display;
        display = "<TABLE class=entry>\n\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td colspan=\"" + Q_NUM_TD_WIDTH + "\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>";
        //Secretary.write("exam", "empAnswer = '" + this.empAnswer  + "'");
        for(int x=0; x < 4; x++)
        {
            //Secretary.write("exam", "empAnswer = '" + this.empAnswer  + "'");
            //Secretary.write("exam", "choice["+x+"] = '" + choices[x]  + "'");
            //Secretary.write("exam", "this.empAnswer.compareTo(choices[x]) = " + this.empAnswer.compareTo(choices[x]));
            //Secretary.write("exam", "this.empAnswer.equals(choices[x]) = " + this.empAnswer.equals(choices[x]));
            display += "\n\t<tr>\n";
            display += "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t";
            if(this.empAnswer.compareTo(choices[x]) == 0)
            {
                if(this.empCorrect)
                    display += "<IMG SRC=\"./images/check_mark.gif\">";
                else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            }
            else
            {
                display += "&nbsp;";
            }
            display += "\n\t\t</td>\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
            if(this.empAnswer.compareTo(choices[x]) == 0)
                display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
            else
                display += LETTER[x] + ") " + choices[x];
            display += "\n\t\t</td>\n\t</tr>";
        }
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"2\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n";
        }
        display += "</table>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayGraded()");
        return display;
    }

    public String displayToTake()
    {
        //Secretary.startFxn("MultipleChoiceVO.displayToTake()");
        String display;
        display = "<TABLE class=entry>\n\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td colspan=\"" + Q_NUM_TD_WIDTH + "\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>";
        for(int x=0; x < 4; x++)
        {
            display += "\n\t<tr>\n"
                + "\t\t<td width=\"" + RADIO_TD_WIDTH 
                + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
                + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t"
                + "<input type=\"radio\" name=\"" + this.getEntryTypeCode() + "_" + this.getQuestionNum()
                +  "\" value=\"" + CHOICE_FIELD[x] + "\"";
            if((this.empAnswer != null) && (choices[x].equals(this.empAnswer)))
                display += " checked";
            display += ">&nbsp;"
                + choices[x]
                + "\n\t\t</td>\n"
                + "\t</tr>\n\t";
        }
        display += "</table>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayToTake()");
        return display;
    }

    public String displayToView()
    {
        //Secretary.startFxn("MultipleChoiceVO.displayToView()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < 4; x++)
        {
            display += "\t<tr>\n"
                + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t&nbsp;\n\t\t</td>\n"
                + "\t\t<td VALIGN=\"top\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
            if(this.choices[x] != null)
            {
                if(this.solution.compareTo(choices[x]) == 0)
                    display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
                else
                    display += LETTER[x] + ") " + choices[x];
            }
            display += "\n\t\t</td>\n\t</tr>\n";
        }
        display += "</table>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayToView()");
        return display;
    }
    public String displayToSelect()
    {
        //Secretary.startFxn("MultipleChoiceVO.displayToView()");
        String display = "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n\t\t<td colspan=\"2\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b>\n\t\t</td>\n\t</tr>\n\t<tr>\n"
            + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" 
            + this.question + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < 4; x++)
        {
            display += "\t<tr>\n"
                + "\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\" VALIGN=\"top\">\n\t\t\t&nbsp;\n\t\t</td>\n"
                + "\t\t<td VALIGN=\"top\" width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t";
            if(this.choices[x] != null)
            {
                if(this.solution.compareTo(choices[x]) == 0)
                    display += "<B>" + LETTER[x] + ") " + choices[x] + "</B>";
                else
                    display += LETTER[x] + ") " + choices[x];
            }
            display += "\n\t\t</td>\n\t</tr>\n";
        }
        display += "</table>\n";
        //Secretary.endFxn("exam", "MultipleChoiceVO.displayToView()");
        return display;
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
       // Secretary.startFxn("MultipleChoice.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        this.question = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        this.solution = (String)request.getParameter("choice" + newSolution);
        for(int x = 0; x < 4; x++)
        {
            String choice = (String)request.getParameter(CHOICE_FIELD[x]);
            this.choices[x] = choice;
        }
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
      //  Secretary.endFxn("exam", "MultipleChoice.processCreateForm(request, " + time + ")");
    }
    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("MultipleChoice.processModForm(request)");
        this.question = (String)request.getParameter(QUESTION_FIELD);
        String newSolution = (String)request.getParameter(SOLUTION_FIELD);
        this.solution = (String)request.getParameter("choice" + newSolution);
        for(int x = 0; x < 4; x++)
        {
            String choice = (String)request.getParameter(CHOICE_FIELD[x]);
            this.choices[x] = choice;
        }
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "MultipleChoice.processModForm(request)");
    }

    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //Secretary.startFxn("MultipleChoiceVO.processGradingForm()");
        String paramName;
        Boolean thisCorrect;
        paramName = "comment_" + this.getExamLoc();
        String comment = replaceSubString(((String)(request.getParameter(paramName))), "\n", "<BR>");
        comment = replaceSubString(comment, " ", "&nbsp;");
        this.setGradersComment(comment);
        paramName = "points_earned_" + this.getExamLoc();
        Integer pointsEarned = new Integer((String)(request.getParameter(paramName)));
        this.setPointValueEarnedTotal(pointsEarned.intValue());
        //Secretary.endFxn("exam", "MultipleChoiceVO.processGradingForm()");
    }

    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("MultpleChoice.processEmpAnswer()");
        // Fetch the character answer the employee entered.
        String temp = (String)request.getParameter(this.getEntryTypeCode() + "_" + this.getQuestionNum());
        //Secretary.write("exam", "temp = " + temp);
        // Figure out where in the array this answer is.
        int arrLoc = Arrays.binarySearch(CHOICE_FIELD, temp);
        // Fetch the choice in this array loc
        this.empAnswer = choices[arrLoc];
        this.empCorrect = this.empAnswer.equalsIgnoreCase(this.solution);
        //for(int k=0; k < 4 ; k++)
        //{
        //    String answ = CHOICE_FIELD[k];            
        //}
        if(this.empCorrect){
            this.setPointValueEarnedTotal(this.getPointValueTotal());
        }
        else{
            this.setPointValueEarnedTotal(0);
        }
        //Secretary.endFxn("exam", "MultpleChoice.processEmpAnswer()");
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

    public String[] getChoices()
    {
        return choices;
    }

    public void setChoices(String[] newChoices)
    {
        choices = newChoices;
    }

    public String getChoiceNum(int x)
    {
        return this.choices[x];
    }

    public String getEmpAnswer()
    {
        return empAnswer;
    }

    public void setEmpAnswer(String newEmpAnswer)
    {
        empAnswer = newEmpAnswer;
    }

    public boolean getEmpCorrect()
    {
        return empCorrect;
    }

    public void setEmpCorrect(boolean newEmpCorrect)
    {
        empCorrect = newEmpCorrect;
        if(!empCorrect)
        { 
            this.setPointValueEarnedTotal(0);
        }
        else
        {
            this.setPointValueEarnedTotal(this.getPointValueTotal());
        }
    }
     
    public boolean getAnswerEntered()
    {
        if(this.getEmpAnswer() == null)
            return(false);
        else
            return(true);
    }
    
    public MultipleChoiceVO()
    {
        super();
        this.question = "";
        this.solution = "";
        this.choices = new String[4];
        this.setNumSolutions(1);
        //this.setEntryTypeCode("mc");
    }

}