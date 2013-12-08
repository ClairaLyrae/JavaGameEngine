package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.math.Color4f;

public class TextBox extends GUIcomponent {
	
	public TextBox(int x, int y, String t, Color4f color)
	{
		backgroundColor = new Color4f(1f, 0f, 0f, .5f);
		borderColor = new Color4f(0f, 0f, 0f, 0f);
		width = t.length()*8;
		height = 10;
		xPos = x+1;
		yPos = y+1;
		text = t;
		textColor = color;
		children = new ArrayList<GUIcomponent>();
		
	}

	@Override
	public void onUpdate(float delta) {
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
