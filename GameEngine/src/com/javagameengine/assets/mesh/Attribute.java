package com.javagameengine.assets.mesh;


// Type of data in this buffer
public enum Attribute 
{
    POSITION(3, "in_position", AttributeFormat.FLOAT),	// 3 floats
    SIZE(1, "in_point_size", AttributeFormat.FLOAT),	// 1 float (points)
    NORMAL(3, "in_normal", AttributeFormat.FLOAT),	// normal vector (normalized) 3 floats
    TANGENT(4, "in_tangent", AttributeFormat.FLOAT),	// tangent vector (normalized) 3 floats
    COLOR(4, "in_color", AttributeFormat.FLOAT),	// 4 floats (r, g, b, a)
    TEXCOORDS(2, "in_texcoord", AttributeFormat.FLOAT); // 2 floats. Hardware supports multiple, if needed

    private int componentSize = 0;
    private String name;
    private AttributeFormat format;

    Attribute(int componentSize, String name, AttributeFormat f)
    {
    	this.format = f;
    	this.name = name;
        this.componentSize = componentSize;
    }
    
    public AttributeFormat getDataFormat()
    {
    	return format;
    }
    
    public String getAttribName()
    {
    	return name;
    }
    
    public int getComponentSize()
    {
        return componentSize;
    }
}