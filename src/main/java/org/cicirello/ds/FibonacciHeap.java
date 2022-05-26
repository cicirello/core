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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;

import org.cicirello.util.Copyable;

/**
 * <p>An implementation of a Fibonacci Heap. An instance of a FibonacciHeap
 * contains (element, priority) pairs, such that the elements are distinct.
 * The priority values are of type int.</p> 
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article:
 * M. L. Fredman and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network
 * Optimization Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.</p>
 *
 * <p><b>Priority order:</b>
 * FibonacciHeap instances are created via factory methods with names beginning
 * with <code>create</code>. The priority order depends upon the factory method
 * used to create the FibonacciHeap. Methods named <code>createMinHeap</code> produce
 * a min heap with priority order minimum-priority-first-out. Methods named 
 * <code>createMaxHeap</code> produce a max heap with priority order 
 * maximum-priority-first-out.</p>
 *
 * <p><b>Distinctness:</b>
 * The {@link Object#hashCode} and {@link Object#equals} methods are used
 * to enforce distinctness, so be sure that the class of the elements
 * properly implements these methods, or else behavior is not guaranteed.</p>
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory
 * methods, such as with:</p>
 * <pre><code>
 * FibonacciHeap&lt;String&gt; pq = FibonacciHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of
 * this class are as follows (where n is the current size of the heap and m is the size of
 * a Collection parameter where relevant). Note that in many cases in this list, the
 * runtimes are amortized time and not actual time (see a reference on Fibonacci heaps for
 * details).</p>
 * <ul>
 * <li><b>O(1):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, 
 *     {@link #contains}, {@link #createMaxHeap()}, 
 *     {@link #createMinHeap()}, {@link #element}, {@link #isEmpty}, {@link #iterator},
 *     {@link #offer(E, int)}, {@link #offer(PriorityQueueNode.Integer)},
 *     {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link #peekPriority(E)},
 *     {@link #promote}, {@link #size()}</li>
 * <li><b>O(lg n):</b> {@link #demote}, {@link #poll}, {@link #pollElement}, {@link #remove()},
 *      {@link #remove(Object)}, {@link #removeElement()}</li> 
 * <li><b>O(n):</b> {@link #clear}, {@link #copy()}, {@link #equals}, {@link #hashCode}, 
 *     {@link #toArray()}, {@link #toArray(Object[])}</li>
 * <li><b>O(m):</b> {@link #addAll(Collection)}, {@link #containsAll(Collection)}, 
 *     {@link #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}</li>
 * <li><b>O(m lg n):</b> {@link #removeAll(Collection)}</li>
 * <li><b>O(n lg n):</b> {@link #retainAll(Collection)}</li>
 * </ul>
 * <p>The amortized runtime of {@link #change} depends on the direction of change. If the
 * priority is decreased for a min-heap or increased for a max-heap, the amortized runtime
 * of {@link #change} is O(1); but if the priority is increased for a min-heap or decreased
 * for a max-heap, then the amortized time of {@link #change} is O(lg n).</p>
 *
 * @param <E> The type of object contained in the FibonacciHeap.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class FibonacciHeap<E> implements PriorityQueue<E>, Copyable<FibonacciHeap<E>> {
	
	private final HashMap<E, Node<E>> index;
	private final PriorityComparator compare;
	private final int extreme;
	
	private int size;
	private Node<E> min;
	
	// This array is what is referred to in CLRS description
	// of algorithm as A in the method consolidate. As an optimization
	// we construct the array once, and reuse it on all calls to consolidate.
	private final Node<E>[] rootsByDegrees;
	
	private final static double INV_LOG_GOLDEN_RATIO = 2.0780869212350273;
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty FibonacciHeap.
	 */
	private FibonacciHeap() {
		this((p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 *
	 * Initializes an empty FibonacciHeap.
	 */
	private FibonacciHeap(PriorityComparator compare) {
		this.compare = compare;
		extreme = compare.comesBefore(0, 1) ? java.lang.Integer.MAX_VALUE : java.lang.Integer.MIN_VALUE;
		index = new HashMap<E, Node<E>>();
		// length of array used by consolidate is initialized to 45 as follows:
		// 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
		// 2) Thus, the highest that D(n) can be for a call to consolidate is:
		//    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
		// 3) Array must be of length 1+D(n), so longest array must be is 45.
		// 4) consolidate computes the actual D(n) for a specific call, and uses only
		//    part of this array.
		rootsByDegrees = nodeArrayAllocate(45);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a FibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	private FibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		this(initialElements, (p1, p2) -> p1 < p2);
	}
	
	/* 
	 * PRIVATE: Use factory methods for creation.
	 * Initializes a FibonacciHeap from a collection of (element, priority) pairs.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	private FibonacciHeap(Collection<PriorityQueueNode.Integer<E>> initialElements, PriorityComparator compare) {
		this(compare);
		for (PriorityQueueNode.Integer<E> element : initialElements) {
			if (index.containsKey(element.element)) {
				throw new IllegalArgumentException("initialElements contains duplicates");
			}
			internalOffer(element.copy());
		}
	}
	
	/*
	 * private copy constructor to support the copy() method.
	 */
	private FibonacciHeap(FibonacciHeap<E> other) {
		this(other.compare);
		size = other.size;
		min = other.min.copy(index);
	}
	
	@Override
	public FibonacciHeap<E> copy() {
		return new FibonacciHeap<E>(this);
	}
	
	/**
	 * Creates an empty FibonacciHeap with minimum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return an empty FibonacciHeap with a minimum-priority-first-out priority order
	 */
	public static <E> FibonacciHeap<E> createMinHeap() {
		return new FibonacciHeap<E>();
	}
	
	/**
	 * Creates a FibonacciHeap from a collection of (element, priority) pairs,
	 * with a minimum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return a FibonacciHeap with a minimum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	public static <E> FibonacciHeap<E> createMinHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new FibonacciHeap<E>(initialElements);
	}
	
	/**
	 * Creates an empty FibonacciHeap with maximum-priority-first-out priority order.
	 *
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return an empty FibonacciHeap with a maximum-priority-first-out priority order
	 */
	public static <E> FibonacciHeap<E> createMaxHeap() {
		return new FibonacciHeap<E>((p1, p2) -> p1 > p2);
	}
	
	/**
	 * Creates a FibonacciHeap from a collection of (element, priority) pairs,
	 * with a maximum-priority-first-out priority order.
	 *
	 * @param initialElements The initial collection of (element, priority) pairs.
	 * @param <E> The type of elements contained in the FibonacciHeap.
	 *
	 * @return a FibonacciHeap with a maximum-priority-first-out priority order
	 *
	 * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
	 */
	public static <E> FibonacciHeap<E> createMaxHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
		return new FibonacciHeap<E>(initialElements, (p1, p2) -> p1 > p2);
	}
	
	@Override
	public final boolean change(E element, int priority) {
		if (!offer(element, priority)) {
			// No need to null check this because condition above guarantees
			// that this should be non-null.
			Node<E> node = index.get(element);
			if (compare.comesBefore(priority, node.e.value)) {
				internalPromote(node, priority);
				return true;
			} else if (compare.comesBefore(node.e.value, priority)) {
				internalDemote(node, priority);
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public final void clear() {
		size = 0;
		// clear the index
		index.clear();
		// set min to null which should cause garbage collection
		// of entire fibonacci heap (impossible to have references to Nodes
		// external from this class.
		min = null;
	}
	
	@Override
	public final boolean contains(Object o) {
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
			return index.containsKey(pair.element);
		}
		return index.containsKey(o);
	}
	
	@Override
	public final boolean demote(E element, int priority) {
		Node<E> node = index.get(element);
		if (node != null && compare.comesBefore(node.e.value, priority)) {
			internalDemote(node, priority);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if this FibonacciHeap contains the same (element, priority)
	 * pairs as another FibonacciHeap, including the specific structure
	 * the FibonacciHeap, as well as that the priority order is the same.
	 *
	 * @param other The other FibonacciHeap.
	 *
	 * @return true if and only if this and other contain the same (element, priority)
	 * pairs, with the same priority order.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof FibonacciHeap) {
			@SuppressWarnings("unchecked")
			FibonacciHeap<E> casted = (FibonacciHeap<E>)other;
			if (size != casted.size) return false;
			if (compare.comesBefore(0, 1) != casted.compare.comesBefore(0, 1)) return false;
			Iterator<PriorityQueueNode.Integer<E>> iter = iterator();
			Iterator<PriorityQueueNode.Integer<E>> otherIter = casted.iterator();
			while (iter.hasNext()) {
				if (!iter.next().equals(otherIter.next())) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Computes a hashCode for the BinaryHeapInteger.
	 *
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			h = 31 * h + java.lang.Integer.hashCode(e.value);
			h = 31 * h + e.element.hashCode();
		}
		return h;
	}
	
	@Override
	public final boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public final Iterator<PriorityQueueNode.Integer<E>> iterator() {
		return new FibonacciHeapIterator();
	}
	
	@Override
	public final boolean offer(E element, int priority) {
		if (index.containsKey(element)) {
			return false;
		} 
		return internalOffer(new PriorityQueueNode.Integer<E>(element, priority));
	}
	
	@Override
	public final boolean offer(PriorityQueueNode.Integer<E> pair) {
		if (index.containsKey(pair.element)) {
			return false;
		}
		return internalOffer(pair.copy());
	}
	
	@Override
	public final E peekElement() {
		return min != null ? min.e.element : null;
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> peek() {
		return min != null ? min.e : null;
	}
	
	@Override
	public final int peekPriority() {
		return min != null ? min.e.value : extreme;
	}
	
	@Override
	public final int peekPriority(E element) {
		Node<E> node = index.get(element);
		return node != null ? node.e.value : extreme;
	}
	
	@Override
	public final E pollElement() {
		PriorityQueueNode.Integer<E> min = poll();
		return min != null ? min.element : null;
	}
	
	@Override
	public final PriorityQueueNode.Integer<E> poll() {
		if (size == 1) {
			PriorityQueueNode.Integer<E> pair = min.e;
			index.remove(pair.element);
			min = null;
			size = 0;
			return pair;
		} else if (size > 1) {
			Node<E> z = min;
			if (z.child != null) {
				z.child.clearParentReferences();
				z.child.insertListInto(min);
			}
			min = min.right;
			z.left.right = min;
			min.left  = z.left;
			consolidate();
			index.remove(z.e.element);
			size--;
			return z.e;
		}
		return null;
	}
	
	@Override
	public final boolean promote(E element, int priority) {
		Node<E> node = index.get(element);
		if (node != null && compare.comesBefore(priority, node.e.value)) {
			internalPromote(node, priority);
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean remove(Object o) {
		Node<E> node = null;
		if (o instanceof PriorityQueueNode.Integer) {
			PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
			node = index.get(pair.element);
		} else {
			node = index.get(o);
		}
		if (node == null) {
			return false;
		}
		internalPromote(node, compare.comesBefore(min.e.value-1, min.e.value) ? min.e.value-1 : min.e.value+1);
		poll();
		return true;
	}
	
	@Override
	public final boolean retainAll(Collection<?> c) {
		HashSet<E> deleteThese = new HashSet<E>();
		for (PriorityQueueNode.Integer<E> e : this) {
			deleteThese.add(e.element);
		}
		for (Object o : c) {
			if (o instanceof PriorityQueueNode.Integer) {
				PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer)o;
				deleteThese.remove(pair.element);
			} else {
				deleteThese.remove(o);
			}
		}
		boolean changed = false;
		for (E e : deleteThese) {
			remove(e); 
			changed = true;
		}
		return changed;
	}
	
	@Override
	public final int size() {
		return size;
	}
	
	@Override
	public final Object[] toArray() {
		Object[] array = new Object[size];
		int i = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			array[i] = e;
			i++;
		}
		return array;
	}
	
	@Override
	public final <T> T[] toArray(T[] array) {
		@SuppressWarnings("unchecked")
		T[] result = array.length >= size ? array : (T[])Array.newInstance(array.getClass().getComponentType(), size);
		int i = 0;
		for (PriorityQueueNode.Integer<E> e : this) {
			@SuppressWarnings("unchecked")
			T nextElement = (T)e;
			result[i] = nextElement;
			i++;
		}
		if (result.length > size) {
			result[size] = null;
		}
		return result;
	}
	
	/*
	 * used internally: doesn't check if already contains element
	 */
	private boolean internalOffer(PriorityQueueNode.Integer<E> pair) {
		if (min == null) {
			min = new Node<E>(pair);
			index.put(pair.element, min);
			size = 1;
		} else {
			Node<E> added = new Node<E>(pair, min);
			index.put(pair.element, added);
			if (compare.comesBefore(pair.value, min.e.value)) {
				min = added;
			}
			size++;
		}
		return true;
	}
	
	private void internalPromote(Node<E> x, int priority) {
		// only called if priority decreased for a minheap (increased for a maxheap)
		// so no checks needed here.
		x.e.value = priority;
		Node<E> y = x.parent;
		if (y != null && compare.comesBefore(priority, y.e.value)) {
			cut(x, y);
			cascadingCut(y);			
		}
		if (compare.comesBefore(priority, min.e.value)) {
			min = x;
		}
	}
	
	private void internalDemote(Node<E> x, int priority) {
		// only called if priority increased for a minheap (decreased for a maxheap)
		// so no checks needed here.
		
		// 1. promote (opposite) to front
		internalPromote(x, compare.comesBefore(min.e.value-1, min.e.value) ? min.e.value-1 : min.e.value+1);
		// 2. poll() to remove
		poll();
		// 3. reinsert with new priority
		x.e.value = priority;
		internalOffer(x.e);
	}
	
	private void cascadingCut(Node<E> y) {
		Node<E> z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}
	
	private void cut(Node<E> x, Node<E> y) {
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
	
	private void consolidate() {
		int dn = (int)(Math.log(size) * INV_LOG_GOLDEN_RATIO);
		
		// first node of iteration
		Node<E> w = min;
		// disconnect from left to enable detecting end of list
		w.left.right = null;
		do{
			Node<E> x = w;
			// prepare for next iteration
			w = w.right;
			// disconnect x from root list
			x.left = x.right = null;
			
			int d = x.degree;
			while (rootsByDegrees[d] != null) {
				Node<E> y = rootsByDegrees[d];
				if (compare.comesBefore(y.e.value, x.e.value)) {
					Node<E> temp = x;
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
					if (compare.comesBefore(rootsByDegrees[i].e.value, min.e.value)) {
						min = rootsByDegrees[i];
					}
				}
				// need this since this array is shared by all calls to consolidate
				rootsByDegrees[i] = null;
			}
		}
	}
	
	private void fibHeapLink(Node<E> y, Node<E> x) {
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
	
	private Node<E>[] nodeArrayAllocate(int n) {
		@SuppressWarnings("unchecked")
		Node<E>[] array = new Node[n];
		return array;
	}
	
	@FunctionalInterface
	private static interface PriorityComparator {
		boolean comesBefore(int p1, int p2);
	}
	
	private class Node<E2> {
		private PriorityQueueNode.Integer<E2> e;
		private Node<E2> parent;
		private Node<E2> child;
		private Node<E2> left;
		private Node<E2> right;
		private int degree;
		private boolean mark;
		
		/*
		 * new root list (i.e., called to create new top-level list when empty
		 */
		public Node(PriorityQueueNode.Integer<E2> e) {
			this.e = e;
			singletonList();
		}
		
		/*
		 * adds newly constructed node to root list
		 */
		public Node(PriorityQueueNode.Integer<E2> e, Node<E2> list) {
			this.e = e;
			insertInto(list);
		}
		
		private Node(Node<E2> other) {
			e = other.e.copy();
			degree = other.degree;
			mark = other.mark;
		}
		
		private Node(Node<E2> other, Node<E2> toTheLeft) {
			this(other);
			left = toTheLeft;
		}
		
		private Node<E2> copy(HashMap<E2, Node<E2>> indexCopy) {
			return copyList(this, null, indexCopy);
		}
		
		private Node<E2> copyList(Node<E2> x, Node<E2> p, HashMap<E2, Node<E2>> indexCopy) {
			Node<E2> y = new Node<E2>(x);
			indexCopy.put(y.e.element, y);
			y.parent = p;
			if (x.child != null) {
				y.child = copyList(x.child, y, indexCopy);
			}
			Node<E2> rightOf = y;
			for (Node<E2> next = x.right; next != x; next = next.right, rightOf = rightOf.right) {
				rightOf.right = new Node<E2>(next, rightOf);
				indexCopy.put(rightOf.right.e.element, rightOf.right);
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

		private void insertInto(Node<E2> list) {
			right = list.right;
			left = list;
			list.right = list.right.left = this;
		}
		
		private void insertListInto(Node<E2> list) {
			list.right.left = left;
			left.right = list.right;
			list.right = this;
			left = list;
		}
		
		private void clearParentReferences() {
			for (Node<E2> next = this; next.parent != null; next = next.right) {
				next.parent = null;
			}
		}
	}
	
	private class FibonacciHeapIterator implements Iterator<PriorityQueueNode.Integer<E>> {
		
		private final Deque<Node<E>> stack;
		
		public FibonacciHeapIterator() {
			stack = new ArrayDeque<Node<E>>(size);
			if (size > 0) {
				stack.push(min);
				for (Node<E> next = min.right; next != min; next = next.right) {
					stack.push(next);
				}
			}				
		}
		
		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}
		
		@Override
		public PriorityQueueNode.Integer<E> next() {
			// normally, the next method of an Iterator is
			// required to throw NoSuchElementException is caled when empty.
			// The pop() method of the ArrayDeque does this though. So no need
			// for explicit check.
			Node<E> current = stack.pop();
			if (current.degree > 0) {
				stack.push(current.child);
				Node<E> next = current.child.right;
				for (int i = 1; i < current.degree; i++, next = next.right) {
					stack.push(next);
				}
			}
			return current.e;
		}
	}
}
