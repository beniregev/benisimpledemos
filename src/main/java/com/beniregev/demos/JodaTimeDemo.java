package com.beniregev.demos;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

/**
 * localDateTIme=2018-11-25T18:54:20.694 ;                          localDateTimeString=2018-11-25T18:54:20.694
 * testStartDate (Asia/Jerusalem) = 2018-11-25T18:54:20.694+02:00 ; testStartDateString=2018-11-25T18:54:20:694
 */
public class JodaTimeDemo {
    public static void main(String[] args) {
        final Long startDateMilliseconds = DateTime.now().getMillis();
        final LocalDateTime localDateTIme = new LocalDateTime(startDateMilliseconds);
        final String localDateTimeString = localDateTIme.toString();
        System.out.println("localDateTIme=" + localDateTIme + " ; localDateTimeString=" + localDateTimeString);

        final DateTime testStartDate = new DateTime(startDateMilliseconds, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Jerusalem")));
        final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        final String testStartDateString = testStartDate.toString(fmt);
        System.out.println("testStartDate (Asia/Jerusalem) = " + testStartDate + " ; testStartDateString=" + testStartDateString);

        final DateTime dateTimeUTC = new DateTime(startDateMilliseconds, DateTimeZone.UTC);
        final String dateTimeUTCString = dateTimeUTC.toString();
        System.out.println("dateTime-UTC = " + dateTimeUTC + " ; dateTimeString=" + dateTimeUTCString);

        //final DateTime dateTimeNYC = new DateTime(startDateMilliseconds, DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/New_York")));
        final DateTime dateTimeNYC = new DateTime(startDateMilliseconds);
        System.out.println("dateTime-NYC (a) = " + dateTimeNYC + " (" + dateTimeNYC.toString(fmt) + ") ; dateTimeString=" + dateTimeNYC.toString());

        dateTimeNYC.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/New_York")));
        System.out.println("dateTime-NYC (b) = " + dateTimeNYC + " (" + dateTimeNYC.toString(fmt) + ") ; dateTimeString=" + dateTimeNYC.toString());

//        final String dateTimeNYCString = dateTimeNYC.toString();
//        System.out.println("dateTime (NYC) = " + dateTimeNYC + " (" + dateTimeNYC.toString(fmt) + ") ; dateTimeString=" + dateTimeNYCString);

    }
}
