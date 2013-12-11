package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.math.Color4f;

public class DiscreteSlider extends Slider
{
	private int ticks;
	private int currentTick;
	
	public DiscreteSlider(int w, int h, int x, int y, Color4f lineColor, Color4f tabColor, int ticks, int initial)
	{
		super(w, h, x, y, lineColor, tabColor, initial/ticks);
		this.ticks = ticks;
	}
	
	@Override
	public void setSliderValue(float f)
	{
		currentTick = (int)(f*(float)(ticks-1));
		sliderPos = (float)currentTick;
		
		onSliderAdjust(sliderPos);
		onSliderAdjustDiscrete(currentTick);
	}
	
	public void draw()
	{

		if(!visible)
			return;

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
		
		float tickInterval = (float)(width*(ticks + 1))/(float)(ticks*ticks);
		
		for(int i = 0; i < ticks; i++)
		{
			float tickStart = absoluteX + i*tickInterval;
			glBegin(GL_QUADS);
			glColor4f(lineColor.r, lineColor.g, lineColor.b, lineColor.a);
			glVertex2f(tickStart - lineWidth, halfHeight - height/4f); // bottom left
			glVertex2f(tickStart - lineWidth, halfHeight + height/4f); // top left
			glVertex2f(tickStart + lineWidth, halfHeight + height/4f); // top right
			glVertex2f(tickStart + lineWidth, halfHeight - height/4f); // bottom right
			glEnd();
		}

		float tabAbsoluteX = absoluteX + currentTick*tickInterval;
		
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
	
	public void onSliderAdjustDiscrete(int val)
	{
		
	}
}
