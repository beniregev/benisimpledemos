package com.beniregev.demos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StreamsFilterDemo {
    class PracticeName {
        private Long id;
        private String name;
        private Locale locale;

        public PracticeName(Long id, String name, Locale locale) {
            this.id = id;
            this.name = name;
            this.locale = locale;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }

    public static final Locale LOCALE_EN_US = new Locale("en", "US");
    public static final Locale LOCALE_IW_IL = new Locale("iw", "IL");
    public static final Locale LOCALE_ES_US = new Locale("es", "US");
    public static final Locale LOCALE_ES_ES = new Locale("es", "ES");
    public static final Locale LOCALE_RU_IL = new Locale("ru", "IL");

//    public static void main(String[] args) {
//        List<PracticeName> practicesNamesList = new ArrayList<>();
//        practicesNamesList.add(new PracticeName(1L, "practiceName01 en_US", LOCALE_EN_US));
//
//        practicesNamesList.add(new PracticeName(2L, "practiceName02 en_US", LOCALE_EN_US));
//        practicesNamesList.add(new PracticeName(2L, "practiceName02 iw_IL", LOCALE_IW_IL));
//
//        practicesNamesList.add(new PracticeName(3L, "practiceName03 en_US", LOCALE_EN_US));
//        practicesNamesList.add(new PracticeName(3L, "practiceName03 iw_IL", LOCALE_IW_IL));
//        practicesNamesList.add(new PracticeName(3L, "practiceName03 es_ES", LOCALE_ES_US));
//
//        practicesNamesList.add(new PracticeName(4L, "practiceName04 en_US", LOCALE_EN_US));
//        practicesNamesList.add(new PracticeName(4L, "practiceName04 iw_IL", LOCALE_IW_IL));
//        practicesNamesList.add(new PracticeName(4L, "practiceName04 es_US", LOCALE_ES_US));
//        practicesNamesList.add(new PracticeName(4L, "practiceName04 es_ES", LOCALE_ES_ES));
//
//        practicesNamesList.add(new PracticeName(5L, "practiceName05 en_US", LOCALE_EN_US));
//        practicesNamesList.add(new PracticeName(5L, "practiceName05 iw_IL", LOCALE_IW_IL));
//        practicesNamesList.add(new PracticeName(5L, "practiceName05 es_US", LOCALE_ES_US));
//        practicesNamesList.add(new PracticeName(5L, "practiceName05 es_ES", LOCALE_ES_ES));
//        practicesNamesList.add(new PracticeName(5L, "practiceName05 ru_IL", LOCALE_RU_IL));
//
//        practicesNamesList.stream()
//                .filter(p -> {
//                    if ("iw_IL".equals(p.getLocale().toString())) {
//                        return
//                    }
//                })
//    }
}
