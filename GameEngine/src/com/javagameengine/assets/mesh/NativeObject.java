package com.javagameengine.assets.mesh;

import com.javagameengine.assets.material.InvalidAssetException;

/**
 * NativeObject describes an object that is loaded into the native GL or AL. These objects are
 * kept in GL/AL memory until they are explicitely destroyed.
 */
public abstract class NativeObject
{
    protected int id = -1;

    /**
     * A reference to a "handle". By hard referencing a certain object, it's
     * possible to find when a certain GLObject is no longer used, and to delete
     * its instance from the graphics library.
     */
    protected Object handleRef = null;

    protected boolean updateNeeded = true;
    protected final Class<?> type;
    
    public NativeObject(Class<?> type)
    {
        this.type = type;
        this.handleRef = new Object();
    }
    
    public int getId()
    {
        return id;
    }

    /**
     * Internal use only. Indicates that the object has changed
     * and its state needs to be updated.
     */
    public void setUpdateNeeded()
    {
        updateNeeded = true;
    }

    /**
     * Internal use only. Indicates that the state changes were applied.
     */
    public void clearUpdateNeeded()
    {
        updateNeeded = false;
    }

    /**
     * Internal use only. Check if {@link #setUpdateNeeded()} was called before.
     */
    public boolean isUpdateNeeded()
    {
        return updateNeeded;
    }

    public abstract boolean create();
    
    public abstract void destroy();
}