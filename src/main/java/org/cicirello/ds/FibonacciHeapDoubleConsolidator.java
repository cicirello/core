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
 *
 */
final class FibonacciHeapDoubleConsolidator<E2> {
	
	// This array is what is referred to in CLRS description
	// of algorithm as A in the method consolidate. As an optimization
	// we construct the array once, and reuse it on all calls to consolidate.
	private final FibonacciHeapDoubleNode<E2>[] rootsByDegrees;
	
	private final static double INV_LOG_GOLDEN_RATIO = 2.0780869212350273;
	
	private final SimpleFibonacciHeapDouble.PriorityComparator compare;
	
	FibonacciHeapDoubleConsolidator(SimpleFibonacciHeapDouble.PriorityComparator compare) {
		this.compare = compare;
		// length of array used by consolidate is initialized to 45 as follows:
		// 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
		// 2) Thus, the highest that D(n) can be for a call to consolidate is:
		//    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
		// 3) Array must be of length 1+D(n), so longest array must be is 45.
		// 4) consolidate computes the actual D(n) for a specific call, and uses only
		//    part of this array.
		rootsByDegrees = nodeArrayAllocate(45);
	}
	
	private FibonacciHeapDoubleNode<E2>[] nodeArrayAllocate(int n) {
		@SuppressWarnings("unchecked")
		FibonacciHeapDoubleNode<E2>[] array = new FibonacciHeapDoubleNode[n];
		return array;
	}
	
	final FibonacciHeapDoubleNode<E2> consolidate(FibonacciHeapDoubleNode<E2> min, int size) {
		int dn = (int)(Math.log(size) * INV_LOG_GOLDEN_RATIO);
		
		// first node of iteration
		FibonacciHeapDoubleNode<E2> w = min;
		// disconnect from left to enable detecting end of list
		w.left.right = null;
		do{
			FibonacciHeapDoubleNode<E2> x = w;
			// prepare for next iteration
			w = w.right;
			// disconnect x from root list
			x.left = x.right = null;
			
			int d = x.degree;
			while (rootsByDegrees[d] != null) {
				FibonacciHeapDoubleNode<E2> y = rootsByDegrees[d];
				if (compare.comesBefore(y.e.value, x.e.value)) {
					FibonacciHeapDoubleNode<E2> temp = x;
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
		return min;
	}
	
	private void fibHeapLink(FibonacciHeapDoubleNode<E2> y, FibonacciHeapDoubleNode<E2> x) {
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
}
