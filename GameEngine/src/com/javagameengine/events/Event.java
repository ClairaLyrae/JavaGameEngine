package com.javagameengine.events;

import org.lwjgl.Sys;

/**
 * Base class for events. Events are immutable objects which are given a set of parameters describing the event
 * in their constructors, and are read-only from then on. Provides a timestamp which represents the system time 
 * at the moment the event was fired.
 */
public abstract class Event
{
	protected long timestamp;
	protected boolean isCancelled;

	public Event()
	{
		timestamp = Sys.getTime();
	}

	/**
	 * Cancels the event. May not necessarily have an effect on the event depending on the type. However,
	 * listeners can query isCancelled to determine if they should act on the event.
	 */
	public void cancel()
	{
		isCancelled = true;
	}
	
	/**
	 * @return True if this event is cancelled
	 */
	public boolean isCancelled()
	{
		return isCancelled;
	}
	
	/**
	 * @return The value of Sys.getTime() at the creation of this event
	 */
	public long getTimestamp()
	{
		return timestamp;
	}
}
