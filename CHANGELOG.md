# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-07-06

### Added

### Changed

### Deprecated

### Removed

### Fixed

### CI/CD
* Added Webhook to trigger builds on JitPack.

### Other


## [2.2.0] - 2022-06-07

### Added
* MergeablePriorityQueue interface for priority queues with merge support.
* MergeablePriorityQueueDouble interface for priority queues with merge support.
* BinaryHeap.merge(BinaryHeap) for merging binary heaps with int priorities.
* BinaryHeapDouble.merge(BinaryHeapDouble) for merging binary heaps with double priorities.
* FibonacciHeap.merge(FibonacciHeap) for merging Fibonacci heaps with int priorities.
* FibonacciHeapDouble.merge(FibonacciHeapDouble) for merging Fibonacci heaps with double priorities.

### Changed
* Improved implementation of BinaryHeap.retainAll(Collection) to an O(m + n) runtime.
* Improved implementation of BinaryHeapDouble.retainAll(Collection) to an O(m + n) runtime.
* Improved implementation of BinaryHeap.removeAll(Collection) to an O(m + n) runtime.
* Improved implementation of BinaryHeapDouble.removeAll(Collection) to an O(m + n) runtime.
* Improved implementation of BinaryHeap.addAll(Collection) to an O(m + n) runtime.
* Improved implementation of BinaryHeapDouble.addAll(Collection) to an O(m + n) runtime.
* Improved implementation of FibonacciHeap.retainAll(Collection) to an O(m + n) runtime.
* Improved implementation of FibonacciHeapDouble.retainAll(Collection) to an O(m + n) runtime.
* Improved implementation of FibonacciHeap.removeAll(Collection) to an O(m + n) runtime.
* Improved implementation of FibonacciHeapDouble.removeAll(Collection) to an O(m + n) runtime.

### Removed
* Default implementation of PriorityQueue.removeAll(Collection): All relevant classes now implement this directly.
* Default implementation of PriorityQueueDouble.removeAll(Collection): All relevant classes now implement this directly.


## [2.1.0] - 2022-05-26

### Added
* FibonacciHeap: implements PriorityQueue with a Fibonacci heap for both min-heap and max-heap cases.
* FibonacciHeapDouble: implements PriorityQueueDouble with a Fibonacci heap for both min-heap and max-heap cases.
* IntFibonacciHeap: implements IntPriorityQueue with a Fibonacci heap for both min-heap and max-heap cases.
* IntFibonacciHeapDouble: implements IntPriorityQueueDouble with a Fibonacci heap for both min-heap and max-heap cases.
* Added promote and demote methods to the PriorityQueue, PriorityQueueDouble, IntPriorityQueue, IntPriorityQueueDouble
  interfaces for changing priorities strictly in one direction, and implemented in the various heap classes.

### Changed
* Altered change method of the PriorityQueue, PriorityQueueDouble, IntPriorityQueue, IntPriorityQueueDouble
  interfaces, and of the classes that implement them to return a boolean rather than void.
* Refactored IntBinaryHeap and IntBinaryHeapDouble to improve max-heap implementation.

### Fixed
* Bug in BinaryHeap and BinaryHeapDouble with potential to break encapsulation.


## [2.0.0] - 2022-05-19

**BREAKING CHANGES:** See changes section for details. Breaking changes include increasing
minimum supported Java version to Java 17.

### Added
* DisjointSetForest: representation of disjoint sets of generic objects.
* DisjointIntegerSetForest: representation of disjoint sets of integers with a disjoint set forest.
* PriorityQueue: interface for priority queues with int-valued priorities.
* PriorityQueueDouble: interface for priority queues with double-valued priorities.
* BinaryHeap: implements PriorityQueue with binary heap for both min-heap and max-heap cases.
* BinaryHeapDouble: implements PriorityQueueDouble with binary heap for both min-heap and max-heap cases.
* IntPriorityQueue: interface for priority queues of int elements with int-valued priorities.
* IntPriorityQueueDouble: interface for priority queues of int elements with double-valued priorities.
* IntBinaryHeap: implements IntPriorityQueue with binary heap for both min-heap and max-heap cases.
* IntBinaryHeapDouble: implements IntPriorityQueueDouble with binary heap for both min-heap and max-heap cases.

### Changed
* Minimum supported Java version is now Java 17.
* Migrated test cases to JUnit Jupiter 5.8.2.


## [1.1.0] - 2022-02-08

### Added
* IntegerList class, which is a partially-filled array of primitive int values.
* DoubleList class, which is a partially-filled array of primitive double values.


## [1.0.0] - 2021-09-24

### Added
* This initial release has only the Copyable interface. More will be added in the
  near future.
