package com.javagameengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.console.MeshCommand;
import com.javagameengine.console.Console;
import com.javagameengine.console.DisplayCommand;
import com.javagameengine.console.SceneCommand;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Quaternion;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.Camera;
import com.javagameengine.scene.component.CoordinateGrid;
import com.javagameengine.scene.component.TestComponent;
import com.javagameengine.scene.component.TestSceneDebugger;
import com.javagameengine.gui.*;

public class TestGame extends Game
{
	// We want to manually put stuff in our game, so here we make a scene and load it in during startup
	protected void onCreate()
	{
		AssetManager.loadAll();
		
		// Create a new scene
		Scene s = new Scene("3d");
		Scene s2 = new Scene("menu");
		Node root = s.getRoot();
		
		
		// Add some debug components to the root
		root.addComponent(new TestSceneDebugger());
		root.addComponent(new CoordinateGrid(2f, 8f));
		root.addComponent(new Camera());
		
		// Add gui components to root
		root.addComponent(new GLMenuWindow(Display.getHeight(),Display.getWidth()));

	
		
		// Register the box making command
		Console.registerCommand(new MeshCommand());
		Console.registerCommand(new SceneCommand());
		
		// Load the scene into the game
		addScene(s);
		addScene(s2);
		setActiveScene(s.getName());
		
		// Auto load commands to console from file! Also we are manually firing some events
		Console.executeFromFile(new File("commands.txt"));
		EventManager.global.callEvent(new KeyEvent(Keyboard.KEY_1, '1', true));
		EventManager.global.callEvent(new KeyEvent(Keyboard.KEY_M, 'm', true));
		EventManager.global.callEvent(new KeyEvent(Keyboard.KEY_F, 'f', true));
	}

	// Dont care about destroying it
	protected void onDestroy()
	{
		
	}

	protected void onUpdate()
	{
		
	}

	protected void onRender()
	{
		
	}

	public static void main(String[] args)
	{
		TestGame game = new TestGame();

		try
		{
			game.run(args);
		} catch (LWJGLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
