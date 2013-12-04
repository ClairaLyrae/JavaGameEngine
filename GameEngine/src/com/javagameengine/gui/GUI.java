package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.javagameengine.console.Console;

public abstract class GUI {
	
	static private boolean visible = true;
	int width;
	int height;
	int centerX;
	int centerY;
	ArrayList<GUIcomponent> rootComponents; 
	
	public GUI(){
		
		height = Display.getHeight();
		width = Display.getWidth();
		centerX = width/2;
		centerY = height/2;
		rootComponents = new ArrayList<GUIcomponent>();
		create();
	}
	
	public GUI(int h, int w){
		
		height = h;
		width = w;
		centerX = width/2;
		centerY = height/2;
		rootComponents = new ArrayList<GUIcomponent>();
		create();
	}
	

	public void draw()
	{
		int i;
		
		if(!visible)
			return;
		


		for(i=0; i<rootComponents.size();i++)
		{
			rootComponents.get(i).draw();
		}
	}
	
	public abstract void create();
	
}
