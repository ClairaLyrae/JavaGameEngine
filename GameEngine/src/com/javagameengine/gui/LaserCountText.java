package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.events.Listener;
import com.javagameengine.math.Color4f;

public class LaserCountText extends TextBox implements Listener {
	
	static int shotCount;

	
	public LaserCountText(int x, int y, String t, Color4f color)
	{
		backgroundColor = new Color4f(0f, 0f, 0f, 0f);
		borderColor = new Color4f(0f, 0f, 0f, 0f);
		width = t.length()*8;
		height = 10;
		xPos = x+1;
		yPos = y+1;
		text = t;
		textColor = color;
		children = new ArrayList<GUIcomponent>();
		shotCount = 0;
	}
	
	public static void increase()
	{
		shotCount++;
	}

	@Override
	public void onUpdate(float delta) 
	{
		text = Integer.toString(shotCount);
	}

	@Override
	public void onDestroy()
	{
		getGUI().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		getGUI().getEventManager().registerListener(this);
	}


}
