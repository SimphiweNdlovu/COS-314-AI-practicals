import java.io.*;
import java.util.ArrayList;


public class ReadProblemInstances {

    ArrayList<ProblemInstance> problemInstances = new ArrayList<>();

    public void createProblemInstance(String dirPath) throws IOException {
        File file = new File(dirPath);
        String problemInstanceName = file.getName();
        ProblemInstance problemInstance = new ProblemInstance(problemInstanceName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            String[] firstLineParts = line.split(" ");
            problemInstance.itemCount = Integer.parseInt(firstLineParts[0]);
            problemInstance.knapsackCapacity = Integer.parseInt(firstLineParts[1]);
            
            while ((line = br.readLine()) != null) {
                String[] itemParts = line.split(" ");
                double weight = Double.parseDouble(itemParts[1]);
                double value = Double.parseDouble(itemParts[0]);
                // System.out.println(value + " " + weight);
                problemInstance.itemList.add(new Item(weight, value));
            }
        }
        problemInstances.add(problemInstance);
        

    }

   public ReadProblemInstances Readinstances() throws IOException {
               String[] instancePaths = {
            "Knapsack Instances/f1_l-d_kp_10_269",
            "Knapsack Instances/f2_l-d_kp_20_878",
            "Knapsack Instances/f3_l-d_kp_4_20",
            "Knapsack Instances/f4_l-d_kp_4_11",
            "Knapsack Instances/f5_l-d_kp_15_375",
            "Knapsack Instances/f6_l-d_kp_10_60",
            "Knapsack Instances/f7_l-d_kp_7_50",
            "Knapsack Instances/knapPI_1_100_1000_1",
            "Knapsack Instances/f8_l-d_kp_23_10000",
            "Knapsack Instances/f9_l-d_kp_5_80",
            "Knapsack Instances/f10_l-d_kp_20_879"
 
        };


        ReadProblemInstances readProblemInstances = new ReadProblemInstances();
        for (int i = 0; i < instancePaths.length; i++) {
            try {
                readProblemInstances.createProblemInstance(instancePaths[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

  
        return readProblemInstances;
      
    }
}
