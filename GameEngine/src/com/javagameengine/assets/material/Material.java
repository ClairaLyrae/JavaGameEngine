package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL20;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.NativeObject;
import com.javagameengine.assets.mesh.Attribute;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.RendererState;
import com.javagameengine.renderer.RendererState.BlendMode;

/**
 * Material defines a material composed of Textures and Shaders that can be
 * applied to Renderable objects and rendered in the rendering queue. Implements the
 * Bindable interface. Is a NativeObject, as the shading program with associated 
 * uniforms can be stored as a GPU state. 
 */
public class Material extends NativeObject implements Bindable
{
	public enum TextureType
	{
		DIFFUSE("tex_diffuse"), NORMAL("tex_normal"), SPECULAR("tex_specular"), EMISSIVE(
				"tex_emissive"), CUBE("tex_cube"), ALPHA("tex_alpha");

		private String name;

		private TextureType(String name)
		{
			this.name = name;
		}

		public String getUniformName()
		{
			return name;
		}
	}

	public static Material loadFromFile(File f) throws IOException
	{
		BufferedReader reader = null;
		Material m = new Material();
		try
		{
			reader = new BufferedReader(new FileReader(f));
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] split = line.split(" ");
				if (split.length > 0 && split[0].equalsIgnoreCase("blend"))
				{
					m.setTransparency(true);
					if(split.length > 1)
					{
						BlendMode bm = BlendMode.valueOf(split[1].toUpperCase());
						if(bm != null)
							m.srcBlend = bm;
					}
					if(split.length > 2)
					{
						BlendMode bm = BlendMode.valueOf(split[2].toUpperCase());
						if(bm != null)
							m.destBlend = bm;
					}	
				}
				if (split.length == 2 && split[0].equalsIgnoreCase("s"))
				{
					Shader s = AssetManager.getShader(split[1]);
					if (s == null)
						throw new IllegalStateException("Shader " + split[1]
								+ " could not be found.");
					m.setShader(s);
				}
				if (split.length > 2 && split[0].equalsIgnoreCase("t"))
				{
					Texture t;
					TextureType type = TextureType.valueOf(TextureType.class,
							split[1].toUpperCase());
					if (type == null)
						throw new IllegalStateException("Texture type "
								+ split[1] + " for texture " + split[2]
								+ " is not valid.");
					if (type == TextureType.CUBE)
						t = AssetManager.createCubeMap(split[2]);
					else
						t = AssetManager.getTexture(split[2]);
					if (t == null)
						throw new IllegalStateException("Texture " + split[2]
								+ " could not be found.");
					m.setTexture(type, t);
				}
				if (split.length == 3 && split[0].equalsIgnoreCase("p"))
				{
				}
			}
		} finally
		{
			if (reader != null)
				reader.close();
		}
		return m;
	}

	private boolean blendEnable;

	private Shader[] shaders = new Shader[Shader.Type.values().length];

	private Texture[] textures = new Texture[TextureType.values().length];

	// TEMP 
	public BlendMode srcBlend = BlendMode.ONE;
	// TEMP 
	public BlendMode destBlend = BlendMode.ONE_MINUS_SRC_ALPHA;
	
	private List<ShaderVariable> inputs = new ArrayList<ShaderVariable>();
	private List<ShaderVariable> outputs = new ArrayList<ShaderVariable>();
	
	private float tilingX = 1.0f;
	private float tilingY = 1.0f;
	private float offsetX = 1.0f;
	private float offsetY = 1.0f;
	
	public Material()
	{
		super(Material.class);
	}

	@Override
	public int bind()
	{
		// Finally, bind the program
		glUseProgram(id);
		// Bind all the textures first
		for (TextureType type : TextureType.values())
		{
			int i = type.ordinal();
			if (textures[i] == null)
				continue;
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(textures[i].getType().getGLParam(),
					textures[i].getID());
			int loc = glGetUniformLocation(id, type.getUniformName());
			glUniform1i(loc, i);
		}
		return id;
	}
	
	@Override
	public boolean create()
	{
		if (id != -1)
			destroy();
		id = glCreateProgram();
		// Make sure all shaders are loaded and attach them
		for (int i = 0; i < shaders.length; i++)
		{
			if (shaders[i] != null)
			{
				if (shaders[i].getID() == -1)
					shaders[i].create();
				glAttachShader(id, shaders[i].getID());
			}
		}
		// Make sure all textures are loaded
		for (TextureType type : TextureType.values())
		{
			Texture t = getTexture(type);
			if (t == null)
				continue;
			if (t.getID() == -1)
				t.create();

		}
		// Make sure all attributes are bound correctly
		for (Attribute type : Attribute.values())
		{
			glBindAttribLocation(id, type.ordinal(), type.getAttribName());
		}
		// Link the program and check result
		glLinkProgram(id);
		if (glGetProgram(id, GL_LINK_STATUS) == GL_FALSE)
		{
			System.out.println("Failed to link shader.");
			System.out.println(GL20.glGetShaderInfoLog(id, 1024));
			System.out.println(GL20.glGetProgramInfoLog(id, 1024));
			return false;
		}
		// Validate program and check result
		glValidateProgram(id);
		if (glGetProgram(id, GL_VALIDATE_STATUS) == GL_FALSE)
		{
			System.out.println("Failed to validate shader.");
			System.out.println(GL20.glGetProgramInfoLog(id, 1024));
		}
		return true;
	}

	@Override
	public void destroy()
	{
		if (id != -1)
			glDeleteProgram(id);
	}

	public Shader getShader(Shader.Type type)
	{
		return shaders[type.ordinal()];
	}

	public Shader[] getShaders()
	{
		return shaders;
	}

	public Texture getTexture(TextureType type)
	{
		return textures[type.ordinal()];
	}

	public Texture[] getTextures()
	{
		return textures;
	}

	public boolean isTransparent()
	{
		return blendEnable;
	}

	public void setShader(Shader p)
	{
		shaders[p.getType().ordinal()] = p;
	}

	public void setTexture(TextureType type, Texture t)
	{
		textures[type.ordinal()] = t;
	}

	public void setTransparency(boolean state)
	{
		blendEnable = state;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("textures=[");
		for (TextureType type : TextureType.values())
		{
			if (getTexture(type) != null)
				sb.append(type.toString() + ", ");
		}
		sb.append("], shaders=[");
		for (Shader.Type type : Shader.Type.values())
		{
			if (getShader(type) != null)
				sb.append(type.toString() + ", ");
		}
		sb.append("], transparent=[" + blendEnable + "]");
		return sb.toString();
	}
}
