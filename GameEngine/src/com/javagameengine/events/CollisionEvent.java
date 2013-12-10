package com.javagameengine.events;

import com.javagameengine.scene.component.PhysicsComponent;

/**
 * Event describing two colliding objects.
 */
public class CollisionEvent extends Event
{
	private PhysicsComponent a;
	private PhysicsComponent b;
	private float inertia;
	
	public CollisionEvent(PhysicsComponent a, PhysicsComponent b, float inertia)
	{
		this.a = a;
		this.b = b;
		this.inertia = inertia;
	}
	
	public float getInertia()
	{
		return inertia;
	}
	
	public PhysicsComponent getColliderA()
	{
		return a;
	}
	
	public PhysicsComponent getColliderB()
	{
		return b;
	}
	
	public String toString()
	{
		return String.format("CollisionEvent");
	}
}
