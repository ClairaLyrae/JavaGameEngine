package com.javagameengine.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javagameengine.Game;
import com.javagameengine.scene.Scene;

/**
 * Handles Events and Listeners. When callEvent is called, this class looks through all of its registered 
 * Listener objects and calls each method that has the annotation @EventMethod before it, only has one
 * parameter, and that parameter's type extends Event. 
 */
public class EventManager
{
	private boolean ignoreEvents = false;
	public static final EventManager global = new EventManager();
	
	private List<EventManager> subManagers = new ArrayList<EventManager>();
	private List<Listener> listeners = new ArrayList<Listener>();
	private HashMap<Class<? extends Event>, HashMap<Listener, Method>> methodCalls = new HashMap<Class<? extends Event>, HashMap<Listener, Method>>();
	
	public EventManager()
	{
	}

	/**
	 * @return True if this manager is ignoring events
	 */
	public boolean isIgnoringEvents()
	{
		return ignoreEvents;
	}
	
	/**
	 * @param state True if manager should ignore called events
	 */
	public void ignoreEvents(boolean state)
	{
		ignoreEvents = state;
	}
	
	/**
	 * @return List of all EventManagers connected with this EventManager
	 */
	public List<EventManager> getSubManagers()
	{
		return subManagers;
	}
	
	/**
	 * @param em EventManager to register
	 */
	public void registerEventManager(EventManager em)
	{
		subManagers.add(em);
	}
	
	/**
	 * @param em EventManager to unregister
	 * @return True if the given EventManager was unregistered
	 */
	public boolean unregisterEventManager(EventManager em)
	{
		return subManagers.remove(em);
	}
	
	/**
	 * @param em EventManager to check
	 * @return True if given EventManager is registered to this manager
	 */
	public boolean hasEventManager(EventManager em)
	{
		return subManagers.contains(em);
	}
	
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
		List<Class<? extends Event>> list = new ArrayList<Class<? extends Event>>();
		for(Class<? extends Event> c : methodCalls.keySet())
		{
			HashMap<Listener, Method> methods = methodCalls.get(c);
			methods.remove(l);
			if(methods.isEmpty())
				list.add(c);
		}
		for(Class<? extends Event> c : list)
			methodCalls.remove(c);
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
		if(!ignoreEvents)
		{
			Map<Listener, Method> methods = methodCalls.get(e.getClass());
			if(methods != null)
			{
				Map<Listener, Method> methodsCopy = new HashMap<Listener, Method>();
				for(Listener l : methods.keySet())
					methodsCopy.put(l, methods.get(l));
				for(Listener l : methodsCopy.keySet())
				{
					try {
						methodsCopy.get(l).invoke(l, e);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		for(EventManager em : subManagers)
			em.callEvent(e);
		if(this == global)
		{
			Scene s = Game.getHandle().getActiveScene();
			if(s != null)
				s.getEventManager().callEvent(e);
		}
	}
	
	public String toString()
	{
		return "eventManager=[eventClasses=" + methodCalls.size() + "], listeners=[" + listeners.size() + "]";
	}
}
