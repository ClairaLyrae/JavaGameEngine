package com.javagameengine.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame implements ActionListener {
	
	private JPanel panel;
	private JLabel messageLabel;
	private JTextField textField;
	private JButton button;
	private final int WINDOW_WIDTH = 320;
	private final int WINDOW_HEIGHT = 100;
	
	public Window()
	{
		super("Welcome");
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// when closed, window goes away and does nothing else
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// center window
		setLocationRelativeTo(null);
		// add a FlowLayout manager to content pane.
//		setLayout(new FlowLayout());
		buildPanel();
		add(panel);
		setVisible(true);
		
	}
	
	private void buildPanel()
	{
		// create a label for the message
		messageLabel = new JLabel("Get ready. You're in for a real treat.");
		// create a text field
//		textField = new JTextField(10);
		// create a button
		button = new JButton("Start");
		
		button.addActionListener(this);
		
		panel = new JPanel();
		
		panel.add(messageLabel);
//		panel.add(textField);
		panel.add(button);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == button)
		{
			setVisible(false);
			JOptionPane.showMessageDialog(null, "Godspeed");
				
		}
	}
	

}
