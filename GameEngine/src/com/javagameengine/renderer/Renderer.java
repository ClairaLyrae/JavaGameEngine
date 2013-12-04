package com.javagameengine.renderer;

import static org.lwjgl.opengl.GL11.*;
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

import javax.swing.RootPaneContainer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.lights.Light;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.console.Console;
import com.javagameengine.math.Color4f;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.component.CoordinateGrid;
import com.javagameengine.gui.GUI;
import com.javagameengine.gui.WelcomeGUI;

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
	public static Matrix4f modelview_matrix = new Matrix4f();
	public static Matrix4f projection_matrix = new Matrix4f();
	private static List<RenderOperation> operations = new ArrayList<RenderOperation>();
	
	public static void render()
	{
		int width = Display.getWidth();
		int height = Display.getHeight();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.2f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
	    GL11.glEnable(GL11.GL_CULL_FACE);
	    GL11.glEnable(GL11.GL_NORMALIZE);
	    
	    
		projection_matrix = Matrix4f.perspectiveMatrix(45.0f, ((float) width / (float) height), 0.1f, 100.0f);
		modelview_matrix.loadIdentity();
			

	    // LIGHT SETUP (Since its before camera transform, it moves with camera)

		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(1f).put(1f).put(1f).put(0.0f).flip();
		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(1f).put(1f).put(1f).put(1f).flip();
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(0f).put(0f).put(0f).put(0.2f).flip();
		FloatBuffer specular = BufferUtils.createFloatBuffer(4);
		specular.put(1f).put(1f).put(1f).put(1.0f).flip();
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, specular);
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_INTENSITY, specular);
		glEnable(GL_LIGHT0);

	    // CAMERA MOVEMENT TEMPORARY

		Transform cam = new Transform().inherit(camerat);
		modelview_matrix = Matrix4f.lookAtMatrix(0f,5f,10f,0f,0f,0f,0f,1f,0f);
		modelview_matrix.multiply(cam.getTransformMatrix());
		
		//Material mat = AssetManager.getMaterial("ship");
		glShadeModel(GL_SMOOTH);
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL);

		// RENDER OPS
		for(RenderOperation op : operations)
		{
			// Load the render operation transform data
			Transform transform = op.getTransform();
			
			Matrix4f modelviewtemp = modelview_matrix.multiplyInto(transform.getTransformMatrix(), null);

			int progID = op.getGraphicsObject().bind();
			
			
			FloatBuffer MV_buffer2 = modelviewtemp.toBuffer();
			int loc = glGetUniformLocation(progID, "mv");
			glUniformMatrix4(loc, false, MV_buffer2);

			FloatBuffer P_buffer = projection_matrix.toBuffer();
			loc = glGetUniformLocation(progID, "p");
			glUniformMatrix4(loc, false, P_buffer);
			
			Color4f lightcol = light.getColor();
			loc = glGetUniformLocation(progID, "light_diffuse");
			glUniform4f(loc, lightcol.r, lightcol.g, lightcol.b, lightcol.a);
			loc = glGetUniformLocation(progID, "light_position");
			glUniform4f(loc, 10f, 10f, 5f, 1f);
			
			
			
			
			op.getGraphicsObject().draw();
		}

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
	    
	    GL11.glPushMatrix();
	    // draw gui
	  //  Scene s = Game.getHandle().getActiveScene();
	    WelcomeGUI welcome = new WelcomeGUI();
	    welcome.draw();
	    
	  //  GLMenuWindow.draw();
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

	public static Transform camerat = new Transform();
	public static Light light;
	private Renderer()
	{
		light = new Light();
		light.setColor(Color4f.red);
	}
}





//Texture t = AssetManager.getTexture("ship_diff");
//t.bind();
//
//glEnable(GL_TEXTURE_2D);
//
//
//
//glBegin(GL_QUADS);
//glColor4f(1f, 2f, 3f, 1f);
//glTexCoord2f(0f, 0f);
//glVertex3f(100f, 100f, 0f);
//glTexCoord2f(0f, 1f);
//glVertex3f(100f, 200f, 0f);
//glTexCoord2f(1f, 1f);
//glVertex3f(200f, 200f, 0f);
//glTexCoord2f(1f, 0f);
//glVertex3f(200f, 100f, 0f);
//glEnd();











