package com.example.dimondeyejava;

public class Video {
    public String name;
    public String urlPath;
    public String status;
    public int layoutId;

    public Video(){

    }

    public Video(String name, String urlPath, String status){
        this.name = name;
        this.urlPath = urlPath;
        this.status = status;
    }

    public Video(String name, String status){
        this.name = name;
        this.status = status;
    }

    public Video(String name, String status, int layoutId){
        this.name = name;
        this.status = status;
        this.layoutId = layoutId;
    }
}
