package com.javagameengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.NativeObject;
import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.KeyHeldEvent;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.events.MouseEvent;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.events.SceneChangeEvent;
import com.javagameengine.gui.GUI;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Scene;
import com.javagameengine.sound.SoundManager;

/**
 * This is the basic implementation of a game in the engine. It provides the basic functionality of the game and
 * handles the main game loop. Games built off the engine must include a class that extends this class. To run the game, the method "run()"
 * is called, which initializes the game engine state and structure. 
 * <p>
 * The AbstractGame class also includes a number of abstract methods which are called at particular points in initialization and the game loop 
 * itself. These methods are used by classes that extend AbstractGame to set up game-specific data during start-up and the loop.
 */
public abstract class Game
{
	//THIS IS A TEST
	private static Game handle;
	
	/**
	 * Obtains the Game instance that is currently running.
	 * @return Null if game is not running, otherwise current game
	 */
	public static Game getHandle()
	{
		return handle;
	}
	
	private Scene activeScene = null;
	
	// Game settings
	private int framerateCap = 60;
	
	// Loop timing
	private long lastFrameTime; // used to calculate delta
	private float fps;
	private int delta = 0; // Time since last frame

	// Game status
	public boolean closeRequested = false;
	private boolean isPaused = false;
	
	private int[] validKeyHolds = {
			Keyboard.KEY_UP,
			Keyboard.KEY_DOWN,
			Keyboard.KEY_LEFT,
			Keyboard.KEY_RIGHT,
			Keyboard.KEY_S,
			Keyboard.KEY_D,
			Keyboard.KEY_F,
			Keyboard.KEY_E,
			Keyboard.KEY_W,
			Keyboard.KEY_R,
			Keyboard.KEY_T,
			Keyboard.KEY_G,
			Keyboard.KEY_Q,
			Keyboard.KEY_A
			};
	
	/**
	 * @return The active scene. (null if no scenes are active)
	 */
	public Scene getActiveScene()
	{
		return activeScene;
	}
	
	/**
	 * @return Time between last two frames (milliseconds)
	 */
	public int getDelta()
	{
		return delta;
	}
	/**
	 * @return The average FPS of the game
	 */
	public float getFPS()
	{
		return fps;
	}
	
	/**
	 * @return System time (milliseconds)
	 */
	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public void exit()
	{
		closeRequested = true;
	}
	
	/**
 	 * Links the LWJGL input device event system with the object-based event system of the engine. Analyzes the LWJGL
	 * input event buffer and calls events on the game's main EventManager. 
	 */
	private void input()
	{
		// While keyboard has events in buffer
		for(int key : validKeyHolds)
		{
			if(Keyboard.isKeyDown(key))
				EventManager.global.callEvent(new KeyHeldEvent(key, ' ', getDelta()));
		}
		while(Keyboard.next())
		{
			KeyPressEvent e;
			int key = Keyboard.getEventKey();
			boolean isPress = Keyboard.isKeyDown(key);
			char c = Keyboard.getEventCharacter();
			e = new KeyPressEvent(key, c, isPress);
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
				if(dw > 0)	// We have to normalize it, because getEventDWheel returns a totally different number based on mouse
					dw = 1;
				else
					dw = -1;
				e = new MouseScrollEvent(dw);
				EventManager.global.callEvent(e);
			}
			if(button >= 0)
			{
				e = new MouseClickEvent(x, y, button, buttonState);
				EventManager.global.callEvent(e);
			}
		}
		if(Display.isCloseRequested())
		{
			closeRequested = true;
		}
	}
	/**
	 * @param Scene to check
	 * @return True if given scene is the active scene
	 */
	public boolean isActiveScene(Scene s)
	{
		if(s == null)
			return false;
		return s == activeScene;
	}
	/**
	 * @param Name of scene to check
	 * @return True if given scene is the active scene
	 */
	public boolean isActiveScene(String s)
	{
		if(s == null || activeScene == null)
			return false;
		return activeScene.getName().equals(s);
	}

	public boolean isPaused()
	{
		return isPaused;
	}
	
	/**
	 * Called when the game starts.
	 */
	protected abstract void onCreate();
	
	/**
	 * Called when the game quits.
	 */
	protected abstract void onDestroy();
	
	/**
	 * Called after graphics are rendered.
	 */
	protected abstract void onRender();
	
	/**
	 * Called after the game logic is updated.
	 */
	protected abstract void onUpdate(float delta);
	
	/**
	 * Pauses the game. Setting pause to true stops loop from calling update() on scene.
	 * @param state True to pause the game
	 */
	public void pause(boolean state)
	{
		isPaused = state;
		Scene s = getActiveScene();
		if(s != null)
		{
			s.getEventManager().ignoreEvents(isPaused);
		}
	}

	/**
	 * Runs the game. Initializes the game engine and starts the main loop. 
	 * @param	args	Arguments applied to game on startup
	 * @throws GameInitializationException 
	 */
	public void run(String[] args) throws LWJGLException
	{
		// If we are running, don't start another one
		if(handle != null)	
			throw new LWJGLException("Another instance of a game is already running.");
		handle = this;
		Renderer.initialize();
		SoundManager.initialize();
		Console.initialize();
		AssetManager.loadAll();
		onCreate();
		updateDelta();
		updateFPS();
		while (!closeRequested)	// Main game loop
		{
			// Update timing information
			updateDelta();
			updateFPS();
			Display.setTitle("JavaGameEngine Test Game [FPS: " + fps + " Delta: " + delta + "]");
			input();
			// Call loop update methods that trickle down the hierarchy
			if (activeScene != null)
			{
				float deltaf = (float)delta/1000f;	// Find seconds since last frame
				activeScene.update(deltaf);
				onUpdate(deltaf);
				Renderer.reset();
				activeScene.queueRender();	
				Renderer.render();
				onRender();
			}
			Display.sync(framerateCap);	
		}
		// Cleanup
		onDestroy();
		NativeObject.destroyAll();
		Display.destroy();
		AL.destroy();
		EventManager.global.unregisterListener(Console.handle);
		handle = null;
	}

	/**
	 * @param Name of scene to set as active
	 * @return True if scene was set as active
	 */
	public boolean setActiveScene(String s)
	{
		pause(false);
		Scene newScene = AssetManager.getScene(s);
		if(newScene == null)
			return false;
		if(newScene == activeScene)
			return false;
		EventManager.global.callEvent(new SceneChangeEvent(activeScene, false));
		EventManager.global.callEvent(new SceneChangeEvent(newScene, true));
		Scene oldScene = activeScene;
		this.activeScene = newScene;
		if(oldScene != null)
			oldScene.getRoot().relink();
		newScene.getRoot().relink();
		return true;
	}
	
	/**
	 * Updates the time between last two frames (called only during game loop)
	 */
	private void updateDelta()
	{
		long time = getTime();
		delta = (int) (time - lastFrameTime);
		lastFrameTime = time;
	}
	
	/**
	 * Updates the FPS calculation between the last two frames (called only during game loop)
	 */
	private void updateFPS()
	{
		fps = 1000f/delta;
	}
}
