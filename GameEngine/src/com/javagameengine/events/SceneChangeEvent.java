package com.javagameengine.events;

import com.javagameengine.scene.Scene;

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
