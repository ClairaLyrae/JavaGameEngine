package com.javagameengine.events;

import com.javagameengine.scene.Scene;

/**
 * Event describing the current active scene being switched out and a new
 * active scene being loaded
 */
public class SceneChangeEvent extends Event
{
	private Scene s;
	private boolean isNewScene;
	
	public SceneChangeEvent(Scene s, boolean isActive)
	{
		this.s = s;
		this.isNewScene = isActive;
	}
	
	public boolean isNewActiveScene()
	{
		return isNewScene;
	}

	public Scene getScene()
	{
		return s;
	}
}
