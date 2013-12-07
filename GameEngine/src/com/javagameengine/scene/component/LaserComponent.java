package com.javagameengine.scene.component;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

public class LaserComponent extends Component implements Listener
{
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		if(e.isCancelled() || e.getButton() != 0 || !e.state())
			return;
		Node n = new Node("laserShot");
		scene.getRoot().addChild(n);
		n.getTransform().set(node.getWorldTransform());
		n.addComponent(new LaserShot());
	}
	
	@Override
	public void onUpdate(float deltaf)
	{
	}

	@Override
	public void onDestroy()
	{
		scene.getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		scene.getEventManager().registerListener(this);
	}

}
