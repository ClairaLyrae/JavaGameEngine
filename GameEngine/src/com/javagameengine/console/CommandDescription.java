package com.javagameengine.console;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.javagameengine.events.EventMethod.Priority;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDescription
{
	String name() default "";
	
	String[] commandDescriptions() default {""};
	
	String[] commandFormats() default {""};
	
	int minargs() default 0;
}
