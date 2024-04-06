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
package org.moeaframework.problem.jmetal;

import java.util.BitSet;
import java.util.List;
import java.util.function.Supplier;

import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.RegisteredProblemProvider;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;
import org.uma.jmetal.problem.binaryproblem.BinaryProblem;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.Binh2;
import org.uma.jmetal.problem.multiobjective.Fonseca;
import org.uma.jmetal.problem.multiobjective.Kursawe;
import org.uma.jmetal.problem.multiobjective.Osyczka2;
import org.uma.jmetal.problem.multiobjective.Schaffer;
import org.uma.jmetal.problem.multiobjective.Srinivas;
import org.uma.jmetal.problem.multiobjective.Tanaka;
import org.uma.jmetal.problem.multiobjective.Viennet2;
import org.uma.jmetal.problem.multiobjective.Viennet3;
import org.uma.jmetal.problem.multiobjective.Viennet4;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ5;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ6;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ7;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F1;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F2;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F3;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F4;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F5;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F6;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F7;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F8;
import org.uma.jmetal.problem.multiobjective.lz09.LZ09F9;
import org.uma.jmetal.problem.multiobjective.uf.UF1;
import org.uma.jmetal.problem.multiobjective.uf.UF10;
import org.uma.jmetal.problem.multiobjective.uf.UF2;
import org.uma.jmetal.problem.multiobjective.uf.UF3;
import org.uma.jmetal.problem.multiobjective.uf.UF4;
import org.uma.jmetal.problem.multiobjective.uf.UF5;
import org.uma.jmetal.problem.multiobjective.uf.UF6;
import org.uma.jmetal.problem.multiobjective.uf.UF7;
import org.uma.jmetal.problem.multiobjective.uf.UF8;
import org.uma.jmetal.problem.multiobjective.uf.UF9;
import org.uma.jmetal.problem.multiobjective.wfg.WFG1;
import org.uma.jmetal.problem.multiobjective.wfg.WFG2;
import org.uma.jmetal.problem.multiobjective.wfg.WFG3;
import org.uma.jmetal.problem.multiobjective.wfg.WFG4;
import org.uma.jmetal.problem.multiobjective.wfg.WFG5;
import org.uma.jmetal.problem.multiobjective.wfg.WFG6;
import org.uma.jmetal.problem.multiobjective.wfg.WFG7;
import org.uma.jmetal.problem.multiobjective.wfg.WFG8;
import org.uma.jmetal.problem.multiobjective.wfg.WFG9;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT1;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT2;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT3;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT4;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT5;
import org.uma.jmetal.problem.multiobjective.zdt.ZDT6;
import org.uma.jmetal.solution.binarysolution.BinarySolution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import org.uma.jmetal.util.bounds.Bounds;

public class JMetalProblems extends RegisteredProblemProvider {
	
