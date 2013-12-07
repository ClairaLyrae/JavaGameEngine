package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.javagameengine.console.Console;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.scene.RenderableComponent;
import com.javagameengine.scene.Scene;

public class CoordinateGrid extends RenderableComponent implements Renderable, Drawable
{

	private float tick;
	private float size;
	private boolean visible = true;
	
	public CoordinateGrid(float tick, float size)
	{
		this.tick = tick;
		this.size = size;
	}
	
	@Override
	public void draw()
	{
		if(!visible)
			return;
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glBegin(GL11.GL_LINES); // Start Drawing 
		
		if(tick > 0f)
		{
			GL11.glColor4f(1f, 1f, 1f, 0.06f); 
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
		}
		
		// Draw majors
		GL11.glColor4f(1f, 0f, 0f, 0.2f); 
		glVertex3f(size, 0f, 0f);
		glVertex3f(-size, 0f, 0f);
		GL11.glColor4f(0f, 1f, 0f, 0.2f); 
		glVertex3f(0f, size, 0f);
		glVertex3f(0f, -size, 0f);
		GL11.glColor4f(0f, 0f, 1f, 0.2f); 
		glVertex3f(0f, 0f, size);
		glVertex3f(0f, 0f, -size);
		
		glEnd();
		
	}

	@Override
	public void onDestroy()
	{
	}

	@Override
	public void onCreate()
	{
	}

	public void onUpdate(float delta)
	{
	}

	@Override
	public Matrix4f getMatrix()
	{
		return node.getWorldTransform().getTransformMatrix();
	}

	@Override
	public Bindable getBindable()
	{
		return null;
	}

	@Override
	public Drawable getDrawable()
	{
		return this;
	}
}
