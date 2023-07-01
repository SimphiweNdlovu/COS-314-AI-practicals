import java.util.ArrayList;

public class Data {

    public String age;
    public String menopause;
    public String tumor_size;
    public String inv_nodes;
    public String node_caps;
    public int deg_malig;
    public String breast;

    public String breast_quad;
    public String out;

    public String irradiat;

    public int id;

    public ArrayList<Float> normalizedData=new ArrayList<>();

    Data(int id,String age,String men,String tumor_size,String inv_nodes,String node_caps,int deg_malig,String breast,String breast_quad,String irradiat,String out)
    {
        this.id=id;
        this.age=age;
        this.menopause=men;
        this.tumor_size=tumor_size;
        this.inv_nodes=inv_nodes;
        this.node_caps=node_caps;
        this.deg_malig=deg_malig;
        this.breast=breast;
        this.irradiat=irradiat;
        this.breast_quad=breast_quad;
        this.out=out;
    }

    //recurrence-events,30-39,premeno,30-34,0-2,no,2,left,left_up,no
    public void setNormalizedData(ArrayList<Float> arr) {
        for(int i=0;i<arr.size();++i)
        {
            normalizedData.add(arr.get(i));
        }
    }



    @Override
    public String toString() {
        return "Data{" +
                "age=" + age +
                ", menopause=" + menopause +
                ", tumor-size=" + tumor_size +
                ", inv-nodes=" + inv_nodes +
                ", node-caps=" + node_caps +
                ", deg-malig=" + deg_malig +
                ", breast=" + breast +
                ", breast-quad=" + breast_quad +
                ", irradiat=" + irradiat +
                ", out= "+ out+
                '}';
    }
}
