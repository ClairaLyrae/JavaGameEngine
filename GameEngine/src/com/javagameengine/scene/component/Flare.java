package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Material.TextureType;
import com.javagameengine.assets.material.Shader;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Attribute;
import com.javagameengine.assets.mesh.AttributeUsage;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.renderer.OcclusionQuery;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.RenderableComponent;

public class Flare extends RenderableComponent
{
	private static final String fragShaderSource = "#version 150\n " +
		"uniform sampler2D tex_diffuse;\n" +
		"in vec2 texcoords;\n" +
		"out vec4 fragColor;\n" +
		"void main()\n" +
		"{\n" +
		"fragColor = texture2D(tex_diffuse, texcoords);\n" +
		"}\n";
	
	private static final String vertShaderSource = "#version 150\n" +
		"uniform mat4 p;\n" +
		"uniform mat4 v;\n" +
		"uniform mat4 mv;\n" +
		"uniform float flare_size;\n" +
		"uniform int flare_depth_test;\n" +
		"uniform int flare_scale;\n" +
		"uniform int flare_rotate;\n" +
		"in vec3 in_position;\n" +
		"out vec2 texcoords;\n" +
		"void main()\n" +
		"{\n" +
		"	texcoords = in_position.xy;\n" +
		"	gl_Position = (vec4(0.0, 0.0, 0.0, 5.0)*mv + vec4(in_position.x, in_position.y, 0.0, 0.0))*p;\n" +
		"	gl_Position = (vec4(0.0, 0.0, 0.0, (5 + (1/flare_size))/gl_Position.z)*mv + vec4(in_position.x, in_position.y, 0.0, 0.0))*p;\n" +
		"	gl_Position.x -= 0.75;\n" +
		"	gl_Position.y -= 1.25;\n" +
		"	gl_Position.z = -0.99;\n" +
		"}\n";

	private static Mesh billboard = new Mesh();
	private static boolean isInitialized = false;
	
	private static Shader vertShader;
	private static Shader fragShader;
	
	private boolean enableDistScaling = false;	// Allow dist scaling
	private boolean enableRotateToUp = true;	// Always align flare up
	private boolean enableOcclusion = true;	// Scale flare when occlusion size is hidden
	private boolean enableDepthTest = false;	// Allow openGL to draw over this flare (not always on top)
	
	private float size = 10f;	// Flare size at full view
	private float sizePercent = 1f;	// Read only, holds current occlusion percentage
	private float depthTestSize = 100f;	// Controls speed of fade out
	
	public float getOcclusionSize()
	{
		return this.depthTestSize;
	}
	
	public void setOcclusionSize(float s)
	{
		depthTestSize = s;
	}
	
	public float getSize()
	{
		return size;
	}
	
	public void setSize(float f)
	{
		size = f;
	}
	
	public boolean hasScaling()
	{
		return enableDistScaling;
	}
	
	public void setScaling(boolean b)
	{
		enableDistScaling = b;
	}

	public boolean hasRotation()
	{
		return enableRotateToUp;
	}
	
	public void setRotation(boolean b)
	{
		enableRotateToUp = b;
	}

	public boolean isOcclusionEnabled()
	{
		return enableOcclusion;
	}

	public void setOcclusionEnabled(boolean enableOcclusion)
	{
		this.enableOcclusion = enableOcclusion;
	}

	public boolean isDepthTestEnabled()
	{
		return enableDepthTest;
	}

	public void setDepthTestEnabled(boolean enableDepthTest)
	{
		this.enableDepthTest = enableDepthTest;
	}
	
	private Material mat = new Material();
	
	public static Flare createFlare(Texture t)
	{
		if(!isInitialized)
			initialize();
		Flare flare = new Flare();
		flare.mat.setTexture(TextureType.DIFFUSE, t);
		flare.mat.setShader(fragShader);
		flare.mat.setShader(vertShader);
		flare.mat.create();
		return flare;
	}
	
	private Flare()
	{
		
	}

	private static void initialize()
	{
		fragShader = new Shader(Shader.Type.FRAGMENT, fragShaderSource);
		fragShader.create();
		vertShader = new Shader(Shader.Type.VERTEX, vertShaderSource);
		vertShader.create();
		billboard = AssetManager.getMesh("skybox");
	}

	@Override
	public boolean onRender()
	{
		if(enableOcclusion)
		{
			new OcclusionQuery() {
				@Override
				public void query() 
				{
					GL11.glDepthMask(false);
					GL11.glColorMask(false, false, false, false);
					GL20.glUseProgram(0);
					GL11.glPointSize(7f);
					GL11.glBegin(GL_POINTS);
					GL11.glColor4f(1f, 0f, 0f, 1f);
					GL11.glVertex3f(0f, 0f, 0f);
					GL11.glEnd();
					GL20.glUseProgram(mat.getID());
					GL11.glDepthMask(true);
					GL11.glColorMask(true, true, true, true);
				}

				@Override
				public void onComplete(int samplesPassed) 
				{ 
					sizePercent = samplesPassed/40f;
				}
			};
		}
		else
			sizePercent = 1f;
		
		int loc;

		loc = glGetUniformLocation(mat.getID(), "flare_depth_test");
		GL20.glUniform1i(loc, enableDepthTest ? 1 : 0);
		
		loc = glGetUniformLocation(mat.getID(), "flare_scale");
		GL20.glUniform1i(loc, enableDistScaling ? 1 : 0);
		
		loc = glGetUniformLocation(mat.getID(), "flare_rotate");
		GL20.glUniform1i(loc, enableRotateToUp ? 1 : 0);
		
		loc = glGetUniformLocation(mat.getID(), "flare_size");
		GL20.glUniform1f(loc, size*sizePercent);
		return true;
	}

	@Override
	public Bindable getBindable()
	{
		return mat;
	}

	@Override
	public Drawable getDrawable()
	{
		return billboard;
	}

	@Override
	public boolean isTransparent()
	{
		return true;
	}
}
