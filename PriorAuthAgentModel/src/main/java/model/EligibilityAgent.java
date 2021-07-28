package model;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class EligibilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;
	private Patient patient;
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
		//Save data into patient class or other structure
	      this.patient = new Patient(str_xml);
	      if (this.patient != null) {
	     	   System.out.println(this.patient.getFirstName());
	        }
	        else {
	     	   System.out.println("null");
	        }
	    //insert patient into ksession
          kSession.insert(this.patient);
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
