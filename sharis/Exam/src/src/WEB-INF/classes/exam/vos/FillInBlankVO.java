/***********************************************************************
 * Module:  FillInBlankVO.java
 * Author:  jsandlin
 * Purpose: Defines the Class FillInBlankVO
 ***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;

public class FillInBlankVO extends QuestionVO
{
    private java.lang.String QUESTION_FIELD = "FillInBlankQ";
    private java.lang.String EMP_CORRECT_FIELD = "fibCorrect";
    private String[] SOLUTION_FIELDS = new String[MAX_NUM_ENTRIES];
    private String question;
    private Vector solutions;
    private Vector empAnswers;
    private Vector empCorrect;
    
    public void logValues()
    {
        super.logValues();
        log.write("exam", "    question = " + this.question);
        log.write("exam", "    solutions = ");
        for(int x=0; x<solutions.size(); x++)
        {
            log.write("exam", "       " + this.getSolutionAt(x));
        }
        if(empAnswers != null)
        {
            log.write("exam", "    empAnswers = ");
            for(int x=0; x<empAnswers.size(); x++)
            {
                log.write("exam", "        " + this.getEmpAnswerAt(x));
            }
            if(empCorrect != null)
            {
                log.write("exam", "    empCorrect = ");
                for(int x=0; x<empCorrect.size(); x++)
                {
                    log.write("exam", "        " + ((Boolean)(empCorrect.elementAt(x))).toString());
                }
            }
        }
        log.write("exam", "--------------------------------");
    }
    
    public String displayForm()
    {
        //log.write("exam", "FillInBlankVO.displayForm()");
        String form= "<TABLE class=entry>\n\t\t<tr>\n\t\t\t<td colspan=\"4\"><input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\"> Points Each</td>\n\t\t</tr>\n"
            + "\t\t<tr>\n\t\t\t<td width=\""
            + Q_NUM_TD_WIDTH + "\">" + this.getQuestionNum() + ")</td>\n"
            + "\t\t\t<td width=\"" + Q_TEXT_FIELD_WIDTH 
            + "\" colspan=\"3\">\n" 
            + "\t\t\t\t<textarea class=\"description\" name=\"" + QUESTION_FIELD 
            + "\"></textarea>\n\t\t\t</td>\n\t\t</tr>\n";   
        // Print MAX_NUM_ENTRIES blanks in rows of 3.
        for(int x=0; x < MAX_NUM_ENTRIES; x++){
            form += "\t\t<tr>\n\t\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">&nbsp;</td>\n";
            for(int y=0;(y<3 &&  x < MAX_NUM_ENTRIES);y++){
                form += "\t\t\t<td width=\"" + QUESTION_TD_WIDTH + "\">\n"
                    + "\t\t\t\t<input type=\"text\" name=\""
                    + SOLUTION_FIELDS[x] 
                    + "\" size=\"" + BLANK_WIDTH 
                    + "\">\n\t\t\t</td>\n";
                x++;
            }
            form += "\t\t</tr>\n";
        }
        form += "</table>\n";
        return form;
    }

    public String displayToMod()
    {
        //log.write("exam", "FillInBlankVO.displayToMod()");
        String tempQ = replaceSubString(question, "\"", "&quot;");
        //log.write("exam", "tempQ = " + tempQ);
        tempQ = replaceSubString(tempQ, "<br>", "\n");
        //log.write("exam", "tempQ = " + tempQ);
        String display= "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"4\"><input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\"" + this.getPointValuePerAnswer()
            + "\"\"> points each</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\""
            + Q_NUM_TD_WIDTH + "\">" + this.getQuestionNum() + ")</td>\n"
            + "\t\t<td width=\"" + Q_TEXT_FIELD_WIDTH 
            + "\" colspan=\"3\">" 
            + "<textarea class=\"description\" name=\""
            + QUESTION_FIELD + "\""
            + ">" + tempQ + "</textarea></td>\n\t</tr>\n";
        //log.write("exam", "display = " + display);
        // Print MAX_NUM_ENTRIES blanks in rows of 3.
        int x=0, colNum = 0;
        while(x < MAX_NUM_ENTRIES)
        {
            // Add the column of dead space on the left.
            display += "\t<tr>\n"
                +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">&nbsp;</td>\n";
            // Add the actual Solutions
            while((MAX_NUM_ENTRIES > (NUM_COLS_PER_ROW * colNum)) && (x < MAX_NUM_ENTRIES) && (colNum < 3))
            {
                display += "\t\t<td valign=\"top\"><input type=\"text\" name=\""
                        + SOLUTION_FIELDS[x] + "\" size=\"" + BLANK_WIDTH + "\"";
                if(x < solutions.size())
                {
                    display += " value=\"" + replaceSubString(((String)solutions.elementAt(x)), "\"", "&quot;") + "\"";
                }
                display +=  "></td>\n";
                x++;
                colNum++;
            }
            display += "\t</tr>";
            colNum = 0;
        }
        display += "\n</table>\n";
        return display;
    }


    public String displayToGrade() {
        //log.write("exam", "FillInBlankVO.displayToGrade()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"5\">\n\t\t\tPoint Value: <b>" + this.getPointValuePerAnswer()
            + "</b> points each\n\t\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" 
            + RADIO_TD_WIDTH + "\" valign=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
            +  "&nbsp;\n\t\t</td>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
            +  "&nbsp;\n\t\t</td>\n\t\t"
            + "<td colspan=\"2\" width=\""+QUESTION_TD_WIDTH+"\">\n\t\t\t" + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n";
        int colNum = 0;
        int size = solutions.size();
        int numCols = 3;
        int x = 0;
        int entryTdWidth = ((QUESTION_TD_WIDTH - (RADIO_TD_WIDTH * 3))/2);
        display += "\t<tr>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
            +  "<img src=\"./images/check_mark.gif\">"
            +  "\n\t\t</td>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
            +  "<img src=\"./images/x_mark.gif\">"
            +  "\n\t\t</td>\n"
            +  "\t\t<td width=\"" + entryTdWidth + "\">\n\t\t\t"
            + "<P STYLE=\"color:green\">"
            + "Employee's Answers</P>\n\t\t</td>\n"
            +  "\t\t<td width=\"" + entryTdWidth + "\">\n\t\t\t"
            + "<P STYLE=\"color:blue\">"
            + "Solutions</P>\n\t\t</td>\n"
            +  "\t</tr>\n";
        String empCorrectField;
        while(x < size)
        {
            empCorrectField = EMP_CORRECT_FIELD + "_" + this.getExamLoc() + "_" + x;
            // Add the column of dead space on the left.
            display += "\t<tr>\n"
                +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
                +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
                +  "<input type=\"radio\" name=\"" + empCorrectField + "\" value=\"true\" ";
            if((this.empCorrect != null) && (this.getEmpCorrectAt(x)))
            {
                display += "checked ";
            }
            display +=  "onClick=\"sendValueToPoints(this.form, " + this.getExamLoc() + ", " + this.getNumSolutions() + ", " + this.getPointValuePerAnswer() + ", '" + EMP_CORRECT_FIELD + "');\">"
                +  "\n\t\t</td>\n"
                +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t"
                +  "<input type=\"radio\" name=\"" + empCorrectField + "\" value=\"false\" ";
            if((this.empCorrect != null) && (!this.getEmpCorrectAt(x)))
            {
                display += "checked ";
            }
            display += "onClick=\"sendValueToPoints(this.form, " + this.getExamLoc() + ", " + this.getNumSolutions() + ", " + this.getPointValuePerAnswer() + ", '" + EMP_CORRECT_FIELD + "');\">"
                +  "\n\t\t</td>\n";
            // Add the actual Solutions
            display += "\t\t"
                + "<td valign=\"top\" width=\"" + entryTdWidth + "\">\n\t\t\t<B><U>&nbsp;"
                + ((String)empAnswers.elementAt(x))
                + "&nbsp;</U></B>\n\t\t</td>\n";
            display += "\t\t"
                + "<td valign=\"top\" width=\"" + entryTdWidth + "\">\n\t\t\t"
                + ((String)solutions.elementAt(x))
                + "\n\t\t</td>\n";
            x++;
            display += "\t</tr>\n";
            colNum = 0;
        }
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\""
            + " value=\"" + this.getPointValueEarnedTotal() + "\""
            + " onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\">\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\" colspan=\"4\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"4\">\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>\n"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        return display;
    }    


    public String displayGraded()
    {
        //this.logValues();
        //log.write("exam", "FillInBlankVO.displayGraded()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td width=\"" 
            + RADIO_TD_WIDTH + "\" valign=\"top\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t"
            + "<td colspan=\"2\" width=\""+QUESTION_TD_WIDTH+"\">\n\t\t\t" + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n";
        int colNum = 0;
        int size = solutions.size();
        int numCols = 3;
        int x = 0;
        display += "\t<tr>\n"
            +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n"
            +  "\t\t<td width=\"" + (QUESTION_TD_WIDTH/2) + "\">\n\t\t\t"
            + "<P STYLE=\"color:green\">"
            + "Employee's Answers</P>\n\t\t</td>\n"
            +  "\t\t<td width=\"" + (QUESTION_TD_WIDTH/2) + "\">\n\t\t\t"
            + "<P STYLE=\"color:blue\">"
            + "Solutions</P>\n\t\t</td>\n"
            +  "\t</tr>\n";
        while(x < size)
        {
            // Add the column of dead space on the left.
            display += "\t<tr>\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t";
            if(this.empCorrect != null)
            {
                display += "<img src=\"";
                if(this.getEmpCorrectAt(x))
                    display += "./images/check_mark.gif";
                else
                    display += "./images/x_mark.gif";                
                display += "\">\n\t\t";
            }
            else
            {
                display += "&nbsp;\n\t\t";
            }
            display += "</td>\n";
            // Add the actual Solutions
            display += "\t\t"
                + "<td valign=\"top\" width=\"" + (QUESTION_TD_WIDTH/2) + "\">\n\t\t\t<B><U>&nbsp;"
                + ((String)empAnswers.elementAt(x))
                + "&nbsp;</U></B>\n\t\t</td>\n";
            display += "\t\t"
                + "<td valign=\"top\" width=\"" + (QUESTION_TD_WIDTH/2) + "\">\n\t\t\t"
                + ((String)solutions.elementAt(x))
                + "\n\t\t</td>\n";
            x++;
            display += "\t</tr>\n";
            colNum = 0;
        }
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"2\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"3\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n";
        }
        display += "\t</table>\n";
        return display;
    }

    public String displayToTake()
    {
        //log.write("exam", "FillInBlankVO.displayToTake()");
        int size = this.solutions.size();
        int colNum = 0;
        int numCols = 3;
        int x = 0;
        int colSpan = size / NUM_COLS_PER_ROW + 1;
        String display= "<TABLE class=entry>\n\t<tr>\n\t\t<td width=\""
            + Q_NUM_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum() + ")\n\t\t</td>\n"
            + "\t\t<td width=\"" + QUESTION_TD_WIDTH 
            + "\" colspan=\"" + colSpan + "\">\n" 
            + "\t\t\t" + this.question
            + "\n\t\t</td>\n\t</tr>\n";
        // Print MAX_NUM_ENTRIES blanks in rows of 3.
        while(x < size)
        {
            // Add the column of dead space on the left.
            display += "\t<tr>\n"
                +  "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n";
            // Add the actual Solutions
            while((size > (NUM_COLS_PER_ROW * colNum)) && (x < size) && (colNum <= numCols))
            {
                display += "\t\t<td valign=\"top\">\n\t\t\t"
                    + "<input type=\"text\" class=\"disableAutoComplete\" name=\""
                    + this.getEntryTypeCode() + "_" + this.getQuestionNum() + "_" 
                    + SOLUTION_FIELDS[x] + "\" size=\"" + BLANK_WIDTH 
                    + "\"";
                if((this.empAnswers != null) && (x < this.empAnswers.size()))
                    display += " value=\"" + ((String)(this.empAnswers.elementAt(x))) + "\"";
                display += ">\n\t\t</td>\n";
                x++;
                colNum++;
            }
            display += "\t</tr>";
            colNum = 0;
        }	
        display += "\n</table>\n";
        display += "\t<input type=\"hidden\" name=\"" + "num_blanks_" 
            + this.getQuestionNum() + "\" value=\"" + this.solutions.size() + "\">";
        return display;
    }

    public String displayToView()
    {
        //log.write("exam", "FillInBlankVO.displayToView()");
        int size = this.solutions.size();
        int colNum = 0;
        int numCols = 3;
        int x = 0;
        int colSpan = size / NUM_COLS_PER_ROW + 1;
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"" + (colSpan + 1) + "\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td width=\"" 
            + RADIO_TD_WIDTH + "\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td";
            if(colSpan > 1)
                display += " colspan=\"" + colSpan + "\"";
            display += " width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n";
        while(x < size)
        {
            // Add the column of dead space on the left.
            display += "\t<tr>\n\t\t"
                +  "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n";
            // Add the actual Solutions
            while((size > (NUM_COLS_PER_ROW * colNum)) && (x < size) && (colNum <= numCols))
            {
                display += " \t\t"
                    + "<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t<B><U>&nbsp; "
                    + ((String)solutions.elementAt(x))
                    + "&nbsp;</U></B>\n\t\t</td>\n";
                x++;
                colNum++;
            }
            display += "\t</tr>";
            colNum = 0;
        }
        display += "\n</table>\n";
        return display;
    }
   
    public String displayToSelect()
    { 
        //log.write("exam", "FillInBlankVO.displayToView()");
        int size = this.solutions.size();
        int colNum = 0;
        int numCols = 3;
        int x = 0;
        int colSpan = size / NUM_COLS_PER_ROW + 1;
        String display = "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n\t\t<td colspan=\"" + (colSpan + 1) + "\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td width=\"" 
            + RADIO_TD_WIDTH + "\">\n\t\t\t"
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td";
            if(colSpan > 1)
                display += " colspan=\"" + colSpan + "\"";
            display += " width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t" + this.question + "\n\t\t</td>\n"
            + "\t</tr>\n";
        while(x < size)
        {
            // Add the column of dead space on the left.
            display += "\t<tr>\n\t\t"
                +  "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n";
            // Add the actual Solutions
            while((size > (NUM_COLS_PER_ROW * colNum)) && (x < size) && (colNum <= numCols))
            {
                display += " \t\t"
                    + "<td width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t<B><U>&nbsp; "
                    + ((String)solutions.elementAt(x))
                    + "&nbsp;</U></B>\n\t\t</td>\n";
                x++;
                colNum++;
            }
            display += "\t</tr>";
            colNum = 0;
        }
        display += "\n</table>\n";
        return display;
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "FillInBlank.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        this.solutions = new Vector();
        question = (String)request.getParameter(QUESTION_FIELD);
        question = replaceSubString(question, "\"", "&quot;");
        //log.write("exam", "question = " + question);
        String entry;
        String empty = "";
        for(int x = 0; x < this.SOLUTION_FIELDS.length; x++)
        {
            //log.write("exam", "SOLUTION_FIELDS[" + x + "] = " + SOLUTION_FIELDS[x]);
            entry = request.getParameter(SOLUTION_FIELDS[x]);
            if((entry != null) && (entry.compareTo(empty) != 0))
            {
                //log.write("exam", "entry = " + entry);
                this.solutions.add(entry);
            }
            entry = null;
        }
        this.setNumSolutions(this.solutions.size());
        //log.write("exam", "getNumSolutions = " + this.getNumSolutions());
        Integer pointValuePerAnswer = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValuePerAnswer.intValue());
        //log.write("exam", "this.getPointValuePerAnswer = " + this.getPointValuePerAnswer());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "FillInBlank.processCreateForm(request, " + time + ")");
    }
   
    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "FillInBlank.processModForm(request)");
        this.solutions = new Vector();
        question = (String)request.getParameter(QUESTION_FIELD);
        question = replaceSubString(question, "\"", "&quot;");
        //log.write("exam", "question = " + question);
        String entry;
        String empty = "";
        for(int x = 0; x < this.SOLUTION_FIELDS.length; x++)
        {
            //log.write("exam", "SOLUTION_FIELDS[" + x + "] = " + SOLUTION_FIELDS[x]);
            entry = request.getParameter(SOLUTION_FIELDS[x]);
            if((entry != null) && (entry.compareTo(empty) != 0))
            {
                //log.write("exam", "entry = " + entry);
                this.solutions.add(entry);
            }
            entry = null;
        }
        this.setNumSolutions(this.solutions.size());
        //log.write("exam", "getNumSolutions = " + this.getNumSolutions());
        Integer pointValuePerAnswer = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValuePerAnswer.intValue());
        //log.write("exam", "this.getPointValuePerAnswer = " + this.getPointValuePerAnswer());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "FillInBlank.processModForm(request)");
    }
    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        log.write("exam", "FillInBlankVO.processEmpAnswerForm()");
        String paramName;
        this.empAnswers = new Vector();
        for(int x=0; x < this.solutions.size(); x++)
        {
            paramName = this.getEntryTypeCode() + "_" + this.getQuestionNum() 
                + "_" + SOLUTION_FIELDS[x];
            this.empAnswers.add((String)(request.getParameter(paramName)));
        }
    }

    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //log.write("exam", "FillInBlankVO.processGradingForm()");
        this.empCorrect = new Vector();
        String paramName;
        Boolean thisCorrect;
        paramName = "comment_" + this.getExamLoc();
        String comment = replaceSubString(((String)(request.getParameter(paramName))), "\n", "<BR>");
        comment = replaceSubString(comment, " ", "&nbsp;");
        this.setGradersComment(comment);
        paramName = "points_earned_" + this.getExamLoc();
        Integer pointsEarned = new Integer((String)(request.getParameter(paramName)));
        this.setPointValueEarnedTotal(pointsEarned.intValue());
        for(int x=0; x < this.solutions.size(); x++)
        {
            paramName = EMP_CORRECT_FIELD + "_" + this.getExamLoc() + "_" + x;
            thisCorrect = new Boolean(request.getParameter(paramName));
            this.empCorrect.add(thisCorrect);
        }
    }
    
    public String getQuestion()
    {
        return this.question;
    }

    public void setQuestion(String newQuestion)
    {
        question = newQuestion;
    }

    public void addEmpAnswer(String answ)
    {
        if(this.empAnswers == null)
            this.empAnswers = new Vector();
        this.empAnswers.add(answ);
    }
    
    public void addEmpCorrect(Boolean cor)
    {
        if(this.empCorrect == null)
            this.empCorrect = new Vector();
        this.empCorrect.add(cor);
    }
    public void addEmpCorrect (boolean b)
    {
        if(this.empCorrect == null)
            this.empCorrect = new Vector();
        this.empCorrect.add(new Boolean(b));
    }
    public void addSolution(String ans)
    {
        this.solutions.add(ans);
        this.setNumSolutions(this.getNumSolutions() + 1);
    }

    public int getEmpAnswersSize()
    {
        return this.empAnswers.size();
    }

    public int getSolutionsSize()
    {
        return this.getNumSolutions();
    }
    
    public String getSolutionAt(int vectorLoc)
    {
        return (String)(solutions.elementAt(vectorLoc));
    }
    public String getEmpAnswerAt(int vectorLoc)
    {
        return (String)(empAnswers.elementAt(vectorLoc));
    }
    public boolean getEmpCorrectAt(int vLoc)
    {
        return ((Boolean)(empCorrect.elementAt(vLoc))).booleanValue();
    }
    
    public boolean getAnswerEntered()
    {
        if(empAnswers.size() == solutions.size())
            return true;
        else
            return false;
    }
    
    public FillInBlankVO()
    {
        super();
        this.question = "";
        this.solutions = new Vector();
        //this.setEntryTypeCode("fb");
        String name;
        for(int x=0; x<MAX_NUM_ENTRIES; x++)
        {
            name = "solution_" + LETTER[x];
            java.lang.reflect.Array.set(SOLUTION_FIELDS, x, name);
        }
    }
}