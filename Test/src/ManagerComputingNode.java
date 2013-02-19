package trm;

import java.util.ArrayList;

import java.util.Iterator;


public class ManagerComputingNode {
	//String[] hostNameList = new String[100];
	ArrayList<String> hostNameList = new ArrayList<String>();
	
	public ManagerComputingNode(){
		hostNameList.add("localhost");
	}
	
	public boolean addNode(String hostName){
		if(!hostNameList.contains(hostName)){
			hostNameList.add(hostName);
			return true;
		} 
		return false;
	}
	
	public boolean removeNode(String hostName){
		if(hostNameList.contains(hostName)){
			hostNameList.remove(hostName);
			return true;
		}
		return false;
	}
	
	public ArrayList<String> getHostNameList(){
		return this.hostNameList;
	}
	
	public void setHostNameList(ArrayList<String> hostNameList){
		this.hostNameList = hostNameList;
	}
	
	public String getHostNameDisponible(){
		//ManagerComputingNode man = new ManagerComputingNode();
		Iterator<String> itr = hostNameList.iterator();
		while (itr.hasNext()) {
			String hostElement = itr.next();
			ComputingNode cpNode = new ComputingNode(hostElement);
			if(cpNode.getNbCoeursDisponibles()>0 && cpNode.getNbCoeursDisponibles()<cpNode.getNbcoeurs()){
				return hostElement;
			}
			//cpNode.setStatus();
			System.out.println(cpNode.toString());
		}
		return "";
	}
	
	public static void main(String[] args) {
		ManagerComputingNode man = new ManagerComputingNode();
		Iterator<String> itr = man.hostNameList.iterator();
		while (itr.hasNext()) {
			String hostElement = itr.next();
			ComputingNode cpNode = new ComputingNode(hostElement);
			//cpNode.setStatus();
			System.out.println(cpNode.toString());
		}

	}
	
}
