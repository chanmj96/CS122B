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

public class xmlParser extends DefaultHandler{

	List movies;
    List cats;
    List actors;
	
	private String tempVal;
	
	//to maintain context
	private Movie tempMov;
	private Cat tempCat;
	private Actor tempAct;
	
	public xmlParser(){
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
			
			//parse the file and also register this class for call backs
			sp.parse("actors63.xml", this);
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
		
		System.out.println("No of actors '" + actors.size() + "'.");
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
            CallableStatement cs = dbcon.prepareCall("{CALL add_star(?,?,?)");

    		Iterator it = actors.iterator();
	    	while(it.hasNext()) {
		    	Actor a = (Actor)it.next();
                cs.setString(1, a.getName());
                cs.setInt(2, a.getDOB());
                cs.registerOutParameter(3,java.sql.Types.VARCHAR);
                cs.addBatch();
		    }
            iNoRows = cs.executeBatch();
            dbcon.commit();
            System.out.println("Successfully inserted Actors.");
            cs.close();
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
		if(qName.equalsIgnoreCase("Actor")) {
			//create a new instance of employee
			tempAct = new Actor();
			//tempEmp.setType(attributes.getValue("type"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("Actor")) {
			//add it to the list
			actors.add(tempAct);
			
		}else if (qName.equalsIgnoreCase("stagename")) {
			tempAct.setName(tempVal);
		}else if (qName.equalsIgnoreCase("dob")) {
			tempAct.setDOB(tempVal);
		}
		
	}
	
	public static void main(String[] args){
		xmlParser xParse = new xmlParser();
		xParse.run();
        
        //movieParser mParse = new movieParser();
        //mParse.run();
        
        //castParser cParse = new castParser();
        //cParse.run();
    }	
	
}




