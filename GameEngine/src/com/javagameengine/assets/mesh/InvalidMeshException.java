package com.javagameengine.assets.mesh;

import com.javagameengine.assets.material.InvalidAssetException;

// Not yet used fully

/**
 * Represents an exception caused by handling invalid meshes that contain errors in geometry or missing data
 * @author ClairaLyrae
 */
public class InvalidMeshException extends InvalidAssetException
{

	public InvalidMeshException(String m)
	{
		super(m);
	}

}
