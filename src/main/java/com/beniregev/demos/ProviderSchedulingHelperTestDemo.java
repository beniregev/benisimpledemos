package com.beniregev.demos;

import com.beniregev.ui.controller.helper.ProviderSchedulingHelper;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ProviderSchedulingHelperTestDemo {
    private final int STANDARD_HOUR_BLOCK_NUMBER = 24;
    private final int DST_HOUR_BLOCK_NUMBER_IN_MARCH = 23;
    private final int DST_HOUR_BLOCK_NUMBER_IN_NOV = 25;

    private final String ZONE_ID="America/New_York";
    private final TimeZone tz = TimeZone.getTimeZone(ZONE_ID);
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    int hourIdx;
    Calendar cal;

    /* date */
    Date date;

    /** helper */
    private ProviderSchedulingHelper helper;

    /** Initialize */
    public void init() {
        helper = new ProviderSchedulingHelper();
        cal = Calendar.getInstance();
        System.out.println("cal value (after init()) = " + DATE_FORMAT.format(cal.getTime()) + " TimeZone = " + cal.getTimeZone().getDisplayName());
    }

    /** MarchDate_before */
    public void dstMarchDate_before() {
        date = new DateTime(2018, 3, 11, 1, 0, 0, 0).toDate();
        System.out.println("date value (after dstMarchDate_before()) = " + DATE_FORMAT.format(date));
    }

    /** MartchDate_after */
    public void dstMarchDate_after() {
        date = new DateTime(2018, 3, 11, 10, 0, 0, 0).toDate();
        System.out.println("date value (after dstMarchDate_after()) = " + DATE_FORMAT.format(date));
    }

    /** NovDate_before") */
    public void dstNovDate_before() {
        date = new DateTime(2018, 11, 4, 1, 0, 0, 0).toDate();
        System.out.println("date value (after dstNovDate_before()) = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void dstNovDate_after() {
        date = new DateTime(2018, 11, 4, 10, 0, 0, 0).toDate();
        System.out.println("date value (after dstNovDate_after()) = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void standardDate() {
        date = new DateTime(2018, 1, 11, 1, 0, 0, 0).toDate();
        System.out.println("date value (after standardDate()) = " + DATE_FORMAT.format(date));
    }

    /** StandardDate */
    public void setDateAndHourIdx() {
        cal.setTime(date);
        hourIdx = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println("date value (after setDateAndHourIdx()) = " + DATE_FORMAT.format(date) + " ; cal value = " + DATE_FORMAT.format(cal.getTime()) + " ; TimeZone = " + cal.getTimeZone().getDisplayName() + " ; hourIdx = " + hourIdx);
    }

    /**
     * SHOULD = Test the providerSchedulingHelper.modifyHourIdxForDST before transition at dst changing day of March",
     * EXPECTED TO = "returning hourinx as usual
     */
    public void testModifyHourIdxForDSTInMarch_before(){
        init();
        dstMarchDate_before();
        setDateAndHourIdx();

        final int mHourIdx = helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_MARCH, cal);
        Assert.assertEquals(mHourIdx, hourIdx);
    }

    /**
     * SHOULD = Test the providerSchedulingHelper.modifyHourIdxForDST after transition at dst changing day of March",
     * EXPECTED TO = "returning hourinx - 1 "
     */
    public void testModifyHourIdxForDSTInMarch_after(){
        init();
        dstMarchDate_after();
        setDateAndHourIdx();

        final int mHourIdx = helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_MARCH, cal);
        Assert.assertEquals(mHourIdx, hourIdx-1);
    }

    /**
     * SHOULD = Test the providerSchedulingHelper.modifyHourIdxForDST before transition at dst changing date in November
     * EXPECTED TO = return the value of hourinx as is
     */
    public void testModifyHourIdxForDSTInNov_before(){
        init();
        dstNovDate_before();
        setDateAndHourIdx();

        final int mHourIdx = helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_NOV, cal);
        System.out.println("testModifyHourIdxForDSTInNov_beforeDemo() " + (mHourIdx == hourIdx ? "Passed Successfully." : "FAILED! Expected: " + mHourIdx + " --> actual: " + hourIdx));
    }

    /**
     * SHOULD = "Test the providerSchedulingHelper.modifyHourIdxForDST after transition at dst changing date in November",
     * EXPECTED TO = return value of hourinx + 1 hour
     */
//    public void testModifyHourIdxForDSTInNov_after(){
//        init();
//        dstNovDate_after();
//        setDateAndHourIdx();
//
//        final int mHourIdx =helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_NOV, cal);
//        Assert.assertEquals(mHourIdx, hourIdx+1);
//    }

    /**
     * SHOULD: Test the providerSchedulingHelper.modifyHourIdxForDST for a normal day.
     * EXPECTED TO: pass without throwing an exception>
     */
//    public void testModifyHourIdxForStandard(){
//        init();
//        standardDate();
//        setDateAndHourIdx();
//
//        final int mHourIdx =helper.modifyHourIdxForDST(tz, STANDARD_HOUR_BLOCK_NUMBER, cal);
//        Assert.assertEquals(mHourIdx, hourIdx);
//    }
    public static void main(String[] args) {
        ProviderSchedulingHelperTestDemo demo = new ProviderSchedulingHelperTestDemo();
        demo.testModifyHourIdxForDSTInNov_before();
    }
}
