package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class LevelOfCareAgent extends Agent implements DecisionAgent {
	
	private AID service, mednec;
	private AID aidNameOnly;
	
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

	public AID getAidNameOnly() {
		return aidNameOnly;
	}

	public void setAidNameOnly(AID aidNameOnly) {
		this.aidNameOnly = aidNameOnly;
	}

	protected void setup() {
		System.out.println("LevelOfCareAgent start");
		aidNameOnly = getAID();
		aidNameOnly.clearAllAddresses();
		
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
		
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find service
	    	setService(findAgent(myAgent, "service"));
	    	System.out.println("LevelOfCare Found "+getService());
	    	
	    	//Find med nec
	    	setMednec(findAgent(myAgent, "mednec"));
	    	System.out.println("LevelOfCare Found "+getMednec());
	    	
	    	// Start of demo
	    	ACLMessage msg = myAgent.blockingReceive();		
			
			if (msg != null) {
				System.out.println("LOC: Behaviour recieved message");
				
				//delay
				try
				{
				    Thread.sleep(3000);
				}
				catch(InterruptedException ex)
				{
				    Thread.currentThread().interrupt();
				}
				
				kSession.insert(msg);
				kSession.fireAllRules();
				
			} else {
				block();
			}
			
			//Message 2 approves
			ACLMessage msg2 = myAgent.blockingReceive();		
			
			if (msg2 != null) {
				System.out.println("LOC: Behaviour recieved message");
				
				//delay
				try
				{
				    Thread.sleep(2000);
				}
				catch(InterruptedException ex)
				{
				    Thread.currentThread().interrupt();
				}
				
				kSession.insert(msg2);
				kSession.fireAllRules();
				
			} else {
				block();
			}
	    	
		}
	}

}
