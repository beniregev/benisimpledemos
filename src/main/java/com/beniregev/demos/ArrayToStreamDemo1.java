package com.beniregev.demos;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class ArrayToStreamDemo1 {
    public static void main(String[] args) {

        String[] array = {"a", "b", "c", "d", "e"};
        Locale[] locales = {new Locale("ru_RU"), new Locale("en_US"), new Locale("iw_IL"), new Locale("es_ES")};
        String[] parts = {"×ž×¨×¤×�×ª ×™×œ×“×™×�","×©×“×¨×•×ª ×”×™×œ×“ 4","×§×•×ž×” 7","×¨×ž×ª ×’×Ÿ","IL","52002","IL","×™×œ×“×™×�","IMG_PracticeLogo_ChildrensMedicalGroup.png","IMG_PracticeLogo_ChildrensMedicalGroup.jpg","childrensMedicalGroupiwIL","populator.demopractice.welcomeMessage_cmg","populator.demopractice.hours","SC_PEDIATRICS","DEFAULT","5200","TRUE","TRUE","TRUE","","","","",""};
        Long[] longArray = { 1L, 3L, 7L, 17L, 5L, 2L, 4L, 12L, 8L, 9L };

        //Arrays.stream
        System.out.println("parts has " + parts.length + " items");
        int i = 0;
        final String practiceName = parts[i++];
        final String address1 = parts[i++].trim();
        final String address2 = parts[i++].trim();
        final String city = parts[i++].trim();
        final String state = parts[i++].trim();
        final String zipCode = parts[i++].trim();
        final String country = parts[i++].trim();
        final String shortName = parts[i++].trim();
        final String logoFileName = parts[i++].trim();
        final String blurredFileName = parts[i++].trim();
        final String virtualServerName = parts[i++].trim();
        final String welcomeMessageKey = parts[i++].trim();
        final String hoursKey = parts[i++].trim();
        final String subCat = parts[i++].trim();
        final String practiceTypeString = parts[i++].trim();
        final String practiceRank = parts[i++].trim();
        final String showAvailableNowTab = parts[i++].trim();
        final String showSchedulingTab = parts[i++].trim();
        final String practiceActive = parts[i++].trim();

        Stream<String> stream1 = Arrays.stream(parts);
        stream1.forEach(x -> System.out.println(x));

        //Stream.of
        Stream<String> stream2 = Stream.of(array);
        stream2.forEach(x -> System.out.println(x));

        Stream<Locale> localesStream1 = Arrays.stream(locales);
        Optional<Locale> result = localesStream1.filter(x -> x.toString().equalsIgnoreCase("iw_iL")).findAny();
        System.out.println(result.isPresent() ? "Locale \"" + result.get().toString() + "\" found" : "Locale \"iw_IL\" not found in Locales list." );

        //Stream.of
        Stream<Locale> localesStream2 = Stream.of(locales);
        localesStream2.forEach(x -> System.out.println(x));

        //Stream<Long> longStream = Stream.of(longArray);
        //longStream.forEach(x -> System.out.println(x));
        Stream.of(longArray).forEach(x -> System.out.println(x));
    }
}
