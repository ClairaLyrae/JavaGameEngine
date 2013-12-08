package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.component.LaserShot;


public abstract class Button extends GUIcomponent implements Listener {
	
	boolean clicked;
	
	public Button()
	{
		clicked = false;
	}
	
    public Button(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p, String[] args) {
    	
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
    
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		int x, y;
		if(e.isCancelled() || e.getButton() != 0 || !e.state())
			return;
		x = e.getX();
		y = e.getY();
		if(x > this.xPos && x < this.xPos && y > this.yPos && y < this.yPos)
		{
			
		}
	}

	public abstract void onUpdate(float delta);
	public abstract void onDestroy();
	public abstract void onCreate();

}
