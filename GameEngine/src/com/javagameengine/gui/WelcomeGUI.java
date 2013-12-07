package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.testGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;


public class WelcomeGUI extends GUI {

	
	@Override
	public void create() {
		
		testGUIcomponent mainBox = new testGUIcomponent(300, 400, centerX-150, centerY-200,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		mainBox.addChild(new testButton(100, 100, 50, 50, Color4f.black.setTrans(), 
				Color4f.black.setTrans(), null));
		mainBox.addChild(new TextBox(20, 350, "Welcome", Color4f.black));
		
		rootComponents.add(mainBox);
	}

}
