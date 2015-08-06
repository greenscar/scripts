package beans;
import java.beans.*;
import javax.servlet.*;
import java.util.*;
import vos.*;
import daos.*;
import logging.*;
public class ExamCatBean extends Object implements java.io.Serializable {
    //********************** PROPERTY VARS ***********************//
    private ExamCatDAO examCat;
    private Secretary log;
    final static public String FIELD_NAME = "categoryCode";
    //******************** END PROPERTY VARS *********************//
    //*********************** CONSTRUCTORS ***********************//
    public ExamCatBean() {
        log = new Secretary();
        log.write("ExamCatBean()");
        examCat = new ExamCatDAO();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setName(String cat){
        examCat.setName(cat);
    }
    public String getName(){
        return examCat.getName();
    }
    
    public void setCode(String id){
        examCat.setCode(id);
    }
    public String getCode(){
        return examCat.getCode();
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    
    //********************** HTML DISPLAY FXNS ********************//
    
    /*
     * getExamCatListForm returns the HTML for a exam category dropdown
     *      select form having all exam categories as choices.
     */
    public String getExamCatListForm(){
        System.err.println("AdminBean.getExamCatListForm()");
        Collection c = examCat.getCatList();
        Object examCats[] = c.toArray();
        ExamCatVO cat = null;
        String temp = null;
        String toReturn = "<select name=\"" + FIELD_NAME + "\">\n";
        for(int i=0; i<examCats.length; i++){
            cat = (ExamCatVO)(examCats[i]);
            temp = "\t<option value=\"";
            temp += cat.getCode();
            temp += "\">";
            temp += cat.getName();
            temp += "</option>";
            temp += "\n";
            toReturn = toReturn.concat(temp);
        }
        temp = "</select>\n";
        toReturn = toReturn.concat(temp);
        return toReturn;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
}
