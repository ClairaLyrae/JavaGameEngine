package com.javagameengine.console;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.javagameengine.events.CommandEvent;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;

public class Console implements Listener
{
	public static final String ARG_DELIMITER = " ";
	public static final String CMD_TOKEN = "/";
	
	public static final Console handle = new Console();
	
	private static Map<String, Command> commands = new HashMap<String, Command>();
	private static StringBuilder input = new StringBuilder();
	private static String[] buffer;
	private static int writeIndex;
	private static boolean isVisible = false;
	
	@EventMethod
	public void onKeyEvent(KeyEvent e)
	{
		if(!e.state())	// Ignore key releases
			return;
		if(e.getKey() == Keyboard.KEY_GRAVE)	// Toggle console on tilde ~ key
			isVisible = !isVisible;
		if(!isVisible)	// Ignore if console is closed
			return;
		e.cancel();	// Cancel key event
		if(e.getKey() == Keyboard.KEY_BACK && input.length() > 0)
		{
			println("Deleting");
			input.deleteCharAt(input.length() - 1);
		}
		if(e.getKey() == Keyboard.KEY_RETURN)
		{
			if(input.length() > 0)
			{
				String[] split = input.toString().split(" ");
				if(split[0].startsWith(CMD_TOKEN))
				{
					String cmdName = split[0].substring(CMD_TOKEN.length());
					Command c = commands.get(cmdName);
					if(c == null)
					{
						println("Unknown command.");
					}
					else
					{
						String[] args = Arrays.copyOfRange(split, 1, split.length);
						if(c.getMinArgs() <= args.length)
						{
							c.execute(args);
							EventManager.global.callEvent(new CommandEvent(c, args));
						}
						else
							println("Not enough arguments.");
					}
				}
				println(input.toString());
		        input.delete(0, input.length());
			}
		}
		else
		{
			input.append(e.getChar());
		}
	}
	
	public static boolean registerCommand(Command c)
	{
		if(commands.containsKey(c.getName()))
			return false;
		commands.put(c.getName(), c);
		return true;
	}
	
	public static boolean unregisterCommand(Command c)
	{
		return unregisterCommand(c.getName());
	}
	
	public static boolean unregisterCommand(String name)
	{
		if(!commands.containsKey(name))
			return false;
		commands.remove(name);
		return true;
	}
	
	public static boolean isVisible()
	{
		return isVisible();
	}
	
	
	public static void setVisible(boolean b)
	{
		isVisible = b;
		if(isVisible)
		{
			
		}
		else
		{
			
		}
			
	}
	
	private Console()
	{
		buffer = new String[32];
		writeIndex = 0;
	}
	
	public static void println(String s)
	{
		if(++writeIndex >= buffer.length)
			writeIndex = 0;
		buffer[writeIndex] = s;
	}
	
	public static void resize(int i)
	{
		buffer = Arrays.copyOf(buffer, i);
		if(writeIndex > buffer.length)
			writeIndex = buffer.length - 1;
	}
	
	public static void draw()
	{
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		
		glEnable(GL_BLEND); //Enable blending.
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //Set blending function.
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );

		glBegin(GL_QUADS);
	    glColor4f(0.5f, 0.5f, 0.5f, 0.2f);
	    glVertex3f(0, 0, 0f);
	    glVertex3f(width, 0, 0f);
	    glVertex3f(width, height/4, 0f);
	    glVertex3f(0, height/4, 0f);
	    glEnd();
	    
		glBegin(GL_LINES);
	    glColor4f(0.5f, 0.5f, 0.5f, 1f);
	    glVertex3f(width, height/4, 0f);
	    glVertex3f(0, height/4, 0f);
	    glEnd();
	    
	    int spacing = 15;
	    int ypos = height/4;
	    glColor4f(0.6f, 0.6f, 0.6f, 1f);
		
	    int bpos = writeIndex;
	    for(int i = 0; i < buffer.length; i++)
	    {
	    	if(ypos < 0)
	    		break;
	    	if(bpos < 0)
	    		bpos = buffer.length - 1;
	    	if(buffer[bpos] != null)
	    	{
		    	ypos -= spacing;
	    		SimpleText.drawString(buffer[bpos], 5, ypos);
	    	}
	    	bpos--;
	    }

	    // If console is enabled, draw the input box too
		if(isVisible)
		{
		    glColor4f(0.5f, 0.5f, 0.5f, 0.2f);
			glBegin(GL_QUADS);
		    glVertex3f(0, height/4, 0f);
		    glVertex3f(width, height/4, 0f);
		    glVertex3f(width, height/4 + 20, 0f);
		    glVertex3f(0, height/4 + 20, 0f);
		    glEnd();

			glBegin(GL_LINES);
		    glColor4f(0.5f, 0.5f, 0.5f, 1f);
		    glVertex3f(width, height/4 + 20, 0f);
		    glVertex3f(0, height/4 + 20, 0f);
		    glEnd();

		    glColor4f(1f, 1f, 1f, 1f);
		    SimpleText.drawString("> " + input.toString() , 5, height/4 + 6);
		}
	}
}
