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
 * <p>A MergeablePriorityQueue is a PriorityQueue that includes a merge
 * method. All PriorityQueue 
 * implementations enforce distinct elements, and use the
 * {@link Object#hashCode} and {@link Object#equals} methods to
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * @param <E> The type of object contained in the PriorityQueue.
 * @param <T> The type of MergeablePriorityQueue supported by the merge
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public interface MergeablePriorityQueue<E, T extends MergeablePriorityQueue<E, T>> extends PriorityQueue<E> {
	
	/**
	 * <p>Merges another priority queue into this one, adding all of its (element, priority) pairs.
	 * This is a destructive operation with no guarantees to the state of the other priority queue
	 * upon completion. Additionally, implementations of this method may assume that <code>other</code> and <code>this</code>
	 * do not share any elements, and the priority queue may become unstable if they do. The priority order
	 * of both priority queues must be the same (e.g., both minheaps or both maxheaps).</p>
	 *
	 * @param other The priority queue that you want to merge into <code>this</code>. Implementations
	 * need not make any guarantees as to the state of <code>other</code> upon completion.
	 *
	 * @return true if and only if this priority queue changed as a result of the merge
	 */
	boolean merge(T other);
}
