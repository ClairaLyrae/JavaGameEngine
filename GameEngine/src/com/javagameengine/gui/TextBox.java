package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.math.Color4f;

public class TextBox extends GUIcomponent {
	
	public TextBox(int w, int h, int x, int y, String t, Color4f color)
	{
		backgroundColor = new Color4f(1f, 0f, 0f, .5f);
		borderColor = new Color4f(0f, 0f, 0f, 0f);
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		text = t;
		textColor = color;
		children = new ArrayList<GUIcomponent>();
		
	}

	@Override
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
