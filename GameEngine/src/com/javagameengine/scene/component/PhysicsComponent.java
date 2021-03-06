package com.javagameengine.scene.component;

import java.util.HashSet;
import java.util.Set;

import com.javagameengine.events.CollisionEvent;
import com.javagameengine.math.Quaternion;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Bounded;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

public class PhysicsComponent extends Component
{		
	public static Vector3f gravity = new Vector3f(0f,-0.2f,0f);
	public static Set<PhysicsComponent> collidable = new HashSet<PhysicsComponent>();
	
	public static void calculateCollisions()
	{
		for(PhysicsComponent phys : collidable)
			phys.collideWithAll();
		for(PhysicsComponent phys : collidable)
			phys.isChecked = false;
	}
	
	Vector3f velocity = new Vector3f(0f);
	Vector3f angular_velocity = new Vector3f(0f);
	
	private boolean isCollidable = false;
	private boolean isChecked = false;
	
	float mass;
	private float radius;
	private float elasticity = 1f;
	
	public PhysicsComponent(float mass, float radius)
	{
		this(mass,  false, radius);
	}
	
	public PhysicsComponent(float mass, boolean collision, float radius)
	{
		this(mass, 1f, collision, radius);
	}
	
	public PhysicsComponent(float mass, float elasticity, boolean collision, float radius)
	{
		this.elasticity = elasticity;
		this.radius = radius;
		this.mass = mass;
		if(collision)
			setCollidable(true);
	}
	
	public boolean isColliding(PhysicsComponent p)
	{
		if(this.getScene() != p.getScene())
			return false;
		if(!isCollidable() || !p.isCollidable())
			return false;
		float r1 = radius;
		float r2 = p.getRadius();
		Vector3f p1 = getNode().getWorldTransform().getPosition();
		Vector3f p2 = p.getNode().getWorldTransform().getPosition();

		// Check if collision happened
		if(p1.subtractInto(p2, null).magnitudeSquared() < (r1+r2)*(r1+r2))
			return false;
		return true;
	}
	
	/**
	 * Checks for collision and calculates results for the given PhysicsComponent
	 * @param Object to check and do collision with
	 * @return True if collision occured
	 */
	
	private Vector3f para1 = new Vector3f();
	private Vector3f perp1 = new Vector3f();
	private Vector3f para2 = new Vector3f();
	private Vector3f perp2 = new Vector3f();
	private Vector3f normal = new Vector3f();
	
