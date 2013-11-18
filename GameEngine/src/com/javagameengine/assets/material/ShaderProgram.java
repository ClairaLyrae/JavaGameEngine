package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.File;

import org.lwjgl.opengl.Display;

import com.javagameengine.assets.mesh.NativeObject;

public class ShaderProgram extends NativeObject
{
	
	public ShaderProgram(Shader ... e)
	{
		super(ShaderProgram.class);
		for(Shader se : e)
		{
			int type = se.getType().ordinal();
			if(elements[type] == null)
				elements[type] = se;
		}
	}

	public Shader getShader(Shader.Type type)
	{
		return elements[type.ordinal()];
	}
	
	public void setShader(Shader se)
	{
		if(id != -1)
			throw new IllegalStateException("Shader program is already bound, cannot modify.");
		elements[se.getType().ordinal()] = se;
	}

	private Shader[] elements = new Shader[Shader.Type.values().length];
	
    public void create()
    {
    	if(id != -1)
			destroy();
        id = glCreateProgram();
        for(int i = 0; i < elements.length; i++)
        {
        	if(elements[i] != null)
        	{
        		if(elements[i].getId() == -1)
					elements[i].create();
                glAttachShader(id, elements[i].getId());
        	}
        }
        glLinkProgram(id);
        glValidateProgram(id);
    }

	@Override
	public void destroy()
	{
		if(id != -1)
	        glDeleteProgram(id);
	}
}
