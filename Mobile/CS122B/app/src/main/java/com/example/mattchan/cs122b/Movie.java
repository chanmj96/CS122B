package com.example.mattchan.cs122b;

/**
 * Created by Sam on 3/6/18.
 */

public class Movie {
    private String id;
    private String title;
    private String year;
    private String director;
    private String cast = "";
    private String genres = "";

    public Movie(String id, String title, String year, String director, String cast, String genres){
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;

        String[] clist = cast.split(",");
        System.out.println("HERE: " + cast);
        for(int j = 0; j < clist.length; ++j){
            String person = clist[j].split(":")[0];
            System.out.println("Person: " + person);
            this.cast += person;
            if(j + 1 < clist.length){
                this.cast += ", ";
            }
        }

        String[] glist = genres.split(",");
        for(int j = 0; j < glist.length; ++j){
            this.genres += glist[j];
            if(j + 1 < glist.length){
                this.genres += ", ";
            }
        }
    }

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getYear(){
        return year;
    }
    public String getDirector(){
        return director;
    }
    public String getCast(){
        return cast;
    }
    public String getGenres(){
        return genres;
    }
}
