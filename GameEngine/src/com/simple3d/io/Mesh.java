package com.simple3d.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Mesh
{
	private float[][] vertex = new float[0][3];
	private float[][] normal = new float[0][3];
	private float[][] face = new float[0][3];
	
	private int vertexCount = 0;
	private int faceCount = 0;
	
	public float[][] getVertices()
	{
		return vertex;
		
	}
	
	public float[] getVertex(int i)
	{
		return vertex[i];
	}
	
	public float[][] getNormals()
	{
		return normal;
	}
	
	public float[] getNormal(int i)
	{
		return normal[i];
	}
	
	public int getVertexCount()
	{
		return vertexCount;
	}

	public int getFaceCount()
	{
		return faceCount;
	}
	
	public void loadOBJFile(File f) throws FileNotFoundException, IOException, InvalidMeshException
	{
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line;
		while((line = r.readLine()) != null)
		{
			
		}
	}
}
