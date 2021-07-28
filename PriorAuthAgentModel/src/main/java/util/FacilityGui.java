package util;

import model.FacilityAgent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FacilityGui extends JFrame {
	
	private FacilityAgent myAgent;
	
	
	public FacilityGui(FacilityAgent a) {
		super(a.getLocalName());
		
		myAgent = a;
	
		JPanel p = new JPanel();
		
		getContentPane().add(p, BorderLayout.CENTER);
		JLabel lab = new JLabel("Click here to start demo:");
		p.add(lab);
		JButton startButton = new JButton("Start Demo");

		startButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				myAgent.startDemo();
				startButton.setVisible(false);
				lab.setText("Demo Started");
				
			}
		} );
		p = new JPanel();
		p.add(startButton);
		getContentPane().add(p, BorderLayout.SOUTH);
		
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth()*3 / 4;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}	

}
