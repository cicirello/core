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

/**
 * <p>Disjoint sets of integers from [0, n) implemented with a disjoint set forest.
 * An instance of this class represents a set of disjoint sets of the n integers:
 * {0, 1, ..., n-1}. Initially each integer is in a set by itself, and the class provides
 * methods for combining sets and checking if elements are in the same set. It does not
 * support splitting a set. It is implemented as Disjoint Set Forests are described by 
 * Cormen, Leiserson, Rivest, and Stein describe in <i>Introduction to Algorithms</i>,
 * including the use of the union by rank heuristic and path compression.</p>
 *
 * <p>For disjoint sets of objects, see the {@link DisjointSetForest} class.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class DisjointIntegerSetForest {
	
	private final int[] rank;
	private final int[] parent;
	
	/**
	 * Initializes a disjoint set forest containing the 
	 * integers from 0 through n-1, each initially in a set by itself.
	 *
	 * @param n The number of integers in the disjoint set forest.
	 */
	public DisjointIntegerSetForest(int n) {
		rank = new int[n];
		parent = new int[n];
		for (int i = 0; i < n; i++) {
			parent[i] = i;
		}
	}
	
	/**
	 * Finds the id of the set currently containing the element.
	 * Calls to this method also apply path compression to make subsequent
	 * calls faster.
	 *
	 * @param element The element whose set is sought, which must be in [0,n).
	 *
	 * @return the id of the set that currently contains the element, where the id 
	 * is a representative member of that set.
	 *
	 * @throws ArrayIndexOutOfBoundsException if element is less than 0 or if
	 * element is greater than or equal to n.
	 */
	public int findSet(int element) {
		if (parent[element] != element) {
			parent[element] = findSet(parent[element]);
		}
		return parent[element];
	}
	
	/**
	 * Checks whether or not two elements are in the same set.
	 *
	 * @param element1 The first element, which must be in [0,n).
	 * @param element2 The second element, which must be in [0,n).
	 *
	 * @return true if and only if element1 and element2 are elements in the same set
	 * of the disjoint set forest.
	 *
	 * @throws ArrayIndexOutOfBoundsException if element1 or element2 is less than 0 or if
	 * element1 or element2 is greater than or equal to n.
	 */
	public boolean sameSet(int element1, int element2) {
		return findSet(element1) == findSet(element2);
	}
	
	/**
	 * Gets the size of the DisjointIntegerSetForest in total number of
	 * elements.
	 *
	 * @return the number of elements in the DisjointIntegerSetForest
	 */
	public int size() {
		return rank.length;
	}
	
	/**
	 * Merges two sets into one, where the sets are chosen by specifying
	 * a representative member of each. If the two elements are already in
	 * the same set, then no merger takes place.
	 * Calls to this method also apply path compression while finding the sets
	 * containing each of the elements, and the union itself uses the union by rank
	 * heuristic. The combination of these two should improve cost of subsequent operations.
	 *
	 * @param element1 A representative member of one of the sets, which must be in [0,n).
	 * @param element2 A representative member of the other set, which must be in [0,n).
	 *
	 * @throws ArrayIndexOutOfBoundsException if either element1 or element2 is less than 0 or if
	 * element1 ot element2 is greater than or equal to n.
	 */
	public void union(int element1, int element2) {
		link(findSet(element1), findSet(element2));
	}
	
	private void link(int x, int y) {
		if (rank[x] > rank[y]) {
			parent[y] = x;
		} else {
			parent[x] = y;
			if (rank[x] == rank[y]) {
				rank[y]++;
			}
		}
	}
}
