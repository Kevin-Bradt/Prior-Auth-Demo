package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class ManagerAgent extends Agent implements DecisionAgent {
		
	
	
	protected void setup() {
		System.out.println("ManagerAgent start");
    	// Try receiving message
    	addBehaviour(new Messaging());
	}
	
	private class Messaging extends OneShotBehaviour {
		
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-manager");
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
		}
	}
}
