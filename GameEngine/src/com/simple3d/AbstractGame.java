package com.simple3d;

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

import com.simple3d.events.EventManager;
import com.simple3d.events.KeyEvent;
import com.simple3d.events.MouseClickEvent;
import com.simple3d.events.MouseEvent;
import com.simple3d.events.MouseMoveEvent;
import com.simple3d.events.MouseScrollEvent;
import com.simple3d.scene.Scene;

public abstract class AbstractGame
{	
	private String windowTitle = "";
	public boolean closeRequested = false;
	
			
	private Scene activeScene = null;
	private EventManager eventManager = new EventManager();

	private int framerateCap = 60;
	private int displayWidth = 1024;
	private int displayHeight = 780;
	
	private String[] args;
	
	public AbstractGame(String[] args)
	{
		this.args = args;
		// Process .exe arguments
	}
	
	public EventManager getEventManager()
	{
		return eventManager;
	}

	public void loadScene(Scene s)
	{
		this.activeScene = s;
	}
	
	public Scene getActiveScene()
	{
		return activeScene;
	}
	
	// Init Methods

	protected void initDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			//Display.setVSyncEnabled(true);
			Display.setTitle(windowTitle);
			Display.create();
		} catch (LWJGLException e)
		{
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}
	}

	private void initGraphics()
	{
		/* OpenGL */
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
	}
	private void initPhysics()
	{

	}

	// Loop Methods
	
	long lastFrameTime; // used to calculate delta
	float fps;

	public long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta()
	{
		long time = getTime();
		int delta = (int) (time - lastFrameTime);
		lastFrameTime = time;
		return delta;
	}
	private long keyPressTime = 0;
	private void input()
	{
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
			eventManager.callEvent(e);
		}
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
				eventManager.callEvent(e);				
			}
			if(Mouse.hasWheel() && dw != 0)
			{
				e = new MouseScrollEvent(dw);
				eventManager.callEvent(e);
			}
			if(button >= 0)
			{
				e = new MouseClickEvent(button, buttonState);
				eventManager.callEvent(e);
			}
		}
		if(Display.isCloseRequested())
		{
			closeRequested = true;
		}
	}
	
	protected void logic(int delta)
	{
		if (activeScene == null)
			return;
		activeScene.logic(delta);
	}

	protected void physics(int delta)
	{
		if (activeScene == null)
			return;
		activeScene.physics(delta);
	}

	protected void graphics()
	{		
		glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		glLoadIdentity(); // Reset The View
		
		// Okay, now we have a clean screen and depth buffer, and the modelview is an identity transform.
		// Now we should go through the scene node tree. See Node.java for cont. notes
		
		if (activeScene == null)
			return;
		activeScene.graphics();
	}

	protected void run(String[] args)
	{
		int deltaBufferSize = 10;
		int[] deltaBuffer = new int[10];
		int deltaBufferPointer = 0;
		int deltaBufferSum = 0;
		
		initDisplay();
		
		onCreate();
		
		initPhysics();
		initGraphics();
		getDelta(); // Initialize delta timer
		while (!closeRequested)
		{
			int delta = getDelta();
			
			// FPS weighted sum
			deltaBufferSum-=deltaBuffer[deltaBufferPointer];
			deltaBufferSum+=delta;
			deltaBuffer[deltaBufferPointer] = delta;
			if(++deltaBufferPointer >= deltaBufferSize)
				deltaBufferPointer = 0;
			fps = 1000f/((float)deltaBufferSum/(float)deltaBufferSize);
			Display.setTitle("FPS: " + fps + " Delta: " + delta);
			
			input();
			logic(delta);
			physics(delta);
			graphics();
			Display.update();
			Display.sync(framerateCap);	
		}
		Display.destroy();
		onDestroy();
	}

	public abstract void onDestroy();
	protected abstract void onCreate();
}
