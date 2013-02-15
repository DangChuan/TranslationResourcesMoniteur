import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class GetJsonData {

	private static String url = "http://www.apache.org/";

	public static void main(String[] args) {
		String content = "";
		File res_status = new File(
				"/home/chuan/Chuan_Systran/14_02/res_status_13.json");
		try {
			// Read the entire contents of sample.txt
			content = FileUtils.readFileToString(res_status);

			// For the sake of this example we show the file
			// content here.
			System.out.println("File content: " + content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String jsonStr = content;

		try {
			JSONObject rootObject = new JSONObject(jsonStr);
			JSONObject system = rootObject.getJSONObject("system");
			// JSONObject hardwareConcurrency =
			// system.getJSONObject("hardwareConcurrency");
			int nbCoeurs = system.getInt("hardwareConcurrency");
			System.out.println("Nombre de coeurs de la machine: " + nbCoeurs);

			JSONArray resources = rootObject.getJSONArray("resources");
			// Get element which contains the instances of filters
			JSONObject element1 = resources.getJSONObject(1);
			JSONArray filterInstances = element1.getJSONArray("instances");
			String idUniqueFilter = element1.getString("id");
			// JSONArray filterInstances =
			// resources.getJSONObject(1).getJSONArray("instances");
			int nbFilters = filterInstances.length();
			System.out.println("IdUnique du filter: " + idUniqueFilter);
			System.out.println("Nombre de filters en tournant: " + nbFilters);

			// Get element which contains the instances of translators
			JSONObject element3 = resources.getJSONObject(3);
			JSONArray traductorInstances = element3.getJSONArray("instances");
			String idUniqueTraductor = element3.getString("id");
			// JSONArray traductorInstances =
			// resources.getJSONObject(3).getJSONArray("instances");
			int nbTraductors = traductorInstances.length();
			System.out.println("IdUnique du traductor: " + idUniqueTraductor);
			System.out.println("Nombre de traducteurs en tournant: "
					+ nbTraductors);

			/*
			 * JSONArray rows = rootObject.getJSONArray("rows"); // Get all
			 * JSONArray rows
			 * 
			 * for(int i=0; i < rows.length(); i++) { // Loop over each each row
			 * JSONObject row = rows.getJSONObject(i); // Get row object
			 * JSONArray elements = row.getJSONArray("elements"); // Get all
			 * elements for each row as an array
			 * 
			 * for(int j=0; j < elements.length(); j++) { // Iterate each
			 * element in the elements array JSONObject element =
			 * elements.getJSONObject(j); // Get the element object JSONObject
			 * duration = element.getJSONObject("duration"); // Get duration sub
			 * object JSONObject distance = element.getJSONObject("distance");
			 * // Get distance sub object
			 * 
			 * System.out.println("Duration: " + duration.getInt("value")); //
			 * Print int value System.out.println("Distance: " +
			 * distance.getInt("value")); // Print int value } }
			 */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * // Create an instance of HttpClient. HttpClient client = new
		 * HttpClient();
		 * 
		 * PostMethod post = new
		 * PostMethod("http://localhost/1/0/0/translate/file/enfr/0");
		 * 
		 * FileRequestEntity file = new FileRequestEntity(new
		 * File("/home/nguyen/test.html"), "test_bbc_file");
		 * 
		 * post.setRequestEntity(file);
		 * 
		 * try { // Execute the method. int statusCodePost =
		 * client.executeMethod(post);
		 * 
		 * if (statusCodePost != HttpStatus.SC_OK) {
		 * System.err.println("Method failed: " + post.getStatusLine()); }
		 * 
		 * String in = post.getResponseBodyAsString();
		 * FileUtils.writeStringToFile(new File("/home/nguyen/test-fr.html"),
		 * in); // Sau buoc nay thi da luu dc file Json (la ket qua cua viec
		 * thuc hien lenh ...status) // => gia su chinh la "res_status_7.json"
		 * 
		 * //File res_status = new
		 * File("/home/chuan/Chuan_Systran/14_02/res_status_7.json");
		 * 
		 * } catch (HttpException e) {
		 * System.err.println("Fatal protocol violation: " + e.getMessage());
		 * e.printStackTrace(); } catch (IOException e) {
		 * System.err.println("Fatal transport error: " + e.getMessage());
		 * e.printStackTrace(); } finally { // Release the connection.
		 * post.releaseConnection(); }
		 */

	}
}
