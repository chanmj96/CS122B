import java.util.ArrayList;
public class Movie{

    private String title;
    private int year;
    private String director;
    private String cat; 
    private String actor;
    public Movie() {
        title = "";
        year = -1;
        director = "";
<<<<<<< HEAD
        cat = "";//cat = new ArrayList<String>();
        actor = "";//actor = new ArrayList<String>();
=======
        cat = "";
        actor = "";
>>>>>>> f8bb53cf08f587a0f9d4d850b03f38748552efa5
    }

    public void setTitle(String t)
    {
        title = t;
    }
    public void setYear(String s)
    {
        if( s == null || s.equals(""))
            year = -1;
        else
            try{
                year = Integer.parseInt(s);
            }
            catch(Exception e) {year = -1;}
    }
    public void setDirector(String s)
    {
        director = s;
    }
    public void setActor(String s)
    {
        if(s.equals("sa") || s.equals("s a") || s.equals(""))
            return;
        actor = s;
    }
    public void setCat(String s)
    {
        if(s.equals(""))
            return;
        cat = s;
    }
    public String getTitle(){ return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public String getCats() { return cat; }
    public String getActors() { return actor; }
}
