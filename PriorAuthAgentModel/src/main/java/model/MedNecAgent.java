package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MedNecAgent extends Agent implements DecisionAgent {
		
	private AID levelofcare;
	private AID aidNameOnly;
	
	public AID getLevelofcare() {
		return levelofcare;
	}

	public void setLevelofcare(AID levelofcare) {
		this.levelofcare = levelofcare;
	}

	public AID getAidNameOnly() {
		return aidNameOnly;
	}

	public void setAidNameOnly(AID aidNameOnly) {
		this.aidNameOnly = aidNameOnly;
	}

	protected void setup() {
		System.out.println("MedNecAgent start");
		aidNameOnly = getAID();
		aidNameOnly.clearAllAddresses();
		
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
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find level of care
	    	setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	System.out.println("Med Nec Found "+getLevelofcare());
	    	
	    	// Start of demo
	    	ACLMessage msg = myAgent.blockingReceive();		
			
			if (msg != null) {
				System.out.println("Med nec: Behaviour recieved message");
				
				//delay
				try
				{
				    Thread.sleep(4000);
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
	    	
		}
	}
}
