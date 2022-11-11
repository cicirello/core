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

/** Test cases for BinaryHeapDouble for max heap cases: constructor with initial capacity. */
public class BinaryCapacityMaxHeapDoubleTests extends SharedTestHelpersMaxHeapsDouble {

  public BinaryCapacityMaxHeapDoubleTests() {
    super(() -> BinaryHeapDouble.createMaxHeap(8), BinaryHeapDouble::createMaxHeap);
  }

  @Test
  public void testDefaultMaxHeap() {
    defaultMaxHeap();
  }

  @Test
  public void testDefaultReverseMaxHeap() {
    defaultReverseMaxHeap();
  }

  @Test
  public void testDefaultArbitraryMaxHeap() {
    defaultArbitraryMaxHeap();
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
  public void testOfferSeparateMaxHeap() {
    offerSeparateMaxHeap();
  }

  @Test
  public void testOfferSeparateReverseMaxHeap() {
    offerSeparateReverseMaxHeap();
  }

  @Test
  public void testOfferSeparateArbitraryMaxHeap() {
    offerSeparateArbitraryMaxHeap();
  }

  @Test
  public void testAddSeparateMaxHeap() {
    addSeparateMaxHeap();
  }
}
