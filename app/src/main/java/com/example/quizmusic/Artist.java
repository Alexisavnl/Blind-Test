package com.example.quizmusic;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Artist implements Serializable{

    private int id;
    private String name;
    private String picture_small;
    private int nb_fan;
    private List<Artist> artists;
    private boolean selected = false;

    public Artist(int id, String name, String picture_small, int nb_fan) {
        this.id = id;
        this.name = name;
        this.picture_small = picture_small;
        this.nb_fan = nb_fan;
        artists = new ArrayList<>();
        System.out.println(id+name+picture_small+nb_fan);
        artists.add(this);
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture_small() {
        return picture_small;
    }

    public int getNb_fan() {
        return nb_fan;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void isSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected(){
        return selected;
    }
    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", artistName='" + name + '\'' +
                ", cover='" + picture_small + '\'' +
                ", followers='" + nb_fan + '\'' +
                '}';
    }

}
