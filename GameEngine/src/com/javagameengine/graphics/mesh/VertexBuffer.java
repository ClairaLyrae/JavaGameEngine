package com.javagameengine.graphics.mesh;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

import com.javagameengine.math.FastMath;

/**
 * A <code>VertexBuffer</code> contains a particular type of geometry
 * data used by Meshes. Every VertexBuffer set on a Mesh
 * is sent as an attribute to the vertex shader to be processed.
 * <p>
 * Several terms are used throughout the javadoc for this class, explanation:
 * <ul>
 * <li>Element - A single element is the largest individual object
 * inside a VertexBuffer. E.g. if the VertexBuffer is used to store 3D position
 * data, then an element will be a single 3D vector.</li>
 * <li>Component - A component represents the parts inside an element. 
 * For a 3D vector, a single component is one of the dimensions, X, Y or Z.</li>
 * </ul>
 */
public class VertexBuffer extends NativeObject
{
	// Type of data in this buffer
    public static enum Type {
        POSITION(3),	// 3 floats
        SIZE(1),	// 1 float (points)
        NORMAL(3),	// normal vector (normalized) 3 floats
        COLOR(4),	// 4 floats (r, g, b, a)
        POSITION_INDEX(1), 	// index buffer, must be integer!
        TEXCOORDS_INDEX(1), 	// index buffer, must be integer!
        NORMAL_INDEX(1), 	// index buffer, must be integer!
        TEXCOORDS(2); // 2 floats. Hardware supports multiple, if needed

        private int componentSize = 0;

        Type(int componentSize)
        {
            this.componentSize = componentSize;
        }
        
        public int getComponentSize()
        {
            return componentSize;
        }
    }

    // Hints to GPU what the mesh is used for (guides GPU to place it in the optimal spot)
    public static enum Usage 
    {
        STATIC,	// Mesh data is rarely changed
        DYNAMIC,	// Mesh data is updated occasionally
        STREAM,		// Updated every frame
        CPU;	// CPU only, dont send to GPU
    }

    // Format of the data that is stored in the buffer.
    public static enum Format 
    {
        HALF_FLOAT(2),
        FLOAT(4),
        DOUBLE(8),
        BYTE(1),
        UNSIGNED_BYTE(1),
        SHORT_INT(2),
        SHORT_INT_UNSIGNED(2),
        INT(4),
        INT_UNSIGNED(4);

        private int byteSize = 0;

        Format(int byteSize)
        {
            this.byteSize = byteSize;
        }
        
        public int getByteSize()
        {
            return byteSize;
        }
    }

    protected int offset = 0;	// Offset of data from start of buffer
    protected int lastLimit = 0;	// End of data from start of buffer
    protected int stride = 0;	// Number of bytes to skip between format data
    protected int components = 0;	// Number of format data

    /**
     * derived from components * format.getComponentSize()
     */
    protected transient int componentsLength = 0;	// 
    protected Buffer data = null;	// NIO buffer holding the data
    protected Usage usage;	// Usage of the data
    protected Type bufType;	// Type of data
    protected Format format;	// Format of data
    protected boolean normalized = false;	// Normalized data
    protected transient boolean dataSizeChanged = false;	// Has data changed?

    /**
     * Creates an empty, uninitialized buffer.
     * Must call setupData() to initialize.
     */
    public VertexBuffer(Type type)
    {
    	super(VertexBuffer.class);
        this.bufType = type;
    }

    /**
     * @return The offset after which the data is sent to the GPU.
     */
    public int getOffset() 
    {
        return offset;
    }

    /**
     * @param offset Specify the offset (in bytes) from the start of the buffer
     * after which the data is sent to the GPU.
     */
    public void setOffset(int offset) 
    {
        this.offset = offset;
    }

    /**
     * @return The stride (in bytes) for the data. 
     * 
     * @see #setStride(int) 
     */
    public int getStride() 
    {
        return stride;
    }

    /**
     * Set the stride (in bytes) for the data. 
     * <p>
     * If the data is packed in the buffer, then stride is 0, if there's other 
     * data that is between the current component and the next component in the 
     * buffer, then this specifies the size in bytes of that additional data.
     * 
     * @param stride the stride (in bytes) for the data
     */
    public void setStride(int stride)
    {
        this.stride = stride;
    }

