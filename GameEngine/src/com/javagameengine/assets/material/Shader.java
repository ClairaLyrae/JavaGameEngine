package com.javagameengine.assets.material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.*;

import com.javagameengine.assets.NativeObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Compiles GLSL shader files into a representative object.
 * @author ClairaLyrae
 */
public class Shader extends NativeObject
{
	public enum Type 
	{
		VERTEX(GL_VERTEX_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER);
		private int param;
		private Type(int param)
		{
			this.param = param;
		}
		public int getGLParam()
		{
			return param;
		}
	}
	
    private Type type;
    private String source = null;
    
    private Shader(Type type, String data)
    {
    	super(Shader.class);
    	this.type = type;
    	this.source = data;
    }
    
    public static Shader loadFromFile(File f) throws IOException
    {
    	 Type type = null;
    	 String[] fsplit = f.getName().split("\\.");

		 if(f.getName().contains("vert"))
			 type = Type.VERTEX;
		 else if(f.getName().contains("frag"))
			 type = Type.FRAGMENT;
		 else if(f.getName().contains("geom"))
			 type = Type.GEOMETRY;
    	 if(type == null)
    		 throw new IllegalStateException("Shader is of unknown type");
    	 
    	 StringBuilder shaderSource = new StringBuilder();
         BufferedReader reader = null;
         try 
         {
             reader = new BufferedReader(new FileReader(f));
             String line;
             while ((line = reader.readLine()) != null)
                 shaderSource.append(line).append('\n');
         } finally {
             if(reader != null) 
            	 reader.close();
         }
         Shader sh = new Shader(type, shaderSource.toString());
         return sh;
    }

    public Type getType()
    {
    	return type;
    }
    
	@Override
	public void destroy()
	{
		if(id != -1)
	        glDeleteShader(id);
	}

	@Override
	public boolean create()
	{
		if(id != -1)
			destroy();
        id = glCreateShader(type.getGLParam());
        glShaderSource(id, source);
        glCompileShader(id);
        if(GL20.glGetShader(id, GL_COMPILE_STATUS) == GL_FALSE) 
        {
            System.out.println(glGetShaderInfoLog(id, 1024));
            id = -1;
            return false;
        }
        return true;
	}
	
	public String toString()
	{
		return "type=" + type.toString();
	}
}