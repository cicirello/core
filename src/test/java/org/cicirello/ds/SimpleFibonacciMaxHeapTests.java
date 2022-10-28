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
 * Test cases for SimpleFibonacciHeap for max heap cases.
 */
public class SimpleFibonacciMaxHeapTests extends SharedTestHelpersMaxHeaps {
	
	public SimpleFibonacciMaxHeapTests() {
		super(SimpleFibonacciHeap::createMaxHeap, SimpleFibonacciHeap::createMaxHeap);
	}
	
	@Test
	public void testDefaultMaxHeap() {
		defaultDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testDefaultReverseMaxHeap() {
		defaultReverseDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testDefaultArbitraryMaxHeap() {
		defaultArbitraryDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testFromListMaxHeap() {
		listDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testFromListReverseMaxHeap() {
		listReverseDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testFromListArbitraryMaxHeap() {
		listArbitraryDuplicatesAllowedMaxHeap();
	}
	
	@Test
	public void testFromListEmptyMaxHeap() {
		listEmptyMaxHeap();
	}
	
	@Test
	public void testRemoveViaElementMaxHeap() {
		removeViaElementMaxHeap();
	}
	
	@Test
	public void testRemoveViaPairMaxHeap() {
		removeViaPairMaxHeap();
	}
	
	@Test
	public void testRemoveOneLeftMaxHeap() {
		removeOneLeftMaxHeap();
	}
	
	@Test
	public void testRemoveOneLeftViaPairMaxHeap() {
		removeOneLeftViaPairMaxHeap();
	}
	
	@Test
	public void testRemoveSamePriorityMaxHeap() {
		removeSamePriorityMaxHeap();
	}
	
	@Test
	public void testRemoveSamePriorityViaPairMaxHeap() {
		removeSamePriorityViaPairMaxHeap();
	}
	
	@Test
	public void testRemovePercolationMaxHeap() {
		removePercolationMaxHeap();
	}
	
	@Test
	public void testRemovePercolationViaPairMaxHeap() {
		removePercolationViaPairMaxHeap();
	}
	
	@Test
	public void testChangeToFrontMaxHeap() {
		changeToFrontMaxHeap();
	}
	
	@Test
	public void testChangeToBackMaxHeap() {
		changeToBackMaxHeap();
	}
	
	@Test
	public void testChangeToInteriorMaxHeap() {
		changeToInteriorMaxHeap();
	}
	
	@Test
	public void testChangeEqualMaxHeap() {
		changeEqualMaxHeap();
	}
	
	@Test
	public void testChangeNewElementMaxHeap() {
		changeNewElementMaxHeap();
	}
	
	@Test
	public void testChangeNotHigherParentMaxHeap() {
		changeNotHigherParentMaxHeap();
	}
	
	@Test
	public void testChangeCascadingCutMaxHeap() {
		changeCascadingCutMaxHeap();
	}
	
	@Test
	public void testChangeMultiLevelMaxHeap() {
		changeMultiLevelMaxHeap();
	}
	
	@Test
	public void testPromoteDemoteToFrontMaxHeap() {
		promoteDemoteToFrontMaxHeap();
	}
	
	@Test
	public void testPromoteDemoteToBackMaxHeap() {
		promoteDemoteToBackMaxHeap();
	}
	
	@Test
	public void testPromoteDemoteToInteriorMaxHeap() {
		promoteDemoteToInteriorMaxHeap();
	}
	
	@Test
	public void testPromoteDemoteEqualMaxHeap() {
		promoteDemoteEqualMaxHeap();
	}
	
	@Test
	public void testPromoteDemoteNewElementMaxHeap() {
		promoteDemoteNewElementMaxHeap();
	}
	
	@Test
	public void testAddSimpleMaxHeap() {
		addSimpleMaxHeap();
	}
}