    /**
     * @return A native buffer, in the specified {@link Format format}.
     */
    public Buffer getData()
    {
        return data;
    }

    /**
     * @return The usage of this buffer. See {@link Usage} for more
     * information.
     */
    public Usage getUsage()
    {
        return usage;
    }

    /**
     * @param usage The usage of this buffer. See {@link Usage} for more
     * information.
     */
    public void setUsage(Usage usage)
    {
        this.usage = usage;
    }

    /**
     * @param normalized Set to true if integer components should be converted
     * from their maximal range into the range 0.0 - 1.0 when converted to
     * a floating-point value for the shader.
     * E.g. if the {@link Format} is {@link Format#INT_UNSIGNED}, then
     * the components will be converted to the range 0.0 - 1.0 by dividing
     * every integer by 2^32.
     */
    public void setNormalized(boolean normalized)
    {
        this.normalized = normalized;
    }

    /**
     * @return True if integer components should be converted to the range 0-1.
     * @see VertexBuffer#setNormalized(boolean) 
     */
    public boolean isNormalized()
    {
        return normalized;
    }

    /**
     * @return The type of information that this buffer has.
     */
    public Type getBufferType()
    {
        return bufType;
    }

    /**
     * @return The {@link Format format}, or data type of the data.
     */
    public Format getFormat()
    {
        return format;
    }

    /**
     * @return The number of components of the given {@link Format format} per
     * element.
     */
    public int getNumComponents()
    {
        return components;
    }

    /**
     * @return The total number of data elements in the data buffer.
     */
    public int getNumElements()
    {
        int elements = data.capacity() / components;
        if (format == Format.HALF_FLOAT)
            elements /= 2;
        return elements;
    }

    /**
     * Called to initialize the data in the <code>VertexBuffer</code>. Must only
     * be called once.
     * 
     * @param usage The usage for the data, or how often will the data
     * be updated per frame. See the {@link Usage} enum.
     * @param components The number of components per element.
     * @param format The {@link Format format}, or data-type of a single
     * component.
     * @param data A native buffer, the format of which matches the {@link Format}
     * argument.
     */
    public void setupData(Usage usage, int components, Format format, Buffer data){
        if (id != -1)
            throw new UnsupportedOperationException("Data has already been sent. Cannot setupData again.");

        if (usage == null || format == null || data == null)
            throw new IllegalArgumentException("None of the arguments can be null");
            
        if (components < 1 || components > 4)
            throw new IllegalArgumentException("components must be between 1 and 4");

        this.data = data;
        this.components = components;
        this.usage = usage;
        this.format = format;
        this.componentsLength = components * format.getByteSize();
        this.lastLimit = data.limit();
        setUpdateNeeded();
    }

    /**
     * Called to update the data in the buffer with new data. Can only
     * be called after 
     * has been called. Note that it is fine to call this method on the
     * data already set, e.g. vb.updateData(vb.getData()), this will just
     * set the proper update flag indicating the data should be sent to the GPU
     * again.
     * It is allowed to specify a buffer with different capacity than the
     * originally set buffer.
     *
     * @param data The data buffer to set
     */
    public void updateData(Buffer data){
        if (id != -1){
            // request to update data is okay
        }

        // will force renderer to call glBufferData again
        if (data != null && (this.data.getClass() != data.getClass() || data.limit() != lastLimit)){
            dataSizeChanged = true;
            lastLimit = data.limit();
        }
        
        this.data = data;
        setUpdateNeeded();
    }

    /**
     * Returns true if the data size of the VertexBuffer has changed.
     * Internal use only.
     * @return true if the data size has changed
     */
    public boolean hasDataSizeChanged() 
    {
        return dataSizeChanged;
    }

    @Override
    public void clearUpdateNeeded()
    {
        super.clearUpdateNeeded();
        dataSizeChanged = false;
    }

    /**
     * Converts single floating-point data to {@link Format#HALF_FLOAT half} floating-point data.
     */
    public void convertToHalf(){
        if (id != -1)
            throw new UnsupportedOperationException("Data has already been sent.");

        if (format != Format.FLOAT)
            throw new IllegalStateException("Format must be float!");

        int numElements = data.capacity() / components;
        format = Format.HALF_FLOAT;
        this.componentsLength = components * format.getByteSize();
        
        ByteBuffer halfData = BufferUtils.createByteBuffer(componentsLength * numElements);
        halfData.rewind();

        FloatBuffer floatData = (FloatBuffer) data;
        floatData.rewind();

        for (int i = 0; i < floatData.capacity(); i++){
            float f = floatData.get(i);
            short half = FastMath.convertFloatToHalf(f);
            halfData.putShort(half);
        }
        this.data = halfData;
        setUpdateNeeded();
        dataSizeChanged = true;
    }

