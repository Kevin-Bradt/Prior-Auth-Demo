package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ManagerAgent extends Agent implements DecisionAgent {
		
	private AID eligibility, provider, service, facility;
	
	public AID getEligibility() {
		return eligibility;
	}

	public void setEligibility(AID eligibility) {
		this.eligibility = eligibility;
	}

	public AID getProvider() {
		return provider;
	}

	public void setProvider(AID provider) {
		this.provider = provider;
	}

	public AID getService() {
		return service;
	}

	public void setService(AID service) {
		this.service = service;
	}
	
	public AID getFacility() {
		return facility;
	}

	public void setFacility(AID facility) {
		this.facility = facility;
	}

	protected void setup() {
		System.out.println("ManagerAgent start");
		
		// Start KieSession for drools
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-manager");
    	
    	// Register the manager in the yellow pages
		registerAgent(this, getAID(), "manager");
    	
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
	    	
	    	// Find eligibility
	    	setEligibility(findAgent(myAgent, "eligibility"));
	    	System.out.println("Manager Found "+getEligibility()); 
	    	
	    	// Find provider
	    	setProvider(findAgent(myAgent, "provider"));
	    	System.out.println("Manager Found "+getProvider());
	    	
	    	// Find service
	    	setService(findAgent(myAgent, "service"));
	    	System.out.println("Manager Found "+getService());
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
