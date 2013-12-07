package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import com.javagameengine.assets.NativeObject;
import com.javagameengine.math.FastMath;

public class AttributeBuffer<T extends Buffer> extends NativeObject
{
	// Describes attribute
    protected T data = null;	// NIO buffer holding the data
    private boolean isEnabled = true;
    private boolean isIndex = false;
    protected boolean normalized = false;	// Normalized data

    // Open GL Hints & Pointer Info
    protected int offset = 0;	// Offset of data from start of buffer
    protected int stride = 0;	// Number of bytes to skip between format data
    protected AttributeUsage usage;	// Usage of the data
    protected AttributeFormat format;

    public AttributeBuffer(AttributeUsage usage)
    {
    	super(AttributeBuffer.class);
        this.usage = usage;
        this.normalized = false;
    }
    
    public AttributeBuffer(AttributeUsage usage, T data)
    {
    	this(usage);
    	this.data = data;
    }
    
    public AttributeFormat getFormat()
    {
    	if(data == null)
    		return null;
    	if(data instanceof ShortBuffer)
    		format = AttributeFormat.SHORT_INT_UNSIGNED;
    	else if(data instanceof FloatBuffer)
    		format = AttributeFormat.FLOAT;
    	else if(data instanceof IntBuffer)
    		format = AttributeFormat.INT;
    	else if(data instanceof ByteBuffer)
    		format = AttributeFormat.BYTE;
    	else if(data instanceof DoubleBuffer)
    		format = AttributeFormat.DOUBLE;
    	else
    		format = AttributeFormat.INT;
    	return format;
    }
    
    public T getData()
    {
        return data;
    }

    public int getOffset() 
    {
        return offset;
    }

    public int getStride() 
    {
        return stride;
    }

    public AttributeUsage getUsage()
    {
        return usage;
    }

    public boolean isEnabled()
    {
    	return isEnabled;
    }
    
    public boolean isIndex()
    {
    	return isIndex;
    }

    public boolean isNormalized()
    {
        return normalized;
    }

    public void setData(T data)
    {
    	if(isLive())
    		throw new IllegalStateException("Cannot alter buffer data while data is bound to GPU");
    	this.data = data;
    }
    
    public void setEnabled(boolean b)
    {
    	isEnabled = b;
    }
    
    public void setIndexStatus(boolean b)
    {
    	if(b && !((data instanceof ShortBuffer) || (data instanceof IntBuffer)))
    		throw new IllegalStateException("Index buffers must be short or int buffers.");
    	isIndex = b;
    }

    public void setNormalized(boolean normalized)
    {
        this.normalized = normalized;
    }

    public void setOffset(int offset) 
    {
        this.offset = offset;
    }

    public void setStride(int stride)
    {
        this.stride = stride;
    }
    
    public void setUsage(AttributeUsage usage)
    {
        this.usage = usage;
    }
    
	public int size()
    {
    	if(data == null)
    		return 0;
        return data.limit();
    }
    
    @Override
	public boolean create()
	{
    	if(isLive())
    		throw new IllegalStateException("Buffer is already bound to GPU");
		int glBindingPoint = GL_ARRAY_BUFFER;
		if(isIndex())
			glBindingPoint = GL15.GL_ELEMENT_ARRAY_BUFFER;
		id = glGenBuffers();
		glBindBuffer(glBindingPoint, id);
		if(data instanceof FloatBuffer)
			glBufferData(glBindingPoint, (FloatBuffer)data, usage.getGLEnum());
		else if(data instanceof ShortBuffer)
			glBufferData(glBindingPoint, (ShortBuffer)data, usage.getGLEnum());
		else if(data instanceof IntBuffer)
			glBufferData(glBindingPoint, (IntBuffer)data, usage.getGLEnum());
		else if(data instanceof DoubleBuffer)
			glBufferData(glBindingPoint, (DoubleBuffer)data, usage.getGLEnum());
		else if(data instanceof ByteBuffer)
			glBufferData(glBindingPoint, (ByteBuffer)data, usage.getGLEnum());
		glBindBuffer(glBindingPoint, 0);
		return true;
	}
    
    @Override
    public void destroy() 
    {
    	if(isLive())
    		glDeleteBuffers(id);
    }
	
	public String toString()
	{
		String s = "attrib[usage=" + usage.name() +
				", size=" + size() +
				", id=" + getID() + 
				"]";
		return s;
	}
}