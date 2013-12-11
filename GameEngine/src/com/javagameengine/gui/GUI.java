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
import com.javagameengine.events.EventManager;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.scene.Scene;


/**
 * GUI serves as a container for all gui components. GUIs can be built by instantiating
 * GUIcomponents and adding them to the list rootComponents. These rootComponents can 
 * have children added to them. Event manager can be used to keep track of gui
 * instantiations
 */
public abstract class GUI {
	
	private EventManager eventmanager = new EventManager();
	
	private boolean hasCursor = true;
	static protected boolean visible = true;
	protected int width;
	protected int height;
	protected int centerX;
	protected int centerY;
	protected Scene scene;
	ArrayList<GUIcomponent> rootComponents; 
	static protected boolean crosshairs_visible = true;
	
	public GUI()
	{
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

	public EventManager getEventManager()
	{
		return eventmanager;
	}
	
	public boolean hasCursor()
	{
		return hasCursor;
	}
	
	public void setCursor(boolean state)
	{
		hasCursor = state;
	}
	
	

	public void draw()
	{
		if(!visible)
			return;
		for(GUIcomponent c : rootComponents)
			c.draw();
	}
	
	public abstract void create();

	
	public void addComponent(GUIcomponent comp)
	{
		rootComponents.add(comp);
		comp.setGUI(this);
	}
	
	public void removeComponent(GUIcomponent comp)
	{
		rootComponents.remove(comp);
		comp.setGUI(null);
	}
	
	public void setScene(Scene newScene)
	{
		scene = newScene;
		for(GUIcomponent c : rootComponents)
			c.setGUI(this);
	}
	
	public Scene getScene()
	{
		return scene;
		
	}
	
	public void update(float delta)
	{
		for(GUIcomponent c : rootComponents)
			c.update(delta);
	}
	
	public void updateSize()
	{
		System.out.println("updateSize called");
		if(height != 0)
		{
			height = Display.getHeight();
			centerY = height/2;
		}
		if(width != 0)
		{
			width = Display.getWidth();
			centerX = width/2;
		}
		for(GUIcomponent c : rootComponents)
		{
			c.updateAbsolute();
		}
	}
	

}
