package com.javagameengine.events;

import org.lwjgl.input.Keyboard;

/**
 * Describes a keyboard event in which a key is pressed, released, or held. 
 * @author ClairaLyrae
 */
public class KeyEvent extends InputEvent
{
	private int key;
	private boolean state;
	private long timeHeld;
	private char c;
	
	public enum Type{
		KEY_PRESSED,
		KEY_RELEASED,
		KEY_HELD;
	};
	
	/**
	 * Creates a new KeyEvent
	 * @param 	key			The key pressed (given by the LWJGL Keyboard class)
	 * @param 	c			The key character pressed
	 * @param 	state		The state of the key after this event (true if pressed)
	 * @param 	timeHeld	The amount of time since the last KeyEvent for this key
	 */
	public KeyEvent(int key, char c, boolean state, long timeHeld)
	{
		this.c = c;
		this.key = key;
		this.state = state;
		this.timeHeld = timeHeld;
	}
	
	public KeyEvent(int key, char c, boolean state)
	{
		this.c = c;
		this.key = key;
		this.state = state;
		this.timeHeld = 0;
	}
	
	/**
	 * @return Character represented by the key
	 */
	public char getChar()
	{
		return c;
	}
	
	/**
	 * @return Key int value reported by the LWJGL Keyboard class
	 */
	public int getKey()
	{
		return key;
	}
	
	/**
	 * @return State of key after this event (true if pressed)
	 */
	public boolean state()
	{
		return state;
	}
	
	/**
	 * @return True if key has been held since last frame
	 */
	public boolean isHeld()
	{
		return timeHeld != 0;
	}
	
	/**
	 * @return Time since last KeyEvent for this key
	 */
	public long getTimeHeld()
	{
		return timeHeld;
	}
	
	public String toString()
	{
		return String.format("KeyEvent : Key[%s, %c], State[%b], Time[%d]", Keyboard.getKeyName(key), c, state, timeHeld);
	}
}
