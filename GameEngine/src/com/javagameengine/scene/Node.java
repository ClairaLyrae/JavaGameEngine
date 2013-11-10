


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

import com.javagameengine.Graphics;
import com.javagameengine.Logic;
import com.javagameengine.Physics;
import com.javagameengine.math.Matrix;
import com.javagameengine.math.Transform;
import com.javagameengine.math.Vector3f;
import com.javagameengine.math.Vector4f;

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
public class Node implements Logic, Physics, Graphics, Bounded
{
	protected Bounds boundingBox = new Bounds();
	protected Bounds boundingBoxNode = new Bounds();
	
	protected Scene scene;
	protected Node parent;
	protected Transform transform = new Transform();
	private LinkedHashSet<Node> children = new LinkedHashSet<Node>();
	private LinkedHashSet<Logic> logicalComponents = new LinkedHashSet<Logic>();
	private LinkedHashSet<Physics> physicsComponents = new LinkedHashSet<Physics>();
	private LinkedHashSet<Graphics> graphicsComponents = new LinkedHashSet<Graphics>();
	private LinkedHashSet<Bounded> boundedComponents = new LinkedHashSet<Bounded>();
	private LinkedHashSet<Component> components = new LinkedHashSet<Component>();
	protected String name;
	
	protected boolean enabled = true;	// setting to false will ignore this subtree!
	
	protected boolean modifiedNode = false;
	protected boolean modifiedTransform = false;
	protected boolean modifiedChildren = false;
	protected boolean modifiedComponent = false;
	
	protected float[] transformMatrix = new float[4];
	
	public Node(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean b)
	{
		enabled = b;
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
	
	public boolean addChild(Node n)
	{
		modifiedChildren = true;
		return children.add(n);
	}
	
	public boolean removeChild(Node n)
	{
		modifiedChildren = true;
		return children.remove(n);
	}
	
	public List<Node> removeChildren()
	{
		modifiedChildren = true;
		List<Node> list = getChildren();
		children.clear();
		return list;
	}
	
	public List<Node> removeChildren(List<Node> list)
	{
		modifiedChildren = true;
		List<Node> rem = new ArrayList<Node>();
		for(Node n : list)
		{
			if(removeChild(n))
				rem.add(n);
		}
		return rem;
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
	public List<Component> getPhysicsComponents()
	{
		return new ArrayList<Component>((Collection<? extends Component>)physicsComponents);
	}

	@SuppressWarnings("unchecked")
	public List<Component> getLogicComponents()
	{
		return new ArrayList<Component>((Collection<? extends Component>)logicalComponents);
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
		modifiedComponent = true;
		c.node = this;
		if(c instanceof Graphics)
			graphicsComponents.add((Graphics)c);
		if(c instanceof Physics)
			physicsComponents.add((Physics)c);
		if(c instanceof Logic)
			logicalComponents.add((Logic)c);
		if(c instanceof Bounded)
			boundedComponents.add((Bounded)c);
		return components.add(c);
	}

	public boolean removeComponent(Component c)
	{
		boolean removed = components.remove(c);
		if(!removed)
			return false;
		c.node = null;
		modifiedComponent = true;
		if(c instanceof Graphics)
			graphicsComponents.remove((Graphics)c);
		if(c instanceof Physics)
			physicsComponents.remove((Physics)c);
		if(c instanceof Logic)
			logicalComponents.remove((Logic)c);
		if(c instanceof Bounded)
			boundedComponents.remove((Bounded)c);
		return true;
	}
	
	public List<Component> removeComponents()
	{
		if(components.size() == 0)
			return Collections.emptyList();
		modifiedComponent = true;
		List<Component> list = getComponents();
		components.clear();
		graphicsComponents.clear();
		physicsComponents.clear();
		logicalComponents.clear();
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
	
	public void setTransform(Transform t)
	{
		transform = t;
		modifiedTransform = true;
	}
	
	/**
	 * Iterates up the tree until the root is reached, then back down applying transforms one
	 * by one starting from the identity transform to each child's transform until back at this 
	 * node. Returns the result.
	 * @return Total transform in relation to origin
	 */
	public Transform getAbsoluteTransform()
	{
		// Go up the chain to the root, then back out, applying transforms. End result will be absolute transform referenced to the identity
		if(parent == null)
			return new Transform();
		Transform pt = parent.getAbsoluteTransform();
		return pt.transformInto(transform, pt);
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
		// Ignore if disabled
		if(!enabled)
			return;
		
		modifiedNode = modifiedComponent|modifiedTransform;	// is node modified?

		for(Logic c : logicalComponents)
		{
			c.logic(delta);	
		}
		
		if(modifiedComponent || modifiedTransform) // update current node bounds if changed
		{
			
		}
		
		boundingBox.set(boundingBoxNode); // update bounding box with current Node bounds
		
		for(Node n : children)
		{
			// Things before subtree of n
			
			n.logic(delta);	// Call update
			modifiedNode |= n.modifiedNode;	// if any child node is modified, this one has been too
			
			// things after subtree of n
			
			boundingBox.extend(n.getBounds());	// Update running limit box with the bounding box of this child
		}
		
		modifiedComponent = false;
		modifiedTransform = false;		  
	}
	
	/**
	 * Updates the MODELVIEW_MATRIX by applying the transform at this node, then iterates
	 * through the tree calling graphics on child nodes and components.
	 */
	public void graphics()
	{
		// Ignore if disabled
		if(!enabled)	
			return;
		
		// Now we need to transform the modelview, using our Transform object. 
		// TODO This can maybe be done more elegantly?
		Vector3f pos = transform.getPosition();
		Vector3f scale = transform.getScale();
		Vector4f rot = transform.getRotation().getAxisAngle();
		GL11.glTranslatef(pos.x, pos.y, pos.z);
		GL11.glScalef(scale.x, scale.y, scale.z);
		GL11.glRotatef(rot.w, rot.x, rot.y, rot.z);

		// Now we need to save this transform so we can reload it later. 
		// TODO The buffer involved shouldn't be created each time, it should be a permanent variable somewhere
		ByteBuffer vbb = ByteBuffer.allocateDirect(64); 
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = vbb.asFloatBuffer();
		GL11.glGetFloat(GL_MODELVIEW_MATRIX, fb);
		
		// Call graphics method on child components
		for(Graphics rc : graphicsComponents)
		{
			rc.graphics();
			GL11.glLoadMatrix(fb);	// Reload the node transform
		}
		
		// Call graphics method on child nodes
		for(Node n : children)
		{
			n.graphics();
			GL11.glLoadMatrix(fb);	// Reload the node transform
		}
	}
	

	/**
	 * Iterates through the tree calling physics on child nodes and components. 
	 * <p>
	 * Don't worry about physics yet, or at all.
	 */
	public void physics(int delta)
	{
		// Ignore if disabled
		if(!enabled)
			return;
		
		// Call physics method on child nodes
		for(Physics pc : physicsComponents)
			pc.physics(delta);
		
		// Call physics method on child nodes
		for(Node n : children)
			n.physics(delta);
	}
}
