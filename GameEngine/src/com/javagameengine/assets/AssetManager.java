package com.javagameengine.assets;

import java.util.HashMap;
import java.util.Map;

import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.texture.Texture;

/**
 * Loads all the assets located in the main game directory. Provides access to the assets.
 * @author ClairaLyrae
 */
public class AssetManager
{
	public static final String directory = "/resources";
	public static final AssetManager handle = new AssetManager();
	
	private static Map<String, Mesh> meshes = new HashMap<String, Mesh>();
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	// List of audio files
	// List of materials
	// List of shaders
	
	private AssetManager()
	{
		// This is where the resource manager loads everything we need!
	}
	
	// And here will be static methods to access the assets
}
