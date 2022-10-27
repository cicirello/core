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
	
	@Test
	public void testElementPollThenAddMinHeap() {
		elementPollThenAddMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPollThenAddMinHeap() {
		pollThenAddMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaElementMinHeap() {
		removeViaElementMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaPairMinHeap() {
		removeViaPairMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftMinHeap() {
		removeOneLeftMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftViaPairMinHeap() {
		removeOneLeftViaPairMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityMinHeap() {
		removeSamePriorityMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityViaPairMinHeap() {
		removeSamePriorityViaPairMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationMinHeap() {
		removePercolationMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationViaPairMinHeap() {
		removePercolationViaPairMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToFrontMinHeap() {
		changeToFrontMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToBackMinHeap() {
		changeToBackMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToInteriorMinHeap() {
		changeToInteriorMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeEqualMinHeap() {
		changeEqualMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeNewElementMinHeap() {
		changeNewElementMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToFrontMinHeap() {
		promoteDemoteToFrontMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToBackMinHeap() {
		promoteDemoteToBackMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToInteriorMinHeap() {
		promoteDemoteToInteriorMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteEqualMinHeap() {
		promoteDemoteEqualMinHeap(FibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteNewElementMinHeap() {
		promoteDemoteNewElementMinHeap(FibonacciHeap::createMinHeap);
	}
}
