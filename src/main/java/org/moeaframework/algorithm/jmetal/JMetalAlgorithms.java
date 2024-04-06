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
package org.moeaframework.algorithm.jmetal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.text.WordUtils;
import org.moeaframework.algorithm.DefaultAlgorithms;
import org.moeaframework.algorithm.jmetal.adapters.BinaryProblemAdapter;
import org.moeaframework.algorithm.jmetal.adapters.DoubleProblemAdapter;
import org.moeaframework.algorithm.jmetal.adapters.JMetalAlgorithmAdapter;
import org.moeaframework.algorithm.jmetal.adapters.PermutationProblemAdapter;
import org.moeaframework.algorithm.jmetal.adapters.ProblemAdapter;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Settings;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.ProviderNotFoundException;
import org.moeaframework.core.spi.RegisteredAlgorithmProvider;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.ProblemException;
import org.moeaframework.util.TypedProperties;
import org.uma.jmetal.algorithm.AlgorithmBuilder;
import org.uma.jmetal.algorithm.multiobjective.abyss.ABYSSBuilder;
import org.uma.jmetal.algorithm.multiobjective.agemoea.AGEMOEABuilder;
import org.uma.jmetal.algorithm.multiobjective.agemoeaii.AGEMOEAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.cdg.CDGBuilder;
import org.uma.jmetal.algorithm.multiobjective.dmopso.DMOPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.espea.ESPEABuilder;
import org.uma.jmetal.algorithm.multiobjective.fame.FAME;
import org.uma.jmetal.algorithm.multiobjective.gde3.GDE3Builder;
import org.uma.jmetal.algorithm.multiobjective.gwasfga.GWASFGA;
import org.uma.jmetal.algorithm.multiobjective.ibea.IBEA;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.mochc.MOCHCBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.mombi.MOMBI;
import org.uma.jmetal.algorithm.multiobjective.mombi.MOMBI2;
import org.uma.jmetal.algorithm.multiobjective.mosa.MOSA;
import org.uma.jmetal.algorithm.multiobjective.mosa.cooling.impl.Exponential;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.omopso.OMOPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.paes.PAES;
import org.uma.jmetal.algorithm.multiobjective.pesa2.PESA2Builder;
import org.uma.jmetal.algorithm.multiobjective.rnsgaii.RNSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.algorithm.multiobjective.wasfga.WASFGA;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.crossover.impl.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.crossover.impl.HUXCrossover;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.BitFlipMutation;
import org.uma.jmetal.operator.mutation.impl.NonUniformMutation;
import org.uma.jmetal.operator.mutation.impl.UniformMutation;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.operator.selection.impl.RandomSelection;
import org.uma.jmetal.operator.selection.impl.RankingAndCrowdingSelection;
import org.uma.jmetal.operator.selection.impl.SpatialSpreadDeviationSelection;
import org.uma.jmetal.solution.binarysolution.BinarySolution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.archive.Archive;
import org.uma.jmetal.util.archive.BoundedArchive;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.archive.impl.GenericBoundedArchive;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.densityestimator.impl.CrowdingDistanceDensityEstimator;
import org.uma.jmetal.util.errorchecking.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

public class JMetalAlgorithms extends RegisteredAlgorithmProvider {
	
	private final boolean verbose;

	public JMetalAlgorithms() {
		this(Settings.isVerbose());
	}
	
