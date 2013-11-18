package com.javagameengine.renderer;

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
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.*;

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

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.InvalidTextureException;
import com.javagameengine.assets.material.ShaderProgram;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.InvalidMeshException;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.mesh.MeshUtil;
import com.javagameengine.console.Console;
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
		// SETUP

		int width = Display.getWidth();
		int height = Display.getHeight();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.2f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
	    GL11.glEnable(GL11.GL_CULL_FACE);
	    GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations		

	    // VIEW SETUP
		
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
	    glLoadIdentity();
	    GL11.glPushMatrix();


	    // LIGHT SETUP (Since its before camera transform, it moves with camera)

		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(1f).put(1f).put(1f).put(0.0f).flip();
		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(1f).put(1f).put(1f).put(1f).flip();
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(0f).put(0f).put(0f).put(0.2f).flip();
		FloatBuffer specular = BufferUtils.createFloatBuffer(4);
		specular.put(1f).put(1f).put(1f).put(1.0f).flip();
		GL11.glPointSize(5f);
		GL11.glColor3f(1f, 0f, 0f);
		glBegin(GL11.GL_POINTS); // Start Drawing 
		glVertex3f(position.get(), position.get(), position.get()); 
		GL11.glEnd();
		position.rewind();
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, specular);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_INTENSITY, specular);
		glEnable(GL_LIGHT0);
		specular.rewind();
		diffuse.rewind();
		glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);
		glMaterial(GL_FRONT, GL_SPECULAR, specular);
		glMateriali(GL_FRONT, GL_SHININESS, 50);
		

	    // CAMERA MOVEMENT TEMPORARY

		GLU.gluLookAt(0f,5f,10f,0f,0f,0f,0f,1f,0f);
		Transform cam = new Transform().inherit(camerat);
		Vector3f cpos = camerat.getPosition();
		Vector3f cscale = camerat.getScale();
		Vector4f crot = camerat.getRotation().toAxisAngle();
		// We need to translate to our new position, then scale it, and finally rotate. Order is critical.
		GL11.glTranslatef(cpos.x, cpos.y, cpos.z);
		GL11.glScalef(cscale.x, cscale.y, cscale.z);
		GL11.glRotatef(crot.w*FastMath.RAD_TO_DEG, crot.x, crot.y, crot.z);

	    // SHADER TEST

		Texture texspec = AssetManager.getTexture("ship_spec");
		Texture texdiff = AssetManager.getTexture("ship_diff");
		Texture texemissive = AssetManager.getTexture("ship_emissive");
		Texture texnorm = AssetManager.getTexture("ship_bump");
		glEnable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);
	    glUseProgram(sprog.getId());
	    
		int loc = glGetUniformLocation(sprog.getId(), "TextureUnit0");
		glUniform1i(loc, 0);
		loc = glGetUniformLocation(sprog.getId(), "TextureUnit1");
		glUniform1i(loc, 1);
		loc = glGetUniformLocation(sprog.getId(), "TextureUnit2");
		glUniform1i(loc, 2);
		loc = glGetUniformLocation(sprog.getId(), "TextureUnit3");
		glUniform1i(loc, 3);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texdiff.getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texnorm.getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texspec.getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texemissive.getId());

		glBegin(GL_QUADS);
			glNormal3f(0f, 0f, 1f);
			glTexCoord2f(0f, 0f);
			glVertex3f(0f, 5f, 0f);
			glTexCoord2f(0f, 1f);
			glVertex3f(5f, 5f, 0f);
			glTexCoord2f(1f, 1f);
			glVertex3f(5f, 0f, 0f);
			glTexCoord2f(1f, 0f);
			glVertex3f(0f, 0f, 0f);
		glEnd();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	    glUseProgram(0);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		
		// RENDER OPS
		
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
			
			// Draw the operation
			op.getGraphicsObject().draw();
			
			GL11.glPopMatrix();
		}
	    GL11.glPopMatrix();

	    glUseProgram(0);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    // DRAW CONSOLE
		
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
	
	public static ShaderProgram sprog;
	public static Transform camerat = new Transform();
	private Renderer()
	{

		sprog = new ShaderProgram(AssetManager.getShader("fnormal"), AssetManager.getShader("vnormal"));
		sprog.create();
	}
}
