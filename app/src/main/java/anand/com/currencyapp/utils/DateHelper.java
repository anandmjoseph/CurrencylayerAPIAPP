package anand.com.currencyapp.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Anand M Joseph (anand.joseph@plexure.com)
 */
public class DateHelper {

    /**
     * Get key for date in format YYYY-MM-DD
     *
     * @param date  Date to make key from.
     * @return Key generated
     */
    public static String getKeyForDate(GregorianCalendar date){
        return date.get(Calendar.YEAR) + "-" + String.format("%02d", date.get(Calendar.MONTH)+1) + "-" + String
        .format("%02d", date.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * Get names for last 7 days.
     * Example: Today, Yesterday, YYYY-MM-DD...
     *
     *
     * @return  List of date names
     */
    public static ArrayList<String> getStringForLastWeek(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Today");
        list.add("Yesterday");

        GregorianCalendar today = new GregorianCalendar();
        today.add(Calendar.DAY_OF_YEAR,-2);
        list.add(getKeyForDate(today));

        for (int i = 0; i < 4; i++) {
            today.add(Calendar.DAY_OF_YEAR,-1);
            list.add(getKeyForDate(today));
        }
        return  list;
    }
}
