package com.javagameengine.scene.component;

import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.RenderableComponent;

public class MeshRenderer extends RenderableComponent
{
	private Mesh mesh = null;
	private Material material = null;
	
	public MeshRenderer(Material mat, Mesh m)
	{
		mesh = m;
		material = mat;
	}
	
	public Mesh getMesh()
	{
		return mesh;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMesh(Mesh m)
	{
		this.mesh = m;
	}
	
	public void setMaterial(Material m)
	{
		this.material = m;
	}

	@Override
	public Bounds getBounds()
	{
		if(mesh == null)
			return Bounds.getVoid();
		return mesh.getBounds();
	}
	
	public void onUpdate(float delta)
	{
		// Update mesh bounds and vertex buffers... if needed!
	}
	
	@Override
	public boolean isTransparent()
	{
		if(material == null)
			return false;
		return material.isTransparent();
	}

	@Override
	public Bindable getBindable()
	{
		return material;
	}

	@Override
	public Drawable getDrawable()
	{
		return mesh;
	}
}
