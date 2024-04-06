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

import org.junit.Assert;
import org.junit.Test;
import org.moeaframework.algorithm.jmetal.mocks.MockPermutationProblem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.uma.jmetal.solution.permutationsolution.PermutationSolution;

public class PermutationProblemAdapterTest {
	
	@Test
	public void testNumberOfBits() {
		MockPermutationProblem problem = new MockPermutationProblem();
		PermutationProblemAdapter adapter = new PermutationProblemAdapter(problem);
		
		Assert.assertEquals(10, adapter.numberOfVariables());
		Assert.assertEquals(10, adapter.length());
		Assert.assertEquals(1, adapter.getNumberOfMutationIndices());
	}
	
	@Test
	public void testConvert() {
		MockPermutationProblem problem = new MockPermutationProblem();
		PermutationProblemAdapter adapter = new PermutationProblemAdapter(problem);
		
		PermutationSolution<Integer> theirSolution = adapter.createSolution();
		Solution mySolution = adapter.convert(theirSolution);
		
		List<Integer> theirPermutation = theirSolution.variables();
		int[] myPermutation = EncodingUtils.getPermutation(mySolution.getVariable(0));
		
		Assert.assertEquals(theirPermutation.size(), myPermutation.length);
		
		for (int i = 0; i < theirPermutation.size(); i++) {
			Assert.assertEquals((int)theirPermutation.get(i), myPermutation[i]);
		}
	}

}
