/*
 * Module org.cicirello.core
 * Copyright 2019-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of module org.cicirello.core.
 *
 * Module org.cicirello.core is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 *
 * Module org.cicirello.core is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with module org.cicirello.core.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package org.cicirello.ds;

import java.util.HashMap;

/**
 * <p>Represents disjoint sets of objects with a disjoint set forest. This class assumes
 * that the objects in the sets are of a class that has appropriately implemented the
 * {@link Object#equals} and {@link Object#hashCode} methods. If you rely on the default
 * implementations of those methods from the {@link Object} class, then the disjoint set
 * forest will treat two different objects containing same data as different objects (i.e.,
 * the default behavior of equals and hashCode when not overridden). 
 * Each object when first added to the DisjointSetForest is initially in a set by itself.
 * The class provides methods for combining sets and checking if elements are in the same 
 * set, or in any set. It does not support splitting a set. It is implemented as Disjoint 
 * Set Forests are described by Cormen, Leiserson, Rivest, and Stein describe in <i>Introduction 
 * to Algorithms</i>, including the use of the union by rank heuristic and path compression.</p>
 *
 * <p>For disjoint sets of integers, see the {@link DisjointIntegerSetForest} class.</p>
 *
 * @param <T> The type of object contained in the DisjointSetForest.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class DisjointSetForest<T> {
	
	private final HashMap<T, Node> nodes;
	private int size;
	
	/**
	 * Initializes an empty DisjointSetForest.
	 */
	public DisjointSetForest() {
		nodes = new HashMap<T, Node>();
	}
	
	/**
	 * Checks if an element is in the disjoint set forest.
	 *
	 * @param element The element to check
	 *
	 * @return true if and only if element is in the disjoint set forest.
	 */
	public boolean contains(T element) {
		return nodes.containsKey(element);
	}
	
	/**
	 * Finds the id of the set currently containing the element.
	 * Calls to this method also apply path compression to make subsequent
	 * calls faster.
	 *
	 * @param element The element whose set is sought.
	 *
	 * @return the id of the set that currently contains the element.
	 *
	 * @throws IllegalArgumentException if element is not in the DisjointSetForest.
	 */
	public int findSet(T element) {
		Node node = nodes.get(element);
		if (node == null) {
			throw new IllegalArgumentException("Element not in disjoint set forest: " + element.toString());
		}
		return findSet(node).id;
	}
	
	/**
	 * Adds a new set to the disjoint set forest containing only the new element.
	 * 
	 * @param element The new element to add to the disjoint set forest, which must
	 * not already exist within the DisjointSetForest.
	 *
	 * @throws IllegalArgumentException if element already exists in the DisjointSetForest.
	 */
	public void makeSet(T element) {
		if (nodes.containsKey(element)) {
			throw new IllegalArgumentException("Already contains this element: " + element.toString());
		} else {
			nodes.put(element, new Node());
		}
	}
	
	/**
	 * Gets the size of the DisjointSetForest.
	 *
	 * @return the number of elements in the DisjointSetForest.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Merges two sets into one, where the sets are chosen by specifying
	 * a representative member of each. If the two elements are already in
	 * the same set, then no merger takes place.
	 * Calls to this method also apply path compression while finding the sets
	 * containing each of the elements, and the union itself uses the union by rank
	 * heuristic. The combination of these two should improve cost of subsequent operations.
	 *
	 * @param element1 A representative member of one of the sets.
	 * @param element2 A representative member of the other set.
	 * 
	 * @throws IllegalArgumentException if either of the elements doesn't exist in the 
	 * DisjointSetForest.
	 */
	public void union(T element1, T element2) {
		Node x = nodes.get(element1);
		Node y = nodes.get(element2);
		if (x != null && y != null) {
			link(findSet(x), findSet(y));
		} else {
			throw new IllegalArgumentException("One or both of the elements does not exist.");
		}
	}
	
	private Node findSet(Node node) {
		if (node.parent != node) {
			node.parent = findSet(node.parent);
		}
		return node.parent;
	}
	
	private void link(Node x, Node y) {
		if (x.rank > y.rank) {
			y.parent = x;
		} else {
			x.parent = y;
			if (x.rank == y.rank) {
				y.rank++;
			}
		}
	}
	
	private class Node {
		private int rank;
		private Node parent;
		private final int id;
		
		private Node() {
			parent = this;
			id = size;
			size++;
		}
	}
}
