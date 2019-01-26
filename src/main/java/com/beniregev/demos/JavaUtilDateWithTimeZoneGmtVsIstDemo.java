package com.beniregev.demos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JavaUtilDateWithTimeZoneGmtVsIstDemo {

    public static void main(String[] args) {
        Date date = new Date(1546161300000L);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss zzzz");
        String DateToStr = format.format(date);
        System.out.println("date=" + DateToStr);


        Date curDate = new Date();
        format = new SimpleDateFormat();
        DateToStr = format.format(curDate);
        System.out.println("Default pattern: " + DateToStr);

        format = new SimpleDateFormat("yyyy/MM/dd");
        DateToStr = format.format(curDate);
        System.out.println("current Date = " + DateToStr);

        format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        format = new SimpleDateFormat("dd MMMM yyyy zzzz", Locale.ENGLISH);
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        format = new SimpleDateFormat("MMMM dd HH:mm:ss zzzz yyyy",
                Locale.ITALIAN);
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        try {
            Date strToDate = format.parse(DateToStr);
            System.out.println(strToDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
