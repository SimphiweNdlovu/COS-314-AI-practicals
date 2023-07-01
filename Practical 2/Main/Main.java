import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        knownOptimum knownOptimum = new knownOptimum();
        knownOptimum.createKnownOptimums();

        ReadProblemInstances ProblemInstances = new ReadProblemInstances();

        try {
            ProblemInstances = ProblemInstances.Readinstances();
          
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //do GA
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

        geneticAlgorithm.GoGA(ProblemInstances.problemInstances, knownOptimum.knownOptimums);

        //do ACO
        ACOKnapsackSolver acoKnapsackSolver = new ACOKnapsackSolver();
        acoKnapsackSolver.GoACO(ProblemInstances.problemInstances, knownOptimum.knownOptimums);


    }
}
