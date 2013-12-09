package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.javagameengine.Game;
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
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(400, 500, centerX-200, centerY-250,
				Color4f.white.setTrans(), Color4f.black.setTrans(.9f), null);
				
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
		};
		innerBox.addChild(sound_onoff);
		
		
		//Volume Slider
		Button volume_slider = new Button(10, 25, 270, 365, null, 0)
		{
			@Override
			public void onClick(){
			}
		};
		innerBox.addChild(volume_slider);
		
		
		//Music on/off
		Button music_onoff = new Button(50, 20, 250, 325, "On", 1)
		{
			@Override
			public void onClick(){
			}
		};
		innerBox.addChild(music_onoff);
		
		
		//Crosshair
		Button crosshair_onoff = new Button(50, 20, 250, 245, "ON", 1)
		{
			@Override
			public void unClick(){
				if(this.clicked)
				{
					System.out.println("Crosshairs off");
					GUI.crosshairs_visible = false;
					this.text = "OFF";
				}
				else
				{
					System.out.println("Crosshairs on");
					GUI.crosshairs_visible = true;
					this.text = "ON";
				}
			}
			
	/*		@Override
			protected void addText() {
				String t;
				if(GUI.crosshairs_visible)
					t = "ON";
				else
					t = "OFF";
				int x = (width/2) - (t.length()/2)*8;
				int y = (height/2) - 4;
				this.addChild(new TextBox(x, y, t, textColor));
			}*/
		};
		innerBox.addChild(crosshair_onoff);
		
		
		//Resolution
		
		
		//FullScreen
		Button fullscreen_onoff = new Button(50, 20, 250, 165, "On", 1)
		{
			@Override
			public void onClick(){
			}
		};
		innerBox.addChild(fullscreen_onoff);
		
		
		//VSync
		Button vsync_onoff = new Button(50, 20, 250, 125, "On", 1)
		{
			@Override
			public void onClick(){
			}
		};
		innerBox.addChild(vsync_onoff);
		
		
		//Multisamp
		Button mult_onoff = new Button(50, 20, 250, 85, "On", 1)
		{
			@Override
			public void onClick(){
			}
		};
		innerBox.addChild(mult_onoff);
		
		//Back
		Button backButton = new Button(200, 20, 100, 35, "RETURN TO MAIN MENU", 0)
		{
			@Override
			public void onClick(){
				Game.getHandle().getActiveScene().setGUI(new WelcomeGUI());
			}
		};
		innerBox.addChild(backButton);
				
				
		mainBox.addChild(innerBox);
		innerBox.addChild(slider);

		
		rootComponents.add(mainBox);
	}

}
