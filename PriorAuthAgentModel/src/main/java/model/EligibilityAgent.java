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
import data.Physician;
import policies.Policy;

@SuppressWarnings("restriction")
public class EligibilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	private Patient patient;
	private MedicalInfo medicalInfo;
	private Policy policy;
	private Physician physician;
	private KieSession kSession;

	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}
	
	
	protected void setup() {
		System.out.println("EligibilityAgent start");
		
		KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	this.kSession = kContainer.newKieSession("ksession-eligibility");
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "eligibility");
    	
    	// Try receiving message
    	addBehaviour(new Messaging(kSession, this));
	}
	
	// Drools calls this when agent receives form piece from manager
	public void parseForm(String str_xml) {
		//Parse string to xml if needed
		//System.out.println(str_xml);
		//Save data into patient class or other structure
	    this.patient = new Patient(str_xml);
	    this.medicalInfo = new MedicalInfo(str_xml);
	    this.physician = new Physician(str_xml);
	    this.policy = new Policy();
	    //System.out.println(this.patient.getFirstName() + " " + this.patient.getLastName());
	    //System.out.println(this.medicalInfo.getMedication() + " " + this.medicalInfo.getDiagnosis());
	    
	    //insert patient into ksession
        kSession.insert(this.patient);
        kSession.insert(this.medicalInfo);
        kSession.insert(this.physician);
        kSession.insert(this.policy);
        kSession.fireAllRules();

		//Insert this patient into Drools
		//Fire rules to see if more info is needed
		//If good, drools will call other method
	}
	
	private class Messaging extends CyclicBehaviour {
		
		private KieSession kSession;
		
		public Messaging(KieSession k, Agent a) {
			super(a);
			
			//get KieSession
			kSession = k;
			kSession.insert(myAgent);
			kSession.fireAllRules();
			
			//Find manager
	    	setManager(findAgent(myAgent, "manager"));
	    	System.out.println("Eligibilty Found "+getManager());
	    	
	    	
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
