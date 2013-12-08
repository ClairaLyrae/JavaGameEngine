package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.GLquadGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;


public class SettingsGUI extends GUI {

	
	@Override
	public void create() {
		
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(Display.getWidth(), Display.getHeight(), 0, 0,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(400, 500, centerX-200, centerY-250,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		
		//Slider box
		GLquadGUIcomponent slider = new GLquadGUIcomponent(100, 2, 225, 375,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		
		//Headings
		innerBox.addChild(new TextBox(25, 450, "Audio Settings", Color4f.white));
		innerBox.addChild(new TextBox(40, 410, "Sound:", Color4f.white));
		innerBox.addChild(new TextBox(40, 370, "Volume:", Color4f.white));
		innerBox.addChild(new TextBox(40, 330, "Background Music:", Color4f.white));
		innerBox.addChild(new TextBox(25, 290, "Display Settings", Color4f.white));
		innerBox.addChild(new TextBox(40, 250, "Crosshair:", Color4f.white));
		innerBox.addChild(new TextBox(40, 210, "Resolution:", Color4f.white));
		innerBox.addChild(new TextBox(40, 170, "Full Screen:", Color4f.white));
		innerBox.addChild(new TextBox(40, 130, "VSync:", Color4f.white));
		innerBox.addChild(new TextBox(40, 90, "Multisampling:", Color4f.white));
		
		
		//Sound on/off
		innerBox.addChild(new Button(50, 20, 250, 405, "On", 1));
		
		//Volume Slider
		innerBox.addChild(new Button(10, 25, 270, 365, null, 0));
		
		//Music on/off
		innerBox.addChild(new Button(50, 20, 250, 325, "On", 1));
		
		//Crosshair
		innerBox.addChild(new Button(50, 20, 250, 245, "On", 1));
		
		//Resolution
		
		//FullScreen
		innerBox.addChild(new Button(50, 20, 250, 165, "On", 1));
		
		//Crosshair
		innerBox.addChild(new Button(50, 20, 250, 125, "On", 1));
		
		//Multisamp
		innerBox.addChild(new Button(50, 20, 250, 85, "On", 1));
				
				
		mainBox.addChild(innerBox);
		innerBox.addChild(slider);

		
		Texture t = AssetManager.getTexture("starBackground");
		if(t == null)
			System.out.println("CADSKJGHSKJDG");
	//	mainBox.setBackground(t);
		
		rootComponents.add(mainBox);
	}

}
