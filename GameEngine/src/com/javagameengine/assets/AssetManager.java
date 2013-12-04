package com.javagameengine.assets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Shader;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Mesh;

/**
 * Loads all the assets located in the main game directory. Provides access to the assets.
 * @author ClairaLyrae
 */
public class AssetManager
{
	public static final AssetManager handle = new AssetManager();
	
	public static final String[] meshExtensions = {"obj"};
	public static final String[] shaderExtensions = {"vert", "frag", "geom", "geo", "vs", "fs", "gs", "glsl"};
	public static final String[] textureExtensions = {"bmp", "jpg", "jpeg", "gif"};
	public static final String[] materialExtensions = {"mtl"};
	
	public static final String dir = "assets";
	public static final String meshDir = "assets/meshes";
	public static final String shaderDir = "assets/shaders";
	public static final String textureDir = "assets/textures";
	public static final String materialDir = "assets/materials";
	public static final String audioDir = "assets/audio";
	
	private static Map<String, Mesh> meshes;
	private static Map<String, Texture> textures;
	private static Map<String, Shader> shaders;
	private static Map<String, Material> materials;
	private static Map<String, Texture> audio;
	
	private static Set<NativeObject> nativeObjects;
	
	private AssetManager()
	{
		nativeObjects = new HashSet<NativeObject>();
		
		meshes = new HashMap<String, Mesh>();
		textures = new HashMap<String, Texture>();
		shaders = new HashMap<String, Shader>();
		materials = new HashMap<String, Material>();
		audio = new HashMap<String, Texture>();
	}
	
	public static List<String> getMeshList()
	{
		return new ArrayList<String>(meshes.keySet());
	}
	
	public static List<String> getShaderList()
	{
		return new ArrayList<String>(shaders.keySet());
	}
	
	public static List<String> getTextureList()
	{
		return new ArrayList<String>(textures.keySet());
	}
	
	public static void loadAll()
	{
		// This is where the resource manager loads everything we need!
		loadDir(meshDir);
		loadDir(textureDir);
		loadDir(shaderDir);
		loadDir(audioDir);
		loadDir(materialDir);
	}	
	
	public static void loadFile(File f) throws IOException
	{
		String[] name = f.getName().split("\\.");
		if(name.length <= 1)
			return;
		String ext = name[name.length - 1];
		String fname = name[0];
		if(isFileType(ext, meshExtensions))
		{
			Mesh m = Mesh.loadFromFile(f);
			meshes.put(fname, m);
			m.create();
			System.out.println("Mesh '" + fname + "' loaded: " + m.toString());
		}
		else if(isFileType(ext, textureExtensions))
		{
			Texture t = Texture.loadFromFile(f);
			t.create();
			textures.put(fname, t);
			System.out.println("Texture '" + fname + "' loaded: " + t.toString());
		}
		else if(isFileType(ext, shaderExtensions))
		{
			Shader s = Shader.loadFromFile(f);
			s.create();
			shaders.put(fname, s);
			System.out.println("Shader '" + fname + "' loaded: " + s.toString());
		}
		else if(isFileType(ext, materialExtensions))
		{
			Material m = Material.loadFromFile(f);
			m.create();
			materials.put(fname, m);
			System.out.println("Material '" + fname + "' loaded: " + m.toString());
		}
		else
			System.out.println("Unknown filetype");
			// Generic file
	}
	
	public static boolean isFileType(String ext, String[] types)
	{
		for(String type : types)
		{
			if(ext.equalsIgnoreCase(type))
				return true;
		}
		return false;
	}
	
	public static void loadDir(String dir)
	{
		File fdir = new File(dir);
		if(!fdir.exists())
		{
			fdir.mkdirs();
			return;
		}
		File[] files = fdir.listFiles();
		for(File f : files)
		{
			try
			{
				loadFile(f);
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Error loading asset '" + f.getName() + "'. " + e.getClass());
			} 
		}
	}
	
	public static Mesh getMesh(String s)
	{
		return meshes.get(s);
	}

	public static Texture getTexture(String s)
	{
		return textures.get(s);
	}
	public static Shader getShader(String s)
	{
		return shaders.get(s);
	}

	public static Material getMaterial(String string)
	{
		return materials.get(string);
	}
	
	public static void addNativeObject(NativeObject no)
	{
		nativeObjects.add(no);
	}
}
