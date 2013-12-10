package com.javagameengine.renderer;

/**
 * Interface describing an object that contains a bind() method used for setting up the openGL
 * context. Influences drawing order of objects.
 */
public interface Bindable
{
	/**
	 * Binds the openGL context of this object for later drawing.
	 * @return Index of shader program in conext (-1 if fixed-function)
	 */
	public int bind();
}