    /**
     * Reduces the capacity of the buffer to the given amount
     * of elements, any elements at the end of the buffer are truncated
     * as necessary.
     *
     * @param numElements The number of elements to reduce to.
     */
    public void compact(int numElements){
        int total = components * numElements;
        data.clear();
        switch (format){
            case BYTE:
            case UNSIGNED_BYTE:
            case HALF_FLOAT:
                ByteBuffer bbuf = (ByteBuffer) data;
                bbuf.limit(total);
                ByteBuffer bnewBuf = BufferUtils.createByteBuffer(total);
                bnewBuf.put(bbuf);
                data = bnewBuf;
                break;
            case SHORT_INT:
            case SHORT_INT_UNSIGNED:
                ShortBuffer sbuf = (ShortBuffer) data;
                sbuf.limit(total);
                ShortBuffer snewBuf = BufferUtils.createShortBuffer(total);
                snewBuf.put(sbuf);
                data = snewBuf;
                break;
            case INT:
            case INT_UNSIGNED:
                IntBuffer ibuf = (IntBuffer) data;
                ibuf.limit(total);
                IntBuffer inewBuf = BufferUtils.createIntBuffer(total);
                inewBuf.put(ibuf);
                data = inewBuf;
                break;
            case FLOAT:
                FloatBuffer fbuf = (FloatBuffer) data;
                fbuf.limit(total);
                FloatBuffer fnewBuf = BufferUtils.createFloatBuffer(total);
                fnewBuf.put(fbuf);
                data = fnewBuf;
                break;
            default:
                throw new UnsupportedOperationException("Unrecognized buffer format: "+format);
        }
        data.clear();
        setUpdateNeeded();
        dataSizeChanged = true;
    }

    /**
     * Modify a component inside an element.
     * The <code>val</code> parameter must be in the buffer's format:
     * {@link Format}.
     * 
     * @param elementIndex The element index to modify
     * @param componentIndex The component index to modify
     * @param val The value to set, either byte, short, int or float depending
     * on the {@link Format}.
     */
    public void setElementComponent(int elementIndex, int componentIndex, Object val){
        int inPos = elementIndex * components;
        int elementPos = componentIndex;

        if (format == Format.HALF_FLOAT){
            inPos *= 2;
            elementPos *= 2;
        }

        data.clear();

        switch (format){
            case BYTE:
            case UNSIGNED_BYTE:
            case HALF_FLOAT:
                ByteBuffer bin = (ByteBuffer) data;
                bin.put(inPos + elementPos, (Byte)val);
                break;
            case SHORT_INT:
            case SHORT_INT_UNSIGNED:
                ShortBuffer sin = (ShortBuffer) data;
                sin.put(inPos + elementPos, (Short)val);
                break;
            case INT:
            case INT_UNSIGNED:
                IntBuffer iin = (IntBuffer) data;
                iin.put(inPos + elementPos, (Integer)val);
                break;
            case FLOAT:
                FloatBuffer fin = (FloatBuffer) data;
                fin.put(inPos + elementPos, (Float)val);
                break;
            default:
                throw new UnsupportedOperationException("Unrecognized buffer format: "+format);
        }
    }

    /**
     * Get the component inside an element.
     * 
     * @param elementIndex The element index
     * @param componentIndex The component index
     * @return The component, as one of the primitive types, byte, short,
     * int or float.
     */
    public Object getElementComponent(int elementIndex, int componentIndex){
        int inPos = elementIndex * components;
        int elementPos = componentIndex;

        if (format == Format.HALF_FLOAT){
            inPos *= 2;
            elementPos *= 2;
        }

        data.clear();

        switch (format){
            case BYTE:
            case UNSIGNED_BYTE:
            case HALF_FLOAT:
                ByteBuffer bin = (ByteBuffer) data;
                return bin.get(inPos + elementPos);
            case SHORT_INT:
            case SHORT_INT_UNSIGNED:
                ShortBuffer sin = (ShortBuffer) data;
                return sin.get(inPos + elementPos);
            case INT:
            case INT_UNSIGNED:
                IntBuffer iin = (IntBuffer) data;
                return iin.get(inPos + elementPos);
            case FLOAT:
                FloatBuffer fin = (FloatBuffer) data;
                return fin.get(inPos + elementPos);
            default:
                throw new UnsupportedOperationException("Unrecognized buffer format: "+format);
        }
    }

