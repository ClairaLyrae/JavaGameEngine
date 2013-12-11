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
				Color4f.white.setTrans(), Color4f.black.setTrans(.5f), null);
		
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
		Button sound_onoff = new Button(50, 20, 300, 405, "On", 1)
		{
			
			@Override
			public void unClick(){
				if(this.clicked)
				{
					SoundManager.mute(true);
					this.text = "OFF";
					if(text!=null)
						updateText(0);
				}
				else
				{
					SoundManager.mute(false);
					this.text = "ON";
					if(text!=null)
						updateText(0);
				}
			}
		};
		innerBox.addChild(sound_onoff);
		
		
		//Volume Slider
		Slider volume_slider = new Slider(100, 20, 275, 365, Color4f.white.setTrans(), Color4f.red.setTrans(), SoundManager.getGlobalVolume())
		{
			@Override
			public void onSliderAdjust(float f)
			{
				SoundManager.setGlobalVolume(f);
			}
		};
		innerBox.addChild(volume_slider);
		
		//Music on/off
		Button music_onoff = new Button(50, 20, 300, 325, "On", 1)
		{
			@Override
			public void unClick(){
				if(this.clicked)
				{
					this.text = "OFF";
					if(text!=null)
						updateText(0);
					SoundManager.mute(true);
				}
				else
				{
					this.text = "ON";
					if(text!=null)
						updateText(0);
					SoundManager.mute(false);
				}
			}
		};
		innerBox.addChild(music_onoff);
		
		
		//Crosshair
		Button crosshair_onoff = new Button(50, 20, 300, 245, null, 1)
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
		
		
		//Resolution slider100, 20, 275, 365
		DiscreteSlider resolution_slider = new DiscreteSlider(100, 20, 275, 205, Color4f.white.setTrans(), Color4f.red.setTrans(), 5, 4)
		{
			@Override
			public void onSliderAdjustDiscrete(int i)
			{
				int width = 0;
				int height = 0;
				
				switch(i)
				{
				case 0:
					width = 1600;
					height = 900;
					break;
				case 1:
					width = 1280;
					height = 768;
					break;
				case 2:
					width = 1024;
					height = 600;	
					break;
				case 3:
					width = 800;
					height = 600;	
					break;
				case 4:
					width = 720;
					height = 480;	
					break;
				}
				Game.getHandle().setDisplaySize(width, height);
			}
			
		};
		innerBox.addChild(resolution_slider);
		
		
		//FullScreen
		Button fullscreen_onoff = new Button(50, 20, 300, 165, "OFF", 1)
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
		Button vsync_onoff = new Button(50, 20, 300, 125, null, 1)
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
		Button mult_moment = new Button(50, 20, 300, 85, null, 0)
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
				
				
		this.addComponent(innerBox);
	}

}
