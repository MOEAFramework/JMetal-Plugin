# JMetal-Plugin

Extends the [MOEA Framework](http://github.com/MOEAFramework/MOEAFramework) with a number of optimization algorithms implemented in JMetal.

> Antonio J. Nebro, Juan J. Durillo, and Matthieu Vergne. 2015. Redesigning the jMetal Multi-Objective Optimization Framework. In Proceedings of the Companion Publication of the 2015 Annual Conference on Genetic and Evolutionary Computation (GECCO Companion '15). Association for Computing Machinery, New York, NY, USA, 1093–1100. https://doi.org/10.1145/2739482.2768462

## Installation

Add the following dependency to your `pom.xml`.  The version of this plugin matches the JMetal version number.

```xml

<dependency>
    <groupId>org.moeaframework</groupId>
    <artifactId>jmetal-plugin</artifactId>
    <version>5.11</version>
</dependency>
```

## Usage

With this plugin, you can reference any of the supported JMetal algorithms.

```java

NondominatedPopulation result = new Executor()
		.withProblem("DTLZ2_3")
		.withAlgorithm("AbYSS")
		.withMaxEvaluations(10000)
		.run();
```

## Limitations

Some functionality may not be available for JMetal algorithms.  This includes the ability to instrument algorithms
to collect runtime data.  JMetal algorithms only generate the end-of-run Pareto approximation set.

## License

Copyright 2009-2022 David Hadka and other contributors.  All rights reserved.

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
