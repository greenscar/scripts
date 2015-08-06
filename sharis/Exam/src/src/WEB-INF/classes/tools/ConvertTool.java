/*
 * ConvertTool.java
 *
 * Created on September 21, 2004, 2:07 PM
 */
package tools;

import java.util.*;


public class ConvertTool {
    
    /** Creates a new instance of ConvertTool */
    public ConvertTool() {
    }
    /*
  public static void main (String args[]) {
    Date d = new Date();
    //System.out.println("d => " + d);
    //System.out.println("after = > " + ConvertTool.getDateAfter(d));
    System.out.println("midnight = " + ConvertTool.getMidnightOf(d));
  }
     */
    /*
     * Get tomorrow's date 
     */
    public static Date getDateAfter(Date date)
    {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    public static Date getMidnightOf(Date date)
    {
        //System.out.println("getMidnightOf("+date+")");
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //System.out.println("getMidnightOf("+calendar.getTime()+")");
        return calendar.getTime();
    } 
    /*
     * Convert the provided Date object 
     * and return an int of that date in the format
     * YYYY-MM-DD
     */
    public static String dateToYYYYMMDD(Date d)
    {
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(d);
        return date;
    }    
    public static int getYear(Date d)
    {
        java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy");
        Integer year = new Integer(sd.format(d));
        return year.intValue();
    }    
    // Convert Date to String for display
    public static String parseDate(java.util.Date theDate)
    {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
        String temp = df.format(theDate);
        return temp;
    }
    /*
     * Convert the provided float amount to a dollar value.
     * ex: 45.23223 => $45.23
     */
    public static String parseDollar(float dollarAmt)
    {
        java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance();
        String temp = nf.format(dollarAmt);
        return temp;
    }
    
    public static java.sql.Date getSQLDate(Date d)
    {
        return new java.sql.Date(d.getTime());
    }
    
    public static Date getDateFromFloat(float f){
        Float temp = new Float(f);
        return new java.util.Date(temp.longValue());
    }
    public static float getFloatFromDate(java.util.Date theDate){
        Long now = new Long(theDate.getTime());
        return now.floatValue();
    }
    
    public static float getNowAsFloat(){
      java.util.Date rightNow = new java.util.Date();
      Long now = new Long(rightNow.getTime());
      return now.floatValue();
   }
    public static float getDateXHoursAgo(int numHours)
    {
        java.util.Date rightNow = new java.util.Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        // Go back 2 hrs
        calendar.add(Calendar.HOUR, -numHours);
        Date toReturn = calendar.getTime();
        Long now = new Long(toReturn.getTime());
        return now.floatValue();
        //return toReturn;
      /*
      Then go back x days, where n is an int
      To add x day ahead, use positive int
      To substract x days , use negative int
       */
        /*
        date.add(Calendar.DATE,-30); // go back 30 days
        date.set(Calendar.YEAR, years);
        date.add(Calendar.MONTH, months);
        date.add(Calendar.HOUR, hours);
        date.add(Calendar.MINUTE, minutes);
        date.add(Calendar.SECOND, seconds);
        */
        // Set your initial date
        //calendar.set(2001, 04, 30); 
    }
    public static String destroyHTMLTags(String name)
    {   
        name = name.replaceAll("&#34;","\"");
        name = name.replaceAll("&#034;","\"");
        name = name.replaceAll("&quot;","\"");
        name = name.replaceAll("&#39;","\'");
        name = name.replaceAll("&#039;","\'");
        name = name.replaceAll("&amp;", "&");
        return name;
    }
}
