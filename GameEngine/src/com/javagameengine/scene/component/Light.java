package com.javagameengine.scene.component;

import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Component;

public class Light extends Component
{
	private float angle = 0.0f;
	private float intensity = 0.0f;
	private float limit = 0.0f;
	protected Color4f color = Color4f.white;
	private boolean shadows = false;
	
	public void setColor(Color4f c)
	{
		this.color = c;
	}
	
	public Color4f getColor()
	{
		return color;
	}

	public float getIntensity()
	{
		return intensity;
	}

	public void setIntensity(float intensity)
	{
		this.intensity = intensity;
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
	public void onUpdate(int delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		
	}
}
