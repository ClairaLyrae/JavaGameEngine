package com.javagameengine.assets.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Uniform extends ShaderVariable
{	
	public enum Global 
	{
		VIEW_MATRIX(new Uniform(Dimension.MAT4X4, Format.FLOAT, "v_matrix")),
		PROJECTION_MATRIX(new Uniform(Dimension.MAT4X4, Format.FLOAT, "p_matrix")),
		LIGHT_ARRAY(new Uniform(Dimension.VALUE, Format.FLOAT, "lights"));
		
		final Uniform u;
		private static Map<String, Uniform> handles = new HashMap<String, Uniform>();
		
		private Global(Uniform u)
		{
			this.u = u;
		}
		
		public Uniform get(String name)
		{
			return handles.get(name);
		}
	}
	
	public enum Local
	{
		MODEL_MATRIX,
		NORMAL_MATRIX;
	}
	
	// GLOBAL UNIFORMS: 
	//	VIEW_MAT(4x4), 
	//	PROJ_MAT(4x4),  
	//  FOG(Color/Distance(4f))
		// Global fog. Distance = 0 disables
	//  LIGHT_AMBIENT(4f)
		// Ambient color. Alpha = 0 disables
	//	LIGHT_ARRAY(Position/Limit(4f), Direction/Angle(4f), Color/Intensity(4f))
		// If limit = 0, infinite dist (no falloff)
		// If angle != 0, spotlight
		// If direction = 0, omni
	
	// MODEL UNIFORMS: 
	//	MODEL_MAT(4x4), 
	//	NORM_MAT(3x3)
	
	// MATERIAL UNIFORMS: 
	//	TEXTURES(Sampler2d(), Offset(2f), Scale(2f)),
		// Offset and Scale modify the textures
	//	MATERIAL(Shininess(1f), Color_diff(4f), Color_spec(4f))
		// Shininess is specular
		// Color_diff is diffuse color (use for whatever, w no textures it is model color)
		// Color_spec is for spec colors
	
	
	
	private static Map<String, Uniform> handles = new HashMap<String, Uniform>();
	
	public Uniform(Dimension dim, Format f, String name)
	{
		super(Type.UNIFORM, dim, f, name);
	}
	
	public Uniform(Dimension dim, Format f, int size, String name)
	{
		super(Type.UNIFORM, dim, f, size, name);
	}
	
	public static Uniform getGlobal(String name)
	{
		return handles.get(name);
	}

	private Set<Material> materials = new HashSet<Material>();
	
	public void registerMaterial(Material m)
	{
		materials.add(m);
	}
	
	public void unregisterMaterial(Material m)
	{
		materials.remove(m);
	}
}