	public JMetalAlgorithms(boolean verbose) {
		super();
		this.verbose = verbose;
		
		register(this::newAbYSS, "AbYSS", "AbYSS-JMetal");
		register(this::newAGEMOEA, "AGEMOEA", "AGE-MOEA", "AGEMOEA-JMetal", "AGE-MOEA-JMetal");
		register(this::newAGEMOEAII, "AGEMOEAII", "AGE-MOEA-II", "AGEMOEAII-JMetal", "AGE-MOEA-II-JMetal");
		register(this::newCDG, "CDG", "CDG-JMetal");
		register(this::newDMOPSO, "DMOPSO", "DMOPSO-JMetal");
		register(this::newESPEA, "ESPEA", "ESPEA-JMetal");
		register(this::newFAME, "FAME", "FAME-JMetal");
		register(this::newGDE3, "GDE3-JMetal");
		register(this::newGWASFGA, "GWASFGA", "GWASFGA-JMetal");
		register(this::newIBEA, "IBEA-JMetal");
		register(this::newMOCell, "MOCell", "MOCell-JMetal");
		register(this::newMOCHC, "MOCHC", "MOCHC-JMetal");
		register(this::newMOEAD, "MOEAD-JMetal");
		register(this::newMOMBI, "MOMBI", "MOMBI-JMetal");
		register(this::newMOMBI2, "MOMBI2", "MOMBI2-JMetal");
		register(this::newMOSA, "MOSA", "MOSA-JMetal");
		register(this::newNSGAII, "NSGAII-JMetal");
		register(this::newNSGAIII, "NSGAIII-JMetal");
		register(this::newOMOPSO, "OMOPSO-JMetal");
		register(this::newPAES, "PAES-JMetal");
		register(this::newPESA2, "PESA2-JMetal");
		register(this::newRNSGAII, "RNSGAII", "RNSGAII-JMetal");
		register(this::newSMPSO, "SMPSO-JMetal");
		register(this::newSMSEMOA, "SMSEMOA-JMetal");
		register(this::newSPEA2, "SPEA2-JMetal");
		register(this::newWASFGA, "WASFGA", "WASFGA-JMetal");
	}

	@Override
	public Algorithm getAlgorithm(String name, TypedProperties properties,Problem problem) {
		try  {
			return super.getAlgorithm(name, properties, problem);
		} catch (JMetalException e) {
			throw new ProviderNotFoundException(name, e);
		}
	}
	
	/**
	 * Converts a MOEA Framework problem into an appropriately typed JMetal problem.
	 * 
	 * @param problem the MOEA Framework problem
	 * @return the JMetal problem
	 */
	private ProblemAdapter<? extends org.uma.jmetal.solution.Solution<?>> createProblemAdapter(Problem problem) {
		Set<Class<?>> types = new HashSet<Class<?>>();
		Solution schema = problem.newSolution();
		
		for (int i=0; i<schema.getNumberOfVariables(); i++) {
			types.add(schema.getVariable(i).getClass());
		}
		
		if (types.isEmpty()) {
			throw new ProblemException(problem, "Problem has no defined types");
		}
		
		if (types.size() > 1) {
			throw new ProblemException(problem, "Problem has multiple types defined, expected only one: " +
					Arrays.toString(types.toArray()));
		}
		
		Class<?> type = types.iterator().next();

		if (RealVariable.class.isAssignableFrom(type)) {
			return new DoubleProblemAdapter(problem);
		} else if (BinaryVariable.class.isAssignableFrom(type)) {
			return new BinaryProblemAdapter(problem);
		} else if (Permutation.class.isAssignableFrom(type)) {
			return new PermutationProblemAdapter(problem);
		} else {
			throw new ProblemException(problem, "Problems with type " + type.getSimpleName() + 
					" are not currently supported by JMetal");
		}
	}
	
	private DoubleProblemAdapter createDoubleProblemAdapter(Problem problem) {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		
		if (!(adapter instanceof DoubleProblemAdapter)) {
			throw new JMetalException("algorithm only supports problems with real decision variables");
		}
		
		return (DoubleProblemAdapter)adapter;
	}
	
	private BinaryProblemAdapter createBinaryProblemAdapter(Problem problem) {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		
		if (!(adapter instanceof BinaryProblemAdapter)) {
			throw new JMetalException("algorithm only supports problems with binary decision variables");
		}
		
		return (BinaryProblemAdapter)adapter;
	}
	
