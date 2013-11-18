package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.javagameengine.console.Console;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.renderer.RenderState;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Scene;

public class CoordinateGrid extends Component implements Renderable, Listener
{

	private float tick;
	private float size;
	private boolean visible = true;
	
	public CoordinateGrid(float tick, float size)
	{
		this.tick = tick;
		this.size = size;
	}
	
	@EventMethod
	public void onKey(KeyEvent e)
	{
		if(e.isCancelled() || !e.state() || e.getKey() != Keyboard.KEY_G)
			return;
		visible = !visible;
		Console.println("Coordinate grid visibility is set to: " + visible);
	}
	
	@Override
	public void draw()
	{
		if(!visible)
			return;
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glColor3f(0.3f, 0.3f, 0.3f); 
		glBegin(GL11.GL_LINES); // Start Drawing 
		
		// Draw minors
		for(float i = 0; i <= size; i+=tick)
		{
			glVertex3f(-size, i, 0f);
			glVertex3f(+size, i, 0f);
			glVertex3f(-size, -i, 0f);
			glVertex3f(+size, -i, 0f);
			glVertex3f(i, -size, 0f);
			glVertex3f(i, +size, 0f);
			glVertex3f(-i, -size, 0f);
			glVertex3f(-i, +size, 0f);
		}
		for(float i = 0; i <= size; i+=tick)
		{
			glVertex3f(-size, 0f, i);
			glVertex3f(+size, 0f, i);
			glVertex3f(-size, 0f, -i);
			glVertex3f(+size, 0f, -i);
			glVertex3f(i, 0f, -size);
			glVertex3f(i, 0f, +size);
			glVertex3f(-i, 0f, -size);
			glVertex3f(-i, 0f, +size);
		}
		for(float i = 0; i <= size; i+=tick)
		{
			glVertex3f(0f, -size, i);
			glVertex3f(0f, +size, i);
			glVertex3f(0f, -size, -i);
			glVertex3f(0f, +size, -i);
			glVertex3f(0f, i, -size);
			glVertex3f(0f, i, +size);
			glVertex3f(0f, -i, -size);
			glVertex3f(0f, -i, +size);
		}
		
		// Draw majors
		glColor3f(0.5f, 0.5f, 0.5f); 
		glVertex3f(size, 0f, 0f);
		glVertex3f(-size, 0f, 0f);
		glVertex3f(0f, size, 0f);
		glVertex3f(0f, -size, 0f);
		glVertex3f(0f, 0f, size);
		glVertex3f(0f, 0f, -size);
		
		glEnd();
		
	}

	@Override
	public void onDestroy()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().registerListener(this);
	}

	public void onUpdate(int delta)
	{
	}

}
