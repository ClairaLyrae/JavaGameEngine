package com.javagameengine;

import java.io.File;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.console.Console;
import com.javagameengine.console.MeshCommand;
import com.javagameengine.console.SceneCommand;
import com.javagameengine.events.EventManager;
import com.javagameengine.events.KeyPressEvent;
import com.javagameengine.math.FastMath;
import com.javagameengine.scene.Node;
import com.javagameengine.scene.Scene;
import com.javagameengine.scene.component.Camera;
import com.javagameengine.scene.component.CameraStatic;
import com.javagameengine.scene.component.CoordinateGrid;
import com.javagameengine.scene.component.MeshRenderer;
import com.javagameengine.scene.component.PhysicsComponent;
import com.javagameengine.scene.component.ShipControlComponent;

public class TestGame extends Game
{
	// We want to manually put stuff in our game, so here we make a scene and load it in during startup
	protected void onCreate()
	{
		AssetManager.loadAll();
		
		// Create a new scene
		Scene s = new Scene("3d");
		Scene s2 = new Scene("menu");
		Node root = s.getRoot();
		
		// SETUP SHIP
		
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
		ship_camera.setDepth(0.1f, 10000f);
		ship_camera.useDisplayBorders(true);
		ship_camera.setType(Camera.Type.PERSPECTIVE);
		
		ShipControlComponent ship_control = new ShipControlComponent(ship_physics, ship_camera);
		
		root.addChild(ship);
		ship.addComponent(ship_renderer);
		ship.addComponent(ship_physics);
		ship.addComponent(ship_control);
		camera_node.addComponent(ship_camera);

		
		// SETUP SKYBOX
		Node skybox_node = new Node("Skybox");
		MeshRenderer mrsky = new MeshRenderer(AssetManager.getMaterial("skybox"), AssetManager.getMesh("skybox"));
		root.addChild(skybox_node);
		skybox_node.addComponent(mrsky);
		skybox_node.getTransform().scale(5000f);
		
		
		
		// Add some debug components to the root
		//root.addComponent(new CoordinateGrid(5f, 50f));
		root.addComponent(new CameraStatic());
		
		
		
		// ADD ASTEROIDS!
		float posSpread = 20f;
		int numAsteroids = 200;
		for(int i = 0; i < numAsteroids; i++)
		{
			Random r =  new Random(System.currentTimeMillis());
			int index = r.nextInt(6) + 1;
			Mesh mesh = AssetManager.getMesh("asteroid_" + index);
			Material mat = AssetManager.getMaterial("asteroid_" + index);
			MeshRenderer mr = new MeshRenderer(mat, mesh);
			PhysicsComponent ast_phys = new PhysicsComponent(100f, 0.5f, true, 0.5f);
			ast_phys.getAngularVelocity().add(r.nextFloat()*0.001f, r.nextFloat()*0.001f, r.nextFloat()*0.001f);
			//ast_phys.getLinearVelocity().add((r.nextFloat()-0.5f)*5f, (r.nextFloat()-0.5f)*5f, (r.nextFloat()-0.5f)*5f);
			Node node = new Node("asteroid_" + i);
			root.addChild(node);
			node.addComponent(mr);
			node.addComponent(ast_phys);
			node.getTransform().translate((r.nextFloat()-0.5f)*posSpread, (r.nextFloat()-0.5f)*posSpread, (r.nextFloat()-0.5f)*posSpread + 30f);
			node.getTransform().scale(r.nextFloat());
			node.getTransform().rotate(r.nextFloat()*180f, r.nextFloat(), r.nextFloat(), r.nextFloat());
		}
		
		// Register the box making command
		Console.registerCommand(new MeshCommand());
		Console.registerCommand(new SceneCommand());
		
		// Load the scene into the game
		addScene(s);
		addScene(s2);
		setActiveScene(s.getName());
		
		// Auto load commands to console from file! Also we are manually firing some events
		Console.executeFromFile(new File("commands.txt"));
		EventManager.global.callEvent(new KeyPressEvent(Keyboard.KEY_1, '1', true));
		EventManager.global.callEvent(new KeyPressEvent(Keyboard.KEY_M, 'm', true));
		EventManager.global.callEvent(new KeyPressEvent(Keyboard.KEY_F, 'f', true));
	}

	// Dont care about destroying it
	protected void onDestroy()
	{
		
	}

	protected void onUpdate()
	{
		
	}

	protected void onRender()
	{
		
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
}
