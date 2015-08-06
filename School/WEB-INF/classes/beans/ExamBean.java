package beans;
import vos.*;
import daos.*;
import logging.*;
import java.util.*;
import examParts.*;
import java.lang.reflect.*;
import db.*;
import java.sql.*;
public class ExamBean {
    //*********************** STATIC VARS ************************//
    final static public String SESSION = "exam";
    final static public String GET_HEADER = "Create New Exam";
    final static public String GET_FIRST_Q_TYPE = "getFirstQType";
    final static public String GET_FIRST_Q = "getFirstQ";
    final static public String GET_Q_AND_NEXT_Q_TYPE = "getNextQType";
    final static public String DISPLAY_W_SOLUTIONS = "Display With Solutions";
    final static public String DISPLAY_LIST_TO_CHOOSE = "Display List";
    final static public String DISPLAY_TO_TAKE = "dispToTak";
    final static public String NEXT_Q_TYPE_CODE = "nextQTypeCode";
    final static public String PROCESS_TAKEN_TEST = "processTaken";
    final static public String DISPLAY_GRADED = "dispGraded";
    //********************* END STATIC VARS **********************//
    
    //********************** PROPERTY VARS ***********************//
    //**************** FUNCTIONAL VARS *****************//
    private int currentExamLoc;
    private int currentQNum;
    private int examNum;
    private String nextQTypeCode;
    private String thisQTypeCode;
    //************** END FUNCTIONAL VARS ***************//
    
    //***************** CLASSES VARS *******************//
    private ExamDAO examDAO;
    private EntryTypeDAO etDAO;
    private Secretary log;
    //**************** END CLASSES VARS ****************//
    //******************** END PROPERTY VARS *********************//
    
