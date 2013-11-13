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
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

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
	
	private int id = -1;
	private Mode mode;
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
		if(id < 0)
			throw new UnsupportedOperationException("Texture is already bound to the GPU");
		id = glGenTextures(); //Generate texture ID
	    glBindTexture(GL_TEXTURE_2D, id); //Bind texture ID

	    //Setup wrap mode
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	    //Setup texture scaling filtering
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	    //Send texel data to OpenGL
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

	    //Return the texture ID so we can bind it later again
	    return id;
	}

    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA

    public static Image loadImage(String loc) throws FileNotFoundException, IOException
    {
    	 BufferedImage image = null;
         image = ImageIO.read(new File(loc));
         Texture tex;
         
         int[] pixels = new int[image.getWidth() * image.getHeight()];
         image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

         ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

         for(int y = 0; y < image.getHeight(); y++){
             for(int x = 0; x < image.getWidth(); x++){
                 int pixel = pixels[y * image.getWidth() + x];
                 buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                 buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                 buffer.put((byte) (pixel & 0xFF));               // Blue component
                 buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
             }
         }

         buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
         return null;
    }
}
