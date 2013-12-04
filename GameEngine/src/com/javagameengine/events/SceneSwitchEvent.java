package com.javagameengine.events;

import com.javagameengine.scene.Scene;

public class SceneSwitchEvent extends Event
{
	private Scene oldest;
	private Scene newest;
	
	public SceneSwitchEvent(Scene oldest, Scene newest)
	{
		this.oldest = oldest;
		this.newest = newest;
	}

	public Scene getNewest()
	{
		return newest;
	}

	public Scene getOldest()
	{
		return oldest;
	}
}
