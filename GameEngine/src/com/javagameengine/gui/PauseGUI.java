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


public class PauseGUI extends GUI {

	
	@Override
	public void create() {
		
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(Display.getWidth(), Display.getHeight(), 0, 0,
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(400, 100, centerX-200, centerY-50,
				Color4f.white.setTrans(), Color4f.black.setTrans(.9f), null);
		
		// Return Button
		Button returnButt = new Button(100, 20, 25, 20, "RETURN", 0){
			@Override
			public void onClick(){
				Game.getHandle().getActiveScene().setGUI(new HUD());
			}
		};
		
		
		// MAIN MENU BUTTON
		Button menuButt = new Button(100, 20, 150, 20, "MAIN MENU", 0){
			@Override
			public void onClick(){
				Game.getHandle().getActiveScene().setGUI(new HUD());
				Game.getHandle().setActiveScene("menu3d");
			}
		};
		
		
		// Quit Button
		Button quitButt = new Button(100, 20, 275, 20, "QUIT", 0){
			public void onClick(){
				Game.getHandle().exit();
			}
		};
		
		
		innerBox.addChild(returnButt);
		innerBox.addChild(menuButt);
		innerBox.addChild(quitButt);


		innerBox.addChild(new TextBox(156, 70, "GAME PAUSED", Color4f.white));
		
		mainBox.addChild(innerBox);
	

		
		rootComponents.add(mainBox);
	}

}
