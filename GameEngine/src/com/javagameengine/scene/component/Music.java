package com.javagameengine.scene.component;

import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.scene.Component;
import com.javagameengine.sound.SoundManager;
import com.javagameengine.sound.Sound;

public class Music extends Component
{
	private SoundBuffer sound;
	private Sound source;
		
	public Music(SoundBuffer s)
	{
		sound = s;
	}
	
	public SoundBuffer getSound()
	{
		return sound;
	}
	
	public void setSound(SoundBuffer s)
	{
		sound = s;
		source.setSound(s);
	}

	private float fadeTime = 1f;
	private float fadeTimeCounter = 0f;
	private boolean fadeDir = false;
	
	public void fadeIn(float time)
	{
		fadeDir = true;
		fadeTime = time;
		fadeTimeCounter = 0f;
	}

	public void fadeOut(float time)
	{
		fadeDir = false;
		fadeTime = time;
		fadeTimeCounter = 0f;
	}
	
	@Override
	public void onUpdate(float deltaf)
	{
		if(fadeTime > 0f)
		{
			fadeTimeCounter += deltaf;
			if(fadeDir)
				source.setGain((fadeTimeCounter/fadeTime));
			else
				source.setGain((1f - (fadeTimeCounter/fadeTime)));
			if(fadeTimeCounter >= fadeTime)
			{
				fadeTimeCounter = 0f;
				fadeTime = 0f;
			}
		}
	}
	
	@Override
	public void onActivate()
	{
		source = SoundManager.getSource();
		if(sound != null)
			source.setSound(sound);
		source.use();
		fadeIn(10f);
		source.setGain(0f);
		source.loop();
	}

	@Override
	public void onDeactivate()
	{
		source.discard();
	}
}
