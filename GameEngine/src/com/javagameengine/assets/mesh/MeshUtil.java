package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.javagameengine.math.Vector2f;
import com.javagameengine.math.Vector3f;

/**
 * Provides utility methods for manipulating meshes or loading meshes from files.
 * @author ClairaLyrae
 */
public class MeshUtil
{	
    /**
     * Generate a new FloatBuffer using the given array of floats
     * @param data Array of floats to create buffer around
     */
    public static FloatBuffer createFloatBuffer(float ... data) 
    {
        if (data == null) 
        	return null;
        FloatBuffer buff = BufferUtils.createFloatBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }
    
    /**
     * Generate a new IntBuffer using the given array of ints
     * @param data Array of ints to create buffer around
     */
    public static IntBuffer createIntBuffer(int ... data) 
    {
        if (data == null) 
        	return null;
        IntBuffer buff = BufferUtils.createIntBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }
    
    /**
     * Generate a new ShortBuffer using the given array of shorts
     * @param data Array of shorts to create buffer around
     */
    public static ShortBuffer createShortBuffer(short ... data) 
    {
        if (data == null) 
        	return null;
        ShortBuffer buff = BufferUtils.createShortBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }
    
    /**
     * Generate a new ByteBuffer using the given array of bytes
     * @param data Array of bytes to create buffer around
     */
    public static ByteBuffer createByteBuffer(byte ... data) 
    {
        if (data == null) 
        	return null;
        ByteBuffer buff = BufferUtils.createByteBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }
    
    public static Mesh getBox()
    {
		Mesh m = new Mesh(Mesh.Mode.QUAD);
		float[] vertex = { 
			-1.0f, -1.0f, -1.0f, 
			1.0f, -1.0f, -1.0f, 
			1.0f, 1.0f, -1.0f, 
			-1.0f, 1.0f, -1.0f, 
			-1.0f, -1.0f, 1.0f, 
			1.0f, -1.0f, 1.0f, 
			1.0f, 1.0f, 1.0f, 
			-1.0f, 1.0f, 1.0f 
		};
		m.setBuffer(GLBuffer.Type.POSITION, vertex);

		float[] normal = { 
				-1.0f, -1.0f, -1.0f, 
				1.0f, -1.0f, -1.0f, 
				1.0f, 1.0f, -1.0f, 
				-1.0f, 1.0f, -1.0f, 
				-1.0f, -1.0f, 1.0f, 
				1.0f, -1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f, 
				-1.0f, 1.0f, 1.0f 
		};
		m.setBuffer(GLBuffer.Type.NORMAL, normal);
		m.updateBounds();
		return m;
    }

