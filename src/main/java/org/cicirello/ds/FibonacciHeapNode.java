/*
 * Module org.cicirello.core
 * Copyright 2019-2023 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Internal package access class for representing nodes in a fibonacci heap with priority of type
 * int.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
final class FibonacciHeapNode<E> {
  PriorityQueueNode.Integer<E> e;
  private FibonacciHeapNode<E> parent;
  private FibonacciHeapNode<E> child;
  private FibonacciHeapNode<E> left;
  private FibonacciHeapNode<E> right;
  private int degree;
  private boolean mark;

  /*
   * new root list (i.e., called to create new top-level list when empty
   */
  public FibonacciHeapNode(PriorityQueueNode.Integer<E> e) {
    this.e = e;
    singletonList();
  }

  /*
   * adds newly constructed node to root list
   */
  public FibonacciHeapNode(PriorityQueueNode.Integer<E> e, FibonacciHeapNode<E> list) {
    this.e = e;
    insertInto(list);
  }

  private FibonacciHeapNode(FibonacciHeapNode<E> other) {
    e = other.e.copy();
    degree = other.degree;
    mark = other.mark;
  }

  private FibonacciHeapNode(FibonacciHeapNode<E> other, FibonacciHeapNode<E> toTheLeft) {
    this(other);
    left = toTheLeft;
  }

  final FibonacciHeapNode<E> copy() {
    return copyList(this, null);
  }

  private FibonacciHeapNode<E> copyList(FibonacciHeapNode<E> x, FibonacciHeapNode<E> p) {
    FibonacciHeapNode<E> y = new FibonacciHeapNode<E>(x);
    y.parent = p;
    if (x.child != null) {
      y.child = copyList(x.child, y);
    }
    FibonacciHeapNode<E> rightOf = y;
    for (FibonacciHeapNode<E> next = x.right;
        next != x;
        next = next.right, rightOf = rightOf.right) {
      rightOf.right = new FibonacciHeapNode<E>(next, rightOf);
      rightOf.right.parent = p;
      if (next.child != null) {
        rightOf.right.child = copyList(next.child, rightOf.right);
      }
    }
    rightOf.right = y;
    y.left = rightOf;
    return y;
  }

  final FibonacciHeapNode<E> parent() {
    return parent;
  }

  final void singletonList() {
    left = right = this;
  }

  final void insertInto(FibonacciHeapNode<E> list) {
    right = list.right;
    left = list;
    list.right = list.right.left = this;
  }

  final void insertListInto(FibonacciHeapNode<E> list) {
    list.right.left = left;
    left.right = list.right;
    list.right = this;
    left = list;
  }

  final void clearParentReferences() {
    for (FibonacciHeapNode<E> next = this; next.parent != null; next = next.right) {
      next.parent = null;
    }
  }

  final void cascadingCut(FibonacciHeapNode<E> root) {
    FibonacciHeapNode<E> z = parent;
    if (z != null) {
      if (!mark) {
        mark = true;
      } else {
        cut(z, root);
        z.cascadingCut(root);
      }
    }
  }

  final void cut(FibonacciHeapNode<E> y, FibonacciHeapNode<E> root) {
    // 1. remove this from child list of y, decrementing degree of y
    if (y.degree > 1) {
      // ensure y's child reference isn't this
      y.child = right;
      // link this's left and right neighbors to remove this
      left.right = right;
      right.left = left;
      y.degree--;
    } else {
      y.child = null;
      y.degree = 0;
    }
    // 2. add this to the root list
    insertInto(root);
    parent = null;
    mark = false;
  }

  final FibonacciHeapNode<E> find(Object element) {
    NodeIterator<E> iter = new NodeIterator<E>(this);
    while (iter.hasNext()) {
      FibonacciHeapNode<E> n = iter.next();
      if (n.e.element.equals(element)) {
        return n;
      }
    }
    return null;
  }

  static <T> FibonacciHeapNode<T> find(FibonacciHeapNode<T> start, Object element) {
    return start == null ? null : start.find(element);
  }

  final FibonacciHeapNode<E> removeSelf() {
    // this assumes there is more than one node. don't call for single node
    if (child != null) {
      child.clearParentReferences();
      child.insertListInto(this);
    }
    FibonacciHeapNode<E> min = right;
    left.right = min;
    min.left = left;
    return min;
  }

  static final class Consolidator<E2> {

    // This array is what is referred to in CLRS description
    // of algorithm as A in the method consolidate. As an optimization
    // we construct the array once, and reuse it on all calls to consolidate.
    private final FibonacciHeapNode<E2>[] rootsByDegrees;

    private static final double INV_LOG_GOLDEN_RATIO = 2.0780869212350273;

    private final Prioritizer compare;

    Consolidator(Prioritizer compare) {
      this.compare = compare;
      // length of array used by consolidate is initialized to 45 as follows:
      // 1) since size is an int, the implicit limit on capacity is Integer.MAX_VALUE.
      // 2) Thus, the highest that D(n) can be for a call to consolidate is:
      //    floor(log(Integer.MAX_VALUE) / log((1+sqrt(5))/2)) = 44.
      // 3) Array must be of length 1+D(n), so longest array must be is 45.
      // 4) consolidate computes the actual D(n) for a specific call, and uses only
      //    part of this array.
      rootsByDegrees = nodeArrayAllocate(45);
    }

    private FibonacciHeapNode<E2>[] nodeArrayAllocate(int n) {
      @SuppressWarnings("unchecked")
      FibonacciHeapNode<E2>[] array = new FibonacciHeapNode[n];
      return array;
    }

    final FibonacciHeapNode<E2> consolidate(FibonacciHeapNode<E2> min, int size) {
      int dn = (int) (Math.log(size) * INV_LOG_GOLDEN_RATIO);

      // first node of iteration
      FibonacciHeapNode<E2> w = min;
      // disconnect from left to enable detecting end of list
      w.left.right = null;
      do {
        FibonacciHeapNode<E2> x = w;
        // prepare for next iteration
        w = w.right;
        // disconnect x from root list
        x.left = x.right = null;

        int d = x.degree;
        while (rootsByDegrees[d] != null) {
          FibonacciHeapNode<E2> y = rootsByDegrees[d];
          if (compare.comesBefore(y.e.value, x.e.value)) {
            FibonacciHeapNode<E2> temp = x;
            x = y;
            y = temp;
          }
          fibHeapLink(y, x);
          rootsByDegrees[d] = null;
          d++;
        }
        rootsByDegrees[d] = x;
      } while (w != null);

      min = null;
      for (int i = 0; i <= dn; i++) {
        if (rootsByDegrees[i] != null) {
          if (min == null) {
            rootsByDegrees[i].singletonList();
            min = rootsByDegrees[i];
          } else {
            rootsByDegrees[i].insertInto(min);
            if (compare.comesBefore(rootsByDegrees[i].e.value, min.e.value)) {
              min = rootsByDegrees[i];
            }
          }
          // need this since this array is shared by all calls to consolidate
          rootsByDegrees[i] = null;
        }
      }
      return min;
    }

    private void fibHeapLink(FibonacciHeapNode<E2> y, FibonacciHeapNode<E2> x) {
      // 1. Remove y from root list step.
      //    This is not needed because I'm instead
      //    dismantling root list in consolidate
      //    before rebuilding it.
      // 2. Make y a child of x
      if (x.degree > 0) {
        y.insertInto(x.child);
        y.parent = x;
        x.degree++;
      } else {
        y.singletonList();
        x.child = y;
        y.parent = x;
        x.degree = 1;
      }
      y.mark = false;
    }
  }

  static final class FibonacciHeapIterator<E2> implements Iterator<PriorityQueueNode.Integer<E2>> {

    private final NodeIterator<E2> iter;

    public FibonacciHeapIterator(FibonacciHeapNode<E2> root) {
      iter = new NodeIterator<E2>(root);
    }

    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public PriorityQueueNode.Integer<E2> next() {
      return iter.next().e;
    }
  }

  static final class NodeIterator<E2> implements Iterator<FibonacciHeapNode<E2>> {

    private final Deque<FibonacciHeapNode<E2>> stack;

    public NodeIterator(FibonacciHeapNode<E2> root) {
      stack = new ArrayDeque<FibonacciHeapNode<E2>>();
      if (root != null) {
        stack.push(root);
        for (FibonacciHeapNode<E2> next = root.right; next != root; next = next.right) {
          stack.push(next);
        }
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public FibonacciHeapNode<E2> next() {
      // normally, the next method of an Iterator is
      // required to throw NoSuchElementException is called when empty.
      // The pop() method of the ArrayDeque does this though. So no need
      // for explicit check.
      FibonacciHeapNode<E2> current = stack.pop();
      if (current.degree > 0) {
        stack.push(current.child);
        FibonacciHeapNode<E2> next = current.child.right;
        for (int i = 1; i < current.degree; i++, next = next.right) {
          stack.push(next);
        }
      }
      return current;
    }
  }
}
