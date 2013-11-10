package com.javagameengine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.events.EventManager;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.events.MouseEvent;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.graphics.Screen;
import com.javagameengine.scene.Scene;

/**
 * This is the basic implementation of a game in the engine. It provides the basic functionality of the game and
 * handles the main game loop. Games built off the engine must include a class that extends this class. To run the game, the method "run()"
 * is called, which initializes the game engine state and structure. 
 * <p>
 * The AbstractGame class also includes a number of abstract methods which are called at particular points in initialization and the game loop 
 * itself. These methods are used by classes that extend AbstractGame to set up game-specific data during start-up and the loop.
 * @author ClairaLyrae
 */
public abstract class AbstractGame
{
	public boolean closeRequested = false;

	private Scene activeScene = null;
	private Screen screen = new Screen();

	private int framerateCap = 60;
	private int displayWidth = 1024;
	private int displayHeight = 780;
	

	public void loadScene(Scene s)
	{
		this.activeScene = s;
	}
	
	public Scene getActiveScene()
	{
		return activeScene;
	}
	
	private long lastFrameTime; // used to calculate delta
	private float fps;
	private int delta = 0; // Time since last frame

	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta()
	{
		return delta;
	}
	
	private void updateDelta()
	{
		long time = getTime();
		delta = (int) (time - lastFrameTime);
		lastFrameTime = time;
	}
	
	private int deltaBufferSize = 10;
	private int[] deltaBuffer = new int[10];
	private int deltaBufferPointer = 0;
	private int deltaBufferSum = 0;

	private void updateFPS()
	{
		deltaBufferSum-=deltaBuffer[deltaBufferPointer];
		deltaBufferSum+=delta;
		deltaBuffer[deltaBufferPointer] = delta;
		if(++deltaBufferPointer >= deltaBufferSize)
			deltaBufferPointer = 0;
		fps = 1000f/((float)deltaBufferSum/(float)deltaBufferSize);
	}
	
	public float getFPS()
	{
		return fps;
	}
	
	private long keyPressTime = 0;
	
	/**
	 * Links the LWJGL input device event system with the object-based event system of the engine. Analyzes the LWJGL
	 * input event buffer and calls events on the game's main EventManager. 
	 */
	private void input()
	{
		// While keyboard has events in buffer
		if(activeScene == null)
			return;
		while(Keyboard.next())
		{
			KeyEvent e;
			int key = Keyboard.getEventKey();
			boolean isRepeat = Keyboard.isRepeatEvent();
			boolean isPress = Keyboard.isKeyDown(key);
			char c = Keyboard.getEventCharacter();
			if(Keyboard.areRepeatEventsEnabled() && isRepeat)
			{
			 	keyPressTime += Keyboard.getEventNanoseconds();
				e = new KeyEvent(key, c, isPress, Keyboard.getEventNanoseconds());
			}
			else if(keyPressTime > 0)
			{
				e = new KeyEvent(key, c, isPress, keyPressTime);
				keyPressTime = 0;
			}
			else
				e = new KeyEvent(key, c, isPress);
			activeScene.getEventManager().callEvent(e);
		}
		// While mouse has events in buffer
		while(Mouse.next())
		{
			MouseEvent e;
			int x = Mouse.getEventX();
			int y = Mouse.getEventY();
			int dx = Mouse.getEventDX();
			int dy = Mouse.getEventDY();
			int dw = Mouse.getEventDWheel();
			boolean buttonState = Mouse.getEventButtonState();
			int button = Mouse.getEventButton();
			if(dx != 0 || dy != 0)
			{
				e = new MouseMoveEvent(x, y, dx, dy);
				activeScene.getEventManager().callEvent(e);				
			}
			if(Mouse.hasWheel() && dw != 0)
			{
				e = new MouseScrollEvent(dw/120);	// LWJGL returns one scroll 'click' as +/- 120, for some reason. This normalizes it...
				activeScene.getEventManager().callEvent(e);
			}
			if(button >= 0)
			{
				e = new MouseClickEvent(button, buttonState);
				activeScene.getEventManager().callEvent(e);
			}
		}
		if(Display.isCloseRequested())
		{
			closeRequested = true;
		}
	}

	private boolean isRunning = false;
	
	/**
	 * Runs the game. Initializes the game engine and starts the main loop. 
	 * @param	args	Arguments applied to game on startup
	 */
	public void run(String[] args)
	{
		// If we are running, don't start another one
		if(isRunning)	
			return;
		isRunning = true;
		
		// Initialize Display
		try
		{
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			//Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException e)
		{
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}
		
		// Initialize graphics
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix

		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
		
		onCreate();
		
		while (!closeRequested)	// Main game loop
		{
			// Update timing information
			updateDelta();
			updateFPS();
			Display.setTitle("FPS: " + fps + " Delta: " + delta);
			
			// Call loop update methods that trickle down the hierarchy
			input();
			if (activeScene == null)
				break;
			activeScene.logic(delta);
			onLogic();
			activeScene.physics(delta);
			onPhysics();
			activeScene.renderTo(screen);
			onGraphics();
			
			Display.update();	// Update the LWJGL display
			Display.sync(framerateCap);		// If we are running at an FPS above framerateCap, idle until we are synced
		}
		
		// Cleanup
		Display.destroy();
		onDestroy();
		isRunning = false;
	}

	/**
	 * Called after logic is calculated.
	 */
	protected abstract void onLogic();

	/**
	 * Called after physics are calculated.
	 */
	protected abstract void onPhysics();

	/**
	 * Called after graphics are calculated.
	 */
	protected abstract void onGraphics();
	
	/**
	 * Called when the game quits.
	 */
	protected abstract void onDestroy();
	
	/**
	 * Called when the game starts.
	 */
	protected abstract void onCreate();
}
