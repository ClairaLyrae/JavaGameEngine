package com.simple3d.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EventManager
{		
	public EventManager()
	{
		
	}
	
	private List<Listener> listeners = new ArrayList<Listener>();
	private HashMap<Class<? extends Event>, HashMap<Listener, Method>> methodCalls = new HashMap<Class<? extends Event>, HashMap<Listener, Method>>();
	
	public boolean registerListener(Listener l)
	{
		boolean added = false;
		for(Method m : l.getClass().getMethods())
		{
			if(!m.isAnnotationPresent(EventMethod.class))
				continue;
			Class<?> clazz[] = m.getParameterTypes();
			if(clazz.length != 1)
				continue;
			if(!Event.class.isAssignableFrom(clazz[0]))
				continue;
			@SuppressWarnings("unchecked")
			Class<? extends Event> c = (Class<? extends Event>)clazz[0];
			if(methodCalls.get(c) == null)
			{
				HashMap<Listener, Method> map = new HashMap<Listener, Method>();
				methodCalls.put(c, map);
			}
			methodCalls.get(c).put(l, m);
			added = true;
		}
		if(added)
			listeners.add(l);
		return added;
	}
	
	public void unregisterListener(Listener l)
	{
		for(Class<? extends Event> c : methodCalls.keySet())
		{
			HashMap<Listener, Method> methods = methodCalls.get(c);
			if(methods == null)
				continue;
			methods.remove(l);
		}
		listeners.remove(l);
	}
	
	public boolean hasListener(Listener l)
	{
		return listeners.contains(l);
	}
	
	public void callEvent(Event e)
	{
		System.out.println("Calling event: " + e.toString());
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
