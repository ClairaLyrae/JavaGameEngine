package com.javagameengine.console;

import java.util.List;

import com.javagameengine.assets.AssetManager;

public class AssetCommand extends Command
{
	public AssetCommand()
	{
		super("asset", 1);
	}

	@Override
	public String execute(String[] args)
	{
		if(args.length == 1 && args[0].equalsIgnoreCase("reload"))
		{
			//reload assets
		}
		if(args.length >= 1 && args[0].equalsIgnoreCase("list"))
		{
			List<String> meshlist = null;
			List<String> shaderlist = null;
			List<String> texturelist = null;
			meshlist = AssetManager.getMeshList();
			shaderlist = AssetManager.getShaderList();
			texturelist = AssetManager.getTextureList();
			
			if(args.length == 2)
			{
				List<String> list = null;
				if(args[1].equalsIgnoreCase("meshes"))
					list = meshlist;
				if(args[1].equalsIgnoreCase("shaders"))
					list = shaderlist;
				if(args[1].equalsIgnoreCase("textures"))
					list = texturelist;
				if(list != null)
				{
					for(String s : list)
						Console.println("- " + s);
				}
			}
			else
			{
				Console.println("Meshes:");
				for(String s : meshlist)
					Console.println("- " + s);
				Console.println("Shaders:");
				for(String s : shaderlist)
					Console.println("- " + s);
				Console.println("Textures:");
				for(String s : texturelist)
					Console.println("- " + s);
			}
		}
		// TODO Auto-generated method stub
		return null;
	}
}
