package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Scene;
import com.javagameengine.util.SimpleText;

import static org.lwjgl.opengl.GL11.glColor4f;


public class SettingsButton extends Button{
	
    public SettingsButton(int w, int h, int x, int y) {
    	
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		addText();

    }
	
	private void addText() {
		this.addChild(new TextBox(5, 5, "SETTINGS", Color4f.black));
	}

	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		System.out.println("Mouse click");
		int x, y;
		if(e.isCancelled() || e.getButton() != 0)
			return;
		x = e.getX();
		y = e.getY();
		if(x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height)
		{
			System.out.println("Button click");

			if(e.state())
			{
				this.backgroundColor = this.backgroundColor.inverse();
				this.textColor = this.textColor.inverse();
	//			this.text = "Clicked!";
			}
			else
			{
				this.backgroundColor = this.backgroundColor.inverse().setTrans();
				this.textColor = this.textColor.inverse();
	//			this.text = "Not Clicked";
			}
			clicked = !clicked;

		}
	}

	@Override
	public void onUpdate(float delta) {
		int i;
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
	}


}


