package com.example.quizmusic;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

    private String pseudo;
    private int correctCount;
    private int duration;
    private String artist;

    public Score(String pseudo, int correctCount, int duration, String artist) {
        this.pseudo = pseudo;
        this.correctCount = correctCount;
        this.duration = duration;
        this.artist = artist;
    }

    public Score(){

    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public int compareTo(Score o) {
        if((o.correctCount - this.correctCount) == 0){
            return (this.duration - o.duration);
        }else {
            return (o.correctCount - this.correctCount);
        }

    }
}
