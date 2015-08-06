/***********************************************************************
 * Module:  ExamVO.java
 * Author:  jsandlin
 * Purpose: Defines the Class ExamVO
 ***********************************************************************/

package exam;
import logging.Secretary;
import java.util.*;
import exam.vos.*;
import user.*;
import tools.HTMLVars;

public class ExamVO implements HTMLVars, java.io.Serializable
{
    private int examNum = 0;
    private String examName = "";
    private boolean isActive = false;
    private boolean dispAfterTaking = false;
    private Date dateCreated = null;
    private Date dateLastMod = null;
    private Date dateTaken = null;
    private Date dateCompleted = null;
    private String psID = "";
    private boolean graded, canRetake;
    private int pointsExtra;
    private int maxQuestionNum = 0;
    private int pointValueTotal;
    private int pointValueFinalGrade;
    private String gradersComment;
    private UserVO creator = null;
    private UserVO taker = null;
    // section will be a group of exam entries between a PageBreakVO
    private int currentSectionNum;
    // fullExam is a Vector of section Vectors
    private Vector fullExam;
    
    private Vector entryTypes;
   
    private int takeNum  = 0;

    private ExamEntryVO currentEntry;
    
    public void logExam()
    {
        Secretary.startFxn("exam", "-=-=-=-=-=-=-=-=-=-=-=-= logExam START =-=-=-=-=-=-=-=-=-=-=-=-");
        this.logHeaderInfo();
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            Secretary.startFxn("exam", "START SECTION");
            for(int y = 0; y < tempSection.size(); y++)
            {
                ((ExamEntryVO)(tempSection.elementAt(y))).logValues();
            }
            Secretary.endFxn("exam", "END SECTION");
        }
        Secretary.endFxn("exam", "-=-=-=-=-=-=-=-=-=-=-=- logExam FINISH -=-=-=-=-=-=-=-=-=-=-=-");
    }
    
   public boolean isSelfGrading()
   {
      //Secretary.startFxn("exam", "ExamVO.figureIsSelfGrading()");
      boolean isSelfGrading = true;
      Vector tempSection = null;
      for(int x = 0; x < this.fullExam.size(); x++)
      {
         tempSection = (Vector)(this.fullExam.elementAt(x));
         for(int y = 0; y < tempSection.size(); y++)
         {
            ExamEntryVO ev = (ExamEntryVO)(tempSection.elementAt(y));
            if((ev instanceof QuestionVO) && (!ev.isSelfGrading()))
               isSelfGrading = false;
         }
      }
      return isSelfGrading;
      //Secretary.endFxn("exam", "ExamVO.figureIsSelfGrading() => " + this.isSelfGrading);
   }
    public String getPsID()
    {
        return this.psID;
    }
    public void setPsID(String n)
    {
        this.psID = n;
    }
    public boolean getCanRetake()
    {
       return this.canRetake;
    }
    public void setCanRetake(int i)
    {
       if(i > 0) 
          this.setCanRetake(true);
       else
          this.setCanRetake(false);
    }
    public void setCanRetake(boolean b)
    {
       this.canRetake = b;
    }
    public void setCanRetake(String s)
    {
       Boolean b = new Boolean(s);
       this.canRetake = b.booleanValue();
    }
    public void setPointsExtra(int p){ this.pointsExtra = p; }
    public int getPointsExtra(){ return this.pointsExtra; }
    public void setPsIDViaDateCreated(){ String temp = this.dateCreated.toString(); }
    
    public void logEntry(int eLoc){
        ExamEntryVO ev = this.getEntryAtLoc(eLoc);
        ev.logValues();
    }
    public Vector getFullExamVector()
    {
        return this.fullExam;
    }
    public ExamEntryVO getCurrentEntry()
    {
        return this.currentEntry;
    }
    /*
     * getEntryAtLoc
     */
    public ExamEntryVO getEntryAtLoc(int eLoc)
    {
        //Secretary.startFxn("exam", "ExamVO.getEntryAtLoc(" + eLoc + ")");
        /*
         * Go through the fullExam Vector and each section vector within.
         *  Find the examEntryVO at the given exam location.
         *  Return this examEntryVO
         */
        ExamEntryVO toReturn = null;
        Vector section = null;
        ExamEntryVO tempVO = null;
        int numEntriesPassed = 0;
        try
        {
            for(int i=0; i < this.fullExam.size(); i++)
            {
               section = ((Vector)(this.fullExam.elementAt(i)));
               for(int k=0; k < section.size(); k++) 
               {
                   tempVO = ((ExamEntryVO)(section.elementAt(k)));
                   if(tempVO.getExamLoc() == eLoc)
                   {
                       toReturn = tempVO;
                       break;
                   }
               }
            }
        }catch(Exception e)
        {
            Secretary.write("exam", "Exception in ExamVO.getEntryAtLoc("+eLoc+") => " + e.toString());
        }
        return toReturn;
    }
    
    public void logHeaderInfo()
    {
        Secretary.write("exam", "examNum = " + examNum);
        Secretary.write("exam", "examName = " + examName);
        //Secretary.write("exam", "catID = " + catID);
        //Secretary.write("exam", "catName = " + catName);
        Secretary.write("exam", "canRetake => " + this.canRetake);
        Secretary.write("exam", "creator.getEmpNum() = " + creator.getEmpNum());
        Secretary.write("exam", "isActive = " + isActive);
        Secretary.write("exam", "dispAfterTaking = " + dispAfterTaking);
        Secretary.write("exam", "dateCreated = " + dateCreated);
        Secretary.write("exam", "dateLastMod = " + dateLastMod);
        Secretary.write("exam", "maxQuestionNum = " + maxQuestionNum);
        if(taker != null){
            Secretary.write("exam", "taker.getEmpNum() = " + taker.getEmpNum());
        }
        Secretary.write("exam", "takeNum = " + takeNum);
        Secretary.write("exam", "dateTaken = " + dateTaken);
    }
    
    /*
    public Iterator getExamCatCodes(){ return examCatCodes.iterator(); }
    public Iterator getExamCatNames(){ return examCatNames.iterator(); }
    public void addExamCatToList(String code, String name)
    {
        this.examCatCodes.add(code);
        this.examCatNames.add(name);
    }
     */
    public void setEntryTypes(Vector v){ this.entryTypes = v; }
    public Iterator getEntryTypes(){ return this.entryTypes.iterator(); }
    
    // REQUIRE: An EntryType exists that has the code provided.
    public EntryType getEntryTypeViaCode(String code)
    {
       Iterator i = entryTypes.iterator();
       Vector codes = new Vector();
       while(i.hasNext())
       {
          EntryType et = ((EntryType)(i.next()));
          String temp = et.getCode();
          if(code.equals(temp))
             return et;
       }
       return(null);
    }
    
    public boolean isGraded(){ return this.graded; }
    public void setGraded(boolean g){ this.graded = g; }
    
    public String displayBodyWithSolutions()
    {
        Secretary.startFxn("exam", "ExamVO.displayBodyWithSolutions()");
        String toReturn = "";
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                toReturn += ((ExamEntryVO)(tempSection.elementAt(y))).displayToView() + "\n<br>\n";
            }
        }
        Secretary.endFxn("exam", "ExamVO.displayBodyWithSolutions()");
        return toReturn;
    } 
    public String displayBodyWithSolutionsForDevel()
    {
        Secretary.startFxn("exam", "ExamVO.displayBodyWithSolutionsForDevel()");
        String toReturn = "";
        Vector tempSection = new Vector();
        Secretary.write("exam", "fullExam.size() = " + this.fullExam.size());
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            Secretary.write("exam", "tempSection.size() = " + tempSection.size());
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO evo = (ExamEntryVO)(tempSection.elementAt(y));
                if(evo != this.getLastEntry())
                {
                    toReturn += ((ExamEntryVO)(tempSection.elementAt(y))).displayToView() + "\n<br>\n";
                }    
            }
        }
        Secretary.endFxn("exam", "ExamVO.displayBodyWithSolutionsForDevel()");
        return toReturn;
    } 
    public String displayBodyGraded()
    {
        Secretary.startFxn("exam", "ExamVO.displayBodyGraded()");
        String toReturn = "";
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                toReturn += ((ExamEntryVO)(tempSection.elementAt(y))).displayGraded() + "\n<br>\n";
            }
        }
        Secretary.endFxn("exam", "ExamVO.displayBodyGraded()");
        return toReturn;
    } 
    public String displayBodyToGrade(){
        Secretary.startFxn("exam", "ExamVO.displayBodyToGrade()");
        String toReturn = "";
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                toReturn += ((ExamEntryVO)(tempSection.elementAt(y))).displayToGrade() + "\n<br>\n";
            }
        }
        Secretary.endFxn("exam", "ExamVO.displayBodyToGrade()");
        return toReturn;
    }
    public String displaySectionToTake(int sectNum)
    {
        Secretary.startFxn("exam", "ExamVO.displaySectionToTake(" + sectNum + ")");
        String toReturn = "";
        Vector tempSection = (Vector)(this.fullExam.elementAt(sectNum - 1));
        Secretary.write("exam", "tempSection.size() = " + tempSection.size());
        for(int y = 0; y < tempSection.size(); y++)
        {
            //Secretary.write("exam", "y = " + y);
            toReturn += ((ExamEntryVO)(tempSection.elementAt(y))).displayToTake() + "\n<br>\n";
        }
        Secretary.endFxn("exam", "ExamVO.displaySectionToTake(" + sectNum + ")");
        return toReturn;
    }
    public String displayBodyToMod()
    {
        Secretary.startFxn("exam", "ExamVO.displayBodyToMod()");
        int qNum = 1;
        String toReturn = "\t<P ALIGN=\"CENTER\"><BUTTON onClick=\"location.href='modifyExam?act=addEntry1&eLoc=1&qNum="+qNum+"'\" NAME=\"insert_here\">Insert a question or header here</BUTTON></P>\n";
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO evo = ((ExamEntryVO)(tempSection.elementAt(y)));
                if(evo instanceof QuestionVO)
                    qNum = evo.getQuestionNum() + 1;
                toReturn += "\n<FORM name=\"mod_form_" + evo.getExamLoc() + "\" method=\"POST\" action=\"modifyExam?act=modEntryDisplay&eLoc="+ evo.getExamLoc() + "\">\n";
                toReturn += evo.displayToSelect();
                toReturn += "\t\t<input type=\"hidden\" name=\"entryType\" value=\"" + evo.getEntryTypeCode() + "\">"
                     + "\n</FORM>\n"
                     + "\t<P ALIGN=\"CENTER\"><BUTTON onClick=\"location.href='modifyExam?act=addEntry1&eLoc="
                     + (evo.getExamLoc() + 1) + "&qNum="+ qNum 
                     +"'\" NAME=\"insert_here\">Insert a question or header here</BUTTON></P>\n";
            }
        }
        Secretary.endFxn("exam", "ExamVO.displayBodyToMod()");
        return toReturn;
    }
    public String displayEntryToMod(int eLoc)
    {
        Secretary.startFxn("exam", "ExamVO.displayEntryToMod("+eLoc+")");
        ExamEntryVO ev = this.getEntryAtLoc(eLoc);
        String entryType = ev.getEntryTypeCode().trim();
        String toReturn = ev.displayToMod()
                + "\t<input type=\"hidden\" name=\"entryType\" value=\""+ entryType + "\">";
        Secretary.endFxn("exam", "ExamVO.displayEntryToMod("+eLoc+")");
        return toReturn;
    }

    /*
    * Display the creation form from the last entry in the entry vector
    */
    public String displayLastEntryToCreate(){
        String toReturn = "";
        ExamEntryVO lastVO = this.getLastEntry();
        toReturn += lastVO.displayForm() + "\n<br>\n";
        return toReturn;
    }
    
    /*
    * Process the form filled out for the last entry
    */
    public void processEntryCreateForm(javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "ExamVO.processEntryCreateForm(request)");
        ExamEntryVO evo = this.getLastEntry();
        long time = System.currentTimeMillis();
        evo.processCreateForm(request, time);
        if(evo instanceof QuestionVO)
        //if(!(evo instanceof SectionHeaderVO || evo instanceof PageBreakVO)){
            this.pointValueTotal += ((QuestionVO)evo).getPointValueTotal();
        //}
        Secretary.endFxn("exam", "ExamVO.processEntryCreateForm(request)");
    }
    /*
    * Delete an entry from the exam.
    */
    public void processEntryDelete(int eLoc)
    {
        Secretary.startFxn("exam", "ExamVO.processEntryDelete("+eLoc+")");
        this.currentEntry = this.getEntryAtLoc(eLoc);
        // Renumber all eLoc above eLoc
        Vector tempSection = null;
        ExamEntryVO tempVO = null;
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                if(tempVO == this.currentEntry)
                {
                    // DELETE THIS ENTRY
                    Secretary.write("exam", "----- DELETING -----");
                    tempVO.logValues();
                    Secretary.write("exam", "--- END DELETING ---");
                    tempSection.remove(tempVO);
                    /*
                     * THE SIZE OF THE VECTOR HAS DECREASED. THEREFORE, IF
                     * WE DON'T DECREMENT THE LOCATION OF THE VECTOR WE ARE
                     * LOOKING IN, WE WILL OVERLOOK THE NEXT ENTRY
                     */
                    y--;
                }
                else if(tempVO.getExamLoc() > eLoc)
                {
                    tempVO.setExamLoc(tempVO.getExamLoc() - 1);
                    if((tempVO instanceof QuestionVO) && (this.currentEntry instanceof QuestionVO))
                        tempVO.setQuestionNum(tempVO.getQuestionNum() - 1);
                }
                 
            }
        }
        this.computePointValueTotal();
        Secretary.endFxn("exam", "ExamVO.processEntryDelete("+eLoc+")");
    }
    
    /*
    * Process the form filled out for updating an entry.
    */
    
    public void processEntryMod(int eLoc, javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "ExamVO.processEntryMod("+eLoc+", request)");
        int vectorLoc = eLoc - 1;
        ExamEntryVO voToMod = this.getEntryAtLoc(eLoc);
        voToMod.processModForm(request);
        //((ExamEntryVO)(entryVOS.elementAt(vectorLoc))).processModForm(request);
        this.computePointValueTotal();
        Secretary.endFxn("exam", "ExamVO.processEntryMod("+eLoc+", request)");
        //((ExamEntryVO)(entryVOS.elementAt(vectorLoc))).logValues();
    }
    
    /*
    * Return the last entry in this exam.
    */
    public ExamEntryVO getLastEntry()
    {
        Secretary.startFxn("exam", "ExamVO.getLastEntry()");
        Vector lastSection = (Vector)(this.fullExam.elementAt(this.fullExam.size() - 1));
        ExamEntryVO lastEntry = null;
        if(lastSection.size() == 0)
        {
            Secretary.write("exam", "fullExam.size => " + this.fullExam.size());
            Vector tempSection = (Vector)(this.fullExam.elementAt(this.fullExam.size() - 2));
            Secretary.write("exam", "tempSection.size() => " + tempSection.size());
            lastEntry = ((ExamEntryVO)(tempSection.elementAt(tempSection.size() - 1)));
            Secretary.write("exam", "lastEntry.class => " + ((Object)lastEntry).getClass().toString());
        }
        else
        {
            lastEntry = ((ExamEntryVO)(lastSection.elementAt(lastSection.size() - 1)));
        }
        Secretary.endFxn("exam", "ExamVO.getLastEntry() => " + ((Object)lastEntry.getClass()).toString());
        return lastEntry;
    }
    public void setCurrentEntry(int eLoc)
    {
        this.currentEntry = this.getEntryAtLoc(eLoc);
        //this.currentEntry = (ExamEntryVO)(this.entryVOS.elementAt(eLoc));
    }
    
    /***********************************************************************
     * TEST ADD ENTRY
     **********************************************************************/
    public void addEntry(ExamEntryVO newEntry)
    {
        //Secretary.startFxn("exam", "ExamVO.addEntry(" + newEntry.toString() + ")");
        if(newEntry.getExamLoc() > this.getNumEntries())
        {
            //Secretary.write("exam", "APPENDING ENTRY TO END OF EXAM");
            // ENTRY IS BEING ADDED TO END OF EXAM
            // GOES ON END OF LAST SECTION
            int numSections = this.fullExam.size();
            if(newEntry instanceof PageBreakVO)
            {
                Vector lastSection = (Vector)(this.fullExam.elementAt(numSections - 1));
                lastSection.add(newEntry);
                this.fullExam.add(new Vector());
                this.currentSectionNum++;
            }
            else
            {
                ((Vector)(this.fullExam.elementAt(numSections - 1))).add(newEntry);
            }
        }
        else
        {
            // ENTRY GOES SOMEWHERE IN MIDDLE OF EXAM
            Vector tempSection = new Vector();
            for(int x = (this.fullExam.size() - 1); x >= 0; x--)
            {
                tempSection = (Vector)(this.fullExam.elementAt(x));
                for(int y = (tempSection.size() - 1); y >= 0; y--)
                {
                    ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                    if(tempVO.getExamLoc() > newEntry.getExamLoc())
                    {
                        tempVO.setExamLoc(tempVO.getExamLoc() + 1);
                        if((newEntry instanceof QuestionVO) && (tempVO instanceof QuestionVO))
                        {
                            ((QuestionVO)tempVO).setQuestionNum(((QuestionVO)tempVO).getQuestionNum() + 1);
                        }
                    }
                    else if(tempVO.getExamLoc() == newEntry.getExamLoc())
                    {
                        tempVO.setExamLoc(tempVO.getExamLoc() + 1);
                        if((newEntry instanceof QuestionVO) && (tempVO instanceof QuestionVO))
                        {
                            ((QuestionVO)tempVO).setQuestionNum(((QuestionVO)tempVO).getQuestionNum() + 1);
                        }
                        if(newEntry instanceof PageBreakVO)
                        {
                             /*
                             * tempSection is the Exam Section we are currently in
                             */
                            Vector v = new Vector();
                            for(int q=0; q < tempSection.size(); q++)
                            {
                                ExamEntryVO tv = ((ExamEntryVO)(tempSection.elementAt(q)));
                                if(tv.getExamLoc() > newEntry.getExamLoc())
                                {
                                    ExamEntryVO ee = ((ExamEntryVO)(tempSection.remove(q)));
                                    v.add(ee);
                                    q--;
                                }
                            }
                            this.fullExam.add(x+1,v);
                            // ADD NEW PageBreak TO END OF THIS VECTOR.
                            tempSection.add(newEntry);
                        }// END if(newEntry instanceof PageBreakVO)
                        else
                        {
                            int numSections = this.fullExam.size();
                            // INSERT NEW ENTRY INTO tempSection AT LOCATION y
                            tempSection.add(y, newEntry);
                        }// END else => if(newEntry instanceof PageBreakVO)
                    }// END else if(tempVO.getExamLoc() == newEntry.getExamLoc())
                }// END for(int y = 0; y < tempSection.size(); y++)
            }// END for(int x = 0; x < this.fullExam.size(); x++)
        }// END else => if(newEntry.getExamLoc() > this.getNumEntries())
        this.computePointValueTotal();
        //Secretary.endFxn("exam", "ExamVO.addEntry(" + newEntry.toString() + ")");
    }
    /***********************************************************************
     * END TEST ADD ENTRY
     **********************************************************************/
    
    /*
     * Renumber all questions with qNum >= the provided argument to
     * question num + 1;
     */
    private void renumberQuestions(ExamEntryVO newEntryVO, int qNum)
    {
        Secretary.startFxn("exam", "ExamVO.renumberQuestions(newEntryVO, " + qNum + ")");
        //if(newEntryVO instanceof SectionHeaderVO || newEntryVO instanceof PageBreakVO)
        if(newEntryVO instanceof QuestionVO)
        {
            //3) If entry is a QuestionVO, renumber all entries whose question number > this new one's
            //Secretary.write("exam", "newEntry questionNum == " + qNum);
            //newEntryVO.setQuestionNum(qNum);
            Vector tempSection = new Vector();
            for(int x = 0; x < this.fullExam.size(); x++)
            {
                tempSection = (Vector)(this.fullExam.elementAt(x));
                for(int y = 0; y < tempSection.size(); y++)
                {
                    ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                    if((tempVO instanceof QuestionVO) && (tempVO.getQuestionNum() >= qNum))
                    {
                        tempVO.setQuestionNum(tempVO.getQuestionNum() + 1);
                    }
                }
            }
            this.maxQuestionNum++;
        }
        else
            newEntryVO.setQuestionNum(0);
        Secretary.endFxn("exam", "ExamVO.renumberQuestions(newEntryVO, " + qNum + ")");
    }
    
    
   /**
    * METHOD NAME: addBlankEntryToEnd
    * ARGUMENTS: The entryTypeCode of the type of entry to add
    * REQUIRE: this.examVO NOT NULL
    * FUNCTION: Add an entry to the end of the exam of the selected type.
    *           Increment the maxQuestionNum counter appropriately.
    *           Create the appropriate ExamEntryVO object and pass that to
    *             the addEntry function to append it to the end of the exam.
    * RETURN: n/a
    */    
    public void addBlankEntryToEnd(String entryTypeCode)
    {
        Secretary.startFxn("exam", "ExamVO.addBlankEntryToEnd("+entryTypeCode+")");
        EntryType et = this.getEntryTypeViaCode(entryTypeCode);
        String voClassName = "exam.vos." + et.getClassName() + "VO";
        try
        {
            Class x = Class.forName(voClassName);
            ExamEntryVO newEntry = (ExamEntryVO)x.newInstance();
            newEntry.setEntryType(et);
            newEntry.setExamNum(this.getExamNum());
            newEntry.setExamLoc(this.getNumEntries() + 1);
            if(newEntry instanceof QuestionVO)
            {
                newEntry.setQuestionNum(++this.maxQuestionNum);
            }
            else
                newEntry.setQuestionNum(0);
            //this.addEntryToEnd(newEntry);
            this.addEntry(newEntry);
        }catch(ClassNotFoundException e){
            Secretary.write("exam", "ClassNotFoundException in ExamVO.addBlankEntryToEnd("+entryTypeCode+") : " + e.getMessage());
        }catch(InstantiationException e){
            Secretary.write("exam", "InstantiationException in ExamVO.addBlankEntryToEnd("+entryTypeCode+") : " + e.getMessage());
        }catch(IllegalAccessException e){
            Secretary.write("exam", "llegalAccessException in ExamVO.addEntry("+entryTypeCode+") : " + e.getMessage());
        }
        Secretary.endFxn("exam", "ExamVO.addBlankEntryToEnd("+entryTypeCode+")");
    }
    /*
    * insertBlankEntry is used when doing exam modification to insert an entry 
    *    at a particular location.
    * This will insert a blank entry and renumber all entries whose location is
    *    greater than eLoc.
    */
    public void addBlankEntryToLoc(String entryTypeCode, int eLoc, int qNum)
    {
        Secretary.startFxn("exam", "ExamVO.addBlankEntryToLoc("+entryTypeCode + ", " + eLoc + ", " + qNum + ")");
        EntryType et = this.getEntryTypeViaCode(entryTypeCode);
        String voClassName = "exam.vos." + et.getClassName() + "VO";
        try
        {
            //1) Create the entry instance.
            Class x = Class.forName(voClassName);
            ExamEntryVO newEntry = (ExamEntryVO)x.newInstance();
            newEntry.setInsertTime(System.currentTimeMillis());
            newEntry.setEntryType(et);
            newEntry.setExamNum(this.getExamNum());
            newEntry.setExamLoc(eLoc);
            if(newEntry instanceof QuestionVO)
                newEntry.setQuestionNum(qNum);
            else
                newEntry.setQuestionNum(0);
            //this.addEntryToLoc(newEntry);
            this.addEntry(newEntry);
        }catch(ClassNotFoundException e){
            Secretary.write("exam", "ClassNotFoundException in ExamVO.addBlankEntry("+entryTypeCode+") : " + e.getMessage());
        }catch(InstantiationException e){
            Secretary.write("exam", "InstantiationException in ExamVO.addBlankEntry("+entryTypeCode+") : " + e.getMessage());
        }catch(IllegalAccessException e){
            Secretary.write("exam", "llegalAccessException in ExamVO.addEntry("+entryTypeCode+") : " + e.getMessage());
        }
        Secretary.endFxn("exam", "ExamVO.addBlankEntryToLoc("+entryTypeCode + ", " + eLoc + ", " + qNum + ")");
    }
    /*
    * setExamNum changes this exam number to the provided argument.
    * Due to the DB, each entry holds the exam num also. 
    * Therefore, when the examNum is updated in the exam, it must 
    *      also be updated in all entries.
    */
    public void setExamNum(int newExamNum)
    {
        examNum = newExamNum;
        int numEntries = this.getNumEntries();
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                tempVO.setExamNum(newExamNum);
            }
        }
        /*
        for(int x=0; x < numEntries; x++)
            ((ExamEntryVO)entryVOS.elementAt(x)).setExamNum(newExamNum);
         */
    }
    public int getExamNum()
    {
        return examNum;
    }
    public String getExamName()
    {
        return examName;
    }
    public void setExamName(String newExamName)
    {
        examName = newExamName;
    }
    /*
    public String getCatID()
    {
        return catID;
    }
    public void setCatID(String newCatID)
    {
        catID = newCatID;
    }
    public void setCatViaCatID(String newCatID){
        this.catID = newCatID;
        this.catName = (String)(this.examCatNames.elementAt(this.examCatCodes.indexOf(newCatID)));
    }
    public String getCatName()
    {
        return catName;
    }
    public void setCatName(String newCatName)
    {
        catName = newCatName;
    }
     */
    public int getCreatorEmpNum()
    {
        return creator.getEmpNum();
    }
    public int getTakerEmpNum(){
        return taker.getEmpNum();
    }
    public UserVO getCreator(){
        return this.creator;
    }
    public void setCreator(UserVO user){
        this.creator = user;
    }
    public UserVO getTaker(){
        return this.taker;
    }
    public void setCreatorEmpNum(int newCreatorEmpNum)
    {
        //Secretary.startFxn("exam", "ExamVO.setCreatorEmpNum(" + newCreatorEmpNum + ")");
        this.creator.setEmpNum(newCreatorEmpNum);
        //Secretary.endFxn("exam", "ExamVO.setCreatorEmpNum(" + newCreatorEmpNum + ")");
    }
    public void setTaker(UserVO aTaker){
        this.taker = aTaker;
    }
    public void setTakerEmpNum(int num){
        if(this.taker == null)
            this.taker = new UserVO();
        this.taker.setEmpNum(num);
    }
    public void setTakerFirstName(String fn){
        if(this.taker == null)
            this.taker = new UserVO();
        this.taker.setFirstName(fn);
    }
    public void setTakerLastName(String ln){
        if(this.taker == null)
            this.taker = new UserVO();
        this.taker.setLastName(ln);
    }
    public void setGradersComment(String x){
        this.gradersComment = x;
    }
    
    public boolean getIsActive()
    {
        return isActive;
    }
    public void setIsActive(int i)
    {
       if(i == 1) this.setIsActive(true);
       else this.setIsActive(false);
    }
    public void setIsActive(boolean newIsActive)
    {
        isActive = newIsActive;
    }
    public boolean getDispAfterTaking()
    {
        return dispAfterTaking;
    }
    public void setDispAfterTaking(boolean newDispAfterTaking)
    {
        dispAfterTaking = newDispAfterTaking;
    }
    public void setDispAfterTaking(int i)
    {
       if(i == 1)
            this.setDispAfterTaking(true);
        else
            this.setDispAfterTaking(false);
    }
    public void setDispAfterTaking(String dispAfter)
    {  
        if(dispAfter.equals("true"))
            this.setDispAfterTaking(true);
        else
            this.setDispAfterTaking(false);
    }
    public int getTakeNum()
    {
        return takeNum;
    }
    public void setTakeNum(int newTakeNum)
    {
        takeNum = newTakeNum;
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                tempVO.setTakeNum(newTakeNum);
            }
        }
    }
    /*
    * getNumEntries returns the number of entries in this exam
    */
    public int getNumEntries(){
        int total = 0;
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            total += ((Vector)(this.fullExam.elementAt(x))).size();
        }
        return total;
    }
    public int getNumSections()
    {
        return this.fullExam.size();
    }
    public String getGradersComment()
    {
        return this.gradersComment;
    }
    /************************************************************
     * Date Functions
     ***********************************************************/
    public Date getDateCreated()
    {
        return dateCreated;
    }
    public void setDateCreated()
    {
        this.dateCreated = new java.util.Date();
    }
    public void setDateCreated(Date newDateCreated)
    {
        dateCreated = newDateCreated;
    }
    public Date getDateLastMod()
    {
        return dateLastMod;
    }
    public void setDateLastMod(Date newDateLastMod)
    {
        dateLastMod = newDateLastMod;
    }
    public void setDateLastMod(){
        this.dateLastMod = new java.util.Date();
    }
    public Date getDateTaken()
    {
        return dateTaken;
    }
    public void setDateTaken(Date newDateTaken)
    {
        dateTaken = newDateTaken;
    }
    /************************************************************
     * END Date Functions
     ***********************************************************/
    
    /************************************************************
     * Point Value Functions
     ***********************************************************/
    public void setPointValueFinalGrade(int newFinalGrade)
    {
        pointValueFinalGrade = newFinalGrade;
    }
    public int getPointValueFinalGrade()
    {
        return pointValueFinalGrade;
    }
    public String getGradeString()
    {
        return (String)(this.getPointValueFinalGrade() + "/" + this.getPointValueTotal());
    }
    public int getGradePercent()
    {
        Float f = new Float(((float)(this.pointValueFinalGrade) / (float)(this.pointValueTotal)) * 100);
        return f.intValue();
    }
    public void computePointValueFinalGrade(){
        Secretary.startFxn("exam", "ExamVO.computePointValueFinalGrade()");
        //Secretary.write("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
        int newV = 0;
        Vector tempSection = new Vector();
        //Secretary.write("exam", "newV = " + newV);
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            //Secretary.write("exam", "examSection = " + x + " && newV = " + newV);
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                //tempVO.logValues();
                if(tempVO instanceof QuestionVO)
                {
                    //logging.Secretary.write("exam", "sectionLoc = " + y + " && newV = " + newV);
                    newV += ((QuestionVO)tempVO).getPointValueEarnedTotal();
                }
                
            }
        }
        //Secretary.write("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
        newV += this.pointsExtra;
        this.pointValueFinalGrade = newV;
        //Secretary.write("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
        Secretary.endFxn("exam", "ExamVO.computePointValueFinalGrade()");
        //Secretary.endFxn("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
    }
    public void computePointValueTotal(){
        //Secretary.startFxn("exam", "ExamVO.computePointValueTotal()");
        int newV = 0;
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                if(tempVO instanceof QuestionVO)
                    newV += ((QuestionVO)tempVO).getPointValueTotal();
            }
            //Secretary.write("exam", "Section # " + (x+1) + " => " + newV + " points on exam");
        }
        this.pointValueTotal = newV;
        //Secretary.write("exam", "pointValueTotal = " + newV);
        //Secretary.endFxn("exam", "ExamVO.computePointValueTotal()");
        //Secretary.write("exam", "pointValueTotal = " + this.pointValueTotal);
    }
    
    public void setPointValueTotal(int pv){
        this.pointValueTotal = pv;
    }
    public int getPointValueTotal(){
        return this.pointValueTotal;
    }
    /************************************************************
     * END Point Value Functions
     ***********************************************************/
    
    
    /************************************************************
     * Form Process Functions
     ***********************************************************/
    public void processTakeSectionForm(javax.servlet.http.HttpServletRequest request, int sectionNum)
    {
        Secretary.startFxn("exam", "ExamVO.processTakeSectionForm(request, " + sectionNum + ")");
        //Set completion time. 
        this.dateCompleted = new Date();
        Vector tempSection = null;
        tempSection = (Vector)(this.fullExam.elementAt(sectionNum - 1));
        for(int y = 0; y < tempSection.size(); y++)
        {
            ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
            Secretary.write("exam", "tempVO is a " + ((Object)tempVO).getClass().toString());
            if(tempVO instanceof QuestionVO)
                ((QuestionVO)tempVO).processEmpAnswerForm(request);
        }
        //Secretary.write("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
        this.computePointValueFinalGrade();
        Secretary.endFxn("exam", "ExamVO.processTakeSectionForm(request, " + sectionNum + ")");
    }
   /*
    * Read the taken exam and place values into each question object.
    */
    public void processTakeForm(javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "ExamVO.processTakeForm(request)");
        //Set completion time. 
        this.dateCompleted = new Date();
        Vector tempSection = null;
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                Secretary.write("exam", "tempVO is a " + ((Object)tempVO).getClass().toString());
                if(tempVO instanceof QuestionVO)
                    ((QuestionVO)tempVO).processEmpAnswerForm(request);
            }
        }
        Secretary.write("exam", "pointValueFinalGrade = " + this.pointValueFinalGrade);
        this.computePointValueFinalGrade();
        if(this.isSelfGrading())
           this.setGraded(true);
        Secretary.endFxn("exam", "ExamVO.processTakeForm(request)");
    }

    public void processGrading(javax.servlet.http.HttpServletRequest request)
    {
        Secretary.startFxn("exam", "ExamVO.processGrading(request)");
        this.setGradersComment(((String)request.getParameter("gradersComment")));
        if(request.getParameter("extraPoints") == null || request.getParameter("extraPoints").length() == 0)
        {
            this.setPointsExtra(0);
        }
        else
        {
           Integer p = new Integer(request.getParameter("extraPoints"));
           this.setPointsExtra(p.intValue());
        }
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                if(tempVO instanceof QuestionVO)
                    ((QuestionVO)tempVO).processGradingForm(request);
            }
        }
        this.graded = true;
        this.computePointValueFinalGrade();
        Secretary.endFxn("exam", "ExamVO.processGrading(request)");
    }
    /************************************************************
     * END Form Process Functions
     ***********************************************************/
    
    /*
     * Go through all of exam and ensure all questions are answered.
     * If a question is found that is not answered, return the section 
     *      number of that question's location.
     */
    public int getIncompleteSectionNum()
    {
        Secretary.startFxn("exam", "ExamVO.getIncompleteSectionNum()");
        Vector tempSection = new Vector();
        for(int x = 0; x < this.fullExam.size(); x++)
        {
            tempSection = (Vector)(this.fullExam.elementAt(x));
            for(int y = 0; y < tempSection.size(); y++)
            {
                ExamEntryVO tempVO = ((ExamEntryVO)(tempSection.elementAt(y)));
                //Secretary.write("exam", ((Object)tempVO).getClass().toString());
                if(tempVO instanceof QuestionVO)
                {
                    QuestionVO qVO = (QuestionVO)tempVO;
                    if(!qVO.getAnswerEntered())
                    {
                        Secretary.endFxn("exam", "ExamVO.getIncompleteSectionNum() => " + x);
                        return(x + 1);
                    }
                }   
            }
        }
        Secretary.endFxn("exam", "ExamVO.getIncompleteSectionNum() => " + 0);
        return(0);
    }
    private Vector getSectionAtLoc(int vLoc)
    {
        return ((Vector)(this.fullExam.elementAt(vLoc)));
    }
    public ExamVO()
    {
        //Secretary.write("exam", "ExamVO CONSTRUCTOR()");
        entryTypes = new Vector();
        this.fullExam = new Vector();
        this.currentSectionNum = 0;
        this.fullExam.add(new Vector());
        this.creator = new UserVO();
        this.pointValueTotal = 0;
        this.pointsExtra = 0;
        this.graded = false;
    }
}