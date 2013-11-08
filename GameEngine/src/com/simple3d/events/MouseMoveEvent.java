package com.simple3d.events;

public class MouseMoveEvent extends MouseEvent
{
	private int x, y, dx, dy;
	
	public MouseMoveEvent(int x, int y, int dx, int dy)
	{
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getPastX()
	{
		return x-dx;
	}
	
	public int getPastY()
	{
		return y-dy;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getDeltaX()
	{
		return dx;
	}

	public int getDeltaY()
	{
		return dy;
	}
	
	public String toString()
	{
		return String.format("MouseMoveEvent : Position[%d, %d], Delta[%d, %d]", x, y, dx, dy);
	}
}
