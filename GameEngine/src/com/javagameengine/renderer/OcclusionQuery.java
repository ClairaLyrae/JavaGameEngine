package com.javagameengine.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;

import com.javagameengine.assets.NativeObject;

public abstract class OcclusionQuery extends NativeObject
{
	private static List<OcclusionQuery> pending = new ArrayList<OcclusionQuery>();
	
	public OcclusionQuery()
	{
		super(OcclusionQuery.class);
		create();
		GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, id);
		query();
		glEndQuery(GL15.GL_SAMPLES_PASSED);
		pending.add(this);
	}
	
	public static void updateQueries()
	{
		Iterator<OcclusionQuery> itr = pending.iterator();
		while(itr.hasNext())
		{
			OcclusionQuery q = itr.next();
			if(q.poll())
			{
				q.onComplete(q.getResult());
				q.destroy();
				itr.remove();
			}
		}
	}
	
	public abstract void query();
	
	public abstract void onComplete(int samplesPassed);
	
	protected boolean poll()
	{
		return GL15.glGetQueryObjecti(id, GL_QUERY_RESULT_AVAILABLE) != GL11.GL_FALSE;
	}
	
	protected int getResult()
	{
		return GL15.glGetQueryObjecti(id, GL_QUERY_RESULT)/Renderer.getNumSamples();
	}
	
	@Override
	public boolean create()
	{
		id = GL15.glGenQueries();
		return id != -1;
	}

	@Override
	public void destroy()
	{
		GL15.glDeleteQueries(id);
	}
}
