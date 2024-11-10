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

import java.util.ArrayList;
import java.util.List;

import org.moeaframework.algorithm.Algorithm;
import org.moeaframework.algorithm.AlgorithmException;
import org.moeaframework.algorithm.extension.Extension;
import org.moeaframework.algorithm.extension.Extensions;
import org.moeaframework.core.Solution;
import org.moeaframework.core.TypedProperties;
import org.moeaframework.core.population.NondominatedPopulation;
import org.moeaframework.problem.Problem;

/**
 * Adapter for JMetal algorithms. This allows JMetal algorithms to be used within the MOEA Framework as an
 * {@link Algorithm}.  There is one substantial difference: since JMetal does not distinguish iterations of the
 * algorithm, one "step" is the full execution of the algorithm.
 */
public class JMetalAlgorithmAdapter<T extends org.uma.jmetal.solution.Solution<?>> implements Algorithm {

	/**
	 * The JMetal algorithm.
	 */
	private final org.uma.jmetal.algorithm.Algorithm<List<T>> algorithm;

	/**
	 * The JMetal problem adapter.
	 */
	private final ProblemAdapter<T> problem;
	
	/**
	 * The max evaluations the algorithm is run.
	 */
	private final int maxEvaluations;
	
	private final Extensions extensions;

	/**
	 * The JMetal solution set.
	 */
	private List<T> solutionSet;

	/**
	 * Constructs an adapter for the specified JMetal algorithm.
	 * 
	 * @param algorithm the JMetal algorithm
	 * @param properties the properties used to configure this algorithm
	 * @param problem the problem adapter
	 */
	public JMetalAlgorithmAdapter(
			org.uma.jmetal.algorithm.Algorithm<List<T>> algorithm,
			TypedProperties properties,
			ProblemAdapter<T> problem) {
		super();
		this.algorithm = algorithm;
		this.maxEvaluations = properties.getTruncatedInt("maxEvaluations");
		this.problem = problem;
		this.extensions = new JMetalExtensions(this);
	}
	
	@Override
	public String getName() {
		return algorithm.name();
	}

	@Override
	public void evaluate(Solution solution) {
		problem.getProblem().evaluate(solution);
	}

	@Override
	public int getNumberOfEvaluations() {
		if (solutionSet == null) {
			return 0;
		} else {
			return maxEvaluations;
		}
	}

	@Override
	public Problem getProblem() {
		return problem.getProblem();
	}

	@Override
	public NondominatedPopulation getResult() {
		NondominatedPopulation result = new NondominatedPopulation();

		if (solutionSet != null) {
			for (int i = 0; i < solutionSet.size(); i++) {
				Solution solution = problem.convert(solutionSet.get(i));
				solution.setObjectiveValues(solutionSet.get(i).objectives());
				solution.setConstraintValues(solutionSet.get(i).constraints());
				result.add(solution);
			}
		}

		return result;
	}
	
	@Override
	public void initialize() {
		// do nothing
	}
	
	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void step() {
		if (solutionSet == null) {
			try {
				algorithm.run();
				solutionSet = algorithm.result();
			} catch (Exception e) {
				throw new AlgorithmException(this, e);
			}
		}
	}

	@Override
	public boolean isTerminated() {
		return solutionSet != null;
	}

	@Override
	public void terminate() {
		if (solutionSet == null) {
			solutionSet = new ArrayList<T>();
		}
	}

	@Override
	public Extensions getExtensions() {
		return extensions;
	}
	
	private static class JMetalExtensions extends Extensions {

		public JMetalExtensions(Algorithm algorithm) {
			super(algorithm);
		}

		@Override
		public void add(Extension extension) {
			throw new UnsupportedOperationException("JMetal does not support extensions");
		}
		
	}

}
