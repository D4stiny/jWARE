package jWARE;

public class Controller {
	private WebManager webManager = new WebManager();
	private String controlURL = "";
	
	/* Default controller, creates a new web manager for use with communicating with the Control server */
	Controller(String urlServer) {
		webManager = new WebManager();
		controlURL = urlServer;
	}
	
	/* Tells the server that we are alive, the server responds with an action */
	String ping(String parameters) throws Exception {
		if(parameters.length() != 0)
			return webManager.request((controlURL + "/ping.php?parameters=" + parameters), "GET");
		
		return webManager.request((controlURL + "/ping.php"), "GET");
	}
}
