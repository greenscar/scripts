/***********************************************************************
* Module:  MatchingVO.java
* Author:  jsandlin
* Purpose: Defines the Class MatchingVO
***********************************************************************/
package exam.vos;
import logging.Secretary;
import java.util.*;

public class MatchingVO extends QuestionVO
{
    private static String DESCRIPTION_FIELD = "MatchingDescription";
    private static String[] QUESTION_FIELDS = new String[MAX_NUM_ENTRIES];
    private static String[] SOLUTION_FIELDS = new String[MAX_NUM_ENTRIES];
    private static String[] CHOICE_FIELDS = new String[MAX_NUM_ENTRIES];
    private String description = "";
    private Vector questions;
    private Vector choices;
    private Vector solutions;
    private Vector empAnswers;
    private Vector empCorrect;

    public void logValues()
    {
        super.logValues();
        log.write("exam", "    description = " + description);
        log.write("exam", "    questions = ");
        for(int x=0; x<questions.size(); x++)
        {
            log.write("exam", "        " + (String)questions.elementAt(x));
        }
        log.write("exam", "    choices = ");
        for(int x=0; x<choices.size(); x++)
        {
            log.write("exam", "        " + (String)choices.elementAt(x));
        }
        log.write("exam", "    solutions = ");
        for(int x=0; x<solutions.size(); x++)
        {
            log.write("exam", "        " + ((Character)solutions.elementAt(x)).toString());
        }
        if(empAnswers != null)
        {
            log.write("exam", "    empAnswers = ");
            for(int x=0; x<empAnswers.size(); x++)
            {
                log.write("exam", "        " + (Character)empAnswers.elementAt(x));
            }
            if(empCorrect != null)
            {
                log.write("exam", "    empCorrect = ");
                for(int x=0; x<empCorrect.size(); x++)
                {
                    log.write("exam", "        " + ((Boolean)empCorrect.elementAt(x)).toString());
                }
            }
        }
        log.write("exam", "--------------------------------");
    }
    public String displayForm()
    {
        //log.write("exam", "Matching.displayform()");
        String form = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"4\"><input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\"> Points Each</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t"
            + "<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td colspan=\"3\">\n\t\t\t"
            + "<input type=\"text\" size=\"" + Q_TEXT_FIELD_WIDTH
            + "\" name=\"" + DESCRIPTION_FIELD + "\">\n\t\t</td>\n\t</tr>";
        //log.write("exam", "MAX_NUM_ENTRIES = " + MAX_NUM_ENTRIES);
        for(int x=0; x < MAX_NUM_ENTRIES; x++)
        {
            form += "\t<tr>\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\" valign=\"top\">\n\t\t\t";
            form += "<input type=\"text\" size=\"1\" name=\"" + SOLUTION_FIELDS[x] + "\">";
            form += "\n\t\t</td>\n\t\t<td valign=\"TOP\" align=\"left\">";
            form += "\n\t\t\t<textarea class=\"matching\" name=\""+ QUESTION_FIELDS[x] + "\" size=\"50%\"></textarea>\n\t\t";
            form += "</td>\n\t\t<td align=\"right\" valign=\"top\">\n\t\t\t";
            form += LETTER[x] + ")\n\t\t</td>\n\t\t<td>\n" + "\t\t\t<textarea class=\"matching\" name=\"" + CHOICE_FIELDS[x] + "\"></textarea>";
            form += "\n\t\t</td>\n\t</tr>";
        }		
        form += "\n</table>\n";
        return form;
    }

