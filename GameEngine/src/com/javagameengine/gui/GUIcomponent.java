package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.assets.material.Texture;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Scene;
import com.javagameengine.util.SimpleText;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.opengl.GL11;

public abstract class GUIcomponent {
	
	protected int width;
	protected int height;
	protected int xPos;
	protected int yPos;
	protected int absoluteX;
	protected int absoluteY;
	protected Color4f borderColor;
	protected Color4f backgroundColor;
	protected GUIcomponent parent;
	protected ArrayList<GUIcomponent> children;
	protected boolean visible = true;
	protected Scene scene;
	protected String text;
	protected Color4f textColor;
	protected Texture texture;
	

	public GUIcomponent()
	{
		absoluteX = 0;
		absoluteY = 0;
		text = null;
		textColor = Color4f.white;
		children = new ArrayList<GUIcomponent>();
	}
	
	/*
	public GUIcomponent(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p)
	{
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = borC;
		backgroundColor = bgC;
		parent = p;
		children = new ArrayList<GUIcomponent>();
		
		

	}
	*/
	
	public void setText(String s)
	{
		text = s;
	}

	public void setTextColor(Color4f color)
	{
		textColor = color;
	}
	
	public void setBackground(Texture t)
	{
		texture = t;
	}
	
	public void addChild(GUIcomponent newChild)
	{
		newChild.parent = this;
		if(newChild.parent!=null)
		{
			newChild.absoluteX = xPos + newChild.xPos;
			newChild.absoluteY = yPos + newChild.yPos;
		}
		// add scene to child
		this.children.add(newChild);
	}

	public void draw()
	{
		int parentX; 
		int parentY;
		
		if(parent == null)
		{
			parentX = 0;
			parentY = 0;
		}
		else
		{
			parentX = parent.xPos;
			parentY = parent.yPos;
		}
		
		if(!visible)
			return;
		
		if(texture == null)
		{
			glBegin(GL_QUADS);
			glColor4f(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
			glVertex2f(parentX + xPos, parentY + yPos); // bottom left
			glVertex2f(parentX + xPos, parentY + yPos + height); // top left
			glVertex2f(parentX + xPos + width, parentY + yPos + height); // top right
			glVertex2f(parentX + xPos + width, parentY + yPos); // bottom right
			glEnd();
		}
		else
		{
			texture.bind();
			glBegin(GL_QUADS);
			glColor4f(1f, 1f, 1f, 1f);
			GL11.glTexCoord2f(0f, 0f);
			glVertex2f(parentX + xPos, parentY + yPos); // bottom left
			GL11.glTexCoord2f(0f, 1f);
			glVertex2f(parentX + xPos, parentY + yPos + height); // top left
			GL11.glTexCoord2f(1f, 1f);
			glVertex2f(parentX + xPos + width, parentY + yPos + height); // top right
			GL11.glTexCoord2f(1f, 0f);
			glVertex2f(parentX + xPos + width, parentY + yPos); // bottom right
			glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}

		glBegin(GL11.GL_LINE_LOOP);
		glColor4f(borderColor.r, borderColor.g, borderColor.b, borderColor.a);
		glVertex2f(parentX + xPos, parentY + yPos); // bottom left
		glVertex2f(parentX + xPos, parentY + yPos + height); // top left
		glVertex2f(parentX + xPos + width, parentY + yPos + height); // top right
		glVertex2f(parentX + xPos + width, parentY + yPos); // bottom right
		glEnd();
		
		if(text != null)
		{
			glColor4f(textColor.r, textColor.g, textColor.b, textColor.a);
			SimpleText.drawString(text, parentX + xPos, parentY + yPos);
		}
		
		// draw children of current component
		drawChildren();
	}
	
	public void drawChildren()
	{
		int i;
		
		for(i=0;i<this.children.size();i++)
		{
			this.children.get(i).draw();
		}
	}
	
	
	public Scene getScene()
	{
		return scene;
	}
	
	public abstract void onUpdate(float delta);
	public abstract void onDestroy();
	public abstract void onCreate();

	public void setScene(Scene newScene) {
		
		int i;
		
		scene = newScene;
		
		for(i=0; i<children.size();i++)
		{
			children.get(i).setScene(scene);
		}
	}
}


