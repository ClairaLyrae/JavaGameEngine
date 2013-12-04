package com.javagameengine.gui;

import java.util.ArrayList;


import com.javagameengine.math.Color4f;

public class testGUIcomponent extends GUIcomponent {

	public testGUIcomponent(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p)
	{
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = borC;
		backgroundColor = bgC;
		parent = p;
		children = new ArrayList<GUIcomponent>();
	}
	
	//@Override
	public void onUpdate(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

	}

}
