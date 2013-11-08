package com.simple3d.events;

public class MouseClickEvent extends MouseEvent
{
	private int button;
	private boolean state;
	private long timeHeld;
	
	public MouseClickEvent(int key, boolean state, long timeHeld)
	{
		this.button = key;
		this.state = state;
		this.timeHeld = timeHeld;
	}
	
	public MouseClickEvent(int key, boolean state)
	{
		this.button = key;
		this.state = state;
		this.timeHeld = 0;
	}
	
	public boolean state()
	{
		return state;
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
