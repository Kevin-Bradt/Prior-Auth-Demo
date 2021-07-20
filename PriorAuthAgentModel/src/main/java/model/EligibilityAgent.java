package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class EligibilityAgent extends Agent implements DecisionAgent {
		
	
	
	protected void setup() {
		System.out.println("EligibilityAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-eligibility");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "eligibility");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		private AID manager;
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	manager = findAgent(myAgent, "manager");
	    	System.out.println("Eligibilty Found "+manager);
		}
	}
}
