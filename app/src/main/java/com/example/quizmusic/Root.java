package com.example.quizmusic;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Root {
    @SerializedName("data")
    private ArrayList<Artist> artists;

    public Root(ArrayList<Artist> artists) {
        this.artists = artists;
        System.out.println("je suis dans root");
    }

    @Override
    public String toString() {
        return "Root{" +
                "artists=" + artists +
                '}';
    }


    public ArrayList<Artist> getArtists() {
        return artists;
    }
}
