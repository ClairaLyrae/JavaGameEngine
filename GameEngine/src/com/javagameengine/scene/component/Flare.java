package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
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
import com.javagameengine.math.FastMath;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.renderer.OcclusionQuery;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.renderer.RendererState;
import com.javagameengine.renderer.RendererState.BlendMode;
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
    		"uniform int flare_always_on_top;\n" +
    		"uniform int flare_allow_perspective;\n" +
    		"uniform int flare_draw_at_infinity;\n" +
    		"uniform int flare_rotate_to_up;\n" +
            "in vec3 in_position;\n" +
            "out vec2 texcoords;\n" +
            "void main()\n" +
            "{\n" +
            "    texcoords = in_position.xy;\n" +
            "    gl_Position = (vec4(0.0, 0.0, 0.0, 1.0)*mv + vec4(in_position.x, in_position.y, 0.0, 0.0))*p;\n" +
            "    gl_Position = (vec4(0.0, 0.0, 0.0, ((1/flare_size))/gl_Position.z)*mv + vec4(in_position.x-0.5, in_position.y-0.5, 0.0, 0.0))*p;\n" +
            "    //gl_Position.x -= 0.5*flare_size;\n" +
            "    //gl_Position.y -= 0.5*flare_size;\n" +
    		"	 if(flare_always_on_top == 1)\n" +
    		"	 {\n" +
    		"	 	gl_Position.z = -0.99;\n" +
    		"	 }\n" +
            "}\n";

	private static Mesh billboard = new Mesh();
	private static boolean isInitialized = false;
	
	private static Shader vertShader;
	private static Shader fragShader;

	public BlendMode srcBlend = BlendMode.SRC_ALPHA;
	public BlendMode destBlend = BlendMode.ONE_MINUS_SRC_ALPHA;
	
	private boolean drawAtInfinity = false;	// Draw this flare at effective infinity (extend position vector)
	private boolean scaleByDistance = false;	// Allow flare to be affected by perspective
	private boolean rotateToUp = true;	// Always align flare up
	private boolean fadeOut = true;	// Scale flare when occlusion size is hidden
	private boolean isAlwaysOnTop = true;	// Draw flare over all other objects
	
	private float size = 10f;	// Flare size at full view
	private float sizePercent = 1f;	// Read only, holds current occlusion percentage
	private float fadeTestSize = 100f;	// Controls speed of fade out
	private float fadeTestSizeMax = 100f;	// Controls speed of fade out
	
	public float getFadeTestSize()
	{
		return this.fadeTestSize;
	}
	
	public void setFadeTestSize(float s)
	{
		fadeTestSize = s;
		fadeTestSizeMax = FastMath.PI*fadeTestSize*fadeTestSize*0.25f;
	}
	
	public float getSize()
	{
		return size;
	}
	
	public void setSize(float f)
	{
		size = f;
	}

	public boolean isAtInfinity()
	{
		return scaleByDistance;
	}
	
	public void setAtInfinity(boolean b)
	{
		scaleByDistance = b;
	}
	
	public boolean isPerspectiveEnabled()
	{
		return scaleByDistance;
	}
	
	public void setPerspectiveEnabled(boolean b)
	{
		scaleByDistance = b;
	}

	public boolean isRotateEnabled()
	{
		return rotateToUp;
	}
	
	public void setRotateEnabled(boolean b)
	{
		rotateToUp = b;
	}

	public boolean isFadeEnabled()
	{
		return fadeOut;
	}

	public void setFadeEnabled(boolean state)
	{
		this.fadeOut = state;
	}

	public boolean isAlwaysOnTop()
	{
		return isAlwaysOnTop;
	}

	public void setAlwaysOnTop(boolean s)
	{
		this.isAlwaysOnTop = s;
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
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(srcBlend.getGLParam(), destBlend.getGLParam());
		if(fadeOut)
		{
			new OcclusionQuery() {
				@Override
				public void query() 
				{
					GL11.glDepthMask(false);
					GL11.glColorMask(false, false, false, false);
					GL20.glUseProgram(0);
					GL11.glPointSize(fadeTestSize);
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
					sizePercent = (float)samplesPassed/fadeTestSizeMax;
				}
			};
		}
		else
			sizePercent = 1f;
		
		int loc;

		//this.setSize(5f);
		this.setFadeTestSize(8f);
		//this.fadeOut = true;
		//this.isAlwaysOnTop = false;
		//this.scaleByDistance = false;
		//this.drawAtInfinity = false;
		//this.rotateToUp = false;
		
		// If flare is drawn over everything
		loc = glGetUniformLocation(mat.getID(), "flare_always_on_top");
		GL20.glUniform1i(loc, isAlwaysOnTop ? 1 : 0);
		
		// Scale flare by distance. If 0, const size relative to screen.
		loc = glGetUniformLocation(mat.getID(), "flare_allow_perspective");
		GL20.glUniform1i(loc, scaleByDistance ? 1 : 0);
		
		// Rotate flare to aling to screen
		loc = glGetUniformLocation(mat.getID(), "flare_rotate_to_up");
		GL20.glUniform1i(loc, rotateToUp ? 1 : 0);
		
		// Draw flare at infinite dist
		loc = glGetUniformLocation(mat.getID(), "flare_draw_at_infinity");
		GL20.glUniform1i(loc, drawAtInfinity ? 1 : 0);
		
		// Size of flare
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
