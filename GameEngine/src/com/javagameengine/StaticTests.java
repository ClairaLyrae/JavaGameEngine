package com.javagameengine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.javagameengine.events.EventManager;
import com.javagameengine.console.Console;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.scene.component.CoordinateGrid;

public class StaticTests
{
	public static Matrix4f view_matrix = new Matrix4f();
	public static Matrix4f model_matrix = new Matrix4f();
	public static Matrix4f projection_matrix = new Matrix4f();
	
	public static void main(String[] args)
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

		AssetManager.loadAll();
		
	    while(!Display.isCloseRequested())
	    {
	    	drawTest();
	    	Display.update();
			Display.sync(60);
	    }
		
	    Display.destroy();
	}
	
	public static void drawTest()
	{

		int width = Display.getWidth();
		int height = Display.getHeight();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);   
	    glLoadIdentity();

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
		specular.rewind();
		diffuse.rewind();
		glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);
		glMaterial(GL_FRONT, GL_SPECULAR, specular);
		glMateriali(GL_FRONT, GL_SHININESS, 50);
		
		GLU.gluLookAt(10f, 10f, 5f, 0f, 0f, 0f, 0f, 1f, 0f);
		
		Mesh mesh = AssetManager.getMesh("sphere");
		Material mat = AssetManager.getMaterial("ship");
		
		System.out.println("MESH:" + mesh + ", MAT:" + mat);
		
		CoordinateGrid grid = new CoordinateGrid(1, 10);
		grid.draw();
//		glPointSize(5f);
//		glLineWidth(5f);
//		glBegin(GL_LINES);
//			glColor3f(0f, 1f, 0f);
//			glVertex3f(0f, 0f, 0f);
//			glVertex3f(0f, 1f, 0f);
//			glVertex3f(0f, -1f, 0f);
//			glVertex3f(1f, 0f, 0f);
//			glVertex3f(-1f, 0f, 0f);
//		glEnd();
//
	    GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_LIGHT0);   

		glColor3f(0f, 1f, 0f);
	    glShadeModel(GL_SMOOTH);
		glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
		mesh.draw();
		
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glDisable(GL11.GL_LIGHTING);
	}
}
