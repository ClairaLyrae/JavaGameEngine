package com.javagameengine.io;

import static org.lwjgl.opengl.GL11.GL_POINTS;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

// TODO What is already here is not really anything. Before making this class, we have to figure out
// how openGL deals with mesh data efficiently and construct the class based around that!

/**
 * Stores the information describing a mesh and provides methods for manipulating the mesh or specifying openGL
 * rendering parameters.
 * @author ClairaLyrae
 */
public class Mesh
{
	protected int meshMode = GL_POINTS;
	
	protected float[] vertices;
	protected float[] normals;
	protected float[] faces;
	
	protected int vertexCount = 0;
	protected int faceCount = 0;
	
	public Mesh()
	{
		vertices = new float[0];
		normals = new float[0];
		faces = new float[0];
	}
	
	public float[] getVertices()
	{
		return vertices;
		
	}
	
	public float getVertex(int i)
	{
		return vertices[i];
	}
	
	public float[] getNormals()
	{
		return normals;
	}
	
	public float getNormal(int i)
	{
		return normals[i];
	}
	
	public int getVertexCount()
	{
		return vertexCount;
	}

	public int getFaceCount()
	{
		return faceCount;
	}
	
	public FloatBuffer getFloatBuffer()
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4); 
		vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
		FloatBuffer fb = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
		fb.put(vertices);    // add the coordinates to the FloatBuffer
		fb.position(0);      // set the buffer to read the first coordinate
		return fb;
	}
}
