package db;
import java.sql.*;
import logging.*;
public class DBMgr {
    Secretary log = null;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    /** Creates a new instance of DbMgr */
    public DBMgr() {
        log = new Secretary();
        log.write("DbMgr() Constructor");
    }
    public void connectToDB(){
        log.write("DbMgr.connectToDB()");
        String driverName = "sun.jdbc.odbc.JdbcOdbcDriver"; // JDBC driver
        String connectionURL = "jdbc:odbc:psnt"; // JDBC connection
        String userName = "helpdesk";
        String password = "helpdesk";
        try{
            //Load Driver that comes with JDK
            Class.forName(driverName); 
            //Establish the connection
            this.conn = DriverManager.getConnection(connectionURL, userName, password);
        }catch(ClassNotFoundException e){
            log.write("Couldn't find the database driver: "
                + e.getMessage());
        }catch(SQLException e){
            log.write("SQL Problem: " + e.getMessage());
            log.write("SQL State: " + e.getSQLState());
            log.write("Vendor Error: " + e.getErrorCode());
        }
    }
    private void closeDBConn(){
        try{
            this.conn.close();
        }catch(SQLException e){
            log.write("SQLException in DBMgr.closeDBConn()");
        }
    }
    public void performUpdate(String q){
        log.write("DBMgr.performInsert(" + q + ")");
        this.connectToDB();
        try{
            this.stmt = conn.createStatement();
            stmt.executeUpdate(q);
        }catch(SQLException e){
            log.write("DBMgr.performInsert SQLException in => " + q 
                + " : " + e.getMessage());
        }catch(Exception e){
            log.write("DBMgr.performInsert Exception in => " + q 
                + " : " + e.getMessage());
        }
        this.closeDBConn();
    }
    
}