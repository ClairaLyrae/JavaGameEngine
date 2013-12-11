package com.javagameengine.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

public final class KeyBinding
{
	private String name;
	private Set<Integer> keyList = new HashSet<Integer>();
	
	public KeyBinding(String name, List<Integer> keys)
	{
		this.name = name;
		keyList.addAll(keys);
	}
	
	public KeyBinding(String name, int ... keys)
	{
		this.name = name;
		for(int k : keys)
			this.keyList.add(k);
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasShift()
	{
		return keyList.contains(Keyboard.KEY_LSHIFT) || keyList.contains(Keyboard.KEY_RSHIFT);
	}
	
	public boolean hasCtrl()
	{
		return keyList.contains(Keyboard.KEY_LSHIFT) || keyList.contains(Keyboard.KEY_RSHIFT);
	}
	
	public boolean hasAlt()
	{
		return keyList.contains(Keyboard.KEY_RMENU) || keyList.contains(Keyboard.KEY_LMENU);
	}
	
	public boolean isPressed()
	{
		for(int key : keyList)
		{
			if(!Keyboard.isKeyDown(key))
				return false;
		}
		return true;
	}
	
	public static KeyBinding fromKeyboardState(String name)
	{
		return new KeyBinding(name, InputManager.getPressedKeys());
	}
}
