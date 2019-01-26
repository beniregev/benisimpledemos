package com.beniregev.demos;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONArrayDemo {
    public JSONArray initJSONArray() {
        String stringJSONArray = "[{\"engagementId\":\"FDaBOfMMREHYDVIJDUZUVSWTHLXIEDDECPTUHTXbPFadARNEVTSWEWBTHEWMEJOPQXHfGYKMfNOYE\"," +
                "\"entryAction\":\"SCHEDULED_APPOINTMENT\",\"isCancelEnabled\":true,\"optionalText\":\"\"," +
                "\"accountId\":\"QAbBZJOXJMAKfKWcXBWecKXCBFKKBZEPZFJBSGVbBcZZTFTKJfNSMTEOLLSLaSCSYSeHGYRaMGYcG\",\"userName\":\"Mary Jones\"," +
                "\"providerType\":\"Family Doctor\",\"scheduledDateOnly\":\"01/01/2019\",\"scheduledTimeOnly\":\"6:30 PM IST\"," +
                "\"scheduledStartDateTime\":1546360200000,\"checkId\":\"4028b881680a157f01680a3260f40036\"," +
                "\"notificationId\":\"EKHUcCTFWeOWETEREHOcGOQEAVKfPXBFGEcbdDNKaReGHRIWfeBMLVWYOCDVCTNIBcFXXXOZAWNJA\"," +
                "\"name\":\"ScheduledEngagementStartEvent\",\"type\":\"nonModal\",\"created\":1546359300340,\"lifespan\":1800,\"delay\":1042822}]";
        JSONArray jsonArray = JSONArray.fromObject(stringJSONArray);
        return jsonArray;
    }

    public JSONArray buildJsonArray() {
        JSONObject item = new JSONObject();
        item.put("engagementId", "FDaBOfMMREHYDVIJDUZUVSWTHLXIEDDECPTUHTXbPFadARNEVTSWEWBTHEWMEJOPQXHfGYKMfNOYE");
        item.put("entryAction", "SCHEDULED_APPOINTMENT");
        item.put("isCancelEnabled", true);
        item.put("optionalText", "");
        item.put("accountId", "QAbBZJOXJMAKfKWcXBWecKXCBFKKBZEPZFJBSGVbBcZZTFTKJfNSMTEOLLSLaSCSYSeHGYRaMGYcG");
        item.put("userName", "Benny Regev");
        item.put("providerType", "Family Doctor");
        item.put("scheduledDateOnly", "01/01/2019");
        item.put("scheduledTimeOnly", "6:30 PM IST");
        item.put("scheduledStartDateTime", 1546360200000L);
        item.put("checkId", "4028b881680a157f01680a3260f40036");
        item.put("notificationId", "EKHUcCTFWeOWETEREHOcGOQEAVKfPXBFGEcbdDNKaReGHRIWfeBMLVWYOCDVCTNIBcFXXXOZAWNJA");
        item.put("name", "ScheduledEngagementStartEvent");
        item.put("type", "nonModal");
        item.put("created", 1546359300340L);
        item.put("lifespan", 1800);
        item.put("delay", "1042822");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(item);
        return jsonArray;
    }

    public JSONArray getJsonArrayWithAdditionalList(final JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        list.add("1 --> \"hideCancelAppointmentLink\": true");
        list.add("2 --> \"hideCancelAppointmentLink\": false");
        list.add("3 --> \"hideCancelAppointmentLink\": true");
        list.add("4 --> \"hideCancelAppointmentLink\": true");
        list.add("5 --> \"hideCancelAppointmentLink\": false");
        list.add("6 --> \"hideCancelAppointmentLink\": false");
        list.add("7 --> \"hideCancelAppointmentLink\": true");

        jsonArray.add(list);
        return jsonArray;
    }

    public JSONArray getJsonArrayWithAdditionalMap(final JSONArray array) {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("1", Boolean.TRUE);
        map.put("2", Boolean.FALSE);
        map.put("3", Boolean.TRUE);
        map.put("4", Boolean.TRUE);
        map.put("5", Boolean.FALSE);
        map.put("6", Boolean.FALSE);
        map.put("7", Boolean.TRUE);

        array.add(map);
        return array;

    }

    public static void main(final String[] args) {
        JSONArrayDemo demo = new JSONArrayDemo();

        JSONArray jsonArrayFromString = demo.initJSONArray();
        JSONArray jsonArrayWithList = jsonArrayWithList = demo.getJsonArrayWithAdditionalList(jsonArrayFromString);
        System.out.println("JSONArray initJSONArray()  = \"" + jsonArrayFromString + "\"");
        System.out.println("JSONArray getJsonArrayWithAdditionalList(JSONArray) = \"" + jsonArrayWithList + "\"");
        System.out.println("jsonArrayFromString.length()=" + jsonArrayFromString.toString().length() + " ; jsonArrayWithList.length() = " + jsonArrayWithList.toString().length()) ;
        System.out.println("jsonArrayWithList.contains('hideCancelAppointmentLink')=" + jsonArrayWithList.toString().contains("hideCancelAppointmentLink"));

        JSONArray jsonArray = demo.buildJsonArray();
        JSONArray jsonArrayWithMap = demo.getJsonArrayWithAdditionalMap(jsonArray);
        System.out.println("JSONArray buildJsonArray() = \"" + jsonArray + "\"");
        System.out.println("JSONArray getJsonArrayWithAdditionalMap(JSONArray) = \"" + jsonArrayWithMap + "\"");
        System.out.println("jsonArray.length()=" + jsonArray.toString().length() + " ; jsonArrayWithMap.length() = " + jsonArrayWithMap.toString().length()) ;
    }
}
