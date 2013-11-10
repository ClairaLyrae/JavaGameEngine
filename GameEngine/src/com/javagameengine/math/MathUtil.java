package com.javagameengine.math;

/**
 * Static utility class containing math methods (mostly faster float approximations of standard math operations) 
 * @author ClairaLyrae
 */
public class MathUtil
{
	public static float fastSqrt(float f)
	{
		return (float) Math.sqrt(f); // TODO Need to sort out a fast approximate of calculating float sqrt
	}

	public static float fastInvSqrt(float f)
	{
		return (float) Math.sqrt(f); // TODO Easier to calculate inverse of square root fast
	}
}
