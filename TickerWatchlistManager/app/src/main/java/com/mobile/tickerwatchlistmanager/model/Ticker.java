package com.mobile.tickerwatchlistmanager.model;

public class Ticker {
    private String name;

    public Ticker (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String string = name;
        return string;
    }
}
