package com.javagameengine.gui;

import java.util.ArrayList;





import com.javagameengine.assets.material.Texture;
import com.javagameengine.events.Listener;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Scene;

/**
 * GLquadGUIcomponent extends GUIcomponent and describes a box (GL_QUAD) which is part
 * of a gui
 *
 */
public class GLquadGUIcomponent extends GUIcomponent {

	public GLquadGUIcomponent(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p)
	{
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = borC;
		backgroundColor = bgC;
		parent = null;
		
		children = new ArrayList<GUIcomponent>();
	}
	
	//public void 
	
	public void onUpdate(float delta) 
	{
	}

	@Override
	public void onDestroy() 
	{
	}

	@Override
	public void onCreate() 
	{
	}

}
