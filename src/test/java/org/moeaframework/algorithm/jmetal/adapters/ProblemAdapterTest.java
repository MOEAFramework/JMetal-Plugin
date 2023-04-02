/* Copyright 2009-2023 David Hadka
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

import org.junit.Assert;
import org.junit.Test;
import org.moeaframework.algorithm.jmetal.mocks.MockConstraintProblem;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Settings;
import org.moeaframework.core.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

/**
 * Tests the {@link ProblemAdapter} class.
 */
public class ProblemAdapterTest {
	
	private class TestProblemAdapter<T extends org.uma.jmetal.solution.Solution<?>> extends ProblemAdapter<T> {
		
		private static final long serialVersionUID = -4544075640184829372L;

		public TestProblemAdapter(Problem problem) {
			super(problem);
		}

		@Override
		public T createSolution() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Solution convert(T solution) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Test
	public void testDefaultMethods() {
		MockConstraintProblem problem = new MockConstraintProblem();
		TestProblemAdapter<DoubleSolution> adapter = new TestProblemAdapter<DoubleSolution>(problem);
		
		Assert.assertEquals(problem.getName(), adapter.name());
		Assert.assertEquals(problem.getNumberOfVariables(), adapter.numberOfVariables());
		Assert.assertEquals(problem.getNumberOfObjectives(), adapter.numberOfObjectives());
		Assert.assertEquals(problem.getNumberOfConstraints(), adapter.numberOfConstraints());
		Assert.assertEquals(problem.getNumberOfVariables(), adapter.getNumberOfMutationIndices());
	}
	
	@Test
	public void testEvaluate() {
		MockConstraintProblem problem = new MockConstraintProblem();
		ProblemAdapter<DoubleSolution> adapter = new DoubleProblemAdapter(problem);
		
		DoubleSolution solution = adapter.createSolution();
		adapter.evaluate(solution);
		
		Assert.assertEquals(problem.getNumberOfVariables(), solution.variables().size());
		Assert.assertEquals(problem.getNumberOfObjectives(), solution.objectives().length);
		Assert.assertEquals(5.0, solution.objectives()[0], Settings.EPS);
	}

}
