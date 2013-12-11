package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.math.Color4f;

public class Slider extends GUIcomponent implements Listener
{
	float sliderPos = 0f;
	Color4f lineColor;
	Color4f tabColor;
	

    public Slider(int w, int h, int x, int y, Color4f lineColor, Color4f tabColor, float initial) 
    {
    	sliderPos = initial;
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = Color4f.red.setTrans(0f);
		backgroundColor = Color4f.red.setTrans(0f);
		this.lineColor = lineColor;
		this.tabColor = tabColor;
		parent = null;
		children = new ArrayList<GUIcomponent>();
    }
	
	@EventMethod
	public void onMouseMove(MouseMoveEvent e)
	{
		if(e.isCancelled() || !Mouse.isButtonDown(0))
			return;
		int mousePastX = e.getPastX();
		int mousePastY = e.getPastY();
		if(mousePastY > this.absoluteY + this.height || mousePastY < this.absoluteY)
			return;
		if(mousePastX > this.absoluteX + this.width || mousePastX < this.absoluteX)
			return;
		
		float moved = (float)e.getDeltaX()/width;
		float sliderPost = sliderPos + moved;
		if(sliderPost > 1f)
			sliderPost = 1f;
		if(sliderPost < 0f)
			sliderPost = 0f;
		setSliderValue(sliderPost);
	}

	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		if(e.isCancelled() || !Mouse.isButtonDown(0))
			return;
		int mousePastX = e.getX();
		int mousePastY = e.getY();
		if(mousePastY > this.absoluteY + this.height || mousePastY < this.absoluteY)
			return;
		
		float clickpos = (float)(mousePastX - absoluteX)/width;
		if(clickpos > 1f || clickpos < 0f)
			return;
		setSliderValue(clickpos);
	}
	
	public float getSliderValue()
	{
		return sliderPos;
	}
	
	public void setSliderValue(float f)
	{
		sliderPos = f;
		onSliderAdjust(f);
	}
	
	public void onSliderAdjust(float f) {}

	@Override
	public void draw()
	{
		if(!visible)
			return;
		float tabAbsoluteX = absoluteX + sliderPos*width;

		float tabWidth = 5f;
		float lineWidth = 1f;
		float halfHeight = absoluteY + (height/2f);
		
		glBegin(GL_QUADS);
		glColor4f(lineColor.r, lineColor.g, lineColor.b, lineColor.a);
		glVertex2f(absoluteX - tabWidth, halfHeight - lineWidth); // bottom left
		glVertex2f(absoluteX - tabWidth, halfHeight + lineWidth); // top left
		glVertex2f(absoluteX + width + tabWidth, halfHeight + lineWidth); // top right
		glVertex2f(absoluteX + width + tabWidth, halfHeight - lineWidth); // bottom right
		glEnd();
		
		glBegin(GL_QUADS);
		glColor4f(tabColor.r, tabColor.g, tabColor.b, tabColor.a);
		glVertex2f(tabAbsoluteX - tabWidth, absoluteY); // bottom left
		glVertex2f(tabAbsoluteX - tabWidth, absoluteY + height); // top left
		glVertex2f(tabAbsoluteX + tabWidth, absoluteY + height); // top right
		glVertex2f(tabAbsoluteX + tabWidth, absoluteY); // bottom right
		glEnd();
		
		// draw children of current component
		drawChildren();
	}

	@Override
	public void onUpdate(float delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy()
	{
		getGUI().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		getGUI().getEventManager().registerListener(this);
	}

}
