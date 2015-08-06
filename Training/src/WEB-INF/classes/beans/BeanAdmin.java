package beans;

import java.beans.*;
import exam.*;
import java.util.*;
import logging.Secretary;
import java.sql.*;
import exam.vos.ExamEntryVO;

public class BeanAdmin extends BeanUser{
    private Iterator examCatNames;
    private Iterator examCatCodes;
   /*
    private Iterator entryTypeCodes;
    private Iterator entryTypeNames;
    private String anEntryTypeName;
    private String anEntryTypeCode;
    */
    private Iterator entryTypes;
    private EntryType anEntryType;
    private String anExamCatName;
    private String anExamCatCode;
    private String nextEntryType;
    private int examLocToMod;
    /** Creates new Admin */
    public BeanAdmin() {
        super();
        Secretary.write("exam", "BeanAdmin CONSTRUCTOR");
    }
    
    
    public String getExamTakerFullName(){
        return this.examVO.getTaker().getFullName();
    }
    public int getExamPointValueTotal(){
        return this.examVO.getPointValueTotal();
    }
   /**********************************************************************
    * FOR  MODIFYING EXAMS
    **********************************************************************/
    public String getExamBodyToMod(){
       Secretary.startFxn("exam", "BeanAdmin.getExamBodyToMod()");
       String toReturn = examVO.displayBodyToMod();
       Secretary.endFxn("exam", "BeanAdmin.getExamBodyToMod()");
       return toReturn;
    }
    /*
     * setExamPrepareToModify takes an examNum as an argument.
     * It then checks to see if the exam has been taken.
     * If that exam has been taken:
     *     The exam is changed to inactive in the DB.
     *     The exam number and date last mod are updated so the 
     *         original is not touched.
     *     The new version is inserted in the DB INACTIVE.
     * If the exam has NOT been taken:
     *     The exam is changed to inactive in the DB.
     *     The date last mod needs changed.
     * NOTE: The exam being modified is marked as inactive in the DB. 
     *       When the mods are completed, the exam needs to be marked ACTIVE
     *       This prevents the exam from being taken during the modification.
     * At the end of this function, examVO is the exam we will modify.
     */
    public boolean examPrepareToModify(Connection connWeb)
    {
        Secretary.startFxn("exam", "BeanAdmin.examPrepareToModify(connWeb)");
        examDAO.setExamVO(examVO);
        boolean beenTaken = false;
        try
        {
            connWeb.setAutoCommit(false);
            if(examDAO.dbLoadNumTakes(connWeb) > 0){
               beenTaken = true;
            }
            Secretary.write("exam", "beenTaken == " + beenTaken);
            //1) Update original exam to inactive.
            examVO.setIsActive(false);
            //If the exam has not been taken, change the date last mod in the original exam.
            if(!beenTaken)
               examVO.setDateLastMod(new java.util.Date());
            examDAO.setExamVO(examVO);
            if(!examDAO.dbUpdateHeader(connWeb))
               return false;
            /*
             * If the exam has been taken, edit a duplicate, not the original.
             */
            if(beenTaken){
                //2) Update date of last mod to right now.
                examVO.setDateLastMod(new java.util.Date());
                //3) Update examNum to maxExamNum + 1.
                examVO.setExamNum(examDAO.dbLoadMaxExamNum(connWeb) + 1);
                //4) Insert modified header to DB.
                examDAO.setExamVO(examVO);
                if(!examDAO.dbInsertHeader(connWeb))
                   return false;
                /*
                 * THE TABLE KEY FOR EACH ENTRY (insert_time) MUST BE 
                 * MODIFIED SO THE ENTRY CAN BE INSERTED INTO THE DB.
                 */
                int numEntries = examVO.getNumEntries();
                ExamEntryVO tempVO;
                for(int x = 1; x <= numEntries; x++)
                {
                    // Create correct ExamEntryDAO based on examVO
                    tempVO = examVO.getEntryAtLoc(x);
                    tempVO.setInsertTime(System.currentTimeMillis() + x);
                    //Thread.sleep(3);
                    //newEntry.setInsertTime(System.currentTimeMillis());
                }
                if(!examDAO.dbInsertBody(connWeb))
                   return false;
            }
            if(this.userDAO == null)
                this.userDAO = new user.UserDAO();
            userDAO.dbLoadVOViaEmpNum(connWeb, examVO.getCreatorEmpNum());
            connWeb.commit();
            connWeb.setAutoCommit(true);
        }catch(SQLException e)
        {         
            try{
                connWeb.rollback();
                connWeb.setAutoCommit(true) ;
            }catch(SQLException e1)
            {
                Secretary.write("exam", "SQLException in BeanAdmin.examPrepareToModify(connWeb) => " + e.toString());
            }
            Secretary.write("exam", "SQLException in BeanAdmin.examPrepareToModify(connWeb) => " + e.toString());
        }
        /*
        catch(InterruptedException e){
           Secretary.write("exam", "InterruptedException in BeanAdmin.examPrepareToModify(connWeb)");
        }
        */
        examVO.setCreator(userDAO.getVO());    
        Secretary.write("exam", "-------------- Exam Before Modification --------------");
        examVO.logExam();
        Secretary.write("exam", "------------ END Exam Before Modification ------------");
        Secretary.endFxn("exam", "BeanAdmin.examPrepareToModify(connWeb)");
        return true;
    }
    
