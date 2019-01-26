package com.beniregev.demos;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Binyamin Regev
 * @version 1.0.00
 * @see Matcher
 * @see Pattern
 */
public class MatchingAnyCurrencySymbolUsingJavaRegExDemo {
    public static void main(String[] args) {
        // region Charactersets
        String hebrewAlephBet = "אבגדהוזחטיכךלמםנןסעפףצץקרשת";
        Charset UTF8_CHARSET = Charset.forName("UTF-8");
        SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();

        if (availableCharsets != null) {
            Set<String> characterSets = availableCharsets.keySet();

            System.out.println("******* Scanning through Set<String> using Stream() *******");
            characterSets.stream().forEach(cs -> System.out.println(cs));


            System.out.println("******* Scanning through Set<String> using Iterator<String> *******");
            Iterator<String> it = characterSets.iterator();
            while(it.hasNext()){
                System.out.println(it.next());
            }
        }
        // endregion

        // region Find the symbols for currencies
        String content = "Let's find the symbols for currencies : $ United States Dollar (USD), € Euro (EUR), ¥ Jappanese Yen(JPY), £ Great Briten Paund (GBP)";

        String regex = "\\p{Sc}";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find())
        {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end() + " ");
            System.out.println(" : " + matcher.group());
        }
        // endregion



    }
}
