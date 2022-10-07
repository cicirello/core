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
 * Utility class for filling integer arrays with consecutive integers.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class ArrayFiller {
	
	private ArrayFiller() {}
	
	/**
	 * Creates an array and fills it with the integers: 0, 1, ....
	 *
	 * @param n The length of the array.
	 * @return The newly created array.
	 */
	public static int[] create(int n) {
		int[] array = new int[n];
		fill(array);
		return array;
	}
	
	/**
	 * Creates an array and fills it with the integers: x+0, x+1, ....
	 *
	 * @param n The length of the array.
	 * @param offset The amount to offset each integer by, i.e., the
	 * value of the first int in the sequence.
	 *
	 * @return The newly created array.
	 */
	public static int[] create(int n, int offset) {
		int[] array = new int[n];
		fill(array, offset);
		return array;
	}
	
	/**
	 * Creates an array and fills it with the integers: n-1, n-1, ..., 1, 0,
	 * where n is the length of the array.
	 *
	 * @param n The length of the array.
	 * @return The newly created array.
	 */
	public static int[] createReverse(int n) {
		int[] array = new int[n];
		reverse(array);
		return array;
	}
	
	/**
	 * Creates an array and fills it with the integers: x+n-1, x+n-1, ..., x+1, x,
	 * where n is the length of the array.
	 *
	 * @param n The length of the array.
	 * @param offset The amount to offset each integer by, i.e., the
	 * value of the last int in the sequence.
	 *
	 * @return The newly created array.
	 */
	public static int[] createReverse(int n, int offset) {
		int[] array = new int[n];
		reverse(array, offset);
		return array;
	}
	
	/**
	 * Fills an existing array with integers: 0, 1, ....
	 *
	 * @param array The array to fill.
	 */
	public static void fill(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
	}
	
	/**
	 * Fills an existing array with integers: x+0, x+1, ....
	 *
	 * @param array The array to fill.
	 * @param offset The amount to offset each integer by, i.e., the
	 * value of the first int in the sequence.
	 */
	public static void fill(int[] array, int offset) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i + offset;
		}
	}
	
	/**
	 * Fills part of an existing array with integers: 0, 1, ..., (k-1).
	 *
	 * @param array The array to fill.
	 * @param k The size of the part of the array to fill.
	 */
	public static void fillPartial(int[] array, int k) {
		for (int i = 0; i < k; i++) {
			array[i] = i;
		}
	}
	
	/**
	 * Fills part of an existing array with integers: x+0, x+1, ....
	 *
	 * @param array The array to fill.
	 * @param k The size of the part of the array to fill.
	 * @param offset The amount to offset each integer by, i.e., the
	 * value of the first int in the sequence.
	 */
	public static void fillPartial(int[] array, int k, int offset) {
		for (int i = 0; i < k; i++) {
			array[i] = i + offset;
		}
	}
	
	/**
	 * Fills an existing array with integers: n-1, n-2, ..., 1, 0,
	 * where n is the length of the array
	 *
	 * @param array The array to fill.
	 */
	public static void reverse(int[] array) {
		int max = array.length - 1;
		for (int i = 0; i < array.length; i++) {
			array[i] = max - i;
		}
	}
	
	/**
	 * Fills an existing array with integers: x+n-1, x+n-2, ..., x+1, x,
	 * where n is the length of the array
	 *
	 * @param array The array to fill.
	 * @param offset The amount to offset each integer by, i.e., the
	 * value of the last int in the sequence.
	 */
	public static void reverse(int[] array, int offset) {
		int max = offset + array.length - 1;
		for (int i = 0; i < array.length; i++) {
			array[i] = max - i;
		}
	}
}
