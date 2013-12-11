package com.javagameengine.util;

public class MovingAverage
{
	int i = 0;
	int size = 0;
	float[] values;
	
	public MovingAverage(int size)
	{
		values = new float[size];
	}
	
	public void reset()
	{
		i = 0;
		size = 0;
	}
	
	public void put(float f)
	{
		values[i++] = f;
		if(i >= values.length)
			i = 0;
		if(size < values.length)
			size++;
	}
	
	public float get()
	{
		float av = 0;
		for(int j = 0; j < size; j++)
			av += values[j];
		return av/(float)size;
	}
}
