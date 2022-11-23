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
package org.moeaframework.algorithm.jmetal;

import org.junit.Assert;
import org.junit.Test;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Problem;
import org.moeaframework.problem.DTLZ.DTLZ2;
import org.moeaframework.problem.ZDT.ZDT5;
import org.moeaframework.util.TypedProperties;

/**
 * Tests the {@link JMetalAlgorithms} class to ensure the JMetal algorithms
 * can be constructed and used correctly.
 * 
 * Additional weight vector files can be found at:
 *   https://github.com/jMetal/jMetal/tree/jmetal-5.11/resources/weightVectorFiles
 */
public class JMetalAlgorithmsTest {
	
	@Test
	public void testPropertyInjection() {
		TypedProperties properties = new TypedProperties();
		properties.setInt("maxEvaluations", 10000);
		properties.setString("replacementStrategy", "WORST_IN_ARCHIVE");
		
		new JMetalAlgorithms().getAlgorithm("ESPEA", properties, new DTLZ2(2));
		
		Assert.assertTrue(properties.getUnaccessedProperties().size() == 0);
	}

	@Test
	public void testAbYSS() {
		runTest("AbYSS", new DTLZ2(2));
	}
	
	@Test
	public void testCDG() {
		runTest("CDG", new DTLZ2(2));
	}
	
	@Test
	public void testDMOPSO() {
		runTest("DMOPSO", new DTLZ2(2));
	}
	
	@Test
	public void testESPEA() {
		runTest("ESPEA", new DTLZ2(2));
	}
	
	@Test
	public void testFAME() {
		runTest("FAME", new DTLZ2(2));
	}
	
	@Test
	public void testGDE3() {
		runTest("GDE3", new DTLZ2(2));
	}
	
	@Test
	public void testGWASFGA() {
		runTest("GWASFGA", new DTLZ2(2));
	}
	
	@Test
	public void testIBEA() {
		runTest("IBEA", new DTLZ2(2));
	}
	
	@Test
	public void testMOCell() {
		runTest("MOCell", new DTLZ2(2));
	}
	
	@Test
	public void testMOCHC() {
		runTest("MOCHC", new ZDT5());
	}
	
	@Test
	public void testMOEAD() {
		runTest("MOEAD", new DTLZ2(2));
	}
	
	@Test
	public void testMOMBI() {
		TypedProperties properties = TypedProperties.withProperty("pathWeights",
				"resources/weightVectorFiles/mombi2/weight_02D_152.sld");
		
		runTest("MOMBI", properties, new DTLZ2(2));
	}
	
	@Test
	public void testMOMBI2() {
		TypedProperties properties = TypedProperties.withProperty("pathWeights",
				"resources/weightVectorFiles/mombi2/weight_02D_152.sld");
		
		runTest("MOMBI2", properties, new DTLZ2(2));
	}
	
	@Test
	public void testMOSA() {
		runTest("MOSA", new DTLZ2(2));
	}
	
	@Test
	public void testNSGAII() {
		runTest("NSGAII", new DTLZ2(2));
	}
	
	@Test
	public void testNSGAIII() {
		runTest("NSGAIII", new DTLZ2(2));
	}
	
	@Test
	public void testOMOPSO() {
		runTest("OMOPSO", new DTLZ2(2));
	}
	
	@Test
	public void testPAES() {
		runTest("PAES", new DTLZ2(2));
	}
	
	@Test
	public void testPESA2() {
		runTest("PESA2", new DTLZ2(2));
	}
	
	@Test
	public void testRNSGAII() {
		runTest("RNSGAII", new DTLZ2(2));
	}
	
	@Test
	public void testSMPSO() {
		runTest("SMPSO", new DTLZ2(2));
	}
	
	@Test
	public void testSPEA2() {
		runTest("SPEA2", new DTLZ2(2));
	}
	
	@Test
	public void testWASFGA() {
		runTest("WASFGA", new DTLZ2(2));
	}
	
	private void runTest(String algorithmName, TypedProperties properties, Problem problem) {
		properties.setInt("maxEvaluations", 500);
		
		Algorithm algorithm = new JMetalAlgorithms().getAlgorithm(algorithmName, properties, problem);
		Assert.assertNotNull(algorithm);
		
		algorithm.step();
		
		Assert.assertTrue(algorithm.isTerminated());
		Assert.assertTrue(algorithm.getResult().size() > 0);
		
		System.out.println(algorithmName + ": " + String.join(", ",
				properties.getAccessedProperties().stream().sorted().toList()));	
	}

	private void runTest(String algorithmName, Problem problem) {
		runTest(algorithmName, new TypedProperties(), problem);
	}
	
}
