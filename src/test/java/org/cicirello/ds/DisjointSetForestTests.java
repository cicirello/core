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
 * JUnit tests for the DisjointSetForest class.
 */
public class DisjointSetForestTests {
	
	private DisjointSetForest<String> createSets(int n) {
		DisjointSetForest<String> sets = new DisjointSetForest<String>();
		for (int i = 0; i < n; i++) {
			assertEquals(i, sets.size());
			String x = ((char)('A'+i))+"";
			sets.makeSet(x);
		}
		assertEquals(n, sets.size());
		return sets;
	}
	
	@Test
	public void testConstruction() {
		final int[] N = {0, 1, 2, 4, 8, 16};
		for (int n : N) {
			DisjointSetForest<String> sets = createSets(n);
			assertEquals(n, sets.size());
			assertFalse(sets.contains("hello"));
			
			int[] ids = new int[n];
			for (int e = 0; e < n; e++) {
				String element = ""+((char)('A'+e));
				ids[e] = sets.findSet(element);
				assertEquals(ids[e], sets.findSet(element));
				assertTrue(sets.contains(element));
			}
			for (int i = 0; i < n; i++) {
				for (int j = i+1; j < n; j++) {
					assertNotEquals(ids[i], ids[j]);
				}
			}
			
			IllegalArgumentException thrown = assertThrows( 
				IllegalArgumentException.class,
				() -> sets.findSet("hello")
			);
			
			if (n > 0) {
				thrown = assertThrows( 
					IllegalArgumentException.class,
					() -> sets.makeSet("A")
				);
			}
		}
	}
	
	@Test
	public void testUnionOneLevel() {
		for (int n = 2; n <= 16; n *= 2) {
			DisjointSetForest<String> sets = createSets(n);
			for (int i = 0; i < n; i += 2) {
				String x = "" + ((char)('A'+i));
				String y = "" + ((char)('A'+i+1));
				sets.union(x, y);
				assertEquals(sets.findSet(x), sets.findSet(y));
				for (int j = 0; j < n; j++) {
					if (j!=i && j!=i+1) {
						String z = "" + ((char)('A'+j));
						assertNotEquals(sets.findSet(x), sets.findSet(z));
					}
				}
			}
			
			IllegalArgumentException thrown = assertThrows( 
				IllegalArgumentException.class,
				() -> sets.union("A", "hello")
			);
		}
		for (int n = 2; n <= 16; n *= 2) {
			DisjointSetForest<String> sets = createSets(n);
			for (int i = 0; i < n; i += 2) {
				String x = "" + ((char)('A'+i));
				String y = "" + ((char)('A'+i+1));
				sets.union(y, x);
				assertEquals(sets.findSet(x), sets.findSet(y));
				for (int j = 0; j < n; j++) {
					if (j!=i && j!=i+1) {
						String z = "" + ((char)('A'+j));
						assertNotEquals(sets.findSet(x), sets.findSet(z));
					}
				}
			}
			IllegalArgumentException thrown = assertThrows( 
				IllegalArgumentException.class,
				() -> sets.union("hello", "A")
			);
		}
	}
	
	@Test
	public void testUnionMultilevelSame() {
		int n = 8;
		DisjointSetForest<String> sets = createSets(n);
		for (int i = 0; i < n; i += 2) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+1));
			sets.union(x, y);
		}
		for (int i = 0; i < n; i += 4) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+2));
			sets.union(x,y);
		}
		for (int i = 0; i < 4; i++) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+4));
			assertEquals(sets.findSet("A"), sets.findSet(x));
			assertNotEquals(sets.findSet("A"), sets.findSet(y));
		}
		for (int i = 0; i < 4; i++) {
			String x = "" + ((char)('A'+4));
			String y = "" + ((char)('A'+i+4));
			String z = "" + ((char)('A'+i));
			assertEquals(sets.findSet(x), sets.findSet(y));
			assertNotEquals(sets.findSet(x), sets.findSet(z));
		}
	}
	
	@Test
	public void testUnionMultilevelDifferentRanksSmallerRightParam() {
		int n = 16;
		DisjointSetForest<String> sets = createSets(n);
		for (int i = 0; i < n; i += 2) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+1));
			sets.union(x, y);
		}
		for (int i = 0; i < n; i += 4) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+2));
			sets.union(x, y);
		}
		sets.union("A","E");
		sets.union("A","I");
		for (int i = 0; i < 12; i++) {
			String x = "" + ((char)('A'+i));
			assertEquals(sets.findSet("A"), sets.findSet(x));
			for (int j = 12; j < n; j++) {
				String y = "" + ((char)('A'+j));
				assertNotEquals(sets.findSet(x), sets.findSet(y));
			}
		}
	}
	
	@Test
	public void testUnionMultilevelDifferentRanksSmallerLeftParam() {
		int n = 16;
		DisjointSetForest<String> sets = createSets(n);
		for (int i = 0; i < n; i += 2) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+1));
			sets.union(x, y);
		}
		for (int i = 0; i < n; i += 4) {
			String x = "" + ((char)('A'+i));
			String y = "" + ((char)('A'+i+2));
			sets.union(x, y);
		}
		sets.union("A","E");
		sets.union("I","A");
		for (int i = 0; i < 12; i++) {
			String x = "" + ((char)('A'+i));
			assertEquals(sets.findSet("A"), sets.findSet(x));
			for (int j = 12; j < n; j++) {
				String y = "" + ((char)('A'+j));
				assertNotEquals(sets.findSet(x), sets.findSet(y));
			}
		}
	}
}
