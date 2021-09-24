# Module org.cicirello.core

Copyright (C) 2019-2021 [Vincent A. Cicirello](https://www.cicirello.org/).

Website: https://core.cicirello.org/

API documentation: https://core.cicirello.org/api

| __Artifacts and Releases__ | [![Maven Central](https://img.shields.io/maven-central/v/org.cicirello/core.svg?label=Maven%20Central&logo=apachemaven)](https://search.maven.org/artifact/org.cicirello/core) [![GitHub release (latest by date)](https://img.shields.io/github/v/release/cicirello/core?logo=GitHub)](https://github.com/cicirello/core/releases) |
| :--- | :--- |
| __Build Status__ | [![build](https://github.com/cicirello/core/workflows/build/badge.svg)](https://github.com/cicirello/core/actions/workflows/build.yml) [![docs](https://github.com/cicirello/core/workflows/docs/badge.svg)](https://core.cicirello.org/api/) [![CodeQL](https://github.com/cicirello/core/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/cicirello/core/actions/workflows/codeql-analysis.yml) | 
| __License__ | [![GitHub](https://img.shields.io/github/license/cicirello/core)](https://github.com/cicirello/core/blob/main/LICENSE) | 

## Overview

The Java module org.cicirello.core provides some of the core utilities and data structures used in 
several of our other libraries and projects, including but not limited 
to [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa) 
and [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools), 
as well as various applications that use those libraries.

## Java 11+

We currently support Java 11+. Our development process utilizes OpenJDK 11, and all
jar files released via Maven Central, GitHub Packages, and GitHub Releases are built
with a Java 11 target. 

The initial release consists entirely of source code that can be compiled for a 
Java 8 target (e.g., if you modify the "pom.xml" and remove the 
"module-info.java"). However, we do not guarantee that this will remain true, as we have 
decided upon Java 11 as our minimum supported Java version to enable utilizing newer
language features in our ongoing development efforts.

## Versioning Scheme

We use [Semantic Versioning](https://semver.org/) with 
version numbers of the form: MAJOR.MINOR.PATCH, where differences 
in MAJOR correspond to incompatible API changes, differences in MINOR 
correspond to introduction of backwards compatible new functionality, 
and PATCH corresponds to backwards compatible bug fixes.

## Building the Library (with Maven)

The org.cicirello.core module is built using Maven. The root of the
repository contains a Maven `pom.xml`.  To build the library, 
execute `mvn package` at the root of the repository, which
will compile all classes, run all tests, run javadoc, and generate 
jar files of the library, the sources, and the javadocs. The file names
make this distinction explicit.  All build outputs will then
be found in the directory `target`.

To include generation of a code coverage report during the build,
execute `mvn package -Pcoverage` at the root of the repository to 
enable a Maven profile that executes JaCoCo during the test phase.

## Examples

Some of our other projects use this module. You may
consult the source code of [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools)
and/or [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa) for code
examples. 

## Java Modules

This library provides a Java module, `org.cicirello.core`. To use in your project,
add the following to your `module-info.java`:

```Java
module your.module.name.here {
	requires org.cicirello.core;
}
```

## Importing the Library from Maven Central

Add this to the dependencies section of your pom.xml, replacing 
the version number with the version that you want to use.

```XML
<dependency>
  <groupId>org.cicirello</groupId>
  <artifactId>core</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Importing the Library from GitHub Packages

If you'd prefer to import from GitHub Packages, rather than Maven Central, 
then: (1) add the dependency as indicated in previous section above, and (2) add 
the following to the repositories section of your pom.xml:

```XML
<repository>
  <id>github</id>
  <name>GitHub cicirello Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/cicirello/core</url>
  <releases><enabled>true</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

## Downloading Jar Files

If you don't use a dependency manager that supports importing from Maven Central,
or if you simply prefer to download manually, prebuilt jars are also attached to 
each [GitHub Release](https://github.com/cicirello/core/releases).

## License

The org.cicirello.core module is licensed under 
the [GNU General Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).

## Contribute

If you would like to contribute in any way, such 
as reporting bugs, suggesting new functionality, or code contributions 
such as bug fixes or implementations of new functionality, then start 
by reading the [contribution guidelines](https://github.com/cicirello/.github/blob/main/CONTRIBUTING.md).
This project has adopted 
the [Contributor Covenant Code of Conduct](https://github.com/cicirello/.github/blob/main/CODE_OF_CONDUCT.md).

