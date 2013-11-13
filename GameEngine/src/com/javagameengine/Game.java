package com.javagameengine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.events.MouseEvent;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.graphics.Renderer;
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
public abstract class Game
{
	private static Game handle;
	public boolean closeRequested = false;
	private Scene activeScene = null;
	private int framerateCap = 60;
	
	public static Game getHandle()
	{
		return handle;
	}
	
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
			EventManager.global.callEvent(e);
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
				EventManager.global.callEvent(e);				
			}
			if(Mouse.hasWheel() && dw != 0)
			{
				e = new MouseScrollEvent(dw/120);	// LWJGL returns one scroll 'click' as +/- 120, for some reason. This normalizes it...
				EventManager.global.callEvent(e);
			}
			if(button >= 0)
			{
				e = new MouseClickEvent(button, buttonState);
				EventManager.global.callEvent(e);
			}
		}
		if(Display.isCloseRequested())
		{
			closeRequested = true;
		}
	}
	
	/**
	 * Runs the game. Initializes the game engine and starts the main loop. 
	 * @param	args	Arguments applied to game on startup
	 * @throws GameInitializationException 
	 */
	public void run(String[] args) throws GameInitializationException
	{
		// If we are running, don't start another one
		if(handle != null)	
			throw new GameInitializationException("Another instance of a game is already running.");
		handle = this;
		
		// Initialize Display
		try
		{
			Display.setDisplayMode(new DisplayMode(1600, 900));
			Display.create(new PixelFormat().withDepthBits(24).withSamples(4).withSRGB(true));
		} catch (LWJGLException e)
		{
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}

		EventManager.global.registerListener(Console.handle);
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
			activeScene.update(delta);
			onUpdate();
			Renderer.clearQueue();
			// Load the renderer queue with the scene
			if(activeScene.getRoot() != null)
				activeScene.getRoot().queueRenderOperations();	
			Renderer.render();
			onRender();
			
			Display.sync(framerateCap);		// If we are running at an FPS above framerateCap, idle until we are synced
		}
		
		// Cleanup
		Display.destroy();
		EventManager.global.unregisterListener(Console.handle);
		onDestroy();
		handle = null;
	}

	/**
	 * Called after the game logic is updated.
	 */
	protected abstract void onUpdate();

	/**
	 * Called after graphics are rendered.
	 */
	protected abstract void onRender();
	
	/**
	 * Called when the game quits.
	 */
	protected abstract void onDestroy();
	
	/**
	 * Called when the game starts.
	 */
	protected abstract void onCreate();
}
