package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL11.GL_POINTS;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import com.javagameengine.scene.Bounds;

// TODO What is already here is not really anything. Before making this class, we have to figure out
// how openGL deals with mesh data efficiently and construct the class based around that!

/**
 * Stores the information describing a mesh and provides methods for manipulating the mesh or specifying openGL
 * rendering parameters.
 * @author ClairaLyrae
 */
public class Mesh
{
	public enum Mode {
		QUAD,
		TRIANGLE,
		TRIANGLE_STRIP,
		TRIANGLE_FAN,
		LINE,
		LINE_STRIP,
		LINE_LOOP,
		POINT;
	}
	
	private Bounds bounds =  Bounds.getVoid();

    private VertexBuffer[] buffers = new VertexBuffer[VertexBuffer.Type.values().length];
    private float pointSize = 1f;
    private float lineWidth = 1f;

    private transient int vertexArrayID = -1;

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
        updateCounts();
    }
	
	public Mesh()
	{
		this(Mode.TRIANGLE);
	}
	
	public Mesh(Mode mode)
	{
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
     * Update the vertex and triangle counts for this mesh
     * based on the current data. This method should be called
     * after the capacities of the mesh's buffers has been altered.
     */
    public void updateCounts()
    {
        VertexBuffer pb = getBuffer(VertexBuffer.Type.POSITION);
        VertexBuffer ib = getBuffer(VertexBuffer.Type.POSITION_INDEX);
        if (pb != null){
            vertexCount = pb.getData().capacity() / pb.getNumComponents();
        }
        if (ib != null){
            elementCount = calculateNumElements(ib.getData().capacity());
        }else{
            elementCount = calculateNumElements(vertexCount);
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
        updateCounts();
    }
    
    /**
     * Clears the buffer of the given type on this mesh
     * @param type The buffer type to clear
     */
    public void clearBuffer(VertexBuffer.Type type)
    {
        VertexBuffer vb = buffers[type.ordinal()];
        buffers[type.ordinal()] = null;
        if (vb != null)
            updateCounts();
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
            if (vb.getNumComponents() != type.getComponentSize() || vb.getFormat() != format){
                throw new UnsupportedOperationException("The buffer already set "
                        + "is incompatible with the given parameters");
            }
            vb.updateData(data);
            updateCounts();
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

    public int getId()
    {
        return vertexArrayID;
    }

    public void setId(int id)
    {
        if (vertexArrayID != -1)
            throw new IllegalStateException("ID has already been set.");
        vertexArrayID = id;
    }

}
