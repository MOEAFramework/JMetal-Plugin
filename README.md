# JMetal-Plugin

Extends the [MOEA Framework](http://github.com/MOEAFramework/MOEAFramework) with optimization algorithms implemented in JMetal.
If using any of the JMetal algorithms, please cite:

> Antonio J. Nebro, Juan J. Durillo, and Matthieu Vergne. 2015. Redesigning the jMetal Multi-Objective Optimization Framework. In Proceedings of the Companion Publication of the 2015 Annual Conference on Genetic and Evolutionary Computation (GECCO Companion '15). Association for Computing Machinery, New York, NY, USA, 1093–1100. https://doi.org/10.1145/2739482.2768462

## Installation

When using Maven, add the following dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>org.moeaframework</groupId>
    <artifactId>jmetal-plugin</artifactId>
    <version>6.6.0</version>
</dependency>
```

Otherwise, download and add the `.jar` file from the [Releases](https://github.com/MOEAFramework/JMetal-Plugin/releases) page
to the classpath, which is typically the `MOEAFramework/lib/` folder.  Note we also publish a `-jar-with-dependencies`
version that includes the appropriate version of JMetal.

## Supported Versions

We aim to support the latest version of JMetal, however, there may be some delay after a new release.  The `{major}.{minor}` version
number of this project will match the supported JMetal version.  Below is a complete list of supported versions:

JMetal Version | JMetal-Plugin Latest | Supported MOEA Framework Versions | Supported Java Versions
-------------- | -------------------- | --------------------------------- | -----------------------
**`6.6`**      | **`>= 6.6.0`**       | **`>= 4.3`**                      | **`>= 19`**
`6.2`         | `6.2.3`             | `>= 4.0`                         | `>= 17`
`6.2`         | `6.2.0`             | `3.6 - 3.11`                    | `>= 14`
`6.1`         | `6.1.0`             | `3.6 - 3.11`                    | `>= 14`
`6.0`         | `6.0.1`             | `3.5 - 3.11`                    | `>= 14`
`5.11`        | `5.11.2`            | `3.2 - 3.11`                    | `>= 14`

## Usage

Once this plugin is added as a Maven dependency, you can reference JMetal algorithms as you would any other:

```java

NondominatedPopulation result = new Executor()
		.withProblem("DTLZ2_3")
		.withAlgorithm("AbYSS")
		.withMaxEvaluations(10000)
		.run();
```

JMetal algorithms are configured by supplying properties to the `Executor`.  We do not provide
a listing or explanation of these properties, but an easy way to figure out what properties are available
is to run:

```java

TypedProperties properties = new TypedProperties();	
new JMetalAlgorithms().getAlgorithm("ESPEA", properties, new DTLZ2(2));
System.out.println(String.join(", ", properties.getAccessedProperties()));
```

which displays:

```
maxEvaluations, pm.distributionIndex, pm.rate, populationSize, replacementStrategy, sbx.distributionIndex, sbx.rate
```

## Limitations

Some functionality may not be available for JMetal algorithms.  This includes the ability to instrument algorithms
to collect runtime data.  JMetal algorithms only generate the end-of-run Pareto approximation set.

Some algorithms are implemented in both JMetal and the MOEA Framework.  If such cases, you can request the JMetal
version by appending `-JMetal` to the name, such as `NSGAII-JMetal`.

## License

Copyright 2009-2024 David Hadka and other contributors.  All rights reserved.

The MOEA Framework is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at your
option) any later version.

The MOEA Framework is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
