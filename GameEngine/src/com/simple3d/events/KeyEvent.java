package com.simple3d.events;

import org.lwjgl.input.Keyboard;

public class KeyEvent extends InputEvent
{
	private int key;
	private boolean state;
	private long timeHeld;
	
	public enum Type{
		KEY_PRESSED,
		KEY_RELEASED,
		KEY_HELD;
	};
	
	public KeyEvent(int key, boolean state, long timeHeld)
	{
		this.key = key;
		this.state = state;
		this.timeHeld = timeHeld;
	}
	
	public KeyEvent(int key, boolean state)
	{
		this.key = key;
		this.state = state;
		this.timeHeld = 0;
	}
	
	public int getKey()
	{
		return key;
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
		return String.format("KeyEvent : Key[%s], State[%b], Time[%d]", Keyboard.getKeyName(key), state, timeHeld);
	}
}