	public boolean collideWith(PhysicsComponent collider)
	{
		if(collider == this)
			return false;
		if(this.getScene() != collider.getScene())
			return false;
		if(!isCollidable() || !collider.isCollidable())
			return false;
		// Get positions (referenced to world)
		Vector3f p1 = getNode().getWorldTransform().getPosition();
		Vector3f p2 = collider.getNode().getWorldTransform().getPosition();
		
		// Get radiuses
		float r1 = radius;
		float r2 = collider.getRadius();

		// Find normal (Vector between objects)
		p2.subtractInto(p1, normal);
		
		// Check if collision happened
		if(normal.magnitudeSquared() >= (r1+r2)*(r1+r2))
			return false;

		// Get velocities
		Vector3f v1 = velocity;
		Vector3f v2 = collider.getLinearVelocity();
		
		// Ignore collision on objects moving away from each other
		if(v1.subtractInto(v2, null).dot(normal) < 0f)
			return false;
		
		normal.normalize();
		
		// We have a collision, call event and ignore collision if it gets cancelled
		CollisionEvent event = new CollisionEvent(this, collider, 0f);
	    scene.getEventManager().callEvent(event);
	    if(event.isCancelled())
	    	return false;
		
		// Get masses
		float m1 = mass;
		float m2 = collider.getMass();
		float msum = m1 + m2;
		float e1 = elasticity;
		float e2 = collider.getElasticity();
		
		normal.scaleInto(normal.dot(v1), para1);
		v1.subtractInto(para1, perp1);
		normal.scaleInto(normal.dot(v1), para2);
		v1.subtractInto(para2, perp2);
		para1.scaleInto((m1-e1*m2), null).add(para2.scaleInto(((e1 + 1f)*m2), null)).scale(1f/msum).addInto(perp1, v1);
		para2.scaleInto((m2-e2*m1), null).add(para1.scaleInto(((e2 + 1f)*m1), null)).scale(1f/msum).addInto(perp2, v2);
		
//		// Find components of velocities parallel and perpendicular to the normal
//		Vector3f v1para = normal.scaleInto(normal.dot(v1), null);
//		Vector3f v2para = normal.scaleInto(normal.dot(v2), null);
//		Vector3f v1perp = v1.subtractInto(v1para, null);
//		Vector3f v2perp = v2.subtractInto(v2para, null);
//		
//		// Compute new velocities
//		Vector3f u1 = v1para.scaleInto((m1-e1*m2), null).add(v2para.scaleInto(((e1 + 1f)*m2), null)).scale(1f/msum).addInto(v1perp, null);
//		Vector3f u2 = v2para.scaleInto((m2-e2*m1), null).add(v1para.scaleInto(((e2 + 1f)*m1), null)).scale(1f/msum).addInto(v2perp, null);
//
////	    System.out.println(
////	    		"p1p2:" + normal + 
////	    		"\nv1par:" + v1para + 
////	    		"\nv1perp:" + v1perp +
////	    		"\nv2par:" + v2para + 
////	    		"\nv2perp:" + v2perp + 
////	    		"\nv1par+perp:" + v1para.addInto(v1perp, null) + 
////	    		"\nv2par+perp:" + v2para.addInto(v2perp, null) + 
////	    		"\nV1:" + v1 + 
////	    		"\nV2:" + v2 +
////	    		"\nU1:" + u1 +
////	    		"\nU2:" + u2 +
////	    		"\nEnergy In: " + (v1.magnitudeSquared()*0.5f*m1 + v2.magnitudeSquared()*0.5f*m2) +
////	    		"\nEnergy Out: " + (u1.magnitudeSquared()*0.5f*m1 + u2.magnitudeSquared()*0.5f*m2));
//	    v1.set(u1);
//	    v2.set(u2);
		return true;
	}
	
	/**
	 * Checks for collision and calculates results between all other valid colliders.
	 * @return True if a collision occured
	 */
	public boolean collideWithAll()
	{
		boolean result = false;
		for(PhysicsComponent p : collidable)
		{
			if(p.isChecked)
				continue;
			if(collideWith(p))
				result = true;
		}
		isChecked = true;
		return result;
	}
	
	public Vector3f getAngularVelocity()
	{
		return angular_velocity;
	}
	
	public float getElasticity()
	{
		return elasticity;
	}
	
	public Vector3f getLinearVelocity()
	{
		return velocity;
	}
	
	public float getMass()
	{
		return mass;
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	public boolean isCollidable()
	{
		return isCollidable;
	}
	
	@Override
	public void onUpdate(float delta)
	{
		Transform t = getNode().getTransform();
		if(velocity.magnitudeSquared() != 0f)
			t.translate(velocity.x*delta, velocity.y*delta, velocity.z*delta);
		if(angular_velocity.magnitudeSquared() != 0f)
		{
			Vector3f av_norm = angular_velocity.normalizeInto(null);
			t.rotate(angular_velocity.magnitude()*delta, av_norm.x, av_norm.y, av_norm.z);
		}
		//Vector3f deltagrav = gravity.scaleInto(1f/delta, null);
		//velocity.add(deltagrav);
	}
	
	public void setCollidable(boolean b)
	{
		if(b)
		{
			isCollidable = true;
			collidable.add(this);
		}
		else
		{
			isCollidable = false;
			collidable.remove(this);
		}
	}

	public void setElasticity(float e)
	{
		elasticity = e;
	}

	public void setMass(float mass)
	{
		this.mass = mass;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}
	
	@Override
	public void onLink()
	{
		
	}
	
	@Override
	public void onUnlink()
	{
		if(isCollidable)
			collidable.remove(this);
	}
	
	@Override
	public void onActivate()
	{
	}

	@Override
	public void onDeactivate()
	{
	}
}
