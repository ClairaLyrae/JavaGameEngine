package com.javagameengine;

import com.javagameengine.console.MeshCommand;
import com.javagameengine.console.Console;
import com.javagameengine.console.DisplayCommand;
import com.javagameengine.events.EventManager;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Quaternion;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.CoordinateGrid;
import com.javagameengine.scene.component.TestComponent;
import com.javagameengine.scene.component.TestSceneDebugger;
import com.javagameengine.gui.*;

public class TestGame extends Game
{
	// We want to manually put stuff in our game, so here we make a scene and load it in during startup
	protected void onCreate()
	{
		// Create a new scene
		Scene s = new Scene("TestScene");
		Node root = s.getRoot();
		
		// Add some debug components to the root
		root.addComponent(new TestSceneDebugger());
		root.addComponent(new CoordinateGrid(2f, 8f));
		
		// Register the box making command
		Console.registerCommand(new MeshCommand());
		Console.registerCommand(new DisplayCommand());
		
		// Load the scene into the game
		loadScene(s);
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
		} catch (GameInitializationException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
