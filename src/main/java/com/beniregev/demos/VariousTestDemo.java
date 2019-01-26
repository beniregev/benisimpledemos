package com.beniregev.demos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class VariousTestDemo {
    private final Locale locale = new Locale( "iw_IL");
    private final HashMap<String, ArrayList<String[]>> practicePatients = new HashMap<String, ArrayList<String[]>>();

    //private ArrayList<String>
    final ArrayList<String[]> practicePatientsForLocale = practicePatients.get(locale.toString());
    final String[][] practicePatientsArray = practicePatientsForLocale.toArray(new String[practicePatients.size()][]);

    public VariousTestDemo() {
    }

    public static void main(String[] args) {

    }
}
