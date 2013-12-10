package com.javagameengine.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for Listener class event methods. Methods annotated with this that are inside classes which
 * implement Listener will be called when the event is fired. 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod
{
	/**
	 * Event priority that determines in what order this event method is called. MONITOR priority event methods are 
	 * called last, and are meant for methods that need to be called after everything else has happened.
	 */
	public enum Priority{
		BLOCKING, HIGH, MED, LOW, MONITOR;
	}

	Priority value() default Priority.LOW;
}
