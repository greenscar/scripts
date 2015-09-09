/***********************************************************************
* Module:  SectionHeaderVO.java
* Author:  jsandlin
* Purpose: Defines the Class SectionHeaderVO
***********************************************************************/
package exam.vos;
import logging.Secretary;

public class SectionHeaderVO extends ExamEntryVO
{
    public static String FIELD_NAME = "Header";
    public String value;

    public void logValues() {
        super.logValues();
        log.write("exam", "    value = " + value);
        log.write("exam", "--------------------------------");
    }

    public String displayForm()
    {
        //log.write("exam", "SectionHeader.displayForm()");
        String form;
        form = "<TABLE class=sectionHeader>\n\t<TR>\n"
        + "\t\t<td>\n\t\t\tSection Header:\n\t\t</TD>\n"
        + "\t\t<td>\n\t\t\t<TEXTAREA class=header NAME=\""
        + FIELD_NAME + "\"></TEXTAREA>\n"
        + "\t\t</td>\n\t</tr>\n\t<tr>\n"
        + "\t\t<td COLSPAN=\"2\">&nbsp;</TD>\n"
        + "\t\t</tr>\n</TABLE>\n";
        return form;
    }

    public String displayGraded()
    {
        //log.write("exam", "SectionHeader.displayGraded()");
        return "\t<tr>\n\t\t<td>\n\t\t\t"
               + "<h3 class=header style=\"text-align:center;\">" + this.value + "</h3>\n"
               + "\t\t</td>\n\t\t</tr>\n";
    }

    public String displayToGrade()
    {
        //log.write("exam", "SectionHeader.displayToGrade()");
        return "<TABLE class=sectionHeader>\n\t<tr>\n\t\t<td>\n\t\t\t"
               + "<h3 class=header style=\"text-align:center;\">" + this.value + "</h3>\n"
               + "\t\t</td>\n\t\t</tr>\n</table>\n";
    }
    public String displayToTake()
    {
        //log.write("exam", "SectionHeader.displayToTake()");
        return "<TABLE class=sectionHeader>\n\t<tr>\n\t\t<td>\n\t\t\t"
               + "<h3 class=header style=\"text-align:center;\">" + this.value + "</h3>\n"
               + "\t\t</td>\n\t\t</tr>\n</table>\n";
    }
    public String displayToMod()
    {
        //log.write("exam", "SectionHeader.displayToMod()");
        return "<TABLE class=sectionHeader>\n\t<tr>\n"
            + "\t\t<td>\n\t\t\tSection Header:\n"
            + "\t\t</td>\n\t\t<td>\n"
            + "\t\t\t<textarea class=header NAME=\"" + FIELD_NAME
            + "\">" + replaceSubString(this.value, "\"", "&quot;") + "</TEXTAREA>\n"
            + "\t\t</td>\n\t\t</tr>\n\t<tr>\n"
            + "\t\t<td COLSPAN=\"2\">&nbsp;</TD>\n"
            + "\t\t</tr>\n</table>\n";
    }

    public String displayToView()
    {
        //log.write("exam", "SectionHeader.displayToView()");
        String x = "<TABLE class=sectionHeader>\n\t<tr>\n\t\t<td>\n\t\t\t"
               + "<h3 class=\"header\" style=\"text-align:center;\">" + this.value + "</h3>\n"
               + "\t\t</td>\n\t\t</tr>\n</table>\n";
        return x;
        //return "\t<tr>\n\t\t<td>\n\t\t\t<h3 class=header>" + this.value + "</h3>\n\t\t</td>\n\t</tr>\n";
    }
    public String displayToSelect()
    {
        String x = "<TABLE class=sectionHeader onMouseOver=\"this.className='red_bold'\" "
            + "onMouseOut=\"this.className='black'\" "
            + "onclick=\"submit();\">\n\t<tr>\n\t\t<td>\n\t\t\t"
               + "<h3 class=\"header\" style=\"text-align:center;\">" + this.value + "</h3>\n"
               + "\t\t</td>\n\t\t</tr>\n</table>\n";
        return x;
        //return "\t<tr>\n\t\t<td>\n\t\t\t<h3 class=header>" + this.value + "</h3>\n\t\t</td>\n\t</tr>\n";
    }
    public void processCreateForm(javax.servlet.http.HttpServletRequest request, long time)
    {
        //Secretary.startFxn("exam", "SectionHeaderVO.processCreateForm(request, " + time + ")");
        this.setInsertTime(time);
        String newValue = (String)request.getParameter(FIELD_NAME);
        this.value = replaceSubString(newValue, "\n", "<BR>");
        //Secretary.endFxn("exam", "SectionHeaderVO.processCreateForm(request, " + time + ")");
    }

    public void processModForm(javax.servlet.http.HttpServletRequest request)
    {
        //Secretary.startFxn("exam", "SectionHeaderVO.processModForm(request)");
        String newValue = (String)request.getParameter(FIELD_NAME);
        this.value = replaceSubString(newValue, "\n", "<BR>");
        //Secretary.endFxn("exam", "SectionHeaderVO.processModForm(request)");
    }
    public String getValue()
    {
        return value;
    }

    public void setValue(String newValue)
    {
        value = newValue;
    }

    public SectionHeaderVO()
    {
        super();
        this.value="";
        //this.setEntryTypeCode("he");
    }

}