    public String displayToMod()
    {
        //log.write("exam", "Matching.displayToMod()");
        String form = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"4\"><input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\""+ this.getPointValuePerAnswer() 
            + "\"> Points Each</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t"
            + "<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">" 
            + this.getQuestionNum() + ")</td>\n\t\t<td colspan=\"3\">\n"
            + "\t\t\t<textarea class=\"description\" name=\""
            + DESCRIPTION_FIELD + "\""
            + ">" + this.description + "</textarea>\n"
            + "\t\t</td>\n\t</tr>\n";
        /*
        log.write("exam", form);
        log.write("exam", "solutons.size = " + this.solutions.size());
        log.write("exam", "questions.size = " + this.questions.size());
        log.write("exam", "choices.size = " + this.choices.size());
         */
        for(int x=0; x < MAX_NUM_ENTRIES || x < MAX_NUM_ENTRIES; x++)
        {
            form += "\t<tr>\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\" valign=\"top\">\n";
            if(MAX_NUM_ENTRIES > x)
            {
                form += "\t\t\t<input type=\"text\" size=\"1\" name=\"" + SOLUTION_FIELDS[x] + "\"";
                if(this.solutions.size() > x)
                {
                    Character solution = (Character)(this.solutions.elementAt(x));
                    //String comment = replaceSubString(((Character)this.solutions.elementAt(x)), "<BR>", "\n");
                    //comment = replaceSubString(comment, "&nbsp;", " ");
                    //comment = replaceSubString(comment, "\"", "&quot;");
                    form += " value=\"" + solution + "\"";
                }
                form += ">\n";
            }
            //log.write("exam", "form = " + form);
            form += "\t\t</td>\n\t\t<td valign=\"TOP\" align=\"left\">\n";
            if(MAX_NUM_ENTRIES > x)
            {
                form += "\t\t\t<textarea class=\"matching\" name=\""+ QUESTION_FIELDS[x] + "\" size=\"50%\">";
                if(this.questions.size() > x)
                {
                    String comment = replaceSubString(((String)this.questions.elementAt(x)), "<BR>", "\n");
                    //comment = replaceSubString(comment, "&nbsp;", " ");
                    comment = replaceSubString(comment, "\"", "&quot;");
                    form += comment;
                }
                form += "</textarea>\n";
            }
            form += "\t\t</td>\n\t\t<td align=\"right\" valign=\"top\">";
            if(MAX_NUM_ENTRIES > x)
            {
                form += LETTER[x] + ")</td>\n\t\t<td>\n"
                + "\t\t\t<textarea class=\"matching\" name=\"" + CHOICE_FIELDS[x] + "\">";
                if(this.choices.size() > x)
                {
                    String comment = replaceSubString(((String)this.choices.elementAt(x)), "<BR>", "\n");
                    //comment = replaceSubString(comment, "&nbsp;", " ");
                    comment = replaceSubString(comment, "\"", "&quot;");
                    form += comment;
                }
                form += "</textarea>\n";
            }
            form += "\t\t</td>\n\t</tr>";
        }
        form += "\n</table>\n";
        return form;
    }

