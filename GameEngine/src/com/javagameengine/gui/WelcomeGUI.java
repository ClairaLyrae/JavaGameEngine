package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

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
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(200, 400, centerX-100, centerY-200,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		innerBox.addChild(new testButton(100, 100, 50, 50, Color4f.white.setTrans(), 
				Color4f.black.setTrans(), null));
		innerBox.addChild(new TextBox(20, 350, "Welcome", Color4f.black));
		mainBox.addChild(innerBox);

		
		Texture t = AssetManager.getTexture("starBackground");
		if(t == null)
			System.out.println("CADSKJGHSKJDG");
		mainBox.setBackground(t);
		
		rootComponents.add(mainBox);
	}

}
