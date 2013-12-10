package com.javagameengine.assets.skybox;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Shader;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.material.Material.TextureType;
import com.javagameengine.assets.mesh.Attribute;
import com.javagameengine.assets.mesh.AttributeUsage;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.mesh.Mesh.Mode;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.RenderableComponent;
import com.javagameengine.scene.component.Light.Usage;

/**
 * A skybox is a predefined mesh with an internally bound GLSL shader that can be loaded
 * from a file. Skybox files are stored as a list of texture names corresponding to the different 
 * faces of the skybox. 
 */
public class Skybox implements Drawable
{
	private static float[] normals = {
        // Front face
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        0.0f, 0.0f, -1.0f,
        
        // Back face
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        
        // Top face
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        0.0f, -1.0f, 0.0f,
        
        // Bottom face
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        
        // Right face
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        -1.0f, 0.0f, 0.0f,
        
        // Left face
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f 
        };
		
	private static float[] vertices = {
        // Front face
        -1.0f, -1.0f,  1.0f,
         1.0f, -1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,
        -1.0f,  1.0f,  1.0f,
        
        // Back face
        -1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,
         1.0f,  1.0f, -1.0f,
         1.0f, -1.0f, -1.0f,
        
        // Top face
        -1.0f,  1.0f, -1.0f,
        -1.0f,  1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,
         1.0f,  1.0f, -1.0f,
        
        // Bottom face
        -1.0f, -1.0f, -1.0f,
         1.0f, -1.0f, -1.0f,
         1.0f, -1.0f,  1.0f,
        -1.0f, -1.0f,  1.0f,
        
        // Right face
         1.0f, -1.0f, -1.0f,
         1.0f,  1.0f, -1.0f,
         1.0f,  1.0f,  1.0f,
         1.0f, -1.0f,  1.0f,
        
        // Left face
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f,  1.0f,
        -1.0f,  1.0f,  1.0f,
        -1.0f,  1.0f, -1.0f 
        };
	
	private static short[] cubeVertexIndices = {
        0,  1,  2,      0,  2,  3,    // front
        4,  5,  6,      4,  6,  7,    // back
        8,  9,  10,     8,  10, 11,   // top
        12, 13, 14,     12, 14, 15,   // bottom
        16, 17, 18,     16, 18, 19,   // right
        20, 21, 22,     20, 22, 23    // left
		};
		
	private static String frag_shader = 
			"#version 150\n" +
			"uniform samplerCube tex_cube;\n" +
			"in vec3 texcoords;\n" +
			"out vec4 fragColor;\n" +
			"void main ()\n" +
			"{\n" +
			"	fragColor = texture(tex_cube, texcoords);\n" +
			"}\n";

	private static String vert_shader = 
			"#version 150\n" +
			"uniform mat4 p;\n" +
			"uniform mat4 v;\n" +
			"in vec3 in_position;\n" +
			"out vec3 texcoords;\n" +
			"void main () \n" +
			"{\n" +
			"mat4 v_mod = v;\n" +
			"v_mod[0].w = 0;\n" +
			"v_mod[1].w = 0;\n" +
			"v_mod[2].w = 0;\n" +
			"texcoords = in_position;\n" +
			"gl_Position = vec4(in_position, 1.0) * v_mod * p;\n" +
			"}";
	
	private static Mesh cube;
	private static Shader fragShader;
	private static Shader vertShader;
	private static boolean isInitialized = false;

	private Material mat;
	
	private Skybox()
	{
	}

