
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class DecisionTree {
    private Node root;

    public DecisionTree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public String classifyInstance(Data instance) {
        Node currentNode = root;
        while (currentNode.getTrueBranch() != null && currentNode.getFalseBranch() != null) {
            String attribute = currentNode.getAttribute();
            String attributeValue = instance.getAttributeValue(attribute);

            if (attributeValue != null && attributeValue.equals("true")) {
                currentNode = currentNode.getTrueBranch();
            } else {
                currentNode = currentNode.getFalseBranch();
            }
        }
        return currentNode.getAttribute();
    }

    public int classifyInstances(ArrayList<Data> instances) {
        int correctClassifications = 0;
        for (Data instance : instances) {
            String classification = classifyInstance(instance);
            if (classification.equals("false") && instance.getClassLabel().equals("no-recurrence-events") ||
                    (classification.equals("true") && instance.getClassLabel().equals("recurrence-events"))) {
                correctClassifications++;
            } else {

            }
        }
        return correctClassifications;
    }
}

class Node {
    private String attribute;
    private Node trueBranch;
    private Node falseBranch;

    public Node(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Node getTrueBranch() {
        return trueBranch;
    }

    public void setTrueBranch(Node trueBranch) {
        this.trueBranch = trueBranch;
    }

    public Node getFalseBranch() {
        return falseBranch;
    }

    public void setFalseBranch(Node falseBranch) {
        this.falseBranch = falseBranch;
    }
}

class Population {
    private ArrayList<DecisionTree> trees;
    private DecisionTree bestTree;

    public Population() {
        trees = new ArrayList<>();
        bestTree = null;
    }

    public DecisionTree getBestTree() {
        return bestTree;
    }

    public void initializePopulation(int populationSize, int maxTreeDepth, Random random) {
        for (int i = 0; i < populationSize; i++) {
            Node root = createRandomTree(maxTreeDepth, random);
            DecisionTree tree = new DecisionTree(root);
            trees.add(tree);
        }
    }

    private Node createRandomTree(int maxDepth, Random random) {
        if (maxDepth == 0 || random.nextDouble() < 0.5) {
            String classification = random.nextDouble() < 0.5 ? "true" : "false";
            return new Node(classification);
        }

        String attribute = getRandomAttribute(random);
        Node root = new Node(attribute);
        root.setTrueBranch(createRandomTree(maxDepth - 1, random));
        root.setFalseBranch(createRandomTree(maxDepth - 1, random));

        return root;
    }

    private String getRandomAttribute(Random random) {
        String[] attributes = { "age", "menopause", "tumor-size", "inv-nodes", "node-caps", "deg-malig", "breast",
                "breast-quad", "irradiat" };

        return attributes[random.nextInt(attributes.length)];
    }
    //It randomly selects another decision tree (parent) from the population.
    //It copies the attribute value from the root node of the selected parent to 
   //the corresponding root node of the child decision tree.
    private void crossover(DecisionTree child, DecisionTree parent) {
        child.getRoot().setAttribute(parent.getRoot().getAttribute());
    }

 

    public double getFitness(DecisionTree tree, ArrayList<Data> dataset) {

        int correctClassifications = tree.classifyInstances(dataset);
     
        return (double) correctClassifications / dataset.size();
    }

