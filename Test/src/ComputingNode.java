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
	//private  map<uuid, int> nbRunningInstancesPerId;
	
	private int nbTotalRunningInstances;
	private int nbCoeurs;

	public ComputingNode(){
		hostName = "";
		nbTotalRunningInstances = 0;
		nbCoeurs = 0;

	}
	
	public ComputingNode(String hostName){
		setHostName(hostName);
		setStatus();
	}
	

	
	public String getHostName(){
		return hostName;
	}
	
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	public int getNbcoeurs(){
		return nbCoeurs;
	}
	
	public void setNbcoeurs(int nbCoeurs){
		this.nbCoeurs = nbCoeurs;
	}

	public int getNbRunningInstances(){
		return nbCoeurs;
	}
	
	public void setNbRunningInstances(int nbRunningInstances){
		this.nbTotalRunningInstances = nbRunningInstances;
	}
	
	// Mettre a jour le status du computing noeud
	public void setStatus(){
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
		
		this.setNbcoeurs(nbCoeurs);
		this.setNbRunningInstances(nbRunningInstances);
	}
	
	public String toString(){
		//return "Cette noeud a hostName: "+ this.hostName +"\nNombre de coeurs: "+ this.nbCoeurs+"\nidUnique de filter: "+ this.idFilter + "\nNombre des instances du filter:" + this.nbFiltersCourant + "\nidUnique de traductor: "+ this.idTraductor +"\nNombre des instances du traductor: "+ this.nbTraductorsCourant;
		return "Cette noeud a hostName: "+ this.hostName +"\nNombre de coeurs: "+ this.nbCoeurs+"\nNombre des instances actives:" + this.nbTotalRunningInstances;
	}
	
	public void startNewInstancesFilter(int nb){
		
	}
	
	public void startNewInstancesTraductor(){
		
	}
	
	public static void main(String[] args) {
		ComputingNode cpNode = new ComputingNode("7lm3x4j");
		//cpNode.setStatus();
		System.out.println(cpNode.toString());
	}
	
}
