package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class MedNecAgent extends Agent implements DecisionAgent {
		
	
	
	protected void setup() {
		System.out.println("MedNecAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-mednec");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "mednec");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		private AID levelofcare;
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find level of care
	    	levelofcare = findAgent(myAgent, "levelofcare");
	    	System.out.println("Med Nec Found "+levelofcare);
	    	
		}
	}
}