    public void examConcludeModify(Connection connWeb, Connection connPS)
    {
        Secretary.startFxn("exam", "BeanAdmin.examConcludeModify(connWeb)");
        examVO.setIsActive(true);
        examDAO.setExamVO(examVO);
        examDAO.dbUpdateHeader(connWeb);
        examDAO.dbUpdateHeaderPS(connPS, false);
        //Secretary.write("exam", "-------------- Exam After Modification --------------");
        //examVO.logExam();
        //Secretary.write("exam", "------------ END Exam After Modification ------------");
        Secretary.endFxn("exam", "BeanAdmin.examConcludeModify(connWeb)");
    }
    
    public void examProcessHeaderMod(Connection connWeb, Connection connPS, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "ExamModifyServlet.examProcessHeaderMod()");
        examVO.setExamName(request.getParameter("examName"));
        Boolean b = new Boolean(request.getParameter("displayAfter"));
        examVO.setDispAfterTaking(b.booleanValue());
        examVO.setDateLastMod();
        examVO.setCanRetake(request.getParameter("canRetake"));
        examDAO.setExamVO(examVO);
        examDAO.dbUpdateHeader(connWeb);        
        examDAO.dbUpdateHeaderPS(connPS, false);
        Secretary.write("exam", "-------------- Your Modded Header --------------");
        examVO.logHeaderInfo();
        Secretary.write("exam", "------------ END Your Modded Header ------------");
        Secretary.endFxn("exam", "ExamModifyServlet.examProcessHeaderMod()");
    }
    
    public void setExamLocToMod(int eLoc)
    {
        this.examLocToMod = eLoc;
    }
    
    public String getExamEntryToMod()
    {
        Secretary.startFxn("exam", "BeanAdmin.getExamEntryToMod()");
        String x = examVO.displayEntryToMod(this.examLocToMod);
        Secretary.endFxn("exam", "BeanAdmin.getExamEntryToMod()");
        return x;
    }
    public void examInsertBlankEntry(Connection connWeb, String eTypeCode, int eLoc, int qNum)
    {
        Secretary.startFxn("exam", "BeanAdmin.examInsertBlankEntry("+eTypeCode+", " + eLoc + ", " + qNum + ")");
        this.examVO.addBlankEntryToLoc(eTypeCode, eLoc, qNum);
        this.examLocToMod = eLoc;
        //this.examVO.logExam();
        examDAO.setExamVO(this.examVO);
        examDAO.dbInsertNewEntry(connWeb,eLoc);
        Secretary.endFxn("exam", "BeanAdmin.examInsertBlankEntry("+eTypeCode+", " + eLoc + ", " + qNum + ")");
    }
    /*
     * examDeleteEntry deletes the entry currently in memory of the ExamVO.
     * The item is removed from the DB before it is removed from the ExamVO
     *  because if we delete from the ExamVO, then set it to the ExamDAO, 
     *  we won't know who to delete from the DB.
     */
    public void examDeleteEntry(Connection connWeb)
    {
        Secretary.startFxn("exam", "BeanAdmin.examDeleteEntry(connWeb)");
        examVO.setCurrentEntry(this.examLocToMod);
        examVO.processEntryDelete(this.examLocToMod);
        examDAO.setExamVO(this.examVO);
        examDAO.dbDeleteEntry(connWeb);
        examDAO.setExamVO(this.examVO);
        examDAO.dbUpdateHeader(connWeb);
        //examDAO.dbUpdatePointValueTotal(connWeb);
        Secretary.endFxn("exam", "BeanAdmin.examDeleteEntry(connWeb)");
    }
    
    public void examProcessEntryMod(Connection connWeb, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "BeanAdmin.examProcessEntryMod(connWeb, request)");
        this.examVO.processEntryMod(this.examLocToMod, request);
        /*
        * Update all entries in DB whose eLoc >= this.examLocToMod
        */
        examDAO.setExamVO(examVO);
        examDAO.dbUpdateEntry(connWeb, this.examLocToMod);
        examDAO.dbUpdateHeader(connWeb);
        //examDAO.dbUpdatePointValueTotal(connWeb);
        Secretary.write("exam", "-------------- Your Modded Entry --------------");
        examVO.logEntry(examLocToMod);
        Secretary.write("exam", "------------ END Your Modded Entry ------------");
        Secretary.endFxn("exam", "BeanAdmin.examProcessEntryMod(connWeb, request)");
    }
   /**********************************************************************
    * END FOR MODIFYING EXAMS
    **********************************************************************/
   
   /**********************************************************************
    * FOR CREATING EXAMS
    **********************************************************************/
    
   /**
    * METHOD NAME: examProcessNewHeader
    * ARGUMENTS: a database connection to the exam database
    *            a database connection to the PS database
    *            The HttpServletRequest which holds data entered by user on examCreate.jsp
    * REQUIRE: Data was entered on examCreate.jsp & submitted via form.
    * FUNCTION: 1) Create a new ExamVO.
    *           2) Create an ID based on what the max ID in the DB is.
    *           3) Create a Peoplesoft ID
    *           4) Process the header information entered in the form into this ExamVO
    *           5) Insert this header into the PS & Exam database
    * RETURN: n/a
    */    
    public void examProcessNewHeader(Connection connWeb, Connection connPS, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "BeanAdmin.examProcessNewHeader(connWeb, request)");
        if(this.examDAO == null)
            this.examDAO = new ExamDAO();
        // Process all form data into our new ExamVO object
        this.examVO = new ExamVO();
        int maxExamNum = examDAO.dbLoadMaxExamNum(connWeb);
        examVO.setExamNum(maxExamNum + 1);
        String psNum = "SHA";
        int maxPsNum = examDAO.dbLoadMaxPsID(connWeb);
        int t = maxPsNum + 1;
        if(t < 10)
            psNum += "0";
        if(t < 100)
            psNum += "0";
        psNum += t; 
        examVO.setPsID(psNum);
        examVO.setExamName(request.getParameter("examName"));
        examVO.setCreator(meVO);
        //examVO.setCreatorEmpNum(meVO.getEmpNum());
        examVO.setDateCreated();
        examVO.setDateLastMod();
        examVO.setIsActive(true);
        examVO.setCanRetake(request.getParameter("canRetake"));
        String dispAfter = request.getParameter("displayAfterTaking");
        examVO.setDispAfterTaking(dispAfter);
        // Insert our new ExamVO object to the DB.
        examDAO.setExamVO(examVO);
        examDAO.dbInsertHeader(connWeb);
        examDAO.dbInsertHeaderToPS(connPS);
        Secretary.endFxn("exam", "BeanAdmin.examProcessNewHeader(connWeb, request)");
    }
    
    /*
     * An entry has been entered in the form.
     * Process the entry entered by:
     *   1) Insert the data entered into the new entry.
     *   2) Insert the entry to the DB
     */
    public void examProcessPrevEntry(Connection connWeb, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "BeanAdmin.examProcessPrevEntry(connWeb, request)");
        //Process the form to the examVO
        //This will include summing up total point value.
        examVO.processEntryCreateForm(request);
        //Add the entry to the DB
        //This will include updating the total point value.
        examDAO.setExamVO(examVO);
        examDAO.dbInsertLastEntry(connWeb);
        // Update the point value
        examDAO.dbUpdateHeader(connWeb);
        //examDAO.dbUpdatePointValueTotal(connWeb);
        Secretary.endFxn("exam", "BeanAdmin.examProcessPrevEntry(connWeb, request)");
    }
    
    /*
     * Look at the last entry in the ExamVO.
     * Display the form for this entry.
     */
    public String getLastEntryForm(){return examVO.displayLastEntryToCreate();}
    
    /*
     * Add an entry to the exam of the selected type.
     * This entry is the one whose form is going to be displayed on the next screen
     *   for the user to input the values.
     * This method will be called after displaying the exam with answers
     *   because it will add an empty entry to the exam to be displayed.
     */
     
   /**
    * METHOD NAME: examAddBlankEntryToEnd
    * ARGUMENTS: The entryTypeCode of the type of entry to add
    * REQUIRE: this.examVO NOT NULL
    * FUNCTION: Add an entry to the end of the exam of the selected type.
    *           This entry is the one whose form is going to be displayed on the 
    *             next screen for the user to input the values.
    *           This method will be called after displaying the exam with answers
    *             because it will add an empty entry to the exam to be displayed.
    * RETURN: n/a
    */    
    
    public void examAddBlankEntryToEnd(String entryTypeCode){
       Secretary.startFxn("exam", "BeanAdmin.examAddBlankEntryToEnd("+entryTypeCode+")");
       this.examVO.addBlankEntryToEnd(entryTypeCode);
       Secretary.endFxn("exam", "BeanAdmin.examAddBlankEntryToEnd("+entryTypeCode+")");
    }  
    public String getExamBodyWithSolutionsForDevel()
    {
        return examVO.displayBodyWithSolutionsForDevel();
    }
    /**********************************************************************
    * END FOR CREATING EXAMS
    **********************************************************************/
    /**********************************************************************
    * FOR GRADING EXAMS
    **********************************************************************/
    public String getExamBodyToGrade()
    {
        Secretary.startFxn("exam", "BeanAdmin.getExamBodyToGrade()");
        String toReturn = examVO.displayBodyToGrade();
        Secretary.endFxn("exam", "BeanAdmin.getExamBodyToGrade()");
        return toReturn;
    }
   /*
    public void examProcessGrading(Connection connWeb, Connection connPS, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "BeanAdmin.examProcessGrading(connWeb, request)");
        examVO.processGrading(request);
        //examVO.logExam();
        this.examDAO.setExamVO(examVO);
        examDAO.dbUpdatePointsEarned(connWeb);
        examDAO.dbUpdateGradersComment(connWeb);
        examDAO.dbUpdatePSGrade(connPS);
        Secretary.endFxn("exam", "BeanAdmin.examProcessGrading(connWeb, request)");
    }
    */
    /*
     * When the admin is done grading an exam, it goes to a verify grading screen.
     * Process the grading they have done thus far, but don't put it in the DB.
     */
    public void examProcessGrading(javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "BeanAdmin.examProcessGrading(connWeb, request)");
        /*
         * 1) Process grades and comments into VO object
         * 2) Use DAO object to update the DB of these comments.
         */
        examVO.processGrading(request);
        Secretary.endFxn("exam", "BeanAdmin.examProcessGrading(connWeb, request)");
    }
    public void examDbProcessGrading(Connection connWeb, Connection connPS)
    {
       
        this.examDAO.setExamVO(examVO);
        examDAO.dbUpdatePointsEarned(connWeb);
        examDAO.dbUpdateGradersComment(connWeb);
        examDAO.dbUpdatePSGrade(connPS);
    }
    /**********************************************************************
    * END FOR GRADING EXAMS
    **********************************************************************/
    
    public void examDelete(Connection connWeb, Connection connPS)
    {
        Secretary.startFxn("exam", "BeanAdmin.examDelete(connWeb)");
        this.examDAO.setExamVO(examVO);
        //get num takes
        int numTakes = this.examDAO.dbLoadNumTakes(connWeb);
        //Secretary.write("exam", "numTakes == " + numTakes);
        if(numTakes > 0){
            //mark inactive
            examVO.setIsActive(false);
            examDAO.setExamVO(examVO);
            examDAO.dbUpdateHeader(connWeb);        
            examDAO.dbUpdateHeaderPS(connPS, true);
        }
        else{
            //examDAO.dbDeleteHeader();
            examDAO.dbDeleteExam(connWeb, connPS);
        }
        Secretary.endFxn("exam", "BeanAdmin.examDelete(connWeb)");
    }
    public void setStudent(user.UserVO svo){this.userVO = svo;}    
    public String getStudentFirstName(){return userVO.getFirstName();}    
    public String getStudentLastName(){return userVO.getLastName();}    
    public int getStudentEmpNum(){return userVO.getEmpNum();}
   
   /******************************************************************
    * Generate the exam category list for the web page.
    *****************************************************************/
   
     
   /**
    * METHOD NAME: examCatListLoad
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: this.examVO NOT NULL
    * FUNCTION: Load the exam category list into the local exam category iterators.
    *           If the exam category list isn't loaded into the examVO, 
    *              load it from the DB first.
    * RETURN: n/a
    */    
    /*
    public void examCatListLoad(Connection connWeb)
    {
        Secretary.startFxn("exam", "BeanAdmin.examCatListLoad(connWeb)");
        if(this.examDAO == null)
            this.examDAO = new ExamDAO();
        Iterator catCodes = this.examVO.getExamCatCodes();
        if(!(catCodes.hasNext()))
        {
            examDAO.setExamVO(this.examVO);
            examDAO.dbLoadExamCatList(connWeb);
            this.examVO = examDAO.getExamVO();
        }
        this.examCatCodes = examVO.getExamCatCodes();
        this.examCatNames = examVO.getExamCatNames();
        Secretary.endFxn("exam", "BeanAdmin.examCatListLoad(connWeb)");
    }
   */
    
    //public boolean examCatListHasMore(){return examCatCodes.hasNext();}
   
   /*
    * If the exam category iterator has more elements, 
    *    assign the next category in the iterator to
    *    anExamCat to work with.
    */
    /*
    public void setNextExamCat(boolean val)
    {
        if(examCatListHasMore())
        {
            anExamCatCode = (String)(examCatCodes.next());
            anExamCatName = (String)(examCatNames.next());
        }
    }
   
    public String getAnExamCatCode(){return this.anExamCatCode;}
    public String getAnExamCatName(){return this.anExamCatName;}
     */
   /******************************************************************
    * END Generate the exam category list for the web page.
    *****************************************************************/
   
   /******************************************************************
    * Generate the entry type list for the web page.
    *****************************************************************/
    
   /**
    * METHOD NAME: entryTypeListLoad
    * ARGUMENTS: a database connection to the exam database
    * REQUIRE: this.examVO NOT NULL
    * FUNCTION: Load the entry type list into the local entry type iterators.
    *           If the entry type list isn't loaded into the examVO, 
    *              load it from the DB first.
    * RETURN: n/a
    */
    public void entryTypeListLoad(Connection connWeb)
    {
        Secretary.startFxn("exam", "BeanAdmin.entryTypeListLoad(connWeb)");
        Iterator ets = this.examVO.getEntryTypes();
        if(!(ets.hasNext()))
        {
            examDAO.setExamVO(this.examVO);
            examDAO.dbLoadEntryTypeList(connWeb);
            this.examVO = examDAO.getExamVO();
        }
        this.entryTypes = examVO.getEntryTypes();
        Secretary.endFxn("exam", "BeanAdmin.entryTypeListLoad(connWeb)");
    }
    
    public boolean entryTypeListHasMore(){return entryTypes.hasNext();}
   
    public void setNextEntryType(boolean val)
    {
        if(entryTypeListHasMore())
        {
           this.anEntryType = (EntryType)(entryTypes.next());
           /*
            anEntryTypeCode = (String)(entryTypeCodes.next());
            anEntryTypeName = (String)(entryTypeNames.next());
            */
        }
    }
   
    public void entryTypeListReset(){
       this.entryTypes = examVO.getEntryTypes();
       /*
        this.entryTypeCodes = examVO.getEntryTypeCodes();
        this.entryTypeNames = examVO.getEntryTypeNames();
        */
    }
    public String getAnEntryTypeCode()
    {
       return anEntryType.getCode().replace('_', ' ');
        //return anEntryTypeCode.replace('_', ' ');
    }
    public String getAnEntryTypeName()
    {
       return anEntryType.getName().replace('_', ' ');
        //return anEntryTypeName.replace('_', ' ');
    }
   /******************************************************************
    * END Generate the entry type list for the web page.
    *****************************************************************/    
}
