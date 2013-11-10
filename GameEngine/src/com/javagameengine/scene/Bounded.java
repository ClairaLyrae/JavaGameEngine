package com.javagameengine.scene;

/**
 * @author ClairaLyrae
 * Describes an object which has physical boundaries in space, and can provide an axis-aligned bounding
 * box that tightly encompasses those boundaries. Object must keep bounding box updated to reflect 
 * the current boundaries.
 */
public interface Bounded
{
	public Bounds getBounds();
}
