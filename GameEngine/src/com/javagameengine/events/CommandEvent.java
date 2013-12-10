package com.javagameengine.events;

import com.javagameengine.console.Command;

/**
 * Called when a command is executed by the console. 
 */
public class CommandEvent extends Event
{
	private Command command;
	private String[] args;
	
	public CommandEvent(Command c, String[] args)
	{
		this.command = c;
		this.args = args;
	}
	
	/**
	 * @return Arguments sent to the executed command
	 */
	public String[] getArgs()
	{
		return args;
	}
	
	/**
	 * @return Command that was executed
	 */
	public Command getCommand()
	{
		return command;
	}
}
