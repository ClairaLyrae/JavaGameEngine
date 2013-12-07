package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import com.javagameengine.math.Color4f;
import com.javagameengine.renderer.Renderer;

public class ShaderVariable
{
	public enum Type {
		INPUT("in"),
		OUTPUT("out"),
		UNIFORM("uniform");
		
		private String name;
		
		private Type(String name)
		{
			this.name = name;
		}

		public static Type parseType(String s)
		{
			for(Type f : Type.values())
			{
				if(s.equalsIgnoreCase(f.getIdentifier()))
					return f;
			}
			return null;
		}
		
		public String getIdentifier()
		{
			return name;
		}
	}
	
	public enum Format {
		FLOAT(""),
		INT("i");
		
		private String name;
		
		private Format(String name)
		{
			this.name = name;
		}

		public static Format parseFormat(String s)
		{
			for(Format f : Format.values())
			{
				if(s.startsWith(f.getIdentifier()))
					return f;
			}
			return null;
		}
		
		public String getIdentifier()
		{
			return name;
		}
	}
	
	public enum Dimension {
		VALUE(null, 1),
		VEC2("vec2", 2),
		VEC3("vec3", 3),
		VEC4("vec4", 4),
		MAT2X2("mat2", 4),
		MAT3X3("mat3", 9),
		MAT4X4("mat4", 16),
		MAT2X3("mat2x3", 6),
		MAT2X4("mat2x4", 8),
		MAT3X4("mat3x4", 12);
		
		private String name;
		private int size;
		
		private Dimension(String name, int size)
		{
			this.name = name;
			this.size = size;
		}
		
		public static Dimension parseDimension(String s)
		{
			for(Dimension d : Dimension.values())
			{
				if(d == Dimension.VALUE)
					continue;
				if(s.contains(d.getIdentifier()))
					return d;
			}
			return Dimension.VALUE;
		}
		
		public String getIdentifier()
		{
			return name;
		}
		
		public int getSize()
		{
			return size;
		}	
	}
	
	protected Type type;
	protected Dimension dim;
	protected Format format;
	
	protected String name;
	
	protected float[] f_array;
	protected int[] i_array;
	
	public static ShaderVariable parseShaderVariable(String definition)   // uniform vec3 waffle;
	{
		String[] split = definition.replace(";", "").split(" ");
		if(split.length != 3)
			return null;
		Type t = Type.parseType(split[0]);
		Format f = Format.parseFormat(split[1]);
		Dimension d = Dimension.parseDimension(split[1]);
		String name = split[2];
		if(t == null || f == null || d == null)
			return null;
		return new ShaderVariable(t, d, f, name);
	}
	
	public String toString()
	{
		return "shaderVariable=[name=" + name + ", type=" + type + ", format=" + format + ", dim=" + dim + "]";
	}
	
	public ShaderVariable(Type type, Dimension dim, Format f, String name)
	{
		this.dim = dim;
		this.format = f;
		this.type = type;
		this.name = name;
		f_array = new float[dim.size];
		i_array = new int[dim.size];
	}
	
	public ShaderVariable(Type type, Dimension dim, Format f, int size, String name)
	{
		this.dim = dim;
		this.format = f;
		this.type = type;
		this.name = name;
		f_array = new float[dim.size*size];
		i_array = new int[dim.size*size];
	}
	
	public void bindUniform(int shaderID)
	{
		if(type != Type.UNIFORM)
			throw new IllegalStateException("Shader variable is not a uniform.");
		int loc = glGetUniformLocation(shaderID, name);
		switch(format)
		{
		case FLOAT:
			switch(dim)
			{
			case MAT2X2:
				GL20.glUniformMatrix2(loc, false, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			case MAT3X3:
				GL20.glUniformMatrix2(loc, false, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			case MAT4X4:
				GL20.glUniformMatrix2(loc, false, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			case VALUE:
				GL20.glUniform1f(loc, f_array[0]);
				break;
			case VEC2:
				GL20.glUniform2(loc, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			case VEC3:
				GL20.glUniform3(loc, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			case VEC4:
				GL20.glUniform4(loc, BufferUtils.createFloatBuffer(f_array.length).put(f_array));
				break;
			}
			break;
		case INT:
			switch(dim)
			{
			case MAT2X2: case MAT3X3: case MAT4X4:
				throw new IllegalStateException("Cannot have an integer matrix shader variable");
			case VALUE:
				GL20.glUniform1i(loc, i_array[0]);
				break;
			case VEC2:
				GL20.glUniform2(loc, BufferUtils.createIntBuffer(i_array.length).put(i_array));
				break;
			case VEC3:
				GL20.glUniform3(loc, BufferUtils.createIntBuffer(i_array.length).put(i_array));
				break;
			case VEC4:
				GL20.glUniform4(loc, BufferUtils.createIntBuffer(i_array.length).put(i_array));
				break;
			}
			break;
		}
	}
	
	public void set(float f)
	{
		f_array[0] = f;
	}

	public void set(float[] f)
	{
		this.f_array = f;
	}

	public void set(int f)
	{
		this.i_array[0] = f;
	}

	public void set(int[] f)
	{
		this.i_array = f;
	}
	
	public Type getType()
	{
		return type;
	}
	
}
