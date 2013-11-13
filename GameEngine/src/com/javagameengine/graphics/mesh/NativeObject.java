package com.javagameengine.graphics.mesh;

/**
 * Describes a native object. An encapsulation of a certain object 
 * on the native side of the graphics or audio library.
 * 
 * This class is used to track when OpenGL and OpenAL native objects are 
 * collected by the garbage collector, and then invoke the proper destructor
 * on the OpenGL library to delete it from memory.
 */
public abstract class NativeObject
{
    /**
     * The ID of the object, usually depends on its type.
     * Typically returned from calls like glGenTextures, glGenBuffers, etc.
     */
    protected int id = -1;

    /**
     * A reference to a "handle". By hard referencing a certain object, it's
     * possible to find when a certain GLObject is no longer used, and to delete
     * its instance from the graphics library.
     */
    protected Object handleRef = null;

    /**
     * True if the data represented by this GLObject has been changed
     * and needs to be updated before used.
     */
    protected boolean updateNeeded = true;

    /**
     * The type of the GLObject, usually specified by a subclass.
     */
    protected final Class<?> type;

    /**
     * Creates a new GLObject with the given type. Should be
     * called by the subclasses.
     * 
     * @param type The type that the subclass represents.
     */
    public NativeObject(Class<?> type)
    {
        this.type = type;
        this.handleRef = new Object();
    }

    /**
     * Protected constructor that doesn't allocate handle ref.
     * This is used in subclasses for the createDestructableClone().
     */
    protected NativeObject(Class<?> type, int id)
    {
        this.type = type;
        this.id = id;
    }

    /**
     * Sets the ID of the GLObject. This method is used in Renderer and must
     * not be called by the user.
     * @param id The ID to set
     */
    public void setId(int id)
    {
        if (this.id != -1)
            throw new IllegalStateException("ID has already been set for this GL object.");
        this.id = id;
    }

    /**
     * @return The ID of the object. Should not be used by user code in most
     * cases.
     */
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

    @Override
    public String toString(){
        return "Native" + type.getSimpleName() + " " + id;
    }

    /**
     * Called when the GL context is restarted to reset all IDs. Prevents
     * "white textures" on display restart.
     */
    public abstract void resetObject();

    public abstract void deleteObject(Object rendererObject);
}