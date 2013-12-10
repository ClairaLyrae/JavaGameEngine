package com.javagameengine.renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL15.*;

import com.javagameengine.assets.NativeObject;

/**
 * Object encapsulating a query to the GPU for the number of samples drawn during a given set of GPU commands.
 * Queries are called by instantiating a anonymous OcclusionQuery class with overridden query() and onResult()
 * methods. The code inside the query() method corresponds to the GPU commands that are being queried, and the
 * onResult() method is run when the query is complete.
 */
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
	
	/**
	 * Check all pending queries for completeness, and fire the onComplete() methods if they
	 * have a result. Can be called at anytime, however the queries take one or more frames to complete,
	 * and it is useless to 
	 */
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
	
	/**
	 * Abstract method containing the GPU commands to query.
	 */
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
