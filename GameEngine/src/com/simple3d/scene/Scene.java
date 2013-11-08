package com.simple3d.scene;

import com.simple3d.Physics;
import com.simple3d.Graphics;
import com.simple3d.Logic;

public class Scene implements Logic, Graphics, Physics
{
	protected Node root = new Node("scene");
	
	public Node getRoot()
	{
		return root;
	}
	
	@Override
	public void graphics()
	{
		if(root == null)
			return;
		root.graphics();
	}

	@Override
	public void logic(int delta)
	{
		if(root == null)
			return;
		root.logic(delta);
	}
	
	@Override
	public void physics(int delta)
	{
		if(root == null)
			return;
		root.physics(delta);
	}

}
