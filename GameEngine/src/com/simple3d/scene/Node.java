


package com.simple3d.scene;

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

import com.simple3d.Graphics;
import com.simple3d.Logic;
import com.simple3d.Physics;
import com.simple3d.math.Matrix;
import com.simple3d.math.Transform;
import com.simple3d.math.Vector3f;
import com.simple3d.math.Vector4f;
import com.simple3d.math.geometry.Bounded;
import com.simple3d.math.geometry.Box;

public class Node implements Logic, Graphics, Bounded, Physics
{
	protected Box boundingBox = new Box();
	protected Box boundingBoxNode = new Box();
	
	protected Scene scene;
	protected Node parent;
	protected Transform transform = new Transform();
	private LinkedHashSet<Node> children = new LinkedHashSet<Node>();
	private LinkedHashSet<Logic> logicalComponents = new LinkedHashSet<Logic>();
	private LinkedHashSet<Physics> physicsComponents = new LinkedHashSet<Physics>();
	private LinkedHashSet<Graphics> graphicsComponents = new LinkedHashSet<Graphics>();
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
	
	public Transform getAbsoluteTransform()
	{
		// Go up the chain to the root, then back out, applying transforms. End result will be absolute transform referenced to the identity
		if(parent == null)
			return new Transform();
		Transform pt = parent.getAbsoluteTransform();
		return pt.transformInto(transform, pt);
	}
	
	private void updateBounds()
	{
		// TODO calculate bounds of node -> boundedNode
	}
	
	public Box getBounds()
	{
		return boundingBox;
	}
	
	public Box getNodeBounds()
	{
		return boundingBoxNode;
	}

//	updateloop 
//	- update bounding boxes
//	- update transform matricies
//	- update components
//	- update children
	
	@Override
	public void logic(int delta)
	{
		if(!enabled)
			return;
		transformMatrix = transform.getTransformMatrix().toFloatArray(Matrix.COL_MAJOR);
		
		modifiedNode = modifiedComponent|modifiedTransform;	// is node modified?

		for(Logic c : logicalComponents)
		{
			c.logic(delta);	
		}
		
		if(modifiedComponent || modifiedTransform) // update current node bounds if changed
			updateBounds();
		
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
	
	public void graphics()
	{
		if(!enabled)	// If this node is disabled, don't continue!
			return;
		
		// TODO Now we need to transform the modelview, using our Transform object. Basic implementation, needs speedup
		Vector3f pos = transform.getPosition();
		Vector3f scale = transform.getScale();
		Vector4f rot = transform.getRotation().getAxisAngle();
		GL11.glTranslatef(pos.x, pos.y, pos.z);
		GL11.glScalef(scale.x, scale.y, scale.z);
		GL11.glRotatef(rot.w, rot.x, rot.y, rot.z);

		// Now we need to save this transform so we can reload it later
		ByteBuffer vbb = ByteBuffer.allocateDirect(64); 
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = vbb.asFloatBuffer();
		GL11.glGetFloat(GL_MODELVIEW_MATRIX, fb);
		
		for(Graphics rc : graphicsComponents)	// Do component renders
		{
			rc.graphics();
			GL11.glLoadMatrix(fb);	// Reload the node transform
		}
		
		for(Node n : children)
		{
			n.graphics();
			GL11.glLoadMatrix(fb);	// Reload the node transform
		}
	}
	
	public void physics(int delta)
	{
		if(!enabled)
			return;
		for(Physics pc : physicsComponents)
			pc.physics(delta);
		for(Node n : children)
			n.physics(delta);
	}
}
