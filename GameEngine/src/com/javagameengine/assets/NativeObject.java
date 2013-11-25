package com.javagameengine.assets;

import java.util.HashSet;
import java.util.Set;

/**
 * NativeObject describes an object that is loaded into the native GL or AL. These objects are
 * kept in GL/AL memory until they are explicitly destroyed.
 */
public abstract class NativeObject
{
	public static Set<NativeObject> handles = new HashSet<NativeObject>();
	
    protected int id = -1;

    protected final Class<?> type;
    
    public static void destroyAll()
    {
    	
    }
    
    public static void updateAll()
    {
    	
    }
    
    public NativeObject(Class<?> type)
    {
        this.type = type;
        handles.add(this);
    }
    
    public int getId()
    {
        return id;
    }

    public abstract boolean create();
    
    public abstract void destroy();
}