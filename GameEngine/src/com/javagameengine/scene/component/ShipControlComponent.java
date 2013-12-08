package com.javagameengine.scene.component;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyHeldEvent;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseMoveEvent;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Matrix3f;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Component;

public class ShipControlComponent extends Component implements Listener
{
	private PhysicsComponent phys;
	private Camera camera;
	private Vector3f cam_lean = new Vector3f();
	private float damping = 1f;
	
	public ShipControlComponent(PhysicsComponent p, Camera cam)
	{
		phys = p;
		camera = cam;
	}
	
	@EventMethod
	public void onKeyHeld(KeyHeldEvent e)
	{
		if(e.isCancelled())
			return;
		int key = e.getKey();
		Vector3f deltav = new Vector3f();
//		Vector3f deltaw = new Vector3f(0f, 0f, 0f); 
		float incTrans = 2f;
//		float incRot = 2f;
		
		if(key == Keyboard.KEY_E)
			deltav.z += incTrans;
		if(key == Keyboard.KEY_D)
			deltav.z -= incTrans;
		if(key == Keyboard.KEY_S)
			deltav.x += incTrans;
		if(key == Keyboard.KEY_F)
			deltav.x -= incTrans;
//		if(key == Keyboard.KEY_A)	// turn left
//			deltaw.y +=  incRot;
//		if(key == Keyboard.KEY_G)	// turn right
//			deltaw.y -=  incRot;
//		if(key == Keyboard.KEY_W)	// pitch up
//			deltaw.x +=  incRot;
//		if(key == Keyboard.KEY_R)	// pitch down
//			deltaw.x -= incRot;
		
		//cam_lean.add(deltaw.scaleInto(-e.getMillisecondsHeld(), null));
		
		float delta = (float)e.getMillisecondsHeld()/1000f;
//		deltaw.scale(delta);
		deltav.scale(delta);
		
		Matrix3f world_rot = phys.getNode().getWorldTransform().getRotation().toRotationMatrix3f();
		// Rotate the velocity vector to face the ship direction
		deltav = world_rot.multiplyInto(deltav, deltav);
//		deltaw = world_rot.multiplyInto(deltaw, deltaw);
		// Add the velocity to the ship's current velocity
		phys.getLinearVelocity().add(deltav);
//		phys.getAngularVelocity().add(deltaw);
	}
	
	

	Vector3f targetHeading = new Vector3f(0f, 0f, 1f);
	float forwardThrustAccum = 0f;
	float forwardThrustLimit = 5f;
	@Override
	public void onUpdate(float delta)
	{
		
		Vector3f deltav = new Vector3f();
//		Vector3f deltaw = new Vector3f(0f, 0f, 0f); 
		float incTrans = 30f*delta;
//		float incRot = 2f;

		Vector3f v_lin = phys.getLinearVelocity();
		v_lin.scale(1f-delta*4f);
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			deltav.z = incTrans;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			deltav.z = -incTrans;
		if(Keyboard.isKeyDown(Keyboard.KEY_F))
			deltav.x = -incTrans;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			deltav.x = incTrans;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			deltav.y = incTrans;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			deltav.y = -incTrans;
		
		
		if(deltav.z == 0f)
		{
			float scaling = 1f-delta;
			if(scaling < 0f)
				scaling = 0f;
			forwardThrustAccum *= scaling;
		}
		else
			forwardThrustAccum += (forwardThrustLimit - FastMath.abs(forwardThrustAccum))*deltav.z/10f;
		if(forwardThrustAccum > forwardThrustLimit)
			forwardThrustAccum = forwardThrustLimit;
		if(forwardThrustAccum < -forwardThrustLimit)
			forwardThrustAccum = -forwardThrustLimit;
		
		
		Matrix3f world_rot = phys.getNode().getWorldTransform().getRotation().toRotationMatrix3f();
		// Rotate the velocity vector to face the ship direction
		deltav = world_rot.multiplyInto(deltav, deltav);
		// Add the velocity to the ship's current velocity
		v_lin.add(deltav);
		
		
		// -1 to 1 values for mouse position on screen
		float xdelta = (2f*Mouse.getX()/Display.getWidth()) - 1f;
		float ydelta = (2f*Mouse.getY()/Display.getHeight()) - 1f;

		targetHeading.x = -ydelta*1.5f;
		targetHeading.y = -xdelta*1.5f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
			targetHeading.z += 4f*delta;
		else if(Keyboard.isKeyDown(Keyboard.KEY_W))
			targetHeading.z -= 4f*delta;
		else
			targetHeading.z *= 1f-(delta*2f);
		if(targetHeading.z < -1.5f)
			targetHeading.z = -1.5f;
		if(targetHeading.z > 1.5f)
			targetHeading.z = 1.5f;
		
		Transform camerat = camera.getNode().getTransform();
		camerat.getPosition().set(targetHeading.y, 0.5f - 0.5f*targetHeading.x, -4f + 0.1f*forwardThrustAccum);
		
		world_rot.multiplyInto(targetHeading, phys.getAngularVelocity());	
	}

	@Override
	public void onUnlink()
	{
		getScene().getEventManager().unregisterListener(this);
	}

	@Override
	public void onLink()
	{
		getScene().getEventManager().registerListener(this);
	}
}
