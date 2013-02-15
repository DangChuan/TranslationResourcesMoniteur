import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class HttpClientTutorial {
  
  private static String url = "http://www.apache.org/";

  private static String url2 = "http://www.google.com/";
  
  private static String url3 = "http://localhost:8888/start/7477016b-104b-4e24-a289-13f48865aa23/1";
  
  public static void main(String[] args) {
    // Create an instance of HttpClient.
    HttpClient client = new HttpClient();

    //PostMethod post = new PostMethod("http://jakarata.apache.org/");
    PostMethod post = new PostMethod("http://localhost/1/0/0/translate/file/enfr/0");
    /*
    NameValuePair[] data = {
      new NameValuePair("user", "joe"),
      new NameValuePair("password", "bloggs")
    };
    post.setRequestBody(data);
    */
    //InputStream inStream = "This is a test";
    post.setRequestBody("This is a test");
    post.setRequestBody("My name is ABC");
    //post.setRequestBody(inStream);
    //post.addParameter("name", "This is a test");
    
    //File file = new File("/home/nguyen/test.html");
    FileRequestEntity file = new FileRequestEntity(new File("/home/nguyen/test.html"), "test_bbc_file");
    
    post.setRequestEntity(file);
    
    try {
        // Execute the method.
        int statusCodePost = client.executeMethod(post);

        if (statusCodePost != HttpStatus.SC_OK) {
          System.err.println("Method failed: " + post.getStatusLine());
        }

        // Read the response body.
        //byte[] responseBody = method.getResponseBody();
        //InputStream in = post.getResponseBodyAsStream();
        String in = post.getResponseBodyAsString();
        
        FileUtils.writeStringToFile(new File("/home/nguyen/test-fr.html"), in);
        //PrintWriter out = new PrintWriter("test-fr.html");
        //out.println(in);
        
        // Deal with the response.
        // Use caution: ensure correct character encoding and is not binary data

        System.out.println(in);

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
    /*
    
    // Create a method instance.
    GetMethod method = new GetMethod(url3);
    
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
      String responseBody2 = method.getResponseBodyAsString();

      // Deal with the response.
      // Use caution: ensure correct character encoding and is not binary data
      
      //System.out.println(new String(responseBody));
      System.out.println(new String(responseBody2));

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
    */
  }
}