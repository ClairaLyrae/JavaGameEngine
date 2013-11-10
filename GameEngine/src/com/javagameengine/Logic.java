package com.javagameengine;

/**
 * Describes an object that changes state or affects the state of other objects during the game loop.
 * @author ClairaLyrae
 */
public interface Logic
{
	public void logic(int delta);
}
