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
package org.moeaframework.algorithm.jmetal.mocks;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

/**
 * Mock class for a problem with constraint violations.
 */
public class MockConstraintProblem extends AbstractProblem {

	public MockConstraintProblem() {
		super(1, 1, 3);
	}

	@Override
	public void evaluate(Solution solution) {
		solution.setObjectiveValue(0, 5.0);
		solution.setConstraintValue(0, -15.0);
		solution.setConstraintValue(1, 0.0);
		solution.setConstraintValue(2, 20.0);
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, 1, 3);
		solution.setVariable(0, new RealVariable(0.0, 1.0));
		return solution;
	}
	
}
