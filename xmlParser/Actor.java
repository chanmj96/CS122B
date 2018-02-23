import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;


public class Actor {
    private String name;
    private int dob;

    public Actor(){}

    public void setName(String new_name){
        name = new_name;
    }
    public String getName(){
        return name;
    }
    public void setDOB(String s){
        if(s == null || s.equals(""))
        {
            dob = -1;
            return;
        }
        try{
            dob = Integer.parseInt(s);
        }
        catch(Exception e) { dob = -1;}
    }
    public int getDOB(){
        return dob;
    }

}
