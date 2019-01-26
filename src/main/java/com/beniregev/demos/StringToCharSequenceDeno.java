package com.beniregev.demos;

import org.apache.commons.lang3.StringUtils;

public class StringToCharSequenceDeno {
    public static void main(String[] args) {
        String notBlank = "string";
        String blankString = "";

        System.out.println("String \"" + notBlank + "\" is" + (StringUtils.isNotBlank(notBlank) ? " not " : " ") +"blank");
        System.out.println("String \"" + blankString + "\" is" + (StringUtils.isNotBlank(blankString) ? " not " : " ") +"blank");
    }
}
