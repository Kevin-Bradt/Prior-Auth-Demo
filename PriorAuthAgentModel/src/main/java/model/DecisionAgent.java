package model;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public interface DecisionAgent {
	
	default void registerAgent(Agent agent, AID name, String service) {
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(name);
		ServiceDescription sd = new ServiceDescription();
		sd.setType(service);
		sd.setName("prior-auth");
		dfd.addServices(sd);
		try {
			DFService.register(agent, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	default AID findAgent(Agent agent, String target) {
		
		AID aid = null;
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(target);
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(agent, template); 
			aid = result[0].getName();
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		return aid;
	}
	
}
