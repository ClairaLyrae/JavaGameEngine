package com.javagameengine;

import com.javagameengine.console.BoxCmd;
import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.exceptions.GameInitializationException;
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

public class TestGame extends Game
{
	protected void onCreate()
	{
	}

	protected void onDestroy()
	{
	}

	public static void main(String[] args)
	{
		TestGame game = new TestGame();
		Scene s = new Scene("TestScene");
		
		Node root = s.getRoot();
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");

		root.addChild(n1);
		n1.addChild(n2);
		n1.addChild(n3);
		root.addChild(n4);
		
		TestSceneDebugger deb = new TestSceneDebugger();
		root.addComponent(deb);
		root.addComponent(new CoordinateGrid(2f, 8f));
		
		TestComponent b = new TestComponent(new Bounds(1.0f, 1.0f, 1.0f), 1);
		n4.addComponent(b);		
		
		game.loadScene(s);

		Console.registerCommand(new BoxCmd());
		EventManager.global.registerListener(Console.handle);
		
		s.print();
		try
		{
			game.run(args);
		} catch (GameInitializationException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void onUpdate()
	{
	}

	protected void onRender()
	{
	}
}
