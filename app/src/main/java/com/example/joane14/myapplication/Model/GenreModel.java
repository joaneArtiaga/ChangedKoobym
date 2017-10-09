package com.example.joane14.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Joane14 on 30/07/2017.
 */

public class GenreModel implements Serializable{

    String genreName;
    long genreId;
    boolean isSelected;

    public GenreModel(){

    }

    public GenreModel(String genreName, long genreId){
        this.genreName = genreName;
        this.genreId = genreId;
    }

    public String getGenreName(){
        return this.genreName;
    }

    public long getGenreId(){
        return this.genreId;
    }

    public void setGenreName(String genreName){
        this.genreName = genreName;
    }

    public void setGenreId(long genreId){
        this.genreId = genreId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
