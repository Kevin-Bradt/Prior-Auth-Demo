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
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(p, BorderLayout.NORTH);
		
		JButton stepButton = new JButton("Next step");
		stepButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				myAgent.stepDemo();
			}
		} );
		
		JLabel lab = new JLabel("Click here to start demo #1:");
		p.add(lab);
		JButton startButton = new JButton("Start Demo");
		startButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				myAgent.startDemo("PAform.xml");
//				startButton.setVisible(false);
//				lab.setText("Demo Started");
				
			}
		} );
		p.add(startButton);
		p.add(stepButton);
		
		p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(p, BorderLayout.CENTER);
		
		JLabel lab2 = new JLabel("Click here to start demo #2:");
		p.add(lab2);
		JButton startButton2 = new JButton("Start Demo");
		startButton2.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				myAgent.startDemo("PADenied.xml");
//				startButton.setVisible(false);
//				lab.setText("Demo Started");
				
			}
		} );
		p.add(startButton2);
		
		p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(p, BorderLayout.SOUTH);
		
		JLabel lab3 = new JLabel("Click here to start demo #3:");
		p.add(lab3);
		JButton startButton3 = new JButton("Start Demo");
		startButton3.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				myAgent.startDemo("PAform.xml");
//				startButton3.setVisible(false);
//				lab.setText("Demo Started");
				
			}
		} );
		p.add(startButton3);
		
		

		
		setResizable(true);
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
