package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.javagameengine.Game;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.component.LaserShot;
import com.javagameengine.util.SimpleText;


public class Button extends GUIcomponent implements Listener{
	
	boolean clicked;
	int mode;
	
	public Button()
	{	
		borderColor = Color4f.red.setTrans();
		backgroundColor = Color4f.red.setTrans();
		parent = null;
		children = new ArrayList<GUIcomponent>();
		textColor = Color4f.black;
		clicked = false;
	}
	
    public Button(int w, int h, int x, int y, 
			String t, int m) {
    	
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		mode = m;
		clicked = false;
		borderColor = Color4f.red.setTrans();
		backgroundColor = Color4f.red.setTrans();
		parent = null;
		children = new ArrayList<GUIcomponent>();
		textColor = Color4f.black;
		text = t;
		if(text==null)
			getState();
		
		if(text!=null)
			addText();

			
	

    }
	


	protected void addText() {
		int x = (width/2) - (text.length()/2)*8;
		int y = (height/2) - 4;
		this.addChild(new TextBox(x, y, text, textColor));
	}
	
	protected void updateText(int index)
	{
		this.children.get(index).text = text;
		this.children.get(index).textColor = textColor;
	}
	
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		int x, y;
		x = e.getX();
		y = e.getY();
		
		
		// For momentary button
		if(mode == 0)
		{
			if(x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height)
			{
				if(e.state())
				{
					this.backgroundColor = this.backgroundColor.inverse();
					this.textColor = this.textColor.inverse();
					clicked = true;
					onClick();
				}
				else
				{
					this.backgroundColor = this.backgroundColor.inverse().setTrans();
					this.textColor = this.textColor.inverse();
					clicked = false;
					unClick();
				}
			}
			else if(clicked)
			{
				this.backgroundColor = this.backgroundColor.inverse().setTrans();
				this.textColor = this.textColor.inverse();
				clicked = false;
			}
		}
			
		
		// For On/Off button
		else if(mode == 1)	
		{


			if(e.isCancelled() || e.getButton() != 0 || !e.state())
				return;
	
			if(x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height)
			{
		
				if((x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height) ||
						!(x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height))
				{
					this.backgroundColor = this.backgroundColor.inverse();
					this.textColor = this.textColor.inverse();
					clicked = !clicked;
					unClick();
				}
			}
		}
		
		// Slider
		if(mode == 2)
		{
			// On the line?
			if(x > absoluteX-50 && x < absoluteX + width + 50 && y > absoluteY && y < absoluteY + height)
			{
				// In the button?
				if(e.state() && (x > absoluteX && x < absoluteX + width && y > absoluteY && y < absoluteY + height))
				{
					this.textColor = this.textColor.inverse();
					clicked = true;
					onClick();
				}
				else
				{
					this.textColor = this.textColor.inverse();
					clicked = false;
					unClick();
				}	
			}	
		}
	}
	
	public void onClick()
	{
		
	}
	
	public void unClick()
	{
		
	}
	
	public void getState() {
		
	}
	
	// Allow key press actions
	@EventMethod
	public void onKey(KeyPressEvent e)
	{
		keyPressed(e);
	}
	
	public void keyPressed(KeyPressEvent e) {
	
	}

	@Override
	public void draw()
	{

		
		if(!visible)
			return;
		
		if(texture == null)
		{
			glBegin(GL_QUADS);
			glColor4f(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
			glVertex2f(absoluteX, absoluteY); // bottom left
			glVertex2f(absoluteX, absoluteY + height); // top left
			glVertex2f(absoluteX + width, absoluteY + height); // top right
			glVertex2f(absoluteX + width, absoluteY); // bottom right
			glEnd();
		}
		else
		{
			texture.bind();
			glBegin(GL_QUADS);
			glColor4f(1f, 1f, 1f, 1f);
			GL11.glTexCoord2f(0f, 0f);
			glVertex2f(absoluteX, absoluteY); // bottom left
			GL11.glTexCoord2f(0f, 1f);
			glVertex2f(absoluteX, absoluteY + height); // top left
			GL11.glTexCoord2f(1f, 1f);
			glVertex2f(absoluteX + width, absoluteY + height); // top right
			GL11.glTexCoord2f(1f, 0f);
			glVertex2f(absoluteX + width, absoluteY); // bottom right
			glEnd();
			texture.unbind();
		}

		glBegin(GL11.GL_LINE_LOOP);
		glColor4f(borderColor.r, borderColor.g, borderColor.b, borderColor.a);
		glVertex2f(absoluteX, absoluteY); // bottom left
		glVertex2f(absoluteX, absoluteY + height); // top left
		glVertex2f(absoluteX + width, absoluteY + height); // top right
		glVertex2f(absoluteX + width, absoluteY); // bottom right
		glEnd();
		
		// draw children of current component
		drawChildren();
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
		getGUI().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		int i;
		
		getGUI().getEventManager().registerListener(this);
		
		for(i=0; i<children.size();i++)
		{
			children.get(i).onCreate();
		}
	}



}
