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

public class castParser extends DefaultHandler{

	List movies;
    List cats;
    List actors;
	
	private String tempVal;
	
	//to maintain context
	private Movie tempMov;
	private Cat tempCat;
	private Actor tempAct;
	
	public castParser(){
		movies = new ArrayList<Movie>();
		cats = new ArrayList<Cat>();
		actors = new ArrayList<Actor>();
	}
	
	public void run() {
		parseDocument();
		printData();
	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse("casts124.xml", this);
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
	private void printData(){
		
		//System.out.println("No of Movies '" + .size() + "'.");
		//System.out.println("No of cats '" + cats.size() + "'.");
		//System.out.println("No of movies '" + movies.size() + "'.");
		
		Iterator it = movies.iterator();
		while(it.hasNext()) {
			Movie m = ((Movie)it.next());
			ArrayList<String> m_actors = m.getActors();
			for(String a : m_actors)
			    System.out.println(a);
		}
	}
	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("filmc")) {
			//create a new instance of employee
			tempMov = new Movie();
			//tempEmp.setType(attributes.getValue("type"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("filmc")) {
			//add it to the list
			movies.add(tempMov);
			
		}else if (qName.equalsIgnoreCase("t")) {
			tempMov.setTitle(tempVal);
		}else if (qName.equalsIgnoreCase("a")) {
			tempMov.setActor(tempVal);
	    }	
	}
	
	public static void main(String[] args){
        castParser cParser = new castParser();
        cParser.run();
    
    }
}




