package com.realpage.tvmaze.domain.consts;

public enum Orientation {
    ASC("ASC"),
    DESC("DESC");

    private String value;

    Orientation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
