package gui;

import model.Route;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;
import java.util.Vector;

/**
 * Panel displaying routes tree.
 * @author joseph
 */
public class RoutesTreePanel extends JTree implements TreeExpansionListener
{
	private Vector<TreePath> expandedNodes = new Vector<TreePath>();
	private Vector<TreePath> collapsedNodes = new Vector<TreePath>();

	/**
	 * @param rootRoutes Root route
	 */
	public RoutesTreePanel(Route rootRoutes)
	{
		super();
		addTreeExpansionListener(this);
		setModel(rootRoutes);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * Memorize a node has been expanded.
	 * @param treeExpansionEvent {@inheritDoc}
	 */
	@Override
	public void treeExpanded(TreeExpansionEvent treeExpansionEvent)
	{
		expandedNodes.add(treeExpansionEvent.getPath());
		collapsedNodes.remove(treeExpansionEvent.getPath());
		//System.out.println("expand : " + treeExpansionEvent.getPath());
	}

	/**
	 * Memorize a node has been collapsed.
	 * @param treeExpansionEvent {@inheritDoc}
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent treeExpansionEvent)
	{
		expandedNodes.remove(treeExpansionEvent.getPath());
		collapsedNodes.add(treeExpansionEvent.getPath());
		//System.out.println("colapse : " + treeExpansionEvent.getPath());
	}

	/**
	 * Update tree routes model.
	 * @param treePathToRemove Path to the node that will me marked as collapsed
	 * @param treePathToAdd Path to the node that will me marked as expanded, if treePathToRemove is already marked as expanded.
	 */
	public void updateModel(TreePath treePathToRemove, TreePath treePathToAdd)
	{
		if(treePathToAdd != null && expandedNodes.contains(treePathToRemove))
		{
			expandedNodes.add(treePathToAdd);
		}
		updateModel(treePathToRemove);
	}

	/**
	 * Update tree routes model.
	 * @param treePathToRemove Path to the node that will me marked as collapsed
	 */
	public void updateModel(TreePath treePathToRemove)
	{
		expandedNodes.remove(treePathToRemove);
		collapsedNodes.remove(treePathToRemove);
		updateModel();
	}

	/**
	 * Update tree routes model.
	 */
	public void updateModel()
	{
		TreeModel model = getModel();
		Route root = (Route) model.getRoot();
		setModel(new Route(""));
		setModel(root);

		removeTreeExpansionListener(this);
		for(TreePath tp : expandedNodes)
		{
			expandPath(tp);
			//System.out.println("expanding : " + tp);
		}
		for(TreePath tp : collapsedNodes)
		{
			collapsePath(tp);
			//System.out.println("collapsing : " + tp);
		}
		addTreeExpansionListener(this);
	}
}