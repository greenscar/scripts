/*
 * Copied from http://www.rgagnon.com/javadetails/java-0063.html
 * MsgLog.write("This need to be logged");
 * will be written in the default log file. Or you can specify a file name. 
 */
package logging;
import java.io.*;
import java.text.*;
import java.util.*;
public class Secretary {
    protected static String defaultLogFile = "C://Inetpub//Tomcat_4.1//webapps//School//training_log.txt";
    
    public static void write(String s){
        write(defaultLogFile, s);
    }
    public static void write(String f, String s){
        System.err.println(s);
        TimeZone tz = TimeZone.getTimeZone("PST"); // or PST, MID, etc ...
        Date now = new Date();
        DateFormat df = new SimpleDateFormat ("yyyy.mm.dd  hh:mm:ss "); 
        df.setTimeZone(tz);
        String currentTime = df.format(now); 
        try{
            FileWriter aWriter = new FileWriter(f, true);
            aWriter.write(currentTime + " " + s + System.getProperty("line.separator"));
            aWriter.flush();
            aWriter.close();
        }catch(IOException e){
            System.err.println("IOException " + e + " in Secretary.write");
        }
    }
}