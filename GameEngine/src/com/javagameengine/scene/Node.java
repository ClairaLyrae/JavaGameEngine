


package com.javagameengine.scene;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.javagameengine.math.Matrix;
import com.javagameengine.math.Quaternion;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;
import com.javagameengine.renderer.RenderOperation;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.renderer.Renderer;

/**
 * Node is the discrete element of the scene graph structure that comprises the heart of the game engine. It 
 * contains references to any number of children nodes and a single reference to a parent node, or null if the
 * node is the first node in the scene graph. 
 * <p>
 * Each Node carries a single Transform object which describes the
 * translation, scale and rotation of the node in relation to the parent node. Therefore, by applying the transforms
 * during tree traversal starting from the root node, the total transform in relation to the world origin is found.
 * The heirarchal structure of the transforms allows for useful game object organization where objects that are 
 * conceptually parts of another object can be moved as a whole solely through a particular root node.
 * <p>
 * Nodes keep track of bounding boxes that describes an axis-aligned box which conservatively encompasses all
 * of the children node and components within it. This bounding box is updated through the logic() call and relies
 * on components to report accurate bounds if applicable. This bounding box is used for object culling purposes in
 * rendering as well as culling unnecessary calculations in other uses. For example, physics collision checks
 * can be vastly sped up by first determining if bounding boxes intersect and ignoring if not.
 * <p>
 * A node also contains any number of Component objects which together form a aggregate description of the node, 
 * either behaviorally or graphically. These can be dynamically added or removed to manipulate the scene.
 * <p>
 * Finally, the node object provides a path that pushes the graphics(), logic(), and physics() methods from the
 * main game loop down the scene graph to each node in the tree and to each component the nodes contain.
 * @author ClairaLyrae
 */
public final class Node implements Bounded
{
	private String name;
	
	protected Bounds boundingBox = Bounds.getVoid();
	protected Bounds boundingBoxNode = Bounds.getVoid();
	
	protected Scene scene;
	
	protected Transform transform = new Transform();
	protected Transform worldTransform = new Transform();

	protected Node parent;
	private LinkedHashSet<Node> children = new LinkedHashSet<Node>();
	
	private LinkedHashSet<Renderable> graphicsComponents = new LinkedHashSet<Renderable>();
	private LinkedHashSet<Bounded> boundedComponents = new LinkedHashSet<Bounded>();
	private LinkedHashSet<Component> components = new LinkedHashSet<Component>();

	protected Node node;
	
	protected float[] transformMatrix = new float[4];
	
	public Node(String name)
	{
		this.name = name;
	}
	
	/**
	 * Unlinks node and subtree from scene and frees any resources in subnodes and components.
	 */
	public void destroy()
	{
		// If the node is not the root node, unlink it from its parent. Otherwise, reset the scene
		if(parent != null)
			parent.removeChild(this);
		else
			scene.clear();
		
		// Destroy components and free component resources
		for(Component c : components)
		{
			c.onDestroy();
		}
		
		// Iterate destroy down tree
		for(Node n : children)
		{
			n.destroy();
		}
		scene = null;
		parent = null;
	}
	
