package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.javagameengine.math.Vector2f;
import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Bounds;

// TODO What is already here is not really anything. Before making this class, we have to figure out
// how openGL deals with mesh data efficiently and construct the class based around that!

/**
 * Stores the information describing a mesh and provides methods for manipulating the mesh or specifying openGL
 * rendering parameters.
 * @author ClairaLyrae
 */
public class Mesh extends NativeObject
{
	
    private int meshID = -1;
    
	public enum Mode {
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
	
	private Bounds bounds =  Bounds.getVoid();

    private VertexBuffer[] buffers = new VertexBuffer[VertexBuffer.Type.values().length];
    private FloatBuffer interleaved_data = null;
    private float pointSize = 1f;
    private float lineWidth = 1f;

    private int vertexCount = -1;
    private int elementCount = -1;

    private Mode mode;
    
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
	
	public Mesh()
	{
		this(Mode.TRIANGLE);
	}
	
	public Mesh(Mode mode)
	{
		super(Mesh.class);
		this.mode = mode;
	}

    /**
     * @return The size of points
     */
    public float getPointSize() {
        return pointSize;
    }

    /**
     * Set the size of points for point meshes. Point size is specified in pixels.
     * @param pointSize The size of points
     */
    public void setPointSize(float pointSize) 
    {
        this.pointSize = pointSize;
    }

    /**
     * @return The width of lines
     */
    public float getLineWidth() 
    {
        return lineWidth;
    }

    /**
     * Set the width of lines for line meshes. Line width is specified in pixels.
     * @param lineWidth The width of lines
     */
    public void setLineWidth(float lineWidth) 
    {
        this.lineWidth = lineWidth;
    }

    /**
     * @param bufferSize Size of vertex buffer
     * @return The number of elements of type mode in this mesh
     */
    private int calculateNumElements(int bufferSize)
    {
        switch (mode)
        {
            case TRIANGLE:
                return bufferSize / 3;
            case TRIANGLE_STRIP: case TRIANGLE_FAN:
                return bufferSize - 2;
            case LINE:
                return bufferSize / 2;
            case LINE_STRIP:
                return bufferSize - 1;
            case POINT: case LINE_LOOP: default:
                return bufferSize;
        }
    }

    /**
     * @return Number of elements on the mesh of type mode
     */
    public int getElementCount()
    {
        return elementCount;
    }

    /**
     * @return Number of vertices on the mesh
     */
    public int getVertexCount()
    {
        return vertexCount;
    }
    
    
    
    


    /**
     * Sets the buffer on this mesh to the buffer given. 
     * @param vb The buffer to set
     * @throws IllegalArgumentException If the buffer type is already set
     */
    public void setBuffer(VertexBuffer vb)
    {
        if (buffers[vb.getBufferType().ordinal()] != null)
            throw new IllegalArgumentException("Buffer type already set: "+vb.getBufferType());
        buffers[vb.getBufferType().ordinal()] = vb;
    }
    
    /**
     * Clears the buffer of the given type on this mesh
     * @param type The buffer type to clear
     */
    public void clearBuffer(VertexBuffer.Type type)
    {
        VertexBuffer vb = buffers[type.ordinal()];
        buffers[type.ordinal()] = null;
    }
    
    /**
     * Sets the buffer of the given type to the given data, or creates a new buffer with the given
     * parameters and data.
     * @param type The type of the buffer to set
     * @param format The format of the given buffer data
     * @param data The data to set the buffer to
     */
    public void setBuffer(VertexBuffer.Type type, VertexBuffer.Format format, Buffer data)
    {
        VertexBuffer vb = buffers[type.ordinal()];
        if (vb == null)
        {
            vb = new VertexBuffer(type);
            vb.setupData(VertexBuffer.Usage.DYNAMIC, type.getComponentSize(), format, data);
            setBuffer(vb);
        }
        else
        {
            vb.updateData(data);
        }
    }
    
    public void setBuffer(VertexBuffer.Type type, FloatBuffer buf) 
    {
        setBuffer(type, VertexBuffer.Format.FLOAT, buf);
    }

    public void setBuffer(VertexBuffer.Type type, float[] buf)
    {
        setBuffer(type, MeshUtil.createFloatBuffer(buf));
    }

    public void setBuffer(VertexBuffer.Type type, IntBuffer buf) 
    {
        setBuffer(type, VertexBuffer.Format.INT_UNSIGNED, buf);
    }

