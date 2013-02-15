package trm;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComputingNode {
	private String hostName;
	private String idNode, idFilter, idTraductor;
	private int nbCoeurs;
	private int nbFiltersCourant; // Nombre des instances du filter
	private int nbTraductorsCourant; // Nombre des instances du traductor
	public ComputingNode(){
		hostName = "";
		idNode = "";
		idFilter = "";
		idTraductor = "";
		nbCoeurs = 0;
		nbFiltersCourant = 0;
		nbTraductorsCourant = 0;
	}
	
	public ComputingNode(String hostName){
		setHostName(hostName);
	}
	
	public ComputingNode(String hostName, int nbCoeurs, int nbFilters, int nbTraductors, String idFilter, String idTraductor){
		setHostName(hostName);
		setFiltersCourant(nbFilters);
		setNbcoeurs(nbCoeurs);
		setNbTraductorsCourant(nbTraductors);		
		setIdFilter(idFilter);
		setIdTraductor(idTraductor);
	}
	
	public String getHostName(){
		return hostName;
	}
	
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	public String getIdNode(){
		return idNode;
	}
	
	public void setIdNode(String id){
		this.idNode = id;
	}
	
	public String getIdFilter(){
		return idFilter;
	}
	
	public void setIdFilter(String id){
		this.idFilter = id;
	}
	
	public String getIdTraductor(){
		return idTraductor;
	}
	
	public void setIdTraductor(String id){
		this.idTraductor = id;
	}
	
	public int getNbcoeurs(){
		return nbCoeurs;
	}
	
	public void setNbcoeurs(int nbCoeurs){
		this.nbCoeurs = nbCoeurs;
	}
	
	public int getNbFiltersCourant(){
		return nbFiltersCourant;
	}
	
	public void setFiltersCourant(int nbFiltersCourant){
		this.nbFiltersCourant = nbFiltersCourant;
	}
	
	public int getNbTraductorsCourant(){
		return nbTraductorsCourant;
	}
	
	public void setNbTraductorsCourant(int nbTraductorsCourant){
		this.nbTraductorsCourant = nbTraductorsCourant;
	}
	
	// Mettre a jour le status du computing noeud
	public void setStatus(){
		int nbFilters=0, nbTraductors=0, nbCoeurs=0;
		String idUniqueFilter = "", idUniqueTraductor = "";
		HttpClient client = new HttpClient();
	
		GetMethod method = new GetMethod("http://"+this.hostName+":8888/status");
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

				JSONArray resources = rootObject.getJSONArray("resources");
				// Get element which contains the instances of filters
				JSONObject element1 = resources.getJSONObject(1);
				JSONArray filterInstances = element1.getJSONArray("instances");
				idUniqueFilter = element1.getString("id");
				// JSONArray filterInstances = resources.getJSONObject(1).getJSONArray("instances");
				nbFilters = filterInstances.length();

				// Get element which contains the instances of translators
				JSONObject element3 = resources.getJSONObject(3);
				JSONArray traductorInstances = element3.getJSONArray("instances");
				idUniqueTraductor = element3.getString("id");
				// JSONArray traductorInstances = resources.getJSONObject(3).getJSONArray("instances");
				nbTraductors = traductorInstances.length();

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
		
		this.setNbcoeurs(nbCoeurs);
		this.setFiltersCourant(nbFilters);
		this.setIdFilter(idUniqueFilter);
		this.setNbTraductorsCourant(nbTraductors);
		this.setIdTraductor(idUniqueTraductor);
	}
	
	public String toString(){
		return "Cette noeud a hostName: "+ this.hostName +"\nNombre de coeurs: "+ this.nbCoeurs+"\nidUnique de filter: "+ this.idFilter + "\nNombre des instances du filter:" + this.nbFiltersCourant + "\nidUnique de traductor: "+ this.idTraductor +"\nNombre des instances du traductor: "+ this.nbTraductorsCourant;
	}
	
	public void startNewInstancesFilter(int nb){
		
	}
	
	public void startNewInstancesTraductor(){
		
	}
	
	public static void main(String[] args) {
		ComputingNode cpNode = new ComputingNode("7lm3x4j");
		cpNode.setStatus();
		System.out.println(cpNode.toString());
	}
	
}
