public class Main {
    public static void main(String[] args) {

        int num_hidden_layers=1;
        System.out.println("\033[1;31m---------------------------------STARTING  TRAINING THE AAN---------------------------------\033[0m" );

        //TRAIN
        long startTime = System.nanoTime();

        ArtificialNeuralNetwork ANN=new ArtificialNeuralNetwork(num_hidden_layers,"training.txt");
        ANN.run();
        ANN.printStats();

        StorageUnit SU=ANN.getStorageUnit();

        long endTime = System.nanoTime();

        double executionTimeSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("\u001B[32m"+"Training time: " + executionTimeSeconds + " seconds"+"\u001B[0m");
        System.out.println("\u001B[35m"+"###########---------------------------------NOW   TESTING THE AAN---------------------------------"+"\u001B[0m" );
        //TEST
        startTime = System.nanoTime();

        ArtificialNeuralNetwork ANN2=new ArtificialNeuralNetwork(num_hidden_layers,"test.txt");
        ANN2.setParameters(SU);
        ANN2.runTest();
        ANN2.printStats();

        endTime = System.nanoTime();

        executionTimeSeconds = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("\u001B[32m"+"testing time: " + executionTimeSeconds + " seconds" +"\u001B[0m");
    }
}
