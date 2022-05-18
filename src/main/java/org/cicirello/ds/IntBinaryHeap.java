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

/**
 *
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class IntBinaryHeap implements IntPriorityQueue {
	
	private final int[] heap;
	private final int[] index;
	private final int[] value;
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
		Arrays.fill(index, -1);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public void change(int element, int priority) {
		if (!offer(element, priority)) {
			internalChange(element, priority);
		}
	}
	
	@Override
	public void clear() {
		if (size > 0) {
			Arrays.fill(index, 0, size, -1);
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
	public boolean contains(int element) {
		return index[element] >= 0;
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
	public int domain() {
		return index.length;
	}
	
	@Override
	public boolean isEmpty() {
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
		if (index[element] >= 0) {
			return false;
		}
		index[heap[size] = element] = size;
		value[element] = priority;
		percolateUp(size);
		size++;
		return true;
	}
	
	@Override
	public int peek() {
		return heap[0];
	}
	
	@Override
	public int peekPriority() {
		return value[heap[0]];
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IndexOutOfBoundsException if element is negative, or if element is greater than
	 *     or equal to the domain n.
	 */
	@Override
	public int peekPriority(int element) {
		return value[element];
	}
	
	@Override
	public int poll() {
		int min = heap[0];
		index[min] = -1;
		size--;
		if (size > 0) {
			index[heap[0] = heap[size]] = 0;
			percolateDown(0);
		} 
		return min;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/*
	 * package-private to enable overriding in 
	 * nested subclass to support max heaps.
	 */
	void internalChange(int element, int priority) {
		if (priority < value[element]) {
			value[element] = priority;
			percolateUp(index[element]);
		} else if (priority > value[element]) {
			value[element] = priority;
			percolateDown(index[element]);
		}
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
}
