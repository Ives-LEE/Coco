package idv.leeicheng.coco.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarControl {
    private static Calendar today;
    private static Calendar select;
    public final static String FORMAT_DAY = "yyyy-MM-dd E";
    public final static String FORMAT_MONTH = "yyyy-MM";
    public final static String FORMAT_YEAR = "yyyy";


    public static Calendar creatCalendar() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static Calendar getToday() {
        return today;
    }

    public static void setToday(Calendar today) {
        CalendarControl.today = today;
    }

    public static Calendar getSelect() {
        return select;
    }

    public static void setSelect(Calendar select) {
        CalendarControl.select = select;
    }

    public static String nextDay() {
        select.add(Calendar.DATE, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DAY);
        return simpleDateFormat.format(select.getTime());
    }

    public static String nextMonth() {
        select.add(Calendar.MONTH, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_MONTH);
        return simpleDateFormat.format(select.getTime());
    }

    public static String nextYear() {
        select.add(Calendar.YEAR, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR);
        return simpleDateFormat.format(select.getTime());
    }

    public static String previousDay() {
        select.add(Calendar.DATE, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DAY);
        return simpleDateFormat.format(select.getTime());
    }

    public static String previousMonth() {
        select.add(Calendar.MONTH, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_MONTH);
        return simpleDateFormat.format(select.getTime());
    }

    public static String previousYear() {
        select.add(Calendar.YEAR, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR);
        return simpleDateFormat.format(select.getTime());
    }

    public static String getFormatDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DAY);
        return simpleDateFormat.format(select.getTime());
    }

    public static String getFormatMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_MONTH);
        return simpleDateFormat.format(select.getTime());
    }

    public static String getFormatYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR);
        return simpleDateFormat.format(select.getTime());
    }

    public static String getTodayFormatDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DAY);
        return simpleDateFormat.format(today.getTime());
    }

    public static String getTodayFormatMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_MONTH);
        return simpleDateFormat.format(today.getTime());
    }

    public static String getTodayFormatYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR);
        return simpleDateFormat.format(today.getTime());
    }

    public static Date makeDay(String day){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DAY);
        try {
            Date date = simpleDateFormat.parse(day);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date makeMonth(String month){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_MONTH);
        try {
            Date date = simpleDateFormat.parse(month);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date makeYear(String year){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YEAR);
        try {
            Date date = simpleDateFormat.parse(year);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