    //*********************** CONSTRUCTORS ***********************//
    public ExamBean(){
        this.currentExamLoc = 1;
        this.currentQNum = 1;
        this.examDAO = new ExamDAO();
        this.etDAO = new EntryTypeDAO();
        this.log = new Secretary();
    }
    public ExamBean(String name, String catCode, int creatorCode, 
                        java.util.Date date, boolean active, boolean display) {
        log.write("ExamBean Constructor");
        int eNum = ExamDAO.getMaxExamNumFromDB() + 1;
        this.currentExamLoc = 1;
        this.currentQNum = 1;
        this.examDAO = new ExamDAO(new ExamHeader(eNum, name, catCode, creatorCode, date, true, display));
        this.etDAO = new EntryTypeDAO();
        this.log = new Secretary();
        log.write("END ExamBean Constructor");
    }
    public ExamBean(ExamDAO eDAO){
        this.currentExamLoc = 1;
        this.currentQNum = 1;
        this.examDAO = eDAO;
        this.etDAO = new EntryTypeDAO();
        this.log = new Secretary();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setCurrentExamLoc(int x){
        this.currentExamLoc = x;
    }
    public void setThisQTypeCode(String newType){
        this.thisQTypeCode = newType;
    }
    public void setNextQTypeCode(String newType){
        this.nextQTypeCode = newType;
    }
    public void setCurrentQNum(int x){
        this.currentQNum = x;
    }
    public void setCatName(String c){
        this.examDAO.setCategoryName(c);
    }
    public void setCreatorViaEmpNum(int num){
        this.examDAO.setCreatorViaEmpNum(num);
    }
    public void setCreator(AdminBean ab){
        this.examDAO.setCreator(ab.getDAO());
    }
    public void setExamNum(int newNum){
        this.examDAO.setExamNum(newNum);
    }
    public void setExamName(String newExamName){
        this.examDAO.setExamName(newExamName);
    }
    public void setCategoryCode(String cat){
        log.write("ExamBean.setCategoryCode("+cat+")");
        this.examDAO.setCategoryCode(cat);
    }
    public void setDateCreated(java.util.Date aDate){
        log.write("ExamBean.setCreationDate("+aDate+")");
        this.examDAO.setDateCreated(aDate);
    }
    public void setDateLastMod(java.util.Date aDate){
        this.examDAO.setDateLastMod(aDate);
    }
    public void setActive(boolean e){
        this.examDAO.setIsActive(e);
    }
    public void setDisplayAfterTaking(boolean dispAftTak) {
        this.examDAO.setDisplayAfterTaking(dispAftTak);
    }
    //******************* END setXXX *******************//
    //********************* getXXX *********************//
    public int getCurrentExamLoc(){
        return this.currentExamLoc;
    }
    public String getNextQTypeCode(){
        return this.nextQTypeCode;
    }
    public String getThisQTypeCode(){
        return this.thisQTypeCode;
    }
    //************* getXXX FROM ExamHeader *************//
    public String getCreatorName(){
        return this.examDAO.getCreatorName();
    }
    public int getCreatorEmpNum(){
        return this.examDAO.getCreatorEmpNum();
    }
    public String getExamName(){
        return this.examDAO.getExamName();
    }
    public int getNum(){
        return this.examDAO.getExamNum();
    }
    public String getCatName(){
        return this.examDAO.getCategoryName();
    }
    public String getCatCode(){
        return this.examDAO.getCategoryCode();
    }
    public java.util.Date getDateCreated(){
        return this.examDAO.getDateCreated();
    }
    public boolean getDisplayAfterTaking() {
        return this.examDAO.getDisplayAfterTaking();
    }
    public boolean getIsActive(){
        return this.examDAO.getIsActive();
    }
    public int getcurrentQNum(){
        return this.currentQNum;
    }
    //******************* END getXXX *******************//
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public String getNextEntryType(){
        log.write("ExamBean.getNextEntryType()");
        return getNextEntryTypeForm();
    }
    /*
     * Create a Class instance based on this.nextQTypeCode and etDAO.getClassExamName()
     * Assign the values given in the form to this instance.
     * Add this instance to the entries Vector
     */
    public void processEntry(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response){
        log.write("ExamBean.processEntry(request, response)");
        int numEntries = examDAO.getNumEntries();
        etDAO.loadDAO(this.nextQTypeCode);
        String classToCreate = etDAO.getClassName();
        try{
            Class x = Class.forName(classToCreate);
            ExamEntry newEntry = (ExamEntry)x.newInstance();
            newEntry.setExamNum(this.examDAO.getExamNum());
            // Set the values of this entry based on the submitted form.
            newEntry.processForm(request, currentExamLoc, currentQNum);
            // Insert the entry into it's proper table
            newEntry.insertToDB();
            /*
             * If newEntry is a SectionHeader, questionNum is irrelevent.
             * Therefore, insert into the DB without the qnum.
             * However, if it is not a SectionHeader, insert the question 
             *      number as well then increment the question number.
             */
            if(newEntry instanceof SectionHeader){
                newEntry.insertToEXAM_ENTRY(0);
            }
            else{
                newEntry.insertToEXAM_ENTRY(this.currentQNum);
                this.currentQNum++;
            }
            // Increment exam location counter
            this.currentExamLoc++;
            // Append this ExamEntry to the entries
            examDAO.addEntry(newEntry);
        }catch(InstantiationException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }catch(ClassNotFoundException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }catch(IllegalAccessException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }
    }
    public void processTaken(javax.servlet.http.HttpServletRequest request){
        examDAO.processTaken(request);
    }
    //******************** END PROCESS FUNCTIONS ******************//
    //********************** HTML DISPLAY FXNS ********************//
    public String getHTMLToTake(String fullName, int empNum){
        log.write("ExamBean.getHTMLToTake()");
        String toDisplay = examDAO.getHeaderToTake(fullName, empNum);
        //log.write("HEADER=\n"+toDisplay);
        toDisplay += "<BR><BR>";
        int numEntries = examDAO.getNumEntries();
        for(int x=0; x < numEntries; x++){
            ExamEntry entry = examDAO.getEntryNum(x);
            String toAdd = entry.displayToTake();
            toDisplay = toDisplay.concat(toAdd);
        }
        return toDisplay;
    }
    public String getHeaderToDisplay(){
        log.write("ExamBean.getHeaderToDisplay()");
        //String creatorName = this.creator.getFullName();
        return examDAO.getHeaderToDisplay(this.getCreatorName());
        
    }
    /*
     * getNextEntryForm looks at the nextQTypeCode in the form.
     * This Code is looked up in the database using the
     *      EntryType class and the Entry type is returned.
     * Finally, the displayForm function is called in the proper class.
     */
    public String getNextEntryForm(){
        log.write("ExamBean.getNextEntryForm("+this.nextQTypeCode+")");
        String toReturn = "<h1>Error in ExamBean.getNextEntryForm()</h1>";
        etDAO.loadDAO(this.nextQTypeCode);
        String classToCreate = etDAO.getClassName();
        try{
            Class x = Class.forName(classToCreate);
            ExamEntry o = (ExamEntry)x.newInstance();
            toReturn = o.displayForm(currentQNum);
        }catch(InstantiationException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }catch(ClassNotFoundException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }catch(IllegalAccessException e){
            log.write("ExamBean.getNextEntryForm threw " + e.toString());
        }
        return toReturn;
    }
    /*
     * Display the exam with solutions. 
     * 1) Display the header
     * 2) Display each Entry with it's solution.
     */
   public String getHtmlWithSolutions(){
        log.write("ExamBean.getHtmlWithSolutions()");
        String toDisplay = examDAO.getHeaderToDisplay(examDAO.getCreatorName());
        toDisplay += "<BR><BR>";
        int numEntries = examDAO.getNumEntries();
        for(int x=0; x < numEntries; x++){
            ExamEntry entry = examDAO.getEntryNum(x);
            String toAdd = entry.displayToView();
            toDisplay = toDisplay.concat(toAdd);
        }
        return toDisplay;
    }
   
    public String getHtmlGraded(){
        log.write("ExamBean.getHtmlGraded()");
        String toDisplay = examDAO.getHeaderGraded();
        toDisplay += "<BR><BR>";
        int numEntries = examDAO.getNumEntries();
        for(int x=0; x < numEntries; x++){
            ExamEntry entry = examDAO.getEntryNum(x);
            String toAdd = entry.displayGraded();
            toDisplay = toDisplay.concat(toAdd);
        }
        return toDisplay;
    }
    /*
     * getNextEntryTypeForm returns the HTML for a exam category dropdown
     *      select form having all exam categories as choices.
     */
    public String getNextEntryTypeForm(){
        log.write("ExamBean.getNextEntryTypeForm()");
        Object quesTypes[] = etDAO.getEntryTypeList().toArray();
        EntryTypeVO typeVO = null;
        String temp = null;
        String toReturn = "<select name=\"" + ExamBean.NEXT_Q_TYPE_CODE + "\">\n";
        for(int i=0; i<quesTypes.length; i++){
            typeVO = (EntryTypeVO)(quesTypes[i]);
            temp = "        <option value=\""
                 + typeVO.getCode()
                 + "\">"
                 + typeVO.getName()
                 + "</option>"
                 + "\n";
            toReturn = toReturn.concat(temp);
        }
        temp = "    </select>\n";
        toReturn = toReturn.concat(temp);
        return toReturn;
    }
    
    //******************* END HTML DISPLAY FXNS *******************//
    //************************* DB METHODS ***********************//
    public void loadFromDB(){
        log.write("loadFromDB()");
        this.examDAO = new ExamDAO();
        examDAO.loadFromDBViaExamNum(this.examNum);
        log.write("examDAO loaded from DB.");
        
    }
    public void insertHeaderToDB(){
        log.write("ExamBean.insertHeaderToDB()");
        this.examDAO.insertHeaderToDB();
    }
    public void insertToDB(){
        log.write("ExamBean.insertToDB()");
    }
    //*********************** END DB METHODS *********************//
}
