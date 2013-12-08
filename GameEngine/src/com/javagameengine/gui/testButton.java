package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Scene;
import com.javagameengine.util.SimpleText;

import static org.lwjgl.opengl.GL11.glColor4f;


public class testButton extends Button{
	
    public testButton(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p) {
    	
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = borC;
		backgroundColor = bgC;
		parent = p;
		children = new ArrayList<GUIcomponent>();
		text = "Button";
		textColor = Color4f.blue;
		
		
    }
	
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		System.out.println("Mouse Clicked");
		int x, y;
		if(e.isCancelled() || e.getButton() != 0)
			return;
		x = e.getX();
		y = e.getY();
		if(x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height)
		{
			System.out.println("Button Clicked");

			if(e.state())
			{
				this.backgroundColor = Color4f.blue.setTrans();
				this.textColor = Color4f.blue.inverse();
				this.text = "Clicked!";
			}
			else
			{
				this.backgroundColor = Color4f.blue.inverse().setTrans();
				this.textColor = Color4f.blue;
				this.text = "Not Clicked";
			}
			clicked = !clicked;

		}
	}

	@Override
	public void onUpdate(int delta) {
		int i;
		System.out.println("Update");
		for(i=0; i<children.size();i++)
		{
			children.get(i).onUpdate(delta);
		}
	}

	@Override
	public void onDestroy()
	{
		int i;

		for(i=0; i<children.size();i++)
		{
			children.get(i).onDestroy();
		}
		getScene().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		int i;
		
		getScene().getEventManager().registerListener(this);
		
		for(i=0; i<children.size();i++)
		{
			children.get(i).onCreate();
		}
	
		getScene().getEventManager().registerListener(this);
	}


}
