package com.simple3d.scene.component;

import com.simple3d.io.Mesh;

public class MeshRenderer extends Geometry
{
	private Mesh mesh;
	
	public MeshRenderer(Mesh m)
	{
		mesh = m;
	}
	
	public Mesh getMesh()
	{
		return mesh;
	}
	
	public void setMesh(Mesh m)
	{
		mesh = m;
	}

	@Override
	public void graphics()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logic(int delta)
	{
		// TODO Auto-generated method stub
		
	}
}
