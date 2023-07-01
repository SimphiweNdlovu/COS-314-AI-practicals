
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ACOKnapsackSolver {
    private int capacity;
    private double[][] items;
    private int numAnts;
    private double alpha;
    private double beta;
    private double evaporationRate;
    private double initialPheromone;
    private int maxIterations;
    private double[][] pheromones;
    private double bestValue;
    private List<Integer> bestSolution;
    private int convergenceThreshold;
    private int convergenceCounter;

    public ACOKnapsackSolver(int capacity, double[][] items, int numAnts, double alpha, double beta,
            double evaporationRate, double initialPheromone, int maxIterations,
        int convergenceThreshold) {
        this.capacity = capacity;
        this.items = items;
        this.numAnts = numAnts;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.initialPheromone = initialPheromone;
        this.maxIterations = maxIterations;
        this.pheromones = new double[items.length][items.length];
        this.bestValue = Integer.MIN_VALUE;
        this.bestSolution = new ArrayList<>();
        this.convergenceThreshold = convergenceThreshold;
        this.convergenceCounter = 0;
    }

    public ACOKnapsackSolver() {
    }

    public void solve() {
        initializePheromones();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<List<Integer>> antSolutions = new ArrayList<>();

            // Construct ant solutions
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> solution = constructSolution();
                antSolutions.add(solution);
            }

            // Update pheromones
            updatePheromones(antSolutions);

            // Update best solution
            for (List<Integer> solution : antSolutions) {
                double value = calculateTotalValue(solution);
                if (value > bestValue) {
                    bestValue = value;
                    bestSolution = solution;
                    convergenceCounter = 0; // Reset convergence counter
                }
            }

            convergenceCounter++;
            if (convergenceCounter >= convergenceThreshold) {
                break; // Terminate if convergence threshold is reached
            }
        }

        System.out.println("Best Solution: " + bestSolution);
        System.out.println("Best Value: " + "\u001B[34m"+ bestValue + "\u001B[0m");
    }

   
    private void initializePheromones() {
        double initialPheromoneLevel = 1.0 / (items.length * initialPheromone);
        for (int i = 0; i < items.length; i++) {
            Arrays.fill(pheromones[i], initialPheromoneLevel);
        }
    }

    private List<Integer> constructSolution() {
        List<Integer> solution = new ArrayList<>();
        boolean[] visited = new boolean[items.length];
        int remainingCapacity = capacity;

        while (true) {
            int item = selectNextItem(visited, solution, remainingCapacity);
            if (item == -1) {
                break;
            }

            solution.add(item);
            visited[item] = true;
            remainingCapacity -= items[item][1];
        }

        return solution;
    }

    private int selectNextItem(boolean[] visited, List<Integer> solution, int remainingCapacity) {
        double[] probabilities = new double[items.length];
        double totalProbability = 0.0;

        for (int i = 0; i < items.length; i++) {
            if (!visited[i] && items[i][1] <= remainingCapacity) {
                probabilities[i] = Math.pow(pheromones[i][i], alpha) * Math.pow(1.0 / items[i][0], beta);
                totalProbability += probabilities[i];
            }
        }

        if (totalProbability == 0.0) {
            return -1;
        }

        double randomValue = new Random().nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < items.length; i++) {
            if (!visited[i] && items[i][1] <= remainingCapacity) {
                cumulativeProbability += probabilities[i] / totalProbability;
                if (randomValue <= cumulativeProbability) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void updatePheromones(List<List<Integer>> antSolutions) {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items.length; j++) {
                if (i != j) {
                    pheromones[i][j] *= evaporationRate;
                }
            }
        }

        for (List<Integer> solution : antSolutions) {
            double solutionValue = calculateTotalValue(solution);

            for (int i = 0; i < solution.size(); i++) {
                int item = solution.get(i);
                pheromones[item][item] += 1.0 / solutionValue;
            }
        }
    }

    private int calculateTotalValue(List<Integer> solution) {
        int totalValue = 0;
        for (int item : solution) {
            totalValue += items[item][0];
        }
        return totalValue;
    }

    public static void GoACO (ArrayList<ProblemInstance> problemInstances, ArrayList<knownOptimum> knownOptimums) {
        int CAPACITY ;
        int numAnts ;
        double alpha = 1.0;
        double beta = 1.0;
        double evaporationRate = 0.5;
        double initialPheromone = 1.0;
        int maxIterations = 1000;
        int convergenceThreshold = 20; // Set the convergence threshold

        double[][] items ={};
    


        System.out.println("\033[1;31m---------------------------------STARTING THE ACO---------------------------------\033[0m" );
        System.out.println("Problem Instances size: " + problemInstances.size());
        long elapsedTime = 0;

        for(int i=0;i<problemInstances.size();i++){
            long start = System.nanoTime();    
               
            elapsedTime += System.nanoTime() - start;

            System.out.println(problemInstances.get(i).name);
            System.out.println(problemInstances.get(i).itemCount);
            System.out.println(problemInstances.get(i).knapsackCapacity);
          
     

     
           
            items = new double[problemInstances.get(i).itemList.size()][2];
            for(int ii=0;ii<problemInstances.get(i).itemList.size();ii++){
                items[ii][0] =  problemInstances.get(i).itemList.get(ii).value;

                items[ii][1] =  problemInstances.get(i).itemList.get(ii).weight;
            }
            CAPACITY = problemInstances.get(i).knapsackCapacity;
            // System.out.println("CAPACITY: "+CAPACITY);
            numAnts = problemInstances.get(i).itemList.size();

            ACOKnapsackSolver solver = new ACOKnapsackSolver(CAPACITY, items, numAnts, alpha, beta,
                evaporationRate, initialPheromone, maxIterations, convergenceThreshold);
        solver.solve();
            System.out.println(" Known optimum: "+knownOptimums.get(i).Optimum);

            System.out.println("ELAPSED TIME: "+"\u001B[32m"+ (elapsedTime/ problemInstances.get(i).itemList.size()) + " ns" +"\u001B[0m");
            System.out.println("--------------------------------------------------");

        }

       
     
        
       
    }
}

