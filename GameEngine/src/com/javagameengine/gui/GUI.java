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
import com.javagameengine.scene.Scene;

public abstract class GUI {
	
	static protected boolean visible = true;
	protected int width;
	protected int height;
	protected int centerX;
	protected int centerY;
	protected Scene scene;
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

	
	public void setScene(Scene newScene)
	{
		int i;
		
		scene = newScene;
		
		for(i=0; i<rootComponents.size();i++)
		{
			rootComponents.get(i).setScene(scene);
		}
	}
	
	
	public Scene getScene()
	{
		return scene;
		
	}

	public void onCreate() {
		int i;
		
		for(i=0; i<rootComponents.size();i++)
		{
			rootComponents.get(i).onCreate();
		}
	}
	
	public void onUpdate(int delta)
	{
		int i;
		
		for(i=0; i<rootComponents.size();i++)
		{
			rootComponents.get(i).onUpdate(delta);
		}
	}
	
}
