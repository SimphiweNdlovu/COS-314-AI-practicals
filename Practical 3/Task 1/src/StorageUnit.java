import java.util.ArrayList;

public class StorageUnit {
    public float bias_1;
    public float bias_2;

    public float learning_rate;

    public ArrayList<Float> arr_weights;

    StorageUnit(float b_1,float b_2,float l_r)
    {
        bias_1=b_1;
        bias_2=b_2;
        learning_rate=l_r;

        arr_weights=new ArrayList<>();
    }

    @Override
    public String toString() {
        printWeights();
        return "StorageUnit{" +
                "bias_1= " + bias_1 +
                " Storage Unit size= " + arr_weights.size() +
                ", bias_2= " + bias_2 +
                ", learning_rate=" + learning_rate +
                '}';
    }

    public void printWeights()
    {
        System.out.println("Weights: ");
        for(int i=0;i<arr_weights.size();++i)
        {
            System.out.println(arr_weights.get(i));
        }
    }
}
