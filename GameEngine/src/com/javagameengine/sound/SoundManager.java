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
	private static int MAX_SOURCES = 16;

	/** Position of the listener. */
	static FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3);

	/** Velocity of the listener. */
	static FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3);

	/** Orientation of the listener. (first 3 elements are "at", second 3 are "up"	 */
	static FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6);
	
	public static void setListenerVelocity(Vector3f vel)
	{
		listenerVel.rewind();
		listenerVel.put(vel.x).put(vel.y).put(vel.z);
		listenerVel.rewind();
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
	}
	
	public static void setListenerPosition(Vector3f pos)
	{
		listenerPos.rewind();
		listenerPos.put(pos.x).put(pos.y).put(pos.z);
		listenerPos.rewind();
		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}
	
	public static void setListenerOrientation(Vector3f pos, Vector3f pos2)
	{
		listenerOri.rewind();
		listenerOri.put(pos.x).put(pos.y).put(pos.z).put(pos2.x).put(pos2.y).put(pos2.z);
		listenerOri.rewind();
		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}


	private static SoundManager global = new SoundManager();
	private static HashMap<Scene, List<Sound>> registeredSounds = new HashMap<Scene, List<Sound>>();
	
	private static Sound[] sources;
	
	public static void play(SoundBuffer s)
	{
		Sound source = getSource();
		source.setSound(s);
		source.play();
	}
	
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
