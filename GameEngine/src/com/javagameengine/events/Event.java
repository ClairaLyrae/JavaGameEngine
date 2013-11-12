package com.javagameengine.events;

import org.lwjgl.Sys;

/**
 * Base class for events. Events are immutable objects which are given a set of parameters describing the event
 * in their constructors, and are read-only from then on. Provides a timestamp which represents the system time 
 * at the moment the event was fired.
 * @author ClairaLyrae
 */
public abstract class Event
{
	long timestamp;
	protected boolean isCancelled;

	public Event()
	{
		timestamp = Sys.getTime();
	}

	public void cancel()
	{
		isCancelled = true;
	}
	
	public boolean isCancelled()
	{
		return isCancelled;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
}
