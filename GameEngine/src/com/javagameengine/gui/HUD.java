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
	
	// full power for laser is 100. This is also the width of the box
	private static int laserPower = 100;
	// increment laserPower at updaterate/updateDivisor
	private static int updateDivisor = 10;
	// how much to decrement laserPower every shot
	final private static int LASER_DEC = 5;
	// how much to increment laserPower every update
	final private static int LASER_INC = 1;
	// boolean to let LaserComponent know not to create new lasers
	public static boolean laserOutOfPower = false;

	@Override
	public void create() {
		
		Crosshairs cursor = new Crosshairs();
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(0, 0, 0, 0,
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		
		// add hint for pause command
		mainBox.addChild(new TextBox(10, Display.getHeight() - 15,
				"PRESS ESC TO PAUSE", Color4f.white.setTrans()));
		
		
		// ADD LASER SHOT COUNTER
		GLquadGUIcomponent laserCount = new GLquadGUIcomponent(205, 30, 
				Display.getWidth() - 200, Display.getHeight() - 31,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		laserCount.addChild(new TextBox(5, 5, "LASER POWER:", Color4f.black));
		GLquadGUIcomponent Lpower = new GLquadGUIcomponent(laserPower, 18, 100, 5, 
				Color4f.green.setTrans(0f), Color4f.green, null){
			@Override
			public void onUpdate(float delta) 
			{
				// update width of laser power box
				width = laserPower;
				
				// increase laser power based on time passing
				if(updateDivisor < 1)
				{
						if(laserPower <= 100 - LASER_INC)
							laserPower += LASER_INC;
						else
							laserPower = 100;
						
						updateDivisor = 10;
						HUD.laserOutOfPower = false;
				}
				else
					updateDivisor--;
			}
		};
		
		laserCount.addChild(Lpower);
	//	laserCount.addChild(new LaserCountText(100, 5, "0", Color4f.black));
		mainBox.addChild(laserCount);


		// ADD SOMETHING TO LISTEN FOR PAUSE COMMAND (ESC)
		Button PauseListener = new Button(0, 0, 0, 0, null, 0)	
		{
			@Override
			public void keyPressed(KeyPressEvent e)
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

	// called when a new laser is created from LaserComponent
	public static void decreaseLaserPower() {
		if(laserPower >= LASER_DEC)
			laserPower -= LASER_DEC;
		else
			laserPower = 0;
		
		if(laserPower <= 0)
		{
			// when laserPower is at zero, make updateDivisor greater than
			//  usual to make player wait extra time to shoot laser again
			updateDivisor = 50;
			HUD.laserOutOfPower = true;
		}
			
	}
	
	

	

}
