/* Copyright 2009-2024 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.algorithm.jmetal.adapters;

import java.util.List;
import java.util.stream.IntStream;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.Problem;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.bounds.Bounds;

/**
 * Converts a problem with real-valued decision variables into JMetal's DoubleProblem.
 */
public class DoubleProblemAdapter extends ProblemAdapter<DoubleSolution> implements DoubleProblem {

	private static final long serialVersionUID = 4011361659496044697L;

	/**
	 * Creates a new real-valued problem adapter.
	 * 
	 * @param problem the problem
	 */
	public DoubleProblemAdapter(Problem problem) {
		super(problem);
	}
	
	public double getLowerBound(int index) {
		return ((RealVariable)schema.getVariable(index)).getLowerBound();
	}

	public double getUpperBound(int index) {
		return ((RealVariable)schema.getVariable(index)).getUpperBound();
	}

	@Override
	public DoubleSolution createSolution() {
		return new DefaultDoubleSolution(variableBounds(), numberOfObjectives(), numberOfConstraints());
	}
	
	@Override
	public Solution convert(DoubleSolution solution) {
		Solution result = problem.newSolution();
		
		for (int i = 0; i < numberOfVariables(); i++) {
			RealVariable.setReal(result.getVariable(i), solution.variables().get(i));
		}
		
		return result;
	}

	@Override
	public List<Bounds<Double>> variableBounds() {
		return IntStream.range(0, problem.getNumberOfVariables()).mapToObj(
				i -> Bounds.create(getLowerBound(i), getUpperBound(i))).toList();
	}

}
