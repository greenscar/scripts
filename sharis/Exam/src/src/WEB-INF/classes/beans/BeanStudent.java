/*
 * StudentBean.java
 *
 * Created on September 22, 2003, 1:00 PM
 */

package beans;
import java.io.*;
import java.beans.*;
/**
 *
 * @author  jsandlin
 */
public class BeanStudent extends BeanUser{
    
    /** Creates new StudentBean */
    public BeanStudent() {
        super();
    }
    
    /*
    public String getExamBodyToTake(){
        logging.Secretary.startFxn("exam", "BeanStudent.getExamBodyToTake()");
        String b = examVO.displayBodyToTake();
        logging.Secretary.endFxn("exam", "BeanStudent.getExamBodyToTake()");
        return b;
    }
    */
    public String getExamSectionToTake()
    {
        String b = examVO.displaySectionToTake(this.currentExamSectionNum);
        return b;
    }
    public void setExamSectionFirst(javax.servlet.http.HttpServletRequest request)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.setExamSectionFirst()");
        this.examProcessTakenSection(request);
        this.currentExamSectionNum = 1;
        logging.Secretary.endFxn("exam", "BeanStudent.setExamSectionFirst()");
    }
    public void setExamSectionPrev(javax.servlet.http.HttpServletRequest request)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.setExamSectionPrev()");
        this.examProcessTakenSection(request);
        if(this.currentExamSectionNum > 1)
            this.currentExamSectionNum --;
        logging.Secretary.endFxn("exam", "BeanStudent.setExamSectionPrev()");
    }
    public void setExamSectionNext(javax.servlet.http.HttpServletRequest request)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.setExamSectionNext()");
        this.examProcessTakenSection(request);
        if(this.currentExamSectionNum < this.examVO.getNumSections())
            this.currentExamSectionNum++;
        logging.Secretary.endFxn("exam", "BeanStudent.setExamSectionNext()");
    }
    public void setExamSectionLast(javax.servlet.http.HttpServletRequest request)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.setExamSectionLast()");
        this.examProcessTakenSection(request);
        this.currentExamSectionNum = this.examVO.getNumSections();
        logging.Secretary.endFxn("exam", "BeanStudent.setExamSectionLast()");
    }
    
    private void examProcessTakenSection(javax.servlet.http.HttpServletRequest request)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.examProcessTakenSection(request)");
        if(this.examVO.getTaker() == null)
            this.examVO.setTaker(this.meVO);
        this.examVO.processTakeSectionForm(request, this.currentExamSectionNum);
        this.serializeExam();
        logging.Secretary.endFxn("exam", "BeanStudent.examProcessTakenSection(request)");
    }
    
    /*
     * startExamTake is the first function called by ExamTakeServlet when a 
     *      student selects which exam to take.
     * startExamTake loads the selected exam from the database and inserts a
     *      row into ExamsOpen with this student's id and the exam's id.
     */
    public void startExamTake(java.sql.Connection connWeb, java.sql.Connection connPS, int examNum)
    {
        logging.Secretary.startFxn("exam", "BeanStudent.startExamTake(connWeb, " + examNum + ")");
        // LOAD THE EXAM FROM THE DATABASE
        this.setExamFromDB(connWeb, connPS, examNum);
        this.examVO.setTaker(this.getUserVO());
        this.examDAO.setExamVO(this.examVO);
        //this.examDAO.dbDeleteFromExamsOpen(connWeb);
        this.examDAO.dbInsertIntoExamsOpen(connWeb);
        logging.Secretary.endFxn("exam", "BeanStudent.startExamTake(connWeb, " + examNum + ")");
    }
    /*
     * examProcessTaken processes an exam taken by a user. 
     *   1) Assign the user to the exam as the taker
     *   2) Process the taker's answers.
     *   3) Log the exam (in case something goes down after the exam is taken
     *          but before it's totally into the DB)
     *   4) Insert the taker's answers into the DB. 
     */
    public boolean examProcessTaken(java.sql.Connection connWeb, java.sql.Connection connPS, javax.servlet.http.HttpServletRequest request){
        logging.Secretary.startFxn("exam", "BeanStudent.examProcessTaken(connWeb, request)");
        boolean toReturn = false;
        if(this.examVO.getNumSections() > 1)
        {
            // EXAM WAS CONTINUABLE. 
            // 1) Process page just submitted.
            this.examVO.processTakeSectionForm(request, this.currentExamSectionNum);
            // 2) Serialize Exam
            this.serializeExam();
            // 3) Ensure ALL questions answered on exam. 
            //    If not, forward user to correct section.
            int incompleteSectionNum = this.examVO.getIncompleteSectionNum();
            if(incompleteSectionNum == 0)
            {
                // ALL QUESTIONS HAVE BEEN ANSWERED
                examVO.setDateTaken(new java.util.Date());
                if(this.meVO.getPsCourseId().length() > 0)
                {
                    this.examDAO.setExamVO(examVO);
                    this.examDAO.dbInsertTaken(connWeb, connPS);
                }
                this.deleteSerializedExam();
                toReturn = true;
            }
            else
            {
                // SET CURRENT SECTION TO INCOMPLETE SECTION.
                // FORWARD USER TO THAT SECTION.
                this.currentExamSectionNum = incompleteSectionNum;
                toReturn = false;
            }
        }
        else
        {
            // EXAM WAS NOT CONTINUABLE. PROCESS SINGLE SECTION
            examVO.setDateTaken(new java.util.Date());
            this.examVO.setTaker(this.meVO);
            this.examVO.processTakeForm(request);
            if(this.meVO.getPsCourseId().length() > 0)
            {
                this.examDAO.setExamVO(examVO);
                this.examDAO.dbInsertTaken(connWeb, connPS);
            }
            toReturn = true;
        }
        logging.Secretary.endFxn("exam", "BeanStudent.examProcessTaken(connWeb, request) => " + toReturn);
        return toReturn;
    }
        
    public boolean savedExamExists()
    {
        File ft = new File("emp_" + this.getEmpNum() + "_exam.ser");
        if(ft.exists()) 
            return true;
        else 
            return false;
    }
    public boolean loadSavedExam()
    {
        return this.loadSerializedExam();
    }
    public String getExamTakerFullName() {
        return this.getFullName();
    }
    public void examListLoadToTake(java.sql.Connection connWeb)
    {
        logging.Secretary.startFxn("exam", "BeanUser.examListLoadActive(connWeb)");
        if(this.examDAO == null)
        {
            this.examDAO = new exam.ExamDAO();
        }
        
        //iteratorExamVOList = examDAO.dbLoadHeaderListToTake(connWeb, this.getEmpNum());
        iteratorExamVOList = examDAO.dbLoadHeadersNotTakenBy(connWeb, this.getEmpNum());
        logging.Secretary.endFxn("exam", "BeanUser.examListLoadActive(connWeb)");
    }
    private void serializeExam()
    {
        logging.Secretary.startFxn("exam", "BeanStudent.serializeExam()");
        try
        {
            FileOutputStream f = new FileOutputStream("emp_" + this.getEmpNum() + "_exam.ser");
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(this.examVO);
            s.close();
            f.close();
        }catch(IOException e)
        {
            logging.Secretary.write("exam", "IOException in BeanStudent.serializeExam => " + e.toString());
            e.printStackTrace();
        }
        logging.Secretary.endFxn("exam", "BeanStudent.serializeExam()");
    }
    
    public boolean loadSerializedExam()
    {
        logging.Secretary.startFxn("exam", "BeanStudent.unserializeExam()");
        File ft = new File("emp_" + this.getEmpNum() + "_exam.ser");
        boolean toReturn = false;
        if(ft.exists())
        {
            try
            {
                    FileInputStream f = new FileInputStream("emp_" + this.getEmpNum() + "_exam.ser");
                    ObjectInputStream s = new ObjectInputStream(f);
                    System.out.println("s defined");
                    Object temp = s.readObject();
                    this.examVO = (exam.ExamVO)temp;
                    this.currentExamSectionNum = 1;
                    f.close();
                    s.close();
                    toReturn = true;
            }
            catch(Exception e)
            {
                logging.Secretary.write("exam", "BeanStudent.unserializeExam ERROR => " + e.toString());
                toReturn = false;
            }
        }
        logging.Secretary.endFxn("exam", "BeanStudent.unserializeExam() => " + toReturn);
        return toReturn;
    }
    public void deleteSerializedExam()
    {
        logging.Secretary.startFxn("exam", "BeanStudent.deleteSerializedExam()");
        boolean success = false;
        try
        {
            String fName = "emp_" + this.getEmpNum() + "_exam.ser";
            java.io.File f = new File(fName);
            success = f.delete();
        }catch(Exception e)
        {
            logging.Secretary.write("exam", "BeanStudent.deleteSerializedExam ERROR => " + e.toString());
        }
        logging.Secretary.endFxn("exam", "BeanStudent.deleteSerializedExam() => " + success);
    }
}
