# Shark Tank Demo

This demo is to show some example agent interactions. 
Agents send messages back and forth to illustrate distribution of tasks and information. 
For this demo, no actual information is sent or processed.

Eclipse run config arguments: `-gui -container-name "PriorAuth" -agents "elig:model.EligibilityAgent;man:model.ManagerAgent;med:model.MedNecAgent;prov:model.ProviderAgent;serv:model.ServiceAgent;loc:model.LevelOfCareAgent"`

Start the model and create a dummy agent. Sending a message with the `conversation-id` as "facility" will start agent interactions.

For a visual flow chart of agent interactions start a sniffer agent that focuses on all the agents.
