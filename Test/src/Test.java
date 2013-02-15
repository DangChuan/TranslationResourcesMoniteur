

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

		long temps = 0;
		for(int i = 0; i < 5 ; i++){
			temps = temps + TrmConfirugation.translateFile("localhost", 1, 0, 0, EnumClass.TranslateType.file, EnumClass.Langue.en, EnumClass.Langue.fr, 0, filePath);
		}
		long avg = temps / 5;
		
		System.out.println("Le temps d'execution moyenne : "+ avg + " miliseconds");
		
		//TrmConfirugation.executerUrl("localhost","start","7477016b-104b-4e24-a289-13f48865aa23",1);
		//TrmConfirugation.executerUrl("localhost","stop","dd9ac52d-a7f1-4f7c-960d-49c6aac94350",4);
		
		
		// Check nombre de node tourner, la capacite de node (nombre de coeurs), nombre de filter, de traductors tourner sur chaque node
		/*
		if(avg > 1000){
			TrmConfirugation.executerUrl(startTraductor);
			TrmConfirugation.executerUrl(startFilter);
		}else{
			TrmConfirugation.executerUrl(stopFilter);
			TrmConfirugation.executerUrl(stopTraductor);
		}
		*/		
		
	}
}