	public Scene getScene()
	{
		return scene;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isLinked()
	{
		return scene != null;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Node getParent()
	{
		return parent;
	}
	
	public List<Node> getChildren()
	{
		return new ArrayList<Node>(children);
	}
	
	protected void updateLinks(Node n)
	{
		scene = n.scene;
		for(Node child : children)
			child.updateLinks(this);
	}
	
	public boolean addChild(Node n)
	{
		if(n.isLinked())
			return false;
		n.parent = this;
		n.updateLinks(this);
		return children.add(n);
	}
	
	public boolean removeChild(Node n)
	{
		if(!children.contains(n))
			return false;
		n.parent = null;
		n.scene = null;
		return children.remove(n);
	}
	
	public void removeChildren()
	{
		for(Node n : children)
			removeChild(n);
	}
	
	public boolean hasChild(Node n)
	{
		return children.contains(n);
	}
	
	public boolean hasChild(String name)
	{
		return getChild(name) != null;
	}

	public Node getChild(String name)
	{
		for(Node n : children)
		{
			if(n.getName().equals(name))
				return n;
		}
		return null;
	}

	public boolean getChild(Node n)
	{
		return children.contains(n);
	}

	public List<Component> getComponents()
	{
		return new ArrayList<Component>(components);
	}
	
	@SuppressWarnings("unchecked")
	public List<Component> getGraphicsComponents()
	{
		return new ArrayList<Component>((Collection<? extends Component>)graphicsComponents);
	}
	
	@SuppressWarnings("unchecked")
	public List<Component> getBoundedComponents()
	{
		return new ArrayList<Component>((Collection<? extends Component>)boundedComponents);
	}
	
	public List<Component> getComponents(Class<? extends Component> clazz)
	{
		List<Component> list = new ArrayList<Component>();
		for(Component c : components)
		{
			if(clazz.isInstance(c))
				list.add(c);
		}
		return list;
	}

	public boolean hasComponentsOf(Class<? extends Component> clazz)
	{
		for(Component c : components)
		{
			if(clazz.isInstance(c))
				return true;
		}
		return false;
	}
	
	public boolean addComponent(Component c)
	{
		if(components.contains(c))
			return false;
		if(c instanceof Renderable)
			graphicsComponents.add((Renderable)c);
		components.add(c);
		c.node = this;
		c.onCreate();
		return true;
	}

	public boolean removeComponent(Component c)
	{
		boolean removed = components.remove(c);
		if(!removed)
			return false;
		if(c instanceof Renderable)
			graphicsComponents.remove((Renderable)c);
		c.onDestroy();
		c.node = null;
		return true;
	}
	
	public List<Component> removeComponents()
	{
		if(components.size() == 0)
			return Collections.emptyList();
		List<Component> list = getComponents();
		components.clear();
		graphicsComponents.clear();
		for(Component c : list)
			c.node = null;
		return list;
	}
	
	public List<Component> removeComponents(List<Component> list)
	{
		List<Component> rem = new ArrayList<Component>();
		for(Component c : list)
		{
			if(removeComponent(c))
				rem.add(c);
		}
		return rem;
	}
	
	public List<Component> removeComponents(Class<? extends Component> clazz)
	{
		return removeComponents(getComponents(clazz));
	}

	public boolean hasComponent(Component c)
	{
		return components.contains(c);
	}
	
	public Transform getTransform()
	{
		return transform;
	}
	
	/**
	 * Retrieves the Transform of this node in respect to the scene graph root identity transform. Updated once
	 * every frame. The alternate method findWorldTransform() will force compute the world Transform, however 
	 * this method is far more optimal.
	 * @return Transform of node in respect to world coordinates
	 */
	public Transform getWorldTransform()
	{
		return worldTransform;
	}
	
	public void setTransform(Transform t)
	{
		transform = t;
	}
	
	/**
	 * Forced computation of the Transform of this node in respect to the scene graph root identity transform
	 * This method does not modify the stored world coordinate Transform. Use getWorldTransform() when
	 * possible, as it is far faster. Only use if the instantaneous Transform is necessary.
	 * <p>
	 * Iterates up the tree until the root is reached, then back down applying transforms one
	 * by one starting from the identity transform to each child's transform until back at this 
	 * node. Returns the result.
	 * @return Total transform in relation to origin
	 */
	public Transform findWorldTransform()
	{
		// TODO Untested
		// Go up the chain to the root, then back out, applying transforms. End result will be absolute transform referenced to the identity
		if(parent == null)
			return new Transform();
		Transform wc = new Transform(worldTransform);
		wc.set(transform);
		return wc.inherit(parent.findWorldTransform());
	}
	
	/**
	 * Bounding box returned is updated once every frame.
	 * @return Bounding box that encompasses the components and child nodes contained in this node
	 */
	public Bounds getBounds()
	{
		return boundingBox;
	}
	
	/**
	 * Bounding box returned is updated once every frame.
	 * @return Bounding box that encompasses the components contained in this node
	 */
	public Bounds getNodeBounds()
	{
		return boundingBoxNode;
	}

	/**
	 * Iteration through the tree calling logic on child nodes and components. 
	 */
	public void logic(int delta)
	{
		// Call logic method on child nodes
		for(Component c : components)
			c.onUpdate(delta);	

		// Call logic method on child nodes
		for(Node n : children)
			n.logic(delta);	
	}
	
	/**
	 * Runs through the tree creating and queuing RenderOperations into the Renderer. 
	 * <p>
	 * First it computes the world transform for each RenderOperation based on the scene graph transform hierarchy, 
	 * and stores it in the node. Then it computes the Bounds of the node based on the Components and the Bounds of  
	 * the subtree. During this it will load the Renderer with RenderOperations based on the computed data.
	 */
	public void queueRenderOperations()
	{
		// Update the world space transforms
		worldTransform.set(transform);
		if(parent != null)
			worldTransform.inherit(parent.worldTransform);
		
		// Call graphics method on child components & update node bounds
		boundingBoxNode = Bounds.getVoid();	// Start off with a void Bounds
		for(Component c : components)
		{
			if(c instanceof Bounded)
				boundingBoxNode.encompass(((Bounded)c).getBounds());
			if(c instanceof Renderable)
				Renderer.queue(new RenderOperation((Renderable)c, worldTransform));
		}
		
		// Call graphics method on child nodes & update subtree bounds
		boundingBox.set(boundingBoxNode); // Update bounding box with calculated Node bounds
		for(Node n : children)
		{
			boundingBox.encompass(n.getBounds());	// Extend the bounding box to include the bounds of this child
			n.queueRenderOperations();
		}
	}
	
	public String toString()
	{
		return String.format("Node[name=%s, " +
				"numChildren=%d, " +
				"numComponents=%d, " +
				"hasParent=%b, " +
				"hasScene=%b, " +
				"transform=%s" +
				"world=%s]",
				name, getChildren().size(), getComponents().size(), parent != null, scene != null, transform, worldTransform);
	}
}
