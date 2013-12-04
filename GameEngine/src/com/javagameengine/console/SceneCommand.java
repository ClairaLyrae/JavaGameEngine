package com.javagameengine.console;

import java.util.List;

import com.javagameengine.Game;
import com.javagameengine.assets.AssetManager;
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
			for(String s : Game.getHandle().getSceneList())
			{
				if(!active.getName().equals(s))
					Console.println("- " + s);
				else
					Console.println("- " + s + " [ACTIVE]");
			}
		}
		// TODO Auto-generated method stub
		return null;
	}
}
