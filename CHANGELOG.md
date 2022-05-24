# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-05-24

### Added
* FibonacciHeapDouble: implements PriorityQueueDouble with a Fibonacci heap for both min-heap and max-heap cases.

### Changed

### Deprecated

### Removed

### Fixed

### CI/CD

### Other


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
