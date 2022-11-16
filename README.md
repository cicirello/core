# Module org.cicirello.core

<img src="https://core.cicirello.org/images/core.png" width="640" alt="org.cicirello.core - A Java library of core data structures and other miscellaneous utilities">

Copyright (C) 2019-2022 [Vincent A. Cicirello](https://www.cicirello.org/).

Website: [https://core.cicirello.org/](https://core.cicirello.org/)

API documentation: [https://core.cicirello.org/api](https://core.cicirello.org/api)

| __Packages and Releases__ | [![Maven Central](https://img.shields.io/maven-central/v/org.cicirello/core.svg?label=Maven%20Central&logo=apachemaven)](https://search.maven.org/artifact/org.cicirello/core) [![GitHub release (latest by date)](https://img.shields.io/github/v/release/cicirello/core?logo=GitHub)](https://github.com/cicirello/core/releases) [![JitPack](https://jitpack.io/v/org.cicirello/core.svg)](https://jitpack.io/#org.cicirello/core) |
| :--- | :--- |
| __Build Status__ | [![build](https://github.com/cicirello/core/workflows/build/badge.svg)](https://github.com/cicirello/core/actions/workflows/build.yml) [![docs](https://github.com/cicirello/core/workflows/docs/badge.svg)](https://core.cicirello.org/api/) [![CodeQL](https://github.com/cicirello/core/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/cicirello/core/actions/workflows/codeql-analysis.yml) |
| __JaCoCo Test Coverage__ | [![coverage](https://raw.githubusercontent.com/cicirello/core/badges/jacoco.svg)](https://github.com/cicirello/core/actions/workflows/build.yml) [![branches coverage](https://raw.githubusercontent.com/cicirello/core/badges/branches.svg)](https://github.com/cicirello/core/actions/workflows/build.yml) |
| __Security__ | [![Snyk security score](https://snyk-widget.herokuapp.com/badge/mvn/org.cicirello/core/badge.svg)](https://snyk.io/vuln/maven%3Aorg.cicirello%3Acore) [![Snyk Known Vulnerabilities](https://snyk.io/test/github/cicirello/core/badge.svg)](https://snyk.io/test/github/cicirello/core) |
| __Other Information__ | [![GitHub](https://img.shields.io/github/license/cicirello/core)](https://github.com/cicirello/core/blob/main/LICENSE) [![style](https://img.shields.io/badge/style-Google%20Java%20Style-informational)](https://google.github.io/styleguide/javaguide.html) |
| __Support__ | [![GitHub Sponsors](https://img.shields.io/badge/sponsor-30363D?logo=GitHub-Sponsors&logoColor=#EA4AAA)](https://github.com/sponsors/cicirello) [![Liberapay](https://img.shields.io/badge/Liberapay-F6C915?logo=liberapay&logoColor=black)](https://liberapay.com/cicirello) [![Ko-Fi](https://img.shields.io/badge/Ko--fi-F16061?logo=ko-fi&logoColor=white)](https://ko-fi.com/cicirello) |

## Overview

The Java module org.cicirello.core provides some of the core utilities and data structures used in 
several of our other libraries and projects, including but not limited 
to [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa) 
and [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools), 
as well as various applications that use those libraries.


## Java 17+

The org.cicirello.core currently supports Java 17+. See the following table for mapping between library
version and minimum supported Java version.

| version | Java requirements |
| --- | --- |
| 2.x.y | Java 17+ |
| 1.x.y | Java 11+ |


## Versioning Scheme

org.cicirello.core uses [Semantic Versioning](https://semver.org/) with 
version numbers of the form: MAJOR.MINOR.PATCH, where differences 
in MAJOR correspond to incompatible API changes, differences in MINOR 
correspond to introduction of backwards compatible new functionality, 
and PATCH corresponds to backwards compatible bug fixes.


## Examples

Some of our other projects use this module. You may
consult the source code of [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools)
and/or [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa) for code
examples. 


## Java Modules

This library provides a Java module, `org.cicirello.core`. If you are using in a project that uses the 
Java Platform Module System, then add the following to your `module-info.java`:

```Java
module your.module.name.here {
	requires org.cicirello.core;
}
```


## Importing from Package Repositories

Prebuilt artifacts are regularly published to Maven Central, GitHub Packages, and JitPack. In most
cases, you'll want to use Maven Central. JitPack may be useful if you want to build your project against
the latest unreleased version, essentially against the default branch of the repository, or a specific commit.
Releases are published to JitPack and GitHub Packages mainly as a fall-back in the unlikely scenario that
Maven Central is unavailable.

### Importing from Maven Central

Add this to the dependencies section of your pom.xml, replacing `x.y.z` with
the version number that you want to use.

```XML
<dependency>
  <groupId>org.cicirello</groupId>
  <artifactId>core</artifactId>
  <version>x.y.z</version>
</dependency>
```

### Importing from GitHub Packages

If you'd prefer to import from GitHub Packages, rather than Maven Central, 
then: (1) add the dependency as indicated in previous section above, and (2) add 
the following to the repositories section of your pom.xml:

```XML
<repository>
  <id>github</id>
  <name>GitHub cicirello Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/cicirello/core</url>
</repository>
```

Note that GitHub Packages requires authenticating to GitHub.

### Importing from JitPack

You can also import from JitPack. As above, you need to first add JitPack to
the repositories section of your pom.xml, such as:

```XML
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

JitPack works a bit differently than Maven Central. Specifically, JitPack builds
artifacts on-demand from the GitHub repository the first time a version is requested. We have
configured our domain on JitPack for the groupId, so you can still specify the dependency 
as (just replace `x.y.z` with the version that you want):

```XML
<dependency>
  <groupId>org.cicirello</groupId>
  <artifactId>core</artifactId>
  <version>x.y.z</version>
</dependency>
```

We have primarily configured JitPack as a source of SNAPSHOT builds. If you want to build
your project against the latest commit, specify the dependency as:

```XML
<dependency>
  <groupId>org.cicirello</groupId>
  <artifactId>core</artifactId>
  <version>main-SNAPSHOT</version>
</dependency>
```

You can also build against a specific commit using the commit hash as the version.

### Downloading Jar Files

If you don't use a dependency manager that supports importing from Maven Central or other
package repositories, or if you simply prefer to download manually, prebuilt jars are also attached to 
each [GitHub Release](https://github.com/cicirello/core/releases).


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

