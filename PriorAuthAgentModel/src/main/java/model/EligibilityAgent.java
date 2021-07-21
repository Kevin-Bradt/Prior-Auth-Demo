package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class EligibilityAgent extends Agent implements DecisionAgent {
		
	private AID aidNameOnly;
	private AID manager;
	
	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}

	public AID getAidNameOnly() {
		return aidNameOnly;
	}

	public void setAidNameOnly(AID aidNameOnly) {
		this.aidNameOnly = aidNameOnly;
	}
	
	protected void setup() {
		System.out.println("EligibilityAgent start");
		aidNameOnly = getAID();
		aidNameOnly.clearAllAddresses();
		
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
		
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	setManager(findAgent(myAgent, "manager"));
	    	System.out.println("Eligibilty Found "+getManager());
	    	
	    	// Start of demo
	    	//Message 1 fails
	    	ACLMessage msg = myAgent.blockingReceive();		
			
			if (msg != null) {
				System.out.println("Eligibility: Behaviour recieved message");
				
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
				System.out.println("Eligibility: Behaviour recieved message");
				
				//delay
				try
				{
				    Thread.sleep(3000);
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
