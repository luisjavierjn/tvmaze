package com.realpage.tvmaze.domain.consts;

public enum OrderBy {
    RATING("RATING"),
    CHANNEL("CHANNEL"),
    GENRE("GENRE"),
    NONE("NONE");

    private String value;

    OrderBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
