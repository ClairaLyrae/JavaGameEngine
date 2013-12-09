package com.javagameengine.scene.component;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.gui.HUD;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

public class LaserComponent extends Component implements Listener
{
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		if(e.isCancelled() || e.getButton() != 0 || !e.state())
			return;
		if(HUD.laserOutOfPower)
			return;
		Node n = new Node("laserShot");
		scene.getRoot().addChild(n);
		n.getTransform().set(node.getWorldTransform());
		n.addComponent(new LaserShot());
	}
	
	@Override
	public void onUnlink()
	{
		scene.getEventManager().unregisterListener(this);
	}

	@Override
	public void onLink()
	{
		scene.getEventManager().registerListener(this);
	}
}
