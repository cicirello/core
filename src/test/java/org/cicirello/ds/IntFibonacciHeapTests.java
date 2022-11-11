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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** JUnit tests for the IntFibonacciHeap class. */
public class IntFibonacciHeapTests extends SharedTestHelpersIntHeap {

  public IntFibonacciHeapTests() {
    super(IntFibonacciHeap::createMinHeap, IntFibonacciHeap::createMaxHeap);
  }

  // Tests not specific to min heap vs max heap

  @Test
  public void testCopy() {
    copy(true);
  }

  // MIN HEAP TESTS

  @Test
  public void testCreateMinHeap() {
    createMinHeap();
  }

  @Test
  public void testClearMinHeap() {
    clearMinHeap();
  }

  @Test
  public void testIncreasingPriorityMinHeap() {
    increasingPriorityMinHeap();
  }

  @Test
  public void testDecreasingPriorityMinHeap() {
    decreasingPriorityMinHeap();
  }

  @Test
  public void testRandomPriorityMinHeap() {
    randomPriorityMinHeap();
  }

  @Test
  public void testChangePriorityMinHeapToFront() {
    changePriorityMinHeapToFront();
  }

  @Test
  public void testChangePriorityMinHeapToBack() {
    changePriorityMinHeapToBack();
  }

  @Test
  public void testChangePriorityMinHeapToInterior() {
    changePriorityMinHeapToInterior();
  }

  @Test
  public void testChangePriorityMinHeapEqualChange() {
    changePriorityMinHeapEqualChange();
  }

  @Test
  public void testChangePriorityMinHeapSameRelativeOrderToNextBest() {
    changePriorityMinHeapSameRelativeOrderToNextBest();
  }

  @Test
  public void testChangePriorityMinHeapSameRelativeOrderToNextWorst() {
    changePriorityMinHeapSameRelativeOrderToNextWorst();
  }

  @Test
  public void testChangePriorityMinHeapNewElement() {
    changePriorityMinHeapNewElement();
  }

  @Test
  public void testMultilevelPromoteDemoteMinHeap() {
    multilevelPromoteDemoteMinHeap();
  }

  @Test
  public void testPromoteToFrontMinHeap() {
    promoteToFrontMinHeap();
  }

  @Test
  public void testDemoteToBackMinHeap() {
    demoteToBackMinHeap();
  }

  @Test
  public void testPromoteDemoteToInteriorMinHeap() {
    promoteDemoteToInteriorMinHeap();
  }

  @Test
  public void testPromoteDemoteEqualChangeMinHeap() {
    promoteDemoteEqualChangeMinHeap();
  }

  @Test
  public void testPromoteDemoteNewElementMinHeap() {
    promoteDemoteNewElementMinHeap();
  }

  // MAX HEAP TESTS

  @Test
  public void testCreateMaxHeap() {
    createMaxHeap();
  }

  @Test
  public void testClearMaxHeap() {
    clearMaxHeap();
  }

  @Test
  public void testIncreasingPriorityMaxHeap() {
    increasingPriorityMaxHeap();
  }

  @Test
  public void testDecreasingPriorityMaxHeap() {
    decreasingPriorityMaxHeap();
  }

  @Test
  public void testRandomPriorityMaxHeap() {
    randomPriorityMaxHeap();
  }

  @Test
  public void testChangePriorityMaxHeapToFront() {
    changePriorityMaxHeapToFront();
  }

  @Test
  public void testChangePriorityMaxHeapToBack() {
    changePriorityMaxHeapToBack();
  }

  @Test
  public void testChangePriorityMaxHeapToInterior() {
    changePriorityMaxHeapToInterior();
  }

  @Test
  public void testChangePriorityMaxHeapEqualChange() {
    changePriorityMaxHeapEqualChange();
  }

  @Test
  public void testChangePriorityMaxHeapSameRelativeOrderToNextBest() {
    changePriorityMaxHeapSameRelativeOrderToNextBest();
  }

  @Test
  public void testChangePriorityMaxHeapSameRelativeOrderToNextWorst() {
    changePriorityMaxHeapSameRelativeOrderToNextWorst();
  }

  @Test
  public void testChangePriorityMaxHeapNewElement() {
    changePriorityMaxHeapNewElement();
  }

  @Test
  public void testMultilevelPromoteDemoteMaxHeap() {
    multilevelPromoteDemoteMaxHeap();
  }

  @Test
  public void testPromoteToFrontMaxHeap() {
    promoteToFrontMaxHeap();
  }

  @Test
  public void testDemoteToBackMaxHeap() {
    demoteToBackMaxHeap();
  }

  @Test
  public void testPromoteDemoteToInteriorMaxHeap() {
    promoteDemoteToInteriorMaxHeap();
  }

  @Test
  public void testPromoteDemoteEqualChangeMaxHeap() {
    promoteDemoteEqualChangeMaxHeap();
  }

  @Test
  public void testPromoteDemoteNewElementMaxHeap() {
    promoteDemoteNewElementMaxHeap();
  }
}
