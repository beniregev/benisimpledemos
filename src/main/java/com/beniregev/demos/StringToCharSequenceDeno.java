package com.beniregev.demos;

import org.apache.commons.lang3.StringUtils;

public class StringToCharSequenceDeno {

    public void stringNotBlank() {
        String notBlank = "string";
        String blankString = "";

        System.out.println("String \"" + notBlank + "\" is" + (StringUtils.isNotBlank(notBlank) ? " not " : " ") +"blank");
        System.out.println("String \"" + blankString + "\" is" + (StringUtils.isNotBlank(blankString) ? " not " : " ") +"blank");
    }


    public static void main(String[] args) {
        StringToCharSequenceDeno demo = new StringToCharSequenceDeno();

        demo.stringNotBlank();

    }
}
