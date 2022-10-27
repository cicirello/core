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
 * Test cases for SimpleBinaryHeap for min heap cases.
 */
public class SimpleBinaryMinHeapTests extends SharedTestHelpersMinHeaps {
	
	@Test
	public void testElementPollThenAddMinHeap() {
		elementPollThenAddMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPollThenAddMinHeap() {
		pollThenAddMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaElementMinHeap() {
		removeViaElementMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveViaPairMinHeap() {
		removeViaPairMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftMinHeap() {
		removeOneLeftMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveOneLeftViaPairMinHeap() {
		removeOneLeftViaPairMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityMinHeap() {
		removeSamePriorityMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemoveSamePriorityViaPairMinHeap() {
		removeSamePriorityViaPairMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationMinHeap() {
		removePercolationMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testRemovePercolationViaPairMinHeap() {
		removePercolationViaPairMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToFrontMinHeap() {
		changeToFrontMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToBackMinHeap() {
		changeToBackMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testChangeToInteriorMinHeap() {
		changeToInteriorMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testChangeEqualMinHeap() {
		changeEqualMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testChangeNewElementMinHeap() {
		changeNewElementMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToFrontMinHeap() {
		promoteDemoteToFrontMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToBackMinHeap() {
		promoteDemoteToBackMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteToInteriorMinHeap() {
		promoteDemoteToInteriorMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteEqualMinHeap() {
		promoteDemoteEqualMinHeap(SimpleBinaryHeap::createMinHeap);
	}
	
	@Test
	public void testPromoteDemoteNewElementMinHeap() {
		promoteDemoteNewElementMinHeap(SimpleBinaryHeap::createMinHeap);
	}
}
