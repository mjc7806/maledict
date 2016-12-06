package net.mjcarpenter.maledict.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.mjcarpenter.maledict.crypto.spn.Key;
import net.mjcarpenter.maledict.crypto.spn.Permutation;
import net.mjcarpenter.maledict.crypto.spn.Round;
import net.mjcarpenter.maledict.crypto.spn.SBox;
import net.mjcarpenter.maledict.crypto.spn.SPNComponent;
import net.mjcarpenter.maledict.crypto.spn.SPNetwork;
import net.mjcarpenter.maledict.ui.dialog.component.ComponentDefinitionDialog;

public class RoundTreeNode extends ComponentTreeNode<Round>
{
	private ComponentTreeNode<SPNetwork> parent;
	List<ComponentLeafNode<? extends SPNComponent>> children;
	
	public RoundTreeNode(Round component, ComponentTreeNode<SPNetwork> parent)
	{
		super(component, Round.class);
		this.parent = parent;
		
		ArrayList<ComponentLeafNode<? extends SPNComponent>> children = new ArrayList<ComponentLeafNode<? extends SPNComponent>>();
		
		// Define and display children in the order that they will execute when the Round runs
		children.add(new KeyTreeNode(this.component.getSubKey(), this));
		for(SBox each: this.component.getSBoxes())
		{
			children.add(new SBoxTreeNode(each, this));
		}
		children.add(new PermutationTreeNode(this.component.getPermutation(), this));
		
		this.children = Collections.unmodifiableList(children);
	}
	
	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return children.get(childIndex);
	}

	@Override
	public int getChildCount()
	{
		return children.size();
	}

	@Override
	public TreeNode getParent()
	{
		return parent;
	}
	
	public int indexOnParent()
	{
		return parent.getIndex(this);
	}

	@Override
	public int getIndex(TreeNode node)
	{
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren()
	{
		return true;
	}

	@Override
	public boolean isLeaf()
	{
		return false;
	}

	@Override
	public Enumeration<ComponentLeafNode<? extends SPNComponent>> children()
	{
		return Collections.enumeration(children);
	}
	
	public String toString()
	{
		return String.format("Round[%d]", indexOnParent());
	}
	
	public ComponentDefinitionDialog<Round> editWithDialog()
	{
		// Rounds don't have their own dialog.
		throw new UnsupportedOperationException();
	}

	@Override
	public void refreshComponent()
	{
		List<SBox> newBoxes = new ArrayList<SBox>();//[component.getSBoxes().length];
		Key newKey = null;
		Permutation newPerm = null;
		
		for(ComponentLeafNode<?> child: children)
		{
			SPNComponent comp = child.getComponent();
			
			if(comp instanceof Key)
			{
				newKey = (Key)comp;
			}
			else if(comp instanceof Permutation)
			{
				newPerm = (Permutation)comp;
			}
			else if(comp instanceof SBox)
			{
				newBoxes.add((SBox)comp);
			}
		}
		
		component = new Round(
				component.bitLength(),
				newKey,
				newPerm,
				newBoxes.toArray(new SBox[component.getSBoxes().length]));
		
		parent.refreshComponent();
	}
}
