/***********************************************************************
* Module:  PageBreakVO.java
* Author:  jsandlin
* Purpose: A PageBreak is an entry type when an exam is "saveable"
*           This entry will place continue buttons at the bottom of the
*           screen and not display any other entries.
***********************************************************************/
package exam.vos;

public class PageBreakVO extends ExamEntryVO
{
    public static String FIELD_NAME = "PageBreak";

    public void logValues() {
        super.logValues();
        log.write("exam", "--------------------------------");
    }

    public String displayForm()
    {
        return this.displayToView();
    }

    public String displayGraded()
    {
        //log.write("exam", "PageBreakVO.displayGraded()");
        return "";
    }
    public String displayToGrade()
    {
        return this.displayToView();
    }
    public String displayToTake()
    {
        return this.displayToTake(false, false);
    }
    public String displayToSelect()
    {
        //log.write("exam", "PageBreakVO.displayForm()");
        String toReturn = "<table class=sectionHeader onMouseOver=\"this.className='red_bold'\" "
            + "onMouseOut=\"this.className='black'\" "
            + "onclick=\"submit();\">\n";
        toReturn += "\t<tr>\n\t\t<td>\n";
        toReturn += "<input type=\"submit\" value=\"<< Save / First\" name=\"goTo\" onclick=\"return(false);\">"
                 +  "<input type=\"submit\" value=\"< Save / Prev\" name=\"goTo\" onclick=\"return(false);\">";
        toReturn += "<input type=\"submit\" value=\"Save / Next>\" name=\"goTo\" onclick=\"return(false);\">"
                 +  "<input type=\"submit\" value=\"Save / Last >>\" name=\"goTo\" onclick=\"return(false);\">";
        toReturn +=  "\t\t</td>\n\t</tr>\n</table>";
        return toReturn;
    }
    public String displayToTake(boolean first, boolean last)
    {
        return "";
    }

    public String displayToMod()
    {
        //log.write("exam", "PageBreakVO.displayToMod()");
        return this.displayToView();
    }

    public String displayToView()
    {
        //log.write("exam", "PageBreakVO.displayToView()");
        String toReturn = "<table class=sectionHeader>\n";
        toReturn += "\t<tr>\n\t\t<td>\n";
        toReturn += "<input type=\"submit\" value=\"<< Save / First\" name=\"goTo\" onclick=\"return(false);\">"
                 +  "<input type=\"submit\" value=\"< Save / Prev\" name=\"goTo\" onclick=\"return(false);\">";
        toReturn += "<input type=\"submit\" value=\"Save / Next>\" name=\"goTo\" onclick=\"return(false);\">"
                 +  "<input type=\"submit\" value=\"Save / Last >>\" name=\"goTo\" onclick=\"return(false);\">";
        toReturn +=  "\t\t</td>\n\t</tr>\n</table>";
        return toReturn;
        //return "\t<tr>\n\t\t<td>\n\t\t\t<h3 class=header>" + this.value + "</h3>\n\t\t</td>\n\t</tr>\n";
    }

    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        this.setInsertTime(time);
        //log.write("exam", "PageBreakVO.processForm(request) => DOES NOTHING");
    }
    
    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //log.write("exam", "PageBreakVO.processForm(request) => DOES NOTHING");
    }
    public PageBreakVO()
    {
        super();
        //this.setEntryTypeCode("pb");
    }

}