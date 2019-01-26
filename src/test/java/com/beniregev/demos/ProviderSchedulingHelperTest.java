package com.beniregev.demos;

import com.beniregev.test.rule.*;
import com.beniregev.ui.controller.helper.ProviderSchedulingHelper;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class ProviderSchedulingHelperTest {
    private final int STANDARD_HOUR_BLOCK_NUMBER = 24;
    private final int DST_HOUR_BLOCK_NUMBER_IN_MARCH = 23;
    private final int DST_HOUR_BLOCK_NUMBER_IN_NOV = 25;

    private final String ZONE_ID = "America/New_York";
    private final TimeZone tz = TimeZone.getTimeZone(ZONE_ID);
    int hourIdx;
    Calendar cal;

    @TestFixture("date")
    Date date;
    @Rule
    public final FixtureRule fixtureRule = new FixtureRule(this);
    @DocHandler
    private static final TestDocumenter DOCUMENTER = TestDocumenterFactory.getDefault(ProviderSchedulingHelperTest.class);

    @TestFixture("helper")
    private ProviderSchedulingHelper helper;

    @MockDoc("Initialize")
    public void init() {
        helper = new ProviderSchedulingHelper();
        cal = Calendar.getInstance();
    }

    @MockDoc("NovDate_before")
    public void dstNovDate_before() {
        date = new DateTime(2018, 11, 4, 1, 0, 0, 0).toDate();
    }

    @MockDoc("NovDate_after")
    public void dstNovDate_after() {
        date = new DateTime(2018, 11, 4, 10, 0, 0, 0).toDate();
    }

    @MockDoc("StandardDate")
    public void standardDate() {
        date = new DateTime(2018, 1, 11, 1, 0, 0, 0).toDate();
    }

    @MockDoc("StandardDate")
    public void setDateAndHourIdx() {
        cal.setTime(date);
        hourIdx = cal.get(Calendar.HOUR_OF_DAY);
    }

    @TestDoc(should = "Test the providerSchedulingHelper.modifyHourIdxForDST before transition "
            + "at dst changing date in November",
            expectedTo = "returning hourinx as usual")
    @FixtureConfig({"init", "dstNovDate_before","setDateAndHourIdx"})
    @Test
    public void testModifyHourIdxForDSTInNov_before() {
        final int mHourIdx =helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_NOV, cal);
        Assert.assertEquals(mHourIdx, hourIdx);
    }

    public void testModifyHourIdxForDSTInNov_beforeDemo() {
        final int mHourIdx =helper.modifyHourIdxForDST(tz, DST_HOUR_BLOCK_NUMBER_IN_NOV, cal);
        System.out.println("testModifyHourIdxForDSTInNov_beforeDemo() " + (mHourIdx == hourIdx ? "Passed Successfully." : "FAILED! Expected: " + mHourIdx + " --> actual: " + hourIdx));
    }

    public static void main(String[] args) {
        ProviderSchedulingHelperTest test = new ProviderSchedulingHelperTest();

        test.init();
        test.dstNovDate_before();
        test.setDateAndHourIdx();
        test.testModifyHourIdxForDSTInNov_beforeDemo();
    }
}
