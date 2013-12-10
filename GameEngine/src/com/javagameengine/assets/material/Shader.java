package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL20;

import com.javagameengine.assets.NativeObject;

/**
 * Compiles GLSL shader files into a representative object. Shader files loaded with this class must contain either
 * "frag", "geom", or "vert" in the filename to designate the type of shader. Is a NativeObject, as the compiled shaders
 * are stored on the GPU.
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
    
    public Shader(Type type, String data)
    {
    	super(Shader.class);
    	this.type = type;
    	this.source = data;
    	parseUniforms();
    }
    
    public void parseUniforms()
    {
    	String version = "";

    	List<ShaderVariable> vars = new ArrayList<ShaderVariable>();
    	
    	String[] program = source.split("\n");
    	for(String s : program)
    	{
    		String[] split = s.replaceAll(";", "").split(" ");
    		if(split.length < 1)
    			continue;
    		if(split[0].equalsIgnoreCase("#version"))
    			version = split[1];
    		else
    		{
    			ShaderVariable var = ShaderVariable.parseShaderVariable(s);
    			if(var != null)
    				vars.add(var);
    		}
    	}
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
		return "shader=[type=" + type.toString() + "], id[" + id + "]]";
	}
}