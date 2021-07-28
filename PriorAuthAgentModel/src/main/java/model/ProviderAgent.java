package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class ProviderAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	
	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}

	protected void setup() {
		System.out.println("ProviderAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-provider");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "provider");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	// Drools calls this when agent receives form piece from manager
	public void parseForm(String str_xml) {
		//Parse string to xml if needed
		//Save data into patient class or other structure
		
		
		
		//Insert this patient into Drools
		//Fire rules to see if more info is needed
		//If good, drools will call other method
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;

		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			kSession = k;
			kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	setManager(findAgent(myAgent, "manager"));
	    	System.out.println("Provider Found "+getManager());
		}
		
		public void action() {
	    	
			// Wait for message
	    	ACLMessage msg = myAgent.blockingReceive();
	    	if (msg != null) {
				kSession.insert(msg);
				kSession.fireAllRules();
			}
			else {
				block();
			}
	    	
		}
		
	}
}