    // Creates a GPU Vertex Buffer Object
    public static int[] createVBO(Mesh model) 
    {
        int vboVertexHandle = glGenBuffers();
        int vboNormalHandle = glGenBuffers();
        FloatBuffer vertices = model.getFloatBuffer(GLBuffer.Type.POSITION);
        FloatBuffer normals = model.getFloatBuffer(GLBuffer.Type.NORMAL);
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
        glNormalPointer(GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return new int[]{vboVertexHandle, vboNormalHandle};
    }
    

    private static float[] asFloats(Vector3f v) 
    {
        return new float[]{v.x, v.y, v.z};
    }

    private static Vector3f parseVertex(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Vector3f(x, y, z);
    }

    private static Vector2f parseTexCoords(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        return new Vector2f(x, y);
    }

    private static Vector3f parseNormal(String line) {
        String[] xyz = line.split(" ");
        float x = Float.valueOf(xyz[1]);
        float y = Float.valueOf(xyz[2]);
        float z = Float.valueOf(xyz[3]);
        return new Vector3f(x, y, z);
    }
    
    public static int parseIntSafe(String s)
    {
    	s = s.replaceAll("[^\\d]", "");
    	int i = 0;
    	try
    	{
        	i = Integer.valueOf(s);
    	} catch(NumberFormatException e) {
    		return -1;
    	}
    	return i;
    }
    
    
    public static Mesh loadModel(File f) throws IOException 
    {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Mesh m = new Mesh();
        List<Vector3f> verts = new ArrayList<Vector3f>();
        List<Vector3f> norms = new ArrayList<Vector3f>();
        List<Vector2f> texcoords = new ArrayList<Vector2f>();
        List<Integer> indexes = new ArrayList<Integer>();
        List<Integer> tindexes = new ArrayList<Integer>();
        List<Integer> nindexes = new ArrayList<Integer>();
        
        String line;
        while ((line = reader.readLine()) != null) 
        {
            String prefix = line.split(" ")[0];
            if (prefix.equals("v")) 
            {
            	Vector3f r = parseVertex(line);
                verts.add(r);
            } 
            else if (prefix.equals("vn")) 
            {
            	Vector3f r = parseNormal(line);
                norms.add(r);
            } 
            else if (prefix.equals("vt")) 
            {
            	Vector2f r = parseTexCoords(line);
                texcoords.add(r);
            } 
            else if (prefix.equals("f")) 
            {
                String[] faceIndices = line.split(" ");
                if(faceIndices.length == 5)
                	m.setMode(Mesh.Mode.QUAD);
                else if(faceIndices.length == 4)
                    m.setMode(Mesh.Mode.TRIANGLE);
                for(int i = 1; i < faceIndices.length; i++)
                {
                	String[] faceparts = faceIndices[i].split("/");
                	int vparse = parseIntSafe(faceparts[0])-1;
                	int tparse = parseIntSafe(faceparts[1])-1;
                	int nparse = parseIntSafe(faceparts[2])-1;
                	if(vparse >= 0)
                	{
                		indexes.add(vparse);	// vert
                		
                	}
                	if(tparse >= 0)
                	{
                		tindexes.add(tparse);	// texcoords
                		
                	}
                	if(nparse >= 0)
                	{
                    	nindexes.add(nparse);	// norm
                		
                	}
                }
            } 
            else
                continue;
        }
        
        FloatBuffer vertbuf = BufferUtils.createFloatBuffer(verts.size() * 3);
        FloatBuffer normbuf = BufferUtils.createFloatBuffer(norms.size() * 3);
        FloatBuffer texcoordbuf = BufferUtils.createFloatBuffer(texcoords.size() * 2);
        IntBuffer ibuf = BufferUtils.createIntBuffer(indexes.size());
        IntBuffer tbuf = BufferUtils.createIntBuffer(tindexes.size());
        IntBuffer nbuf = BufferUtils.createIntBuffer(nindexes.size());
        for(Vector3f v : verts)
        	vertbuf.put(v.x).put(v.y).put(v.z);
        for(Vector3f v : norms)
        	normbuf.put(v.x).put(v.y).put(v.z);
        for(Vector2f v : texcoords)
        	texcoordbuf.put(v.x).put(v.y);
        for(Integer i : indexes)
        	ibuf.put(i);
        for(Integer i : tindexes)
        	tbuf.put(i);
        for(Integer i : nindexes)
        	nbuf.put(i);

        vertbuf.flip();
        normbuf.flip();
        texcoordbuf.flip();
        ibuf.flip();
        tbuf.flip();
        nbuf.flip();
        m.setBuffer(GLBuffer.Type.POSITION, vertbuf);
        m.setBuffer(GLBuffer.Type.NORMAL, normbuf);
        if(!texcoords.isEmpty())
        	m.setBuffer(GLBuffer.Type.TEXCOORDS, texcoordbuf);
        
        System.out.println("Vertices=" + verts.size() + 
        		" TexCoords=" + texcoords.size() + 
        		" Normals=" + norms.size() + 
        		" Indices=" + indexes.size() + 
        		" TIndices=" + tindexes.size() + 
        		" NIndices=" + nindexes.size());
        
        reader.close();
        return m;
    }
}
