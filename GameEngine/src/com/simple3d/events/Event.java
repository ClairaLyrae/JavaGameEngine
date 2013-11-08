package com.simple3d.events;

import org.lwjgl.Sys;

public abstract class Event
{
	long timestamp;
	
	public Event()
	{
		timestamp = Sys.getTime();
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
}
