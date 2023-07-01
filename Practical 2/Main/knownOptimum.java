import java.util.ArrayList;

public class knownOptimum {

    String Filename;
    double Optimum;

    public knownOptimum(String Filename, double Optimum) {
        this.Filename = Filename;
        this.Optimum = Optimum;
    }
    public knownOptimum() {
    }
    public ArrayList<knownOptimum> knownOptimums = new ArrayList<>();

    public void createKnownOptimums() {
        knownOptimums.add(new knownOptimum("f1_l-d_kp_10_269", 295));
        knownOptimums.add(new knownOptimum("f2_l-d_kp_20_878", 1024));
        knownOptimums.add(new knownOptimum("f3_l-d_kp_4_20", 35));
        knownOptimums.add(new knownOptimum("f4_l-d_kp_4_11", 23));
        knownOptimums.add(new knownOptimum("f5_l-d_kp_15_375", 4810694));
        knownOptimums.add(new knownOptimum("f6_l-d_kp_10_60", 52));
        knownOptimums.add(new knownOptimum("f7_l-d_kp_7_50", 107));
        knownOptimums.add(new knownOptimum("knapPI_1_100_1000_1", 9147));
        knownOptimums.add(new knownOptimum("f8_l-d_kp_23_10000", 9767));
        knownOptimums.add(new knownOptimum("f9_l-d_kp_5_80", 130));
        knownOptimums.add(new knownOptimum("f10_l-d_kp_20_879", 1025));

    }
}


