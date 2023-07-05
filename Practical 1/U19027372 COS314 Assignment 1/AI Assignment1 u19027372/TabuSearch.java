import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import java.util.Random;

public class TabuSearch {
    
    private static final int MAX_ITERATIONS = 1000;
    private static final int TABU_TENURE = 10;
    
    
    public String name;

    public int max_bin_Size;
    public int known_optimum;
    public int local_optimum; 

    public ArrayList<Integer> ItemsList=new ArrayList<Integer>();
    
   void GoTabu() {
      
    
        ReadDataSetValues datasetValues = new ReadDataSetValues();
        try {
            datasetValues.GetDataSets();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            datasetValues.SetKnownOptimumValues();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        System.out.println("\033[1;32m---------------------------------STARTING THE TABU  SEARCH ALGORITHM---------------------------------\033[0m" );

        // System.out.println("---------------------------------STARTING THE TABU  SEARCH ALGORITHM---------------------------------" );
        System.out.println();
        for(int i=0;i<datasetValues.dataSets.size();i++)
        {
            
            int Opimum=0;
            int one_bin_from_the_optimum=0;
            int two_bin_from_the_optimum=0;
            int three_bin_from_the_optimum=0;

            int four_bin_from_the_optimum=0;
            int five_bin_from_the_optimum=0;
            int six_bin_from_the_optimum=0;
            int seven_bin_from_the_optimum=0;
            int eight_bin_from_the_optimum=0;
            long elapsedTime = 0;
          

            // System.out.println("datasetValues.dataSets.get(i).ProblemSetName  SIZE : "+datasetValues.dataSets.get(i).ProblemInstances.size());
          
            for(int ii=0;ii<datasetValues.dataSets.get(i).ProblemInstances.size();ii++){
                long start = System.nanoTime();    
               
                elapsedTime += System.nanoTime() - start;
                // System.out.println("ProblemInstances name: "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).name);
                // System.out.println("ProblemInstances Bin Size: "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).bin_Size);
                // System.out.println("ProblemInstances items_count : "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).items_count);
                // System.out.println("ProblemInstances ItemsList size() : "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.size());
                
                // System.out.println("ProblemInstances ItemsList  : ");
                // for(int iii=0;iii<datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.size();iii++){
                //     System.out.print(datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.get(iii).value+" , ");
                // }
                // System.out.println();

                // items=datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList;
                ArrayList<Integer> items = new ArrayList<Integer>();
                // items=datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList;
                for(int iii=0;iii<datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.size();iii++){
                    // System.out.print(datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.get(iii).value+" , ");
                    items.add(datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.get(iii).value);
                }
                // System.out.println("Items List  :: "+items);
                max_bin_Size=datasetValues.dataSets.get(i).ProblemInstances.get(ii).bin_Size;
                ItemsList=items;
                int KnownOptimum=datasetValues.dataSets.get(i).ProblemInstances.get(ii).Known_Opt_binCount_Value;
                GoTabuSearch(max_bin_Size, items);
                // List<List<Integer>> solution = solve(items, binSize);
                // System.out.println("Solution: " + solution);
                // System.out.println("Num Bin used: " + local_optimum);
                if( KnownOptimum==(  local_optimum))
                {
                    Opimum++;
                }
                if( KnownOptimum +1==local_optimum)
                {
                    one_bin_from_the_optimum++;
                }
                if( KnownOptimum+2==local_optimum)
                {
                    two_bin_from_the_optimum++;
                }
                if( KnownOptimum+3==local_optimum)
                {
                    three_bin_from_the_optimum++;
                }
                if( KnownOptimum+4==local_optimum)
                {
                    four_bin_from_the_optimum++;
                }
                if( KnownOptimum+5==local_optimum)
                {
                    five_bin_from_the_optimum++;
                }
                if( KnownOptimum+6==local_optimum)
                {
                    six_bin_from_the_optimum++;
                }   
                if( KnownOptimum+7==local_optimum)
                {
                    seven_bin_from_the_optimum++;
                }
                if( KnownOptimum+8==local_optimum)
                {
                    eight_bin_from_the_optimum++;
                }

                // System.out.println("Known Opt bins used Value : "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).Known_Opt_binCount_Value);
                // System.out.println();
               
            }
            //
            System.out.println("--------------------------------ProblemSetName:  "+datasetValues.dataSets.get(i).ProblemSetName +"--------------------------------");
            System.out.println("ELAPSED TIME: "+ (elapsedTime/datasetValues.dataSets.get(i).ProblemInstances.size()) + " ns");
       
            System.out.println("Opimum: "+Opimum);
            System.out.println("one bin from the optimum: "+one_bin_from_the_optimum);
            System.out.println("two bin from the optimum: "+two_bin_from_the_optimum);
            System.out.println("three bin from the optimum: "+three_bin_from_the_optimum);
            System.out.println("four bin from the optimum: "+four_bin_from_the_optimum);
            System.out.println("five bin from the optimum: "+five_bin_from_the_optimum);
            System.out.println("six bin from_the optimum: "+six_bin_from_the_optimum);
            System.out.println("seven bin from the optimum: "+seven_bin_from_the_optimum);
            System.out.println("eight bin from the optimum: "+eight_bin_from_the_optimum);
            System.out.println("-----------------------------------------------------------------------------------------------------------------");
        }

    }

    public void GoTabuSearch(int bin_Size, ArrayList<Integer> ItemsList) {
        // FIRST FIT ALGORITHM
        local_optimum = bin_summing();
    
        LinkedList<ArrayList<Integer>> tabu_list = new LinkedList<>();
    
        for (int i = 0; i < MAX_ITERATIONS;) {
            Random rand = new Random();
    
            Collections.swap(ItemsList, rand.nextInt(ItemsList.size()), rand.nextInt(ItemsList.size()));
    
            Boolean exists_in_tabu_list = false;
    
            for (int x = 0; x < tabu_list.size(); x++) {
                if (tabu_list.get(x).equals(ItemsList)) {
                    exists_in_tabu_list = true;
                    break;
                }
            }
    
            if (!exists_in_tabu_list) {
                tabu_list.add(new ArrayList<>(ItemsList));
    
                if (tabu_list.size() > TABU_TENURE) {
                    tabu_list.removeFirst();
                }
    
                int new_optimum = bin_summing();
                if (new_optimum < local_optimum) {
                    local_optimum = new_optimum;
                }
    
                i++;
            }
        }
    }
    


    public int bin_summing() {
        int number_of_bins = 0;
        int remaining_bin_size = max_bin_Size;
    
        for (int i = 0; i < ItemsList.size(); i++) {
            if (ItemsList.get(i) > remaining_bin_size) {
                number_of_bins++;
                remaining_bin_size = max_bin_Size;
            }
            remaining_bin_size -= ItemsList.get(i);
        }
    
        if (remaining_bin_size < max_bin_Size) {
            number_of_bins++;
        }
    
        return number_of_bins;
    }
    
   
   
}
