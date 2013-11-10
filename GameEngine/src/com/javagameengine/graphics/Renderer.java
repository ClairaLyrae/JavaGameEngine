package com.javagameengine.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Renderer is the main class that handles the rendering pipeline. The Renderer holds a ViewState which describes the
 * viewpoint of the renderer and pre-processing effects. It also has a RenderTarget which is what the Renderer will
 * draw into. 
 * <p>
 * During the scene graph traversal, the current Renderer is loaded with RenderOperation objects. The RenderOperation 
 * objects have three main things: A bounding box, a RenderState, and a pointer to a render method. When adding these to 
 * the Renderer, it first checks the bounding box based on the ViewState to see if the object can be ignored entirely. 
 * Next, it looks at the RenderState object and tries to group it with other similar RenderStates. 
 * <p>
 * Once the scene is traversed and all renderOperations are given to the Renderer, the renderer draws them. First it loads
 * the ViewState, then the RenderTarget. Next, it goes through the queue of RenderOperations, loading the RenderState for 
 * each subsequent group of RenderOperations and then drawing them. Finally, it does post-processing on the render, and 
 * finalizes the RenderTarget.
 * @author ClairaLyrae
 */
public class Renderer
{
	private ViewState view;
	private RenderTarget target;
	private PriorityQueue<RenderOperation> operations = new PriorityQueue<RenderOperation>();
	
	
	public RenderTarget getRenderTarget()
	{
		return target;
	}
	
	public void setRenderTarget(RenderTarget rt)
	{
		this.target = rt;
	}
	
	public void setViewState(ViewState vs)
	{
		this.view = vs;
	}
	
	public ViewState getViewState()
	{
		return view;
	}
	
	public void render()
	{
		view.load();
		target.load();
		target.initialize();
		// While queue !empty:
		// 		Load RenderState for next group of operations
		//		draw operations
		// Do post-processing
		target.finalize();
	}
	
	public boolean queue(RenderOperation ro)
	{
		operations.add(ro);
		return true;
	} 
}
