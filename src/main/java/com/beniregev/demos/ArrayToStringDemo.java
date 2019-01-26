package com.beniregev.demos;

import java.util.Arrays;

public class ArrayToStringDemo {

    public String arrayToString(String[] stringArray) {
        String arrayAsString = Arrays.toString(stringArray);
        return arrayAsString;
    }

    public static void main(String[] args) {
        String[] stringArray = { "en_US", "iw_IL", "es_ES", "ua_UA" };
        ArrayToStringDemo arrayToStringDemo = new ArrayToStringDemo();
        String arrayAsString = arrayToStringDemo.arrayToString(stringArray);
        System.out.println("arrayAsString = \"" + arrayAsString +"\"");

    }
}
