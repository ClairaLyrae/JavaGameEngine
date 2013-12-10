package com.javagameengine.console;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.javagameengine.Game;
import com.javagameengine.events.CommandEvent;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.util.CyclicArrayBuffer;
import com.javagameengine.util.SimpleText;

/**
 * Console is a statically-accessed class which provides a textual input/output system for the engine. 
 * <p>
 * Strings printed to the console will be held in a size-limited cyclic buffer, which is drawn to the screen if the console is 
 * set to visible. Input is handled by implementing the Listener interface and automatically registering itself to the global 
 * EventManager at runtime. The Console listens to KeyEvents and processes the keystrokes into Commands upon an enter key press.
 * <p>
 * Commands are registered to the Console by the registerCommand method. Registering a command to the console will allow the console
 * to execute the given command if entered by the user.
 */
public class Console implements Listener
{
	public static final Console handle = new Console();
	
	public static final String ARG_DELIMITER = " ";
	public static final String CMD_TOKEN = "/";
	
	private static Map<String, Command> commands;
	private static StringBuilder input = new StringBuilder();
	
	private static CyclicArrayBuffer<String> strbuffer;
	private static CyclicArrayBuffer<String> cmdbuffer;
	
	private static boolean isVisible = false;
	private static boolean dockPosition = true;	// true is up, false is down
	private static int percentSize = 25;
	
	public static void initialize()
	{
		registerCommand(new Command("clear", 0) {
			public String execute(String[] args)
			{
				strbuffer = new CyclicArrayBuffer<String>(128);
				cmdbuffer = new CyclicArrayBuffer<String>(5);
				return null;
			}
		});
		registerCommand(new Command("commands", 0) {
			public String execute(String[] args)
			{
				println("Command List:");
				for(Command c : commands.values())
				{
					println("- " + c.toString());
				}
				return null;
			}
		});
		registerCommand(new Command("exit", 0) {
			public String execute(String[] args)
			{
				Game.getHandle().exit();
				return null;
			}
		});
		registerCommand(new Command("console", 1) {
			public String execute(String[] args)
			{
				if(args[0].equalsIgnoreCase("position"))
					dockPosition = !dockPosition;
				else if(args[0].equalsIgnoreCase("size") && args.length > 1)
				{
					int i = Integer.parseInt(args[1]);
					if(i > 0 && i < 100)
						percentSize = i;
				}
				return null;
			}
		});
		registerCommand(new DisplayCommand());
		registerCommand(new AssetCommand());
		EventManager.global.registerListener(Console.handle);
	}
	
	private Console()
	{
		strbuffer = new CyclicArrayBuffer<String>(128);
		cmdbuffer = new CyclicArrayBuffer<String>(10);
		commands = new HashMap<String, Command>();
	}

	@EventMethod
	public void onMouseMove(MouseMoveEvent e)
	{
		if(!isVisible)
			return;
		e.cancel();
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(!isVisible)
			return;
		e.cancel();
		if(e.getAmount() > 0)
			strbuffer.readForwards();
		else
			strbuffer.readBackwards();
	}
	
	@EventMethod
	public void onKey(KeyPressEvent e)
	{
		if(!e.state())	// Ignore key releases
			return;
		if(e.getKey() == Keyboard.KEY_RSHIFT)	// Toggle console on tilde ~ key
			return;
		if(e.getKey() == Keyboard.KEY_LSHIFT)	// Toggle console on tilde ~ key
			return;
		if(e.getKey() == Keyboard.KEY_GRAVE)	// Toggle console on tilde ~ key
		{
			setVisible(!isVisible());
			strbuffer.setReadToHead();
			return;
		}
		if(!isVisible)	// Ignore if console is closed
			return;
		
		e.cancel();	// Cancel key event
		
		if(e.getKey() == Keyboard.KEY_BACK && input.length() > 0)
			input.deleteCharAt(input.length()-1);
		else if(e.getKey() == Keyboard.KEY_RETURN)	// Try to execute whatever is in the input at the moment
		{
			if(input.length() > 0)
			{
				execute(input.toString());
				cmdbuffer.setReadToHead();
		        input.delete(0, input.length());
			}
		}
		else if(e.getKey() == Keyboard.KEY_UP)	// Scroll through command buffer to more current commands
		{
	        String s = cmdbuffer.readBackwards();
	        if(s == null)
	        	s = "";
		    input.delete(0, input.length());
	        input.append(s);
		}
		else if(e.getKey() == Keyboard.KEY_DOWN)	// Scroll through command buffer to past commands
		{
	        String s = cmdbuffer.readForwards();
	        if(s == null)
	        	s = "";
		    input.delete(0, input.length());
	        input.append(s);
		}
		else
			input.append(e.getChar());
	}
	
