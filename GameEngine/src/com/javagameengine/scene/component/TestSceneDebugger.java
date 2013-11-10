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
		if(e.state() && e.getKey() == Keyboard.KEY_P)
		{
			if(getScene() != null)
			{
				System.out.println("Printing scene...");
				getScene().print();
				
			}
			else
				System.out.println("Printing scene failed! Cannot find scene.");
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
}
