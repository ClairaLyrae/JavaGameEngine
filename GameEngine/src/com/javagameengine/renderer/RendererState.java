package com.javagameengine.renderer;

import static org.lwjgl.opengl.GL11.GL_DST_ALPHA;
import static org.lwjgl.opengl.GL11.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_COLOR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11.*;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

public class RendererState implements Comparable<RendererState>
{
	public enum DepthFunc
	{
		ALWAYS(GL_ALWAYS), // Always passes
		NEVER(GL_NEVER),	// Never passes.
		LESS(GL_LESS),	// Passes if the incoming depth value is less than the stored depth value.
		EQUAL(GL_EQUAL),	// Passes if the incoming depth value is equal to the stored depth value.
		LESS_OR_EQUAL(GL_LEQUAL),	// Passes if the incoming depth value is less than or equal to the stored depth value.
		GREATER(GL_GREATER),	// Passes if the incoming depth value is greater than the stored depth value.
		NOT_EQUAL(GL_NOTEQUAL),	// Passes if the incoming depth value is not equal to the stored depth value.
		GREATER_OR_EQUAL(GL_GEQUAL);	// Passes if the incoming depth value is greater than or equal to the stored depth value.
		
		private int param;
		
		private DepthFunc(int param)
		{
			this.param = param;
		}
		
		public int getGLParam()
		{
			return param;
		}
	}
	
	public enum BlendMode
	{
		ZERO(GL_ZERO),
		ONE(GL_ONE),
		ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA),
		DST_ALPHA(GL_DST_ALPHA),
		ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
		DST_COLOR(GL_DST_COLOR),
		ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
		SRC_ALPHA(GL_SRC_ALPHA),
		ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
		SRC_COLOR(GL_SRC_COLOR);
		
		private int param;
		
		private BlendMode(int param)
		{
			this.param = param;
		}
		
		public int getGLParam()
		{
			return param;
		}
	}
	
	public DepthFunc depthFunc = DepthFunc.LESS_OR_EQUAL;
	public BlendMode blendSource = BlendMode.SRC_ALPHA;
	public BlendMode blendDest = BlendMode.ONE_MINUS_SRC_ALPHA;
	public boolean isBlendEnabled = true;
	public boolean isColorWriteEnabled = true;
	public boolean isDepthWriteEnabled = true;
	public boolean isDepthTestEnabled = true;
	public boolean isFixedFunctionEnabled = true;
	public boolean isNormalized = true;
	public int programID = -1;
	public int layerID = 0;
	
	public static final RendererState defaultState = new RendererState();

	@Override
	public int compareTo(RendererState state)
	{
		if(isBlendEnabled != state.isBlendEnabled)
			return isBlendEnabled ? 1 : -1;
		if(layerID != state.layerID)
			return layerID < state.layerID ? 1 : -1;
		if(programID == state.programID)
		{
			return 0;
		}
		return programID < state.programID ? 1 : -1;
		// We do this all in layers, so layer is most important
		
		// First we render meshes, ideally front to back (so culling works well)
		
		// Then we render transparent meshes, ideally back to front (so they can overlap correctly)
		
	}
	
	public RendererState clone()
	{
		RendererState newstate = new RendererState();
		newstate.depthFunc = depthFunc;
		newstate.blendSource = blendSource;
		newstate.blendDest = blendDest ;
		newstate.isBlendEnabled = isBlendEnabled;
		newstate.isColorWriteEnabled = isColorWriteEnabled;
		newstate.isDepthWriteEnabled = isDepthWriteEnabled;
		newstate.isDepthTestEnabled = isDepthTestEnabled;
		newstate.isFixedFunctionEnabled = isFixedFunctionEnabled;
		newstate.isNormalized = isNormalized;
		newstate.programID = programID;
		return newstate;
	}
	
	public static RendererState createRendererState()
	{
		return defaultState.clone();
	}
	
	public RendererState()
	{
	}
	
	public void enableState()
	{
		if(isBlendEnabled)
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(blendSource.getGLParam(), blendDest.getGLParam());
		}
		else
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(isDepthWriteEnabled);
			GL11.glColorMask(isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled);
		if(isDepthTestEnabled)
		{
			GL11.glEnable(GL_DEPTH_TEST);
			GL11.glDepthFunc(depthFunc.getGLParam());
		}
		else
			GL11.glDisable(GL_DEPTH_TEST);
		if(isNormalized)
			GL11.glEnable(GL_NORMALIZE);
		else
			GL11.glDisable(GL_NORMALIZE);
	}
	
	public void switchState(RendererState prev)
	{
		if(prev.isBlendEnabled != isBlendEnabled)
		{
			if(isBlendEnabled)
				GL11.glEnable(GL11.GL_BLEND);
			else
				GL11.glDisable(GL11.GL_BLEND);
		}
		if(isBlendEnabled)
		{
			if(prev.blendSource != blendSource || prev.blendDest != blendDest)
				GL11.glBlendFunc(blendSource.getGLParam(), blendDest.getGLParam());
		}
		if(prev.isDepthWriteEnabled != isDepthWriteEnabled)
			GL11.glDepthMask(isDepthWriteEnabled);
		if(prev.isColorWriteEnabled != isColorWriteEnabled)
			GL11.glColorMask(isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled);
		if(prev.isDepthTestEnabled != isDepthTestEnabled)
		{
			if(isDepthTestEnabled)
				GL11.glEnable(GL_DEPTH_TEST);
			else
				GL11.glDisable(GL_DEPTH_TEST);
		}
		if(isDepthTestEnabled)
		{
			if(depthFunc != prev.depthFunc)
				GL11.glDepthFunc(depthFunc.getGLParam());
		}
		if(prev.isNormalized != isNormalized)
		{
			if(isNormalized)
				GL11.glEnable(GL_NORMALIZE);
			else
				GL11.glDisable(GL_NORMALIZE);
		}
	}
}
