package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class ManagerAgent extends Agent implements DecisionAgent {
		
	
	protected void setup() {
		System.out.println("ManagerAgent start");
		
		// Start KieSession for drools
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-manager");
    	
    	// Register the manager in the yellow pages
		registerAgent(this, getAID(), "manager");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		private AID eligibility, provider, service;
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
			System.out.println("------------------------------");
			System.out.println("\tShark Tank Demo");
			System.out.println("------------------------------");
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	// Find eligibility
	    	eligibility = findAgent(myAgent, "eligibility");
	    	System.out.println("Manager Found "+eligibility); 
	    	
	    	// Find provider
	    	provider = findAgent(myAgent, "provider");
	    	System.out.println("Manager Found "+provider);
	    	
	    	// Find service
	    	service = findAgent(myAgent, "service");
	    	System.out.println("Manager Found "+service);
		}
	}
}
