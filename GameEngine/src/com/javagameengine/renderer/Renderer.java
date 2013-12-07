package com.javagameengine.renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.List;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.console.Console;
import com.javagameengine.math.Color4f;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.math.Transform;
import com.javagameengine.scene.component.Camera;
import com.javagameengine.scene.component.Light;

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
	public static Renderer handle = new Renderer();
	public static Matrix4f model_matrix = new Matrix4f();
	public static Matrix4f view_matrix = new Matrix4f();
	public static Matrix4f projection_matrix = new Matrix4f();
	
	public static void initialize()
	{
		PixelFormat pixelf;
		try
		{
			pixelf = new PixelFormat().withSamples(4).withDepthBits(24).withSRGB(true);
			Display.setDisplayMode(new DisplayMode(1280, 768));
			Display.create(pixelf); // BLAH
		} 
		catch (LWJGLException e)
		{
			pixelf = new PixelFormat().withDepthBits(24).withSRGB(true);
			try
			{
				Display.create(pixelf);
			} catch (LWJGLException e1)
			{
				Sys.alert("Error", "Initialization failed!\n\n" + e1.getMessage());
				System.exit(0);
			}
		}
		for(int i = 0; i < MAX_LAYERS; i++)
		{
			layers[i] = new RenderQueue();
			transparentLayers[i] = new RenderQueue();
		}
	}
	
	public static final int MAX_PASSES = 10;
	public static final int MAX_LAYERS = 10;
	public static final int MAX_LIGHTS = 10;
	protected static RenderPass[] passes = new RenderPass[MAX_PASSES];	// These are the different render views we can use. 
	protected static RenderQueue[] layers = new RenderQueue[MAX_LAYERS];	// These are the different layers we can draw in. Each layer handles drawing inside itself
	protected static RenderQueue[] transparentLayers = new RenderQueue[MAX_LAYERS]; 
	
	protected static Light[] lights = new Light[MAX_LIGHTS];
	protected static int light_index = 0;
	
	public static void render()
	{		
		// First off, we need to set up the render target & viewport & buffers
		int width = Display.getWidth();
		int height = Display.getHeight();
		
		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport


		if(camera != null)
		{

			GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
			GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		    GL11.glEnable(GL11.GL_CULL_FACE);
		    GL11.glEnable(GL11.GL_NORMALIZE);
		    
			glClear(GL_DEPTH_BUFFER_BIT);
			glDepthMask(true);
			GL11.glClearDepth(1.0f); // Depth Buffer Setup
			
			
			
			
		    GL11.glMatrixMode(GL11.GL_PROJECTION);
		    GL11.glLoadIdentity();
		    //GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f);
		    GLU.gluPerspective(camera.getFOV(), camera.getAspect(), camera.getDepthNear(), camera.getDepthFar());
		    //projection_matrix = Matrix4f.perspectiveMatrix(45.0f, ((float) width / (float) height), 0.1f, 100.0f);
		    projection_matrix = camera.getProjectionMatrix();
			view_matrix = camera.getViewMatrix();
		    GL11.glMatrixMode(GL11.GL_MODELVIEW);
		    GL11.glLoadIdentity();
		    GL11.glMultMatrix(view_matrix.transposeInto(null).toBuffer());

			glDisable(GL_BLEND);
			for(RenderQueue q : layers)
				q.render();
			glDepthMask(false);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			for(RenderQueue q : transparentLayers)
				q.render();
			glDepthMask(true);
		}
		
	    glUseProgram(0);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
	    // DRAW CONSOLE
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 0f, 1f);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
	    glLoadIdentity();
	    GL11.glPushMatrix();
	    Console.draw();
	    GL11.glPopMatrix();
	    
	    GL11.glPushMatrix();
	    // draw gui
	    //  Scene s = Game.getHandle().getActiveScene();
	    //WelcomeGUI welcome = new WelcomeGUI();
	    //welcome.draw();
	    
	    // GLMenuWindow.draw();
	    GL11.glPopMatrix();
	    
	    
		Display.update();
	}
	
	public static Camera camera;
	
	public static void reset()
	{
		for(int i = 0; i < MAX_LIGHTS; i++)
			lights[i] = null;
		light_index = 0;
		for(int i = 0; i < MAX_LAYERS; i++)
		{
			RenderQueue q = layers[i];
			RenderQueue tq = transparentLayers[i];
			if(q != null)
				q.clearQueue();
			if(tq != null)
				tq.clearQueue();
		}
	}
	
	
	public static void queue(Light l)
	{
		if(light_index >= lights.length)
		{
			for(int i = 0; i < lights.length; i++)
			{
				if(lights[i].getUsage() == Light.Usage.TRANSIENT)
				{
					lights[i] = l;
					return;
				}
			}
		}
		else
			lights[light_index++] = l;
	}
	
	public void queue(RenderPass v)
	{
		
	}
	
	public static void queue(Renderable r)
	{
		int l = r.getLayer();
		if(l < 0 || l >= MAX_LAYERS)
			return;
		if(r.isTransparent())
			transparentLayers[l].queue(r);
		else
			layers[l].queue(r);
	}

	public static int getNumLights()
	{
		return light_index;
	}
	
	public static Transform camerat = new Transform();
	public static Light light;
	
	private Renderer()
	{
		light = new Light();
		light.setDiffuseColor(Color4f.red);
	}
}
