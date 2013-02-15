import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class TrmConfirugation {

	  	// curl http://<hostname>:8888/<service>/<id>/<nb>
		// hostname correspond a une noeud, service => start, stop... id => id du instance. nb => nombres d'instances a demarrer, stopper
	  	public static void executerUrl(String hostName, String service, String id, int nb) {
	    // Create an instance of HttpClient.
	    HttpClient client = new HttpClient();

	    String url = "http://"+hostName+":8888/"+service+"/"+id+"/"+Integer.toString(nb);
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

	      // Read the response body.
	      //byte[] responseBody = method.getResponseBody();
	      String responseBody = method.getResponseBodyAsString();

	      // Deal with the response.
	      // Use caution: ensure correct character encoding and is not binary data
	      
	      //System.out.println(new String(responseBody));
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
	  	
	  	// http://localhost/1/0/0/translate/file/enfr/0
	  	// http://<hostname>/<accountId>/<userId>/<configId>/translate/<translationType>/<srcLang><tgtLang>/<profileId>
	  	// accountId apple = 7892 int
	  	// userId = 0 int
	  	// configId = 0 int
	  	// translateType = enum file, doc, par, pars
	  	// srcLang = en, fr, de ...
	  	// profileId = 0, 1, 2,...
	  	
	  	// This method will translate a file (identified by filePath) using a service (identified urlToServer) and return the time to do it.
	  	public static long translateFile(String hostname, int accountId, int userId, int configId, EnumClass.TranslateType translateType, EnumClass.Langue srcLangue, EnumClass.Langue desLangue, int profilId, String filePath) {
	  		long start = 0, end = 0;
	  		long temps = 0;
	  		
	  		String urlToServer = "http://"+hostname+"/"+accountId+"/"+userId+"/"+configId+"/translate/"+translateType.name()+"/"+srcLangue+desLangue+"/"+profilId;
	  	    // Create an instance of HttpClient.
	  	    HttpClient client = new HttpClient();

	  	    // Initial PostMethod with Url which will send the request to server
	  	    // PostMethod post = new PostMethod("http://localhost/1/0/0/translate/file/enfr/0");
	  	    PostMethod post = new PostMethod(urlToServer);
	  	    
	  	    post.setRequestBody("This is a test");
	  	    
	  	    // Set the document to translate as the body of Request
	  	    FileRequestEntity file = new FileRequestEntity(new File(filePath), "test_bbc_file");
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
	  	        // Use caution: ensure correct character encoding and is not binary data
	  	        //System.out.println(in);
	  	        
	  	        // We can save this result in a new file
	  	        //FileUtils.writeStringToFile(new File("/home/nguyen/test-fr.html"), in);  	        
	  	        
	  	        //System.out.println(urlToServer);
	  	        System.out.println("-----------------------------------------------------------------------------");
	  	        System.out.println("Le temps d'execution : "+ temps);

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
}
