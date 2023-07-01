import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ArtificialNeuralNetwork {

    ArrayList<Data> dataList;
    int HIDDEN_LAYERS;

    private static final int INPUT_PARAMETERS = 9;

    private int ID = 0;

    private int ID_DATA = 0;

    private int ID_HIDDEN = 0;
    private static final long SEED = 1234L; 
    private static Random random = new Random(SEED);

    private float TP = 0.0F;
    private float FP = 0.0F;
    private float FN = 0.0F;

    ArrayList<ArrayList<Node>> Layers;

  

    private float bias_1 = 0.1F;

    private float bias_2 = 0.3F;

    private float learning_rate = 0.02F; // was 0.01 but some of the data was taking too long to train
    private String filename;

    ArtificialNeuralNetwork(int HiddenLayers, String filename) {
        this.HIDDEN_LAYERS = HiddenLayers;
        this.filename = filename;
        dataList = new ArrayList<>();
        Layers = new ArrayList<>();

        // begin
        ReadInData(filename);
        InitializeInputLayer();

        InitializeHiddenLayer();

        InitializeOutputLayer();

        createHiddenConnections();
        linkInputLayersToFirstHiddenLayer();
        linkLastHiddenLayerToOutputLayer();

    }

    public void run() {
        feedForwardInputToHiddenLayers();
    }

    public float backPropagationAtOutputLayer(float targetOutput) {
        ArrayList<Node> outputLayer = Layers.get(Layers.size() - 1);

        float actualOutput = outputLayer.get(0).getFn_value();

        System.out.println("targetOutput: " + targetOutput);
        System.out.println("actualOutput: " + actualOutput);

        float error = targetOutput - actualOutput;

        outputLayer.get(0).setFn_value(error);

        return error;
    }

    public void feedForwardInputToHiddenLayers() {
        ArrayList<Node> inputLayer = Layers.get(0);

        for (int i = 0; i < dataList.size(); ++i) {
            boolean first = true;

            System.out.println("Patient ID: " + dataList.get(i).id);

            Data data = dataList.get(i);

            ArrayList<Float> normalizedData = normalize(data);

            data.setNormalizedData(normalizedData);

            System.out.println(normalizedData);

            int epoch = 0;

            while (true) {
                System.out.println("\033[1;31m" + "EPOCH: " + epoch + "\033[0m");

                // Load data onto the input layer
                for (int j = 1; j < normalizedData.size(); ++j) {
                    inputLayer.get(j - 1).setFn_value(normalizedData.get(j));
                }

                // Calculate values for hidden layers
                for (int k = 1; k < Layers.size() - 1; ++k) {
                    ArrayList<Node> layer = Layers.get(k);
                    // System.out.println("Layer: " + k);

                    for (int m = 0; m < layer.size(); ++m) {
                        Node node = layer.get(m);

                        float n = 0.0F;

                        if (k == 1) {
                            n = node.calculateWeightedSumWithBias(bias_1, dataList.get(i), true);
                        } else {
                            n = node.calculateWeightedSumWithBias(bias_1, dataList.get(i), false);
                        }

                     



                        //  ReLU(x) = max(0, x)  Rectified Linear Unit.
                        if (n >= 0)
                            node.setFn_value(n);
                        else
                            node.setFn_value(0);

                        
                    }
                }


                System.out.println();

                boolean termination = feedForwardHiddenLastToOutputLayer(data.normalizedData.get(0), data);

                if (termination) {
                    if (first) {
                        ++TP;
                        first = false;
                    }

                    break;
                }

                first = false;
                float error = backPropagationAtOutputLayer(data.normalizedData.get(0));

            
                if (error == -1.0)
                    ++FN;
                else
                    ++FP;

                System.out.println("\033[1;31m" + "error: " + error + "\033[0m");
              


                DoBackPropagation(error,i);
               

                ++epoch;
            }
        }
    }

    private void DoBackPropagation(float error, int i) {
        System.out.println("---backpropagation---");
        updateWeightAtOutputLayer(error);
        // updateBiasForTheOutputLayer
        float change_in_bias = error * learning_rate;

        bias_2 = bias_2 + change_in_bias;

        updateWeightsAtHiddenLayers(error, dataList.get(i));
        // updateBiasForHiddenLayers
        float change_in_bias1 = error * learning_rate;

        bias_1 = bias_1 + change_in_bias1;
    }

   

    public void updateWeightsAtHiddenLayers(float error, Data data) {
        for (int i = Layers.size() - 2; i > 0; --i) {
            ArrayList<Node> hiddenLayer = Layers.get(i);

            for (int k = 0; k < hiddenLayer.size(); ++k) {
             
                Node node = hiddenLayer.get(k);

             
                float reverse_n = node.calculateWeightedSumWithBias(error, data, false);

                float info_error = reverse_n * 1;

                float change_in_weights = learning_rate * info_error * data.normalizedData.get(k + 1);

                for (int m = 0; m < node.arr_ins.size(); ++m) {
                    Connector ptr = node.arr_ins.get(m);

                    ptr.setWeight(ptr.getWeight() + change_in_weights);

                
                }
            }

        }
    }

    public void updateWeightAtOutputLayer(float error) {
        ArrayList<Node> outputLayer = Layers.get(Layers.size() - 1);

        for (int i = 0; i < outputLayer.size(); ++i) {

            Node node = outputLayer.get(i);

            float change_in_weight = learning_rate * error * node.getFn_value();

            for (int k = 0; k < node.arr_ins.size(); ++k) {
                Connector ptr = node.arr_ins.get(k);

                ptr.setWeight(ptr.getWeight() + change_in_weight);

              
            }
        }
    }

    public boolean feedForwardHiddenLastToOutputLayer(float expected, Data data) {
        ArrayList<Node> outputLayer = Layers.get(Layers.size() - 1);

        for (int i = 0; i < outputLayer.size(); ++i) {
            Node node = outputLayer.get(i);

            float n = node.sumWeightBiasInsForOutputs(bias_2);

            // we are using Binary step function on the outputlayer

            if (n >= 0)
                node.setFn_value(1);
            else
                node.setFn_value(0);

            System.out.println("bias_2: " + bias_2);
            System.out.println("sumWeights: " + node.sumWeightBiasInsForOutputs(bias_2));
           
            // System.out.println("n: " + n);
            System.out.println("\u001B[34m" + "diagnosis: " + node.getFn_value() + "\u001B[0m");
            // System.out.println();

            if (expected == node.getFn_value()) {
                System.out.println("\u001B[92m" + "---------------------diagnosis is equals to expected: " + expected
                        + "---------------" + "\u001B[0m");
                return true;
            }
        }

        return false;
    }

    public void InitializeOutputLayer() {
        ArrayList outputLayer = new ArrayList<>();
        outputLayer.add(new Node(0, ID++, Discriminate.o));
        Layers.add(outputLayer);
    }

    public void linkLastHiddenLayerToOutputLayer() {
        ArrayList<Node> outputLayer = Layers.get(Layers.size() - 1);
        ArrayList<Node> hiddenLayer = Layers.get(Layers.size() - 2);

        for (int i = 0; i < hiddenLayer.size(); ++i) {
            Node node = hiddenLayer.get(i);

            for (int k = 0; k < outputLayer.size(); ++k) {
                Node dest = outputLayer.get(k);

                Connector connector = new Connector(node, dest, generateRandomValue());

                node.connect(connector, "out");
                dest.connect(connector, "in");

                // System.out.println(connector);
            }
        }

    }

    public void linkInputLayersToFirstHiddenLayer() {
        ArrayList<Node> inputLayer = Layers.get(0);
        ArrayList<Node> hiddenLayer = Layers.get(1);

        for (int i = 0; i < inputLayer.size(); ++i) {
            Node node = inputLayer.get(i);

            for (int k = 0; k < hiddenLayer.size(); ++k) {
                Node dest = hiddenLayer.get(k);

                Connector connector = new Connector(node, dest, generateRandomValue());

                node.connect(connector, "out");
                dest.connect(connector, "in");
            }
        }
    }

    public ArrayList<Float> normalize(Data data) {
        float norm_age = 0.0F;
        float norm_men = 0.0F;
        float norm_tumor_size = 0.0F;
        float norm_inv_nodes = 0.0F;
        float norm_node_caps = 0.0F;
        float norm_deg_malig = 0.0F;
        float norm_breast = 0.0F;
        float norm_breast_quad = 0.0F;
        float norm_irridiat = 0.0F;
        float norm_out = 0.0F;

        ArrayList<Float> arr_return = new ArrayList<>();

        if (data.out.equals("recurrence-events"))
            norm_out = 1.0F; // have the disease
        else
            norm_out = 0.0F; // don't have

        arr_return.add(norm_out);

        if (data.age.equals("10-19"))
            norm_age = 0.1111F;
        else if (data.age.equals("20-29"))
            norm_age = 0.2222F;
        else if (data.age.equals("30-39"))
            norm_age = 0.3333F;
        else if (data.age.equals("40-49"))
            norm_age = 0.4444F;
        else if (data.age.equals("50-59"))
            norm_age = 0.5555F;
        else if (data.age.equals("60-69"))
            norm_age = 0.6666F;
        else if (data.age.equals("70-79"))
            norm_age = 0.7777F;
        else if (data.age.equals("80-89"))
            norm_age = 0.8888F;
        else if (data.age.equals("90-99"))
            norm_age = 1.0F;

        arr_return.add(norm_age);

        if (data.menopause.equals("it40"))
            norm_men = 0.0F;
        else if (data.menopause.equals("gen40"))
            norm_men = 0.5F;
        else if (data.menopause.equals("premeno"))
            norm_men = 1.0F;

        arr_return.add(norm_men);

        if (data.tumor_size.equals("0-4"))
            norm_tumor_size = 0.0F;
        else if (data.tumor_size.equals("5-9"))
            norm_tumor_size = 0.0909F;
        else if (data.tumor_size.equals("10-14"))
            norm_tumor_size = 0.1818F;
        else if (data.tumor_size.equals("15-19"))
            norm_tumor_size = 0.2727F;
        else if (data.tumor_size.equals("20-24"))
            norm_tumor_size = 0.3636F;
        else if (data.tumor_size.equals("25-29"))
            norm_tumor_size = 0.4545F;
        else if (data.tumor_size.equals("30-34"))
            norm_tumor_size = 0.5454F;
        else if (data.tumor_size.equals("35-39"))
            norm_tumor_size = 0.6363F;
        else if (data.tumor_size.equals("40-44"))
            norm_tumor_size = 0.7272F;
        else if (data.tumor_size.equals("45-49"))
            norm_tumor_size = 0.8181F;
        else if (data.tumor_size.equals("50-54"))
            norm_tumor_size = 0.9090F;
        else if (data.tumor_size.equals("55-59"))
            norm_tumor_size = 1.0F;

        arr_return.add(norm_tumor_size);

        if (data.inv_nodes.equals("0-2"))
            norm_inv_nodes = 0.0F;
        else if (data.inv_nodes.equals("3-5"))
            norm_inv_nodes = 0.0822F;
        else if (data.inv_nodes.equals("6-8"))
            norm_inv_nodes = 0.1644F;
        else if (data.inv_nodes.equals("9-11"))
            norm_inv_nodes = 0.0247F;
        else if (data.inv_nodes.equals("12-14"))
            norm_inv_nodes = 0.3288F;
        else if (data.inv_nodes.equals("15-17"))
            norm_inv_nodes = 0.4120F;
        else if (data.inv_nodes.equals("18-20"))
            norm_inv_nodes = 0.4932F;
        else if (data.inv_nodes.equals("21-23"))
            norm_inv_nodes = 0.5753F;
        else if (data.inv_nodes.equals("24-26"))
            norm_inv_nodes = 0.6576F;
        else if (data.inv_nodes.equals("27-29"))
            norm_inv_nodes = 0.7397F;
        else if (data.inv_nodes.equals("30-32"))
            norm_inv_nodes = 0.8219F;
        else if (data.inv_nodes.equals("33-35"))
            norm_inv_nodes = 0.9041F;
        else if (data.inv_nodes.equals("36-39"))
            norm_inv_nodes = 1.0F;

        arr_return.add(norm_inv_nodes);

        if (data.node_caps.equals("yes"))
            norm_node_caps = 1.0F;
        else
            norm_node_caps = 0.0F;

        arr_return.add(norm_node_caps);

        if (data.deg_malig == 1)
            norm_deg_malig = 0.0F;
        else if (data.deg_malig == 2)
            norm_deg_malig = 0.5F;
        else if (data.deg_malig == 3)
            norm_deg_malig = 1.0F;

        arr_return.add(norm_deg_malig);

        if (data.breast.equals("left"))
            norm_breast = 0.0F;
        else if (data.breast.equals("right"))
            norm_breast = 1.0F;

        arr_return.add(norm_breast);

        if (data.breast_quad.equals("left_up"))
            norm_breast_quad = 0.0F;
        else if (data.breast_quad.equals("left_low"))
            norm_breast_quad = 0.25F;
        else if (data.breast_quad.equals("right_up"))
            norm_breast_quad = 0.5F;
        else if (data.breast_quad.equals("right_low"))
            norm_breast_quad = 0.75F;
        else if (data.breast_quad.equals("central"))
            norm_breast_quad = 1.0F;

        arr_return.add(norm_breast_quad);

        if (data.irradiat.equals("yes"))
            norm_irridiat = 1.0F;
        else
            norm_irridiat = 0.0F;

        arr_return.add(norm_irridiat);

        return arr_return;

    }

    public void printArrFloat(ArrayList<Float> arr) {
        System.out.print("[");
        for (int i = 0; i < arr.size() - 1; ++i) {
            System.out.print(arr.get(i) + ",");
        }
        System.out.print(arr.get(arr.size() - 1));
        System.out.println("]");
    }

    /***
     * This function assumes that the output layer hasn't been built yet.
     */
    public void createHiddenConnections() {
        if (HIDDEN_LAYERS > 1) {
            for (int i = 1; i < Layers.size(); ++i) {
                ArrayList<Node> hiddenLayer = Layers.get(i);

                if (i + 1 < Layers.size() - 1 && Layers.get(i + 1) != null) {
                    ArrayList<Node> neighbour = Layers.get(i + 1);

                    for (int k = 0; k < hiddenLayer.size(); ++k) {
                        Node node = hiddenLayer.get(k);

                        // every node in hiddenLayer should connect to every node
                        // in neighbour
                        for (int j = 0; j < neighbour.size(); ++j) {
                            Node dest = neighbour.get(j);
                            // System.out.println("node: " + node.getId() + " dest: " + dest.getId());
                            Connector connector = new Connector(node, dest, generateRandomValue());

                            node.connect(connector, "out");
                            dest.connect(connector, "in");

                            // System.out.println(connector);
                        }
                    }
                }
            }
        }
    }

    public static float generateRandomValue() {
        return random.nextFloat();
    }

    public void InitializeHiddenLayer() {
        for (int i = 0; i < HIDDEN_LAYERS; ++i) {
            ArrayList<Node> hiddenLayer = new ArrayList<>();

            for (int k = 0; k < INPUT_PARAMETERS; ++k) {
                hiddenLayer.add(new Node(0, ID_HIDDEN++, Discriminate.h));
            }
            Layers.add(hiddenLayer);
        }
    }

    /***
     * The input layer will be set up with the number of nodes equivalent to the
     * number of inputElements
     */
    public void InitializeInputLayer() {
        ArrayList<Node> inputLayer = new ArrayList<>();

        for (int i = 0; i < INPUT_PARAMETERS; ++i) {
            inputLayer.add(new Node(0, ID++, Discriminate.i));
        }

        Layers.add(inputLayer);

    }

    // recurrence-events,60-69,ge40,30-34,0-2,yes,2,right,right_up,yes
    public void ReadInData(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                int indexOfComma = data.indexOf(",");
                String out = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String age = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String men = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String t_size = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String inv_nodes = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String node_caps = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                int deg_malig = Integer.parseInt(data.substring(0, indexOfComma));
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String breast = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                indexOfComma = data.indexOf(",");
                String breast_quad = data.substring(0, indexOfComma);
                data = data.substring(indexOfComma + 1);

                String irradiant = data;
       
                Data dataObj = new Data(ID_DATA++, age, men, t_size, inv_nodes, node_caps, deg_malig, breast,
                        breast_quad, irradiant, out);

                dataList.add(dataObj);

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void printDataList() {
        for (int i = 0; i < dataList.size(); ++i) {
            System.out.println(dataList.get(i).toString());
        }
    }

    public void printNodeAndConnections(Node node) {
        System.out.println("Node: " + node.getId());
        System.out.println("fn: " + node.getFn_value());

        if (!node.arr_ins.isEmpty()) {
            System.out.println("[IN]");
            for (int i = 0; i < node.arr_ins.size(); ++i) {
                System.out.println(node.arr_ins.get(i));
            }
        }

        if (!node.arr_outs.isEmpty()) {
            System.out.println("[OUT]");
            for (int i = 0; i < node.arr_outs.size(); ++i) {
                System.out.println(node.arr_outs.get(i));
            }
        }
    }

    private float init_b1 = bias_1;
    private float init_b2 = bias_2;
    private float init_learing_rate = learning_rate;

    public void printStats() {
        System.out.println("[" + filename + "]");
        System.out.println("Initial values:");
        System.out.println("bias 1: " + init_b1);
        System.out.println("number of hidden layers: " + HIDDEN_LAYERS);
        System.out.println("number of input params: " + INPUT_PARAMETERS);
        System.out.println("size of training data: 139");
        System.out.println("size of test data: 85");
        System.out.println("bias 2: " + init_b2);
        System.out.println("learning rate: " + init_learing_rate);
        System.out.println();

        System.out.println("New values");
        System.out.println("bias 1: " + bias_1);
        System.out.println("bias 2: " + bias_2);
        System.out.println("True Positives: " + TP);
        System.out.println("False Positives: " + FP);
        System.out.println("False Negatives: " + FN);
        System.out.println("learning rate: " + learning_rate);
    }

    public StorageUnit getStorageUnit() {
        StorageUnit storageUnit = new StorageUnit(bias_1, bias_2, learning_rate);

        for (int i = 0; i < Layers.size(); ++i) {
            ArrayList<Node> layer = Layers.get(i);

            for (int k = 0; k < layer.size(); ++k) {
                Node node = layer.get(k);
                for (int m = 0; m < layer.get(k).arr_outs.size(); ++m) {
                    Connector connector = node.arr_outs.get(m);
                    storageUnit.arr_weights.add(connector.getWeight());
                }
            }
        }

        return storageUnit;
    }

    public void setParameters(StorageUnit storageUnit) {
        int counter = 0;
        // System.out.println(storageUnit.arr_weights.size());

        for (int i = 0; i < Layers.size(); ++i) {
            ArrayList<Node> layer = Layers.get(i);

            for (int k = 0; k < layer.size(); ++k) {
                Node node = layer.get(k);
                for (int m = 0; m < layer.get(k).arr_outs.size(); ++m) {
                    Connector connector = node.arr_outs.get(m);
                    // System.out.println("counter: "+counter);
                    connector.setWeight(storageUnit.arr_weights.get(counter));
                    ++counter;
                }
            }
        }

        this.bias_1 = storageUnit.bias_1;
     
        this.bias_2 = storageUnit.bias_2;
        System.out.println("bias 2: " + bias_2);
        this.learning_rate = storageUnit.learning_rate;

    }

    public void runTest() {


        ArrayList<Node> inputLayer = Layers.get(0);

        for (int i = 0; i < dataList.size(); ++i) {
            boolean first = true;

            System.out.println("Patient ID: " + dataList.get(i).id);

            Data data = dataList.get(i);

            ArrayList<Float> normalizedData = normalize(data);

            data.setNormalizedData(normalizedData);

            System.out.println(normalizedData);

            int epoch = 0;
            
            for(int x=0;x<1;x++) {
                System.out.println("\033[1;31m" + "EPOCH: " + epoch + "\033[0m");

                // Load data onto the input layer
                for (int j = 1; j < normalizedData.size(); ++j) {
                    inputLayer.get(j - 1).setFn_value(normalizedData.get(j));
                }

                // Calculate values for hidden layers
                for (int k = 1; k < Layers.size() - 1; ++k) {
                    ArrayList<Node> layer = Layers.get(k);
                    // System.out.println("Layer: " + k);

                    for (int m = 0; m < layer.size(); ++m) {
                        Node node = layer.get(m);

                        float n = 0.0F;

                        if (k == 1) {
                            n = node.calculateWeightedSumWithBias(bias_1, dataList.get(i), true);
                        } else {
                            n = node.calculateWeightedSumWithBias(bias_1, dataList.get(i), false);
                        }

                        // Calculate f_n and set it on the node
                        if (n >= 0)
                            node.setFn_value(n);
                        else
                            node.setFn_value(0);

                        
                    }
                }

                System.out.println("bias 1: " + bias_1);
                // System.out.println();

                boolean termination = feedForwardHiddenLastToOutputLayer(data.normalizedData.get(0), data);

                if (termination) {
                    if (first) {
                        ++TP;
                        first = false;
                    }
                
                    continue;
                }

                first = false;
            
            
                ArrayList<Node> outputLayer = Layers.get(Layers.size() - 1);
                float targetOutput = data.normalizedData.get(0);
                float actualOutput = outputLayer.get(0).getFn_value();
        
                // System.out.println("targetOutput: " + targetOutput);
                // System.out.println("actualOutput: " + actualOutput);
        
                float error = targetOutput - actualOutput;
        
                outputLayer.get(0).setFn_value(error);

            
                if (error == -1.0)
                    ++FN;
                else
                    ++FP;

                System.out.println("\033[1;31m" + "error: " + error + "\033[0m");
              


               
               

                ++epoch;
            }
        }
    }
}
