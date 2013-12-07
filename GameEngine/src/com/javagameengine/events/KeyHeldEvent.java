package com.javagameengine.events;

import org.lwjgl.input.Keyboard;

public class KeyHeldEvent extends KeyEvent
{	
	long ms_held;
	
	/**
	 * Creates a new KeyHeldEvent
	 * @param 	key			The key pressed (given by the LWJGL Keyboard class)
	 * @param 	c			The key character pressed
	 * @param 	ms_held		Milliseconds the key has been held so far
	 */
	
	public KeyHeldEvent(int key, char c, long ms_held)
	{
		super(key, c);
		this.ms_held = ms_held;
	}
	
	/**
	 * @return State of key after this event (true if pressed)
	 */
	public long getMillisecondsHeld()
	{
		return ms_held;
	}
	
	public String toString()
	{
		return String.format("KeyHeldEvent : Key[%s, %c], timeHeld[%d]", Keyboard.getKeyName(key), c, ms_held);
	}
}