    public void evolve(int numGenerations, int populationSize, double crossoverRate, double mutationRate,
            ArrayList<Data> dataset, Random random) {
        initializePopulation(populationSize, 10, random);
        bestTree = trees.get(0);

        for (int i = 0; i < numGenerations; i++) {
            for (DecisionTree tree : trees) {
                if (random.nextDouble() < crossoverRate) {
                    DecisionTree parent = trees.get(random.nextInt(trees.size()));
                    crossover(tree, parent);
                }
                if (random.nextDouble() < mutationRate) {
                    mutation(tree, random);
                }
            }

            for (DecisionTree tree : trees) {
                double fitness = getFitness(tree, dataset);
                if (fitness > getFitness(bestTree, dataset)) {
                    bestTree = tree;
                }
            }
        }
    }
    private void mutation(DecisionTree tree, Random random) {
        Node root = tree.getRoot();

        String[] attributes = {"age", "menopause", "tumor-size", "inv-nodes", "node-caps", "deg-malig", "breast",
                "breast-quad", "irradiat"};

        if (random.nextDouble() < 0.5) {//50% chance of changing the attribute value of the root node to a random value
            root.setAttribute(attributes[random.nextInt(attributes.length)]);
        } else {// If the selected node has true and false branches (child nodes), 
            //it recursively calls the mutation method on each of the child nodes, 
            //allowing for potential changes in the subtree structure.
            if (root.getTrueBranch() != null) {
                mutation(new DecisionTree(root.getTrueBranch()), random);
            }
            if (root.getFalseBranch() != null) {
                mutation(new DecisionTree(root.getFalseBranch()), random);
            }
        }
    }
    public double getAverageFitness(ArrayList<Data> dataset) {
        double totalFitness = 0.0;
        for (DecisionTree tree : trees) {
            totalFitness += getFitness(tree, dataset);
        }
        return totalFitness / trees.size();
    }


}

class Main {
    public static void main(String[] args) {

        long seed = 12345; // Seed value for replicable results
        Random random = new Random(seed);
        String trainingFilePath = "training_data.data";
        String testFilePath = "test.txt";
        ArrayList<Data> trainingDataset = readDataset(trainingFilePath);
        ArrayList<Data> testDataset = readDataset(testFilePath);

        if (trainingDataset.isEmpty() || testDataset.isEmpty()) {
            System.out.println("Failed to read the dataset.");
            return;
        }
        System.out.println("Training dataset size: " + trainingDataset.size());
        long startTime = System.nanoTime();
        Population population = new Population();
        population.evolve(100, 400, 0.2, 0.002, trainingDataset, random);//when the mutation rate is 0.2, the accuracy is 0.125, but when the mutation rate is 0.5, the accuracy is 0.87
        
        double bestAverageAccuracy = 0.0;
        double accuracy = 0.0;
        System.out.println("Training completed.");
        System.out.println("-----------------------------------------");


        for (int generation = 0; generation < 350; generation++) {
            DecisionTree bestTree = population.getBestTree();
            printDecisionTree(bestTree);

            double averageAccuracy = population.getAverageFitness(testDataset);
            if (averageAccuracy > bestAverageAccuracy) {
                bestAverageAccuracy = averageAccuracy;
            }

            if (bestTree != null) {
                int correctClassifications = bestTree.classifyInstances(testDataset);
                accuracy = (double) correctClassifications / testDataset.size();
            } else {
                System.out.println("No best tree found.");
            }

            System.out.println("Generation: " + (generation + 1));
            //represents the highest average accuracy achieved by any tree in the population on the test dataset throughout the generations
            System.out.println("Best Average Accuracy: " + bestAverageAccuracy);



            //The accuracy is then calculated as the ratio of the number of correctly classified instances
            // to the total number of instances in the test dataset.
            System.out.println("Accuracy: " + accuracy);
            System.out.println("-----------------------------------------");
        }

        System.out.println("Final Best Average Accuracy: " + bestAverageAccuracy);
        System.out.println("Final Accuracy: " + accuracy);
    
        long endTime = System.nanoTime();

        double executionTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("\u001B[32m"+"Training time: " + executionTimeSeconds + " seconds"+"\u001B[0m");
    }

    // ... existing code ...

    private static ArrayList<Data> readDataset(String filePath) {
        ArrayList<Data> dataset = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] attributes = line.split(",");

                String[] dataAttributes = new String[attributes.length - 1];
                for (int i = 1; i < attributes.length; i++) {
                    dataAttributes[i - 1] = attributes[i];
                }

                String classLabel = attributes[0];
                Data data = new Data(dataAttributes, classLabel);
                dataset.add(data);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private static void printDecisionTree(DecisionTree tree) {
        System.out.println("Decision Tree:");
        printNode(tree.getRoot(), 0);
    }

    private static void printNode(Node node, int level) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(node.getAttribute());

        printNode(node.getTrueBranch(), level + 1);
        printNode(node.getFalseBranch(), level + 1);
    }
}

class Data {
    private String[] attributes;
    private String classLabel;

    public Data(String[] attributes, String classLabel) {
        this.attributes = attributes;
        this.classLabel = classLabel;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public String getAttribute(int index) {
        return attributes[index];
    }

    public String getAttributeValue(String attribute) {
        int index = getAttributeIndex(attribute);
        if (index != -1) {
            return attributes[index];
        }
        return null;
    }

    public String getClassLabel() {
        return classLabel;
    }

    private int getAttributeIndex(String attribute) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].equals(attribute)) {
                return i;
            }
        }
        return -1;
    }
}
