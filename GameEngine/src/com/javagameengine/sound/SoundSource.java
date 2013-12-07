package com.javagameengine.sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import com.javagameengine.assets.NativeObject;
import com.javagameengine.assets.audio.Sound;
import com.javagameengine.math.Vector3f;

public class SoundSource extends NativeObject
{
	private boolean loop = false;
	private float gain = 1.0f;
	private float pitch = 1.0f;
	private boolean used = false;
	
	/** Position of the source sound. */
	FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3);

	/** Velocity of the source sound. */
	FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3);

	private Sound sound = null;
	
	public boolean isUsed()
	{
		return used;
	}
	
	public void setUsed(boolean state)
	{
		this.used = state;
		if(!used)
			stop();
	}
	
	public SoundSource()
	{
		super(SoundSource.class);
		sourcePos.put(0f).put(0f).put(0f);
		sourceVel.put(0f).put(0f).put(0f);
		sourcePos.flip();
		sourceVel.flip();
	}
	
	public SoundSource(Sound s)
	{
		this();
		sound = s;
	}
	
	public void setSound(Sound s)
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
		gain = f;
		AL10.alSourcef(id, AL10.AL_GAIN, gain);
	}
	
	public void setLooping(boolean state)
	{
		loop = state;
		if(loop)
			AL10.alSourcei(id, AL10.AL_LOOPING, AL10.AL_TRUE);
		else
			AL10.alSourcei(id, AL10.AL_LOOPING, AL10.AL_FALSE);
	}
	
	public void play()
	{
		AL10.alSourcePlay(id);
	}
	
	public void stop()
	{
		AL10.alSourceStop(id);
	}
	
	public void pause()
	{
		AL10.alSourcePause(id);
	}
	
	public void setSourceVelocity(Vector3f vel)
	{
		sourceVel.rewind();
		sourceVel.put(vel.x).put(vel.y).put(vel.z);
		sourceVel.rewind();
		AL10.alSource(id, AL10.AL_VELOCITY, sourceVel);
	}
	
	public void setSourcePosition(Vector3f pos)
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
		AL10.alSource(id, AL10.AL_POSITION, sourcePos);
		AL10.alSource(id, AL10.AL_VELOCITY, sourceVel);
		if(loop)
			AL10.alSourcei(id, AL10.AL_LOOPING, AL10.AL_TRUE);

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
		setUsed(false);
		AL10.alDeleteSources(id);
	}

}