	public JMetalProblems() {
		super();
		
		registerDouble("DTLZ1_2", () -> new DTLZ1(6, 2), "pf/DTLZ2.2D.pf");
		registerDouble("DTLZ1_3", () -> new DTLZ1(), "pf/DTLZ1.3D.pf");
		registerDouble("DTLZ2_2", () -> new DTLZ2(11, 2), "pf/DTLZ2.2D.pf");
		registerDouble("DTLZ2_3", () -> new DTLZ2(), "pf/DTLZ2.3D.pf");
		registerDouble("DTLZ3_2", () -> new DTLZ3(11, 2), "pf/DTLZ3.2D.pf");
		registerDouble("DTLZ3_3", () -> new DTLZ3(), "pf/DTLZ3.3D.pf");
		registerDouble("DTLZ4_2", () -> new DTLZ4(11, 2), "pf/DTLZ4.2D.pf");
		registerDouble("DTLZ4_3", () -> new DTLZ4(), "pf/DTLZ4.3D.pf");
		registerDouble("DTLZ5_2", () -> new DTLZ5(11, 2), null);
		registerDouble("DTLZ5_3", () -> new DTLZ5(), null);
		registerDouble("DTLZ6_2", () -> new DTLZ6(11, 2), null);
		registerDouble("DTLZ6_3", () -> new DTLZ6(), null);
		registerDouble("DTLZ7_2", () -> new DTLZ7(21, 2), "pf/DTLZ7.2D.pf");
		registerDouble("DTLZ7_3", () -> new DTLZ7(), "pf/DTLZ7.3D.pf");
		
		registerDouble("UF1", () -> new UF1(), "pf/UF1.dat");
		registerDouble("UF2", () -> new UF2(), "pf/UF2.dat");
		registerDouble("UF3", () -> new UF3(), "pf/UF3.dat");
		registerDouble("UF4", () -> new UF4(), "pf/UF4.dat");
		registerDouble("UF5", () -> new UF5(), "pf/UF5.dat");
		registerDouble("UF6", () -> new UF6(), "pf/UF6.dat");
		registerDouble("UF7", () -> new UF7(), "pf/UF7.dat");
		registerDouble("UF8", () -> new UF8(), "pf/UF8.dat");
		registerDouble("UF9", () -> new UF9(), "pf/UF9.dat");
		registerDouble("UF10", () -> new UF10(), "pf/UF10.dat");
		
		registerDouble("LZ1", () -> new LZ09F1(), "pf/LZ09_F1.pf");
		registerDouble("LZ2", () -> new LZ09F2(), "pf/LZ09_F2.pf");
		registerDouble("LZ3", () -> new LZ09F3(), "pf/LZ09_F3.pf");
		registerDouble("LZ4", () -> new LZ09F4(), "pf/LZ09_F4.pf");
		registerDouble("LZ5", () -> new LZ09F5(), "pf/LZ09_F5.pf");
		registerDouble("LZ6", () -> new LZ09F6(), "pf/LZ09_F6.pf");
		registerDouble("LZ7", () -> new LZ09F7(), "pf/LZ09_F7.pf");
		registerDouble("LZ8", () -> new LZ09F8(), "pf/LZ09_F8.pf");
		registerDouble("LZ9", () -> new LZ09F9(), "pf/LZ09_F9.pf");
		
		registerDouble("WFG1_2", () -> new WFG1(1, 10, 2), "pf/WFG1.2D.pf");
		registerDouble("WFG1_3", () -> new WFG1(2, 10, 3), "pf/WFG1.3D.pf");
		registerDouble("WFG2_2", () -> new WFG2(1, 10, 2), "pf/WFG2.2D.pf");
		registerDouble("WFG2_3", () -> new WFG2(2, 10, 3), "pf/WFG2.3D.pf");
		registerDouble("WFG3_2", () -> new WFG3(1, 10, 2), "pf/WFG3.2D.pf");
		registerDouble("WFG3_3", () -> new WFG3(2, 10, 3), "pf/WFG3.3D.pf");
		registerDouble("WFG4_2", () -> new WFG4(1, 10, 2), "pf/WFG4.2D.pf");
		registerDouble("WFG4_3", () -> new WFG4(2, 10, 3), "pf/WFG4.3D.pf");
		registerDouble("WFG5_2", () -> new WFG5(1, 10, 2), "pf/WFG5.2D.pf");
		registerDouble("WFG5_3", () -> new WFG5(2, 10, 3), "pf/WFG5.3D.pf");
		registerDouble("WFG6_2", () -> new WFG6(1, 10, 2), "pf/WFG6.2D.pf");
		registerDouble("WFG6_3", () -> new WFG6(2, 10, 3), "pf/WFG6.3D.pf");
		registerDouble("WFG7_2", () -> new WFG7(1, 10, 2), "pf/WFG7.2D.pf");
		registerDouble("WFG7_3", () -> new WFG7(2, 10, 3), "pf/WFG7.3D.pf");
		registerDouble("WFG8_2", () -> new WFG8(1, 10, 2), "pf/WFG8.2D.pf");
		registerDouble("WFG8_3", () -> new WFG8(2, 10, 3), "pf/WFG8.3D.pf");
		registerDouble("WFG9_2", () -> new WFG9(1, 10, 2), "pf/WFG9.2D.pf");
		registerDouble("WFG9_3", () -> new WFG9(2, 10, 3), "pf/WFG9.3D.pf");
		
		registerDouble("ZDT1", () -> new ZDT1(), "pf/ZDT1.pf");
		registerDouble("ZDT2", () -> new ZDT2(), "pf/ZDT2.pf");
		registerDouble("ZDT3", () -> new ZDT3(), "pf/ZDT3.pf");
		registerDouble("ZDT4", () -> new ZDT4(), "pf/ZDT4.pf");
		registerBinary("ZDT5", () -> new ZDT5(), "pf/ZDT5.pf");
		registerDouble("ZDT6", () -> new ZDT6(), "pf/ZDT6.pf");
		
		registerDouble("Binh2", () -> new Binh2(), "pf/Binh2.pf");
		registerDouble("Fonseca2", () -> new Fonseca(), "pf/Fonseca2.pf");
		registerDouble("Kursawe", () -> new Kursawe(), "pf/Kursawe.pf");
		registerDouble("Osyczka2", () -> new Osyczka2(), "pf/Osyczka2.pf");
		registerDouble("Schaffer", () -> new Schaffer(), "pf/Schaffer.pf");
		registerDouble("Srinivas", () -> new Srinivas(), "pf/Srinivas.pf");
		registerDouble("Tanaka", () -> new Tanaka(), "pf/Tanaka.pf");
		registerDouble("Viennet2", () -> new Viennet2(), "pf/Viennet2.pf");
		registerDouble("Viennet3", () -> new Viennet3(), "pf/Viennet3.pf");
		registerDouble("Viennet4", () -> new Viennet4(), "pf/Viennet4.pf");
	}
	
