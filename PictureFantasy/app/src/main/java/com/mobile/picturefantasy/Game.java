package com.mobile.picturefantasy;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Game {

    private String game;
    private String gameID;
    private ArrayList<Player> players;
    private boolean gameStarted;
    private String host;
    private String picturePicker;
    private int currentRound;
    private int maxRounds;
    private String currentPicture;
    private boolean picturePickedForRound;

    public Game(String game, String gameID, ArrayList<Player> players, boolean gameStarted, String host, String picturePicker, int currentRound, int maxRounds, String currentPicture, boolean picturePickedForRound) {
        this.game = game;
        this.gameID = gameID;
        this.players = players;
        this.gameStarted = gameStarted;
        this.host = host;
        this.picturePicker = picturePicker;
        this.currentRound = currentRound;
        this.maxRounds = maxRounds;
        this.currentPicture = currentPicture;
        this.picturePickedForRound = picturePickedForRound;

    }

    public String getGame() {
        return game;
    }

    public String getGameID() {
        return gameID;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPicturePicker() {
        return picturePicker;
    }

    public void setPicturePicker(String picturePicker) {
        this.picturePicker = picturePicker;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public String getCurrentPicture() {
        return currentPicture;
    }

    public void setCurrentPicture(String currentPicture) {
        this.currentPicture = currentPicture;
    }

    public boolean isPicturePickedForRound() {
        return picturePickedForRound;
    }

    public void setPicturePickedForRound(boolean picturePickedForRound) {
        this.picturePickedForRound = picturePickedForRound;
    }
}
