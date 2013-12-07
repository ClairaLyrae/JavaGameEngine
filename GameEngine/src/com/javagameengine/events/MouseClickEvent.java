package com.javagameengine.events;

/**
 * Event describing a mouse button being clicked.
 * @author ClairaLyrae
 */
public class MouseClickEvent extends MouseEvent
{
	private int button;
	private boolean state;
	private long timeHeld;
	private int x;
	private int y;
	
	
	public MouseClickEvent(int x, int y, int key, boolean state, long timeHeld)
	{
		this.x = x;
		this.y = y;
		this.button = key;
		this.state = state;
		this.timeHeld = timeHeld;
	}
	
	public MouseClickEvent(int x, int y, int key, boolean state)
	{
		this.x = x;
		this.y = y;
		this.button = key;
		this.state = state;
		this.timeHeld = 0;
	}
	
	public boolean state()
	{
		return state;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public boolean isHeld()
	{
		return timeHeld != 0;
	}
	public long getTimeHeld()
	{
		return timeHeld;
	}
	public String toString()
	{
		return String.format("MouseClickEvent : Button[%d], State[%b], TimeHeld[%d]", button, state, timeHeld);
	}
}
