package com.javagameengine.events;

import org.lwjgl.input.Keyboard;



/**
 * Describes a keyboard event in which a key binding is pressed or released. 
 * @author ClairaLyrae
 */
public class KeyBindingPressEvent extends Event
{
	private KeyBinding binding;
	private boolean state;
	
	/**
	 * Creates a new KeyBindingPressEvent
	 * @param	name		The name of the key binding that was pressed
	 * @param 	state		The state of the key after this event (true if pressed)
	 */
	
	public KeyBindingPressEvent(KeyBinding keyBind, boolean state)
	{
		this.binding = keyBind;
		this.state = state;
	}
	
	/**
	 * @return State of key binding after this event (true if pressed)
	 */
	public boolean state()
	{
		return state;
	}
	
	public KeyBinding getKeyBinding()
	{
		return binding;
	}
	
	public String toString()
	{
		return String.format("KeyBindingPressEvent : KeyBinding[" + binding + "], State[%b]", state);
	}
}

