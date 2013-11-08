package com.simple3d;

import com.simple3d.math.Vector3f;
import com.simple3d.math.geometry.Box;
import com.simple3d.scene.Node;
import com.simple3d.scene.Scene;
import com.simple3d.scene.component.GBox;
import com.simple3d.scene.component.TestComponent;

public class TestGame extends AbstractGame
{

	public TestGame(String[] args)
	{
		super(args);
	}

	@Override
	protected void onCreate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args)
	{
		TestGame game = new TestGame(args);
		Scene s = new Scene();
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		n1.addChild(n2);
		n2.addChild(n4);
		n2.addChild(n3);
		Node root = s.getRoot();
		root.addChild(n1);
		root.addChild(n4);
		TestComponent c1 = new TestComponent(1);
		TestComponent c2 = new TestComponent(2);
		GBox b = new GBox();
		b.getBox().set(5.0f, 5.0f, 8.0f);
		GBox b2 = new GBox(new Box(1.0f, 2.0f, 5.0f, new Vector3f(0.3f, 0.5f, -1.2f)));
		n2.addComponent(c1);
		n4.addComponent(c2);
		n3.addComponent(b);
		n3.addComponent(b2);
		game.getEventManager().registerListener(b);
		game.getEventManager().registerListener(b2);
		game.loadScene(s);
		game.run(args);
	}
}
