package com.javagameengine.console;

/**
 * Command is an abstract class describing a textual command that can be parsed and executed. The class
 * has one main method execute which is given the arguments the command should be executed with.
 * <p>
 * The constructor for Command must be given a string corresponding to the name of the command and an
 * integer corresponding to the minimum number of arguments the command requires. Whether or not the Console
 * calls the execute method depends on if the command string sent to the Console matches the format "/<name>"
 * with the required number of arguments following, where arguments are divided by spaces.
 * @author ClairaLyrae
 */
public abstract class Command
{
	private int minargs;
	private String name;
	
	public Command(String name, int minargs)
	{
		this.minargs = minargs;
		this.name = name;
	}
	
	/**
	 * @return Minimum number of arguments this command takes
	 */
	public int getMinArgs()
	{
		return minargs;
	}
	
	/**
	 * @return Name of command
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Execute this command with the given arguments
	 * @param args Arguments for execution of command
	 * @return String to display describing result of command (may be null)
	 */
	public abstract String execute(String[] args);
	
	public String toString()
	{
		return "/" + name + " minargs=" + minargs + "";
	}
}
