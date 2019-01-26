package com.beniregev.ui.controller.helper;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.TimeZone;

public class ProviderSchedulingHelper {

    /**
     * Can be 0 - 23. Represents the first hour displayed when provider has no availability or appointments scheduled.
     */
    //public static final Log log = LogFactory.getLog(ProviderSchedulingHelper.class);

    private static final int DAILY_START_HOUR = 9;
    private final int DST_HOUR_BLOCK_NUMBER_IN_MARCH = 23;
    private final int DST_HOUR_BLOCK_NUMBER_IN_NOV = 25;

    /**
     * calibrate the corresponding hour index in blocks array based on the daylight savings
     *
     * @param userTimeZone time zone for user
     * @param blocksSize size in blocks alloted for an appointment
     * @param appointmentDate appointment date
     * @return the hour index after calibrating on daylight saving
     */
    public int modifyHourIdxForDST(final TimeZone userTimeZone, final int blocksSize, final Calendar appointmentDate ){
        int hourIdx = appointmentDate.get(Calendar.HOUR_OF_DAY);

        //OC-18562 due to the daylight saving, March will have one hour less and Nov has one hour more in the blocks
        //final int blocksSize = blocks.size();
        //In March x of DST changing day, there is no 2:00 am
        if(blocksSize == DST_HOUR_BLOCK_NUMBER_IN_MARCH && hourIdx >=2){
            hourIdx--;
        }else if(blocksSize == DST_HOUR_BLOCK_NUMBER_IN_NOV){
            //In Nov x of DST changing day, there are two 1:00 am.
            final boolean isDaylightTime = userTimeZone.inDaylightTime( appointmentDate.getTime() );
            if((hourIdx ==1 && !isDaylightTime) || hourIdx >=2) {
                hourIdx++;
            }
        }
        return hourIdx;
    }
}
