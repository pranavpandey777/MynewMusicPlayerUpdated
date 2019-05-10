package com.example.mynewmusicplayer;

public class Music {
    String title, artist, path;

    public Music(String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }


    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
