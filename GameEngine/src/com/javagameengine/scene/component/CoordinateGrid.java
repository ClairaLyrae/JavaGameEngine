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

import org.lwjgl.opengl.GL11;

import com.javagameengine.graphics.RenderState;
import com.javagameengine.graphics.Renderable;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;

public class CoordinateGrid extends Component implements Renderable
{

	private float tick;
	private float size;
	
	public CoordinateGrid(float tick, float size)
	{
		this.tick = tick;
		this.size = size;
	}
	
	@Override
	public void draw()
	{
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
	public RenderState getRenderState()
	{
		return null;
	}

	@Override
	public Bounds getRenderBounds()
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		
	}

	@Override
	public void onCreate()
	{
		
	}

	public void onUpdate(int delta)
	{
	}

}
