package com.javagameengine.scene.component;

import org.lwjgl.input.Keyboard;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.scene.Component;

public class TestSceneDebugger extends Component implements Listener
{
	@EventMethod
	public void onKeyEvent(KeyEvent e)
	{
		if(e.isCancelled())
			return;
		if(e.state() && e.getKey() == Keyboard.KEY_P)
		{
			System.out.println("Printing scene...");
			getScene().print();
		}
	}

	@Override
	public void onDestroy()
	{
		if(getScene() != null)
			getScene().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		if(getScene() != null)
			getScene().getEventManager().registerListener(this);
	}

	public void onUpdate(int delta)
	{
	}
}
