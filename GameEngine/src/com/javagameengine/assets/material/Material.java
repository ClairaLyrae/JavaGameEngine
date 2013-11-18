package com.javagameengine.assets.material;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Material defines a material composed of textures and shaders that can be applied to renderable objects and 
 * rendered in the rendering queue.
 * @author ClairaLyrae
 */
public class Material
{
	public enum TextureType {
		DIFFUSE_0,
		DIFFUSE_1,
		DIFFUSE_2,
		NORMAL,
		SPECULAR,
		EMISSIVE,
		ALPHA;
		
		
	}
	
	private int materialID = -1;
	private ShaderProgram shader = null;
	private Texture[] textures = new Texture[10];
	private float tilingX = 1.0f;
	private float tilingY = 1.0f;
	private float offsetX = 1.0f;
	private float offsetY = 1.0f;
	
	public void bind()
	{
		// Bind all the textures first
		for(int i = 0; i < textures.length; i++)
		{
			if(textures[i] == null)
				break;
			glActiveTexture(GL_TEXTURE0 + i); 
			glBindTexture(GL_TEXTURE_2D, textures[i].getId()); 
		}

		//int loc = glGetUniformLocation(shader.getId(), SHADER INPUT ATTR NAME);
		//glUniform1i(loc, SHADER INPUT ATTR POSITION);
		
		// load the program
	    glUseProgram(shader.getId());
	    
	    glBegin(GL_TRIANGLES);
	    glEnd();
	          
	    glUseProgram(0);
	}
}

