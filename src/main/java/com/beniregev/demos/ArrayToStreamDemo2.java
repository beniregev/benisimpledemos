package com.beniregev.demos;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class ArrayToStreamDemo2 {
    public static void main(String[] args) {

        Locale[] locales = {new Locale("ru_RU"), new Locale("en_US"), new Locale("iw_IL"), new Locale("es_ES")};

        //Arrays.stream
        Stream<Locale> localesStream1 = Arrays.stream(locales);
        Optional<Locale> result = localesStream1.filter(x -> x.toString().equalsIgnoreCase("iw_iL")).findAny();
        System.out.println(result.isPresent() ? "Locale \"" + result.get().toString() + "\" found" : "Locale \"iw_IL\" not found in Locales list." );

        //Stream.of
        Stream<Locale> localesStream2 = Stream.of(locales);
//        System.out.print("Locales list: { ");
//        localesStream2.forEach(x -> System.out.print(x + ", "));
//        System.out.println("}");

        boolean exists = localesStream2.filter(x -> x.toString().equalsIgnoreCase("iw_iL")).findAny().isPresent();
        System.out.println(exists ? "Locale was found in Locales list." : "Locale was not found in Locales list." );

    }
}
