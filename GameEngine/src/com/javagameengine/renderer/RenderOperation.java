package com.javagameengine.renderer;

import java.lang.reflect.Method;
import java.nio.FloatBuffer;

import com.javagameengine.math.Transform;
import com.javagameengine.scene.Bounds;

/**
 * @author ClairaLyrae
 * 
 */
public class RenderOperation implements Comparable<RenderOperation>
{
	// Fullscreen layer. Are we drawing to the game layer, a fullscreen effect layer, or the HUD?
	// Viewport. There could be multiple viewports for e.g. multiplayer splitscreen, for mirrors or portals in the scene, etc.
 	// Viewport layer. Each viewport could have their own skybox layer, world layer, effects layer, HUD layer.
	// Translucency. Typically we want to sort opaque geometry and normal, additive, and subtractive translucent geometry into separate groups.
	// Depth sorting. We want to sort translucent geometry back-to-front for proper draw ordering and perhaps opaque geometry front-to-back to aid z-culling.
	// Material. We want to sort by material to minimize state settings (textures, shaders, constants). A material might have multiple passes.
	
	protected int viewID;
	protected int layerID;
	protected boolean isTranslucent;
	protected float depth;
	protected int materialID;
	protected int meshID;
	
	public int compareTo(RenderOperation ro)
	{
		if(viewID > ro.viewID)
			return 1;
		if(layerID > ro.layerID)
			return 1;
		if(isTranslucent && !ro.isTranslucent)
			return 1;
		if(depth > ro.depth)
			return 1;
		if(materialID > ro.materialID)
			return 1;
		if(meshID > ro.meshID)
			return 1;
		return 0;
	}
	
	private Renderable object;
	private Transform transform;
	
	public RenderOperation(Renderable g, Transform t)
	{
		this.object = g;
		this.transform = t;
	}
	
	public void render()
	{
		object.draw();
	}
	
	public Transform getTransform()
	{
		return transform;
	}
	
	public Renderable getGraphicsObject()
	{
		return object;
	}
}
