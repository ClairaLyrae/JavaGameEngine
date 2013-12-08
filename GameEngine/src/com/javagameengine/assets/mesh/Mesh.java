package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;

import com.javagameengine.assets.NativeObject;
import com.javagameengine.math.Vector2f;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.scene.Bounds;

// TODO What is already here is not really anything. Before making this class, we have to figure out
// how openGL deals with mesh data efficiently and construct the class based around that!

/**
 * Stores the information describing a mesh and provides methods for manipulating the mesh or specifying openGL
 * rendering parameters.
 * @author ClairaLyrae
 */
public class Mesh extends NativeObject implements Drawable
{
	private Bounds bounds = Bounds.getVoid();
    private AttributeBuffer<?>[] buffers = new AttributeBuffer<?>[Attribute.values().length];
    private AttributeBuffer<?> indexes = null;
    private Mode mode;

	public Mesh()
	{
		this(Mode.TRIANGLE);
	}
	
	public Mesh(Mode mode)
	{
		super(Mesh.class);
		this.mode = mode;
	}    

	public void setIndexBuffer(AttributeBuffer<?> sb)
	{
		if(!sb.isIndex())
			throw new IllegalStateException("Given attribute buffer is not a valid index buffer");
		indexes = sb;
	}
	
	public AttributeBuffer<?> getIndexBuffer()
	{
		return indexes;
	}
	
    public void updateBounds()
    {
    	// TODO recalculate mesh bounding box
    }
    
    public Bounds getBounds()
    {
    	return bounds;
    }
    
    public Mode getMode()
    {
    	return mode;
    }
    
    public void setMode(Mode mode) 
    {
        this.mode = mode;
    }

    public void setBuffer(Attribute type, AttributeBuffer<?> vb)
    {
        buffers[type.ordinal()] = vb;
    }
    
    public AttributeBuffer<?> clearBuffer(Attribute type)
    {
        AttributeBuffer<?> vb = buffers[type.ordinal()];
        buffers[type.ordinal()] = null;
        return vb;
    }
    
    public void setIndexBuffer(AttributeUsage u, Buffer data)
    {
    	if(data instanceof ShortBuffer)
    		indexes = new AttributeBuffer<ShortBuffer>(u, (ShortBuffer)data);
    	else if(data instanceof IntBuffer)
    		indexes = new AttributeBuffer<IntBuffer>(u, (IntBuffer)data);
    	else
    		throw new IllegalStateException("Index buffer must be short or int.");
    	indexes.setIndexStatus(true);
    }
    
