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

import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;
import org.moeaframework.algorithm.jmetal.mocks.MockBinaryProblem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.uma.jmetal.solution.binarysolution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;

public class BinaryProblemAdapterTest {
	
	@Test
	public void testNumberOfBits() {
		MockBinaryProblem problem = new MockBinaryProblem();
		BinaryProblemAdapter adapter = new BinaryProblemAdapter(problem);
		
		Assert.assertEquals(1, adapter.numberOfVariables());
		Assert.assertEquals(10, adapter.bitsFromVariable(0));
		Assert.assertEquals(10, adapter.totalNumberOfBits());
		Assert.assertEquals(10, adapter.getNumberOfMutationIndices());
	}
	
	@Test
	public void testConvert() {
		MockBinaryProblem problem = new MockBinaryProblem();
		BinaryProblemAdapter adapter = new BinaryProblemAdapter(problem);
		
		BinarySolution theirSolution = adapter.createSolution();
		Solution mySolution = adapter.convert(theirSolution);
		
		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			BinarySet theirBits = theirSolution.variables().get(0);
			BitSet myBits = BinaryVariable.getBitSet(mySolution.getVariable(i));
			
			Assert.assertEquals(theirBits, myBits);
		}
	}

}