	protected final void registerDouble(String name, Supplier<DoubleProblem> constructor, String referenceSet) {
		register(name + "-JMetal", () -> new DoubleProblemWrapper(name, constructor.get()), referenceSet);
	}
	
	protected final void registerBinary(String name, Supplier<BinaryProblem> constructor, String referenceSet) {
		register(name + "-JMetal", () -> new BinaryProblemWrapper(name, constructor.get()), referenceSet);
	}
	
	private abstract class FrameworkProblemAdapter<T extends org.uma.jmetal.problem.Problem<S>,
	S extends org.uma.jmetal.solution.Solution<?>> extends AbstractProblem {
		
		protected final String name;
		
		protected final T innerProblem;
		
		public FrameworkProblemAdapter(String name, T innerProblem) {
			super(innerProblem.numberOfVariables(), innerProblem.numberOfObjectives(), innerProblem.numberOfConstraints());
			this.name = name;
			this.innerProblem = innerProblem;
		}

		@Override
		public String getName() {
			return name;
		}
		
		public abstract void convert(Solution solution, S otherSolution);
		
		public abstract void initVariables(Solution solution);

		@Override
		public void evaluate(Solution solution) {
			S innerSolution = innerProblem.createSolution();
			convert(solution, innerSolution);
			
			innerProblem.evaluate(innerSolution);
			
			solution.setObjectives(innerSolution.objectives());
			solution.setConstraints(innerSolution.constraints());
			
			// JMetal only recognizes constraints < 0 as infeasible whereas MOEA Framework treats any non-zero value
			// as infeasible
			for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
				if (solution.getConstraint(i) > 0.0) {
					solution.setConstraint(i, 0.0);
				}
			}
		}
		
		public Solution newSolution() {
			Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());
			initVariables(solution);
			return solution;
		}

		@Override
		public void close() {
			// do nothing
		}
		
	}
	
	private class DoubleProblemWrapper extends FrameworkProblemAdapter<DoubleProblem, DoubleSolution> {

		public DoubleProblemWrapper(String name, DoubleProblem innerProblem) {
			super(name, innerProblem);
		}

		@Override
		public void convert(Solution solution, DoubleSolution otherSolution) {
			for (int i = 0; i < solution.getNumberOfVariables(); i++) {
				otherSolution.variables().set(i, EncodingUtils.getReal(solution.getVariable(i)));
			}
		}
		
		@Override
		public void initVariables(Solution solution) {
			List<Bounds<Double>> bounds = innerProblem.variableBounds();
			
			for (int i = 0; i < getNumberOfVariables(); i++) {
				solution.setVariable(i, EncodingUtils.newReal(bounds.get(i).getLowerBound(), bounds.get(i).getUpperBound()));
			}
		}
		
	}
	
	private class BinaryProblemWrapper extends FrameworkProblemAdapter<BinaryProblem, BinarySolution> {

		public BinaryProblemWrapper(String name, BinaryProblem innerProblem) {
			super(name, innerProblem);
		}
		
		@Override
		public void convert(Solution solution, BinarySolution otherSolution) {
			for (int i = 0; i < getNumberOfVariables(); i++) {
				BitSet bits = EncodingUtils.getBitSet(solution.getVariable(i));
				BinarySet binarySet = new BinarySet(bits.length());
				
				for (int j = 0; j < bits.length(); j++) {
					binarySet.set(j, bits.get(j));
				}
				
				otherSolution.variables().set(i, binarySet);
			}
		}
		
		@Override
		public void initVariables(Solution solution) {
			for (int i = 0; i < getNumberOfVariables(); i++) {
				solution.setVariable(i, EncodingUtils.newBinary(innerProblem.numberOfBitsPerVariable().get(i)));
			}
		}
		
	}

}
