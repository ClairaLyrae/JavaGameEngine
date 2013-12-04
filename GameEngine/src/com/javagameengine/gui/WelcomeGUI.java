package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.testGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;


public class WelcomeGUI extends GUI {

	
	@Override
	public void create() {
		
		testGUIcomponent mainBox = new testGUIcomponent(200, 200, centerX-100, centerY-100,
				Color4f.red, Color4f.red, null);
		mainBox.addChild(new testGUIcomponent(100, 100, 50, 50, Color4f.black, Color4f.black, null));
		
		rootComponents.add(mainBox);
	}

}
