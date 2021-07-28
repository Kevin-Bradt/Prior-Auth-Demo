package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class LevelOfCareAgent extends Agent implements DecisionAgent {
	
	private AID service, mednec;
	
	public AID getService() {
		return service;
	}

	public void setService(AID service) {
		this.service = service;
	}

	public AID getMednec() {
		return mednec;
	}

	public void setMednec(AID mednec) {
		this.mednec = mednec;
	}

	protected void setup() {
		System.out.println("LevelOfCareAgent start");
		
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-levelofcare");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "levelofcare");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;
		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			// Get KieSession
			kSession = k;
			kSession.insert(myAgent);
			kSession.fireAllRules();
			
			//Find service
	    	setService(findAgent(myAgent, "service"));
	    	System.out.println("LevelOfCare Found "+getService());
	    	
	    	//Find med nec
	    	setMednec(findAgent(myAgent, "mednec"));
	    	System.out.println("LevelOfCare Found "+getMednec());
			
		}
		
		// Cycles forever
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
