package com.javagameengine;

import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.TestComponent;
import com.javagameengine.scene.component.TestSceneDebugger;

public class TestGame extends AbstractGame
{
	protected void onCreate()
	{
	}

	protected void onDestroy()
	{
	}
	
	protected void onLogic()
	{
	}

	protected void onPhysics()
	{
	}

	protected void onGraphics()
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
		
		TestComponent b = new TestComponent(new Bounds(1.0f, 1.0f, 1.0f), 1);
		
		n4.addComponent(b);
		
		game.loadScene(s);
		s.print();
		game.run(args);
	}
}
