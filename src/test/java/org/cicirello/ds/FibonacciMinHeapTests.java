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

/**
 * Test cases for FibonacciHeap for min heap cases.
 */
public class FibonacciMinHeapTests extends SharedTestHelpersMinHeaps {
	
	public FibonacciMinHeapTests() {
		super(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testDefaultMinHeap() {
		defaultMinHeap();
	}
	
	@Test
	public void testDefaultReverseMinHeap() {
		defaultReverseMinHeap();
	}
	
	@Test
	public void testDefaultArbitraryMinHeap() {
		defaultArbitraryMinHeap();
	}
	
	@Test
	public void testElementPollThenAddMinHeap() {
		elementPollThenAddMinHeap();
	}
	
	@Test
	public void testPollThenAddMinHeap() {
		pollThenAddMinHeap();
	}
	
	@Test
	public void testRemoveViaElementMinHeap() {
		removeViaElementMinHeap();
	}
	
	@Test
	public void testRemoveViaPairMinHeap() {
		removeViaPairMinHeap();
	}
	
	@Test
	public void testRemoveOneLeftMinHeap() {
		removeOneLeftMinHeap();
	}
	
	@Test
	public void testRemoveOneLeftViaPairMinHeap() {
		removeOneLeftViaPairMinHeap();
	}
	
	@Test
	public void testRemoveSamePriorityMinHeap() {
		removeSamePriorityMinHeap();
	}
	
	@Test
	public void testRemoveSamePriorityViaPairMinHeap() {
		removeSamePriorityViaPairMinHeap();
	}
	
	@Test
	public void testRemovePercolationMinHeap() {
		removePercolationMinHeap();
	}
	
	@Test
	public void testRemovePercolationViaPairMinHeap() {
		removePercolationViaPairMinHeap();
	}
	
	@Test
	public void testChangeToFrontMinHeap() {
		changeToFrontMinHeap();
	}
	
	@Test
	public void testChangeToBackMinHeap() {
		changeToBackMinHeap();
	}
	
	@Test
	public void testChangeToInteriorMinHeap() {
		changeToInteriorMinHeap();
	}
	
	@Test
	public void testChangeEqualMinHeap() {
		changeEqualMinHeap();
	}
	
	@Test
	public void testChangeNewElementMinHeap() {
		changeNewElementMinHeap();
	}
	
	@Test
	public void testChangeNotLowerParentMinHeap() {
		changeNotLowerParentMinHeap();
	}
	
	@Test
	public void testChangeCascadingCutMinHeap() {
		changeCascadingCutMinHeap();
	}
	
	@Test
	public void testChangeMultiLevelMinHeap() {
		changeMultiLevelMinHeap();
	}
	
	@Test
	public void testPromoteDemoteToFrontMinHeap() {
		promoteDemoteToFrontMinHeap();
	}
	
	@Test
	public void testPromoteDemoteToBackMinHeap() {
		promoteDemoteToBackMinHeap();
	}
	
	@Test
	public void testPromoteDemoteToInteriorMinHeap() {
		promoteDemoteToInteriorMinHeap();
	}
	
	@Test
	public void testPromoteDemoteEqualMinHeap() {
		promoteDemoteEqualMinHeap();
	}
	
	@Test
	public void testPromoteDemoteNewElementMinHeap() {
		promoteDemoteNewElementMinHeap();
	}
}
