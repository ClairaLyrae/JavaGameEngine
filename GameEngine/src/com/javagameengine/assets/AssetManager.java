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
import com.javagameengine.gui.GUI;
import com.javagameengine.scene.Scene;

/**
 * Loads all the assets located in the main game directory. Provides access to the assets.
 * @author ClairaLyrae
 */
public class AssetManager
{
	public static final AssetManager handle = new AssetManager();
	
	public static final String[] meshExtensions = {"obj"};
	public static final String[] shaderExtensions = {"vert", "frag", "geom", "geo", "vs", "fs", "gs", "glsl"};
	public static final String[] textureExtensions = {"bmp", "jpg", "jpeg", "gif", "png"};
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
	private static Map<String, Scene> scenes;
	private static Map<String, GUI> guis;
	
	private AssetManager()
	{
		meshes = new HashMap<String, Mesh>();
		textures = new HashMap<String, Texture>();
		shaders = new HashMap<String, Shader>();
		materials = new HashMap<String, Material>();
		audio = new HashMap<String, Texture>();
		scenes = new HashMap<String, Scene>();
		guis = new HashMap<String, GUI>();
	}
	
	/**
	 * @return List of the names of all loaded GUIs
	 */
	public static List<String> getGUIList()
	{
		return new ArrayList<String>(guis.keySet());
	}
	
	/**
	 * @return List of the names of all loaded Scenes
	 */
	public static List<String> getSceneList()
	{
		return new ArrayList<String>(scenes.keySet());
	}

	/**
	 * @return List of the names of all loaded Meshes
	 */
	public static List<String> getMeshList()
	{
		return new ArrayList<String>(meshes.keySet());
	}

	/**
	 * @return List of the names of all loaded Shaders
	 */
	public static List<String> getShaderList()
	{
		return new ArrayList<String>(shaders.keySet());
	}

	/**
	 * @return List of the names of all loaded Textures
	 */
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
	
	/**
	 * Loads all files in a given dire
	 * @param dir Directory to load from
	 */
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

	/**
	 * Loads the given object into the asset manager if the obect is of a valid type.
	 * @param name Name to give object
	 * @param o Object to load into manager
	 */
	public static boolean addObject(String name, Object o)
	{
		if(o instanceof Mesh)
			addMesh(name, (Mesh)o);
		else if(o instanceof Material)
			addMaterial(name, (Material)o);
		else if(o instanceof GUI)
			addGUI(name, (GUI)o);
		else if(o instanceof Scene)
			addScene((Scene)o);
		else if(o instanceof Texture)
			addTexture(name, (Texture)o);
		else if(o instanceof Shader)
			addShader(name, (Shader)o);
		else
			return false;
		return true;
	}
	
	public static void addMesh(String name, Mesh m)
	{
		if(meshes.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Mesh '" + name + "' already exists.");
		meshes.put(name, m);
	}

	public static void addTexture(String name, Texture t)
	{
		if(textures.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Texture '" + name + "' already exists.");
		textures.put(name, t);
	}
	
	public static void addShader(String name, Shader s)
	{
		if(shaders.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Shader '" + name + "' already exists.");
		shaders.put(name, s);
	}

	public static void addMaterial(String name, Material m)
	{
		if(materials.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Material '" + name + "' already exists.");
		materials.put(name, m);
	}
	
	public static void addGUI(String name, GUI gui)
	{
		if(guis.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. GUI '" + name + "' already exists.");
		guis.put(name, gui);
	}
	
	public static void addScene(Scene s)
	{
		if(scenes.containsKey(s.getName()))
			throw new IllegalStateException("Cannot add to asset pool. Scene '" + s.getName() + "' already exists.");
		scenes.put(s.getName(), s);
	}

	public static Mesh removeMesh(String s)
	{
		return meshes.remove(s);
	}

	public static Texture removeTexture(String s)
	{
		return textures.remove(s);
	}
	
	public static Shader removeShader(String s)
	{
		return shaders.remove(s);
	}

	public static Material removeMaterial(String string)
	{
		return materials.remove(string);
	}
	
	public static GUI removeGUI(String string)
	{
		return guis.remove(string);
	}
	
	public static Scene removeScene(String string)
	{
		return scenes.remove(string);
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
	
	public static GUI getGUI(String string)
	{
		return guis.get(string);
	}
	
	public static Scene getScene(String string)
	{
		return scenes.get(string);
	}
}
