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

import java.util.Arrays;

import org.cicirello.util.Copyable;

/**
 * <p>An implementation of a Binary Heap of
 * (element, priority) pairs, such that the elements are distinct integers
 * in the interval [0, n), and with priority values also of type int.</p> 
 *
 * <p><b>Priority order:</b>
 * IntBinaryHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the IntBinaryHeap. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods. In this example an IntBinaryHeap with an element domain of [0,100)
 * is created:</p>
 * <pre><code>
 * IntBinaryHeap&lt;String&gt; pq = IntBinaryHeap.createMinHeap(100);
 * </code></pre>
 * <p>In the above example, the element domain is [0,100) and the IntBinaryHeap
 * is initially empty.</p>
 *
 * <p><b>Purpose:</b> The purpose of such an IntBinaryHeap is to support
 * implementations of algorithms that require such a specialized case.
 * For example, some graph algorithms such as Dijkstra's algorithm for 
 * single-source shortest paths, and Prim's algorithm for minimum spanning
 * tree, rely on a priority queue of the vertex ids, which are usually
 * ints in some finite range. Although such applications could use the
 * classes that instead implement the {@link PriorityQueue} interface,
 * using Java's wrapper type {@link Integer}, the classes that implement
 * {@link IntPriorityQueue} that specialize the element type to int
 * are optimized for this special case.</p>
 *
 * <p>For a more general purpose binary heap, see the {@link BinaryHeap}
 * class.</p>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap):</p>
 * <ul>
 * <li><b>O(1):</b> {@link #contains(int)}, {@link #createMaxHeap(int)}, 
 *     {@link #createMinHeap(int)}, {@link #domain()}, {@link #isEmpty()},
 *     {@link #peek()}, {@link #peekPriority()}, {@link #peekPriority(int)}, {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #change(int,int)}, {@link #offer(int, int)}, {@link #poll()}</li>
 * <li><b>O(n):</b> {@link #clear()}, {@link #copy()}</li>
 * </ul>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class IntBinaryHeap implements IntPriorityQueue, Copyable<IntBinaryHeap> {
	
	private final int[] heap;
	private final int[] index;
	private final int[] value;
	private final boolean[] in;
	private int size;
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 */
	private IntBinaryHeap(int n) {
		heap = new int[n];
		index = new int[n];
		value = new int[n];
		in = new boolean[n];
	}
	
	/*
	 * private copy constructor to support copy() nethod
	 */
	private IntBinaryHeap(IntBinaryHeap other) {
		heap = other.heap.clone();
		index = other.index.clone();
		value = other.value.clone();
		in = other.in.clone();
		size = other.size;
	}
	
	@Override
	public IntBinaryHeap copy() {
		return new IntBinaryHeap(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public final boolean change(int element, int priority) {
		if (!offer(element, priority)) {
			return internalChange(element, priority);
		}
		return true;
	}
	
	@Override
	public final void clear() {
		if (size > 0) {
			Arrays.fill(in, 0, size, false);
			size = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public final boolean contains(int element) {
		return in[element];
	}
	
	/**
	 * Initializes an empty max-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the max-heap.
	 *
	 * @return an empty max-heap
	 */
	public static IntBinaryHeap createMaxHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntBinaryHeap.Max(n);
	}
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 *
	 * @return an empty min-heap
	 */
	public static IntBinaryHeap createMinHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntBinaryHeap(n);
	}
	
	@Override
	public final int domain() {
		return index.length;
	}
	
	@Override
	public final boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public final boolean offer(int element, int priority) {
		if (in[element]) {
			return false;
		}
		index[heap[size] = element] = size;
		value[element] = priority;
		in[element] = true;
		percolateUp(size);
		size++;
		return true;
	}
	
	@Override
	public final int peek() {
		return heap[0];
	}
	
	@Override
	public final int peekPriority() {
		return value[heap[0]];
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public final int peekPriority(int element) {
		return value[element];
	}
	
	@Override
	public final int poll() {
		int min = heap[0];
		in[min] = false;
		size--;
		if (size > 0) {
			index[heap[0] = heap[size]] = 0;
			percolateDown(0);
		} 
		return min;
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	boolean internalChange(int element, int priority) {
		if (priority < value[element]) {
			value[element] = priority;
			percolateUp(index[element]);
			return true;
		} else if (priority > value[element]) {
			value[element] = priority;
			percolateDown(index[element]);
			return true;
		}
		return false;
	}
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	void percolateDown(int i) {
		int left; 
		while ((left = (i << 1) + 1) < size) { 
			int smallest = i;
			if (value[heap[left]] < value[heap[i]]) {
				smallest = left;
			}
			int right = left + 1;
			if (right < size && value[heap[right]] < value[heap[smallest]]) {
				smallest = right;
			}
			if (smallest != i) {
				int temp = heap[i];
				index[heap[i] = heap[smallest]] = i;
				index[heap[smallest] = temp] = smallest;
				i = smallest; 
			} else {
				break;
			}
		}
	}
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	void percolateUp(int i) {
		int parent;
		while (i > 0 && value[heap[parent = (i-1) >> 1]] > value[heap[i]]) {
			int temp = heap[i];
			index[heap[i] = heap[parent]] = i;
			index[heap[parent] = temp] = parent;
			i = parent;
		}
	}
	
	private static final class Max extends IntBinaryHeap {
		
		private final IntBinaryHeap self;
		
		private Max(int n) {
			super(n);
			self = this;
		}
		
		/*
		 * private copy constructor to support copy() nethod
		 */
		private Max(Max other) {
			super(other);
			self = this;
		}
		
		@Override
		public Max copy() {
			return new Max(this);
		}
		
		/*
		 * max heap order
		 */
		@Override
		boolean internalChange(int element, int priority) {
			if (priority > self.value[element]) {
				self.value[element] = priority;
				percolateUp(self.index[element]);
				return true;
			} else if (priority < self.value[element]) {
				self.value[element] = priority;
				percolateDown(self.index[element]);
				return true;
			}
			return false;
		}
		
		/*
		 * max heap order
		 */
		@Override
		void percolateDown(int i) {
			int left; 
			while ((left = (i << 1) + 1) < self.size) { 
				int smallest = i;
				if (self.value[self.heap[left]] > self.value[self.heap[i]]) {
					smallest = left;
				}
				int right = left + 1;
				if (right < self.size && self.value[self.heap[right]] > self.value[self.heap[smallest]]) {
					smallest = right;
				}
				if (smallest != i) {
					int temp = self.heap[i];
					self.index[self.heap[i] = self.heap[smallest]] = i;
					self.index[self.heap[smallest] = temp] = smallest;
					i = smallest; 
				} else {
					break;
				}
			}
		}
		
		/*
		 * max heap order
		 */
		@Override
		void percolateUp(int i) {
			int parent;
			while (i > 0 && self.value[self.heap[parent = (i-1) >> 1]] < self.value[self.heap[i]]) {
				int temp = self.heap[i];
				self.index[self.heap[i] = self.heap[parent]] = i;
				self.index[self.heap[parent] = temp] = parent;
				i = parent;
			}
		}
	}
}
