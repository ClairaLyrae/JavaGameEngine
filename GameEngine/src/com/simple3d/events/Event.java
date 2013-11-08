package com.simple3d.events;

import org.lwjgl.Sys;

/**
 * Event is a superclass describing an event. All events extend Event. Events
 * are immutable by convention.
 * */
/**
 * @author Chris
 *
 */
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
