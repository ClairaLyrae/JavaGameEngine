package com.javagameengine.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles Events and Listeners. When callEvent is called, this class looks through all of its registered 
 * Listener objects and calls each method that has the annotation @EventClass before it, only has one
 * parameter, and that parameter's type extends Event. 
 * @author ClairaLyrae
 */
public class EventManager
{
	private List<Listener> listeners = new ArrayList<Listener>();
	private HashMap<Class<? extends Event>, HashMap<Listener, Method>> methodCalls = new HashMap<Class<? extends Event>, HashMap<Listener, Method>>();
	
	/**
	 * Registers an object that extends Listener to the EventManager. The Listener class' methods are scanned and if they have
	 * the annotation @EventMethod, only one parameter, and that parameter's type extends Event, then the method is saved along
	 * with the Listener. Once registered, calls to callEvent will result in that Listener's matching methods being called.
	 * @param 	l	Listener to register
	 * @return 		If listener was valid and successfully added.
	 */
	public boolean registerListener(Listener l)
	{
		boolean added = false;	// Keep track of if anything actually changed
		
		// Iterate through all the methods in the listener object
		for(Method m : l.getClass().getMethods())
		{
			// Ignore methods that don't have the @EventMethod annotation
			if(!m.isAnnotationPresent(EventMethod.class))
				continue;
			
			// Check if there is only one parameter and that parameter type extends Event
			Class<?> clazz[] = m.getParameterTypes();
			if(clazz.length != 1)
				continue;
			if(!Event.class.isAssignableFrom(clazz[0]))
				continue;
			@SuppressWarnings("unchecked")
			Class<? extends Event> c = (Class<? extends Event>)clazz[0];
			
			// Add the method and listener object into the tree structure
			if(methodCalls.get(c) == null)
			{
				HashMap<Listener, Method> map = new HashMap<Listener, Method>();
				methodCalls.put(c, map);
			}
			methodCalls.get(c).put(l, m);
			added = true;
		}
		
		// Keep a simple list of the listeners
		if(added)
			listeners.add(l);
		
		return added;
	}
	
	/**
	 * Unregisters a Listener class and removes it from the event system
	 * @param	l	Listener to unregister
	 */
	public void unregisterListener(Listener l)
	{
		// Search through the map structure and remove all references to the Listener
		for(Class<? extends Event> c : methodCalls.keySet())
		{
			HashMap<Listener, Method> methods = methodCalls.get(c);
			if(methods == null)
				continue;
			methods.remove(l);
		}
		listeners.remove(l);
	}
	
	/**
	 * Checks if a listener is registered to this manager.
	 * @param	l	Listener to check
	 * @return		Is listener registered
	 */
	public boolean hasListener(Listener l)
	{
		return listeners.contains(l);
	}
	
	/**
	 * Notifies all registered Listeners that the event has occurred and calls their methods (if any) that match the event.
	 * @param	e	Fires an event to all registered Listeners.
	 */
	public void callEvent(Event e)
	{
		HashMap<Listener, Method> methods = methodCalls.get(e.getClass());
		if(methods == null)
			return;
		for(Listener l : methods.keySet())
		{
			try {
				methods.get(l).invoke(l, e);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				ex.printStackTrace();
			}
		}
	}
}
