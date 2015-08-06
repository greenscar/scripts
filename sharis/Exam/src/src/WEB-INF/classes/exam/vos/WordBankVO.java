/***********************************************************************
* Module:  WordBankVO.java
* Author:  jsandlin
* Purpose: Defines the Class WordBankVO
***********************************************************************/
package exam.vos;
import java.util.*;
import logging.Secretary;
public class WordBankVO extends QuestionVO
{
    private static String DESCRIPTION_FIELD = "WordBankDescription";
    private static String[] QUESTION_FIELDS = new String[MAX_NUM_ENTRIES];
    private static String[] SOLUTION_FIELDS = new String[MAX_NUM_ENTRIES];
    private static String[] CHOICE_FIELDS = new String[MAX_NUM_ENTRIES];
    private static int NUM_COLS_PER_ROW = 4;
    private String description = "";
    private Vector questions;
    private Vector choices;
    private Vector solutions;
    private Vector empAnswers;
    private Vector empCorrect;

    public void logValues(){
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
            log.write("exam", "        " + (Character)solutions.elementAt(x));
        }
        if((empAnswers != null) && (empAnswers.size() > 0))
        {
            log.write("exam", "    empAnswers = ");
            for(int x=0; x<empAnswers.size(); x++)
            {
                log.write("exam", "        " + (Character)empAnswers.elementAt(x));
            }
            log.write("exam", "    empCorrect = ");
            for(int x=0; x<empCorrect.size(); x++)
            {
                log.write("exam", "        " + ((Boolean)empCorrect.elementAt(x)).toString());
            }
        }
        log.write("exam", "--------------------------------");
    }

    public String displayForm()
    {
        //log.write("exam", "WordBank.displayform()");
        String echo = "<TABLE class=entry>\n\t<tr>\n\t\t<td>\n"
            + "\t\t\t<TABLE width=\"700\" BORDER=\"1\">\n\t\t\t<tr>\n"
            + "\t\t\t\t<td colspan=\"3\"><input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\"> points each</td>\n\t\t\t</tr>\n";
        //DISPLAY THE BOXES FOR THE CHOICES.
        //log.write("exam", "MAX_NUM_ENTRIES = " + MAX_NUM_ENTRIES);
        for(int nc=0; nc< MAX_NUM_ENTRIES; )
        {
            //log.write("exam", "nc = " + nc);
            echo += "\t\t\t<tr>\n";
            for(int x=0; x<3; x++, nc++)
            {
                if(nc < MAX_NUM_ENTRIES)
                {
                    echo += "\t\t\t\t<td width = 187>\n\t\t\t\t\t\t" + LETTER[nc]
                    + ")&nbsp<input type=\"TEXT\" size=\"" + BLANK_WIDTH + "\" name=\""
                    + CHOICE_FIELDS[nc] + "\">\n\t\t\t\t</td>\n";
                }
                else
                {
                    echo += "\t\t\t\t<td width = 187>&nbsp</td>\n";
                }
            }
            echo += "\t\t\t</tr>\n";
        }
        echo += "\t\t\t</table>\n\t\t</td>\n\t</tr>\n"
        //DISPLAY THE QUESTION BLANK
            + "\t<tr>\n"
            + "\t\t<td colspan=\"3\">" + this.getQuestionNum()
            + ") <input type=\"TEXT\" size=\"" + Q_TEXT_FIELD_WIDTH + "\" name=\"" 
            + DESCRIPTION_FIELD + "\"></td>\n\t</tr>\n"
            //DISPLAY THE BOXES FOR THE QUESTIONS
            + "\t<tr>\n\t\t<td>\n"
            + "\t\t\t<TABLE width=\"700\" BORDER=\"1\">\n";
        for(int nb=0;nb<MAX_NUM_ENTRIES;)
        {
            echo += "\t\t\t<tr>\n";
            for(int x=0;x<1; x++, nb++)
            {
                if(nb < MAX_NUM_ENTRIES)
                {
                    echo += "\t\t\t\t<td>\n"
                        + "\t\t\t\t\t\t<input type=\"TEXT\" size=\"" + RADIO_TD_WIDTH + "\" name=\"" 
                        + SOLUTION_FIELDS[nb] + "\">\n"
                        + "\t\t\t\t\t\t<input type=\"TEXT\" size=\"" + Q_TEXT_FIELD_WIDTH + "\" name=\"" 
                        + QUESTION_FIELDS[nb] + "\">\n"
                        + "\t\t\t\t</td>\n";
                }
                else
                    echo += "\t\t\t\t<td>&nbsp</td>\n";
            }
            echo += "\t\t\t</tr>\n";
        }
        echo += "\t\t\t</table>\n\t\t</td>\n\t</tr>\n</table>\n";
        return echo;
    }

    public String displayToMod()
    {
        //log.write("exam", "WordBank.displayToMod()");
        String echo = "<TABLE class=entry>\n\t<tr>\n"
            + "\t\t<td colspan=\"3\">\n\t\t\t<input type=\"text\" size=\"2\" name=\"" 
            + this.POINT_VALUE_FIELD + "\" value=\"" + this.getPointValuePerAnswer() + "\"> points each\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>\n"
            + "\t\t\t<table width=\"" + TABLE_WIDTH + "\" BORDER=\"1\">\n";
        //DISPLAY THE BOXES FOR THE CHOICES.
        for(int nc=0; nc< MAX_NUM_ENTRIES; )
        {
            echo += "\t\t\t\t<tr>\n";
            for(int x=0; x<3; x++, nc++)
            {
                if(nc < MAX_NUM_ENTRIES)
                {
                    echo += "\t\t\t\t\t<td width = " + (TABLE_WIDTH/3) + ">\n\t\t\t\t\t\t" 
                        + LETTER[nc] + ")&nbsp\n\t\t\t\t\t\t<input type=\"TEXT\" size=\"" 
                        + BLANK_WIDTH + "\" name=\"" + CHOICE_FIELDS[nc] + "\"";
                    if(this.choices.size() > nc)
                    {
                        echo += " value=\"" + this.choices.elementAt(nc) + "\"";
                    }
                    echo += ">\n\t\t\t\t\t</td>\n";
                }
                else echo += "\t\t\t\t\t<td width = " + (TABLE_WIDTH/3) + ">&nbsp</td>\n";
            }
            echo += "\t\t\t\t</tr>\n";
        }
        echo += "\t\t\t</table>\n"
            + "\t\t</td>\n\t</tr>\n"
            //DISPLAY THE QUESTION BLANK
            + "\t<tr>\n"
            + "\t\t<td colspan=\"3\">\n\t\t\t" + this.getQuestionNum() 
            + ")&nbsp\n\t\t\t<textarea class=\"description\" name=\"" 
            + DESCRIPTION_FIELD + "\">" + this.description + "</textarea>\n\t\t</td>\n\t</tr>\n"
            //DISPLAY THE BOXES FOR THE QUESTIONS
            + "\t<tr>\n\t\t<td>\n"
            + "\t\t\t<table width=\"" + TABLE_WIDTH + "\" BORDER=\"1\">\n";
        int nb = 0;
        while(nb<MAX_NUM_ENTRIES)
        {
            echo += "\t\t\t\t<tr>\n";
            for(int x=0;x<1; x++, nb++)
            {
                if(nb < MAX_NUM_ENTRIES)
                {
                    echo += "\t\t\t\t\t<td>\n"
                        + "\t\t\t\t\t\t<input type=\"TEXT\" size=\"" + RADIO_TD_WIDTH + "\" name=\"" 
                        + SOLUTION_FIELDS[nb] + "\"";
                    if(this.solutions.size() > nb)
                    {
                        echo += " value=\"" + this.solutions.elementAt(nb) + "\"";
                    }
                    echo += ">\n"
                        + "\t\t\t\t\t\t<input type=\"TEXT\" size=\"" + Q_TEXT_FIELD_WIDTH + "\" name=\"" 
                        + QUESTION_FIELDS[nb] + "\"";
                    if(this.questions.size() > nb)
                    {
                        echo += " value=\"" + this.questions.elementAt(nb) + "\"";
                    }
                    echo += ">\n\t\t\t"
                        + "\t\t</td>\n";
                }
                else 
                {
                    echo += "\t\t<td>&nbsp</td>\n";
                }
            } //END for(int x=0;x<1; x++, nb++)
            echo += "\t\t\t\t</tr>\n";
        } //END while(nb<MAX_NUM_ENTRIES)
        echo += "\t\t\t</table>\n\t\t</td>\n\t</tr>\n</table>\n";
        return echo;
    }

    public String displayToGrade() {
        //log.write("exam", "WordBank.displayToGrade()");
        String display = "";
        //DISPLAY THE CHOICES
        int colspan = 0;
        if(choices.size() > 2) colspan = 4;
        else if (choices.size() > 1) colspan = 3;
        else colspan = 2;
        display = "<TABLE class=entry>\n\t<tr>\n\t\t<td colspan=\"" + colspan + "\">\n\t\t\tPoint Value: <b>"
            + this.getPointValuePerAnswer() + "</b> points each\n\t\t<input type=\"hidden\" name=\"points_possible_" + this.getExamLoc() + "\" value=\""+ this.getPointValueTotal() +"\">\n\t\t"
            + "</td>\n\t</tr>\n";
        for(int nc=0;nc<choices.size();)
        {
            display +="\t<tr>\n\t\t"
                + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            for(int x=0;x<3; x++, nc++)
            {
                if(nc < choices.size())
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp" + LETTER[nc] 
                        + ")" + (String)choices.elementAt(nc) + "\n\t\t</td>\n";
                else
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            }
            display += "\t</tr>\n";
        }
        //log.write("exam", "choices done");
        display += "\t<tr>"
            + "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum()+ ")\n\t\t</td>"
            + "\n\t\t<td colspan=\"" + (colspan - 1) + "\">\n\t\t\t" + this.description + "\n\t\t</td>\n"
            + "\t</tr>\n";
            //DISPLAY THE QUESTIONS
        //log.write("exam", "description done");
        for(int nb=0;nb<questions.size();nb++)
        {
            display += "\t<tr>\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t";
            if(((Boolean)(empCorrect.elementAt(nb))).booleanValue())
                display += "<IMG SRC=\"./images/check_mark.gif\">";
            else
                display += "<IMG SRC=\"./images/x_mark.gif\">";
            display += "\n\t\t</td>" 
                + "\n\t\t<td colspan=\"" + (colspan - 1) + "\">\n\t\t\t<B>"
                + "<U>&nbsp"
                + (Character)empAnswers.elementAt(nb)
                + "&nbsp</U></B>&nbsp"
                + (String)questions.elementAt(nb)
                + "\n\t\t</td>\n\t</tr>\n";
        }    
        String comment = replaceSubString(this.getGradersComment(), "<BR>", "\n");
        comment = replaceSubString(comment, "&nbsp;", " ");
        display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t<input type=\"text\" size=\"1\" name=\"points_earned_" + this.getExamLoc() + "\""
            + " value=\"" + this.getPointValueEarnedTotal() + "\""
            + " onBlur=\"checkGradingThisField(this.form, 'points_earned_" + this.getExamLoc() + "')\">\n\t\t</td>"
            + "\n\t\t<td width=\"" + QUESTION_TD_WIDTH + "\" colspan=\"" + (colspan - 1) + "\">\n\t\t\tpoints earned.\n\t\t</td>"
            + "\n\t\t</td>\n\t</tr>\n"
            + "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"" + (colspan - 1) + "\">\n\t\t\tCOMMENTS:<br>\n\t\t\t"
            + "<textarea class=\"essay\" name=\"comment_" + this.getExamLoc() + "\">" 
            + comment + "</textarea>\n\t\t"
            + "</td>\n\t</tr>\n\t</table>\n"
            + "<input type=\"hidden\" name=\"qNum_" + this.getExamLoc() + "\" value=\""+ this.getQuestionNum()+"\">";
        //log.write("exam", "questions done");
        return display;
    }
    
    public String displayGraded()
    {
        //log.write("exam", "WordBank.displayGraded()");
        String display = "<TABLE class=entry>\n";
        //DISPLAY THE CHOICES
        for(int nc=0;nc<choices.size();)
        {
            display +="\t<tr>\n\t\t"
                + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            for(int x=0;x<3; x++, nc++)
            {
                if(nc < choices.size())
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp" + LETTER[nc] 
                        + ")" + (String)choices.elementAt(nc) + "\n\t\t</td>\n";
                else
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            }
            display += "\t</tr>\n";
        }
        //log.write("exam", "choices done");
        display += "\t<tr>"
            + "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum()+ ")\n\t\t</td>"
            + "\n\t\t<td colspan=\"3\">\n\t\t\t" + this.description + "\n\t\t</td>\n"
            + "\t</tr>\n";
            //DISPLAY THE QUESTIONS
        //log.write("exam", "description done");
        for(int nb=0;nb<questions.size();nb++)
        {
            display += "\t<tr>\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t";
            if(((Boolean)(empCorrect.elementAt(nb))).booleanValue())
                display += "<IMG SRC=\"./images/check_mark.gif\">";
            else
                display += "<IMG SRC=\"./images/x_mark.gif\">";
            display += "\n\t\t</td>" 
                + "\n\t\t<td colspan=\"3\">\n\t\t\t<B>"
                + "<U>&nbsp"
                + (Character)empAnswers.elementAt(nb)
                + "&nbsp</U></B>&nbsp"
                + (String)questions.elementAt(nb)
                + "\n\t\t</td>\n\t</tr>\n";
        }    
        if((this.getGradersComment()).compareTo("") != 0){
            //display += "\t<tr>\n\t\t<td width=\"" + Q_NUM_TD_WIDTH + "\">\n\t\t\t&nbsp;\n\t\t</td>\n\t\t<td colspan=\"3\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            display += "\t<tr>\n\t\t<td colspan=\"4\" class=\"comment\">\n\t\t\t<b>COMMENTS:</b>\n\t\t\t<u>"
            + this.getGradersComment() + "</u>\n\t\t"
            + "</td>\n\t</tr>\n\t";
        }
        display += "</table>\n";
        //log.write("exam", "questions done");
        return display;
    }

    public String displayToTake()
    {
        //log.write("exam", "WordBank.displayToTake()");
        String display = "<TABLE class=entry>\n";
        //DISPLAY THE CHOICES
        for(int nc=0;nc<choices.size();)
        {
            display +="\t<tr>\n"
            + "\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            for(int x=0;x<3; x++, nc++)
            {
                if(nc < choices.size())
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp" + LETTER[nc] 
                        + ")" + (String)choices.elementAt(nc) + "\n\t\t</td>\n";
                else
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            }
            display += "\t</tr>\n";
        }
        //log.write("exam", "choices done");
        display += "\t<tr>"
            + "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum()+ ")\n\t\t</td>"
            + "\n\t\t<td colspan=\"3\">\n\t\t\t" + this.description + "\n\t\t</td>\n"
            + "\t</tr>\n"
            //DISPLAY THE QUESTIONS
            + "\t<tr>";
        //log.write("exam", "description done");
        for(int nb=0;nb<questions.size();nb++)
        {
            display += "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>" 
                + "\n\t\t<td colspan=\"3\">\n\t\t\t"
                + "<input type=\"TEXT\" class=\"disableAutoComplete\" size=\"1\" name=\"" 
                + this.getEntryTypeCode() + "_" + this.getQuestionNum() + "_"
                + SOLUTION_FIELDS[nb] + "\"";
            if((empAnswers != null) && (empAnswers.elementAt(nb) != null))
                    display += "value=\"" + (Character)empAnswers.elementAt(nb) + "\"";
            display += ">&nbsp"
                + (String)questions.elementAt(nb)
                + "\n\t\t</td>\n\t</tr>\n";
        }    
        display += "</table>\n";
        //log.write("exam", "questions done");
        display += "\t<input type=\"hidden\" name=\"" + "num_choices_" 
            + this.getQuestionNum() + "\" value=\"" + choices.size() + "\">";
        return display;
    }
    public String displayToView()
    {
        //log.write("exam", "WordBank.displayToView()");
        String display = "<TABLE class=entry>\n";
        //DISPLAY THE CHOICES
        display += "\t<tr>\n\t\t<td colspan=\"4\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t";
        for(int nc=0;nc<choices.size();)
        {
            display +="<tr>\n\t\t"
            + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            for(int x=0;x<3; x++, nc++)
            {
                if(nc < choices.size())
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp" + LETTER[nc] 
                        + ")" + (String)choices.elementAt(nc) + "\n\t\t</td>\n";
                else
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            }
            display += "\t</tr>\n";
        }
        //log.write("exam", "choices done");
        display += "\t<tr>"
            + "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum()+ ")\n\t\t</td>"
            + "\n\t\t<td colspan=\"3\">\n\t\t\t" + this.description + "\n\t\t</td>\n"
            + "\t</tr>\n"
            //DISPLAY THE QUESTIONS
            + "\t<tr>";
        //log.write("exam", "description done");
        for(int nb=0;nb<questions.size();nb++)
        {
            display += "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>" 
                + "\n\t\t<td colspan=\"3\">\n\t\t\t<B>"
                + "<U>&nbsp"
                + (Character)solutions.elementAt(nb)
                + "&nbsp</U></B>&nbsp"
                + (String)questions.elementAt(nb)
                + "\n\t\t</td>\n\t</tr>\n";
        }    
        //log.write("exam", "questions done");
        //display += "\t\t</td>\n\t</tr>\n";
        display += "</table>\n";
        return display;
    }
    public String displayToSelect()
    {
        //log.write("exam", "WordBank.displayToView()");
        String display = "<TABLE class=entry onMouseOver=\"this.className='entry_red_bold'\" "
            + "onMouseOut=\"this.className='entry_black'\" "
            + "onclick=\"submit();\">\n";
        //DISPLAY THE CHOICES
        display += "\t<tr>\n\t\t<td colspan=\"4\">\n\t\t\t" 
            + "Point Value: <b>" 
            + this.getPointValueTotal() + "</b> (" + this.getPointValuePerAnswer()
            + " points each)\n\t\t</td>\n\t</tr>\n\t";
        for(int nc=0;nc<choices.size();)
        {
            display +="<tr>\n\t\t"
            + "<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            for(int x=0;x<3; x++, nc++)
            {
                if(nc < choices.size())
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp" + LETTER[nc] 
                        + ")" + (String)choices.elementAt(nc) + "\n\t\t</td>\n";
                else
                    display += "\t\t<td width=\"" + (QUESTION_TD_WIDTH / 3) + "\">\n\t\t\t&nbsp\n\t\t</td>\n";
            }
            display += "\t</tr>\n";
        }
        //log.write("exam", "choices done");
        display += "\t<tr>"
            + "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t" + this.getQuestionNum()+ ")\n\t\t</td>"
            + "\n\t\t<td colspan=\"3\">\n\t\t\t" + this.description + "\n\t\t</td>\n"
            + "\t</tr>\n"
            //DISPLAY THE QUESTIONS
            + "\t<tr>";
        //log.write("exam", "description done");
        for(int nb=0;nb<questions.size();nb++)
        {
            display += "\n\t\t<td width=\"" + RADIO_TD_WIDTH + "\">\n\t\t\t&nbsp\n\t\t</td>" 
                + "\n\t\t<td colspan=\"3\">\n\t\t\t<B>"
                + "<U>&nbsp"
                + (Character)solutions.elementAt(nb)
                + "&nbsp</U></B>&nbsp"
                + (String)questions.elementAt(nb)
                + "\n\t\t</td>\n\t</tr>\n";
        }    
        //log.write("exam", "questions done");
        //display += "\t\t</td>\n\t</tr>\n";
        display += "</table>\n";
        return display;
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "WordBankVO.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        this.questions = new Vector();
        this.choices = new Vector();
        this.solutions = new Vector();
        java.util.Enumeration e = request.getParameterNames();
        this.description = (String)request.getParameter(DESCRIPTION_FIELD);
        String toAdd = "";
        String question="", solution="", choice="";
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter("choice_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.choices.add(toAdd);
            }
        }
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter("question_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.questions.add(toAdd);
            }
            toAdd = (String)request.getParameter("solution_" + LETTER[loc]);
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
        //Secretary.endFxn("exam", "WordBankVO.processCreateForm(request, " + time + ")");
    }
    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "WordBankVO.processModForm(request)");
        this.questions = new Vector();
        this.choices = new Vector();
        this.solutions = new Vector();
        java.util.Enumeration e = request.getParameterNames();
        this.description = (String)request.getParameter(DESCRIPTION_FIELD);
        String toAdd = "";
        String question="", solution="", choice="";
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter("choice_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.choices.add(toAdd);
            }
        }
        for(int loc = 0; loc < MAX_NUM_ENTRIES; loc++)
        {
            toAdd = (String)request.getParameter("question_" + LETTER[loc]);
            if(toAdd.compareTo("") != 0)
            {
                this.questions.add(toAdd);
            }
            toAdd = (String)request.getParameter("solution_" + LETTER[loc]);
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
        //Secretary.endFxn("exam", "WordBankVO.processModForm(request)");
    }
    public void processEmpAnswerForm(javax.servlet.http.HttpServletRequest request)
    {
        //log.write("exam", "WordBank.processEmpAnswerForm()");
        this.empAnswers = new Vector();
        this.empCorrect = new Vector();
        String paramName, input;
        Character thisAnswer;
        Boolean thisCorrect;
        empAnswers = new Vector(this.solutions.size());
        for(int x=0; x < this.solutions.size(); x++)
        {
            paramName = this.getEntryTypeCode() + "_" + this.getQuestionNum() 
                        + "_" + SOLUTION_FIELDS[x];
            input = (String)(request.getParameter(paramName));
            input = input.trim();
            char answ = input.charAt(0);
            thisAnswer = new Character(Character.toUpperCase(answ));
            thisCorrect = new Boolean(thisAnswer.equals(this.solutions.elementAt(x)));
            empAnswers.add(thisAnswer);
            this.empCorrect.add(thisCorrect);
            if(thisCorrect.booleanValue()){
                this.setPointValueEarnedTotal(this.getPointValueEarnedTotal() + this.getPointValuePerAnswer());
            }
        }
    }

    public void processGradingForm(javax.servlet.http.HttpServletRequest request) {
        //log.write("exam", "WordBankVO.processGradingForm()");
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
    
    public void addEmpAnswer(Character answ)
    {
        if(this.empAnswers == null) this.empAnswers = new Vector();
        this.empAnswers.add(answ);
    }
    public void addEmpCorrect(Boolean cor)
    {
        if(this.empCorrect == null) this.empCorrect = new Vector();
        this.empCorrect.add(cor);
        if(cor.booleanValue())
        { 
            this.setPointValueEarnedTotal(this.getPointValueEarnedTotal() + this.getPointValuePerAnswer());
        }
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
    }

    public int getEmpAnswersSize()
    {
        return this.empAnswers.size();
    }

    public Character getEmpAnswerAt(int vectorLoc)
    {
        return (Character)(empAnswers.elementAt(vectorLoc));
    }

    public boolean getEmpCorrectAt(int vLoc)
    {
        return ((Boolean)(empCorrect.elementAt(vLoc))).booleanValue();
    }
    public Character getSolutionAt(int vectorLoc)
    {
        return (Character)(solutions.elementAt(vectorLoc));
    }
    public String getQuestionAt(int vectorLoc)
    {
        return (String)(questions.elementAt(vectorLoc));
    }
    public String getChoiceAt(int vectorLoc)
    {
        return (String)(choices.elementAt(vectorLoc));
    }
    public int getNumQuestions()
    {
        return questions.size();
    }
    public int getNumChoices()
    {
        return choices.size();
    }
    
    public boolean getAnswerEntered()
    {
        Secretary.startFxn("exam", "WordBankVO.getAnswerEntered()");
        boolean toReturn = false;
        try
        {
            if(empAnswers.size() == solutions.size())
                toReturn = true;
        }catch(NullPointerException e)
        {
            Secretary.write("exam", "NullPointerException in WordBankVO.getAnswerEntered => " + e.toString());
        }
        Secretary.endFxn("exam", "WordBankVO.getAnswerEntered() => " + toReturn);
        return toReturn;
    }
    
    public WordBankVO()
    {
        //this.setEntryTypeCode("wb");
        String name;
        this.questions = new Vector();
        this.choices = new Vector();
        this.solutions = new Vector();
        //this.empAnswers = new Vector();
        for(int x=0; x<MAX_NUM_ENTRIES; x++){
            name = "solution_" + LETTER[x];
            java.lang.reflect.Array.set(SOLUTION_FIELDS, x, name);
            name = "question_" + LETTER[x];
            java.lang.reflect.Array.set(QUESTION_FIELDS, x, name);
            name = "choice_" + LETTER[x];
            java.lang.reflect.Array.set(CHOICE_FIELDS, x, name);
        }
    }

}