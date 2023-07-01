import java.util.ArrayList;

public class Node {
    private Data data;

    public ArrayList<Connector> arr_ins=new ArrayList<>();
    public ArrayList<Connector> arr_outs=new ArrayList<>();

    private float fn_value=0;

    private String id;

    private int id_num;

    private float inputRack;

    Node(Data data,int id_num,Discriminate discriminate)
    {
       this.data=new Data(data.id,data.age,data.menopause,data.tumor_size,data.inv_nodes,data.node_caps,data.deg_malig,data.breast,data.breast_quad,data.irradiat,data.out);
       this.id_num=id_num;
       this.id=discriminate+String.valueOf(id_num);
    }

    //for the hidden layer
    Node(float fn_value,int id_num,Discriminate discriminate)
    {
        this.fn_value=fn_value;
        this.id_num=id_num;
        this.id=discriminate+String.valueOf(id_num);
    }


    public void setFn_value(float fn_value) {
        this.fn_value = fn_value;
    }

    public float getFn_value() {
        return fn_value;
    }

    public Data getData() {
        return data;
    }



    public float calculateWeightedSumWithBias(float bias,Data data,boolean start)
    {
        float sum=0.0F;

        if(start) //input-to-hidden
        {
            for(int i=0;i<arr_ins.size();++i)
            {
                //System.out.println(arr_ins.get(i).getWeight());
                sum=sum+(bias+arr_ins.get(i).getWeight()*data.normalizedData.get(id_num+1));
            }
        }
        else //hidden-to-hidden
        {
            for(int i=0;i<arr_ins.size();++i)
            {
                float hiddenLayerNode_fn_value=arr_ins.get(i).src.fn_value;
                sum=sum+(bias+arr_ins.get(i).getWeight()*hiddenLayerNode_fn_value);
            }
        }

        return sum;
    }

    public float sumWeightBiasInsForOutputs(float bias)
    {
        float sum=0.0F;

        for(int i=0;i<arr_ins.size();++i)
        {
            float hiddenLayerNode_fn_value=arr_ins.get(i).src.fn_value;
            sum=sum+(bias+arr_ins.get(i).getWeight()*hiddenLayerNode_fn_value);
        }

        return sum;
    }

    public float sumWeightBiasOuts(float bias,Data data)
    {
        float sum=0.0F;

        for(int i=0;i<arr_outs.size();++i)
        {
            sum=sum+(bias+arr_outs.get(i).getWeight()*data.normalizedData.get(id_num+1));
        }

        return sum;
    }

    /**
     *
     * @param connector
     * @param type 'in' or 'out'
     */
    public void connect(Connector connector,String type){
        if(type.equals("in"))
            arr_ins.add(connector);
        else
            arr_outs.add(connector);
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", fn_value=" + fn_value +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }
}
