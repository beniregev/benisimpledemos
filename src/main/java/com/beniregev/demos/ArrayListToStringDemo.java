package com.beniregev.demos;

import java.util.ArrayList;

public class ArrayListToStringDemo {
    public static void main(String[] args) {
        ArrayList<String[]> staffForPractice = new ArrayList<>();
        String[] parts = {"×ž×¨×¤×�×ª ×™×œ×“×™×�","×©×“×¨×•×ª ×”×™×œ×“ 4","×§×•×ž×” 7","×¨×ž×ª ×’×Ÿ","IL","52002","IL","×™×œ×“×™×�","IMG_PracticeLogo_ChildrensMedicalGroup.png","IMG_PracticeLogo_ChildrensMedicalGroup.jpg","childrensMedicalGroupiwIL","populator.demopractice.welcomeMessage_cmg","populator.demopractice.hours","SC_PEDIATRICS","DEFAULT","5200","TRUE","TRUE","TRUE","","","","",""};
        staffForPractice.add(parts);

        System.out.println(String.join(",", staffForPractice.get(0)));
    }
}
