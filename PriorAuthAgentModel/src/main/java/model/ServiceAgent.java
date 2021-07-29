package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import data.MedicalInfo;

@SuppressWarnings("restriction")
public class ServiceAgent extends Agent implements DecisionAgent {
		
	private AID manager, levelofcare;
	
	private MedicalInfo medicalInfo;
	
	private KieSession kSession;
	
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

	protected void setup() {
		System.out.println("ServiceAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	this.kSession = kContainer.newKieSession("ksession-service");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "service");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	// Drools calls this when agent receives form piece from manager
	public void parseForm(String str_xml) {
		//Parse string to xml if needed
		//Save data into patient class or other structure
		this.medicalInfo = new MedicalInfo(str_xml);
		if (this.medicalInfo != null) {
     	   System.out.println(this.medicalInfo.getDiagnosis());
        }
        else {
     	   System.out.println("null");
        }

		kSession.insert(this.medicalInfo);
        kSession.fireAllRules();
		//Insert this patient into Drools
		//Fire rules to see if more info is needed
		//If good, drools will call other method
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;

		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			kSession = k;
			kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	setManager(findAgent(myAgent, "manager"));
	    	System.out.println("Service Found "+getManager());
	    	
	    	//Find level of care
	    	setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	System.out.println("Service Found "+getLevelofcare());
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
