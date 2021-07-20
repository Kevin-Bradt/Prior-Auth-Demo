package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class ServiceAgent extends Agent implements DecisionAgent {
		
	
	protected void setup() {
		System.out.println("ServiceAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-service");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "service");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		private AID manager, levelofcare;
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	manager = findAgent(myAgent, "manager");
	    	System.out.println("Service Found "+manager);
	    	
	    	//Find level of care
	    	levelofcare = findAgent(myAgent, "levelofcare");
	    	System.out.println("Service Found "+levelofcare);
		}
	}
}
