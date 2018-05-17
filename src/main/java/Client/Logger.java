package Client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Vincent Guidoux
 */
public class Logger {
    private static Logger ourInstance = new Logger();
    private String log;

    public static Logger getInstance() {
        if(ourInstance == null){
            ourInstance = new Logger();
        }

        return ourInstance;
    }

    private Logger() {
        log = "";
    }

    public void log(String newLine){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        log += "(" + sdf.format(cal.getTime()) + ") : " + newLine + '\n';
    }

    public String getLog() {
        return log;
    }
}
