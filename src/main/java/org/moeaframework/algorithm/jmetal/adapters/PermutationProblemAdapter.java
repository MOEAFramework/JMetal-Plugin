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

import java.util.List;

import org.moeaframework.core.FrameworkException;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.Permutation;
import org.uma.jmetal.problem.permutationproblem.PermutationProblem;
import org.uma.jmetal.solution.permutationsolution.PermutationSolution;
import org.uma.jmetal.solution.permutationsolution.impl.IntegerPermutationSolution;

/**
 * Converts a problem with a single permutation decision variable into JMetal's PermutationProblem.
 */
public class PermutationProblemAdapter extends ProblemAdapter<PermutationSolution<Integer>>
implements PermutationProblem<PermutationSolution<Integer>> {

	private static final long serialVersionUID = -7658974412222795821L;
	
	/**
	 * Creates a new permutation problem adapter.
	 * 
	 * @param problem the problem
	 */
	public PermutationProblemAdapter(Problem problem) {
		super(problem);
		
		if (schema.getNumberOfVariables() != 1) {
			throw new FrameworkException("PermutationProblemAdapter only works with a single Permutation variable");
		}
	}
	
	@Override
	public PermutationSolution<Integer> createSolution() {
		return new IntegerPermutationSolution(length(), numberOfObjectives(), numberOfConstraints());
	}
	
	@Override
	public Solution convert(PermutationSolution<Integer> solution) {
		Solution result = getProblem().newSolution();
		List<Integer> permutationList = solution.variables();
		int[] permutation = new int[permutationList.size()];
		
		for (int i = 0; i < permutationList.size(); i++) {
			permutation[i] = permutationList.get(i);
		}
		
		EncodingUtils.setPermutation(result.getVariable(0), permutation);
		return result;
	}
	
	@Override
	public int numberOfVariables() {
		return length();
	}
	
	@Override
	public int length() {
		return ((Permutation)schema.getVariable(0)).size();
	}
	
	@Override
	public int getNumberOfMutationIndices() {
		return 1;
	}

}
