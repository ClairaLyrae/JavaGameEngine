package com.javagameengine.assets;

import java.util.HashSet;
import java.util.Set;

/**
 * NativeObject describes an object that can be loaded into the native system memory. It maintains an
 * ID which the native system identifies the local copy by, and provides methods to load and remove the
 * object from the native memory which are implemented by specific classes.
 */
public abstract class NativeObject
{
	/**
	 * References to all instantiated objects. Used for clearing the native memory on program exit.
	 */
	private static Set<NativeObject> handles = new HashSet<NativeObject>();
	
    /**
     * Calls the destroy() method on all instantiated objects to clean up all resources used
     * on the GPU. Use only on program exit to free loose native memory.
     */
    public static void destroyAll()
    {
    	for(NativeObject obj : handles)
    		obj.destroy();
    }
    
    protected int id = -1;
    
    protected final Class<?> type;
    
    public NativeObject(Class<?> type)
    {
        this.type = type;
        handles.add(this);
    }
    
    /**
     * Loads this object onto native memory and sets the ID to the native system ID
     * @return True if the object was created successfully
     */
    public abstract boolean create();
    
    /**
     * Flags this object for removal from native memory
     */
    public abstract void destroy();

    /**
     * Obtains the ID given to this object in the native environment
     * @return ID of this object or -1 if the object is not yet live
     */
    public int getID()
    {
        return id;
    }
    
    /**
     * @return True if this object is loaded onto native memory
     */
    public boolean isLive()
    {
    	return id != -1;
    }
}