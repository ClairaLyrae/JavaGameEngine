package com.javagameengine.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Provides utility methods for manipulating meshes or loading meshes from files.
 * @author ClairaLyrae
 */
public class MeshUtil
{
	private static String[] supportedFormats = {"obj"};
	
	/**
	 * Determines the correct method to load the file based on the file extension and calls it
	 * @param f File to load mesh from
	 * @return Mesh loaded from the given file
	 */
	public static Mesh load(File f) throws FileNotFoundException, IOException, InvalidMeshException
	{
		// TODO determine method to use based on file extension
		return null;
	}
	
	/**
	 * Attempts to load a model from an .OBJ file 
	 * @param f File to load mesh from
	 * @return Mesh loaded from the given file
	 */
	private static Mesh loadOBJFile(File f) throws FileNotFoundException, IOException, InvalidMeshException
	{
		// TODO write method to load mesh data from a .obj file into Mesh class
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line;
		while((line = r.readLine()) != null)
		{
			
		}
		r.close();
		return null;
	}
}