	/**
	 * Uses reflection hackery to simplify setting up parameters using the bean-style design of
	 * JMetal's builders.  If the builder has a setter {@code setX} that takes a single argument
	 * of a supported type (int, double, or enum), we check if that property is defined and, if so,
	 * pass it to the setter.
	 * 
	 * The builders also have setters for operators.  These must still be set up explicitly.  There is
	 * unfortunately some inconsistency in the design of JMetal's builders.  Some take arguments in the
	 * constructor instead of using setters, some builders provide defaults whereas others do not,
	 * and some algorithms don't have builders and we must call their constructor instead.
	 * 
	 * @param properties the given properties
	 * @param builder the JMetal builder
	 */
	private void loadProperties(TypedProperties properties, AlgorithmBuilder<?> builder) {
		Class<?> type = builder.getClass();
		
		if (verbose) {
			System.out.println("Configuring " + type.getSimpleName());
		}
		
		for (Method method : type.getMethods()) {
			if (method.canAccess(builder) && 
					method.getName().startsWith("set") &&
					method.getParameterCount() == 1) {
				String methodName = method.getName();
				String property = WordUtils.uncapitalize(methodName.substring(3));
				Class<?> propertyType = method.getParameterTypes()[0];
				
				try {
					if (TypeUtils.isAssignable(propertyType, int.class) && properties.contains(property)) {
						int value = properties.getInt(property, -1);
						MethodUtils.invokeMethod(builder, methodName, value);
						
						if (verbose) {
							System.out.println("  > Setting property '" + property + "' to " + value);
						}
					} else if (TypeUtils.isAssignable(propertyType, double.class) && properties.contains(property)) {
						double value = properties.getDouble(property, -1);
						MethodUtils.invokeMethod(builder, methodName, value);
						
						if (verbose) {
							System.out.println("  > Setting property '" + property + "' to " + value);
						}
					} else if (propertyType.isEnum() && properties.contains(property)) {
						String value = properties.getString(property, null);
						MethodUtils.invokeStaticMethod(propertyType, "valueOf", value);
						
						if (verbose) {
							System.out.println("  > Setting property '" + property + "' to '" + value + "'");
						}
					} else if (property.equals("maxIterations")) {
					    int value = DefaultAlgorithms.getMaxIterations(properties);
					    MethodUtils.invokeMethod(builder, methodName, value);
					    
					    if (verbose) {
						    System.out.println("  > Setting property '" + property + "' to " + value);
					    }
					}
				} catch (Exception e) {
					System.err.println("Failed to set property " + property);
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newAGEMOEA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

	    AGEMOEABuilder builder = new AGEMOEABuilder(adapter)
	    		.setCrossoverOperator(crossover)
	    		.setMutationOperator(mutation);
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newAGEMOEAII(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

	    AGEMOEAIIBuilder builder = new AGEMOEAIIBuilder(adapter)
	    		.setCrossoverOperator(crossover)
	    		.setMutationOperator(mutation);
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newAbYSS(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

		Archive<DoubleSolution> archive = new CrowdingDistanceArchive<DoubleSolution>(
				(int)properties.getDouble("archiveSize", 100));
		
	    ABYSSBuilder builder = new ABYSSBuilder(adapter, archive)
	    		.setCrossoverOperator((CrossoverOperator<DoubleSolution>)crossover)
	    	    .setMutationOperator((MutationOperator<DoubleSolution>)mutation);
	    loadProperties(properties, builder);
	        
	    return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newCDG(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		DifferentialEvolutionCrossover crossover = JMetalFactory.getInstance().createDifferentialEvolution(adapter, properties);
		
		CDGBuilder builder = new CDGBuilder(adapter).setCrossover(crossover);
		loadProperties(properties, builder);
	            
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newDMOPSO(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);

		DMOPSOBuilder builder = new DMOPSOBuilder(adapter);
		loadProperties(properties, builder);
	            
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newESPEA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

	    ESPEABuilder builder = new ESPEABuilder(adapter, crossover, mutation);
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newFAME(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);

		FAME algorithm = new FAME(adapter,	
				(int)properties.getDouble("populationSize", 100),
				(int)properties.getDouble("archiveSize", 100),
				(int)properties.getDouble("maxEvaluations", 25000),
				new SpatialSpreadDeviationSelection<>(properties.getInt("numberOfTournaments", 5)),
				new SequentialSolutionListEvaluator<>());

		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newGDE3(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		DifferentialEvolutionCrossover crossover = JMetalFactory.getInstance().createDifferentialEvolution(adapter, properties);
				
		GDE3Builder builder = new GDE3Builder(adapter).setCrossover(crossover);
		loadProperties(properties, builder);
		
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newGWASFGA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    SelectionOperator selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	    
		GWASFGA algorithm = new GWASFGA(adapter,	
				(int)properties.getDouble("populationSize", 100),
				DefaultAlgorithms.getMaxIterations(properties),
				crossover,
				mutation,
				selection,
				new SequentialSolutionListEvaluator<DoubleSolution>(),
				properties.getDouble("epsilon", 0.01));

		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newIBEA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    SelectionOperator selection = new BinaryTournamentSelection();
	    
		IBEA algorithm = new IBEA(adapter,	
				(int)properties.getDouble("populationSize", 100),
				(int)properties.getDouble("archiveSize", 100),
				(int)properties.getDouble("maxEvaluations", 25000),
				selection,
				crossover,
				mutation);
		
		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newMOCell(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		
		if (properties.contains("feedback")) {
			System.err.println("Warning: Parameter 'feedback' is no longer supported in MOCell (JMetal)");
		}
		
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		BoundedArchive archive = new CrowdingDistanceArchive((int)properties.getDouble("archiveSize", 100));
		
		MOCellBuilder builder = new MOCellBuilder(adapter, crossover, mutation).setArchive(archive);
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newMOCHC(TypedProperties properties, Problem problem) throws JMetalException {
		BinaryProblemAdapter adapter = createBinaryProblemAdapter(problem);
		
		HUXCrossover crossover = new HUXCrossover(properties.getDouble("hux.rate", 1.0));
	    BitFlipMutation mutation = new BitFlipMutation(properties.getDouble("bf.rate", 0.35));
	    
	    SelectionOperator parentSelection = new RandomSelection<BinarySolution>();
	    SelectionOperator newGenerationSelection = new RankingAndCrowdingSelection<BinarySolution>(
	    		(int)properties.getDouble("populationSize", 100));

	    MOCHCBuilder builder = new MOCHCBuilder((BinaryProblemAdapter)adapter)
	            .setCrossover(crossover)
	            .setNewGenerationSelection(newGenerationSelection)
	            .setCataclysmicMutation(mutation)
	            .setParentSelection(parentSelection);
	    loadProperties(properties, builder);
	    
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newMOEAD(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		DifferentialEvolutionCrossover crossover = JMetalFactory.getInstance().createDifferentialEvolution(adapter, properties);
		MutationOperator mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
		MOEADBuilder.Variant variant = MOEADBuilder.Variant.valueOf(properties.getString("variant", "MOEAD"));		

		MOEADBuilder builder = new MOEADBuilder(adapter, variant).setCrossover(crossover).setMutation(mutation);
		loadProperties(properties, builder);
	    
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newMOMBI(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    SelectionOperator selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	
		if (!properties.contains("pathWeights")) {
			throw new JMetalException("must specify pathWeights file");
		}

		MOMBI algorithm = new MOMBI(adapter,
				DefaultAlgorithms.getMaxIterations(properties),
				crossover,
				mutation,
				selection,
				new SequentialSolutionListEvaluator<DoubleSolution>(),
				properties.getString("pathWeights", null));
		
		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newMOMBI2(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    SelectionOperator selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	    
		if (!properties.contains("pathWeights")) {
			throw new JMetalException("must specify pathWeights file");
		}

		MOMBI2 algorithm = new MOMBI2(adapter,
				DefaultAlgorithms.getMaxIterations(properties),
				crossover,
				mutation,
				selection,
				new SequentialSolutionListEvaluator<DoubleSolution>(),
				properties.getString("pathWeights", null));
		
		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newMOSA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter adapter = createProblemAdapter(problem);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    
	    BoundedArchive<DoubleSolution> archive = new GenericBoundedArchive<>(
	    		properties.getInt("archiveSize", 100),
	    		new CrowdingDistanceDensityEstimator<>());
	    
	    org.uma.jmetal.solution.Solution initialSolution = (org.uma.jmetal.solution.Solution)adapter.createSolution();
	    adapter.evaluate(initialSolution);

	    MOSA algorithm = new MOSA(initialSolution,
	    		adapter,
	    		(int)properties.getDouble("maxEvaluations", 25000),
	    		archive,
	    		mutation,
	    		1.0,
	    		new Exponential(0.95));

		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newNSGAII(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
		NSGAIIBuilder builder = new NSGAIIBuilder(adapter, crossover, mutation,
				(int)properties.getDouble("populationSize", 100));
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newNSGAIII(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter adapter = createProblemAdapter(problem);
		
		CrossoverOperator crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		SelectionOperator selection = new BinaryTournamentSelection();

		NSGAIIIBuilder builder = new NSGAIIIBuilder(adapter)
				.setCrossoverOperator(crossover)
				.setMutationOperator(mutation)
				.setSelectionOperator(selection);
		loadProperties(properties, builder);

		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newOMOPSO(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		
		int maxIterations = DefaultAlgorithms.getMaxIterations(properties);
		double mutationProbability = 1.0 / problem.getNumberOfVariables();
		
		UniformMutation uniformMutation = new UniformMutation(
				properties.getDouble("mutationProbability", mutationProbability),
				properties.getDouble("perturbationIndex", 0.5));
		
		NonUniformMutation nonUniformMutation = new NonUniformMutation(
				properties.getDouble("mutationProbability", mutationProbability),
				properties.getDouble("perturbationIndex", 0.5),
				maxIterations);
		
		SolutionListEvaluator evaluator = new SequentialSolutionListEvaluator();
		
		OMOPSOBuilder builder = new OMOPSOBuilder(adapter, evaluator)
				.setUniformMutation(uniformMutation)
        		.setNonUniformMutation(nonUniformMutation)
        		.setEta(properties.getDouble("epsilon", 0.0075));
		loadProperties(properties, builder);
		
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newPAES(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
		PAES paes = new PAES(adapter,
				(int)properties.getDouble("maxEvaluations", 25000),
				(int)properties.getDouble("archiveSize", 100),
				(int)properties.getDouble("bisections", 8),
				mutation);
		
		return new JMetalAlgorithmAdapter(paes, properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newPESA2(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
        PESA2Builder builder = new PESA2Builder(adapter, crossover, mutation);
        loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newRNSGAII(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
		double[] interestPoint = properties.getDoubleArray("interestPoint", new double[problem.getNumberOfObjectives()]);
		
		RNSGAIIBuilder builder = new RNSGAIIBuilder(adapter, crossover, mutation,
				DoubleStream.of(interestPoint).boxed().toList(),
				properties.getDouble("epsilon", 0.01));
		loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newSMPSO(TypedProperties properties, Problem problem) throws JMetalException {
		DoubleProblemAdapter adapter = createDoubleProblemAdapter(problem);
		MutationOperator<DoubleSolution> mutation = (MutationOperator<DoubleSolution>)
				JMetalFactory.getInstance().createMutationOperator(adapter, properties);
		
		BoundedArchive archive = new CrowdingDistanceArchive((int)properties.getDouble("archiveSize", 100));
		
		SMPSOBuilder builder = new SMPSOBuilder(adapter, archive).setMutation(mutation);
		loadProperties(properties, builder);
		
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newSPEA2(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

		SPEA2Builder builder = new SPEA2Builder(adapter, crossover, mutation);
		loadProperties(properties, builder);
		
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Algorithm newSMSEMOA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);

	    SMSEMOABuilder builder = new SMSEMOABuilder(adapter, crossover, mutation);
	    loadProperties(properties, builder);
        
		return new JMetalAlgorithmAdapter(builder.build(), properties, adapter);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Algorithm newWASFGA(TypedProperties properties, Problem problem) throws JMetalException {
		ProblemAdapter<?> adapter = createProblemAdapter(problem);
		CrossoverOperator<?> crossover = JMetalFactory.getInstance().createCrossoverOperator(adapter, properties);
		MutationOperator<?> mutation = JMetalFactory.getInstance().createMutationOperator(adapter, properties);
	    SelectionOperator selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());
	    
		double[] referencePoint = properties.getDoubleArray("referencePoint", new double[problem.getNumberOfObjectives()]);
	    
		WASFGA algorithm = new WASFGA(adapter,
				(int)properties.getDouble("populationSize", 100),
				DefaultAlgorithms.getMaxIterations(properties),
	            crossover,
	            mutation,
	            selection,
	            new SequentialSolutionListEvaluator(),
	            properties.getDouble("epsilon", 0.01),
	            DoubleStream.of(referencePoint).boxed().toList(),
	            properties.getString("weightVectorsFile", ""));

		return new JMetalAlgorithmAdapter(algorithm, properties, adapter);
	}

}
