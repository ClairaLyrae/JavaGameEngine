package com.simple3d.scene.component;

import org.lwjgl.util.Renderable;

import com.simple3d.Graphics;
import com.simple3d.Logic;
import com.simple3d.Physics;
import com.simple3d.events.EventMethod;
import com.simple3d.events.KeyEvent;
import com.simple3d.events.Listener;
import com.simple3d.scene.Component;

public class TestComponent extends Component implements Logic, Physics, Graphics, Listener
{
	int s;
	
	public TestComponent(int s)
	{
		this.s = s;
	}

	@EventMethod
	public void onKeyEvent(KeyEvent e)
	{
		System.out.println("Component[" + s + "] : Key MATCHED!");
	}
	
	@Override
	public void logic(int delta)
	{
	}

	@Override
	public void graphics()
	{
	}

	@Override
	public void physics(int delta)
	{
	}
}
