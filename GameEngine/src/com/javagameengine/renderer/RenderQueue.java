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
import com.javagameengine.scene.component.Light;

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
			for(final Renderable r : list)
			{
				Matrix4f transform = r.getMatrix();
				Matrix4f modelviewtemp = Renderer.view_matrix.multiplyInto(transform, null);
				FloatBuffer MV_buffer2 = modelviewtemp.toBuffer();
				FloatBuffer M_buffer = transform.toBuffer();
				
				GL11.glPushMatrix();
				GL11.glMultMatrix(modelviewtemp.transposeInto(null).toBuffer());
				
				if(progID < 0)
				{
					GL20.glUseProgram(0);
				} else {

					int loc = glGetUniformLocation(progID, "mv");
					glUniformMatrix4(loc, false, MV_buffer2);

					loc = glGetUniformLocation(progID, "m");
					glUniformMatrix4(loc, false, M_buffer);

					loc = glGetUniformLocation(progID, "v");
					glUniformMatrix4(loc, false, Renderer.view_matrix_buffer);
					Renderer.view_matrix_buffer.rewind();
					
					loc = glGetUniformLocation(progID, "p");
					glUniformMatrix4(loc, false, Renderer.projection_matrix_buffer);
					Renderer.projection_matrix_buffer.rewind();
					
					for(int i = 0; i < Renderer.MAX_LIGHTS; i++)
					{
						if(Renderer.lights[i] == null)
							Light.bindInvalid(progID, i);
						else
							Renderer.lights[i].bind(progID,i);
					}
				}
				if(r.onRender());
					r.getDrawable().draw();
				GL11.glPopMatrix();
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
