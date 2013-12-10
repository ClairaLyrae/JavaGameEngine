package com.javagameengine;

import java.io.File;
import java.util.Random;

import org.lwjgl.LWJGLException;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.skybox.Skybox;
import com.javagameengine.assets.sounds.SoundBuffer;
import com.javagameengine.console.Console;
import com.javagameengine.console.MeshCommand;
import com.javagameengine.console.SceneCommand;
import com.javagameengine.gui.GUI;
import com.javagameengine.gui.HUD;
import com.javagameengine.gui.WelcomeGUI;
import com.javagameengine.math.Color4f;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.Camera;
import com.javagameengine.scene.component.Flare;
import com.javagameengine.scene.component.LaserComponent;
import com.javagameengine.scene.component.Light;
import com.javagameengine.scene.component.MeshRenderer;
import com.javagameengine.scene.component.Music;
import com.javagameengine.scene.component.PhysicsComponent;
import com.javagameengine.scene.component.ShipControlComponent;

/**
 * TestGame is a demo extension of the Game class for a simple space shooter.
 */
public class TestGame extends Game
{
	// We want to manually put stuff in our game, so here we make a scene and load it in during startup
	protected void onCreate()
	{
		// Setup 3d menu scene
		Scene menu3d = new Scene("menu3d");
		{
			GUI menugui = new WelcomeGUI();
			menugui.setCursor(true);
			menu3d.setGUI(menugui);
			addEnvironmentToScene(menu3d);
			menu3d.getRoot().addChild(createRotatingCamera(0.1f, 0.1f, 0.1f));
			//menu3d.getRoot().addChild(createShip());
			addAsteroidsToScene(menu3d, 400, 400f, 400f, 400f, 0f, 0f, 0f);
			SoundBuffer music = AssetManager.getSound("spacemusic");
			menu3d.getRoot().addComponent(new Music(music));
		}

		// Setup menu scene
		Scene menu = new Scene("menu");
		{
			GUI welcomegui = new WelcomeGUI();
			welcomegui.setCursor(true);
			menu.setGUI(welcomegui);
		}
		
		// Setup 3d scene
		Scene game3d = new Scene("3d");
		{
			GUI hud = new HUD();
			hud.setCursor(false);
			game3d.setGUI(hud);
			addEnvironmentToScene(game3d);
			game3d.getRoot().addChild(createShip());
			addAsteroidsToScene(game3d, 400, 400f, 400f, 400f, 0f, 0f, 0f);
//			game3d.getRoot().addComponent(Flare.createFlare(AssetManager.getTexture("sunflare")));
//			Node testn = new Node("");
//			testn.addComponent(Flare.createFlare(AssetManager.getTexture("sunflare")));
//			game3d.getRoot().addChild(testn);
//			testn.getTransform().translate(50f, 50f, 50f);
			//game3d.getRoot().addComponent(Flare.createFlare(AssetManager.getTexture("sunflare")));
		}
		
		// Add scenes to asset manager so we can keep track of them
		AssetManager.addScene(game3d);
		AssetManager.addScene(menu);
		AssetManager.addScene(menu3d);
		setActiveScene(menu.getName());
		
		// Do initial console stuff
		Console.registerCommand(new MeshCommand());
		Console.registerCommand(new SceneCommand());
		
		setActiveScene(menu3d.getName());
		
		// Auto load commands to console from file! 
		Console.executeFromFile(new File("commands.txt"));
	}
	
	public static void addEnvironmentToScene(Scene s)
	{
		Node root = s.getRoot();
		Light sunLight = new Light();
		sunLight.setIntensity(3f);
		Node sunLightNode = new Node("light_1");
		sunLightNode.addComponent(sunLight);
			
		Light backLight = new Light();
		backLight.setIntensity(3f);
		Node backLightNode = new Node("light_2");
		backLightNode.addComponent(backLight);
			
		root.addChild(sunLightNode);
		root.addChild(backLightNode);
			
		sunLight.setDiffuseAndSpecularColor(new Color4f("81a79f", 1f));
		sunLight.setAmbientColor(new Color4f("122029", 1f));
			
		backLight.setDiffuseAndSpecularColor(new Color4f("124651", 1f));
			
		sunLightNode.getTransform().translate(50000f, 50000f, 0f);
		backLightNode.getTransform().translate(-50000f, -50000f, -50000f);
			
		Flare sunflare = Flare.createFlare(AssetManager.getTexture("sunflare"));
		sunLightNode.addComponent(sunflare);
		Flare glowflare = Flare.createFlare(AssetManager.getTexture("sunglow"));
		sunLightNode.addComponent(glowflare);
		sunflare.setAlwaysOnTop(true);
		sunflare.setSize(0.5f);
		sunflare.setLayer(1);
		glowflare.setAlwaysOnTop(false);
		glowflare.setFadeEnabled(false);
		glowflare.setSize(1f);
		Skybox mrsky = AssetManager.getSkybox("space2");
		s.setSkybox(mrsky);
	}
	
