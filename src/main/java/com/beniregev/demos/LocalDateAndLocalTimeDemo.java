package com.beniregev.demos;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class LocalDateAndLocalTimeDemo {
    public ZonedDateTime convertUtilDateInMillisToZonedDateTime(final long dateTimeInMillis) {
        Date inputDate = new Date(dateTimeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss zzzz");
        String DateToStr = format.format(inputDate);
        System.out.println("dateTimeInMillis=" + dateTimeInMillis + " ==> java.util.date=" + DateToStr);

        Instant instant = inputDate.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        System.out.println("zonedDateTime=" + zonedDateTime);

        return zonedDateTime;
    }

    public LocalDateTime roundMinutesToNextQuoterHour(final LocalDateTime ldt) {
        int minutesToNextQuaterHour = 60 - ldt.getMinute();
        while (minutesToNextQuaterHour > 15) {
            minutesToNextQuaterHour -= 15;
        }
        return ldt.plusMinutes(minutesToNextQuaterHour);
    }

    public static void main(String[] args) {

        final LocalDateAndLocalTimeDemo demo = new LocalDateAndLocalTimeDemo();

        // region Java8 Date & Time API - ZonedDateTime Example
        ZoneId zoneAmericaNewYork = ZoneId.of("America/New_York");

        ZonedDateTime rightNow = ZonedDateTime.now(zoneAmericaNewYork);
        System.out.println("Right now = " + rightNow + " Time zone = " + rightNow.getZone());

        ZonedDateTime anHourAgo = ZonedDateTime.now(zoneAmericaNewYork).minusHours(1);
        System.out.println("An Hour Ago = " + anHourAgo + " Time zone = " + anHourAgo.getZone());

        //  region Java8 Date & Time API - Set ZonedDateTime to a specific date and time, e.g. today at 9am New-York timezone
        ZonedDateTime americaNewYorkCurrentDateTime = ZonedDateTime.now(zoneAmericaNewYork);
        ZonedDateTime nineOclockAmericaNewYork = ZonedDateTime.of ( americaNewYorkCurrentDateTime.toLocalDate() , LocalTime.of ( 9 , 00 ), zoneAmericaNewYork );
        System.out.println("An Hour Ago = " + nineOclockAmericaNewYork + " Time zone = " + nineOclockAmericaNewYork.getZone());
        //  endregion

        //  region Java8 Date & Time API - Convert ZonedDateTime to java.util.date
        final LocalDateTime dateTimeToConvert = nineOclockAmericaNewYork.toLocalDateTime();
        final Date convertToDate = Date.from(nineOclockAmericaNewYork.toLocalDateTime().atZone(zoneAmericaNewYork).toInstant());

        System.out.println(dateTimeToConvert);
        System.out.println(convertToDate);
        //  endregion


        ZoneId zoneJerusalem = ZoneId.of("Asia/Jerusalem");
        ZonedDateTime rightNowInJerusalem = ZonedDateTime.now(zoneJerusalem);
        System.out.println("An Hour Ago = " + rightNowInJerusalem + " Time zone = " + rightNowInJerusalem.getZone());

        ZonedDateTime now = ZonedDateTime.now(zoneAmericaNewYork);

        System.out.println("\n------- ZonedDateTime = convertUtilDateInMillisToZonedDateTime(long) -------");
        ZonedDateTime zdt = demo.convertUtilDateInMillisToZonedDateTime(1546179300000L);
        System.out.println("main() --> zonedDateTime=" + zdt);
        //  endregion

        System.out.println("\n------- LocalDateTime = roundMinutesToNextQuoterHour(LocalDateTime) -------");
        System.out.println("Current DateTime=" + LocalDateTime.now());
        LocalDateTime ldt = demo.roundMinutesToNextQuoterHour(LocalDateTime.now());
        System.out.println("Closest Appointment is at " + ldt);
        ldt = demo.roundMinutesToNextQuoterHour(ldt.plusMinutes(1));
        System.out.println("Next Available Appointment is at " + ldt);
    }
}
