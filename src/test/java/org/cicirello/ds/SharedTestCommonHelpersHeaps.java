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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.cicirello.util.Copyable;

/**
 * Common functionality for test cases for the various heap classes.
 */
public abstract class SharedTestCommonHelpersHeaps extends SharedTestHelpersHeaps {
	
	private final Supplier<PriorityQueue<String>> minFactory;
	private final Supplier<PriorityQueue<String>> maxFactory;
	private final Function<Collection<PriorityQueueNode.Integer<String>>, PriorityQueue<String>> fromListMinFactory;
	private final Function<Collection<PriorityQueueNode.Integer<String>>, PriorityQueue<String>> fromListMaxFactory;
	
	SharedTestCommonHelpersHeaps(Supplier<PriorityQueue<String>> minFactory, Supplier<PriorityQueue<String>> maxFactory, Function<Collection<PriorityQueueNode.Integer<String>>, PriorityQueue<String>> fromListMinFactory, Function<Collection<PriorityQueueNode.Integer<String>>, PriorityQueue<String>> fromListMaxFactory) {
		super(true);
		this.minFactory = minFactory;
		this.maxFactory = maxFactory;
		this.fromListMinFactory = fromListMinFactory;
		this.fromListMaxFactory = fromListMaxFactory;
	}
	
