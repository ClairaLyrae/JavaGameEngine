package com.javagameengine.sound;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.math.Vector3f;
import com.javagameengine.scene.Scene;

public class SoundManager
{
	private static float global_volume = 1f;
	
	public static float getGlobalVolume()
	{
		return global_volume;
	}
	
	public static void setGlobalVolume(float f)
	{
		for(Sound s : sources)
			s.setGain(s.gain * global_volume);
		global_volume = f;
	}
	
	private static int MAX_SOURCES = 16;

	private static Vector3f listenerPosVec = new Vector3f();
	private static Vector3f listenerVelVec = new Vector3f();
	private static Vector3f listenerOriPosVec = new Vector3f();
	private static Vector3f listenerOriUpVec = new Vector3f();
	private static FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3);
	private static FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3);
	private static FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6);

	/**
	 * Sets the spatial velocity of the listener
	 * @param pos Spatial velocity of listener
	 */
	public static void setListenerVelocity(Vector3f vel)
	{
		listenerVelVec.set(vel);
		listenerVel.rewind();
		listenerVel.put(vel.x).put(vel.y).put(vel.z);
		listenerVel.rewind();
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
	}

	/**
	 * Sets the spatial position of the listener
	 * @param pos Spatial position of listener
	 */
	public static void setListenerPosition(Vector3f pos)
	{
		listenerPosVec.set(pos);
		listenerPos.rewind();
		listenerPos.put(pos.x).put(pos.y).put(pos.z);
		listenerPos.rewind();
		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}
	
	/**
	 * Sets the spatial orientation of the listener
	 * @param pos Spatial position of listener
	 * @param up Spatial "up" direction from listener
	 */
	public static void setListenerOrientation(Vector3f pos, Vector3f up)
	{
		listenerOriPosVec.set(pos);
		listenerOriUpVec.set(up);
		listenerOri.rewind();
		listenerOri.put(pos.x).put(pos.y).put(pos.z).put(up.x).put(up.y).put(up.z);
		listenerOri.rewind();
		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}

	private static SoundManager global = new SoundManager();
	private static HashMap<Scene, List<Sound>> registeredSounds = new HashMap<Scene, List<Sound>>();
	
	private static Sound[] sources;
	
	/**
	 * Plays a sound from a SoundBuffer
	 * @param s SoundBuffer containing to sound to play
	 */
	public static void play(SoundBuffer s)
	{
		Sound source = getSource();
		source.setSound(s);
		source.play();
	}
	
	/**
	 * Plays a sound from a SoundBuffer with the given spatial position and velocity
	 * @param s SoundBuffer containing to sound to play
	 * @param position Position of sound source
	 * @param velocity Velocity of sound source
	 */
	public static void play(SoundBuffer s, Vector3f position, Vector3f velocity)
	{
		Sound source = getSource();
		if(source == null)
			return;
		source.setPosition(position);
		source.setVelocity(velocity);
		source.setSound(s);
		source.play();
	}
	
	/**
	 * Plays a sound from a SoundBuffer
	 * @param s SoundBuffer containing to sound to play
	 * @param vol Gain applied to the sound
	 */
	public static void play(SoundBuffer s, float vol)
	{
		Sound source = getSource();
		if(source == null)
			return;
		source.setSound(s);
		source.setGain(vol);
		source.play();
	}
	
	
	private static int pointer = 0;
	
	/**
	 * Retrieves a valid sound source buffer on the audio hardware that is unused. Since there
	 * are a limited number of sound buffers available, the manager must allocate them dynamically 
	 * as new sounds are played.
	 * @return A valid, unused sound source object. 
	 */
	public static Sound getSource()
	{
		for(int i = pointer; i < sources.length; i++)
		{
			if(!sources[i].isUsed())
			{
				pointer = i+1;
				if(pointer >= sources.length)
					pointer = 0;
				return sources[i];
			}
			
		}
		return null;
	}
	
	/**
	 * Initalize OpenAL. Call once on program start.
	 */
	public static void initialize()
	{
		try
		{
			AL.create();
		} catch (LWJGLException le)
		{
			le.printStackTrace();
			return;
		}
		if(AL10.alGetError() != AL10.AL_NO_ERROR)
			throw new IllegalStateException("Failed to create OpenAL environment.");
		sources = new Sound[MAX_SOURCES];
		for(int i = 0; i < sources.length; i++)
		{
			sources[i] = new Sound();
			sources[i].create();
		}
		setListenerOrientation(Vector3f.zero, Vector3f.zero);
		setListenerPosition(Vector3f.zero);
		setListenerVelocity(Vector3f.zero);
	}
	
	private SoundManager()
	{
		
	}
}
