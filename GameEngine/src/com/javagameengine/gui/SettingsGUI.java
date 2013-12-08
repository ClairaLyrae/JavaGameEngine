package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.GLquadGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;


public class SettingsGUI extends GUI {

	
	@Override
	public void create() {
		
		//Main boxes
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(Display.getWidth(), Display.getHeight(), 0, 0,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(400, 500, centerX-200, centerY-250,
				Color4f.white.setTrans(), Color4f.red.setTrans(), null);
				
		//Slider box
		GLquadGUIcomponent slider = new GLquadGUIcomponent(100, 2, 225, 375,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		
		//Headings
		innerBox.addChild(new TextBox(25, 450, "Audio Settings", Color4f.white));
		innerBox.addChild(new TextBox(40, 410, "Sound:", Color4f.white));
		innerBox.addChild(new TextBox(40, 370, "Volume:", Color4f.white));
		innerBox.addChild(new TextBox(40, 330, "Background Music:", Color4f.white));
		innerBox.addChild(new TextBox(25, 290, "Display Settings", Color4f.white));
		innerBox.addChild(new TextBox(40, 250, "Crosshair:", Color4f.white));
		innerBox.addChild(new TextBox(40, 210, "Resolution:", Color4f.white));
		innerBox.addChild(new TextBox(40, 170, "Full Screen:", Color4f.white));
		innerBox.addChild(new TextBox(40, 130, "VSync:", Color4f.white));
		innerBox.addChild(new TextBox(40, 90, "Multisampling:", Color4f.white));
		
		
		//Sound on/off
		Button sound_onoff = new Button(50, 20, 250, 405, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(sound_onoff);
		
		
		//Volume Slider
		Button volume_slider = new Button(10, 25, 270, 365, null, 0)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(volume_slider);
		
		
		//Music on/off
		Button music_onoff = new Button(50, 20, 250, 325, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(music_onoff);
		
		
		//Crosshair
		Button crosshair_onoff = new Button(50, 20, 250, 245, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(crosshair_onoff);
		
		
		//Resolution
		
		
		//FullScreen
		Button fullscreen_onoff = new Button(50, 20, 250, 165, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(fullscreen_onoff);
		
		
		//VSync
		Button vsync_onoff = new Button(50, 20, 250, 125, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(vsync_onoff);
		
		
		//Multisamp
		Button mult_onoff = new Button(50, 20, 250, 85, "On", 1)
		{
			@Override
			public void onClick(){
			}
		}
		innerBox.addChild(mult_onoff);
				
				
		mainBox.addChild(innerBox);
		innerBox.addChild(slider);

		
		Texture t = AssetManager.getTexture("starBackground");
		if(t == null)
			System.out.println("CADSKJGHSKJDG");
	//	mainBox.setBackground(t);
		
		rootComponents.add(mainBox);
	}

}
