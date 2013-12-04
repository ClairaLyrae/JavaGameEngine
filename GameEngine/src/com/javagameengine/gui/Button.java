package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.javagameengine.events.Listener;


public class Button implements Listener {
	
    public void Button(String[] args) {
    	
		int displayWidth = 900;
		int displayHeight = 600;
		int buttonWidth = 300;
		int buttonHeight = 100;
		
		int button1xpos = 100;
		int button1ypos = 100;
    	
        try {
            // Sets the width of the display to 500 and the height to 350
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            // Sets the title of the display to "Episode 2 - Display"
            Display.setTitle("Episode 3 - OpenGL Rendering");
            // Creates and shows the display
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        // Enter the state that is required for modify the projection. Note that, in contrary to Java2D, the vertex
        // coordinate system does not have to be equal to the window coordinate space. The invocation to glOrtho creates
        // a 2D vertex coordinate system like this:
        // Upper-Left:  (0,0)   Upper-Right:  (640,0)
        // Bottom-Left: (0,480) Bottom-Right: (640,480)
        // If you skip the glOrtho method invocation, the default 2D projection coordinate space will be like this:
        // Upper-Left:  (-1,+1) Upper-Right:  (+1,+1)
        // Bottom-Left: (-1,-1) Bottom-Right: (+1,-1)
        glMatrixMode(GL_PROJECTION);
        // left, right, bottom, top
        glOrtho(0, displayWidth, 0, displayHeight, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        // While we aren't pressing the red button on the display
        while (!Display.isCloseRequested()) {
            // Clear the 2D contents of the window.
            glClear(GL_COLOR_BUFFER_BIT);
            // ">>" denotes a possibly modified piece of OpenGL documentation (http://www.opengl.org/sdk/docs/man/)
            // >> glBegin and glEnd delimit the vertices that define a primitive or a group of like primitives.
            // >> glBegin accepts a single argument that specifies how the vertices are interpreted.
            // All upcoming vertex calls will be taken as points of a quadrilateral until glEnd is called. Since
            // this primitive requires four vertices, we will have to call glVertex four times.
/*            glBegin(GL_QUADS);
            // >> glVertex commands are used within glBegin/glEnd pairs to specify point, line, and polygon vertices.
            // >> glColor sets the current colour. (All subsequent calls to glVertex will be assigned this colour)
            // >> The number after 'glVertex'/'glColor' indicates the amount of components. (xyzw/rgba)
            // >> The character after the number indicates the type of arguments.
            // >>      (for 'glVertex' = d: Double, f: Float, i: Integer)
            // >>      (for 'glColor'  = d: Double, f: Float, b: Signed Byte, ub: Unsigned Byte)
            glColor3f(1.0f, 0.0f, 0.0f);                    // Pure Green
            glVertex2i(0, 0);                               // Upper-left
            glColor3b((byte) 0, (byte) 127, (byte) 0);      // Pure Red
            glVertex2d(640.0, 0.0);                         // Upper-right
            glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
            glVertex2f(640.0f, 480.0f);                     // Bottom-right
            glColor3d(0.0d, 0.0d, 1.0d);                    // Pure Blue
            glVertex2i(0, 480);                             // Bottom-left
            // If we put another four calls to glVertex2i here, a second quadrilateral will be drawn.
            glEnd();*/
            
            // inner quad
            glBegin(GL_QUADS);
            glColor3ub((byte) 255, (byte) 255, (byte) 255); // white
            glVertex2f(button1xpos, button1ypos);
            
            glVertex2f(button1xpos+buttonWidth, button1ypos);
           
            glColor3d(0, 0, 0);
            glVertex2f(button1xpos+buttonWidth, button1ypos+buttonHeight);
  
            glVertex2f(button1xpos, button1ypos+buttonHeight);
            
            
   
            glEnd();
        }
    }

}
