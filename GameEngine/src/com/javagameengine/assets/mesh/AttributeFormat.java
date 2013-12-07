package com.javagameengine.assets.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

// Format of the data that is stored in the buffer.
public enum AttributeFormat 
{
    HALF_FLOAT(2, GL30.GL_HALF_FLOAT),
    FLOAT(4, GL11.GL_FLOAT),
    DOUBLE(8, GL11.GL_DOUBLE),
    BYTE(1, GL11.GL_BYTE),
    UNSIGNED_BYTE(1, GL11.GL_UNSIGNED_BYTE),
    SHORT_INT(2, GL11.GL_SHORT),
    SHORT_INT_UNSIGNED(2, GL11.GL_UNSIGNED_SHORT),
    INT(4, GL11.GL_INT),
    INT_UNSIGNED(4, GL11.GL_UNSIGNED_INT);

    private int gl;
    private int byteSize = 0;

    AttributeFormat(int byteSize, int gl)
    {
    	this.gl = gl;
        this.byteSize = byteSize;
    }
    
    public int getByteSize()
    {
        return byteSize;
    }
    
	public int getGLEnum()
	{
		return gl;
	}
}