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

/** JUnit tests for the DisjointIntegerSetForest class. */
public class DisjointIntegerSetForestTests {

  @Test
  public void testConstruction() {
    final int[] N = {0, 1, 2, 4, 8, 16};
    for (int n : N) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      assertEquals(n, sets.size());
      for (int e = 0; e < n; e++) {
        assertEquals(e, sets.findSet(e));
        assertEquals(e, sets.findSet(e));
      }
    }
  }

  @Test
  public void testCopyEqualsHashCode() {
    for (int n = 2; n <= 16; n *= 2) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      for (int i = 0; i < n; i += 2) {
        DisjointIntegerSetForest copy = sets.copy();
        assertEquals(sets, copy);
        assertEquals(sets.hashCode(), copy.hashCode());
        assertTrue(sets != copy);
        sets.union(i, i + 1);
        assertNotEquals(sets, copy);
        assertNotEquals(sets.hashCode(), copy.hashCode());
      }
    }
    DisjointIntegerSetForest sets = new DisjointIntegerSetForest(6);
    assertNotEquals(sets, null);
    assertNotEquals(sets, "hello");
    DisjointIntegerSetForest copy1 = sets.copy();
    DisjointIntegerSetForest copy2 = sets.copy();
    copy1.union(1, 0);
    copy2.union(2, 0);
    assertNotEquals(copy1, copy2);
    copy1 = sets.copy();
    copy2 = sets.copy();
    copy1.union(0, 1);
    copy2.union(0, 2);
    assertNotEquals(copy1, copy2);
  }

  @Test
  public void testUnionOneLevel() {
    for (int n = 2; n <= 16; n *= 2) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      for (int i = 0; i < n; i += 2) {
        sets.union(i, i + 1);
        assertEquals(sets.findSet(i), sets.findSet(i + 1));
        for (int j = 0; j < n; j++) {
          if (j != i && j != i + 1) {
            assertNotEquals(sets.findSet(i), sets.findSet(j));
          }
        }
      }
    }
    for (int n = 2; n <= 16; n *= 2) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      for (int i = 0; i < n; i += 2) {
        sets.union(i + 1, i);
        assertEquals(sets.findSet(i), sets.findSet(i + 1));
        for (int j = 0; j < n; j++) {
          if (j != i && j != i + 1) {
            assertNotEquals(sets.findSet(j), sets.findSet(i));
          }
        }
      }
    }
  }

  @Test
  public void testSameSetOneLevel() {
    for (int n = 2; n <= 16; n *= 2) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      for (int i = 0; i < n; i += 2) {
        sets.union(i, i + 1);
        assertTrue(sets.sameSet(i, i + 1));
        for (int j = 0; j < n; j++) {
          if (j != i && j != i + 1) {
            assertFalse(sets.sameSet(i, j));
          }
        }
      }
    }
    for (int n = 2; n <= 16; n *= 2) {
      DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
      for (int i = 0; i < n; i += 2) {
        sets.union(i + 1, i);
        assertTrue(sets.sameSet(i, i + 1));
        for (int j = 0; j < n; j++) {
          if (j != i && j != i + 1) {
            assertFalse(sets.sameSet(j, i));
          }
        }
      }
    }
  }

  @Test
  public void testUnionMultilevelSame() {
    int n = 8;
    DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
    for (int i = 0; i < n; i += 2) {
      sets.union(i, i + 1);
    }
    for (int i = 0; i < n; i += 4) {
      sets.union(i, i + 2);
    }
    for (int i = 0; i < 4; i++) {
      assertEquals(sets.findSet(0), sets.findSet(i));
      assertNotEquals(sets.findSet(0), sets.findSet(4 + i));
    }
    for (int i = 0; i < 4; i++) {
      assertEquals(sets.findSet(4), sets.findSet(4 + i));
      assertNotEquals(sets.findSet(4), sets.findSet(i));
    }
  }

  @Test
  public void testSameSetMultiLevel() {
    int n = 8;
    DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
    for (int i = 0; i < n; i += 2) {
      sets.union(i, i + 1);
    }
    for (int i = 0; i < n; i += 4) {
      sets.union(i, i + 2);
    }
    for (int i = 0; i < 4; i++) {
      assertTrue(sets.sameSet(0, i));
      assertFalse(sets.sameSet(0, 4 + i));
    }
    for (int i = 0; i < 4; i++) {
      assertTrue(sets.sameSet(4, 4 + i));
      assertFalse(sets.sameSet(4, i));
    }
  }

  @Test
  public void testUnionMultilevelDifferentRanksSmallerRightParam() {
    int n = 16;
    DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
    for (int i = 0; i < n; i += 2) {
      sets.union(i, i + 1);
    }
    for (int i = 0; i < n; i += 4) {
      sets.union(i, i + 2);
    }
    sets.union(0, 4);
    sets.union(0, 8);
    for (int i = 0; i < 12; i++) {
      assertEquals(sets.findSet(0), sets.findSet(i));
      for (int j = 12; j < n; j++) {
        assertNotEquals(sets.findSet(i), sets.findSet(j));
      }
    }
  }

  @Test
  public void testUnionMultilevelDifferentRanksSmallerLeftParam() {
    int n = 16;
    DisjointIntegerSetForest sets = new DisjointIntegerSetForest(n);
    for (int i = 0; i < n; i += 2) {
      sets.union(i, i + 1);
    }
    for (int i = 0; i < n; i += 4) {
      sets.union(i, i + 2);
    }
    sets.union(0, 4);
    sets.union(8, 0);
    for (int i = 0; i < 12; i++) {
      assertEquals(sets.findSet(0), sets.findSet(i));
      for (int j = 12; j < n; j++) {
        assertNotEquals(sets.findSet(i), sets.findSet(j));
      }
    }
  }
}
