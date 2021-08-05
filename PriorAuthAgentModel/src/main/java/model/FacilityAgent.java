package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.FacilityGui;

public class FacilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	
	private FactHandle agentFH;

	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}


	private FacilityGui myGui;
	
	
	protected void setup() {
		System.out.println("FacilityAgent start");
		
		// Adding agent to controller session
		this.agentFH = DecisionAgent.kSession2.insert(this);
		
		// Create and show the GUI 
		myGui = new FacilityGui(this);
		myGui.showGui();
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "facility");
    	
    	while (getManager() == null) {
    		setManager(findAgent(this, "manager"));
    	}
    	System.out.println("Facility Found "+getManager());
    	
	}
	
	protected void takeDown() {
		myGui.dispose();
	}
	
	public void stepDemo() {
		DecisionAgent.kSession2.update(agentFH, this);
		DecisionAgent.kSession2.fireAllRules();
	}
	
	public String readFile(String string) throws IOException {
	  File f = new File(string);
  	  Reader fileReader = new FileReader(f); 
  	  BufferedReader bufReader = new BufferedReader(fileReader); 
  	  StringBuilder sb = new StringBuilder(); 
  	  String line = bufReader.readLine(); 
  	  while( line != null){ 
  		  sb.append(line).append("\n"); 
  		  line = bufReader.readLine(); 
  		  } 
  	  String inputString = sb.toString(); 
  	  bufReader.close();
  	  return inputString;
	}
	
	public void startDemo(String file) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				System.out.println("Start demo");
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(getManager());
				
				//Content will be string xml of pa_form
				try {
					String inputString = readFile(file);
					msg.setContent(inputString);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.setConversationId("facility-pa-request");
				myAgent.send(msg);
			}
		} );
	}
	
	
}
