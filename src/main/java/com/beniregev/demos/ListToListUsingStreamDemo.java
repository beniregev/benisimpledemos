package com.beniregev.demos;

import java.util.*;
import java.util.stream.Collectors;

public class ListToListUsingStreamDemo {
    static class State {
        private String code;
        private String name;
        private boolean legalResidence;
        private Locale locale;

        public State(String code, String name) {
            this(code, name, false, new Locale("en", "US"));
        }

        public State(String code, String name, boolean legalResidence, Locale locale) {
            this.code = code;
            this.name = name;
            this.legalResidence = legalResidence;
            this.locale = locale;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public boolean isLegalResidence() {
            return legalResidence;
        }

        public void setLegalResidence(boolean legalResidence) {
            this.legalResidence = legalResidence;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return legalResidence == state.legalResidence &&
                    Objects.equals(code, state.code) &&
                    Objects.equals(name, state.name) &&
                    Objects.equals(locale, state.locale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, name, legalResidence, locale);
        }
    }

    public static void main(String[] args) {
        List<State> states = new ArrayList<State>();
        states.add(new State("IL", "Israel", true, new Locale("iw", "IL")));
        states.add(new State("UA", "Ukraine"));
        states.add(new State("RU", "Russia"));
        states.add(new State("IT", "Italy"));
        states.add(new State("ES", "Spain"));
        states.add(new State("CY", "Cyprus"));
        states.add(new State("GB", "England"));
        states.add(new State("CA", "Canada"));

        List<String> statesCodes = states.stream().map(State::getCode).collect(Collectors.toList());
        System.out.println("statesCodes = " + statesCodes.stream().collect(Collectors.joining(", ")));
    }
}
