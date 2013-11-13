package com.javagameengine.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.javagameengine.Game;
import com.javagameengine.graphics.mesh.Mesh;
import com.javagameengine.graphics.mesh.MeshUtil;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.TestComponent;

public class MeshCommand extends Command
{
	public MeshCommand()
	{
		super("mesh", 2);
	}

	@Override
	public String execute(String[] args)
	{
		int index = 0;
		Mesh m;
		try
		{
			index = Integer.parseInt(args[1]);
		} catch (NumberFormatException e)
		{
			return "Invalid index specified.";
		}
		try
		{
			File f = new File(".");
			for(File n : f.listFiles())
				System.out.println(n.getName());
			m = MeshUtil.loadModel(new File(args[0]));
		} catch (FileNotFoundException e)
		{
			System.out.println(args[0]);
			return "Mesh file '" + args[0] + "' not found.";
		} catch (IOException e)
		{
			return "Error reading from file.";
		} 
		if(m == null)
			return "Mesh file not found.";
		Game g = Game.getHandle();
		if(g != null)
		{
			Scene s = g.getActiveScene();
			if(s != null)
			{
				Node n = new Node("Mesh " + index);
				s.getRoot().addChild(n);
				TestComponent b = new TestComponent(m, index);
				n.addComponent(b);
				return "Mesh created";
			}
		}
		return "Executed the mesh command! Argument: " + args[0];
	}
}
