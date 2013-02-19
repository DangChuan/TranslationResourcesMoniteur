package trm;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class TrmConfirugation {
	
	public static void executeUrl(String url){
		HttpClient client = new HttpClient();

		// Create a method instance.
		GetMethod method = new GetMethod(url);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			
			String responseBody = method.getResponseBodyAsString();
			System.out.println(new String(responseBody));

		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	// curl http://<hostname>:8888/<service>/<id>/<nb> : Start, Stop service

	public static void startStopService(String hostName, String service, String id, int nb) {
		String url = "http://" + hostName + ":8888/" + service + "/" + id + "/"
				+ Integer.toString(nb);
		executeUrl(url);
	}

	// http://localhost/1/0/0/translate/file/enfr/0
	// http://<hostname>/<accountId>/<userId>/<configId>/translate/<translationType>/<srcLang><tgtLang>/<profileId>

	public static long translateFile(String hostname, int accountId, int userId, int configId, EnumClass.TranslateType translateType,
			EnumClass.Langue srcLangue, EnumClass.Langue desLangue, int profilId, String filePath) {
		long start = 0, end = 0;
		long temps = 0;

		String urlToServer = "http://" + hostname + "/" + accountId + "/"
				+ userId + "/" + configId + "/translate/"
				+ translateType.name() + "/" + srcLangue + desLangue + "/"
				+ profilId;
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		PostMethod post = new PostMethod(urlToServer);

		// Set the document to translate as the body of Request
		FileRequestEntity file = new FileRequestEntity(new File(filePath),
				"test_bbc_file");
		post.setRequestEntity(file);

		try {
			start = System.currentTimeMillis();
			// Execute the method.
			int statusCodePost = client.executeMethod(post);
			end = System.currentTimeMillis();
			if (statusCodePost != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}

			temps = end - start;

			// Read the response body.
			String in = post.getResponseBodyAsString();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			// System.out.println(in);

			// We can save this result in a new file
			// FileUtils.writeStringToFile(new
			// File("/home/nguyen/test-fr.html"), in);

			// System.out.println(urlToServer);
			System.out
					.println("-----------------------------------------------------------------------------");
			System.out.println("Le temps d'execution : " + temps);

		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			post.releaseConnection();
		}

		return temps;
	}
	
	// This function will return number of instances active on this hostName
	public static int getNbInstancesStarted(String hostName){
		int nbCoeurs = 0, nbRunningInstances = 0;
		
		HttpClient client = new HttpClient();
	
		GetMethod method = new GetMethod("http://"+hostName+":8888/status");
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the content of status.
			String statusContent = method.getResponseBodyAsString();

			try {
				JSONObject rootObject = new JSONObject(statusContent);
				JSONObject system = rootObject.getJSONObject("system");
				// JSONObject hardwareConcurrency = system.getJSONObject("hardwareConcurrency");
				nbCoeurs = system.getInt("hardwareConcurrency");
				System.out.println("Nombre de coeurs de la machine: " + nbCoeurs);
				
				JSONArray resources = rootObject.getJSONArray("resources");
				
				for(int i=0; i<resources.length(); i++){
					JSONObject row = resources.getJSONObject(i); 
					try {
						JSONArray instancesArray = row.getJSONArray("instances");
						for(int j=0; j<instancesArray.length(); j++){
							if(instancesArray.getJSONObject(j).getString("status").equals("started")){
								nbRunningInstances+=1;
							}
						}
						System.out.println("Nombre des instances: " + instancesArray.length());
						System.out.println("Nombre des instances running: " + nbRunningInstances);
					} catch (JSONException e) {
						//e.printStackTrace();
					}
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		
		return nbRunningInstances;

	}
	
	// Methode recupere le Typeid d'une service a partir de (accountId, Service et profilId)
	public String getTypeId(int accountId, String service, int profileId){
		String uuid = "";
		
		return uuid;
	}
	
	// http://<hostname>:8888/install/{TR_install_method}/{TR_unique_id}
	public static void installServiceSurNoeud(String hostName, String uuid){
		String installMethode = "rsync";
		String url = "http://"+hostName+":8888/install/"+installMethode+"/"+uuid;
		executeUrl(url);
	}
	
	// http://<hostname>:8888/uninstall/{TR_unique_id}
	public static void uninstallServiceSurNoeud(String hostName, String uuid){
		String url = "http://"+hostName+":8888/uninstall/"+uuid;
		executeUrl(url);
	}
	
	
	
}
