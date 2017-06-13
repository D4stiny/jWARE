package jWARE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebManager {
	/* Default Constructor, no need for any parameters */
	WebManager() {
		
	}
	
	/* Handles making a request to a URL based on the METHOD specified (ex: url - google.com, method - GET) */
	String request(String URL, String METHOD) throws Exception {
		URL url = new URL(URL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    System.setProperty("http.agent", "JWARE-0.0.1"); 
	    conn.setRequestMethod(METHOD);
	    
	    BufferedReader readStream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String response = "", responseLine = "";
	    
	    while ((responseLine = readStream.readLine()) != null) {
	    	response += responseLine;
	    }
	    
	    readStream.close();
	    
	    return response;
	}
}
