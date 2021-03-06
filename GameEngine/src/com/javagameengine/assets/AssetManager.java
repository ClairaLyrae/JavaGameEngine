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
import com.javagameengine.assets.skybox.Skybox;
import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.gui.GUI;
import com.javagameengine.scene.Scene;

/**
 * AssetManager is a statically accessed class which maintains access to all game related objects.
 * The manager loads all the assets located in the main game directory on runtime, and allows for objects
 * to be added or removed during runtime. 
 */
public class AssetManager
{
	public static final AssetManager handle = new AssetManager();
	
	public static final String[] meshExtensions = {"obj"};
	public static final String[] shaderExtensions = {"vert", "frag", "geom", "geo", "vs", "fs", "gs", "glsl"};
	public static final String[] textureExtensions = {"bmp", "jpg", "jpeg", "gif", "png"};
	public static final String[] materialExtensions = {"mtl"};
	public static final String[] audioExtensions = {"wav"};
	public static final String[] skyboxExtensions = {"sky"};
	
	public static final String dir = "assets";
	public static final String meshDir = "assets/meshes";
	public static final String shaderDir = "assets/shaders";
	public static final String textureDir = "assets/textures";
	public static final String materialDir = "assets/materials";
	public static final String audioDir = "assets/audio";
	public static final String skyboxDir = "assets/skyboxes";
	
	private static Map<String, Mesh> meshes;
	private static Map<String, Texture> textures;
	private static Map<String, Shader> shaders;
	private static Map<String, Material> materials;
	private static Map<String, SoundBuffer> sounds;
	private static Map<String, Scene> scenes;
	private static Map<String, GUI> guis;
	private static Map<String, Skybox> skyboxes;
	
	private AssetManager()
	{
		skyboxes = new HashMap<String, Skybox>();
		meshes = new HashMap<String, Mesh>();
		textures = new HashMap<String, Texture>();
		shaders = new HashMap<String, Shader>();
		materials = new HashMap<String, Material>();
		sounds = new HashMap<String, SoundBuffer>();
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
	 * @return List of the names of all loaded Skyboxes
	 */
	public static List<String> getSkyboxList()
	{
		return new ArrayList<String>(skyboxes.keySet());
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

	/**
	 * @return List of the names of all loaded Textures
	 */
	public static List<String> getAudioList()
	{
		return new ArrayList<String>(sounds.keySet());
	}
	
	/**
	 * Load all hardcoded directories into the AssetManager
	 */
	public static void loadAll()
	{
		// This is where the resource manager loads everything we need!
		loadDir(meshDir);
		loadDir(textureDir);
		loadDir(shaderDir);
		loadDir(audioDir);
		loadDir(materialDir);
		loadDir(skyboxDir);
	}	
	
	/**
	 * Loads the given file into the AssetManager if the file is of a valid type. 
	 * @param f File to load
	 * @throws IOException 
	 */
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
		else if(isFileType(ext, audioExtensions))
		{
			SoundBuffer s = SoundBuffer.loadFromFile(f);
			sounds.put(fname, s);
			s.create();
			System.out.println("Sound '" + fname + "' loaded: " + s.toString());
			//materials.put(fname, m);
			//System.out.println("Material '" + fname + "' loaded: " + m.toString());
		}
		else if(isFileType(ext, skyboxExtensions))
		{
			Skybox s = Skybox.loadFromFile(f);
			skyboxes.put(fname, s);
			System.out.println("Skybox '" + fname + "' loaded: " + s.toString());
		}
		else
			System.out.println("Unknown filetype");
			// Generic file
	}
	
	/**
	 * @param ext File extension of file to test
	 * @param types File extensions to test for
	 * @return True if the given extension is contained in the given array
	 */
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
	 * Loads all files in a given directory
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
		else if(o instanceof SoundBuffer)
			addSound(name, (SoundBuffer)o);
		else
			return false;
		return true;
	}
	
	public static void addSound(String name, SoundBuffer s)
	{
		if(sounds.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Mesh '" + name + "' already exists.");
		sounds.put(name, s);
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
	
	public static void addSkybox(String name, Skybox s)
	{
		if(skyboxes.containsKey(name))
			throw new IllegalStateException("Cannot add to asset pool. Skybox '" + name + "' already exists.");
		skyboxes.put(name, s);
	}
	
	public static void addScene(Scene s)
	{
		if(scenes.containsKey(s.getName()))
			throw new IllegalStateException("Cannot add to asset pool. Scene '" + s.getName() + "' already exists.");
		scenes.put(s.getName(), s);
	}

	public static SoundBuffer removeSound(String s)
	{
		return sounds.remove(s);
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
	
	public static Skybox removeSkybox(String string)
	{
		return skyboxes.remove(string);
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
	
	public static SoundBuffer getSound(String string)
	{
		return sounds.get(string);
	}
	
	public static Skybox getSkybox(String string)
	{
		return skyboxes.get(string);
	}
	
	public static Texture createCubeMap(String name)
	{
		Texture tex_top = getTexture(name + "_top");
		Texture tex_bottom = getTexture(name + "_bottom");
		Texture tex_left = getTexture(name + "_left");
		Texture tex_right = getTexture(name + "_right");
		Texture tex_front = getTexture(name + "_front");
		Texture tex_back = getTexture(name + "_back");
		if(tex_front == null || tex_left == null || tex_top == null || tex_bottom == null || tex_back == null || tex_right == null)
			return null;
		Texture cubemap = new Texture(tex_top, tex_bottom, tex_left, tex_right, tex_front, tex_back);
		cubemap.create();
		textures.put(name, cubemap);
		return cubemap;
	}
}
