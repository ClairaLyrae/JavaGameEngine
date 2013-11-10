package com.javagameengine;

import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.TestComponent;

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
		Scene s = new Scene();
		
		Node root = s.getRoot();
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");

		root.addChild(n1);
		n1.addChild(n2);
		n1.addChild(n3);
		root.addChild(n4);
		
		TestComponent b = new TestComponent();
		b.getBox().set(2.0f, 0.5f, 1.0f);
		b.index = 0;
		
		TestComponent b2 = new TestComponent(new Bounds(1.0f, 1.0f, 1.0f, new Vector3f(0.3f, 0.5f, -1.2f)));
		b2.index = 1;
		
		n4.addComponent(b);
		n3.addComponent(b2);
		
		game.getEventManager().registerListener(b);
		game.getEventManager().registerListener(b2);
		game.loadScene(s);
		game.run(args);
	}
}
