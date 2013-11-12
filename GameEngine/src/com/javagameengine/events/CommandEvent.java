package com.javagameengine.events;

import com.javagameengine.console.Command;

public class CommandEvent extends Event
{
	private Command command;
	private String[] args;
	
	public CommandEvent(Command c, String[] args)
	{
		this.command = c;
		this.args = args;
	}
	
	public String[] getArgs()
	{
		return args;
	}
	
	public Command getCommand()
	{
		return command;
	}
}
