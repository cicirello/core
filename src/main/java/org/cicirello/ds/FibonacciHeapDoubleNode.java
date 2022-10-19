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

import java.util.Iterator;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Internal package access class for representing nodes in a fibonacci heap with
 * priory of type double.
 */
final class FibonacciHeapDoubleNode<E> {
	PriorityQueueNode.Double<E> e;
	FibonacciHeapDoubleNode<E> parent;
	FibonacciHeapDoubleNode<E> child;
	FibonacciHeapDoubleNode<E> left;
	FibonacciHeapDoubleNode<E> right;
	int degree;
	boolean mark;
	
	/*
	 * new root list (i.e., called to create new top-level list when empty
	 */
	public FibonacciHeapDoubleNode(PriorityQueueNode.Double<E> e) {
		this.e = e;
		singletonList();
	}
	
	/*
	 * adds newly constructed node to root list
	 */
	public FibonacciHeapDoubleNode(PriorityQueueNode.Double<E> e, FibonacciHeapDoubleNode<E> list) {
		this.e = e;
		insertInto(list);
	}
	
	private FibonacciHeapDoubleNode(FibonacciHeapDoubleNode<E> other) {
		e = other.e.copy();
		degree = other.degree;
		mark = other.mark;
	}
	
	private FibonacciHeapDoubleNode(FibonacciHeapDoubleNode<E> other, FibonacciHeapDoubleNode<E> toTheLeft) {
		this(other);
		left = toTheLeft;
	}
	
	final FibonacciHeapDoubleNode<E> copy() {
		return copyList(this, null);
	}
	
	private FibonacciHeapDoubleNode<E> copyList(FibonacciHeapDoubleNode<E> x, FibonacciHeapDoubleNode<E> p) {
		FibonacciHeapDoubleNode<E> y = new FibonacciHeapDoubleNode<E>(x);
		y.parent = p;
		if (x.child != null) {
			y.child = copyList(x.child, y);
		}
		FibonacciHeapDoubleNode<E> rightOf = y;
		for (FibonacciHeapDoubleNode<E> next = x.right; next != x; next = next.right, rightOf = rightOf.right) {
			rightOf.right = new FibonacciHeapDoubleNode<E>(next, rightOf);
			rightOf.right.parent = p;
			if (next.child != null) {
				rightOf.right.child = copyList(next.child, rightOf.right);
			}
		}
		rightOf.right = y;
		y.left = rightOf;
		return y;
	}
	
	final void singletonList() {
		left = right = this;
	}

	final void insertInto(FibonacciHeapDoubleNode<E> list) {
		right = list.right;
		left = list;
		list.right = list.right.left = this;
	}
	
	final void insertListInto(FibonacciHeapDoubleNode<E> list) {
		list.right.left = left;
		left.right = list.right;
		list.right = this;
		left = list;
	}
	
	final void clearParentReferences() {
		for (FibonacciHeapDoubleNode<E> next = this; next.parent != null; next = next.right) {
			next.parent = null;
		}
	}
	
	final void cascadingCut(FibonacciHeapDoubleNode<E> root) {
		FibonacciHeapDoubleNode<E> z = parent;
		if (z != null) {
			if (!mark) {
				mark = true;
			} else {
				cut(z, root);
				z.cascadingCut(root);
			}
		}
	}
	
	final void cut(FibonacciHeapDoubleNode<E> y, FibonacciHeapDoubleNode<E> root) {
		// 1. remove this from child list of y, decrementing degree of y
		if (y.degree > 1) {
			// ensure y's child reference isn't this
			y.child = right;
			// link this's left and right neighbors to remove this
			left.right = right;
			right.left = left;
			y.degree--;
		} else {
			y.child = null;
			y.degree = 0;
		}
		// 2. add this to the root list
		insertInto(root);
		parent = null;
		mark = false;
	}
	
	static class FibonacciHeapDoubleIterator<E2> implements Iterator<PriorityQueueNode.Double<E2>> {
		
		private final NodeIterator<E2> iter;
		
		public FibonacciHeapDoubleIterator(FibonacciHeapDoubleNode<E2> root, int size) {
			iter = new NodeIterator<E2>(root, size);
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}
		
		@Override
		public PriorityQueueNode.Double<E2> next() {
			return iter.next().e;
		}
	}
	
	static class NodeIterator<E2> implements Iterator<FibonacciHeapDoubleNode<E2>> {
		
		private final Deque<FibonacciHeapDoubleNode<E2>> stack;
		
		public NodeIterator(FibonacciHeapDoubleNode<E2> root, int size) {
			stack = new ArrayDeque<FibonacciHeapDoubleNode<E2>>(size);
			if (size > 0) {
				stack.push(root);
				for (FibonacciHeapDoubleNode<E2> next = root.right; next != root; next = next.right) {
					stack.push(next);
				}
			}				
		}
		
		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}
		
		@Override
		public FibonacciHeapDoubleNode<E2> next() {
			// normally, the next method of an Iterator is
			// required to throw NoSuchElementException is called when empty.
			// The pop() method of the ArrayDeque does this though. So no need
			// for explicit check.
			FibonacciHeapDoubleNode<E2> current = stack.pop();
			if (current.degree > 0) {
				stack.push(current.child);
				FibonacciHeapDoubleNode<E2> next = current.child.right;
				for (int i = 1; i < current.degree; i++, next = next.right) {
					stack.push(next);
				}
			}
			return current;
		}
	}
}
