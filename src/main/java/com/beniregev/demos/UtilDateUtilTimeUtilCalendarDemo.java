package com.beniregev.demos;

import com.beniregev.ui.controller.helper.ProviderSchedulingHelper;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UtilDateUtilTimeUtilCalendarDemo {

    private final int STANDARD_HOUR_BLOCK_NUMBER = 24;
    private final int DST_HOUR_BLOCK_NUMBER_IN_MARCH = 23;
    private final int DST_HOUR_BLOCK_NUMBER_IN_NOV = 25;

    private final String NEW_YORK_ZONE_ID = "America/New_York";
    private final String JERUSALEM_ZONE_ID = "Asia/Jerusalem";
    private final TimeZone NEW_YORK_TIME_ZOME = TimeZone.getTimeZone(NEW_YORK_ZONE_ID);
    private final TimeZone JERUSALEM_TIME_ZOME = TimeZone.getTimeZone(JERUSALEM_ZONE_ID);
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private int hourIdx;
    private Calendar cal;
    private Date date;

//    private ProviderSchedulingHelper helper;


    public void init() {
//        helper = new ProviderSchedulingHelper();
        cal = Calendar.getInstance();
        System.out.println("init() -- cal (local) = " + DATE_FORMAT.format(cal.getTime()) + " TimeZone = " + cal.getTimeZone().getDisplayName());
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal = Calendar.getInstance();
        System.out.println("init() -- cal (GMT)   = " + DATE_FORMAT.format(cal.getTime()) + " TimeZone = " + cal.getTimeZone().getDisplayName());
    }

    /** MarchDate_before */
    public void dstMarchDate_before() {
        date = new DateTime(2018, 3, 11, 1, 0, 0, 0).toDate();
        System.out.println("dstMarchDate_before() -- date = " + DATE_FORMAT.format(date));
    }

    /** MarchDate_after */
    public void dstMarchDate_after() {
        date = new DateTime(2018, 3, 11, 10, 0, 0, 0).toDate();
        System.out.println("dstMarchDate_after() -- date = " + DATE_FORMAT.format(date));
    }

    /** NovDate_before") */
    public void dstNovDate_before() {
        date = new DateTime(2018, 11, 4, 1, 0, 0, 0).toDate();
        System.out.println("dstNovDate_before() -- date = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void dstNovDate_after() {
        date = new DateTime(2018, 11, 4, 10, 0, 0, 0).toDate();
        System.out.println("dstNovDate_after() -- date = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void standardDate() {
        date = new DateTime(2018, 1, 11, 1, 0, 0, 0).toDate();
        System.out.println("standardDate() -- date = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void setDateAndHourIdx() {
        cal.setTime(date);
        hourIdx = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println("setDateAndHourIdx() -- date = " + DATE_FORMAT.format(date) + " ; cal value = " + DATE_FORMAT.format(cal.getTime()) + " ; TimeZone = " + cal.getTimeZone().getDisplayName() + " ; hourIdx = " + hourIdx);
    }

    public void showMeSomeSimpleDateDemos() {
        Locale locale = Locale.getDefault();
        System.out.println("Locale = \"" + locale + "\"");

        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        birthDate.set(1969, Calendar.JUNE, 2);     //  June 2nd, 1969
        System.out.println("Birth-date=" + DATE_FORMAT.format(birthDate.getTime()) + " TimeZone = " + birthDate.getTimeZone().getDisplayName());

        Calendar examDateTime = Calendar.getInstance();
        //examDateTime.setTime(DateUtils.newDate(2019, Calendar.MAY, 1, 2, 6, 49,0,false));
        System.out.println("Exan Datetime = " + DATE_FORMAT.format(examDateTime.getTime()) + " TimeZone = " + examDateTime.getTimeZone().getDisplayName());

        TimeZone defaultTz = TimeZone.getTimeZone("America/New_York");
//        TimeZone defaultTz = TimeZone.getTimeZone("EST");
        Calendar cal = Calendar.getInstance(defaultTz);
        cal.set(2017, Calendar.MAY, 1, 10, 25, 30);
        System.out.println("Cal = " + DATE_FORMAT.format(cal.getTime()) + " TimeZone = " + cal.getTimeZone().getDisplayName());

        TimeZone eastDaylightTime = TimeZone.getTimeZone("DST");
        Calendar calEDT = Calendar.getInstance(eastDaylightTime);
        calEDT.set(2017, Calendar.MAY, 1, 10, 25, 30);
        System.out.println("Cal = " + DATE_FORMAT.format(calEDT.getTime()) + " TimeZone = " + calEDT.getTimeZone().getDisplayName());

    }

    public LocalDateTime convertUtilDateInMillisToLocalDateTime(final long dateTimeInMillis) {
        Date inputDate = new Date(dateTimeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss zzzz");
        String DateToStr = format.format(inputDate);
        System.out.println("dateTimeInMillis=" + dateTimeInMillis + " ==> java.util.date=" + DateToStr);

        Instant instant = inputDate.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDateTime localDateTime = zdt.toLocalDateTime();
        System.out.println("localDateTime=" + localDateTime);

        return localDateTime;
    }

    public static void main(String[] args) {
        UtilDateUtilTimeUtilCalendarDemo demo = new UtilDateUtilTimeUtilCalendarDemo();

        System.out.println("\n------- demo.init -------");
        demo.init();

        System.out.println("\n------- demo.demo.dstNovDate_before()    -------");
        demo.dstNovDate_before();

        System.out.println("\n------- demo.setDateAndHourIdx()         -------");
        demo.setDateAndHourIdx();

        System.out.println("\n------- demo.showMeSomeSimpleDateDemos() -------");
        demo.showMeSomeSimpleDateDemos();

        System.out.println("\n------- LocalDateTime = demo.convertUtilDateInMillisToLocalDateTime(Long) -------");
        LocalDateTime localDateTime = demo.convertUtilDateInMillisToLocalDateTime(1546161300000L); // 2018-12-30 11:15:00 Israel Standard Time (Asia/Jerusalem)

    }
}
