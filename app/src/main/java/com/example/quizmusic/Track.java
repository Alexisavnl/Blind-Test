package com.example.quizmusic;

import java.io.Serializable;

public class Track implements Serializable {

    private String title;
    private String preview;
    private String artistName;
    private String cover;
    private String coverMax;

    public Track(String title, String preview, String artistName, String cover, String coverMax) {
        this.title = title;
        this.preview = preview;
        this.artistName = artistName;
        this.cover = cover;
        this.coverMax = coverMax;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCover() {
        return cover;
    }

    public String getCoverMax() {
        return coverMax;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCoverMax(String coverMax) {
        this.coverMax = coverMax;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", preview='" + preview + '\'' +
                ", artistName='" + artistName + '\'' +
                ", cover='" + cover + '\'' +
                ", coverMax='" + coverMax + '\'' +
                '}';
    }
}
