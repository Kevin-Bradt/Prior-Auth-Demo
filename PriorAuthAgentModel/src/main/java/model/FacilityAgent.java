package model;

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
	
	public void startDemo() {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				System.out.println("Start demo");
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(getManager());
				msg.setContent("Hello");
				msg.setConversationId("facility");
				myAgent.send(msg);
			}
		} );
	}
	
	
}
