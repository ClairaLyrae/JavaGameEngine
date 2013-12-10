package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import com.javagameengine.assets.NativeObject;

/**
 * A Texture object is a wrapper for a NIO buffer or array of NIO buffers containing data that
 * can be sampled by the GPU. Is a NativeObject which can be loaded into a texture buffer on the GPU
 */
public class Texture extends NativeObject
{	
	/**
	 * Texture format
	 */
	public enum Type {
		CUBE_MAP(GL13.GL_TEXTURE_CUBE_MAP),
		D1(GL11.GL_TEXTURE_1D),
		D1_DIM_ARRAY(GL30.GL_TEXTURE_1D_ARRAY),
		D2(GL11.GL_TEXTURE_2D),
		D2_ARRAY(GL30.GL_TEXTURE_2D_ARRAY),
		D3(GL12.GL_TEXTURE_3D);
				
		private int gl;
		
		private Type(int gl)
		{
			this.gl = gl;
		}
		
		public int getGLParam()
		{
			return gl;
		}
	}

	/**
	 * Data storage mode
	 */
	public enum Mode {
		RGB(3),
		RGBA(4);
		
		private int components;
		
		private Mode(int components)
		{
			this.components = components;
		}
		
		public int getNumComponents()
		{
			return components;
		}
	}

	/**
	 * Minification filter
	 */
	public enum MinFilter {
		NEAREST(GL11.GL_NEAREST),
		LINEAR(GL11.GL_LINEAR),
		NEAREST_MIPMAP_NEAREST(GL11.GL_NEAREST_MIPMAP_NEAREST),
		NEAREST_MIPMAP_LINEAR(GL11.GL_NEAREST_MIPMAP_LINEAR),
		LINEAR_MIPMAP_NEAREST(GL11.GL_LINEAR_MIPMAP_NEAREST),
		LINEAR_MIPMAP_LINEAR(GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		private int gl;
		
		private MinFilter(int gl)
		{
			this.gl = gl;
		}
		
		public int getGLParam()
		{
			return gl;
		}
	}
	
	/**
	 * Magnification filter
	 */
	public enum MagFilter {
		NEAREST(GL11.GL_NEAREST),
		LINEAR(GL11.GL_LINEAR);
		
		private int gl;
		
		private MagFilter(int gl)
		{
			this.gl = gl;
		}
		
		public int getGLParam()
		{
			return gl;
		}
	}

    /**
     * Texture wrapping mode
     */
    public enum WrapMode {
        REPEAT(GL11.GL_REPEAT),
        REPEAT_MIRRORED(GL14.GL_MIRRORED_REPEAT),
        CLAMP(GL11.GL_CLAMP),
        CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER),
        CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE);
		
		private int gl;
		
		private WrapMode(int gl)
		{
			this.gl = gl;
		}
		
