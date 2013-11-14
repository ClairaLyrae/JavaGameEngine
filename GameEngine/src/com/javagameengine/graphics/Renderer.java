package com.javagameengine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.console.Console;
import com.javagameengine.graphics.mesh.InvalidMeshException;
import com.javagameengine.graphics.mesh.InvalidTextureException;
import com.javagameengine.graphics.mesh.Mesh;
import com.javagameengine.graphics.mesh.MeshUtil;
import com.javagameengine.graphics.mesh.Texture;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;

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
	
	private static List<RenderContext> views = new ArrayList<RenderContext>();
	private static RenderTarget target;
	private static List<RenderOperation> operations = new ArrayList<RenderOperation>();
	
	public static RenderTarget getRenderTarget()
	{
		return target;
	}
	
	public static void setRenderTarget(RenderTarget rt)
	{
		target = rt;
	}
	
	public static void registerViewState(RenderContext vs)
	{
		views.add(vs);
	}

    
	public static void render()
	{
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(10.0f).put(5.0f).put(5.0f).put(1.0f).flip();
		
		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();
		
		

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		int width = Display.getWidth();
		int height = Display.getHeight();

		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.2f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations		
		
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
	    glLoadIdentity();
	    GL11.glPushMatrix();

	    // this is where you set up your view:
		GLU.gluLookAt(20f,10f,20f,0f,0f,0f,0f,1f,0f);

	    // and now it's time to set the light position:
		position.rewind();
		GL11.glPointSize(2f);
		GL11.glColor3f(1f, 0f, 0f);
		glBegin(GL11.GL_POINTS); // Start Drawing 
		glVertex3f(position.get(), position.get(), position.get()); 
		GL11.glEnd();
		position.rewind();
		
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);

	    GL11.glEnable(GL11.GL_DEPTH_TEST);
	    GL11.glDepthMask(true);
	    GL11.glEnable(GL11.GL_CULL_FACE);	
	    
	    GL11.glEnable(GL11.GL_NORMALIZE);
	    
		for(RenderOperation op : operations)
		{
			GL11.glPushMatrix();
		    
			// Load the render operation transform data
			Transform transform = op.getTransform();
			Vector3f pos = transform.getPosition();
			Vector3f scale = transform.getScale();
			Vector4f rot = transform.getRotation().toAxisAngle();
			
			// We need to translate to our new position, then scale it, and finally rotate. Order is critical.
			GL11.glTranslatef(pos.x, pos.y, pos.z);
			GL11.glScalef(scale.x, scale.y, scale.z);
			GL11.glRotatef(rot.w*FastMath.RAD_TO_DEG, rot.x, rot.y, rot.z);

			// Load the render state
			RenderState rs = op.getRenderState();
			if(rs != null)
				op.getRenderState().load();
			
			// Draw the operation
			op.getGraphicsObject().draw();
			
			GL11.glPopMatrix();
		}
	    GL11.glPopMatrix();
	    
	    
	    
	    // draw console
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
		glOrtho(0, width, 0, height, 0f, 1f);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
	    glLoadIdentity();
	    GL11.glPushMatrix();
	    
	    Console.draw();
	    
	    GL11.glPopMatrix();
		Display.update();
	}
	
	public static void clearQueue()
	{
		operations.clear();
	}
	
	public static boolean queue(RenderOperation ro)
	{
		operations.add(ro);
		return true;
	}
	
	private static int texid = -1;
	private Renderer()
	{
		File f = new File("texture.png");
		Texture t = null;
		try
		{
			t = Texture.loadTexture(f);
		} catch (IOException | InvalidTextureException e)
		{
			System.out.println("Failed to bind texture");
			e.printStackTrace();
		}
		texid = t.bindTexture();
		System.out.println("Bound texture to id= " + texid);
	}
}
