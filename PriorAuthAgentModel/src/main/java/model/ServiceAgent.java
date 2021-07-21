package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ServiceAgent extends Agent implements DecisionAgent {
		
	private AID aidNameOnly;
	private AID manager, levelofcare;
	
	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}

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
		System.out.println("ServiceAgent start");
		aidNameOnly = getAID();
		aidNameOnly.clearAllAddresses();
		
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
		
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
	    	kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	setManager(findAgent(myAgent, "manager"));
	    	System.out.println("Service Found "+getManager());
	    	
	    	//Find level of care
	    	setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	System.out.println("Service Found "+getLevelofcare());
	    	
	    	// Start of demo
	    	ACLMessage msg = myAgent.blockingReceive();		
			
			if (msg != null) {
				System.out.println("Service: Behaviour recieved message");
				
				//delay
				try
				{
				    Thread.sleep(4500);
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
				System.out.println("Service: Behaviour recieved message");
				
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