	public static Skybox loadFromFile(File f) throws IOException
	{
		String[] names = new String[6];
        BufferedReader reader = null;
        try 
        {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null)
            {
            	String[] split = line.split(" ");
            	if(split.length == 2 && split[0].equalsIgnoreCase("top"))
	            	names[0] = split[1];
            	if(split.length == 2 && split[0].equalsIgnoreCase("bottom"))
	            	names[1] = split[1];
            	if(split.length == 2 && split[0].equalsIgnoreCase("left"))
	            	names[2] = split[1];
            	if(split.length == 2 && split[0].equalsIgnoreCase("right"))
	            	names[3] = split[1];
            	if(split.length == 2 && split[0].equalsIgnoreCase("front"))
	            	names[4] = split[1];
            	if(split.length == 2 && split[0].equalsIgnoreCase("back"))
	            	names[5] = split[1];
            }
        } finally {
            if(reader != null) 
            	reader.close();
        }
		Texture tex_top = AssetManager.getTexture(names[0]);
		Texture tex_bottom = AssetManager.getTexture(names[1]);
		Texture tex_left = AssetManager.getTexture(names[2]);
		Texture tex_right = AssetManager.getTexture(names[3]);
		Texture tex_front = AssetManager.getTexture(names[4]);
		Texture tex_back = AssetManager.getTexture(names[5]);
		if(tex_front == null || tex_left == null || tex_top == null || tex_bottom == null || tex_back == null || tex_right == null)
			throw new IOException("Could not create skybox");
		Texture cubemap = new Texture(tex_top, tex_bottom, tex_left, tex_right, tex_front, tex_back);
		cubemap.create();
		return createSkybox(cubemap);
	}
	
	public static Skybox createSkybox(Texture cubemap)
	{
		if(cubemap == null || cubemap.getType() != Texture.Type.CUBE_MAP)
			throw new IllegalStateException("Cannot create skybox. Texture must be a cube map.");
		if(!isInitialized)
			initialize();
		Skybox sky = new Skybox();
		sky.setTexture(cubemap);
		return sky;
	}
	
	public Texture getTexture()
	{
		if(mat == null)
			return null;
		return mat.getTexture(Material.TextureType.CUBE);
	}
	
	public void setTexture(Texture t)
	{
		if(mat == null)
		{
			mat = new Material();
			mat.setShader(fragShader);
			mat.setShader(vertShader);
			mat.create();
		}
		mat.setTexture(Material.TextureType.CUBE, t);
	}

	@Override
	public void draw()
	{
		mat.bind();

		int progID = mat.getID();
		
		FloatBuffer V_buffer = Renderer.view_matrix.toBuffer();
		int loc = glGetUniformLocation(progID, "v");
		glUniformMatrix4(loc, false, V_buffer);
		
		FloatBuffer P_buffer = Renderer.projection_matrix.toBuffer();
		loc = glGetUniformLocation(progID, "p");
		glUniformMatrix4(loc, false, P_buffer);
		
		cube.draw();
	}
	
	private static void initialize()
	{
		fragShader = new Shader(Shader.Type.FRAGMENT, frag_shader);
		fragShader.create();
		vertShader = new Shader(Shader.Type.VERTEX, vert_shader);
		vertShader.create();
		
		cube = new Mesh();

		FloatBuffer normBuf = BufferUtils.createFloatBuffer(normals.length);
		normBuf.put(normals);
		normBuf.flip();
		FloatBuffer posBuf = BufferUtils.createFloatBuffer(vertices.length);
		posBuf.put(vertices);
		posBuf.flip();
		ShortBuffer indBuf = BufferUtils.createShortBuffer(cubeVertexIndices.length);
		indBuf.put(cubeVertexIndices);
		indBuf.flip();
		
		cube.setBuffer(Attribute.POSITION, AttributeUsage.DYNAMIC, posBuf);
		cube.setBuffer(Attribute.NORMAL, AttributeUsage.DYNAMIC, normBuf);
		cube.setIndexBuffer(AttributeUsage.DYNAMIC, indBuf);
		cube.setMode(Mesh.Mode.TRIANGLE);
	
		cube.create();
		cube = AssetManager.getMesh("skybox");
//		ShortBuffer sb = (ShortBuffer)cube.getIndexBuffer().getData();
//		FloatBuffer fb = (FloatBuffer)cube.getBuffer(Attribute.POSITION).getData();
//		while(fb.hasRemaining())
//			System.out.println("p " + fb.get() + ", " + fb.get() + ", " + fb.get());
//		fb = (FloatBuffer)cube.getBuffer(Attribute.NORMAL).getData();
//		while(fb.hasRemaining())
//			System.out.println("n " + fb.get() + ", " + fb.get() + ", " + fb.get());
//		while(sb.hasRemaining())
//			System.out.println("i " + sb.get());
	}
	
	public String toString()
	{
		return "skybox=[texture=" + mat.getTexture(TextureType.CUBE) + "]";
	}
}