		public int getGLParam()
		{
			return gl;
		}
    }
    
    protected Type type;
    protected Mode mode;
	protected WrapMode wrap = WrapMode.CLAMP_TO_EDGE;
	protected MinFilter minfilter = MinFilter.LINEAR;
	protected MagFilter magfilter = MagFilter.LINEAR;
	protected ByteBuffer[] data;
	protected int width;
	protected int height;
	protected int depth;

	private Texture(int width, int height, int depth, Mode mode, Type type, ByteBuffer data)
	{
		super(Texture.class);
		this.depth = depth;
		this.type = type;
		this.mode = mode;
		this.width = width;
		this.height = height;
		this.data = new ByteBuffer[1];
		this.data[0] = data;
	}
	
	private Texture(int width, int height, int depth, Mode mode, Type type, ByteBuffer[] data)
	{
		super(Texture.class);
		this.depth = depth;
		this.type = type;
		this.mode = mode;
		this.width = width;
		this.height = height;
		this.data = data;
	}

	public Texture(Texture top, Texture bottom, Texture left, Texture right, Texture front, Texture back)
	{
		this(top.width, top.height, 0, top.mode, Type.CUBE_MAP, new ByteBuffer[6]);
		data[0] = bottom.data[0];
		data[1] = top.data[0];
		data[2] = left.data[0];
		data[3] = right.data[0];
		data[4] = front.data[0];
		data[5] = back.data[0];
	}

	public Texture(int width, int height, Mode mode, ByteBuffer buf)
	{
		this(width, height, 0, mode, Type.D2, buf);
	}

	public Texture(int width, int height, Mode mode, ByteBuffer[] buf)
	{
		this(width, height, 0, mode, Type.D2, buf);
	}
	
	public Texture(int width, int height, int depth, Mode mode, ByteBuffer buf)
	{
		this(width, height, depth, mode, Type.D3, buf);
	}
	
	public Texture(int width, Mode mode, ByteBuffer buf)
	{
		this(width, 0, 0, mode, Type.D1, buf);
	}

	public Texture(int width, Mode mode, ByteBuffer[] buf)
	{
		this(width, 0, 0, mode, Type.D1, buf);
	}
	
	public Texture(int width, int height, Mode mode)
	{
		this(width, height, mode, BufferUtils.createByteBuffer(width*height));
	}
	
	public Texture(int width, int height, int depth, Mode mode)
	{
		this(width, height, depth, mode, BufferUtils.createByteBuffer(width*height*depth));
	}
	
	public Texture(int width, Mode mode)
	{
		this(width, 0, 0, mode, BufferUtils.createByteBuffer(width));
	}
	
	public Type getType()
	{
		return type;
	}
	
	public Mode getMode()
	{
		return mode;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}

	public ByteBuffer getData()
	{
		return data[0];
	}
	
	public ByteBuffer[] getDataArray()
	{
		return data;
	}
	
	public int getNumDataBuffers()
	{
		return data.length;
	}
	
	public int getTextureId()
	{
		return id;
	}

    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA

    private static BufferedImage verticalflip(BufferedImage img) 
    {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }
    
    public static Texture loadFromFile(File f) throws FileNotFoundException, IOException
    {
    	 BufferedImage image = null;
      	long beginTime = System.currentTimeMillis();
         image = ImageIO.read(f);
         image = verticalflip(image);
         if(image.getWidth() > GL11.GL_MAX_TEXTURE_SIZE || image.getHeight() > GL11.GL_MAX_TEXTURE_SIZE)
        	 throw new IllegalStateException("Texture " + f.getName() + " exceeds the maximum texture size");
         int[] pixels = new int[image.getWidth() * image.getHeight()];
         image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

         ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

         for(int y = 0; y < image.getHeight(); y++)
         {
             for(int x = 0; x < image.getWidth(); x++)
             {
                 int pixel = pixels[y * image.getWidth() + x];
                 buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                 buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                 buffer.put((byte) (pixel & 0xFF));               // Blue component
                 buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA (pixel >> 24) & 0xFF
             }
         }
         buffer.flip();
         return new Texture(image.getWidth(), image.getHeight(), Mode.RGBA, buffer);
    }

	public MagFilter getMagFilter()
	{
		return magfilter;
	}

	public void setMagFilter(MagFilter magfilter)
	{
		this.magfilter = magfilter;
	}

	public MinFilter getMinFilter()
	{
		return minfilter;
	}

	public void setMinFilter(MinFilter minfilter)
	{
		this.minfilter = minfilter;
	}

	public WrapMode getWrapMode()
	{
		return wrap;
	}

	public void setWrapMode(WrapMode wrap)
	{
		this.wrap = wrap;
	}

	@Override
	public void destroy()
	{
		if(id != -1)
			GL11.glDeleteTextures(id);
	}

	@Override
	public boolean create()
	{
		if(id != -1)
			throw new UnsupportedOperationException("Texture is already loaded to the GPU");
		id = glGenTextures(); //Generate texture ID
	    glBindTexture(type.getGLParam(), id); //Bind texture ID

	    //Setup wrap mode
	    glTexParameteri(type.getGLParam(), GL_TEXTURE_WRAP_S, wrap.getGLParam());
	    glTexParameteri(type.getGLParam(), GL_TEXTURE_WRAP_T, wrap.getGLParam());

	    //Setup texture scaling filtering
	    glTexParameteri(type.getGLParam(), GL_TEXTURE_MIN_FILTER, minfilter.getGLParam());
	    glTexParameteri(type.getGLParam(), GL_TEXTURE_MAG_FILTER, magfilter.getGLParam());
	    
	    switch(type)
	    {
		case CUBE_MAP:
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[0]);
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[1]);
		    
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[2]);
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[3]);
		    
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[4]);
		    glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[5]);
			break;
		case D1:
		    glTexImage1D(type.getGLParam(), 0, GL11.GL_RGBA8, width, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[0]);
			break;
		case D1_DIM_ARRAY:
			for(int i = 0; i < data.length; i++)
				glTexImage1D(type.getGLParam(), i, GL11.GL_RGBA8, width, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[i]);
			break;
		case D2:
		    glTexImage2D(type.getGLParam(), 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[0]);
			break;
		case D2_ARRAY:
			for(int i = 0; i < data.length; i++)
				glTexImage2D(type.getGLParam(), i, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[i]);
			break;
		case D3:
		    GL12.glTexImage3D(type.getGLParam(), 0, GL11.GL_RGBA8, width, height, depth, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data[0]);
			break;
		default:
			break;
	    }
	    return true;
	}
	
	public String toString()
	{
		return "mode=" + mode.toString() + ", wrap=" + wrap.toString() + ", minfilter=" + minfilter + ", magfilter=" + magfilter +", size=[" + width + "," + height + "]";
	}
	
	/**
	 * Binds a texture for fixed-function use. Following fixed function calls will draw with texture.
	 */
	public void bind()
	{
		glEnable(type.getGLParam());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glBindTexture(type.getGLParam(), id);
	}
	
	/**
	 * Unbinds a texture for fixed-function use.
	 */
	public void unbind()
	{
		glBindTexture(type.getGLParam(), 0);
		GL11.glDisable(type.getGLParam());
	}
}