    /**
     * Copies a single element of data from this <code>VertexBuffer</code>
     * to the given output VertexBuffer.
     * 
     * @param inIndex The input element index
     * @param outVb The buffer to copy to
     * @param outIndex The output element index
     * 
     * @throws IllegalArgumentException If the formats of the buffers do not
     * match.
     */
    public void copyElement(int inIndex, VertexBuffer outVb, int outIndex){
        if (outVb.format != format || outVb.components != components)
            throw new IllegalArgumentException("Buffer format mismatch. Cannot copy");

        int inPos  = inIndex  * components;
        int outPos = outIndex * components;
        int elementSz = components;
        if (format == Format.HALF_FLOAT){
            // because half is stored as bytebuf but its 2 bytes long
            inPos *= 2;
            outPos *= 2;
            elementSz *= 2;
        }

        data.clear();
        outVb.data.clear();

        switch (format){
            case BYTE:
            case UNSIGNED_BYTE:
            case HALF_FLOAT:
                ByteBuffer bin = (ByteBuffer) data;
                ByteBuffer bout = (ByteBuffer) outVb.data;
                bin.position(inPos).limit(inPos + elementSz);
                bout.position(outPos).limit(outPos + elementSz);
                bout.put(bin);
                break;
            case SHORT_INT:
            case SHORT_INT_UNSIGNED:
                ShortBuffer sin = (ShortBuffer) data;
                ShortBuffer sout = (ShortBuffer) outVb.data;
                sin.position(inPos).limit(inPos + elementSz);
                sout.position(outPos).limit(outPos + elementSz);
                sout.put(sin);
                break;
            case INT:
            case INT_UNSIGNED:
                IntBuffer iin = (IntBuffer) data;
                IntBuffer iout = (IntBuffer) outVb.data;
                iin.position(inPos).limit(inPos + elementSz);
                iout.position(outPos).limit(outPos + elementSz);
                iout.put(iin);
                break;
            case FLOAT:
                FloatBuffer fin = (FloatBuffer) data;
                FloatBuffer fout = (FloatBuffer) outVb.data;
                fin.position(inPos).limit(inPos + elementSz);
                fout.position(outPos).limit(outPos + elementSz);
                fout.put(fin);
                break;
            default:
                throw new UnsupportedOperationException("Unrecognized buffer format: "+format);
        }

        data.clear();
        outVb.data.clear();
    }

    /**
     * Creates a {@link Buffer} that satisfies the given type and size requirements
     * of the parameters. The buffer will be of the type specified by
     * {@link Format format} and would be able to contain the given number
     * of elements with the given number of components in each element.
     */
    public static Buffer createBuffer(Format format, int components, int numElements){
        if (components < 1 || components > 4)
            throw new IllegalArgumentException("Num components must be between 1 and 4");

        int total = numElements * components;

        switch (format){
            case BYTE:
            case UNSIGNED_BYTE:
                return BufferUtils.createByteBuffer(total);
            case HALF_FLOAT:
                return BufferUtils.createByteBuffer(total * 2);
            case SHORT_INT:
            case SHORT_INT_UNSIGNED:
                return BufferUtils.createShortBuffer(total);
            case INT:
            case INT_UNSIGNED:
                return BufferUtils.createIntBuffer(total);
            case FLOAT:
                return BufferUtils.createFloatBuffer(total);
            case DOUBLE:
                return BufferUtils.createDoubleBuffer(total);
            default:
                throw new UnsupportedOperationException("Unrecoginized buffer format: "+format);
        }
    }

    @Override
    public void resetObject() 
    {
        this.id = -1;
        setUpdateNeeded();
    }

    @Override
    public void deleteObject(Object rendererObject) 
    {
    	// TODO delete this from the GPU
    }
}