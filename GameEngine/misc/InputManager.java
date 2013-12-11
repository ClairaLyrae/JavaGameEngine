package com.javagameengine.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;


public class InputManager
{
	private static float mouseSensitivity = 1f;
	private static boolean invertAxis = false;
	
	public static boolean isMouseInverted()
	{
		return invertAxis;
	}
	
	public static void setMouseInverted(boolean b)
	{
		invertAxis = b;
	}
	
	public static float getMouseSensitivity()
	{
		return mouseSensitivity;
	}
	
	public static void setMouseSensitivity(float f)
	{
		mouseSensitivity = f;
	}
	
	private static List<KeyBinding> keyBindings = new ArrayList<KeyBinding>();
	
	private static boolean[] keyStates = new boolean[Keyboard.KEYBOARD_SIZE];
	
	public static void registerKeyBinding(KeyBinding kb)
	{
		keyBindings.add(kb);
	}

	public static void unregisterKeyBinding(KeyBinding kb)
	{
		keyBindings.remove(kb);
	}
	
	public static boolean unregisterKeyBinding(String s)
	{
		KeyBinding kb = getKeyBinding(s);
		if(kb == null)
			return false;
		keyBindings.remove(kb);
		return true;
	}
	
	public static KeyBinding getKeyBinding(String name)
	{
		for(KeyBinding kb : keyBindings)
		{
			if(kb.getName().equals(name))
				return kb;
		}
		return null;
	}
	
	public static List<KeyBinding> getPressedKeyBindings()
	{
		ArrayList<KeyBinding> pressed = new ArrayList<KeyBinding>();
		for(KeyBinding kb : keyBindings)
		{
			if(kb.isPressed())
				pressed.add(kb);
		}
		return pressed;
	}
	
	public static List<Integer> getPressedKeys()
	{
		ArrayList<Integer> pressed = new ArrayList<Integer>();
		for(int i = 0; i < keyStates.length; i++)
		{
			if(keyStates[i])
				pressed.add(i);
		}
		return pressed;
	}
	
	public static void callEvents()
	{
		callMouseEvents();
		callKeyboardEvents();
	}
	
	public static void callKeyboardEvents()
	{
		for(int i = 0; i < keyStates.length; i++)
		{
			keyStates[i] = Keyboard.isKeyDown(i);
			if(keyStates[i])
				EventManager.global.callEvent(new KeyHeldEvent(i, ' ', 16));
		}
		while(Keyboard.next())
		{
			KeyPressEvent e;
			int key = Keyboard.getEventKey();
			boolean isPress = Keyboard.isKeyDown(key);
			char c = Keyboard.getEventCharacter();
			e = new KeyPressEvent(key, c, isPress);
			EventManager.global.callEvent(e);
		}
	}
	
	public static void callMouseEvents()
	{
		while(Mouse.next())
		{
			MouseEvent e;
			int x = Mouse.getEventX();
			int y = Mouse.getEventY();
			int dx = Mouse.getEventDX();
			int dy = Mouse.getEventDY();
			int dw = Mouse.getEventDWheel();
			boolean buttonState = Mouse.getEventButtonState();
			int button = Mouse.getEventButton();
			if(dx != 0 || dy != 0)
			{
				e = new MouseMoveEvent(x, y, dx, dy);
				EventManager.global.callEvent(e);				
			}
			if(Mouse.hasWheel() && dw != 0)
			{
				if(dw > 0)	// We have to normalize it, because getEventDWheel returns a totally different number based on mouse
					dw = 1;
				else
					dw = -1;
				e = new MouseScrollEvent(dw);
				EventManager.global.callEvent(e);
			}
			if(button >= 0)
			{
				e = new MouseClickEvent(x, y, button, buttonState);
				EventManager.global.callEvent(e);
			}
		}
	}
}
