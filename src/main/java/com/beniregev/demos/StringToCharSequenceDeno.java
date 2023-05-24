package com.beniregev.demos;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class StringToCharSequenceDeno {

    public void stringNotBlank() {
        String notBlank = "string";
        String blankString = "";

        System.out.println("String \"" + notBlank + "\" is" + (StringUtils.isNotBlank(notBlank) ? " not " : " ") +" blank");
        System.out.println("String \"" + blankString + "\" is" + (StringUtils.isNotBlank(blankString) ? " not " : " ") +" blank");
    }

    public void stringGetBytes() {
        String[] strings = { "דלחכגעלדחךגכע",
                "slhad אטרגצכפםם"
        };

        byte[] bytes = strings[0].getBytes(StandardCharsets.UTF_8);
        System.out.println("string[0] = " + strings[0]);
//        for(int i = 0; i<strings.length; i++) {
//
//        }
    }
    public static void main(String[] args) {
        StringToCharSequenceDeno demo = new StringToCharSequenceDeno();

        demo.stringNotBlank();

    }
}
