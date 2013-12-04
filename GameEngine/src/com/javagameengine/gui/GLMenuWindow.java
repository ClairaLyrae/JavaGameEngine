package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.opengl.GL11;

import com.javagameengine.scene.Component;
import com.javagameengine.scene.Scene;
import com.javagameengine.events.Listener;
import com.javagameengine.renderer.Renderable;



public class GLMenuWindow extends Component implements Renderable, Listener{

	private boolean visible = true;
	int height;
	int width; 
	
	public GLMenuWindow(int h, int w){
		
		height = h;
		width = w; 
	}
	
	@Override
	public void draw()
	{
		if(!visible)
			return;

		glBegin(GL_QUADS);
	        // >> glVertex commands are used within glBegin/glEnd pairs to specify point, line, and polygon vertices.
	        // >> glColor sets the current colour. (All subsequent calls to glVertex will be assigned this colour)
	        // >> The number after 'glVertex'/'glColor' indicates the amount of components. (xyzw/rgba)
	        // >> The character after the number indicates the type of arguments.
	        // >>      (for 'glVertex' = d: Double, f: Float, i: Integer)
	        // >>      (for 'glColor'  = d: Double, f: Float, b: Signed Byte, ub: Unsigned Byte)
	     glColor4f(1.0f, 0.0f, 0.0f, .5f);                    // red
	        glVertex2i(width/2 - 100, height/2 + 100);                               // Upper-left
	    //    glColor3b((byte) 0, (byte) 127, (byte) 0);      // Pure Red
	        glVertex2d(width/2 + 100, height/2 + 100);                         // Upper-right
	   //     glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
	        glVertex2f(width/2 + 100, height/2 - 100);                     // Bottom-right
	 //       glColor3d(0.0d, 0.0d, 1.0d);                    // Pure Blue
	        glVertex2i(width/2 - 100, height/2 - 100);                             // Bottom-left
	        // If we put another four calls to glVertex2i here, a second quadrilateral will be drawn.
	        glColor4f(0f, 0f, 0f, .5f);                    // blackish
	        glVertex2i(width/2 - 50, height/2 + 50);                               // Upper-left
	    //    glColor3b((byte) 0, (byte) 127, (byte) 0);      // Pure Red
	        glVertex2d(width/2 + 50, height/2 + 50);                         // Upper-right
	   //     glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
	        glVertex2f(width/2 + 50, height/2 - 50);                     // Bottom-right
	 //       glColor3d(0.0d, 0.0d, 1.0d);                    // Pure Blue
	        glVertex2i(width/2 - 50, height/2 - 50);                             // Bottom-left
	        // If we put another four calls to glVertex2i here, a second quadrilateral will be drawn.
	       
		
		glEnd();
		
	}
	
	@Override
	public int bind()
	{
		return -1;
	}
	
	public void onDestroy()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().unregisterListener(this);
	}

	public void onCreate()
	{
		Scene s = getScene();
		if(s != null)
			s.getEventManager().registerListener(this);
	}

	public void onUpdate(int delta)
	{
	}
	

	
}
