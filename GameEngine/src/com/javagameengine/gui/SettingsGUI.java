package com.javagameengine.gui;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.console.DisplayCommand;
import com.javagameengine.gui.GUIcomponent;
import com.javagameengine.gui.GLquadGUIcomponent;
import com.javagameengine.gui.GUI;
import com.javagameengine.math.Color4f;
import com.javagameengine.sound.SoundManager;


public class SettingsGUI extends GUI {

	
	@Override
	public void create() {
		
		//Main boxes
		GLquadGUIcomponent mainBox = new GLquadGUIcomponent(Display.getWidth(), Display.getHeight(), 0, 0,
				Color4f.white.setTrans(0f), Color4f.white.setTrans(0f), null);
		GLquadGUIcomponent innerBox = new GLquadGUIcomponent(400, 500, centerX-200, centerY-250,
				Color4f.white.setTrans(), Color4f.black.setTrans(.9f), null);
				
		//Slider boxes
		GLquadGUIcomponent slider1 = new GLquadGUIcomponent(100, 2, 225, 375,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent slider2 = new GLquadGUIcomponent(100, 2, 225, 205,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		
		//Tick marks
		GLquadGUIcomponent tick1 = new GLquadGUIcomponent(2, 10, 242, 205,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent tick2 = new GLquadGUIcomponent(2, 10, 258, 205,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent tick3 = new GLquadGUIcomponent(2, 10, 274, 205,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent tick4 = new GLquadGUIcomponent(2, 10, 290, 205,
				Color4f.white.setTrans(), Color4f.white.setTrans(), null);
		GLquadGUIcomponent tick5 = new GLquadGUIcomponent(2, 10, 306, 205,
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
			public void unClick(){
				if(this.clicked)
				{

					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				else
				{

					this.text = "ON";
					if(text!=null)
						updateText(0);
				}
			}
		};
		innerBox.addChild(sound_onoff);
		
		
		//Volume Slider
		Button volume_slider = new Button(10, 25, 270, 365, null, 2)
		{
			@Override
			public void unClick()
			{
				int distance = Mouse.getX() - 445;
				double gainValue = 0.0;
				if(distance > 315)
				{
					// Set global gain to 1 (sound max)
					this.xPos = 315;
					gainValue = 1;
				}
				else if(distance < 225)
				{
					// Set global gain to 0 (sound off)
					this.xPos = 225;
					gainValue = 0;
				}
				else
				{
					this.xPos = distance;
					gainValue = ((double)distance - 225)/(315-225);
				}
				SoundManager.setGlobalVolume((float)gainValue);
			}
		};
		volume_slider.xPos = (int)SoundManager.getGlobalVolume()*(315-225) + 225;
		innerBox.addChild(volume_slider);
		
		//Music on/off
		Button music_onoff = new Button(50, 20, 250, 325, "On", 1)
		{
			@Override
			public void unClick(){
				if(this.clicked)
				{

					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				else
				{

					this.text = "ON";
					if(text!=null)
						updateText(0);
				}
			}
		};
		innerBox.addChild(music_onoff);
		
		
		//Crosshair
		Button crosshair_onoff = new Button(50, 20, 250, 245, null, 1)
		{
			@Override
			public void getState() {
				if(GUI.crosshairs_visible)
					text = "ON";
				else
				{
					this.backgroundColor = this.backgroundColor.inverse().setTrans();
					this.textColor = this.textColor.inverse();
					text = "OFF";
					clicked = true;
				}
					
			}
			@Override
			public void unClick(){
				if(this.clicked)
				{
					System.out.println("Crosshairs off");
					GUI.crosshairs_visible = false;
					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				else
				{
					System.out.println("Crosshairs on");
					GUI.crosshairs_visible = true;
					this.text = "ON";
					if(text!=null)
						updateText(0);
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
		
		
		//Resolution slider
		Button resolution_slider = new Button(10, 25, 286, 195, null, 2)
		{
			@Override
			public void unClick(){
				int distance = Mouse.getX() - 445;
				int resolution = 0;
				int width = 0;
				int height = 0;
				
				if(distance > 310 || (distance > 294 && distance <=310))
				{
					width = 1600;
					height = 900;
					this.xPos = 302;
				}
				else if(distance > 278 && distance <=294)
				{
					// This is the default resolution
					width = 1280;
					height = 768;	
					this.xPos = 286;
				}
				else if(distance > 262 && distance <=278)
				{
					width = 1024;
					height = 600;	
					this.xPos = 270;
				}
				else if(distance > 246 && distance <=262)
				{
					width = 800;
					height = 600;	
					this.xPos = 254;
				}
				else if(distance > 230 && distance <=246)
				{
					width = 720;
					height = 480;	
					this.xPos = 238;
				}
				DisplayMode current = DisplayCommand.findDisplayMode(width, height);
				if(current == null)
					return;
				else
					{
						try {
							Display.setDisplayMode(current);
						} catch (LWJGLException e) {
							e.printStackTrace();
						}
					}
				
				System.out.println(resolution);
			}
		};
		innerBox.addChild(resolution_slider);
		
		
		//FullScreen
		Button fullscreen_onoff = new Button(50, 20, 250, 165, "OFF", 1)
		{
			@Override
			public void unClick(){
				if(this.clicked)
				{
					try {
						Display.setFullscreen(false);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				else
				{
					try {
						Display.setFullscreen(true);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					this.text = "ON";
					if(text!=null)
						updateText(0);
				}
			}
		};
		innerBox.addChild(fullscreen_onoff);
		
		
		//VSync
		Button vsync_onoff = new Button(50, 20, 250, 125, null, 1)
		{
			private boolean VSyncDisabled = false;

			@Override
			public void getState() {
				if(!VSyncDisabled)
				{
					text = "ON";
					clicked = false;
				}
				else
				{
					this.backgroundColor = this.backgroundColor.inverse().setTrans();
					this.textColor = this.textColor.inverse();
					text = "OFF";
					clicked = true;
				}
					
			}
			
			@Override
			public void unClick(){
				// VSync off
				if(this.clicked)
				{
	//				Display.setVSyncEnabled(!clicked);
					VSyncDisabled = clicked;
					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				// VSync on 
				else
				{
	//				Display.setVSyncEnabled(clicked);
					VSyncDisabled = !clicked;
					this.text = "ON";
					if(text!=null)
						updateText(0);
				}
			}
		};
		innerBox.addChild(vsync_onoff);
		
		
		//Multisamp
		Button mult_moment = new Button(50, 20, 250, 85, null, 0)
		{
			private int multisampleNum = 1;

			
			@Override
			public void getState() {
				if(multisampleNum == 0)
					multisampleNum = 1;
				this.text = Integer.toString(multisampleNum);
			}
			
			@Override
			public void unClick(){
				if(multisampleNum == 8)
					multisampleNum = 1;
				else
					multisampleNum *= 2;
				
				this.text = Integer.toString(multisampleNum);
				if(text!=null)
					updateText(0);
			}
			
		};
		innerBox.addChild(mult_moment);
		
		//Back
		Button backButton = new Button(200, 20, 100, 35, "RETURN TO MAIN MENU", 0)
		{
			@Override
			public void unClick(){
				Game.getHandle().getActiveScene().setGUI(AssetManager.getGUI("welcomegui"));
			}
		};
		innerBox.addChild(backButton);
				
				
		mainBox.addChild(innerBox);
		innerBox.addChild(slider1);
		innerBox.addChild(slider2);
		innerBox.addChild(tick1);
		innerBox.addChild(tick2);
		innerBox.addChild(tick3);
		innerBox.addChild(tick4);
		innerBox.addChild(tick5);
	
		rootComponents.add(mainBox);
	}

}
