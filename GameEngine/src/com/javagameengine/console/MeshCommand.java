package com.javagameengine.console;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.MeshRenderer;

/**
 * Console command for loading in meshes from the AssetManager into the current scene. Creates
 * a MeshRenderer component with the given Material and Mesh.
 */
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
		Mesh m = AssetManager.getMesh(args[0]);
		Material mat = null;
		if(args.length > 2)
			mat = AssetManager.getMaterial(args[2]);
		try
		{
			index = Integer.parseInt(args[1]);
		} catch (NumberFormatException e)
		{
			return "Invalid index specified.";
		}
//		try
//		{
//			m = MeshUtil.loadModel(new File(args[0]));
//			if(args.length > 2)
//			{
//				t = Texture.loadFromFile(new File(args[2]));
//			}
//		} catch (IOException | InvalidTextureException e)
//		{
//			System.out.println(args[0]);
//			return "Error loading.";
//		}
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
				
				MeshRenderer ren = new MeshRenderer(mat, m);
				n.addComponent(ren);
				return "Mesh created";
			}
		}
		return "Executed the mesh command! Argument: " + args[0];
	}
}
