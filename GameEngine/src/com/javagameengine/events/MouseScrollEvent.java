package com.javagameengine.events;


/**
 * Event describing the scroll wheel on the mouse being moved up or down. 
 * @author ClairaLyrae
 */
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
