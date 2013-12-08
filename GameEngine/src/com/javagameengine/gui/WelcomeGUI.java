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
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(340, 300, centerX-170, centerY-150,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		Button startButt = new Button(80, 20, 25, 20, "START", 0){
			@Override
			public void onClick(){
				Game.getHandle().setActiveScene("3d");
			}
		};
		Button settingsButt = new Button(80, 20, 130, 20, "SETTINGS", 0){
			@Override
			public void onClick(){
				
			}
		};
		Button quitButt = new Button(80, 20, 235, 20, "QUIT", 0){
			public void onClick(){
				Game.getHandle().exit();
			}
		};
		
		innerBox.addChild(startButt);
		innerBox.addChild(settingsButt);
		innerBox.addChild(quitButt);

		innerBox.addChild(new TextBox(20, 350, "Welcome", Color4f.black));
		
		mainBox.addChild(innerBox);
	

		
		Texture t = AssetManager.getTexture("starBackground");
		if(t == null)
			System.out.println("CADSKJGHSKJDG");
//		mainBox.setBackground(t);
		
		rootComponents.add(mainBox);
	}

}
