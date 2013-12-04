package com.javagameengine.renderer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.javagameengine.math.Color4f;
import com.javagameengine.math.Matrix4f;

// Basically stores render layer queue
public class RenderQueue
{	
	private Map<Bindable, List<Renderable>> queue = new HashMap<Bindable, List<Renderable>>();
	
	public void render()
	{
		for(Bindable b : queue.keySet())
		{
			int progID = -1;
			if(b != null)
				progID = b.bind();
			List<Renderable> list = queue.get(b);
			if(list == null)
				continue;
			for(Renderable r : list)
			{
				Matrix4f transform = r.getMatrix();
				if(progID < 0)
				{
					GL20.glUseProgram(0);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glPushMatrix();
					GL11.glMultMatrix(transform.toBuffer());
					r.getDrawable().draw();
					GL11.glPopMatrix();
				} else {
					Matrix4f modelviewtemp = Renderer.view_matrix.multiplyInto(transform, null);

					FloatBuffer MV_buffer2 = modelviewtemp.toBuffer();
					int loc = glGetUniformLocation(progID, "mv");
					glUniformMatrix4(loc, false, MV_buffer2);

					FloatBuffer M_buffer = transform.toBuffer();
					loc = glGetUniformLocation(progID, "m");
					glUniformMatrix4(loc, false, M_buffer);

					FloatBuffer V_buffer = Renderer.view_matrix.toBuffer();
					loc = glGetUniformLocation(progID, "v");
					glUniformMatrix4(loc, false, V_buffer);
					
					FloatBuffer P_buffer = Renderer.projection_matrix.toBuffer();
					loc = glGetUniformLocation(progID, "p");
					glUniformMatrix4(loc, false, P_buffer);
					
					Color4f lightcol = Renderer.light.getColor();
					loc = glGetUniformLocation(progID, "light_diffuse");
					glUniform4f(loc, lightcol.r, lightcol.g, lightcol.b, lightcol.a);
					loc = glGetUniformLocation(progID, "light_position");
					glUniform4f(loc, 000f, 000f, 00f, 1f);
					
					r.getDrawable().draw();
				}
			}
		}
	}
	
	public void queue(Renderable r)
	{
		List<Renderable> list = queue.get(r.getBindable());
		if(list == null)
			queue.put(r.getBindable(), list = new ArrayList<Renderable>());
		list.add(r);
	}
	
	public void clearQueue()
	{
		queue.clear();
	}
}
