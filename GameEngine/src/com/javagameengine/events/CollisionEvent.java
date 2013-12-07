package com.javagameengine.events;

import com.javagameengine.scene.component.PhysicsComponent;

/**
 * Event describing a mouse button being clicked.
 * @author ClairaLyrae
 */
public class CollisionEvent extends Event
{
	private PhysicsComponent a;
	private PhysicsComponent b;
	private float force;
	
	public CollisionEvent(PhysicsComponent a, PhysicsComponent b, float force)
	{
		this.a = a;
		this.b = b;
		this.force = force;
	}
	
	public float getForce()
	{
		return force;
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
