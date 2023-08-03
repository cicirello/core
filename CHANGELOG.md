# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2023-08-03

### Added

### Changed

### Deprecated

### Removed

### Fixed

### CI/CD

### Other
* Integrated SpotBugs static analysis into build process.


## [2.5.0] - 2023-04-21

### Added
* DoubleArray
* IntegerArray


## [2.4.6] - 2023-02-28

### Changed
* Minor code improvements to add and remove methods of IntegerList and DoubleList.


## [2.4.5] - 2023-02-26

### Changed
* Optimized buffer reallocation and other related internal code in: IntegerList, DoubleList, BinaryHeap, BinaryHeapDouble, SimpleBinaryHeap, SimpleBinaryHeapDouble.


## [2.4.4] - 2023-01-10

### Changed
* Refactored all Fibonacci heap classes.


## [2.4.3] - 2022-11-13

### Changed
* Refactored all priority queue classes to optimize and make other code improvements to priority-order determination.


## [2.4.2] - 2022-11-11

### Changed
* Refactored SimpleFibonacciHeap to reduce cyclomatic complexity, among other maintainability improvements.
* Additional refactoring of SimpleFibonacciHeapDouble for further improvements over 2.4.1.
* Refactored equals method in the SimpleFibonacciHeap, FibonacciHeap, SimpleBinaryHeap, BinaryHeap classes.
* Refactored equals method in the SimpleFibonacciHeapDouble, FibonacciHeapDouble, SimpleBinaryHeapDouble, BinaryHeapDouble classes.
* Reformatted all code to follow [Google's Java style](https://google.github.io/styleguide/javaguide.html).
* Refactored test cases for SimpleFibonacciHeap, FibonacciHeap, SimpleBinaryHeap, BinaryHeap.
* Refactored test cases for SimpleFibonacciHeapDouble, FibonacciHeapDouble, SimpleBinaryHeapDouble, BinaryHeapDouble.
* Refactored test cases for IntFibonacciHeapDouble, IntBinaryHeapDouble.
* Refactored test cases for IntFibonacciHeap, IntBinaryHeap.

### Other
* Configured [refactor-first-maven-plugin](https://github.com/jimbethancourt/RefactorFirst) in the library's pom.xml.
* Adopted [Google's Java style](https://google.github.io/styleguide/javaguide.html).
* Configured [Spotify's fmt-maven-plugin](https://github.com/spotify/fmt-maven-plugin) in the library's pom.xml.


## [2.4.1] - 2022-10-21

### Changed
* Refactored SimpleFibonacciHeapDouble to reduce cyclomatic complexity, among other maintainability improvements.


## [2.4.0] - 2022-10-07

### Added
* ArrayLengthEnforcer: utility class that validates array length equal to target, reallocating if necessary.
* ArrayMinimumLengthEnforcer: utility class that validates array length at least a minimum target, reallocating if necessary.
* ArrayFiller: utility class for creating and/or filling int arrays with consecutive integers.


## [2.3.0] - 2022-09-16

### Added
* SimpleBinaryHeap class: a basic implementation of a binary heap with integer priorities that allows 
  duplicate elements (unlike the BinaryHeap class), but lacks constant time lookups and thus lacks the 
  speed advantage for operations like priority changes that constant time lookups provide.
* SimpleBinaryHeapDouble class: a basic implementation of a binary heap with double priorities that allows 
  duplicate elements (unlike the BinaryHeapDouble class), but lacks constant time lookups and thus lacks the 
  speed advantage for operations like priority changes that constant time lookups provide.
* SimpleFibonacciHeap class: a basic implementation of a Fibonacci heap with integer priorities that allows 
  duplicate elements (unlike the FibonacciHeap class), but lacks constant time lookups and thus lacks the 
  speed advantage for operations like priority changes that constant time lookups provide.
* SimpleFibonacciHeapDouble class: a basic implementation of a Fibonacci heap with double priorities that allows 
  duplicate elements (unlike the FibonacciHeapDouble class), but lacks constant time lookups and thus lacks the 
  speed advantage for operations like priority changes that constant time lookups provide.
* PriorityQueue.pollThenAdd and PriorityQueueDouble.pollThenAdd methods, including default implementation in
  the interfaces, and overridden implementation in the binary heap classes that exploit binary heap structure.
* IntegerList.sort() and DoubleList.sort() methods.

### Changed
* Improved javadoc documentation in the PriorityQueue and PriorityQueueDouble interfaces, and the various
  classes that implement them.
* Refactored FibonacciHeap as a subclass of the new SimpleFibonacciHeap.
* Refactored FibonacciHeapDouble as a subclass of the new SimpleFibonacciHeapDouble.


## [2.2.2] - 2022-07-13

### CI/CD
* Triggering JitPack build on release. No changes in this release.


## [2.2.1] - 2022-07-11

### Other
* Configured JitPack build to use JitPack as a backup source of artifacts. No changes in this release.


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
