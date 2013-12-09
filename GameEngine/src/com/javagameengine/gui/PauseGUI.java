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
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(300, 100, centerX-170, centerY-150,
				Color4f.white.setTrans(), Color4f.black.setTrans(.9f), null);
		
		// Return Button
		Button returnButt = new Button(80, 20, 25, 20, "RETURN TO GAME", 0){
			@Override
			public void onClick(){
				Game.getHandle().getActiveScene().setGUI(new HUD());
			}
		};
		
		
		// MAIN MENU BUTTON
		Button menuButt = new Button(80, 20, 130, 20, "MAIN MENU", 0){
			@Override
			public void onClick(){
				Game.getHandle().setActiveScene("menu3d");
			}
		};
		
		
		// Quit Button
		Button quitButt = new Button(80, 20, 235, 20, "QUIT", 0){
			public void onClick(){
				Game.getHandle().exit();
			}
		};
		
		
		innerBox.addChild(returnButt);
		innerBox.addChild(menuButt);
		innerBox.addChild(quitButt);


		innerBox.addChild(new TextBox(106, 40, "GAME PAUSED", Color4f.black));
		
		mainBox.addChild(innerBox);
	

		
		rootComponents.add(mainBox);
	}

}