    public void setBuffer(Attribute type, AttributeUsage u, Buffer data)
    {
    	if(data instanceof ShortBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<ShortBuffer>(u, (ShortBuffer)data);
    	else if(data instanceof IntBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<IntBuffer>(u, (IntBuffer)data);
    	else if(data instanceof FloatBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<FloatBuffer>(u, (FloatBuffer)data);
    	else if(data instanceof DoubleBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<DoubleBuffer>(u, (DoubleBuffer)data);
    	else if(data instanceof LongBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<LongBuffer>(u, (LongBuffer)data);
    	else if(data instanceof ByteBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<ByteBuffer>(u, (ByteBuffer)data);
    	else if(data instanceof CharBuffer)
    		buffers[type.ordinal()] = new AttributeBuffer<CharBuffer>(u, (CharBuffer)data);
    }

    public AttributeBuffer<?> getBuffer(Attribute type)
    {
        return buffers[type.ordinal()];
    }
    
    public boolean hasBuffer(Attribute type)
    {
    	return buffers[type.ordinal()] != null;
    }
    
    public static Mesh loadFromFile(File f) throws NumberFormatException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        
        List<Vector3f> vertexList = new ArrayList<Vector3f>();
        List<Vector3f> normalList = new ArrayList<Vector3f>();
        List<Vector2f> texcoordList = new ArrayList<Vector2f>();

        List<String> vertexGroup = new ArrayList<String>();
        
        String line;
        while ((line = reader.readLine()) != null) 
        {
            String prefix = line.split(" ")[0];
            if (prefix.equals("v")) 
            {
                String[] xyz = line.split(" ");
            	Vector3f r = new Vector3f(Float.valueOf(xyz[1]), Float.valueOf(xyz[2]), Float.valueOf(xyz[3]));
                vertexList.add(r);
            } 
            else if (prefix.equals("vn")) 
            {
                String[] xyz = line.split(" ");
            	Vector3f r = new Vector3f(Float.valueOf(xyz[1]), Float.valueOf(xyz[2]), Float.valueOf(xyz[3]));
                normalList.add(r);
            } 
            else if (prefix.equals("vt")) 
            {
                String[] xy = line.split(" ");
            	Vector2f r = new Vector2f(Float.valueOf(xy[1]), Float.valueOf(xy[2]));
                texcoordList.add(r);
            } 
            else if (prefix.equals("f")) 
            {
            	String[] faceverts = line.split(" ");
                for(int i = 1; i < faceverts.length; i++)
                	vertexGroup.add(faceverts[i]);
            }
        }

        short indexCount = 0;
        LinkedHashMap<String, Short> vertexGroupMap = new LinkedHashMap<String, Short>();
        
        for(String s : vertexGroup)
        {
        	Short ind = vertexGroupMap.get(s);
        	if(ind == null)
        		vertexGroupMap.put(s, indexCount++);
        }

        int componentSize = vertexGroupMap.size();
        int indexSize = vertexGroup.size();
        Vector3f[] vertexArray = new Vector3f[componentSize];
        Vector3f[] normalArray = new Vector3f[componentSize];
        Vector2f[] texcoordArray = new Vector2f[componentSize];
        short[] indexArray = new short[indexSize];
        
        int i = 0;
        for(String s : vertexGroup)
        {
        	Short sh = vertexGroupMap.get(s);
        	String[] split = s.split("/");
        	Vector3f vert = vertexList.get(Integer.parseInt(split[0])-1);
        	Vector2f texcoord = texcoordList.get(Integer.parseInt(split[1])-1);
        	Vector3f norm = normalList.get(Integer.parseInt(split[2])-1);
        	vertexArray[sh] = vert;
        	normalArray[sh] = norm;
        	texcoordArray[sh] = texcoord;
        	indexArray[i] = sh;
        	i++;
        }
        
     
        // Now we need to fill the tangent list
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length * 3);
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normalArray.length * 3);
        FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(texcoordArray.length * 2);
        ShortBuffer indexBuffer = BufferUtils.createShortBuffer(indexArray.length);
        
        for(Vector3f v : vertexArray)
        	vertexBuffer.put(v.x).put(v.y).put(v.z);
        for(Vector3f v : normalArray)
        	normalBuffer.put(v.x).put(v.y).put(v.z);
        for(Vector2f v : texcoordArray)
        	texcoordBuffer.put(v.x).put(v.y);
        for(Short s : indexArray)
        	indexBuffer.put(s);
        
        vertexBuffer.flip();
        normalBuffer.flip();
        texcoordBuffer.flip();
        indexBuffer.flip();

        Mesh m = new Mesh();
        m.setBuffer(Attribute.POSITION, AttributeUsage.DYNAMIC, vertexBuffer);
        m.setBuffer(Attribute.NORMAL, AttributeUsage.DYNAMIC, normalBuffer);
        m.setBuffer(Attribute.TEXCOORDS, AttributeUsage.DYNAMIC, texcoordBuffer);
        m.setIndexBuffer(AttributeUsage.DYNAMIC, indexBuffer);
        m.calculateTangents();
        reader.close();
        return m;
    }
    
    public void calculateTangents()
    {
    	int uniqueVertCount = size();
    	int dupeVertCount;
    	
    	if(!hasBuffer(Attribute.POSITION) || !hasBuffer(Attribute.TEXCOORDS) || !hasBuffer(Attribute.NORMAL) || uniqueVertCount <= 0)
    		throw new IllegalStateException("Mesh must have vertex position, normal, index and texture coordinate attributes to calculate tangents.");
    	
    	Vector3f[] vertices = Vector3f.getArrayFromBuffer((FloatBuffer) getBuffer(Attribute.POSITION).getData());
    	Vector3f[] normals = Vector3f.getArrayFromBuffer((FloatBuffer) getBuffer(Attribute.NORMAL).getData());
    	Vector2f[] texcoords = Vector2f.getArrayFromBuffer((FloatBuffer)getBuffer(Attribute.TEXCOORDS).getData());    	
    	ShortBuffer indexes = (ShortBuffer)getIndexBuffer().getData();
    	dupeVertCount = vertices.length;
    	
	    Vector3f[] tan1 = new Vector3f[dupeVertCount];
	    Vector3f[] tan2 = new Vector3f[dupeVertCount];
	    Vector4f[] tangent = new Vector4f[dupeVertCount];
	    
	    for(int i = 0; i < dupeVertCount; i++)
	    {
	    	tan1[i] = new Vector3f(0);
	    	tan2[i] = new Vector3f(0);
	    }
	    for (short i = 0; i < uniqueVertCount; i += 3)
	    {
	        short i1 = indexes.get();
	        short i2 = indexes.get();
	        short i3 = indexes.get();
	        
	        Vector3f v1 = vertices[i1];
	        Vector3f v2 = vertices[i2];
	        Vector3f v3 = vertices[i3];
	        
	        Vector2f w1 = texcoords[i1];
	        Vector2f w2 = texcoords[i2];
	        Vector2f w3 = texcoords[i3];
	        
	        float x1 = v2.x - v1.x;
	        float x2 = v3.x - v1.x;
	        float y1 = v2.y - v1.y;
	        float y2 = v3.y - v1.y;
	        float z1 = v2.z - v1.z;
	        float z2 = v3.z - v1.z;
	        
	        float s1 = w2.x - w1.x;
	        float s2 = w3.x - w1.x;
	        float t1 = w2.y - w1.y;
	        float t2 = w3.y - w1.y;
	        
	        float r = 1.0f / (s1 * t2 - s2 * t1);
	        Vector3f sdir = new Vector3f((t2 * x1 - t1 * x2) * r, (t2 * y1 - t1 * y2) * r,
	                (t2 * z1 - t1 * z2) * r).normalize();
	        Vector3f tdir = new Vector3f((s1 * x2 - s2 * x1) * r, (s1 * y2 - s2 * y1) * r,
	                (s1 * z2 - s2 * z1) * r).normalize();
	        
	        tan1[i1].add(sdir);
	        tan1[i2].add(sdir);
	        tan1[i3].add(sdir);
	        
	        tan2[i1].add(tdir);
	        tan2[i2].add(tdir);
	        tan2[i3].add(tdir);
	    }
	    indexes.rewind();
	    for(int i = 0; i < dupeVertCount; i++)
	    {
	        Vector3f n = normals[i];
	        Vector3f t = tan1[i];
	        
	        // Gram-Schmidt orthogonalize
	        Vector3f tan = t.subtractInto(n.scaleInto(n.dot(t), null), null).normalize();
	        
	        tangent[i] = new Vector4f((n.crossInto(t, null).dot(tan2[i]) < 0.0F) ? 1.0F : 1.0F,
	        		tan.x, 
	        		tan.y, 
	        		tan.z);
	    }
	    FloatBuffer fb = Vector4f.getBufferFromArray(tangent);
	    setBuffer(Attribute.TANGENT, AttributeUsage.DYNAMIC, fb);
    }
    
    public int size()
    {
    	if(indexes == null)
    		return 0;
    	return indexes.size();
    }
    
    public void draw()
    {
		glShadeModel(GL_SMOOTH);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glBindVertexArray(id);
		GL11.glDrawElements(mode.getGLParam(), size(), indexes.getFormat().getGLEnum(), indexes.getOffset());	
		glBindVertexArray(0);
    }
    
	@Override
	public boolean create()
	{
		if(size() <= 0)
			throw new IllegalStateException("Mesh has no valid indexes.");
		id = glGenVertexArrays();
		glBindVertexArray(id);
		
		for(int i = 0; i < Attribute.values().length; i++)
		{
			AttributeBuffer<?> vb = buffers[i];
			if(vb == null)
				continue;
			if(!vb.isLive())
				vb.create();
			Attribute type = Attribute.values()[i];
		    glBindBuffer(GL15.GL_ARRAY_BUFFER, vb.getID());
		    glEnableVertexAttribArray(type.ordinal());
		    glVertexAttribPointer(type.ordinal(), 
		    		type.getComponentSize(), 
		    		type.getDataFormat().getGLEnum(), 
		    		vb.isNormalized(), 
		    		vb.getStride(), 
		    		vb.getOffset()); 
		}
		
		if(!indexes.isLive())
			indexes.create();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexes.getID());
		
		glBindVertexArray(0);
		return true;
	}

	@Override
	public void destroy()
	{
		glDeleteVertexArrays(id);
		for(AttributeBuffer<?> vb : buffers)
		{
			if(vb == null || vb.getID() != -1)
				continue;
			vb.destroy();
		}
		if(indexes != null)
			indexes.destroy();
	}
	

	public enum Mode 
	{
		QUAD(GL_QUADS, 4),
		TRIANGLE(GL_TRIANGLES, 3),
		TRIANGLE_STRIP(GL_TRIANGLE_STRIP, 3),
		TRIANGLE_FAN(GL_TRIANGLE_FAN, 3),
		LINE(GL_LINES, 2),
		LINE_STRIP(GL_LINE_STRIP, 2),
		LINE_LOOP(GL_LINE_LOOP, 2),
		POINT(GL_POINTS, 1);
        
		private int gl;
		private int vertices;
		
		private Mode(int gl, int v)
		{
			this.gl = gl;
			this.vertices = v;
		}
		
		public int getNumVertices()
		{
			return vertices;
		}
		
		public int getGLParam()
		{
			return gl;
		}
	}
	
	public String toString()
	{
		String s = "mesh[mode=" + mode.name() +
				", verts=" + size() +
				", id=" + getID();
		for(Attribute a : Attribute.values())
			s += ", " + a.name() + "=[" + getBuffer(a) + "]";
		s += "]";
		return s;
	}
}