	final void containsAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		PriorityQueue<String> pq = minFactory.get();
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		for (int i = 0; i < elements.length; i++) {
			assertFalse(pq.containsAll(list));
			assertTrue(pq.add(elements[i], priorities[i]));
		}
		assertTrue(pq.containsAll(list));
	}
	
	final void containsAllElements() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		PriorityQueue<String> pq = minFactory.get();
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
		}
		for (int i = 0; i < elements.length; i++) {
			assertFalse(pq.containsAll(list));
			assertTrue(pq.add(elements[i], priorities[i]));
		}
		assertTrue(pq.containsAll(list));
	}
	
	final void retainAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		PriorityQueue<String> pq = minFactory.get();
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.offer(elements[i], priorities[i]));
		}
		assertEquals(elements.length, pq.size());
		String[] retain = {"E", "A", "F", "C"};
		ArrayList<Object> keepThese = new ArrayList<Object>();
		keepThese.add(elements[0]);
		keepThese.add(elements[1]);
		keepThese.add(elements[2]);
		keepThese.add(elements[3]);
		assertFalse(pq.retainAll(keepThese));
		assertEquals(elements.length, pq.size());
		
		keepThese.clear();
		keepThese.add(retain[0]);
		keepThese.add(retain[1]);
		keepThese.add(new PriorityQueueNode.Integer<String>(retain[2], 5));
		keepThese.add(new PriorityQueueNode.Integer<String>(retain[3], 15));
		assertTrue(pq.retainAll(keepThese));
		assertEquals(elements.length-2, pq.size());
		assertTrue(pq.contains(elements[0]));
		assertTrue(pq.contains(elements[2]));
		assertFalse(pq.contains(elements[1]));
		assertFalse(pq.contains(elements[3]));
		assertFalse(pq.retainAll(keepThese));
		
		keepThese.clear();
		keepThese.add(retain[1]);
		keepThese.add(retain[3]);
		assertFalse(pq.retainAll(keepThese));
		
		keepThese.clear();
		keepThese.add(retain[0]);
		keepThese.add(retain[2]);
		assertTrue(pq.retainAll(keepThese));
		assertEquals(0, pq.size());
	}
	
	final void removeAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		PriorityQueue<String> pq = minFactory.get();
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		assertTrue(pq.removeAll(list));
		assertEquals(0, pq.size());
		for (String e : elements) {
			assertFalse(pq.contains(e));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list.remove(list.size()-1);
		assertTrue(pq.removeAll(list));
		assertEquals(1, pq.size());
		for (int i = 0; i < elements.length-1; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
		assertTrue(pq.contains(elements[elements.length-1]));
		
		list.clear();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		for (int i = 0; i < elements.length; i++) {
			list2.add(elements[i]);
			list3.add(elements[i]);
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		pq.clear();
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		assertTrue(pq.removeAll(list2));
		assertEquals(0, pq.size());
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list2.remove(list.size()-1);
		assertTrue(pq.removeAll(list2));
		assertEquals(1, pq.size());
		assertFalse(pq.removeAll(list2));
		assertEquals(1, pq.size());
		assertTrue(pq.contains(elements[elements.length-1]));
		for (int i = 0; i < elements.length-1; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
		
		pq.clear();
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		list3.remove(0);
		assertTrue(pq.removeAll(list3));
		assertEquals(1, pq.size());
		assertFalse(pq.removeAll(list3));
		assertEquals(1, pq.size());
		assertTrue(pq.contains(elements[0]));
		for (int i = 1; i < elements.length; i++) {
			String e = elements[i];
			assertFalse(pq.contains(e));
		}
	}
	
	final void iterator() {
		int n = 4;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			PriorityQueue<String> pq = minFactory.get();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			int count = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				count++;
			}
			assertEquals(m, count);
			count = 0;
			final Iterator<PriorityQueueNode.Integer<String>> iter = pq.iterator();
			while (iter.hasNext()) {
				PriorityQueueNode.Integer<String> e = iter.next();
				count++;
			}
			assertEquals(m, count);
			NoSuchElementException thrown = assertThrows( 
				NoSuchElementException.class,
				() -> iter.next()
			);
		}
	}
	
	final void iteratorWithChildren() {
		int n = 8;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		for (int m = 0; m < n; m++) {
			PriorityQueue<String> pq = minFactory.get();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			String minElement = pq.pollElement();
			int count = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				count++;
			}
			assertEquals(m > 0 ? m-1 : 0, count);
			count = 0;
			final Iterator<PriorityQueueNode.Integer<String>> iter = pq.iterator();
			while (iter.hasNext()) {
				PriorityQueueNode.Integer<String> e = iter.next();
				count++;
			}
			assertEquals(m > 0 ? m-1 : 0, count);
			NoSuchElementException thrown = assertThrows( 
				NoSuchElementException.class,
				() -> iter.next()
			);
		}
	}
	
	final void toArray() {
		int n = 4;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		for (int m = 0; m <= n; m++) {
			PriorityQueue<String> pq = minFactory.get();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			Object[] array = pq.toArray();
			assertEquals(m, array.length);
			int j = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				assertEquals(e, (PriorityQueueNode.Integer)array[j]);
				j++;
			}
			assertEquals(m, j);
		}
	}
	
	final void toArrayExistingArray() {
		int n = 4;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		for (int m = 0; m <= n; m++) {
			PriorityQueue<String> pq = minFactory.get();
			for (int j = 0; j < m; j++) {
				pq.offer(elements[j], priorities[j]);
			}
			PriorityQueueNode.Integer[] a1 = new PriorityQueueNode.Integer[n];
			PriorityQueueNode.Integer[] a2 = pq.toArray(a1);
			assertTrue(a1 == a2);
			int j = 0;
			for (PriorityQueueNode.Integer<String> e : pq) {
				assertEquals(e, a2[j]);
				j++;
			}
			assertEquals(m, j);
			if (m<n) {
				assertNull(a2[j]);
			}
		}
		PriorityQueue<String> pq = minFactory.get();
		for (int j = 0; j < n; j++) {
			pq.offer(elements[j], priorities[j]);
		}
		PriorityQueueNode.Integer[] a1 = new PriorityQueueNode.Integer[n-1];
		PriorityQueueNode.Integer[] a2 = pq.toArray(a1);
		assertTrue(a1 != a2);
		assertEquals(n, a2.length);
		int j = 0;
		for (PriorityQueueNode.Integer<String> e : pq) {
			assertEquals(e, a2[j]);
			j++;
		}
		assertEquals(n, j);
	}
	
	final void clear() {
		int n = 11;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list.add(next);
		}
		PriorityQueue<String> pq = fromListMinFactory.apply(list);
		assertEquals(n, pq.size());
		pq.clear();
		assertEquals(0, pq.size());
		for (int i = 0; i < n; i++) {
			assertFalse(pq.contains(pairs[i].element));
		}
	}
	
	final void copy() {
		int n = 24;
		String[] elements = createStringsArbitrary(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list3 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list4 = new ArrayList<PriorityQueueNode.Integer<String>>();
		PriorityQueue<String> pq5 = minFactory.get();
		PriorityQueue<String> pq6 = maxFactory.get();
		int iter = 0;
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list1.add(next);
			list2.add(next);
			iter++;
			pq5.offer(next);
			pq6.offer(next);
			if (iter % 8 == 0) {
				pq5.poll();
				pq6.poll();
			}
		}
		for (int i = 0; i < n; i++) {
			list3.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
			list4.add(new PriorityQueueNode.Integer<String>(elements[i], 42));
		}
		PriorityQueue<String> pq1 = fromListMinFactory.apply(list1);
		PriorityQueue<String> pq2 = fromListMaxFactory.apply(list2);
		PriorityQueue<String> pq3 = fromListMinFactory.apply(list3);
		PriorityQueue<String> pq4 = fromListMaxFactory.apply(list4);
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy1 = (PriorityQueue<String>)((Copyable)pq1).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy2 = (PriorityQueue<String>)((Copyable)pq2).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy3 = (PriorityQueue<String>)((Copyable)pq3).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy4 = (PriorityQueue<String>)((Copyable)pq4).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy5 = (PriorityQueue<String>)((Copyable)pq5).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> copy6 = (PriorityQueue<String>)((Copyable)pq6).copy();
		assertEquals(pq1, copy1);
		assertEquals(pq2, copy2);
		assertEquals(pq3, copy3);
		assertEquals(pq4, copy4);
		assertEquals(pq5, copy5);
		assertEquals(pq6, copy6);
		assertTrue(pq1 != copy1);
		assertTrue(pq2 != copy2);
		assertTrue(pq3 != copy3);
		assertTrue(pq4 != copy4);
		assertTrue(pq5 != copy5);
		assertTrue(pq6 != copy6);
		assertNotEquals(pq2, copy1);
		assertNotEquals(pq3, copy1);
		assertNotEquals(pq4, copy1);
		assertNotEquals(pq1, copy2);
		assertNotEquals(pq3, copy2);
		assertNotEquals(pq4, copy2);
		assertNotEquals(pq1, copy3);
		assertNotEquals(pq2, copy3);
		assertNotEquals(pq4, copy3);
		assertNotEquals(pq1, copy4);
		assertNotEquals(pq2, copy4);
		assertNotEquals(pq3, copy4);
		assertNotEquals(pq6, copy5);
		assertNotEquals(pq5, copy6);
	}
	
	final void copyEmptyHeap() {
		PriorityQueue<String> pqEmptyMin = minFactory.get();
		PriorityQueue<String> pqEmptyMax = maxFactory.get();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> pqEmptyMinCopy = (PriorityQueue<String>)((Copyable)pqEmptyMin).copy();
		@SuppressWarnings("unchecked")
		PriorityQueue<String> pqEmptyMaxCopy = (PriorityQueue<String>)((Copyable)pqEmptyMax).copy();
		assertEquals(pqEmptyMin, pqEmptyMinCopy);
		assertEquals(pqEmptyMax, pqEmptyMaxCopy);
		assertNotEquals(pqEmptyMin, pqEmptyMaxCopy);
		assertNotEquals(pqEmptyMax, pqEmptyMinCopy);
		assertTrue(pqEmptyMin != pqEmptyMinCopy);
		assertTrue(pqEmptyMax != pqEmptyMaxCopy);
		assertEquals(0, pqEmptyMinCopy.size());
		assertEquals(0, pqEmptyMaxCopy.size());
		assertEquals(0, pqEmptyMin.size());
		assertEquals(0, pqEmptyMax.size());
		assertTrue(pqEmptyMinCopy.isEmpty());
		assertTrue(pqEmptyMaxCopy.isEmpty());
		assertTrue(pqEmptyMin.isEmpty());
		assertTrue(pqEmptyMax.isEmpty());
	}
	
	final void addAll() {
		String[] elements = {"A", "B", "C", "D"};
		int[] priorities = { 8, 6, 4, 2 };
		PriorityQueue<String> pq = minFactory.get();
		ArrayList<PriorityQueueNode.Integer<String>> list = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements.length; i++) {
			list.add(new PriorityQueueNode.Integer<String>(elements[i], priorities[i]));
		}
		assertTrue(pq.addAll(list));
		assertEquals(elements.length, pq.size());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		
		String[] elements2 = {"E", "F", "G", "H", "I"};
		int[] priorities2 = { 7, 3, 1, 5, 9 };
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (int i = 0; i < elements2.length; i++) {
			list2.add(new PriorityQueueNode.Integer<String>(elements2[i], priorities2[i]));
		}
		assertTrue(pq.addAll(list2));
		assertEquals(elements.length + elements2.length, pq.size());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		for (int i = 0; i < elements2.length; i++) {
			assertTrue(pq.contains(elements2[i]));
			assertEquals(priorities2[i], pq.peekPriority(elements2[i]));
		}
		
		assertFalse(pq.addAll(new ArrayList<PriorityQueueNode.Integer<String>>()));
		assertEquals(elements.length + elements2.length, pq.size());
		for (int i = 0; i < elements.length; i++) {
			assertTrue(pq.contains(elements[i]));
			assertEquals(priorities[i], pq.peekPriority(elements[i]));
		}
		for (int i = 0; i < elements2.length; i++) {
			assertTrue(pq.contains(elements2[i]));
			assertEquals(priorities2[i], pq.peekPriority(elements2[i]));
		}
	}
	
	final void equalsAndHashCode(Function<Collection<PriorityQueueNode.Integer<String>>, PriorityQueue<String>> otherFactory) {
		int n = 11;
		String[] elements = createStrings(n);
		int[] priorities = createPriorities(elements);
		PriorityQueueNode.Integer<String>[] pairs = createPairs(elements, priorities);
		ArrayList<PriorityQueueNode.Integer<String>> list1 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list2 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list3 = new ArrayList<PriorityQueueNode.Integer<String>>();
		ArrayList<PriorityQueueNode.Integer<String>> list4 = new ArrayList<PriorityQueueNode.Integer<String>>();
		for (PriorityQueueNode.Integer<String> next : pairs) {
			list1.add(next);
			list2.add(next);
			list3.add(next);
			list4.add(next);
		}
		PriorityQueue<String> pq1 = fromListMinFactory.apply(list1);
		PriorityQueue<String> pq2 = fromListMinFactory.apply(list2);
		PriorityQueue<String> pq3 = fromListMaxFactory.apply(list3);
		PriorityQueue<String> pq4 = fromListMaxFactory.apply(list4);
		PriorityQueue<String> pqSimple = otherFactory.apply(list1);
		assertNotEquals(pq1, pqSimple);
		assertEquals(pq1, pq2);
		assertEquals(pq1.hashCode(), pq2.hashCode());
		assertEquals(pq3, pq4);
		assertEquals(pq3.hashCode(), pq4.hashCode());
		assertNotEquals(pq1, pq3);
		assertNotEquals(pq1.hashCode(), pq3.hashCode());
		pq2.offer(""+((char)0), 0);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		pq1.offer(""+((char)0), 1);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		pq1.clear();
		pq2.clear();
		pq3.clear();
		pq4.clear();
		for (int i = 0; i < n; i++) {
			pq1.offer(""+((char)('A'+i)), 42);
			pq3.offer(""+((char)('A'+i)), 42);
			pq2.offer(""+((char)('A'+(n-1)-i)), 42);
			pq4.offer(""+((char)('A'+(n-1)-i)), 42);
		}
		assertNotEquals(pq1, pq3);
		assertNotEquals(pq3, pq1);
		assertNotEquals(pq3, pq4);
		assertNotEquals(pq1, pq2);
		assertNotEquals(pq1.hashCode(), pq2.hashCode());
		assertNotEquals(pq3.hashCode(), pq4.hashCode());
		assertNotEquals(pq1, null);
		assertNotEquals(pq3, null);
		assertNotEquals(pq1, "hello");
		assertNotEquals(pq3, "hello");
	}
}
