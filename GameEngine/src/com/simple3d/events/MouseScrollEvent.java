package com.simple3d.events;

public class MouseScrollEvent extends MouseEvent
{

	private int amount;
	
	public MouseScrollEvent(int amount)
	{
		this.amount = amount;
	}
	
	public boolean isUp()
	{
		return amount > 0;
	}
	
	public boolean isDown()
	{
		return amount < 0;
	}

	public int getAmount()
	{
		return amount;
	}

	public String toString()
	{
		return String.format("MouseScrollEvent : Amount[%d]", amount);
	}
}
