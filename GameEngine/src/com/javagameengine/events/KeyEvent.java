package com.javagameengine.events;

import org.lwjgl.input.Keyboard;

/**
 * Describes a keyboard event relating to a key on the keyboard
 */
public abstract class KeyEvent extends InputEvent
{
	protected int key;
	protected char c;
	
	/**
	 * Creates a new KeyEvent
	 * @param 	key			The key pressed (given by the LWJGL Keyboard class)
	 * @param 	c			The key character pressed
	 */
	public KeyEvent(int key, char c)
	{
		this.c = c;
		this.key = key;
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
}