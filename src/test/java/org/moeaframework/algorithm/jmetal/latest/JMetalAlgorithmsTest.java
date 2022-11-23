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
package org.moeaframework.algorithm.jmetal.latest;

import org.junit.Assert;
import org.junit.Test;
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
		new JMetalAlgorithms().getAlgorithm("AbYSS", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testCDG() {
		new JMetalAlgorithms().getAlgorithm("CDG", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testDMOPSO() {
		new JMetalAlgorithms().getAlgorithm("DMOPSO", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testESPEA() {
		new JMetalAlgorithms().getAlgorithm("ESPEA", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testFAME() {
		new JMetalAlgorithms().getAlgorithm("FAME", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testGDE3() {
		new JMetalAlgorithms().getAlgorithm("GDE3", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testGWASFGA() {
		new JMetalAlgorithms().getAlgorithm("GWASFGA", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testIBEA() {
		new JMetalAlgorithms().getAlgorithm("IBEA", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testMOCell() {
		new JMetalAlgorithms().getAlgorithm("MOCell", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testMOCHC() {
		new JMetalAlgorithms().getAlgorithm("MOCHC", new TypedProperties(), new ZDT5());
	}
	
	@Test
	public void testMOEAD() {
		new JMetalAlgorithms().getAlgorithm("MOEAD", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testMOMBI() {
		TypedProperties properties = TypedProperties.withProperty("pathWeights", "resources/weightVectorFiles/mombi2/weight_02D_152.sld");
		new JMetalAlgorithms().getAlgorithm("MOMBI", properties, new DTLZ2(2));
	}
	
	@Test
	public void testMOMBI2() {
		TypedProperties properties = TypedProperties.withProperty("pathWeights", "resources/weightVectorFiles/mombi2/weight_02D_152.sld");
		new JMetalAlgorithms().getAlgorithm("MOMBI2", properties, new DTLZ2(2));
	}
	
	@Test
	public void testMOSA() {
		new JMetalAlgorithms().getAlgorithm("MOSA", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testNSGAII() {
		new JMetalAlgorithms().getAlgorithm("NSGAII", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testNSGAIII() {
		new JMetalAlgorithms().getAlgorithm("NSGAIII", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testOMOPSO() {
		new JMetalAlgorithms().getAlgorithm("OMOPSO", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testPAES() {
		new JMetalAlgorithms().getAlgorithm("PAES", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testPESA2() {
		new JMetalAlgorithms().getAlgorithm("PESA2", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testRNSGAII() {
		new JMetalAlgorithms().getAlgorithm("RNSGAII", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testSMPSO() {
		new JMetalAlgorithms().getAlgorithm("SMPSO", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testSPEA2() {
		new JMetalAlgorithms().getAlgorithm("SPEA2", new TypedProperties(), new DTLZ2(2));
	}
	
	@Test
	public void testWASFGA() {
		new JMetalAlgorithms().getAlgorithm("WASFGA", new TypedProperties(), new DTLZ2(2));
	}

}
