


package com.javagameengine.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import com.javagameengine.Game;
import com.javagameengine.math.Transform;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.component.Light;

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
public class Node implements Bounded
{
	private String name;
	
	protected Bounds boundingBox = Bounds.getVoid();
	protected Bounds boundingBoxNode = Bounds.getVoid();

	protected Node parent;
	protected Scene scene;
	
	protected Transform transform = new Transform();
	protected Transform worldTransform = new Transform();

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
		List<Node> child_list = new ArrayList<Node>(children);
		List<Component> comp_list = new ArrayList<Component>(components);
		for(Node n : child_list)
			n.destroy();
		for(Component c : comp_list)
			c.destroy(); 
		if(parent != null)
			parent.children.remove(this);
		if(scene != null && scene.getRoot() != this)
			scene = null;
		parent = null;
		isDestroyed = true;
	}
	
	private boolean isDestroyed = false;
	
	public boolean isDestroyed()
	{
		return isDestroyed;
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
	
	public boolean isActive()
	{
		if(scene == null)
			return false;
		return scene == Game.getHandle().getActiveScene();
	}
	
	private void unlink()
	{
		parent = null;
		scene = null;
		boolean active = isActive();
		for(Component c : components)
		{
			c.onUnlink();
			if(active)
				c.onDeactivate();
			c.node = null;
			c.scene = null;
		}
		for(Node child : children)
			child.unlink();
	}
	
	/**
	 * Relinks all components in node and subnodes. Only call during scene load or unloads.
	 */
	public void relink()
	{
		boolean active = isActive();
		for(Component c : components)
		{
			if(active)
				c.onActivate();
			else
				c.onDeactivate();
		}
		for(Node child : children)
			child.relink();
	}
	
	private void linkTo(Node n)
	{
		if(n == null || !n.isLinked())
			return;
		parent = n;
		scene = n.scene;
		boolean active = isActive();
		for(Component c : components)
		{
			c.node = this;
			c.scene = scene;
			c.onLink();
			if(active)
				c.onActivate();
		}
		for(Node child : children)
			child.linkTo(this);
	}
	
	public boolean addChild(Node n)
	{
		if(n.isLinked() || hasChild(n))
			return false;
		children.add(n);
		n.linkTo(this);
		return true;
	}
	
	public boolean removeChild(Node n)
	{
		if(!children.contains(n))
			return false;
		children.remove(n);
		n.unlink();
		return true;
	}
	
	public void removeChildren()
	{
		Node[] childrenarr = children.toArray(new Node[0]);
		for(Node n : childrenarr)
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
		if(isLinked())
		{
			c.node = this;
			c.scene = scene;
			c.onLink();
			if(isActive())
				c.onActivate();
		}
		return true;
	}

	public boolean removeComponent(Component c)
	{
		if(!components.remove(c))
			return false;
		if(c instanceof Renderable)
			graphicsComponents.remove((Renderable)c);
		
		if(isLinked())
		{
			if(isActive())
				c.onDeactivate();
			c.onUnlink();
			c.node = null;
			c.scene = null;
		}
		return true;
	}
	
	public void removeComponents()
	{
		Component[] comparr = components.toArray(new Component[0]);
		for(Component c : comparr)
			removeComponent(c);
	}
	
	public void removeComponents(List<Component> list)
	{
		for(Component c : list)
			removeComponent(c);
	}
	
	public void removeComponents(Class<? extends Component> clazz)
	{
		removeComponents(getComponents(clazz));
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

	private boolean isTransient = false;
	private float timeRemaining;
	
	/**
	 * Checks to see if this node is marked as transient by <code>markAsTransient</code>;
	 * @return True if this node is transient
	 */
	public boolean isTransient()
	{
		return isTransient;
	}
	
	/**
	 * Marks the current node as a transient node. When the node is marked as transient, it will only exist
	 * for the number of seconds given. 
	 * @param time Time until node is destroyed
	 */
	public void markAsTransient(float time)
	{
		isTransient = true;
		timeRemaining = time;
	}
	
	/**
	 * Iteration through the tree calling <code>update</code> on child nodes and components. 
	 */
	public void update(float deltaf)
	{
		if(isTransient)
		{
			timeRemaining -= deltaf;
			if(timeRemaining <= 0)
			{
				destroy();
				return;
			}
		}
		Component[] componentArr = components.toArray(new Component[0]);
		Node[] nodeArr = children.toArray(new Node[0]);
		
		// Call logic method on child nodes
		for(Component c : componentArr)
		{
			if(!c.isDestroyed())
				c.onUpdate(deltaf);	
		}
		// Call logic method on child nodes
		for(Node n : nodeArr)
		{
			if(!n.isDestroyed())
				n.update(deltaf);	
		}
	}
	
	/**
	 * Runs through the tree creating and queuing RenderOperations into the Renderer. 
	 * <p>
	 * First it computes the world transform for each RenderOperation based on the scene graph transform hierarchy, 
	 * and stores it in the node. Then it computes the Bounds of the node based on the Components and the Bounds of  
	 * the subtree. During this it will load the Renderer with RenderOperations based on the computed data.
	 */
	public void queueRender()
	{
		// Update the world space transforms
		worldTransform.set(transform);
		if(parent != null)
			worldTransform.inherit(parent.worldTransform);
		
		// Call graphics method on child components & update node bounds
		boundingBoxNode = Bounds.getVoid();	// Start off with a void Bounds
		for(Component c : components)
		{
			if(c instanceof Light)
				Renderer.queue((Light) c);
			if(c instanceof Bounded)
				boundingBoxNode.encompass(((Bounded)c).getBounds());
			if(c instanceof Renderable)
				Renderer.queue((Renderable)c);
		}
		
		// Call graphics method on child nodes & update subtree bounds
		boundingBox.set(boundingBoxNode); // Update bounding box with calculated Node bounds
		for(Node n : children)
		{
			boundingBox.encompass(n.getBounds());	// Extend the bounding box to include the bounds of this child
			n.queueRender();
		}
	}
	
	public String toString()
	{
		return String.format("Node[name=%s, " +
				"numChildren=%d, " +
				"numComponents=%d, " +
				"numRenderableComponents=%d, " +
				"hasParent=%b, " +
				"hasScene=%b, " +
				"transform=%s" +
				"world=%s]",
				name, getChildren().size(), components.size(), graphicsComponents.size(), parent != null, scene != null, transform, worldTransform);
	}
}
