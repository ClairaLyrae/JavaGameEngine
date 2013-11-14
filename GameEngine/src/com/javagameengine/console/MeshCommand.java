package com.javagameengine.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.javagameengine.Game;
import com.javagameengine.graphics.mesh.InvalidTextureException;
import com.javagameengine.graphics.mesh.Mesh;
import com.javagameengine.graphics.mesh.MeshUtil;
import com.javagameengine.graphics.mesh.Texture;
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
		Texture t = null;
		try
		{
			index = Integer.parseInt(args[1]);
		} catch (NumberFormatException e)
		{
			return "Invalid index specified.";
		}
		try
		{
			m = MeshUtil.loadModel(new File(args[0]));
			if(args.length > 2)
			{
				t = Texture.loadTexture(new File(args[2]));
			}
		} catch (IOException | InvalidTextureException e)
		{
			System.out.println(args[0]);
			return "Error loading.";
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
				if(t != null)
					b.setTexture(t);
				
				n.addComponent(b);
				return "Mesh created";
			}
		}
		return "Executed the mesh command! Argument: " + args[0];
	}
}
