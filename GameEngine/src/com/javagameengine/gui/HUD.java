package com.javagameengine.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.math.Color4f;

public class HUD extends GUI implements Listener{

	@Override
	public void create() {
		
		Crosshairs cursor = new Crosshairs();
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(0, 0, 0, 0,
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		GLquadGUIcomponent laserCount = new GLquadGUIcomponent(200, 30, 
				Display.getWidth() - 200, Display.getHeight() - 31,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		mainBox.addChild(laserCount);
		laserCount.addChild(new TextBox(5, 5, "LASERS SHOT:", Color4f.black));
		laserCount.addChild(new LaserCountText(100, 5, "0", Color4f.black));
		
		Button PauseListener = new Button(0, 0, 0, 0, null, 0){
			@Override
			@EventMethod
			public void onKey(KeyPressEvent e)
			{
				System.out.println("Key Event");
				if(!e.state())
					return;
				if(e.getKey() == Keyboard.KEY_ESCAPE)
					Game.getHandle().getActiveScene().setGUI(new PauseGUI());
			}
		};
		
		rootComponents.add(cursor);
		rootComponents.add(PauseListener);
		rootComponents.add(mainBox);
		
	}
	
	
	@EventMethod
	public void onKey(KeyPressEvent e)
	{
		System.out.println("Key Event");
		if(!e.state())
			return;
		if(e.getKey() == Keyboard.KEY_ESCAPE)
			Game.getHandle().getActiveScene().setGUI(new PauseGUI());
	}
	

}