    public String displayToGrade() {
        //Secretary.startFxn("exam", "MatchingVO.displayToGrade()");
        this.logValues();
        String display;
        int numQs = this.questions.size();
        int numCs = this.choices.size();
        String descript = replaceSubString(this.description, "<BR>", "\n");
        //description = replaceSubString(description, "&nbsp;", " ");
        descript = replaceSubString(descript, "\"", "&quot;");
        display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"5\">\n\t\t\tPoint Value: <b>" + this.getPointValuePerAnswer()
            + "</b> points each\n\t\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t"
            + "<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td width=" + QUESTION_TD_WIDTH + " colspan=\"3\">\n\t\t\t"
            + descript + "\n\t\t</td>\n\t</tr>\n";
        //Secretary.write("exam", "numQs = " + numQs);
        //Secretary.write("exam", "numCs = " + numCs);
        for(int x=0; x < numQs || x < numCs; x++)
        {
            Secretary.write("exam", "x = " + x);
            display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t";
            if(numQs > x)
            {
                if(((Boolean)(empCorrect.elementAt(x))).booleanValue())
                    display += "<IMG SRC=\"./images/check_mark.gif\">";
                else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            }
            display += "\n\t\t</td>\n\t\t<td width=\"" + RADIO_TD_WIDTH 
                + "\" valign=\"top\">\n\t\t\t";
            // Print solution box
            if(numQs > x)
            {
                display += "<B><U> " + (Character)empAnswers.elementAt(x) + " </U></B>";
            }
            int entryWidth = ((QUESTION_TD_WIDTH - Q_NUM_TD_WIDTH) / 2);
            display += "\n\t\t</td>\n\t\t<td valign=\"top\" align=\"left\" width=\"" + (entryWidth - 20) + "\">\n\t\t\t";
            // Print question
            if(numQs > x)
            {
                display += (String)questions.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t\t<td width=\"" + (entryWidth + 20) + "\">\n\t\t\t";
            // Print choice
            if(numCs > x)
            {
                display += LETTER[x] + ") " + (String)choices.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t</tr>\n";
        }	
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        comment = replaceSubString(comment, "\"", "&quot;");
        display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\""
            + " value=\"" + this.getPointValueEarnedTotal() + "\""
            + " onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\")>\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\" colspan=\"3\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"3\">\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>\n"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        //Secretary.endFxn("exam", "MatchingVO.displayToGrade()");
        return display;
    }
    
    public String displayGraded()
    {
        //log.write("exam", "MatchingVO.displayGraded()");
        String display;
        int numQs = this.questions.size();
        int numCs = this.choices.size();
        display = "<TABLE class=entry>\n\t<tr>\n\t\t"
            + "<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td width=" + QUESTION_TD_WIDTH + " colspan=\"3\">\n\t\t\t"
            + this.description + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < numQs || x < numCs; x++)
        {
            display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t";
            if(questions.size() > x)
            {
                if(((Boolean)(empCorrect.elementAt(x))).booleanValue())
                    display += "<IMG SRC=\"./images/check_mark.gif\">";
                else
                    display += "<IMG SRC=\"./images/x_mark.gif\">";
            }
            display += "\n\t\t</td>\n\t\t<td width=\"" + RADIO_TD_WIDTH 
                + "\" valign=\"top\">\n\t\t\t";
            // Print solution box
            if(numQs > x)
            {
                display += "<B><U> " + (Character)empAnswers.elementAt(x) + " </U></B>";
            }
            int entryWidth = ((QUESTION_TD_WIDTH - Q_NUM_TD_WIDTH) / 2);
            display += "\n\t\t</td>\n\t\t<td valign=\"top\" align=\"left\" width=\"" + (entryWidth - 20) + "\">\n\t\t\t";
            // Print question
            if(numQs > x)
            {
                display += (String)questions.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t\t<td width=\"" + (entryWidth + 20) + "\">\n\t\t\t";
            // Print choice
            if(numCs > x)
            {
                display += LETTER[x] + ") " + (String)choices.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t</tr>\n";
        }	
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"3\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"4\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n";
        }
        display += "\n</table>\n";
        return display;
    }

    public String displayToTake()
    {
        //log.write("exam", "MatchingVO.displayToTake()");
        String display;
        int numQs = this.questions.size();
        int numCs = this.choices.size();
        display = "<TABLE class=entry>\n\t<tr>\n\t\t"
            + "<td width=\"" + Q_NUM_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td width=" + QUESTION_TD_WIDTH + " colspan=\"3\">\n\t\t\t"
            + this.description + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < numQs || x < numCs; x++)
        {
            display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n\t\t<td width=\"" + RADIO_TD_WIDTH 
                + "\" valign=\"top\">\n\t\t\t";
            // Print solution box
            if(numQs > x)
            {
                display += "<input type=\"text\" class=\"disableAutoComplete\" size=\"1\" name=\"" 
                    + this.getEntryTypeCode() + "_" + this.getQuestionNum() + "_"
                    + SOLUTION_FIELDS[x] + "\"";
                if((this.empAnswers != null) && (this.empAnswers.elementAt(x) != null))
                    display += "value=\"" + (Character)empAnswers.elementAt(x) + "\"";
                display += ">";
            }
            int entryWidth = ((QUESTION_TD_WIDTH - Q_NUM_TD_WIDTH) / 2);
            display += "\n\t\t</td>\n\t\t<td valign=\"top\" align=\"left\" width=\"" + (entryWidth - 20) + "\">\n\t\t\t";
            // Print question
            if(numQs > x)
            {
                display += (String)questions.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t\t<td width=\"" + (entryWidth + 20) + "\">\n\t\t\t";
            // Print choice
            if(numCs > x)
            {
                display += LETTER[x] + ") " + (String)choices.elementAt(x);
            }
            else display += "&nbsp;";
            display += "\n\t\t</td>\n\t</tr>\n";
        }	
        display += "</table>\n";
        display += "\t<input type=\"hidden\" name=\"" + "num_choices_" 
            + this.getQuestionNum() + "\" value=\"" + choices.size() + "\">";
        return display;
    }

    public String displayToView()
    {
        //log.write("exam", "MatchingVO.displayToView()");
        String display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"3\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t"
            + "<td width=\"" + RADIO_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td colspan=\"2\""
            + " width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t"
            + this.description + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < questions.size() || x < choices.size(); x++)
        {
            display += "\t<tr>\n\t\t"
                + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n\t\t" 
                + "<td align=\"left\" width=\"" + (QUESTION_TD_WIDTH/2.5) + "\">"
                + "\n\t\t\t";
            // Print Solution
            if(solutions.size() > x)
            {
                display += "<B><U> " + (Character)solutions.elementAt(x) + " </U></B>";
            }
            display += "&nbsp ";
            // Print question
            if(questions.size() > x)
            {
                display += (String)questions.elementAt(x);
            }
            display += "\n\t\t</td>\n\t\t<td align=\"left\" width=\"" + (QUESTION_TD_WIDTH/2.5) + "\">\n\t\t\t";
            // Print choice
            if(choices.size() > x)
            {
                display += LETTER[x] + ") "
                    + (String)choices.elementAt(x);
            }
            else display += "&nbsp";
            display += "\n\t\t</td>\n\t</tr>\n";
        }//END for(int x=0; x < questions.size() || x < choices.size(); x++)
        display += "</table>\n";
        return display;
    }
    public String displayToSelect()
    {
        //log.write("exam", "MatchingVO.displayToView()");
        String display = "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n\t\t<td colspan=\"3\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t"
            + "<td width=\"" + RADIO_TD_WIDTH + "\" valign=\"top\">\n\t\t\t" 
            + this.getQuestionNum() + ")\n\t\t</td>\n\t\t<td colspan=\"2\""
            + " width=\"" + QUESTION_TD_WIDTH + "\">\n\t\t\t"
            + this.description + "\n\t\t</td>\n\t</tr>\n";
        for(int x=0; x < questions.size() || x < choices.size(); x++)
        {
            display += "\t<tr>\n\t\t"
                + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n\t\t" 
                + "<td align=\"left\" width=\"" + (QUESTION_TD_WIDTH/2.5) + "\">"
                + "\n\t\t\t";
            // Print Solution
            if(solutions.size() > x)
            {
                display += "<B><U> " + (Character)solutions.elementAt(x) + " </U></B>";
            }
            display += "&nbsp ";
            // Print question
            if(questions.size() > x)
            {
                display += (String)questions.elementAt(x);
            }
            display += "\n\t\t</td>\n\t\t<td align=\"left\" width=\"" + (QUESTION_TD_WIDTH/2.5) + "\">\n\t\t\t";
            // Print choice
            if(choices.size() > x)
            {
                display += LETTER[x] + ") "
                    + (String)choices.elementAt(x);
            }
            else display += "&nbsp";
            display += "\n\t\t</td>\n\t</tr>\n";
        }//END for(int x=0; x < questions.size() || x < choices.size(); x++)
        display += "</table>\n";
        return display;
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "MatchingVO.processCreateform(request, " + time + ")");
        this.setInsertTime(time);
        this.questions = new Vector();
        this.solutions = new Vector();
        this.choices = new Vector();
        //java.util.Enumeration e = request.getParameterNames();
        this.setDescription((String)request.getParameter(DESCRIPTION_FIELD));
        String toAdd = "";
        String question="", solution="", choice="";
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter(CHOICE_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.choices.add(toAdd);
            }
        }
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter(QUESTION_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.questions.add(toAdd);
            }
            toAdd = (String)request.getParameter(SOLUTION_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                char answerChar = (toAdd.toUpperCase().toCharArray())[0];
                int arrLoc = Arrays.binarySearch(LETTER, answerChar);
                if(choices.size() >= arrLoc)
                {
                    this.solutions.add(new Character(answerChar));
                }
            }
        }
        this.setNumSolutions(this.solutions.size());
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "MatchingVO.processCreateform(request, " + time + ")");
    }

    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "MatchingVO.processModform(request)");
        this.questions = new Vector();
        this.solutions = new Vector();
        this.choices = new Vector();
        //java.util.Enumeration e = request.getParameterNames();
        this.setDescription((String)request.getParameter(DESCRIPTION_FIELD));
        String toAdd = "";
        String question="", solution="", choice="";
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter(CHOICE_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.choices.add(toAdd);
            }
        }
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter(QUESTION_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.questions.add(toAdd);
            }
            toAdd = (String)request.getParameter(SOLUTION_FIELDS[loc]);
            if(toAdd.compareTo("") != 0)
            {
                char answerChar = (toAdd.toUpperCase().toCharArray())[0];
                int arrLoc = Arrays.binarySearch(LETTER, answerChar);
                if(choices.size() >= arrLoc)
                {
                    this.solutions.add(new Character(answerChar));
                }
            }
        }
        this.setNumSolutions(this.solutions.size());
        Integer pointValueInt = new Integer(request.getParameter(this.POINT_VALUE_FIELD));
        this.setPointValuePerAnswer(pointValueInt.intValue());
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "MatchingVO.processModform(request)");
    }
    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "MatchingVO.processEmpAnswerForm()");
        this.empAnswers = new Vector();
        this.empCorrect = new Vector();
        String paramName, input;
        Character thisAnswer;
        Boolean thisCorrect;
        //empAnswers = new Vector(this.solutions.size());
        this.setPointValueEarnedTotal(0);
        for(int x=0; x < this.solutions.size(); x++)
        {
            paramName = this.getEntryTypeCode() + "_" + this.getQuestionNum() + "_" + SOLUTION_FIELDS[x];
            input = (String)(request.getParameter(paramName));
            thisAnswer = new Character(Character.toUpperCase(input.trim().charAt(0)));
            thisCorrect = new Boolean(thisAnswer.equals(this.solutions.elementAt(x)));
            this.addEmpAnswer(thisAnswer);
            this.addEmpCorrect(thisCorrect);
            /*
            if(thisCorrect.booleanValue()){
                Secretary.write("exam", "MatchingVO.pointValueEarnedTotal = " + this.getPointValueEarnedTotal());
                log.write("exam", "points earned = " + this.getPointValueEarnedTotal());
                this.setPointValueEarnedTotal(this.getPointValueEarnedTotal() + this.getPointValuePerAnswer());
                Secretary.write("exam", "MatchingVO.pointValueEarnedTotal = " + this.getPointValueEarnedTotal());
            }
             */
        }
        //Secretary.write("exam", "MatchingVO.pointValueEarnedTotal = " + this.getPointValueEarnedTotal());
        //Secretary.endFxn("exam", "MatchingVO.processEmpAnswerForm()");
    }

    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //log.write("exam", "MatchingVO.processGradingForm()");
        String paramName;
        Boolean thisCorrect;
        paramName = "comment_" + this.getExamLoc();
        String comment = replaceSubString(((String)(request.getParameter(paramName))), "\n", "<BR>");
        comment = replaceSubString(comment, " ", "&nbsp;");
        this.setGradersComment(comment);
        paramName = "points_earned_" + this.getExamLoc();
        Integer pointsEarned = new Integer((String)(request.getParameter(paramName)));
        this.setPointValueEarnedTotal(pointsEarned.intValue());        
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String newDescription)
    {
        description = newDescription;
    }

    public boolean choiceExists(String aChoice)
    {
        return this.choices.contains(aChoice);
    }

    public boolean questionExists(String aQuestion)
    {
        return this.questions.contains(aQuestion);
    }

    public void addChoice(int theLoc, String aChoice)
    {
        this.choices.add(theLoc, aChoice);
    }

    public void addQuestion(int qLoc, String aQuestion)
    {
        this.questions.add(qLoc, aQuestion);
    }

    public void addSolution(Character ans)
    {
        this.solutions.add(ans);
        this.setNumSolutions(this.getNumSolutions() + 1);
    }

    public String getQuestionAt(int vectorLoc)
    {
        return (String)(questions.elementAt(vectorLoc));
    }
    public String getChoiceAt(int vectorLoc)
    {
        return (String)(choices.elementAt(vectorLoc));
    }
    public Character getSolutionAt(int vectorLoc)
    {
        return (Character)(solutions.elementAt(vectorLoc));
    }
    public Character getEmpAnswerAt(int vectorLoc)
    {
        return (Character)(empAnswers.elementAt(vectorLoc));
    }
    
    public boolean getEmpCorrectAt(int vLoc)
    {
        return ((Boolean)(empCorrect.elementAt(vLoc))).booleanValue();
    }
    public void addEmpAnswer(Character answ)
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
        if(cor.booleanValue())
        { 
            this.setPointValueEarnedTotal(this.getPointValueEarnedTotal() + this.getPointValuePerAnswer());
        }
    }

    public int getNumQuestions(){
        return questions.size();
    }
    
    public int getNumChoices(){
        return choices.size();
    }

    public int getNumEmpAnswers()
    {
        return this.empAnswers.size();
    }
    
    public boolean getAnswerEntered()
    {
        Secretary.startFxn("exam", "MatchingVO.getAnswerEntered()");
        boolean toReturn = false;
        try
        {
            if(empAnswers.size() == solutions.size())
                toReturn = true;
        }catch(NullPointerException e)
        {
            Secretary.write("exam", "NullPointerException in MatchingVO.getAnswerEntered => " + e.toString());
        }
        Secretary.endFxn("exam", "MatchingVO.getAnswerEntered() => " + toReturn);
        return toReturn;
    }
    
    public MatchingVO()
    {
        //this.setEntryTypeCode("ma");
        String tempName;
        this.questions = new Vector();
        this.choices = new Vector();
        this.solutions = new Vector();
        //this.empAnswers = new Vector();
        for(int x=0; x<MAX_NUM_ENTRIES; x++){
            tempName = "solution_" + LETTER[x];
            java.lang.reflect.Array.set(SOLUTION_FIELDS, x, tempName);
            tempName = "question_" + LETTER[x];
            java.lang.reflect.Array.set(QUESTION_FIELDS, x, tempName);
            tempName = "choice_" + LETTER[x];
            java.lang.reflect.Array.set(CHOICE_FIELDS, x, tempName);
        }
    }

}