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
 * Test cases for SimpleFibonacciHeap for min heap cases.
 */
public class SimpleFibonacciMinHeapTests extends SharedTestHelpersMinHeaps {
	
	@Test
	public void testElementPollThenAddMinHeap() {
		elementPollThenAddMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPollThenAddMinHeap() {
		pollThenAddMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaElementMinHeap() {
		removeViaElementMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaPairMinHeap() {
		removeViaPairMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftMinHeap() {
		removeOneLeftMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftViaPairMinHeap() {
		removeOneLeftViaPairMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityMinHeap() {
		removeSamePriorityMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityViaPairMinHeap() {
		removeSamePriorityViaPairMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationMinHeap() {
		removePercolationMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationViaPairMinHeap() {
		removePercolationViaPairMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToFrontMinHeap() {
		changeToFrontMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToBackMinHeap() {
		changeToBackMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToInteriorMinHeap() {
		changeToInteriorMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeEqualMinHeap() {
		changeEqualMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testChangeNewElementMinHeap() {
		changeNewElementMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToFrontMinHeap() {
		promoteDemoteToFrontMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToBackMinHeap() {
		promoteDemoteToBackMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToInteriorMinHeap() {
		promoteDemoteToInteriorMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteEqualMinHeap() {
		promoteDemoteEqualMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteNewElementMinHeap() {
		promoteDemoteNewElementMinHeap(SimpleFibonacciHeap::createMinHeap);
	}
}
