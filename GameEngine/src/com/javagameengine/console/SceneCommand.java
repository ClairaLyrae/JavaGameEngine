package com.javagameengine.console;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;

public class SceneCommand extends Command
{
	public SceneCommand()
	{
		super("scene", 1);
	}

	@Override
	public String execute(String[] args)
	{
		if(args.length == 2 && args[0].equalsIgnoreCase("setactive"))
		{
			String name = args[1];
			if(Game.getHandle().setActiveScene(name))
				Console.println("Active scene set to: " + name);
			else
				Console.println("Active scene failed to set.");
		}
		if(args.length == 1 && args[0].equalsIgnoreCase("list"))
		{
			Console.println("Scene List");
			Scene active = Game.getHandle().getActiveScene();
			for(String s : AssetManager.getSceneList())
			{
				if(!active.getName().equals(s))
					Console.println("- " + s);
				else
					Console.println("- " + s + " [ACTIVE]");
			}
		}
		else if(args[0].equalsIgnoreCase("print"))
		{
			Scene s = Game.getHandle().getActiveScene();
			if(args.length > 1)
			{
				s = AssetManager.getScene(args[1]);
				if(s == null)
					return "Scene '" + args[1] + "' cannot be found.";
			}
			if(s == null)
				return "No active scene available.";
			Console.println("Scene " + s.getName() + ":");
			printScene(s);
		}
		return null;
	}
	
	public static void printScene(Scene s)
	{
		Console.println("Scene: " + s.getName());
		printSceneRecursive(s.getRoot(), "");
	}

	private static void printSceneRecursive(Node n, String sb)
	{
		sb += "  ";
		
		Console.println(sb + "N: " + n.toString());
		
		for(Component c : n.getComponents())
			Console.println(sb + "C: " + c.toString());
		
		for(Node node : n.getChildren())
			printSceneRecursive(node, sb);
	}
}
