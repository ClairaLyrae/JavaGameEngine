package com.javagameengine.console;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Allows for switching of display modes from inside the console enviroment. 
 * @author ClairaLyrae
 */
public class DisplayCommand extends Command
{
	public DisplayCommand()
	{
		super("display", 0);
	} 
	
	public static DisplayMode findDisplayMode(int width, int height) 
	{		
		try {
			DisplayMode targetDisplayMode = null;
			DisplayMode standardDisplayMode = Display.getDesktopDisplayMode();
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			int freq = standardDisplayMode.getFrequency();
			for (int i = 0; i < modes.length; i++) 
			{
				DisplayMode current = modes[i];
				if (current.isFullscreenCapable() 
						&& current.getWidth() == width
						&& current.getHeight() == height)
				{
					if ((targetDisplayMode != null) && (current.getFrequency() < freq)) 
						continue;
					if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) 
					{
						targetDisplayMode = current;
						freq = targetDisplayMode.getFrequency();
					}
				}
			}
			return targetDisplayMode;
			
		} catch (LWJGLException e) {}
		return null;
	}
	
	
	@Override
	public String execute(String[] args)
	{
		try
		{
		if(args.length == 0)
		{
			DisplayMode di = Display.getDisplayMode();
			return di + " fullscreen=" + di.isFullscreenCapable();
		}
		if(args[0].equalsIgnoreCase("resize"))
		{
			boolean rs = Display.isResizable();
			Display.setResizable(!rs);
			if(rs)
				return "Display resize disabled.";
			return "Display resize enabled.";
		}
		if(args[0].equalsIgnoreCase("fullscreen"))
		{
			if(!Display.getDisplayMode().isFullscreenCapable())
				return "Current display mode does not support fullscreen.";
			boolean fs = Display.isFullscreen();
			Display.setFullscreen(!fs);
			if(fs)
			{
				Display.setFullscreen(false);
				return "Fullcreen mode disabled.";
			}
			Display.setFullscreen(true);
			return "Fullscreen mode enabled.";
		}
		else if(args[0].equalsIgnoreCase("list"))
		{
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			if(modes.length == 0)
				return "Could not obtain any display modes.";
			else
			{
				int i = 1;
				for(DisplayMode m : modes)
				{
					Console.println(i + ": " + m.toString() + "fullscreen=" + m.isFullscreenCapable());
					i++;
				}
			}
			return null;
		}
		else if(args[0].equalsIgnoreCase("set") && args.length > 1)
		{
			int w = Integer.parseInt(args[1]);
			int h = Integer.parseInt(args[2]);
			DisplayMode d = findDisplayMode(w, h);
			if(d == null)
				return "Mode does not exist. See /display list for a list of modes.";
			Display.setDisplayMode(d);
			return "Display mode set to: " + d;
		}
		} catch (NumberFormatException e) {
			return "Invalid parameters";
		} catch (LWJGLException e)
		{
			return "Command could not be executed.";
		}
		return "Unknown command.";
	}
}
