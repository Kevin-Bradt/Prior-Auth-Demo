package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class LevelOfCareAgent extends Agent implements DecisionAgent {
	
	protected void setup() {
		System.out.println("LevelOfCareAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-levelofcare");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "levelofcare");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		private AID service, mednec;
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find service
	    	service = findAgent(myAgent, "service");
	    	System.out.println("LevelOfCare Found "+service);
	    	
	    	//Find med nec
	    	mednec = findAgent(myAgent, "mednec");
	    	System.out.println("LevelOfCare Found "+mednec);
	    	
		}
	}

}
