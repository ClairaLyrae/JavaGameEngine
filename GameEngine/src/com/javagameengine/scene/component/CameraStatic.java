package com.javagameengine.scene.component;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Scene;

// TODO Claira - working on this, will require a fair amount of fiddling with other things. Wes, if you want to help with this it will need some openGL work on view changes that im not clear on... let me know

/**
 * @author ClairaLyrae
 * Provides a movable camera view in component form to render the scene from.
 */
public class CameraStatic extends Component implements Listener
{
	@EventMethod
	public void onMouseMove(MouseMoveEvent e)
	{
		if(!Mouse.isButtonDown(2) || e.isCancelled())
			return;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			Renderer.camerat.translate(0.1f*e.getDeltaX(), 0f, 0f);
			Renderer.camerat.translate(0f, 0.1f*e.getDeltaY(), 0f);
		}
		else
		{
			Renderer.camerat.rotate(0.01f*e.getDeltaX(), 0f, 1f, 0f);
			Renderer.camerat.rotate(0.01f*e.getDeltaY(), 2f, 0f, 0f);
		}
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(e.isCancelled())
			return;
		float s = e.getAmount();
		if(s > 0f)
			s *= 2;
		else
			s *= -0.5;
		Renderer.camerat.scale(s, s, s);
	}

	public void onDestroy()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().unregisterListener(this);
	}

	public void onCreate()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().registerListener(this);
	}

	public void onUpdate(float delta)
	{
	}
}
