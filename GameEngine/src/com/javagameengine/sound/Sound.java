package com.javagameengine.sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import com.javagameengine.assets.NativeObject;
import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.math.Vector3f;

public class Sound extends NativeObject
{
	protected float gain;
	protected float pitch = 1.0f;
	protected boolean used = false;
	protected SoundBuffer sound = null;
	protected FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3);
	protected FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3);
	
	public boolean isUsed()
	{
		return used;
	}
	
	public void use()
	{
		used = true;
	}
	
	public void discard()
	{
		this.used = false;
		stop();
	}
	
	public Sound()
	{
		super(Sound.class);
	}
	
	public Sound(SoundBuffer s)
	{
		this();
		sound = s;
		gain = 1f*SoundManager.getGlobalVolume();
	}
	
	public void setSound(SoundBuffer s)
	{
		sound = s;
		AL10.alSourcei(id, AL10.AL_BUFFER, sound.getID());
	}
	
	public void setPitch(float f)
	{
		pitch = f;
		AL10.alSourcef(id, AL10.AL_PITCH, pitch);
	}
	
	public void setGain(float f)
	{
		gain = f * SoundManager.getGlobalVolume();
		AL10.alSourcef(id, AL10.AL_GAIN, gain);
	}
	
	public boolean isPlaying()
	{
		return AL10.alGetSourcei(id, AL10.AL_PLAYING) == AL10.AL_TRUE;
	}
	
	public boolean isLooping()
	{
		return looping;
	}
	
	private boolean looping = false;
	
	public void rewind()
	{
		AL10.alSourceRewind(id);
	}
	
	public void loop()
	{
		AL10.alSourcei(id, AL10.AL_LOOPING, AL10.AL_TRUE);
		AL10.alSourcePlay(id);
		looping = true;
	}
	
	public void play()
	{
		AL10.alSourcei(id, AL10.AL_LOOPING, AL10.AL_FALSE);
		AL10.alSourcePlay(id);
	}
	
	public void stop()
	{
		AL10.alSourceStop(id);
		looping = false;
	}
	
	public void pause()
	{
		AL10.alSourcePause(id);
	}
	
	public void setVelocity(Vector3f vel)
	{
		sourceVel.rewind();
		sourceVel.put(vel.x).put(vel.y).put(vel.z);
		sourceVel.rewind();
		AL10.alSource(id, AL10.AL_VELOCITY, sourceVel);
	}
	
	public void setPosition(Vector3f pos)
	{
		sourcePos.rewind();
		sourcePos.put(pos.x).put(pos.y).put(pos.z);
		sourcePos.rewind();
		AL10.alSource(id, AL10.AL_POSITION, sourcePos);
	}
	
	@Override
	public boolean create()
	{
		// Bind the buffer with the source.
		id = AL10.alGenSources();

		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			throw new IllegalStateException("Could not bind sound source to buffer");
		
		if(sound != null && sound.isLive())
			AL10.alSourcei(id, AL10.AL_BUFFER, sound.getID());
		
		AL10.alSourcef(id, AL10.AL_PITCH, pitch);
		AL10.alSourcef(id, AL10.AL_GAIN, gain);

		// Do another error check and return.
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			throw new IllegalStateException("Could not bind sound source to buffer");
		return true;
	}

	@Override
	public void destroy()
	{
		if(!this.isLive())
			return;
		discard();
		AL10.alDeleteSources(id);
	}
}
