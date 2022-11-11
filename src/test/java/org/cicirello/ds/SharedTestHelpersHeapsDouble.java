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

import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * Common functionality for test cases for the various heap classes with double-valued priorities.
 */
public abstract class SharedTestHelpersHeapsDouble {

  private final int ADJUST;

  SharedTestHelpersHeapsDouble(boolean isMinCase) {
    ADJUST = isMinCase ? 1 : -1;
  }

  final double[] createPriorities(int n) {
    double[] priorities = new double[n];
    for (int i = 0; i < n; i++) {
      priorities[i] = ADJUST * (2 + 2 * i);
    }
    return priorities;
  }

  final void populate(
      PriorityQueueDouble<String> pq, String[] elements, double[] priorities, int n) {
    for (int j = 0; j < n; j++) {
      pq.offer(elements[j], priorities[j]);
    }
  }

  final String[] createStrings(int n) {
    String[] s = new String[n];
    for (int i = 0; i < n; i++) {
      s[i] = ((char) ('A' + i)) + "";
    }
    return s;
  }

  final String[] createStringsRev(int n) {
    String[] s = new String[n];
    for (int i = 0; i < n; i++) {
      s[n - 1 - i] = ((char) ('A' + ADJUST * i)) + "";
    }
    return s;
  }

  final String[] createStringsArbitrary(int n) {
    ArrayList<String> list = new ArrayList<String>(n);
    for (int i = 0; i < n; i++) {
      list.add(((char) ('A' + ADJUST * i)) + "");
    }
    shuffle(list, new SplittableRandom(42));
    return list.toArray(new String[n]);
  }

  final double[] createPriorities(String[] elements) {
    double[] p = new double[elements.length];
    for (int i = 0; i < elements.length; i++) {
      p[i] = (int) elements[i].charAt(0);
    }
    return p;
  }

  final PriorityQueueNode.Double<String>[] createPairs(String[] elements, double[] priorities) {
    @SuppressWarnings("unchecked")
    PriorityQueueNode.Double<String>[] pairs =
        (PriorityQueueNode.Double<String>[]) new PriorityQueueNode.Double[elements.length];
    for (int i = 0; i < pairs.length; i++) {
      pairs[i] = new PriorityQueueNode.Double<String>(elements[i], priorities[i]);
    }
    return pairs;
  }

  final void shuffle(ArrayList<String> list, SplittableRandom r) {
    for (int i = list.size() - 1; i > 0; i--) {
      int j = r.nextInt(i + 1);
      if (i != j) {
        String temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
      }
    }
  }

  // MAX CASE

  final String[] createStringsMaxCase(int n) {
    String[] s = new String[n];
    for (int i = 0; i < n; i++) {
      s[i] = ((char) ('A' - i)) + "";
    }
    return s;
  }
}
