# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-05-16

### Added
* DisjointSetForest: representation of disjoint sets of generic objects.
* DisjointIntegerSetForest: representation of disjoint sets of integers with a disjoint set forest.
* PriorityQueue: common interface to different implementations of priority queues, with sub-interfaces:
  * PriorityQueue.Integer for int-valued priorities.
  * PriorityQueue.Double for double-valued priorities.
* BinaryHeap: implements PriorityQueue.Integer with binary heap for both min-heap and max-heap cases.
* BinaryHeapDouble: implements PriorityQueue.Double with binary heap for both min-heap and max-heap cases.

### Changed
* Migrated test cases to JUnit Jupiter 5.8.2.

### Deprecated

### Removed

### Fixed

### CI/CD

### Other


## [1.1.0] - 2022-02-08

### Added
* IntegerList class, which is a partially-filled array of primitive int values.
* DoubleList class, which is a partially-filled array of primitive double values.


## [1.0.0] - 2021-09-24

### Added
* This initial release has only the Copyable interface. More will be added in the
  near future.
