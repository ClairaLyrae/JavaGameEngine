package com.javagameengine.events;

import org.lwjgl.input.Keyboard;

/**
 * Describes a keyboard event in which a key is pressed, released, or held. 
 * @author ClairaLyrae
 */
public class KeyPressEvent extends KeyEvent
{
	private boolean state;
	
	/**
	 * Creates a new KeyEvent
	 * @param 	key			The key pressed (given by the LWJGL Keyboard class)
	 * @param 	c			The key character pressed
	 * @param 	state		The state of the key after this event (true if pressed)
	 */
	
	public KeyPressEvent(int key, char c, boolean state)
	{
		super(key, c);
		this.state = state;
	}
	
	/**
	 * @return State of key after this event (true if pressed)
	 */
	public boolean state()
	{
		return state;
	}
	
	public String toString()
	{
		return String.format("KeyEvent : Key[%s, %c], State[%b]", Keyboard.getKeyName(key), c, state);
	}
}
