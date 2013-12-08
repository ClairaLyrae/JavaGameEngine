package com.javagameengine.gui;

import org.lwjgl.opengl.Display;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.math.Color4f;

public class HUD extends GUI{

	@Override
	public void create() {
		
		Crosshairs cursor = new Crosshairs();
		
		rootComponents.add(cursor);
	}
	

}
