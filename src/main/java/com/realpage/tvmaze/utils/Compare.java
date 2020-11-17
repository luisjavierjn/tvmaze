package com.realpage.tvmaze.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.realpage.tvmaze.domain.consts.Orientation;

public class Compare {

    private Compare() {}

    public static boolean Doubles(double a, double b, Orientation o) {
        if(o == Orientation.ASC) return (a <= b);
        else return (a >= b);
    }

    public static boolean Strings(String a, String b, Orientation o) {
        if(o == Orientation.ASC) return (a.compareToIgnoreCase(b) <= 0);
        else return (a.compareToIgnoreCase(b) >= 0);
    }

    public static boolean JNodeArrays(JsonNode a, JsonNode b, Orientation o) {
        if(a.size() == 0 && b.size() == 0) {
            return true;
        }

        if(o == Orientation.ASC) {
            if(a.size() == 0) return true;
            else if(b.size() == 0) return false;
        }

        if(o == Orientation.DESC) {
            if(b.size() == 0) return true;
            else if(a.size() == 0) return false;
        }

        boolean resp = false;
        for(int i=0; i<a.size(); i++) {
            int count = 0;
            for(int j=0; j<b.size(); j++) {
                if(Strings(a.get(i).asText(),b.get(j).asText(),o)) {
                    count++;
                }
            }
            if(count == b.size()) {
                resp = true;
                break;
            }
        }
        return resp;
    }
}
