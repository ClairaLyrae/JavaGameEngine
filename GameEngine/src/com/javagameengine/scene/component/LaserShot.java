package com.javagameengine.scene.component;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.gui.LaserCountText;
import com.javagameengine.math.Color4f;
import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;
import com.javagameengine.sound.SoundManager;
import com.javagameengine.sound.Sound;

public class LaserShot extends Component
{
	private static Material laserMaterial;
	private static Mesh laserMesh;
	private static SoundBuffer sound;
	
	public LaserShot()
	{
		if(laserMaterial == null)
			laserMaterial = AssetManager.getMaterial("laser");
		if(laserMesh == null)
			laserMesh = AssetManager.getMesh("laser");
		if(sound == null)
			sound = AssetManager.getSound("laser2");
	}

	@Override
	public void onActivate()
	{
		Light light = new Light();
		light.setDiffuseColor(Color4f.cyan);
		light.setLimit(4f);
		light.setIntensity(5f);
		light.setUsage(Light.Usage.TRANSIENT);
		
		PhysicsComponent phys = new PhysicsComponent(0f, false, 0f);
		phys.getLinearVelocity().set(node.getTransform().getRotation().toRotationMatrix3f().multiplyInto(new Vector3f(0f, 0f, 40f), null));
		
		node.addComponent(new MeshRenderer(laserMaterial, laserMesh));
		node.addComponent(light);
		node.addComponent(phys);
		node.markAsTransient(4f);

		SoundManager.play(sound, 0.05f);
		LaserCountText.increase();
	}

	@Override
	public void onDeactivate()
	{
	}
}
