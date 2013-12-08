package com.javagameengine.gui;

import org.lwjgl.opengl.Display;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.math.Color4f;

public class HUD extends GUI{

	@Override
	public void create() {
		
		Crosshairs cursor = new Crosshairs();
		GLquadGUIcomponent laserCount = new GLquadGUIcomponent(200, 30, 
				Display.getWidth() - 200, Display.getHeight() - 31,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
		laserCount.addChild(new TextBox(5, 5, "LASERS SHOT:", Color4f.black));
		laserCount.addChild(new LaserCountText(100, 5, "0", Color4f.black));
		
		
		rootComponents.add(cursor);
		rootComponents.add(laserCount);
	}
	

}
