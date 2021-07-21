package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ManagerAgent extends Agent implements DecisionAgent {
		
	private static final String RED = "\033[0;31m"; // foreground red color code 
	private static final String GRN = "\033[0;32m";    // foreground green color code
	private static final String YLW = "\033[0;33m"; // foreground yellow color code
	private static final String RST = "\033[0m";   // reset colors
	
	private AID aidNameOnly;
	private AID eligibility, provider, service, facility;

	private boolean elig, prov, serv;

	public boolean isElig() {
		return elig;
	}

	public void setElig(boolean elig) {
		this.elig = elig;
		System.out.println(elig);
	}

	public boolean isProv() {
		return prov;
	}

	public void setProv(boolean prov) {
		this.prov = prov;
		System.out.println(prov);
	}

	public boolean isServ() {
		return serv;
	}

	public void setServ(boolean serv) {
		this.serv = serv;
	}

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

	public AID getAidNameOnly() {
		return aidNameOnly;
	}

	public void setAidNameOnly(AID aidNameOnly) {
		this.aidNameOnly = aidNameOnly;
	}
	
	protected void setup() {
		System.out.println("ManagerAgent start");
		aidNameOnly = getAID();
		aidNameOnly.clearAllAddresses();
		
		// Start KieSession for drools
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-manager");
    	
    	// Register the manager in the yellow pages
		registerAgent(this, getAID(), "manager");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession));
	}
	
	private class Messaging extends OneShotBehaviour {
		
		private KieSession kSession;
		
		
		public Messaging(KieSession k) {
			kSession = k;
		}
		
		public void action() {
			System.out.println("-------------------------------");
			System.out.println(RED+"\tShark"+GRN+" Tank"+YLW+" Demo"+RST);
			System.out.println("-------------------------------");
	    	FactHandle managerFactH = kSession.insert(myAgent);
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
	    	
	    	
	    	// Start of demo
	    	while(true) {
	    		ACLMessage msg = myAgent.blockingReceive();		
				
				if (msg != null) {
					System.out.println("Manager: Behaviour recieved message");
					
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
					kSession.update(managerFactH, myAgent);
					kSession.fireAllRules();
					
				} else {
					block();
				}
	    	}
	    	
		}
	}
}
