package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import data.MedicalInfo;
import data.Patient;

@SuppressWarnings("restriction")
public class ServiceAgent extends Agent implements DecisionAgent {
		
	private AID manager, levelofcare, mednec;
	
	private MedicalInfo medicalInfo;
	private Patient patient;
	
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
	
	public AID getMednec() {
		return mednec;
	}

	public void setMednec(AID mednec) {
		this.mednec = mednec;
	}

	protected void setup() {
		System.out.println("ServiceAgent start");
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	this.kSession = kContainer.newKieSession("ksession-service");
    	
    	// Adding agent to controller session
    	DecisionAgent.kSession2.insert(this);
   
    	
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
		this.patient = new Patient(str_xml);

		kSession.insert(this.patient);
		kSession.insert(this.medicalInfo);
        kSession.fireAllRules();
		//Insert this patient into Drools
		//Fire rules to see if more info is needed
		//If good, drools will call other method
	}
	
	public void missingInfo(String needed_info) {
		quickMessage(getManager(), this, needed_info, "missing-info" );
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;

		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			kSession = k;
			kSession.insert(myAgent);
	    	kSession.fireAllRules();
	    	
	    	//Find manager
	    	while (getManager() == null) {
	    		setManager(findAgent(myAgent, "manager"));
	    	}
	    	System.out.println("Service Found "+getManager());
	    	
	    	//Find level of care
	    	while (getLevelofcare() == null) {
	    		setLevelofcare(findAgent(myAgent, "levelofcare"));
	    	}
	    	System.out.println("Service Found "+getLevelofcare());
	    	
	    	//Find med nec
	    	while (getMednec() == null) {
	    		setMednec(findAgent(myAgent, "mednec"));
	    	}
	    	System.out.println("Service Found "+getMednec());
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
