import java.util.ArrayList;
import java.util.List;

public class ProblemInstance {
    public String name;
    public int itemCount;
    public int knapsackCapacity;
    public ArrayList<Item> itemList = new ArrayList<>();
    private int optimumValue;

    public ProblemInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getKnapsackCapacity() {
        return knapsackCapacity;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public int getOptimumValue() {
        return this.optimumValue;
    }
}