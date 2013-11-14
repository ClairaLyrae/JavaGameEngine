package com.javagameengine.graphics.mesh;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

/**
 * @author ClairaLyrae
 * A Texture object represents a 2D image.
 */
public class Texture
{
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
    
	private int id = -1;
	private Mode mode;
	private WrapMode wrap = WrapMode.CLAMP_TO_EDGE;
	private MinFilter minfilter = MinFilter.LINEAR;
	private MagFilter magfilter = MagFilter.LINEAR;
	private ByteBuffer data;
	private int width;
	private int height;
	
	public Texture(int width, int height, Mode mode)
	{
		this.mode = mode;
		this.width = width;
		this.height = height;
		BufferUtils.createByteBuffer(width*height);
	}
	
	private Texture(int width, int height, Mode mode, ByteBuffer data)
	{
		this.mode = mode;
		this.width = width;
		this.height = height;
		this.data = data;
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
		return data;
	}
	
	public int getTextureId()
	{
		return id;
	}
	
	public int bindTexture()
	{
		if(id >= 0)
			throw new UnsupportedOperationException("Texture is already bound to the GPU");
		id = glGenTextures(); //Generate texture ID

	    glBindTexture(GL_TEXTURE_2D, id); //Bind texture ID

	    //Setup wrap mode
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap.getGLParam());
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap.getGLParam());

	    //Setup texture scaling filtering
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minfilter.getGLParam());
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magfilter.getGLParam());
        
	    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);

	    //Send texel data to OpenGL
	    glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL_UNSIGNED_BYTE, data);

	    //Return the texture ID so we can bind it later again
	    return id;
	}

    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA

    public static Texture loadTexture(File f) throws FileNotFoundException, IOException, InvalidTextureException
    {
    	 BufferedImage image = null;
         image = ImageIO.read(f);
         
         if(image.getWidth() > GL11.GL_MAX_TEXTURE_SIZE || image.getHeight() > GL11.GL_MAX_TEXTURE_SIZE)
        	 throw new InvalidTextureException();
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
}
