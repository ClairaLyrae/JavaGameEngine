package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.GLquadGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;


public class WelcomeGUI extends GUI {

	
	@Override
	public void create() {
		
		
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(Display.getWidth(), Display.getHeight(), 0, 0,
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(340, 300, centerX-170, centerY-150,
				Color4f.white.setTrans(), Color4f.black.setTrans(.5f), null);
		
		// Start Button
		Button startButt = new Button(80, 20, 25, 20, "START", 0){
			@Override
			public void unClick(){
				Game.getHandle().setActiveScene("3d");
			}
		};
		
		
		// Settings Button
		Button settingsButt = new Button(80, 20, 130, 20, "SETTINGS", 0){
			@Override
			public void unClick(){
				System.out.println("Settings onClick called.");
				Game.getHandle().getActiveScene().setGUI(AssetManager.getGUI("settingsgui"));
			}
		};
		
		
		// Quit Button
		Button quitButt = new Button(80, 20, 235, 20, "QUIT", 0){
			public void unClick(){
				Game.getHandle().exit();
			}
		};
		
		
		innerBox.addChild(startButt);
		innerBox.addChild(settingsButt);
		innerBox.addChild(quitButt);
		innerBox.addChild(new TextBox(90, 200, "L I G H T W E I G H T", Color4f.red));
		innerBox.addChild(new TextBox(108, 180, "Game Engine Demo", Color4f.white));


	//	innerBox.addChild(new TextBox(20, 350, "Welcome", Color4f.black));
		
		this.addComponent(innerBox);
	}
	


}
