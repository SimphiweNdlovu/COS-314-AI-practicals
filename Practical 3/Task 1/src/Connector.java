public class Connector {

    private float weight;
    public Node src;
    public Node dest;

    Connector(Node src, Node dest,float weight)
    {
        this.src=src;
        this.dest=dest;
        this.weight=weight;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        String out="";

        out=out+"("+src.getId()+") ---<"+getWeight()+">--- ("+dest.getId()+")";

        return out;
    }
}
