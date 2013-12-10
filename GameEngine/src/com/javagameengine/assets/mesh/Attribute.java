package com.javagameengine.assets.mesh;

/**
 * Attribute is an enum which determines the different types of attribute buffers that can be tied to a mesh
 * and are automatically bound to active program objects. 
 */
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
    
    /**
     * @return AttributeFormat describing the type of data this attribute is stored in
     */
    public AttributeFormat getDataFormat()
    {
    	return format;
    }
    
    /**
     * @return Name of the attribute as it should appear inside the vertex shader
     */
    public String getAttribName()
    {
    	return name;
    }
    
    /**
     * @return Number of values of type given by getDataFormat this attribute requires per element
     */
    public int getComponentSize()
    {
        return componentSize;
    }
}