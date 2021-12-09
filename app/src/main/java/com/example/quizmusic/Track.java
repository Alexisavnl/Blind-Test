package com.example.quizmusic;

public class Track {

    private String title;
    private String preview;
    private String cover;

    public Track(String title, String preview,String cover) {
        this.title = title;
        this.preview = preview;
        this.cover = cover;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
