package trm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


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
	private Map<String,Integer> nbRunningInstancesPerIdMap = new HashMap<String,Integer>();
	private int nbTotalRunningInstances;
	private int nbCoeurs;
	//private int nbCoeursDisponibles;// NbCoeursDisponibles = nbCoeurs - nbTotalRunningInstances

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
		return nbTotalRunningInstances;
	}
	
	public void setNbRunningInstances(int nbRunningInstances){
		this.nbTotalRunningInstances = nbRunningInstances;
	}
	
	public int getNbCoeursDisponibles(){
		return nbCoeurs - nbTotalRunningInstances;
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
						String uuid = row.getString("id");
						int nbRunningInstancePerUuid = 0;
						System.out.println("So thu tu cua mang instances:" + i);
						System.out.println("UUID :" + uuid);
						for(int j=0; j<instancesArray.length(); j++){
							if(instancesArray.getJSONObject(j).getString("status").equals("started")){
								nbRunningInstances+=1;
								nbRunningInstancePerUuid+=1;
							}
						}
						nbRunningInstancesPerIdMap.put(uuid, nbRunningInstancePerUuid);
						System.out.println(uuid + " Nombre des nbRunningInstancePerUuid: " + nbRunningInstancePerUuid);
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
	
	public void affichierInfoMap(){
		Set s=this.nbRunningInstancesPerIdMap.entrySet();
		Iterator it= s.iterator();
		while(it.hasNext())
        {
            // key=value separator this by Map.Entry to get key and value
            Map.Entry m =(Map.Entry)it.next();

            // getKey is used to get key of Map
            String key=(String) m.getKey();

            // getValue is used to get value of key in Map
            int value=(Integer)m.getValue();

            System.out.println("UUID :"+key+"  nombre d'instances :"+value);
        }

	}
	
	public int getNbInstancesParUuid(String uuid){
		int nbInstances = nbRunningInstancesPerIdMap.get(uuid) != null ? nbRunningInstancesPerIdMap.get(uuid) : 0;
		return nbInstances;
	}
	
	public static void main(String[] args) {
		ComputingNode cpNode = new ComputingNode("7lm3x4j");
		cpNode.affichierInfoMap();
		String uuid = "7477016b-104b-4e24-a289-13f48865aa23";
		System.out.println(cpNode.getNbInstancesParUuid(uuid));
		//cpNode.setStatus();
		//System.out.println(cpNode.toString());
	}
	
}
