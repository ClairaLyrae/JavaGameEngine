package com.javagameengine.console;

import com.javagameengine.Game;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.TestComponent;

public class BoxCmd extends Command
{
	public BoxCmd()
	{
		super("box", 1);
	}

	@Override
	public boolean execute(String[] args)
	{
		Console.println("Executed the box command! Argument: " + args[0]);
		int index = 0;
		try
		{
			index = Integer.parseInt(args[0]);
		} catch (NumberFormatException e)
		{
			Console.println("Invalid index specified.");
			return false;
		}
		Game g = Game.getHandle();
		if(g != null)
		{
			Scene s = g.getActiveScene();
			if(s != null)
			{
				Node n = new Node("Box " + index);
				s.getRoot().addChild(n);
				TestComponent b = new TestComponent(new Bounds(1.0f, 1.0f, 1.0f), index);
				n.addComponent(b);
			}
		}
		return true;
	}
}
