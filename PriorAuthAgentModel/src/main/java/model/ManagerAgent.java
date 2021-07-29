package model;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


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

	@SuppressWarnings("restriction")
	private static String nodeListToString(NodeList nodes) throws TransformerException {
	    DOMSource source = new DOMSource();
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

	    for (int i = 0; i < nodes.getLength(); ++i) {
	        source.setNode(nodes.item(i));
	        transformer.transform(source, result);
	    }

	    return writer.toString();
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
	
	// Drools calls this when facility sends form
	@SuppressWarnings("restriction")
	public void breakdownForm(String str_xml) {
		// Break apart form
		// Send parts to approriate agents
		// i.e. patient_info xml send to Elig. agent as string in ACL Message
		try {	  
	         //File inputFile = new File("NewFile.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         InputSource inputData = new InputSource();
	         inputData.setCharacterStream(new StringReader(str_xml));
	         Document doc = dBuilder.parse(inputData);
	         doc.getDocumentElement().normalize();
	         
	         NodeList patientInfo = doc.getElementsByTagName("patient_info");
	         NodeList physicianInfo = doc.getElementsByTagName("physician_info");
	         NodeList medicalInfo = doc.getElementsByTagName("medical_info");
	         NodeList insuranceInfo = doc.getElementsByTagName("insurance_info");

	 		 // This function will send 3 ACLMessage total
	         String eligibilityInfoMessage = nodeListToString(patientInfo);
	         String providerInfoMessage = nodeListToString(physicianInfo);
	         String serviceInfoMessage = nodeListToString(medicalInfo);
	         
	         
	         quickMessage(getEligibility(),this,eligibilityInfoMessage,"initial-info");
	         quickMessage(getProvider(),this,providerInfoMessage,"initial-info");
	         quickMessage(getService(),this,serviceInfoMessage,"initial-info");
	         
	         
		 } catch (Exception e) {
	         e.printStackTrace();
	     }
		
		
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
	    	
	    	// Find facility
	    	setFacility(findAgent(myAgent, "facility"));
	    	System.out.println("Manager Found "+getFacility());
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
