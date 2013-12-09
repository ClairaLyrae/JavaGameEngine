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
import static org.lwjgl.opengl.GL11.GL_ZERO;

import org.lwjgl.opengl.GL11;

public class RendererState
{
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
	
	protected BlendMode blendSource = BlendMode.ONE;
	protected BlendMode blendDest = BlendMode.ONE;
	protected boolean isBlendEnabled = false;
	protected boolean isColorWriteEnabled = true;
	protected boolean isDepthWriteEnabled = true;
	protected boolean isDepthTestEnabled = true;
	protected boolean isFixedFunctionEnabled = false;
	protected int programID = -1;
	
	public void enableState(RendererState prev)
	{
		if(prev.isBlendEnabled != isBlendEnabled)
		{
			if(isBlendEnabled)
				GL11.glEnable(GL11.GL_BLEND);
			else
				GL11.glDisable(GL11.GL_BLEND);
		}
		if(prev.isDepthWriteEnabled != isDepthWriteEnabled)
			GL11.glDepthMask(isDepthWriteEnabled);
		if(prev.isColorWriteEnabled != isColorWriteEnabled)
			GL11.glColorMask(isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled, isColorWriteEnabled);
		if(prev.isDepthTestEnabled != isDepthTestEnabled)
		{
			
		}
	}
	
	public void setProgram(int id)
	{
		programID = id;
	}
	
	public boolean isColorWriteEnabled()
	{
		return isColorWriteEnabled;
	}

	public void enableColorWrite(boolean state)
	{
		if(isColorWriteEnabled == state)
			return;
		isColorWriteEnabled = state;
		GL11.glColorMask(state, state, state, state);
	}

	public boolean isDepthWriteEnabled()
	{
		return isDepthWriteEnabled;
	}

	public void enableDepthWrite(boolean state)
	{
		if(isDepthWriteEnabled == state)
			return;
		isDepthWriteEnabled = state;
		GL11.glDepthMask(state);
	}

	public boolean isDepthTestEnabled()
	{
		return isDepthTestEnabled;
	}

	public void enableDepthTest(boolean state)
	{
		isDepthTestEnabled = state;
	}
}
