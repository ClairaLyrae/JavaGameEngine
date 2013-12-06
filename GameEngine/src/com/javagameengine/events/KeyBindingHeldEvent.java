package com.javagameengine.events;

import org.lwjgl.input.Keyboard;



/**
 * Describes a keyboard event in which a key binding is pressed or released. 
 * @author ClairaLyrae
 */
public class KeyBindingHeldEvent extends Event
{
	private KeyBinding binding;
	private int ms;
	
	/**
	 * Creates a new KeyBindingPressEvent
	 * @param	name		The name of the key binding that was pressed
	 * @param 	state		The state of the key after this event (true if pressed)
	 */
	
	public KeyBindingHeldEvent(KeyBinding keyBind, int ms)
	{
		this.binding = keyBind;
		this.ms = ms;
	}
	
	/**
	 * @return State of key binding after this event (true if pressed)
	 */
	public int getDeltaHeld()
	{
		return ms;
	}
	
	public KeyBinding getKeyBinding()
	{
		return binding;
	}
	
	public String toString()
	{
		return String.format("KeyBindingPressEvent : KeyBinding[" + binding + "], DeltaHeld[%d]", ms);
	}
}

