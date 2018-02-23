public class Movie{

    private String title;
    private int year;
    private String director;
    private String cat;
    private String actor;
    public Movie() {}

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
        actor = s;
    }
    public String getTitle(){ return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public String getCat() { return cat; }
    public String getActor() { return actor; }
}
