/*
 * PageBreakDAO.java
 *
 * This object actually does nothing because PageBreak does not have a table to itself.
 * However, in order to extend ExamEntryDAO like all other Exam Entries do, it must 
 *      implement these methods.
 * This entry type was added after initial creation of the Exam System.
 * Created on November 9, 2004, 10:35 AM
 */

package exam.daos;
import exam.vos.PageBreakVO;
/**
 *
 * @author  jsandlin
 */
public class PageBreakDAO extends ExamEntryDAO
{
    
    /** Creates a new instance of PageBreakDAO */
    public PageBreakDAO()
    {
    }
    
    public boolean dbDeleteFullExam(java.sql.Connection conn, int eNum)
    {
        return(true);
    }
    
    public boolean dbDeleteVO(java.sql.Connection conn)
    {
        return(true);
    }
    
    public boolean dbInsertVO(java.sql.Connection conn)
    {
        return(true);
    }
    
    public boolean dbLoadVO(java.sql.Connection conn)
    {
        return(true);
    }
    public boolean dbUpdateVO(java.sql.Connection conn)
    {
        return(true);
    }
    public void dropVO()
    {
    }
     public void dbIncrementExamLocVO(java.sql.Connection conn)
    {
    }
   
    public exam.vos.ExamEntryVO getVO()
    {
        return this.examEntryVO;
    }
    
    public void setVO(exam.vos.ExamEntryVO vo)
    {
        this.examEntryVO = (PageBreakVO)vo;
    }
    
}
