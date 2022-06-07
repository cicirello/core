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
 * in the interval [0, n), and with priority values of type double.</p> 
 *
 * <p><b>Priority order:</b>
 * IntBinaryHeapDouble instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the IntBinaryHeapDouble. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods. In this example an IntBinaryHeapDouble with an element domain of [0,100)
 * is created:</p>
 * <pre><code>
 * IntBinaryHeapDouble&lt;String&gt; pq = IntBinaryHeapDouble.createMinHeap(100);
 * </code></pre>
 * <p>In the above example, the element domain is [0,100) and the IntBinaryHeapDouble
 * is initially empty.</p>
 *
 * <p><b>Purpose:</b> The purpose of such an IntBinaryHeapDouble is to support
 * implementations of algorithms that require such a specialized case.
 * For example, some graph algorithms such as Dijkstra's algorithm for 
 * single-source shortest paths, and Prim's algorithm for minimum spanning
 * tree, rely on a priority queue of the vertex ids, which are usually
 * ints in some finite range. Although such applications could use the
 * classes that instead implement the {@link PriorityQueueDouble} interface,
 * using Java's wrapper type {@link Integer}, the classes that implement
 * {@link IntPriorityQueueDouble} that specialize the element type to int
 * are optimized for this special case.</p>
 *
 * <p>For a more general purpose binary heap, see the {@link BinaryHeapDouble}
 * class.</p>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap):</p>
 * <ul>
 * <li><b>O(1):</b> {@link #contains(int)}, {@link #createMaxHeap(int)}, 
 *     {@link #createMinHeap(int)}, {@link #domain()}, {@link #isEmpty()},
 *     {@link #peek()}, {@link #peekPriority()}, {@link #peekPriority(int)}, {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #change(int,double)}, {@link #demote(int,double)}, {@link #offer(int, double)}, 
 *     {@link #poll()}, {@link #promote(int,double)}</li>
 * <li><b>O(n):</b> {@link #clear()}, {@link #copy()}</li>
 * </ul>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class IntBinaryHeapDouble implements IntPriorityQueueDouble, Copyable<IntBinaryHeapDouble> {
	
	private final int[] heap;
	private final int[] index;
	private final double[] value;
	private final boolean[] in;
	private int size;
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 */
	private IntBinaryHeapDouble(int n) {
		heap = new int[n];
		index = new int[n];
		value = new double[n];
		in = new boolean[n];
	}
	
	/*
	 * private copy constructor to support copy() nethod
	 */
	private IntBinaryHeapDouble(IntBinaryHeapDouble other) {
		heap = other.heap.clone();
		index = other.index.clone();
		value = other.value.clone();
		in = other.in.clone();
		size = other.size;
	}
	
	@Override
	public IntBinaryHeapDouble copy() {
		return new IntBinaryHeapDouble(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean change(int element, double priority) {
		// note for anyone who may be editing this... 
		// DON'T call offer(element, priority) 
		// doing so will cause problems for the nested private class Max that
		// overrides both change and offer to negate priority.
		if (!in[element]) {
			internalOffer(element, priority);
			return true;
		}
		return internalPromote(element, priority) || internalDemote(element, priority);
	}
	
	@Override
	public final void clear() {
		if (size > 0) {
			Arrays.fill(in, false);
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
	public static IntBinaryHeapDouble createMaxHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntBinaryHeapDouble.Max(n);
	}
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 *
	 * @return an empty min-heap
	 */
	public static IntBinaryHeapDouble createMinHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntBinaryHeapDouble(n);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean demote(int element, double priority) {
		return in[element] && internalDemote(element, priority);
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
	public boolean offer(int element, double priority) {
		if (in[element]) {
			return false;
		}
		internalOffer(element, priority);
		return true;
	}
	
	@Override
	public final int peek() {
		return heap[0];
	}
	
	@Override
	public double peekPriority() {
		return value[heap[0]];
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public double peekPriority(int element) {
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
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean promote(int element, double priority) {
		return in[element] && internalPromote(element, priority);
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	private void internalOffer(int element, double priority) {
		index[heap[size] = element] = size;
		value[element] = priority;
		in[element] = true;
		percolateUp(size);
		size++;
	}
	
	private boolean internalPromote(int element, double priority) {
		if (priority < value[element]) {
			value[element] = priority;
			percolateUp(index[element]);
			return true;
		}
		return false;
	}
	
	private boolean internalDemote(int element, double priority) {
		if (priority > value[element]) {
			value[element] = priority;
			percolateDown(index[element]);
			return true;
		}
		return false;
	}
	
	private void percolateDown(int i) {
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
	
	private void percolateUp(int i) {
		int parent;
		while (i > 0 && value[heap[parent = (i-1) >> 1]] > value[heap[i]]) {
			int temp = heap[i];
			index[heap[i] = heap[parent]] = i;
			index[heap[parent] = temp] = parent;
			i = parent;
		}
	}
	
	private static final class Max extends IntBinaryHeapDouble {
		
		private Max(int n) {
			super(n);
		}
		
		private Max(Max other) {
			super(other);
		}
		
		@Override
		public Max copy() {
			return new Max(this);
		}
		
		@Override
		public boolean change(int element, double priority) {
			return super.change(element, -priority);
		}
		
		@Override
		public boolean demote(int element, double priority) {
			return super.demote(element, -priority);
		}
		
		@Override
		public boolean offer(int element, double priority) {
			return super.offer(element, -priority);
		}
		
		@Override
		public double peekPriority() {
			return -super.peekPriority();
		}
		
		@Override
		public double peekPriority(int element) {
			return -super.peekPriority(element);
		}
		
		@Override
		public boolean promote(int element, double priority) {
			return super.promote(element, -priority);
		}
	}
}
