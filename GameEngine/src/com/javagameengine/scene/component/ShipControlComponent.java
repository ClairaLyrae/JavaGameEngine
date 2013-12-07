package com.javagameengine.scene.component;

import org.lwjgl.input.Keyboard;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyHeldEvent;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.events.Listener;
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
		Vector3f deltaw = new Vector3f(0f, 0f, 0f);
		if(key == Keyboard.KEY_E)
			deltav.z += 0.01f;
		if(key == Keyboard.KEY_D)
			deltav.z -= 0.01f;
		if(key == Keyboard.KEY_S)
			deltav.x += 0.01f;
		if(key == Keyboard.KEY_F)
			deltav.x -= 0.01f;
		if(key == Keyboard.KEY_A)	// turn left
			deltaw.y += 0.0001f;
		if(key == Keyboard.KEY_G)	// turn right
			deltaw.y -= 0.0001f;
		if(key == Keyboard.KEY_Q)	// roll left
			deltaw.z += 0.0001f;
		if(key == Keyboard.KEY_T)	// roll right
			deltaw.z -= 0.0001f;
		if(key == Keyboard.KEY_W)	// pitch up
			deltaw.x += 0.0001f;
		if(key == Keyboard.KEY_R)	// pitch down
			deltaw.x -= 0.0001f;
		
		cam_lean.add(deltaw.scaleInto(-e.getMillisecondsHeld(), null));
		
		Matrix3f world_rot = phys.getNode().getWorldTransform().getRotation().toRotationMatrix3f();
		// Rotate the velocity vector to face the ship direction
		deltav = world_rot.multiplyInto(deltav, deltav);
		deltaw = world_rot.multiplyInto(deltaw, deltaw);
		// Add the velocity to the ship's current velocity
		phys.getLinearVelocity().add(deltav);
		phys.getAngularVelocity().add(deltaw);
	}
	
	@Override
	public void onUpdate(int delta)
	{
		// Slowly damp the ship's rotation
		if(Keyboard.isKeyDown(Keyboard.KEY_Y))
			phys.angular_velocity.scale(1f - 1f/(damping*delta));
		if(Keyboard.isKeyDown(Keyboard.KEY_H))
			phys.velocity.scale(1f - 1f/(damping*delta));
		
		float lim = 0.2f;
		
		if(cam_lean.x > lim)
			cam_lean.x = lim;
		if(cam_lean.y > lim)
			cam_lean.y = lim;
		if(cam_lean.z > lim)
			cam_lean.z = lim;
		if(cam_lean.x < -lim)
			cam_lean.x = -lim;
		if(cam_lean.y < -lim)
			cam_lean.y = -lim;
		if(cam_lean.z < -lim)
			cam_lean.z = -lim;
		
		Transform camtram = new Transform();
		camtram.translate(0f, 0.7f, -4f);
		camtram.rotate(cam_lean.magnitude()*5f, cam_lean.x - 0.1f, cam_lean.y, cam_lean.z);
		camera.getNode().getTransform().set(camtram);
		cam_lean.scale(1f - 1f/(4f*delta));
		
	}

	@Override
	public void onDestroy()
	{
		getScene().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		getScene().getEventManager().registerListener(this);
	}
}
