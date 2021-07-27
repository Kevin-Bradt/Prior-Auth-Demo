package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MedNecAgent extends Agent implements DecisionAgent {
		
	private AID levelofcare;
	
	public AID getLevelofcare() {
		return levelofcare;
	}

	public void setLevelofcare(AID levelofcare) {
		this.levelofcare = levelofcare;
	}

	protected void setup() {
		System.out.println("MedNecAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-mednec");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "mednec");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;
		
		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			kSession = k;
			kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find level of care
	    	setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	System.out.println("Med Nec Found "+getLevelofcare());
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
