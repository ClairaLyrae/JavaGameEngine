package com.javagameengine.gui;

import java.util.ArrayList;



import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Scene;

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
		if(parent!=null)
		{
			absoluteX = parent.xPos + xPos;
			absoluteY = parent.yPos + yPos;
		}
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
		int i;
		
		for(i=0; i<children.size();i++)
		{
			children.get(i).onCreate();
		}
	}

}
