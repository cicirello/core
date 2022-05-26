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
 * <p>An implementation of a Fibonacci Heap of
 * (element, priority) pairs, such that the elements are distinct integers
 * in the interval [0, n), and with priority values of type int.</p> 
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article:
 * M. L. Fredman and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network
 * Optimization Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.</p>
 *
 * <p><b>Priority order:</b>
 * IntFibonacciHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the IntFibonacciHeap. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods. In this example an IntFibonacciHeap with an element domain of [0,100)
 * is created:</p>
 * <pre><code>
 * IntFibonacciHeap&lt;String&gt; pq = IntFibonacciHeap.createMinHeap(100);
 * </code></pre>
 * <p>In the above example, the element domain is [0,100) and the IntFibonacciHeap
 * is initially empty.</p>
 *
 * <p><b>Purpose:</b> The purpose of such an IntFibonacciHeap is to support
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
 * <p>For a more general purpose binary heap, see the {@link FibonacciHeapDouble}
 * class.</p>
 *
 * <p><b>Method runtimes:</b>
 * The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap). Note that 
 * in many cases in this list, the runtimes are amortized time and not actual 
 * time (see a reference on Fibonacci heaps for details).</p>
 * <ul>
 * <li><b>O(1):</b> {@link #contains(int)}, {@link #createMaxHeap(int)}, 
 *     {@link #createMinHeap(int)}, {@link #domain()}, {@link #isEmpty()}, {@link #offer(int, int)},
 *     {@link #peek()}, {@link #peekPriority()}, {@link #peekPriority(int)}, {@link #promote(int,int)}, 
 *     {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #demote(int,int)}, {@link #poll()}</li>
 * <li><b>O(n):</b> {@link #clear()}, {@link #copy()}</li>
 * </ul>
 * <p>The amortized runtime of {@link #change(int,int)} depends on the direction of change. If the
 * priority is decreased for a min-heap or increased for a max-heap, the amortized runtime
 * of {@link #change(int,int)} is O(1); but if the priority is increased for a min-heap or decreased
 * for a max-heap, then the amortized time of {@link #change(int,int)} is O(lg n).</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class IntFibonacciHeap implements IntPriorityQueue, Copyable<IntFibonacciHeap> {
	
	private final Node[] index;
	private Node min;
	private int size;
	
	// This array is what is referred to in CLRS description
	// of algorithm as A in the method consolidate. As an optimization
	// we construct the array once, and reuse it on all calls to consolidate.
	private final Node[] rootsByDegrees;
	
	private final static double INV_LOG_GOLDEN_RATIO = 2.0780869212350273;
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 */
	private IntFibonacciHeap(int n) {
		index = new Node[n];
		
		// length of array used by consolidate is initialized to 45 as follows:
		// 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
		// 2) Thus, the highest that D(n) can be for a call to consolidate is:
		//    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
		// 3) Array must be of length 1+D(n), so longest array must be is 45.
		// 4) consolidate computes the actual D(n) for a specific call, and uses only
		//    part of this array.
		rootsByDegrees = new Node[45];
		
		// Uses the usual defaults for the following:
		//    min = null;
		//    size = 0;
	}
	
	/*
	 * private copy constructor to support copy() nethod
	 */
	private IntFibonacciHeap(IntFibonacciHeap other) {
		size = other.size;
		index = new Node[other.index.length];
		min = size > 0 ? other.min.copy(index) : null;
		
		// length of array used by consolidate is initialized to 45 as follows:
		// 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
		// 2) Thus, the highest that D(n) can be for a call to consolidate is:
		//    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
		// 3) Array must be of length 1+D(n), so longest array must be is 45.
		// 4) consolidate computes the actual D(n) for a specific call, and uses only
		//    part of this array.
		rootsByDegrees = new Node[45];
	}
	
	@Override
	public IntFibonacciHeap copy() {
		return new IntFibonacciHeap(this);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean change(int element, int priority) {
		// note for anyone who may be editing this... 
		// DON'T call offer(element, priority) 
		// doing so will cause problems for the nested private class Max that
		// overrides both change and offer to negate priority.
		if (index[element] == null) {
			internalOffer(element, priority);
			return true;
		}
		return internalPromote(element, priority) || internalDemote(element, priority);
	}
	
	@Override
	public final void clear() {
		size = 0;
		min = null;
		Arrays.fill(index, null);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public final boolean contains(int element) {
		return index[element] != null;
	}
	
	/**
	 * Initializes an empty max-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the max-heap.
	 *
	 * @return an empty max-heap
	 *
	 * @throws IllegalArgumentException if n is non-positive
	 */
	public static IntFibonacciHeap createMaxHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntFibonacciHeap.Max(n);
	}
	
	/**
	 * Initializes an empty min-heap of (int, priority) pairs,
	 * such that the domain of the elements are the integers in [0, n).
	 *
	 * @param n The size of the domain of the elements of the min-heap.
	 *
	 * @return an empty min-heap
	 *
	 * @throws IllegalArgumentException if n is non-positive
	 */
	public static IntFibonacciHeap createMinHeap(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("domain must be positive");
		}
		return new IntFibonacciHeap(n);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean demote(int element, int priority) {
		return index[element] != null && internalDemote(element, priority);
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
	public boolean offer(int element, int priority) {
		if (index[element] != null) {
			return false;
		}
		internalOffer(element, priority);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws NullPointerException if empty
	 */
	@Override
	public final int peek() {
		return min.element;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws NullPointerException if empty
	 */
	@Override
	public int peekPriority() {
		return min.priority;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 * * @throws NullPointerException if element is not in the heap
	 */
	@Override
	public int peekPriority(int element) {
		return index[element].priority;
	}
	
	@Override
	public final int poll() {
		if (size == 1) {
			int e = min.element;
			index[e] = null; 
			min = null;
			size = 0;
			return e;
		} else if (size > 1) {
			Node z = min;
			if (z.child != null) {
				z.child.clearParentReferences();
				z.child.insertListInto(min);
			}
			min = min.right;
			z.left.right = min;
			min.left  = z.left;
			consolidate();
			index[z.element] = null;
			size--;
			return z.element;
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public boolean promote(int element, int priority) {
		return index[element] != null && internalPromote(element, priority);
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	private void consolidate() {
		int dn = (int)(Math.log(size) * INV_LOG_GOLDEN_RATIO);
		
		// first node of iteration
		Node w = min;
		// disconnect from left to enable detecting end of list
		w.left.right = null;
		do{
			Node x = w;
			// prepare for next iteration
			w = w.right;
			// disconnect x from root list
			x.left = x.right = null;
			
			int d = x.degree;
			while (rootsByDegrees[d] != null) {
				Node y = rootsByDegrees[d];
				if (y.priority < x.priority) {
					Node temp = x;
					x = y;
					y = temp;
				}
				fibHeapLink(y, x);
				rootsByDegrees[d] = null;
				d++;
			}
			rootsByDegrees[d] = x;
		} while (w != null);
		
		min = null;
		for (int i = 0; i <= dn; i++) {
			if (rootsByDegrees[i] != null) {
				if (min == null) {
					rootsByDegrees[i].singletonList();
					min = rootsByDegrees[i];
				} else {
					rootsByDegrees[i].insertInto(min);
					if (rootsByDegrees[i].priority < min.priority) {
						min = rootsByDegrees[i];
					}
				}
				// need this since this array is shared by all calls to consolidate
				rootsByDegrees[i] = null;
			}
		}
	}
	
	private void fibHeapLink(Node y, Node x) {
		// 1. Remove y from root list step.
		//    This is not needed because I'm instead
		//    dismantling root list in consolidate
		//    before rebuilding it.
		// 2. Make y a child of x
		if (x.degree > 0) {
			y.insertInto(x.child);
			y.parent = x;
			x.degree++;
		} else {
			y.singletonList();
			x.child = y;
			y.parent = x;
			x.degree = 1;
		}
		y.mark = false;
	}
	
	private void internalOffer(int element, int priority) {
		if (min == null) {
			index[element] = min = new Node(element, priority);
			size = 1;
		} else {
			index[element] = new Node(element, priority, min);
			if (priority < min.priority) {
				min = index[element];
			}
			size++;
		}
	}
	
	private boolean internalPromote(int element, int priority) {
		Node x = index[element];
		if (priority < x.priority) {
			x.priority = priority;
			Node y = x.parent;
			if (y != null && priority < y.priority) {
				cut(x, y);
				cascadingCut(y);			
			}
			if (priority < min.priority) {
				min = x;
			}
			return true;
		}
		return false;
	}
	
	private void cut(Node x, Node y) {
		// 1. remove x from child list of y, decrementing degree of y
		if (y.degree > 1) {
			// ensure y's child reference isn't x
			y.child = x.right;
			// link x's left and right neighbors to remove x
			x.left.right = x.right;
			x.right.left = x.left;
			y.degree--;
		} else {
			y.child = null;
			y.degree = 0;
		}
		// 2. add x to the root list
		x.insertInto(min);
		x.parent = null;
		x.mark = false;
	}
	
	private void cascadingCut(Node y) {
		Node z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}
	
	private boolean internalDemote(int element, int priority) {
		if (priority > index[element].priority) {
			// 1. promote (opposite) to front
			internalPromote(element, min.priority-1);
			// 2. poll() to remove
			poll();
			// 3. reinsert with new priority
			internalOffer(element, priority);
			return true;
		}
		return false;
	}
	
	private class Node {
		
		private final int element;
		private int priority;
		
		private Node parent;
		private Node child;
		private Node left;
		private Node right;
		private int degree;
		private boolean mark;
		
		/*
		 * new root list (i.e., called to create new top-level list when empty
		 */
		public Node(int element, int priority) {
			this.element = element;
			this.priority = priority;
			singletonList();
		}
		
		/*
		 * adds newly constructed node to root list
		 */
		public Node(int element, int priority, Node list) {
			this.element = element;
			this.priority = priority;
			insertInto(list);
		}
		
		private Node(Node other) {
			element = other.element;
			priority = other.priority;
			degree = other.degree;
			mark = other.mark;
		}
		
		private Node(Node other, Node toTheLeft) {
			this(other);
			left = toTheLeft;
		}
		
		private Node copy(Node[] indexCopy) {
			return copyList(this, null, indexCopy);
		}
		
		private Node copyList(Node x, Node p, Node[] indexCopy) {
			Node y = new Node(x);
			indexCopy[y.element] = y;
			y.parent = p;
			if (x.child != null) {
				y.child = copyList(x.child, y, indexCopy);
			}
			Node rightOf = y;
			for (Node next = x.right; next != x; next = next.right, rightOf = rightOf.right) {
				rightOf.right = new Node(next, rightOf);
				indexCopy[rightOf.right.element] = rightOf.right;
				rightOf.right.parent = p;
				if (next.child != null) {
					rightOf.right.child = copyList(next.child, rightOf.right, indexCopy);
				}
			}
			rightOf.right = y;
			y.left = rightOf;
			return y;
		}
		
		private void singletonList() {
			left = right = this;
		}

		private void insertInto(Node list) {
			right = list.right;
			left = list;
			list.right = list.right.left = this;
		}
		
		private void insertListInto(Node list) {
			list.right.left = left;
			left.right = list.right;
			list.right = this;
			left = list;
		}
		
		private void clearParentReferences() {
			for (Node next = this; next.parent != null; next = next.right) {
				next.parent = null;
			}
		}
	}
	
	
	private static final class Max extends IntFibonacciHeap {
		
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
		public boolean change(int element, int priority) {
			return super.change(element, -priority);
		}
		
		@Override
		public boolean demote(int element, int priority) {
			return super.demote(element, -priority);
		}
		
		@Override
		public boolean offer(int element, int priority) {
			return super.offer(element, -priority);
		}
		
		@Override
		public int peekPriority() {
			return -super.peekPriority();
		}
		
		@Override
		public int peekPriority(int element) {
			return -super.peekPriority(element);
		}
		
		@Override
		public boolean promote(int element, int priority) {
			return super.promote(element, -priority);
		}
	}
}
