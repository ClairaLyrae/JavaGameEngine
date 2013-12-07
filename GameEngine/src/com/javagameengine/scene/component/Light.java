package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.javagameengine.math.Color4f;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.renderer.Bindable;
import com.javagameengine.renderer.Drawable;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.RenderableComponent;

public class Light extends Component
{	
	public enum Type {
		SPOT,
		OMNI,
		DIRECTIONAL;
	}
	
	public enum Usage {
		STATIC,
		TRANSIENT;
	}
	
	private Usage usage = Usage.STATIC;
	private Type type = Type.DIRECTIONAL;
	private float spotAngle = 0.0f;
	private float spotExp = 0.0f;
	private float limit = 0.0f;
	private float intensity = 1.0f;
	
	protected Color4f colorDiffuse = Color4f.black;
	protected Color4f colorSpecular = Color4f.black;
	protected Color4f colorAmbient = Color4f.black;
	
	private boolean shadows = false;
	
	public Usage getUsage()
	{
		return usage;
	}
	
	public void setUsage(Usage u)
	{
		usage = u;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public void setType(Type t)
	{
		type = t;
	}
	
	public Color4f getDiffuseColor()
	{
		return colorDiffuse;
	}
	
	public Color4f getSpecularColor()
	{
		return colorSpecular;
	}
	
	public float getIntensity()
	{
		return intensity;
	}
	
	public void setIntensity(float f)
	{
		intensity = f;
	}
	
	public Color4f getAmbientColor()
	{
		return colorAmbient;
	}
	
	public void setDiffuseColor(Color4f c)
	{
		this.colorDiffuse = c;
	}
	
	public void setSpecularColor(Color4f c)
	{
		this.colorSpecular = c;
	}
	
	public void setAmbientColor(Color4f c)
	{
		this.colorAmbient = c;
	}
	
	public void setDiffuseAndAmbientColor(Color4f c)
	{
		this.colorAmbient = c;
		this.colorDiffuse = c;
	}
	
	public void setAllColors(Color4f c)
	{
		this.colorAmbient = c;
		this.colorDiffuse = c;
		this.colorSpecular = c;
	}
	
	public void setAmbientAndSpecularColor(Color4f c)
	{
		this.colorAmbient = c;
		this.colorSpecular = c;
	}
	public void setDiffuseAndSpecularColor(Color4f c)
	{
		this.colorSpecular = c;
		this.colorDiffuse = c;
	}
	
	
	public Color4f getColor()
	{
		return colorDiffuse;
	}

	public float getSpotAngle()
	{
		return spotAngle;
	}

	public void setSpotAngle(float angle)
	{
		this.spotAngle = angle;
	}
	
	public float getLimit()
	{
		return limit;
	}

	public void setLimit(float limit)
	{
		this.limit = limit;
	}

	@Override
	public void onUpdate(float delta)
	{
	}

	@Override
	public void onDestroy()
	{
	}

	@Override
	public void onCreate()
	{
	}
	
	public static void bindInvalid(int progID, int lightIndex)
	{
		int loc = glGetUniformLocation(progID, "light_valid[" + lightIndex + "]");
		GL20.glUniform1i(loc, 0);
	}
	
	public void bind(int progID, int lightIndex)
	{
		Transform world = node.getWorldTransform();
		Vector3f position = world.getPosition();
		Vector3f direction = world.getRotation().direction();
		int loc;
		String arrayLoc = "[" + lightIndex + "]";
		loc = glGetUniformLocation(progID, "light_color_diffuse" + arrayLoc);
		glUniform4f(loc, colorDiffuse.r, colorDiffuse.g, colorDiffuse.b, colorDiffuse.a);
		loc = glGetUniformLocation(progID, "light_color_specular" + arrayLoc);
		glUniform4f(loc, colorSpecular.r, colorSpecular.g, colorSpecular.b, colorSpecular.a);
		loc = glGetUniformLocation(progID, "light_color_ambient" + arrayLoc);
		glUniform4f(loc, colorAmbient.r, colorAmbient.g, colorAmbient.b, colorAmbient.a);
		
		loc = glGetUniformLocation(progID, "light_position" + arrayLoc);
		GL20.glUniform3f(loc, position.x, position.y, position.z);
		loc = glGetUniformLocation(progID, "light_direction" + arrayLoc);
		GL20.glUniform3f(loc, direction.x, direction.y, direction.z);

		loc = glGetUniformLocation(progID, "light_limit" + arrayLoc);
		GL20.glUniform1f(loc, limit);
		loc = glGetUniformLocation(progID, "light_intensity" + arrayLoc);
		GL20.glUniform1f(loc, intensity);
		loc = glGetUniformLocation(progID, "light_spot_angle" + arrayLoc);
		GL20.glUniform1f(loc, spotAngle);
		loc = glGetUniformLocation(progID, "light_spot_exponent" + arrayLoc);
		GL20.glUniform1f(loc, spotExp);
		
		loc = glGetUniformLocation(progID, "light_valid" + arrayLoc);
		GL20.glUniform1i(loc, 1);
	}
	
	public Matrix4f getWorldMatrix()
	{
		return node.getTransform().getTransformMatrix();
	}
}
