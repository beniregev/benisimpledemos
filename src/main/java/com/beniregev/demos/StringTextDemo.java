package com.beniregev.demos;

import org.apache.commons.lang3.StringUtils;

public class StringTextDemo {
    private static final String REGEX_PATTERN1 = "\\w+";
    private static final String REGEX_PATTERN2 = "[\\w-_'\\.]+";
    private static final String REGEX_PATTERN3 = "[A-Za-z0-9-_']+";

    private String str;


    public StringTextDemo(String str) {
        this.str = str;
    }

    public String getREGEX_PATTERN1() {
        return REGEX_PATTERN1;
    }

    public String getREGEX_PATTERN2() {
        return REGEX_PATTERN2;
    }

    public String getREGEX_PATTERN3() {
        return REGEX_PATTERN3;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public static void main(String[] args) {
        StringTextDemo stringTextDemo = new StringTextDemo("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij.klmnopqrstuvwxyz0123456789'");

        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN1));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN2));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN3));

        System.out.println("-------------------------------------------------");

        stringTextDemo.setStr("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890._-");
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN1));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN2));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN3));

        System.out.println("-------------------------------------------------");

        stringTextDemo.setStr("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzבנירגב");

        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN1));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN2));
        System.out.println(stringTextDemo.getStr().matches(REGEX_PATTERN3));
    }
}
