import java.util.ArrayList;
public class Movie{

    private String title;
    private int year;
    private String director;
    private ArrayList cat;
    private ArrayList actor;
    public Movie() {
        title = "";
        year = -1;
        director = "";
        cat = new ArrayList<String>();
        actor = new ArrayList<String>();
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
        actor.add(s);
    }
    public void setCat(String s)
    {
        if(s.equals(""))
            return;
        cat.add(s);
    }
    public String getTitle(){ return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public ArrayList<String> getCats() { return cat; }
    public ArrayList<String> getActors() { return actor; }
}
