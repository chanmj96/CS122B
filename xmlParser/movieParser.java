import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import org.xml.sax.helpers.DefaultHandler;

public class movieParser extends DefaultHandler{

	List movies;
    List cats;
    List actors;
	
	private String tempVal;
	
	//to maintain context
	private Movie tempMov;
	private Cat tempCat;
	private Actor tempAct;
	
	public movieParser(){
		movies = new ArrayList<Movie>();
		cats = new ArrayList<Cat>();
		actors = new ArrayList<Actor>();
	}
	
	public void run() {
		parseDocument();
		insertIntoDB();
	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			File f = new File("xmlParser/mains243.xml");
			//parse the file and also register this class for call backs
			sp.parse(f, this);
			//sp.parse("casts124.xml", this);
			//sp.parse("mains243.xml", this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Iterate through the list and print
	 * the contents
	 */
private void insertIntoDB(){
		
		System.out.println("No of movies '" + movies.size() + "'.");
		//System.out.println("No of cats '" + cats.size() + "'.");
		//System.out.println("No of movies '" + movies.size() + "'.");
	    try{
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        String loginUser = "testuser";
		    String loginPasswd = "password";
		    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		    int[] iNoRows = null;
		
		    Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            dbcon.setAutoCommit(false);
            CallableStatement cStmt = dbcon.prepareCall("{CALL add_movie(?,?,?,?,?)}");

    		Iterator it = movies.iterator();
	    	while(it.hasNext()) {
		    	Movie a = (Movie)it.next();
		    	String title = a.getTitle();
		    	int year = a.getYear();
		    	String sname = a.getActors();
		    	String gname = a.getCats();
		    	String dir = a.getDirector();
		    	cStmt.setString(1, title);
    			cStmt.setInt(2,  year);	
	    	    
	    		cStmt.setString(3, dir);
	    		cStmt.setString(4, sname);
	    		cStmt.setString(5, gname);
	    		cStmt.addBatch();
            iNoRows = cStmt.executeBatch();
            dbcon.commit();
	    	}
            System.out.println("Successfully inserted Movies.");
            cStmt.close();
            dbcon.close();
		}
		catch(SQLException e)
		{
		    e.printStackTrace();
	    }
	    catch (java.lang.Exception ex) {
            ex.printStackTrace();
	    }
	}
	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("film")) {
			//create a new instance of employee
			tempMov = new Movie();
			//tempEmp.setType(attributes.getValue("type"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("film")) {
			//add it to the list
			movies.add(tempMov);
			
		}else if (qName.equalsIgnoreCase("t")) {
			tempMov.setTitle(tempVal);
		}else if (qName.equalsIgnoreCase("year")) {
			tempMov.setYear(tempVal);
		}else if (qName.equalsIgnoreCase("dirn")) {
		    tempMov.setDirector(tempVal);
		}else if (qName.equalsIgnoreCase("cats")) {
            tempMov.setCat(tempVal);
		}
		
	}
	
	public static void main(String[] args){
        movieParser mParser = new movieParser();
        mParser.run();
    
    }
}




