package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.FacilityGui;

public class FacilityAgent extends Agent implements DecisionAgent {
		
	private AID manager;

	public AID getManager() {
		return manager;
	}

	public void setManager(AID manager) {
		this.manager = manager;
	}

	private FacilityGui myGui;
	
	
	protected void setup() {
		System.out.println("FacilityAgent start");
		
		// Create and show the GUI 
		myGui = new FacilityGui(this);
		myGui.showGui();
    	
    	// Register the eligibility agent in the yellow pages
    	registerAgent(this, getAID(), "facility");
    	
    	while (getManager() == null) {
    		setManager(findAgent(this, "manager"));
    	}
    	System.out.println("Facility Found "+getManager());
    	
	}
	
	protected void takeDown() {
		myGui.dispose();
	}
	
	public String readFile(String string) throws IOException {
	  File f = new File(string);
  	  Reader fileReader = new FileReader(f); 
  	  BufferedReader bufReader = new BufferedReader(fileReader); 
  	  StringBuilder sb = new StringBuilder(); 
  	  String line = bufReader.readLine(); 
  	  while( line != null){ 
  		  sb.append(line).append("\n"); 
  		  line = bufReader.readLine(); 
  		  } 
  	  String inputString = sb.toString(); 
//  	  System.out.println(inputString);
  	  bufReader.close();
  	  return inputString;
	}
	
	public void startDemo() {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				System.out.println("Start demo");
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(getManager());
				
				//Content will be string xml of pa_form
//				try {
//					String inputString = readFile("PAform.xml");
//					msg.setContent(inputString);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				msg.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<pa_form>\n"
						+ "	<date>07/21/2021</date>\n"
						+ "	<patient_info>\n"
						+ "		<first_name>First</first_name>\n"
						+ "		<last_name>Last</last_name>\n"
						+ "		<member_id>12345678</member_id>\n"
						+ "		<address>123 First Ave</address>\n"
						+ "		<city>Minneapolis</city>\n"
						+ "		<state>MN</state>\n"
						+ "		<zip>55414</zip>\n"
						+ "		<phone>8888888888</phone>\n"
						+ "		<dob>12/31/1999</dob>\n"
						+ "		<allergies></allergies>\n"
						+ "		<primary_insurance>UHC</primary_insurance>\n"
						+ "		<policy_num>12345</policy_num>\n"
						+ "		<group_num>12345</group_num>\n"
						+ "		<new>false</new>\n"
						+ "		<continued>07/21/2021</continued>\n"
						+ "		<hospitalized>false</hospitalized>\n"
						+ "	</patient_info>\n"
						+ "	<insurance_info>\n"
						+ "	</insurance_info>\n"
						+ "	<physician_info>\n"
						+ "		<first_name>Bob</first_name>\n"
						+ "		<last_name>Smith</last_name>\n"
						+ "		<credential>M.D.</credential>\n"
						+ "		<address>11000 Optum Cir</address>\n"
						+ "		<city>Eden Prairie</city>\n"
						+ "		<state>MN</state>\n"
						+ "		<zip>55344</zip>\n"
						+ "		<phone>0123456789</phone>\n"
						+ "		<fax>0123456789</fax>\n"
						+ "		<npi_num>0001</npi_num>\n"
						+ "		<specialty>injections</specialty>\n"
						+ "		<contact_name>Burt</contact_name>\n"
						+ "		<hospital>Methodist Hospital</hospital>\n"
						+ "	</physician_info>\n"
						+ "	<medical_info>\n"
						+ "		<medication>IgIV</medication>\n"
						+ "		<strength>300mg</strength>\n"
						+ "		<directions>infusion</directions>\n"
						+ "		<diagnosis>Primary immunodeficiency</diagnosis>\n"
						+ "		<hiv_aids>false</hiv_aids>\n"
						+ "		<cpt_code>90283</cpt_code>\n"
						+ "		<icd_10>D83.9</icd_10>\n"
						+ "		<pregnant>false</pregnant>\n"
						+ "		<explanation>preventative care</explanation>\n"
						+ "		<prev_medications>\n"
						+ "			<medication>\n"
						+ "				<name>IgIV</name>\n"
						+ "				<strength>300mg</strength>\n"
						+ "				<date_range>2019-2020</date_range>\n"
						+ "				<description>Monthly infusion</description>\n"
						+ "			</medication>\n"
						+ "		</prev_medications>\n"
						+ "	</medical_info>\n"
						+ "</pa_form>");
				msg.setConversationId("facility-pa-request");
				myAgent.send(msg);
			}
		} );
	}
	
	
}
