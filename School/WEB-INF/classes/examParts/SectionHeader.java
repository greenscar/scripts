package examParts;
import daos.*;
import java.util.*;
import java.sql.*;
public  class SectionHeader extends ExamEntry {
    //*********************** STATIC VARS ************************//
    final static public String FIELD_NAME = "aNewHeader";
    final static public String ENTRY_TYPE_CODE = "he";
    //********************* END STATIC VARS **********************//
    //********************** PROPERTY VARS ***********************//
    private String value;
    //******************** END PROPERTY VARS *********************//
    
    //*********************** QUERY STRINGS **********************//
    private static String insertStmtStr;
    private static String updateStmtStr;
    private static String deleteStmtStr;
    private static String selectStmtStr;
    //********************* END QUERY STRINGS ********************//
    
    //******************* PREPARED STATEMENTS ********************//
    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement selectStmt;
    //***************** END PREPARED STATEMENTS ******************//
    
    //*********************** CONSTRUCTORS ***********************//
    public SectionHeader(){
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        this.createPreparedStatements();
    }
    public SectionHeader(String v) {
        super();
        this.setEntryTypeCode(ENTRY_TYPE_CODE);
        this.value = v;
        this.createPreparedStatements();
    }
    //********************* END CONSTRUCTORS *********************//
    
    //******************** GET & SET FUNCTIONS *******************//
    public void setValue(String v){
        log.write("SectionHeader.setValue(" + v + ")");
        this.value = v;
    }
    public String getValue(){
        log.write("SectionHeader.getValue()");
        return this.value;
    }
    //****************** END GET & SET FUNCTIONS *****************//
    
    //********************* PROCESS FUNCTIONS ********************//
    public void processForm(javax.servlet.http.HttpServletRequest request, int location, int qNum) {
        log.write("SectionHeader.processForm(request, " + location + ", " + qNum + ")");
        String newValue = (String)request.getParameter(FIELD_NAME);
        newValue = this.replaceSubstring(newValue, "\n", "<BR>");
        this.setValue(newValue);
        this.setExamLoc(location);
    }
    //******************** END PROCESS FUNCTIONS ******************//
    
    //********************** HTML DISPLAY FXNS ********************//
    public String displayForm(int currentQNum) {
        log.write("SectionHeader.displayForm()");
        String form;
        form = "<TABLE CLASS=QUESTION>\n      <TR>\n"
             + "          <TD>\n             Section Header:\n          </TD>\n"
             + "          <TD>\n             <TEXTAREA CLASS=header NAME=\""
	     + FIELD_NAME + "\">I luv Aubri</TEXTAREA>\n"
             + "          </TD>\n       </TR>\n       <TR>\n"
	     + "          <TD COLSPAN=\"2\">&nbsp;</TD>\n"
     	     + "       </TR>\n    </TABLE>\n";
        return form;
    }
    public String displayToMod() {
        log.write("SectionHeader.displayToMod()");
        String form;
        form = "<TABLE CLASS=QUESTION>\n      <TR>\n"
	     + "          <TD>\n             Section SectionHeader:\n"
             + "          </TD>\n          <TD>\n"
             + "             <TEXTAREA CLASS=SectionHeader NAME=\"" + FIELD_NAME 
             + "\">" + this.value + "</TEXTAREA>\n"
             + "          </TD>\n       </TR>\n       <TR>\n"
	     + "          <TD COLSPAN=\"2\">&nbsp;</TD>\n"
	     + "       </TR>\n    </TABLE>\n";
        return form;
    }
    public String displayGraded() {
        log.write("SectionHeader.displayGraded()");
        String display;
        display = "   <h3 CLASS=SectionHeader>"
                + this.value + "</h3>\n";
        return display;
    }
    public String displayToTake() {
        log.write("SectionHeader.displayToTake()");
        String display;
        display = "   <h3 CLASS=SectionHeader>"
                + this.value + "</h3>\n";
        return display;
    }
    public String displayToView() {
        log.write("SectionHeader.displayToView()");
        String display;
        display = "   <h3 CLASS=SectionHeader>"
                + this.value + "</h3>\n";
        return display;
    }
    //******************* END HTML DISPLAY FXNS *******************//
    
    //*************************** DB FXNS *************************//
    protected void createPreparedStatements() {
        log.write("SectionHeader.createPreparedStatements()");
        try{
            insertStmtStr = "INSERT INTO SECTION_HEADER "
                          + "(exam_num, exam_loc, description) VALUES "
                          + "(?, ?, ?)";
            insertStmt = dbUtil.createPreparedStatement(insertStmtStr);

            updateStmtStr = "UPDATE SECTION_HEADER SET "
                    + "description=? "
                    + "WHERE exam_num = ? AND exam_loc = ?";
            this.updateStmt = dbUtil.createPreparedStatement(updateStmtStr);
            
            deleteStmtStr = "DELETE FROM SECTION_HEADER "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.deleteStmt = dbUtil.createPreparedStatement(deleteStmtStr);
            
            selectStmtStr = "SELECT description FROM SECTION_HEADER "
                          + "WHERE exam_num = ? AND exam_loc = ?";
            this.selectStmt = dbUtil.createPreparedStatement(selectStmtStr);
        }catch(Exception e){
            log.write("Exception in SectionHeader.createPreparedStatements()" 
                + " : " + e.toString());
        }
    }
    public void insertToDB(){
        log.write("SectionHeader.insertToDB()");
        try{
            insertStmt.setInt(1, this.examNum);
            insertStmt.setInt(2, this.examLoc);
            insertStmt.setString(3, this.value);
            insertStmt.executeUpdate();
        }catch(SQLException e){
            log.write("SQLException in SectionHeader.insertToDB(): " 
                        + e.toString());
        }
    }
    public void loadFromDB(int eNum, int eLoc) throws SQLException{
        log.write("SectionHeader.loadFromDB("+eNum+", "+eLoc+")");
        selectStmt.setInt(1, eNum);
        selectStmt.setInt(2, eLoc);
        ResultSet rs = selectStmt.executeQuery();
        if(rs.next()){
            this.setExamNum(eNum);
            this.setExamLoc(eLoc);
            this.setValue(rs.getString(1));
        }
    }
    public void loadTakenFromDB(int eNum, int eLoc, int tNum) {
        try{
            this.loadFromDB(eNum, eLoc);
        }catch(SQLException e){
            log.write("SQLException in SectionHeader.loadTakenFromDB()");
        }
    }
    public void updateDB() {
    }
    public void deleteFromDB() {
    }
    
    public void insertTakeToDB(javax.servlet.http.HttpServletRequest request) {
        log.write("SectionHeader.insertTakeToDB()");
    }
    
    //************************* END DB FXNS ***********************//
    
}
