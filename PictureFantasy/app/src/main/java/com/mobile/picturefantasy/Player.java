package com.mobile.picturefantasy;

public class Player {
    private String name;
    private boolean ready;
    private int points;
    private boolean pictureTakenForRound;

    public Player(String name, boolean ready, int points, boolean pictureTakenForRound) {
        this.name = name;
        this.ready = ready;
        this.points = points;
        this.pictureTakenForRound = pictureTakenForRound;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.name = email;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPictureTakenForRound() {
        return pictureTakenForRound;
    }

    public void setPictureTakenForRound(boolean pictureTakenForRound) {
        this.pictureTakenForRound = pictureTakenForRound;
    }
}