    public void setBuffer(VertexBuffer.Type type, int[] buf)
    {
        setBuffer(type, MeshUtil.createIntBuffer(buf));
    }

    public void setBuffer(VertexBuffer.Type type, ShortBuffer buf) 
    {
        setBuffer(type, VertexBuffer.Format.SHORT_INT_UNSIGNED, buf);
    }

    public void setBuffer(VertexBuffer.Type type, int components, byte[] buf)
    {
        setBuffer(type, MeshUtil.createByteBuffer(buf));
    }

    public void setBuffer(VertexBuffer.Type type, ByteBuffer buf) 
    {
        setBuffer(type, VertexBuffer.Format.UNSIGNED_BYTE, buf);
    }

    public void setBuffer(VertexBuffer.Type type, short[] buf)
    {
        setBuffer(type, MeshUtil.createShortBuffer(buf));
    }

    /**
     * Get the VertexBuffer of the given type for this mesh
     * @param type The type of VertexBuffer
     * @return The VertexBuffer (null if not set)
     */
    public VertexBuffer getBuffer(VertexBuffer.Type type)
    {
        return buffers[type.ordinal()];
    }
    
    public FloatBuffer getFloatBuffer(VertexBuffer.Type type) 
    {
        VertexBuffer vb = getBuffer(type);
        if (vb == null)
            return null;
        return (FloatBuffer) vb.getData();
    }
    
    public IntBuffer getIndexBuffer(VertexBuffer.Type t) 
    {
    	if(t != VertexBuffer.Type.NORMAL_INDEX && t != VertexBuffer.Type.POSITION_INDEX && t != VertexBuffer.Type.TEXCOORDS_INDEX)
            throw new UnsupportedOperationException("Given buffer type is not an integer buffer.");
        VertexBuffer vb = getBuffer(t);
        if (vb == null)
            return null;
        
        Buffer buf = vb.getData();
        return (IntBuffer)buf;
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
    
    public static Mesh loadFromFile(File f) throws NumberFormatException, IOException
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
                String[] xyz = line.split(" ");
            	Vector3f r = new Vector3f(Float.valueOf(xyz[1]), Float.valueOf(xyz[2]), Float.valueOf(xyz[3]));
                verts.add(r);
            } 
            else if (prefix.equals("vn")) 
            {
                String[] xyz = line.split(" ");
            	Vector3f r = new Vector3f(Float.valueOf(xyz[1]), Float.valueOf(xyz[2]), Float.valueOf(xyz[3]));
                norms.add(r);
            } 
            else if (prefix.equals("vt")) 
            {
                String[] xy = line.split(" ");
            	Vector2f r = new Vector2f(Float.valueOf(xy[1]), Float.valueOf(xy[2]));
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
                		indexes.add(vparse);	// vert
                	if(tparse >= 0)
                		tindexes.add(tparse);	// texcoords
                	if(nparse >= 0)
                    	nindexes.add(nparse);	// norm
                }
            } 
            else
                continue;
        }
        
        FloatBuffer vertbuf = BufferUtils.createFloatBuffer(indexes.size() * 3);
        FloatBuffer normbuf = BufferUtils.createFloatBuffer(nindexes.size() * 3);
        FloatBuffer tangentbuf = BufferUtils.createFloatBuffer(nindexes.size() * 3);
        FloatBuffer texcoordbuf = BufferUtils.createFloatBuffer(tindexes.size() * 2);
        