//public static void printMatrixStates()
//{
//	FloatBuffer fb = BufferUtils.createFloatBuffer(16);
//	glGetFloat(GL_MODELVIEW_MATRIX, fb);
//	Matrix4f gl_MV = new Matrix4f().fromBuffer(fb).transpose();
//	fb.rewind();
//	GL11.glGetFloat(GL_PROJECTION_MATRIX, fb);
//	Matrix4f gl_P = new Matrix4f().fromBuffer(fb).transpose();
//	fb.rewind();
//	
//	System.out.println("gl MV matrix: " + gl_MV);
//	System.out.println("custom MV matrix: " + modelview_matrix);
//	System.out.println("gl P matrix: " + gl_P);
//	System.out.println("custom P matrix: " + projection_matrix);
//	System.out.println("gl MV*P matrix: " + gl_MV.multiplyInto(gl_P, null));
//	System.out.println("custom MV*P matrix: " + modelview_matrix.multiplyInto(projection_matrix, new Matrix4f()));
//}







//public static void render()
//{
//	// SETUP
//
//	int width = Display.getWidth();
//	int height = Display.getHeight();
//	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//	GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
//    GL11.glMatrixMode(GL11.GL_PROJECTION);
//    GL11.glLoadIdentity();
//	GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
//	projection_matrix = Matrix4f.perspectiveMatrix(45.0f, ((float) width / (float) height), 0.1f, 100.0f);
//	modelview_matrix.loadIdentity();
//	
//	GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.2f); // Black Background
//	GL11.glClearDepth(1.0f); // Depth Buffer Setup
//	GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
//	GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
//    GL11.glEnable(GL11.GL_CULL_FACE);
//    GL11.glEnable(GL11.GL_NORMALIZE);
//	GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations		
//
//    // VIEW SETUP
//	
//    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
//    glLoadIdentity();
//    GL11.glPushMatrix();
//
//
//    // LIGHT SETUP (Since its before camera transform, it moves with camera)
//
//	FloatBuffer position = BufferUtils.createFloatBuffer(4);
//	position.put(1f).put(1f).put(1f).put(0.0f).flip();
//	FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
//	diffuse.put(1f).put(1f).put(1f).put(1f).flip();
//	FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
//	ambient.put(0f).put(0f).put(0f).put(0.2f).flip();
//	FloatBuffer specular = BufferUtils.createFloatBuffer(4);
//	specular.put(1f).put(1f).put(1f).put(1.0f).flip();
//	GL11.glPointSize(5f);
//	GL11.glColor3f(1f, 0f, 0f);
//	glBegin(GL11.GL_POINTS); // Start Drawing 
//	glVertex3f(position.get(), position.get(), position.get()); 
//	GL11.glEnd();
//	position.rewind();
//    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
//    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, ambient);
//    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, diffuse);
//    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, specular);
//    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_INTENSITY, specular);
//	glEnable(GL_LIGHT0);
//	specular.rewind();
//	diffuse.rewind();
//	glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);
//	glMaterial(GL_FRONT, GL_SPECULAR, specular);
//	glMateriali(GL_FRONT, GL_SHININESS, 50);
//	
//
//    // CAMERA MOVEMENT TEMPORARY
//
//	Transform cam = new Transform().inherit(camerat);
////	
//	modelview_matrix = Matrix4f.lookAtMatrix(0f,5f,10f,0f,0f,0f,0f,1f,0f);
//	GLU.gluLookAt(0f,5f,10f,0f,0f,0f,0f,1f,0f);
//
//	modelview_matrix.multiply(cam.getTransformMatrix());
//	//cam.getTransformMatrix().multiplyInto(modelview_matrix, modelview_matrix);
//
//	//cam.getTranslationMatrix().multiplyInto(modelview_matrix, modelview_matrix);
//	//cam.getRotationMatrix().multiplyInto(modelview_matrix, modelview_matrix);
//	//cam.getScaleMatrix().multiplyInto(modelview_matrix, modelview_matrix);
//	
//	Vector3f cpos = camerat.getPosition();
//	Vector3f cscale = camerat.getScale();
//	Vector4f crot = camerat.getRotation().toAxisAngle();
//	// We need to translate to our new position, then scale it, and finally rotate. Order is critical.
//	GL11.glTranslatef(cpos.x, cpos.y, cpos.z);
//	GL11.glScalef(cscale.x, cscale.y, cscale.z);
//	GL11.glRotatef(crot.w*FastMath.RAD_TO_DEG, crot.x, crot.y, crot.z);
//	
//	//modelview_matrix.transpose().translateTransform(cpos.x, cpos.y, cpos.z).transpose();
//	//modelview_matrix.transpose().scaleTransform(cscale.x, cscale.y, cscale.z).transpose();
//	//modelview_matrix.multiplyInto(Matrix4f.rotationMatrix(crot.w, crot.x, crot.y, crot.z), modelview_matrix);
//	
//	Mesh mesh = AssetManager.getMesh("box");
//	Material mat = AssetManager.getMaterial("ship");
//   
//
//	Matrix4f MVP = projection_matrix.multiplyInto(modelview_matrix, new Matrix4f());
//	
//	
//
//	//glPointSize(5f);
//	//mesh.setMode(Mesh.Mode.POINT);
//		GL11.glEnable(GL11.GL_LIGHTING);
//	    GL11.glEnable(GL11.GL_LIGHT0);   
//
//		glPushAttrib( GL_ALL_ATTRIB_BITS );
//
//	    glShadeModel(GL_SMOOTH);
//		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL);
//		glPointSize(5f);
//		mat.bind();
//
//		//printMatrixStates();
//
////		FloatBuffer fb1 = BufferUtils.createFloatBuffer(16);
////		glGetFloat(GL_MODELVIEW_MATRIX, fb1);
////		Matrix4f gl_MV = new Matrix4f().fromBuffer(fb1).transpose();
////		fb1.rewind();
////		FloatBuffer fb2 = BufferUtils.createFloatBuffer(16);
////		GL11.glGetFloat(GL_PROJECTION_MATRIX, fb2);
////		Matrix4f gl_P = new Matrix4f().fromBuffer(fb2).transpose();
////		fb2.rewind();
//		
//		FloatBuffer MV_buffer = modelview_matrix.toBuffer();
//		int loc = glGetUniformLocation(mat.getId(), "mv");
//		glUniformMatrix4(loc, false, MV_buffer);
//		//glUniformMatrix4(loc, true, fb1);
//		
//		FloatBuffer P_buffer = projection_matrix.toBuffer();
//		loc = glGetUniformLocation(mat.getId(), "p");
//		glUniformMatrix4(loc, false, P_buffer);
//		//glUniformMatrix4(loc, true, fb1);
//		
//		mesh.draw();
//		
//        GL11.glDisable(GL11.GL_LIGHT0);
//        GL11.glDisable(GL11.GL_LIGHTING);
//
//	//mesh.setMode(Mesh.Mode.TRIANGLE);
//	glUseProgram(0);
//	
//	// RENDER OPS
//	for(RenderOperation op : operations)
//	{
//		
//		GL11.glPushMatrix();
//	    
//		// Load the render operation transform data
//		Transform transform = op.getTransform();
//		Vector3f pos = transform.getPosition();
//		Vector3f scale = transform.getScale();
//		Vector4f rot = transform.getRotation().toAxisAngle();
//		
//		// We need to translate to our new position, then scale it, and finally rotate. Order is critical.
//		GL11.glTranslatef(pos.x, pos.y, pos.z);
//		GL11.glScalef(scale.x, scale.y, scale.z);
//		GL11.glRotatef(rot.w*FastMath.RAD_TO_DEG, rot.x, rot.y, rot.z);
//		
//		mat.bind();
//		op.getGraphicsObject().draw();
//		
//		GL11.glPopMatrix();
//	}
//    GL11.glPopMatrix();
//
//    glUseProgram(0);
//	glDisable(GL_TEXTURE_2D);
//	glDisable(GL_LIGHTING);
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    // DRAW CONSOLE
//	
//    GL11.glDisable(GL11.GL_CULL_FACE);
//    GL11.glMatrixMode(GL11.GL_PROJECTION);
//    GL11.glLoadIdentity();
//	glOrtho(0, width, 0, height, 0f, 1f);
//    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
//    glLoadIdentity();
//    GL11.glPushMatrix();
//    Console.draw();
//    GL11.glPopMatrix();
//	Display.update();
//}
