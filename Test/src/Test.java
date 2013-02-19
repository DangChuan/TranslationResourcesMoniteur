package trm;

import java.util.Iterator;

public class Test {

	public static void main(String[] args) {
		// String[] nodes = { 'localhost' };

		String urlToServer = "http://localhost/1/0/0/translate/file/enfr/0";
		String filePath = "/home/nguyen/bbc.html";
		String startTraductor = "http://localhost:8888/start/7477016b-104b-4e24-a289-13f48865aa23/1";
		String stopTraductor = "http://localhost:8888/stop/7477016b-104b-4e24-a289-13f48865aa23/1";
		String startFilter = "http://localhost:8888/start/dd9ac52d-a7f1-4f7c-960d-49c6aac94350/1";
		String stopFilter = "http://localhost:8888/stop/dd9ac52d-a7f1-4f7c-960d-49c6aac94350/1";
		String status = "http://localhost:8888/status/resources";

		// http://localhost/1/0/0/translate/file/enfr/0
		// http://<hostname>/<accountId>/<userId>/<configId>/translate/<translationType>/<srcLang><tgtLang>/<profileId>
		// Tu 1 requete nhu vay ta se trich rut ra cac thanh phan de phuc vu cho viec recherche uuid nhu ham sau

		// UUID uuid = getTypeId(accountId, service, profileId)
				
		// int nbInstances = TrmConfirugation.getNbInstancesStarted("localhost");
        // int nbInstancesPerTypeId = TrmConfiguiration.getNbInstancesStartedPerTypeId(hostname, uuid)
		// int nbInstancesPerTypeId = TrmConfiguiration.getNbInstancesStartedPerTypeId(hostname, accountId, service, profileId)
		
		String hostName = "7lm3x4j";
		String uuid = "7477016b-104b-4e24-a289-13f48865aa23";
		ComputingNode cpNode = new ComputingNode(hostName);
		long temps = 0;
		for (int i = 0; i < 5; i++) {
			temps = temps + TrmConfirugation.translateFile(hostName, 1, 0, 0,
							EnumClass.TranslateType.file, EnumClass.Langue.en,
							EnumClass.Langue.fr, 0, filePath);
		}
		long avg = temps / 5;

		System.out.println("Le temps d'execution moyenne : " + avg
				+ " miliseconds");

		// TrmConfirugation.startStopService("localhost","start","7477016b-104b-4e24-a289-13f48865aa23",1);
		// TrmConfirugation.startStopService("localhost","stop","dd9ac52d-a7f1-4f7c-960d-49c6aac94350",4);

		// Check nombre de noeuds tourner, la capacite de chaque noeud (nombre de coeurs),
		// nombre de filter, de traductors tournant sur chaque noeud
		
		 if(avg > 1500){ 	
			 if(cpNode.getNbcoeurs() - cpNode.getNbRunningInstances() > 0){
				 TrmConfirugation.startStopService(hostName,"start",uuid,1); 
			 }
			 //TrmConfirugation.startStopService(hostName,"start","7477016b-104b-4e24-a289-13f48865aa23",1);
			 //TrmConfirugation.startStopService("localhost","start","dd9ac52d-a7f1-4f7c-960d-49c6aac94350",1);	
		 }else{
			 if(cpNode.getNbInstancesParUuid(uuid) > 0){
				 TrmConfirugation.startStopService(hostName,"stop",uuid,1); 
			 }
			 //TrmConfirugation.startStopService(hostName,"stop","7477016b-104b-4e24-a289-13f48865aa23",1);
			 //TrmConfirugation.startStopService("localhost","stop","dd9ac52d-a7f1-4f7c-960d-49c6aac94350",1);			 		  
		 }
		 Test t = new Test();
		 t.traiterRequete();
		 
	}
	
	public void traiterRequete(){
		
		// Verifier la liste de hostName pour choisir une noeud disponible
		ManagerComputingNode man = new ManagerComputingNode();
		String hostNameDisponible = man.getHostNameDisponible();
		System.out.println(hostNameDisponible);
		
		// Demarrer ce host = getTypeId du filter, traductor, ensuite les installer, les demarrer
		int accountId = 0, profileId = 0;
		String service = "Filter";
		ConnectionSQLiteDB connectBD = new ConnectionSQLiteDB();
		String uuid = connectBD.getTypeId(accountId, service, profileId);
		
		//TrmConfirugation.installServiceSurNoeud(hostNameDisponible, uuid);
		
	}
	
}