//        for(Integer i : indexes)
//        {
//        	Vector3f v = verts.get(i);
//        	vertbuf.put(v.x).put(v.y).put(v.z);
//        }
//        for(Integer i : tindexes)
//        {
//        	Vector2f v = texcoords.get(i);
//        	texcoordbuf.put(v.x).put(v.y);
//        }
//        for(Integer i : nindexes)
//        {
//        	Vector3f v = norms.get(i);
//        	normbuf.put(v.x).put(v.y).put(v.z);
//        	// TODO Calculate tangents
//        	//Vector3f tangent;
//        	//tangentbuf.put(tangent.x).put(tangent.y).put(tangent.z);
//        };
        int numVerts = m.getMode().getNumVertices();
        int numFloats = m.getMode().getNumVertices();
    	Vector3f[] tri_verts = new Vector3f[numVerts];
    	Vector3f[] tri_norms = new Vector3f[numVerts];
    	Vector2f[] tri_texcoords = new Vector2f[numVerts];
    	Vector3f[] tri_tan1 = new Vector3f[numVerts];
    	Vector3f[] tri_tan2 = new Vector3f[numVerts];
        
        int vertCounter = 0;
        for(int i = 0; i < indexes.size(); i++)
        {
        	int vert_i = indexes.get(i);
        	int texcoord_i = tindexes.get(i);
        	int norm_i = nindexes.get(i);

        	Vector3f vv = verts.get(vert_i);
        	vertbuf.put(vv.x).put(vv.y).put(vv.z);
        	tri_verts[vertCounter] = vv;
        	Vector2f vt = texcoords.get(texcoord_i);
        	texcoordbuf.put(vt.x).put(vt.y);
        	tri_texcoords[vertCounter] = vt;
        	Vector3f vn = norms.get(norm_i);
        	normbuf.put(vn.x).put(vn.y).put(vn.z);
        	tri_norms[vertCounter] = vn;
        	
        	vertCounter++;
        	if(vertCounter >= numVerts)
        	{
				vertCounter = 0;
            	// TODO Calculate tangents
            	// Now we have a quad/tri, and we need to loop through the stored verts and calculate each tangent
        	}
        }

        vertbuf.flip();
        normbuf.flip();
        tangentbuf.flip();
        texcoordbuf.flip();
        
        m.setBuffer(VertexBuffer.Type.POSITION, vertbuf);
        m.setBuffer(VertexBuffer.Type.NORMAL, normbuf);
        m.setBuffer(VertexBuffer.Type.TANGENT, normbuf);
        m.setBuffer(VertexBuffer.Type.TEXCOORDS, texcoordbuf);
        
        System.out.println("Vertices=" + verts.size() + 
        		" TexCoords=" + texcoords.size() + 
        		" Normals=" + norms.size() + 
        		" Indices=" + indexes.size() + 
        		" TIndices=" + tindexes.size() + 
        		" NIndices=" + nindexes.size());
        
        reader.close();
        return m;
    }

    public void draw()
    {
		VertexBuffer vbuf = getBuffer(VertexBuffer.Type.POSITION);
		VertexBuffer nbuf = getBuffer(VertexBuffer.Type.NORMAL);
		VertexBuffer tanbuf = getBuffer(VertexBuffer.Type.TANGENT);
		VertexBuffer tbuf = getBuffer(VertexBuffer.Type.TEXCOORDS);
		VertexBuffer cbuf = getBuffer(VertexBuffer.Type.COLOR);
	    if(vbuf != null){
		    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		    glBindBuffer(GL_ARRAY_BUFFER, vbuf.getId());
		    GL11.glVertexPointer(nbuf.getType().getComponentSize(), GL11.GL_FLOAT, 0, 0);
	    }
	    if(nbuf != null){
		    GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		    glBindBuffer(GL_ARRAY_BUFFER, nbuf.getId());
		    GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
	    }
	    if(tbuf != null){
		    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		    glBindBuffer(GL_ARRAY_BUFFER, tbuf.getId());
		    GL11.glTexCoordPointer(tbuf.getType().getComponentSize(), GL11.GL_FLOAT, 0, 0);
	    }
	    if(cbuf != null){
		    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		    glBindBuffer(GL_ARRAY_BUFFER, cbuf.getId());
		    GL11.glColorPointer(cbuf.getType().getComponentSize(), GL11.GL_FLOAT, 0, 0);
	    }
	    if(tanbuf != null){
		    //hrm
	    }
	    
	    //If you are not using IBOs:
	    GL11.glDrawArrays(mode.getGLParam(), 0, vbuf.getData().limit()/3);
	    
//	    //If you are using IBOs:
//	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
//	    GL11.glDrawElements(GL11.GL_TRIANGLES, numberIndices, GL11.GL_UNSIGNED_INT, 0);
//
//	    //The alternate glDrawElements.    
//	    glDrawRangeElements(GL11.GL_TRIANGLES, 0, maxIndex, numberIndices,
//						GL11.GL_UNSIGNED_INT, 0);
    }
    
	@Override
	public void create()
	{
		for(VertexBuffer vb : buffers)
		{
			if(vb == null || vb.getId() != -1)
				continue;
			vb.create();
		}
	}

	@Override
	public void destroy()
	{
		for(VertexBuffer vb : buffers)
		{
			if(vb == null || vb.getId() != -1)
				continue;
			vb.destroy();
		}
	}
}
