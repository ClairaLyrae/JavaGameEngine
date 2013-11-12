package com.javagameengine.console;

public abstract class Command
{
	private int minargs;
	private String name;
	
	public Command(String name, int minargs)
	{
		this.minargs = minargs;
		this.name = name;
	}
	
	public int getMinArgs()
	{
		return minargs;
	}
	
	public String getName()
	{
		return name;
	}
	
	public abstract boolean execute(String[] args);
}