	/**
	 * Try to parse the given string into a registered Command, and execute it. 
	 * @param s String to parse.
	 */
	public static void execute(String s)
	{	
		String[] split = s.split(" ");
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
					EventManager.global.callEvent(new CommandEvent(c, args));
					String r = c.execute(args);
					if(r != null)
						println(r);
				}
				else
					println("Not enough arguments.");
			}
		}
		else
		{
			println(s);
		}
		cmdbuffer.write(s);
	}
	
	/**
	 * Register the given command to the Console.
	 * @param c Command to register
	 * @return False if command is already registered.
	 */
	public static boolean registerCommand(Command c)
	{
		if(commands.containsKey(c.getName()))
			return false;
		commands.put(c.getName(), c);
		return true;
	}

	/**
	 * Unregister the given command from the Console.
	 * @param c Command to unregister
	 * @return False if command is not already registered.
	 */
	public static boolean unregisterCommand(Command c)
	{
		return unregisterCommand(c.getName());
	}

	/**
	 * Unregister the given command from the Console.
	 * @param name Name of command to unregister.
	 * @return False if command is not already registered.
	 */
	public static boolean unregisterCommand(String name)
	{
		if(!commands.containsKey(name))
			return false;
		commands.remove(name);
		return true;
	}
	
	/**
	 * @return True if console is visible and accepting commands.
	 */
	public static boolean isVisible()
	{
		return isVisible;
	}
	
	public static void setVisible(boolean b)
	{
		isVisible = b;
		Game.getHandle().pause(b);
	}
	
	/**
	 * Print the given string to the console buffer.
	 * @param s String to print to console
	 */
	public static void println(String s)
	{
		strbuffer.write(s);
	}
	
	/**
	 * Draws the console to the screen. Must be called during ortho projection where 0,0 is the left corner.
	 * Subject to change as render system is implemented.
	 */
	public static void draw()
	{
		if(!isVisible)
			return;
		// Get the display dimensions
		int width = Display.getWidth();
		int height = Display.getHeight();
	
		// Now we need to calculate all the positions of drawn elements
		int yBufferLim;
		int yBuffer;
		int yBufferText;
		int yInput;
		int yInputText;
	    int yBufferSpacing;
	    float percent = (float)percentSize/100f;
	    if(dockPosition)	// If the console is at the top
	    {
			yBufferLim = height;
			yBuffer = (int)((float)height * (1f-percent));
			yBufferText = yBuffer - 8;
			yInput = yBuffer - 20;
			yInputText = yInput + 6;
		    yBufferSpacing = 15;
	    }	
	    else		// If the console is at the bottom
	    {
			yBufferLim = 0;
			yBuffer = (int)((float)height * percent);
			yBufferText = yBuffer;
			yInput = yBuffer + 20;
			yInputText = yBuffer + 6;
		    yBufferSpacing = -15;
	    }
		
	    // Draw the buffer box and border

		
		glBegin(GL_QUADS);
	    	glColor4f(0.5f, 0.5f, 0.5f, 0.2f);
	    	
	    	glVertex3f(0, yBufferLim, 0f);
	    	glVertex3f(width, yBufferLim, 0f);
	    	
	    	glVertex3f(width, yBuffer, 0f);
	    	glVertex3f(0, yBuffer, 0f);
	    glEnd();
	    
	    
		glBegin(GL_LINES);
			glColor4f(0.5f, 0.5f, 0.5f, 1f);
			glVertex3f(width, yBuffer, 0f);
			glVertex3f(0, yBuffer, 0f);
	    glEnd();
	    
	    // Draw the buffer contents
	    glColor4f(0.6f, 0.6f, 0.6f, 1f);
	    int bpos = strbuffer.getReadPos();
	    String s = strbuffer.read();
	    while(s != null)
	    {
	    	yBufferText += yBufferSpacing;
	    	if(yBufferText > height || yBufferText < 0)
	    		break;
    		SimpleText.drawString(s, 5, yBufferText);
    		s = strbuffer.readBackwards();
	    }
	    strbuffer.setReadPos(bpos);

	    // If console is enabled, draw the input box too
		glColor4f(0.5f, 0.5f, 0.5f, 0.2f);
		glBegin(GL_QUADS);
		    glVertex3f(0, yBuffer, 0f);
		    glVertex3f(width, yBuffer, 0f);
		    glVertex3f(width, yInput, 0f);
		    glVertex3f(0, yInput, 0f);
		glEnd();
		glBegin(GL_LINES);
		    glColor4f(0.5f, 0.5f, 0.5f, 1f);
		    glVertex3f(width, yInput, 0f);
		    glVertex3f(0, yInput, 0f);
		glEnd();
		glColor4f(1f, 1f, 1f, 1f);
		SimpleText.drawString("> " + input.toString() + "_" , 5, yInputText);
	}
	
	public static void executeFromFile(File f)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = null;
			while((line = reader.readLine()) != null)
				Console.execute(line);
		} catch (IOException e)
		{
			Console.println("Could not parse commands from " + f.getName());
		}
	}
}
