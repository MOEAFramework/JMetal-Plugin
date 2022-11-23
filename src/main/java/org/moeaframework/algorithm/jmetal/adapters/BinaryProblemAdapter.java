/* Copyright 2009-2022 David Hadka
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

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.EncodingUtils;
import org.uma.jmetal.problem.binaryproblem.BinaryProblem;
import org.uma.jmetal.solution.binarysolution.BinarySolution;
import org.uma.jmetal.solution.binarysolution.impl.DefaultBinarySolution;

/**
 * Converts a problem with binary decision variables into JMetal's BinaryProblem.
 */
public class BinaryProblemAdapter extends ProblemAdapter<BinarySolution> implements BinaryProblem {

	private static final long serialVersionUID = -7944545872958727275L;
	
	/**
	 * The total number of bits across all binary variables.
	 */
	private final int totalNumberOfBits;
	
	/**
	 * Creates a new binary problem adapter
	 * 
	 * @param problem the problem
	 */
	public BinaryProblemAdapter(Problem problem) {
		super(problem);
		
		// count the total number of bits
		int numberOfBits = 0;
		
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			numberOfBits += ((BinaryVariable)schema.getVariable(i)).getNumberOfBits();
		}
		
		totalNumberOfBits = numberOfBits;
	}
	
	@Override
	public int getBitsFromVariable(int index) {
		return ((BinaryVariable)schema.getVariable(index)).getNumberOfBits();
	}
	
	@Override
	public int getTotalNumberOfBits() {
		return totalNumberOfBits;
	}
	
	@Override
	public List<Integer> getListOfBitsPerVariable() {
		return IntStream.range(0, getNumberOfVariables()).mapToObj(i -> getBitsFromVariable(i)).toList();
	}
	
	@Override
	public BinarySolution createSolution() {
		return new DefaultBinarySolution(getListOfBitsPerVariable(), getNumberOfObjectives(), getNumberOfConstraints());
	}
	
	@Override
	public Solution convert(BinarySolution solution) {
		Solution result = problem.newSolution();
		
		for (int i = 0; i < getNumberOfVariables(); i++) {
			EncodingUtils.setBitSet(result.getVariable(i), solution.variables().get(i));
		}
		
		return result;
	}
	
	@Override
	public int getNumberOfMutationIndices() {
		return getTotalNumberOfBits();
	}

}