	public Node createRotatingCamera(float x, float y, float z)
	{
		Node camNode = new Node("camera");
		Camera camera = new Camera();
		camera.setEnabled(true);
		camera.setFOV(45f);
		camera.setDepth(0.1f, 100000f);
		camera.useDisplayBorders(true);
		camera.setType(Camera.Type.PERSPECTIVE);
		camNode.addComponent(camera);
		PhysicsComponent camrot = new PhysicsComponent(100f, 0.5f, false, 0.5f);
		camrot.getAngularVelocity().set(x, y, z);
		camNode.addComponent(camrot);
		return camNode;
	}
	
	public Node createShip()
	{
		Node ship = new Node("ship");
		Mesh ship_mesh = AssetManager.getMesh("ship");
		Material ship_mat = AssetManager.getMaterial("ship");
		MeshRenderer ship_renderer = new MeshRenderer(ship_mat, ship_mesh);
		PhysicsComponent ship_physics = new PhysicsComponent(55f, 0.0f, true, 1f);
		
		Node camera_node = new Node("ship_camera");
		camera_node.getTransform().translate(0f, 0.5f, -4f);
		ship.addChild(camera_node);
		Camera ship_camera = new Camera();
		ship_camera.setEnabled(true);
		ship_camera.setFOV(45f);
		ship_camera.setDepth(0.1f, 100000f);
		ship_camera.useDisplayBorders(true);
		ship_camera.setType(Camera.Type.PERSPECTIVE);
		
		LaserComponent ship_laser = new LaserComponent();
		ShipControlComponent ship_control = new ShipControlComponent(ship_physics, ship_camera);
	
		ship.addComponent(ship_renderer);
		ship.addComponent(ship_physics);
		ship.addComponent(ship_control);
		ship.addComponent(ship_laser);
		camera_node.addComponent(ship_camera);
		return ship;
	}
	
	public void addAsteroidsToScene(Scene scene, int numAsteroids, float xscale, float yscale, float zscale, float xpos, float ypos, float zpos)
	{
		for(int i = 0; i < numAsteroids; i++)
		{
			Random r =  new Random(System.nanoTime() + i);
			int index = r.nextInt(6) + 1;
			Mesh mesh = AssetManager.getMesh("asteroid_" + index);
			Material mat = AssetManager.getMaterial("asteroid_" + index);
			MeshRenderer mr = new MeshRenderer(mat, mesh);
			//ast_phys.getLinearVelocity().add((r.nextFloat()-0.5f)*5f, (r.nextFloat()-0.5f)*5f, (r.nextFloat()-0.5f)*5f);
			Node node = new Node("asteroid_" + i);
			scene.getRoot().addChild(node);
			node.addComponent(mr);
			float astSize = r.nextFloat()*5f+1f;
			node.getTransform().translate((r.nextFloat()-0.5f)*xscale + xpos, (r.nextFloat()-0.5f)*yscale + ypos, (r.nextFloat()-0.5f)*zscale + zpos);
			node.getTransform().scale(astSize);
			node.getTransform().rotate(r.nextFloat()*180f, r.nextFloat(), r.nextFloat(), r.nextFloat());
			PhysicsComponent ast_phys = new PhysicsComponent(100f, 0.5f, true, astSize);
			ast_phys.getAngularVelocity().add((r.nextFloat()-0.5f)*0.1f, (r.nextFloat()-0.5f)*0.1f, (r.nextFloat()-0.5f)*0.1f);
			node.addComponent(ast_phys);
		}
	}

	public static void main(String[] args)
	{
		TestGame game = new TestGame();
		try
		{
			game.run(args);
		} catch (LWJGLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void onDestroy()
	{
		
	}

	protected void onUpdate(float delta)
	{
		
	}

	protected void onRender()
	{
		
	}
}
