package com.javagameengine.events;

/**
 * Interface for classes that respond to Events handled by the EventManager class.
 * <p> 
 * To create a method that responds to events, the method must respect the following:
 * <ul>
 * <li>The method is annotated by @EventMethod
 * <li>The method has one parameter
 * <li>The parameter's type extends the Event class
 * </ul>
 * <p>
 * The object implementing Listener must then be registered in a suitable EventManager 
 * class. When the EventManager receives an event, it will call the methods in the
 * Listener that satisfy the cases above with the appropriate Event object.
 * @author ClairaLyrae
 */ 
public interface Listener
{

}
