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
 
package org.cicirello.util;

/**
 * This class is an implementation of a partially-filled array of 
 * primitive int values.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class IntegerList implements Copyable<IntegerList> {
	
	/**
	 * The default initial capacity of the list. The capacity will grow as needed.
	 */
	public final static int DEFAULT_INITIAL_CAPACITY = 16;
	
	private int[] list;
	private int size;
	
	/**
	 * Initializes an empty IntegerList.
	 */
	public IntegerList() {
		list = new int[DEFAULT_INITIAL_CAPACITY];
	}
	
	/**
	 * Initializes an empty IntegerList, with a specified initial capacity. This may be useful
	 * if you anticipate adding a large number of elements as it can help minimize the number
	 * of array reallocations.
	 *
	 * @param initialCapacity The initial capacity for the buffer array holding the contents.
	 *
	 * @throws IllegalArgumentException if initialCapacity is less than 1.
	 */
	public IntegerList(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("The initial capacity must be positive.");
		}
		list = new int[initialCapacity];
	}
	
	/**
	 * Initializes an IntegerList from an array,
	 *
	 * @param initialContents The initial contents of the list, which must contain at least one element.
	 *    This constructor clones this array so it is free of unintended side-effects.
	 *
	 * @throws IllegalArgumentException if initialContents.length is less than one.
	 */
	public IntegerList(int[] initialContents) {
		if (initialContents.length < 1) {
			throw new IllegalArgumentException("Initialization from array requires at least one element in array.");
		}
		list = initialContents.clone();
		size = list.length;
	}
	
	private IntegerList(IntegerList other) {
		list = other.list.clone();
		size = other.size;
	}
	
	/**
	 * Adds a new element to the end of the list, growing the internal array if necessary.
	 *
	 * @param element The value to add.
	 */
	public void add(int element) {
		if (size >= list.length) {
			reallocate();
		}
		list[size] = element;
		size++;
	}
	
	/**
	 * Inserts a new element into the list, growing the internal array if necessary.
	 *
	 * @param index The index for the insertion.
	 * @param element The value to add.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of bounds, such that index is
	 * negative or index is greater than size().
	 */
	public void add(int index, int element) {
		if (index > size) {
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
		if (size >= list.length) {
			reallocate();
		}
		if (index != size) {
			System.arraycopy(list, index, list, index+1, size - index);
		}
		list[index] = element;
		size++;
	}
	
	/**
	 * Logically clears the list.
	 */
	public void clear() {
		size = 0;
	}
	
	/**
	 * Checks if the list contains a value.
	 *
	 * @param element The element to search for.
	 * @return true if and only if the list contains at least one copy of element.
	 */
	public boolean contains(int element) {
		for (int i = 0; i < size; i++) {
			if (list[i] == element) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public IntegerList copy() {
		return new IntegerList(this);
	}
	
	/**
	 * Gets the element at a specified index.
	 *
	 * @param index The index of the desired element.
	 * @return The element at the specified index.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of bounds, such that index is
	 * negative or index is greater than or equal to size().
	 */
	public int get(int index) {
		if (index < size) {
			return list[index];
		}
		throw new IndexOutOfBoundsException("index is out of bounds");
	}
	
	/**
	 * Gets the index of the first occurrence of an element from the left.
	 *
	 * @param element The element searched for.
	 * @return The index of the first occurrence of the element in the list from the
	 * left, or -1 if the element doesn't exist.
	 */
	public int indexOf(int element) {
		for (int i = 0; i < size; i++) {
			if (list[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Checks if the list is empty.
	 *
	 * @return true if and only if size() equals 0.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Gets the index of the last occurrence of an element.
	 *
	 * @param element The element searched for.
	 * @return The index of the last occurrence of the element in the list,
	 * or -1 if the element doesn't exist.
	 */
	public int lastIndexOf(int element) {
		for (int i = size - 1; i >= 0; i--) {
			if (list[i] == element) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes the element at a specified index.
	 *
	 * @param index The index of the element to remove.
	 * @return The element that was removed.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of bounds, such that index is
	 * negative or index is greater than or equal to size().
	 */
	public int remove(int index) {
		if (index < size) {
			int element = list[index];
			System.arraycopy(list, index+1, list, index, size - index - 1);
			size--;
			return element;
		}
		throw new IndexOutOfBoundsException("index is out of bounds");
	}
	
	/**
	 * Sets an element at a specified index, replacing any value that is currently there.
	 *
	 * @param index The index.
	 * @param element The new element to set at that index.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of bounds, such that index is
	 * negative or index is greater than or equal to size().
	 */
	public void set(int index, int element) {
		if (index < size) {
			list[index] = element;
		} else {
			throw new IndexOutOfBoundsException("index is out of bounds");
		}
	}
	
	/**
	 * Gets the size of the list.
	 * @return The size of the list, which is the number of elements currently within it.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns an array containing all of the element from the list. This method
	 * constructs a new array with length equal to the current size() of the list.
	 * This method does not expose the encapsulated array.
	 *
	 * @return An array containing all of the elements currently in the list.
	 */
	public int[] toArray() {
		int[] result = new int[size];
		System.arraycopy(list, 0, result, 0, size);
		return result;
	}
	
	/**
	 * Checks if this list is equal to another list, such that
	 * they are the same size, and contain all of the same elements
	 * in the same order.
	 *
	 * @param other The other list
	 * @return true If the lists are the same size and contain the same
	 * elements in the same order.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof IntegerList) {
			IntegerList o = (IntegerList)other;
			if (size != o.size) return false;
			for (int i = 0; i < size; i++) {
				if (list[i] != o.list[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Computes a hashCode for the list.
	 * @return a hashCode
	 */
	@Override
	public int hashCode() {
		if (size > 0) {
			int h = list[0];
			for (int i = 1; i < size; i++) {
				h = (31 * h) + list[i];
			}
		}
		return 0;
	}
	
	/**
	 * Generate a String representation of the list.
	 * @return A String representation of the list.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");
		if (size > 0) {
			s.append(list[0]);
			for (int i = 1; i < size; i++) {
				s.append(", ");
				s.append(list[i]);
			}
		}
		s.append("]");
		return s.toString();
	}
	
	private void reallocate() {
		int[] temp = new int[list.length << 1];
		System.arraycopy(list, 0, temp, 0, size);
		list = temp;
	}
}
