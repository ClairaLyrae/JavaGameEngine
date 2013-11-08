package com.simple3d.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MeshUtil
{
	private static String[] supportedFormats = {"obj"};
	
	public static Mesh load(File f) throws FileNotFoundException, IOException, InvalidMeshException
	{
		// TODO determine method to use based on file extension
		return null;
	}
	
	private static Mesh loadOBJFile(File f) throws FileNotFoundException, IOException, InvalidMeshException
	{
		// TODO write method to load mesh data from a .obj file into Mesh class
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line;
		while((line = r.readLine()) != null)
		{
			
		}
		return null;
	}
}
