package com.javagameengine.util;

import com.javagameengine.assets.material.Texture;

public class Font
{
	
	
	public static Font createFont(Texture t)
	{
		return null;
	}
	
	private Font() {}
	
	private Texture fontTexture;
	
	public void drawCharacter(int x, int y, char c)
	{
		
	}
	
	public int getCharacterHeight()
	{
		return 0;
	}
	
	public int getCharacterWidth()
	{
		return 0;
	}
	
	private int charWidth = 4;
	private int charHeight = 8;
	
	public int draw(int x, int y, String s)
	{
		char[] chars = s.toCharArray();
		for(int i = 0; i < chars.length; i++)
			drawCharacter(x + i*charWidth, y, chars[i]);
		return 0;
	}
}
