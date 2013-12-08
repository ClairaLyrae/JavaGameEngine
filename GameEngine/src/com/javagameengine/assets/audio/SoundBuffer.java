package com.javagameengine.assets.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.javagameengine.assets.NativeObject;

public class SoundBuffer extends NativeObject
{
	private WaveData data;
	
	public SoundBuffer()
	{
		super(SoundBuffer.class);
	}
	
	public SoundBuffer(WaveData data)
	{
		super(SoundBuffer.class);
		this.data = data;
	}

	public static SoundBuffer loadFromFile(File f) throws FileNotFoundException 
	{
		WaveData wav = WaveData.create(new BufferedInputStream(new FileInputStream(f)));
		if(wav == null)
			throw new IllegalStateException("Could not load wav data from file '" + f.getName() + "'");
		return new SoundBuffer(wav);
	}

	@Override
	public boolean create() 
	{
		if(data == null)
			throw new IllegalStateException("Could not load sound to audio card");
		id = AL10.alGenBuffers();
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			throw new IllegalStateException("Could not load sound to audio card");
		AL10.alBufferData(id, data.format, data.data, data.samplerate);
		return true;
	}

	@Override
	public void destroy()
	{
		AL10.alDeleteSources(id);
		AL10.alDeleteBuffers(id);
	}
	
	public String toString()
	{
		return "";
	}
}