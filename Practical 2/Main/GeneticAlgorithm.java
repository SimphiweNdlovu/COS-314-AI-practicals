


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int TOURNAMENT_SIZE = 4;// Number of individuals to select for tournament  
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_ITERATIONS = 1000;
    private static final double CONVERGENCE_THRESHOLD = 0.001; // Improvement threshold for convergence
    private static final int CONVERGENCE_ITERATIONS = 100; // Number of iterations to check for convergence

    private static double[] values = {};
    private static double[] weights = {};

    private static  int NUM_ITEMS = 0;
    private static  double CAPACITY = 0.0;

    private static final Random random = new Random();

    private static class Individual {
        private boolean[] genes;
        private double fitness=0.0;

        public Individual() {
            genes = new boolean[NUM_ITEMS];
            fitness = 0.0;
        }

        public Individual(boolean[] genes) {
            this.genes = genes;
            calculateFitness();
        }

        public boolean[] getGenes() {
            return genes;
        }

        public double getFitness() {
            return fitness;
        }

        public void calculateFitness() {
            double totalValue = 0.0;
            double totalWeight = 0.0;

            for (int i = 0; i < NUM_ITEMS; i++) {
                if (genes[i]) {
                    totalValue += values[i];
                    totalWeight += weights[i];
                }
            }

            if (totalWeight > CAPACITY) {
                totalValue = 0.0; // Penalize solutions that exceed the capacity
            }

            fitness = totalValue;
        }

        public void mutate() {
            for (int i = 0; i < NUM_ITEMS; i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    genes[i] = !genes[i]; // Flip the bit
                }
            }
        }
    }




    public void GoGA(ArrayList<ProblemInstance> problemInstances, ArrayList<knownOptimum> knownOptimums) {
        System.out.println("Genetic Algorithm");
                System.out.println("\033[1;32m---------------------------------STARTING THE Genetic Algorithm---------------------------------\033[0m" );
        System.out.println("Problem Instances size: " + problemInstances.size());
            long elapsedTime = 0;
        for(int i=0;i<problemInstances.size();i++){
               long start = System.nanoTime();    
               
            elapsedTime += System.nanoTime() - start;
            System.out.println(problemInstances.get(i).name);
            System.out.println(problemInstances.get(i).itemCount);
            System.out.println(problemInstances.get(i).knapsackCapacity);
          
     

     
            values = new double[problemInstances.get(i).itemList.size()];
            weights = new double[problemInstances.get(i).itemList.size()];
            for(int ii=0;ii<problemInstances.get(i).itemList.size();ii++){
                values[ii] = problemInstances.get(i).itemList.get(ii).value;
                weights[ii] = problemInstances.get(i).itemList.get(ii).weight;
            }
            CAPACITY = problemInstances.get(i).knapsackCapacity;
            // System.out.println("CAPACITY: "+CAPACITY);
            NUM_ITEMS = problemInstances.get(i).itemList.size();

            GoGA();
            System.out.println(" Known optimum: "+knownOptimums.get(i).Optimum);
                        System.out.println("ELAPSED TIME: "+ "\u001B[32m"+(elapsedTime/ problemInstances.get(i).itemList.size()) + " ns" +"\u001B[0m");
            System.out.println("--------------------------------------------------");

        }


    }

    public static void GoGA() {
        List<Individual> population = new ArrayList<>();

        // Generate an initial population
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            population.add(generateRandomIndividual());
        }

        int iteration = 0;
        Individual bestIndividual = null;
        int stagnantIterations = 0;
        double prevBestFitness = 0.0;

        while (iteration < MAX_ITERATIONS && stagnantIterations < CONVERGENCE_ITERATIONS) {
            // Perform tournament selection
            Individual parent1 = tournamentSelection(population);
            Individual parent2 = tournamentSelection(population);

            // Perform crossover
            Individual child = crossover(parent1, parent2);

            // Perform mutation
            child.mutate();

            // Calculate the fitness of the child
            child.calculateFitness();

            // Replace a random individual in the population with the child
            int randomIndex = random.nextInt(TOURNAMENT_SIZE);
            population.set(randomIndex, child);

            // Update the best individual
            if (bestIndividual == null || child.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = child;
            }

            // Check for convergence
            if (Math.abs(bestIndividual.getFitness() - prevBestFitness) < CONVERGENCE_THRESHOLD) {
               
                stagnantIterations++;
            } else {
            stagnantIterations = 0;
            }
            prevBestFitness = bestIndividual.getFitness();
            iteration++;
        }
    
        System.out.println("--Best solution found--");
        System.out.println("Genes: " + arrayToString(bestIndividual.getGenes()));
        System.out.println("Fitness: " + "\u001B[34m"+bestIndividual.getFitness() + "\u001B[0m");
        // System.out.println("Iterations: " + iteration);
    }
    
    private static Individual generateRandomIndividual() {
        boolean[] genes = new boolean[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            genes[i] = random.nextBoolean();
        }
        return new Individual(genes);
    }
    
    private static Individual tournamentSelection(List<Individual> population) {
        Individual bestIndividual = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual individual = population.get(random.nextInt(population.size()));
            if (bestIndividual == null || individual.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }
    
    private static Individual crossover(Individual parent1, Individual parent2) {
        boolean[] parent1Genes = parent1.getGenes();
        boolean[] parent2Genes = parent2.getGenes();
        boolean[] childGenes = new boolean[NUM_ITEMS];
        int crossoverPoint = random.nextInt(NUM_ITEMS);
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (i < crossoverPoint) {
                childGenes[i] = parent1Genes[i];
            } else {
                childGenes[i] = parent2Genes[i];
            }
        }
        return new Individual(childGenes);
    }
    
    private static String arrayToString(boolean[] array) {
        StringBuilder sb = new StringBuilder();
        for (boolean element : array) {
            sb.append(element ? "1" : "0").append(" ");
        }
        return sb.toString().trim();
    }

   
}