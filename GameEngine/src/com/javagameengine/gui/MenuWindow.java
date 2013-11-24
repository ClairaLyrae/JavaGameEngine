package com.javagameengine.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuWindow extends JFrame {
	
	private JLabel messageLabel;	// to display a message
	private final int LABEL_WIDTH = 400;	// The label's width
	private final int LABEL_HEIGHT = 200;	// The label's height
	
	// variables that reference menu components
	private JMenuBar menuBar;	// the menu bar
	private JMenu fileMenu;		// the file menu
	private JMenu settingsMenu;	// the settings menu
	private JMenuItem exitItem;	// an item to exit the application
	private JRadioButtonMenuItem blackItem;
	JCheckBoxMenuItem visibleItem;
	
	
	public MenuWindow()
	{
		// call the JFrame constructor
		super("Example Menu System");
		
		// specify an action for the close button
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create the message label and set its size and color
		messageLabel = new JLabel("WELCOME TO THIS THING", SwingConstants.CENTER);
		messageLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		messageLabel.setForeground(Color.BLACK);
		
		// add the label to the content pane.
		add(messageLabel);
		
		// build the menu bar.
		buildMenuBar();
		
		// pack and display the window
		pack();
		setVisible(true);
	}
	
	
	// buildMenuBar method build menu bar
	private void buildMenuBar()
	{
		// Create the menu bar.
		menuBar = new JMenuBar();
		
		// create the file and settings menus
		buildFileMenu();
		buildSettingsMenu();
		
		// add the file and settings menus to the menu bar
		menuBar.add(fileMenu);
		menuBar.add(settingsMenu);
		
		// set the window's menu bar
		setJMenuBar(menuBar);
	}
	
	// buildFileMenu method builds the File menu and returns a reference to its JMenu object
	private void buildFileMenu()
	{
		// create an Exit menu item
		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ExitListener());
		
		// create a JMenu object for the File menu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		// add the exit menu item to the File menu
		fileMenu.add(exitItem);
		
	}
	
	// buildSettingsMenu method build the Settings menu and returns a reference to its JMenu object
	private void buildSettingsMenu()
	{
		settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic(KeyEvent.VK_A);
	}
	
	// private inner class that handles the event that is generated when the user selects Exit from the File menu
	private class ExitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	// main method creates an instance of the MenuWindow class,
	//	which causes it to display its window
	public static void main(String[] args)
	{
		new MenuWindow();
	}
	

}
