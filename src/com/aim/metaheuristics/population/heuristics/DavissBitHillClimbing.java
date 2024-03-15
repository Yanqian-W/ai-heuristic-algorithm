package com.aim.metaheuristics.population.heuristics;

import uk.ac.nott.cs.aim.domains.chesc2014_SAT.SAT;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;
import uk.ac.nott.cs.aim.satheuristics.genetics.PopulationHeuristic;

import java.util.Random;
import java.util.stream.IntStream;

public abstract class DavissBitHillClimbing extends PopulationHeuristic {

	public DavissBitHillClimbing(SAT oProblem, Random oRandom) {
		super(oProblem, oRandom);
	}

	public void applyHeuristic(int iSolutionMemoryIndex) {
		
		int[] variableIndices = IntStream.range(0, this.problem.getNumberOfVariables()).toArray();
		int[] perm = ArrayMethods.shuffle(variableIndices, this.random);
		
		double currentCost = this.problem.getObjectiveFunctionValue(iSolutionMemoryIndex);

        for (int i : perm) {
            this.problem.bitFlip(i, iSolutionMemoryIndex);
            double candidateCost = this.problem.getObjectiveFunctionValue(iSolutionMemoryIndex);

            if (acceptMove(currentCost, candidateCost)) {
                currentCost = candidateCost;
            } else {
                this.problem.bitFlip(i, iSolutionMemoryIndex);
            }
        }
	}

	public abstract boolean acceptMove(double paramDouble1, double paramDouble2);
}
