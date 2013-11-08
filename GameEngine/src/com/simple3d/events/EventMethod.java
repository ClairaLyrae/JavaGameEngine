package com.simple3d.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod
{
	public enum Priority{
		HIGH, MED, LOW;
	}

	Priority value() default Priority.LOW;
}
