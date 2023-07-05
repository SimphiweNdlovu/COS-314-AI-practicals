import java.io.IOException;
import java.util.*;

public class BinPackingForILS {
    static int[] items = {}; // items to be packed
    static int binCapacity ; // capacity of each bin
    static int[] bins; // array to store bins

    void  GoILS() {
        int maxIterations = 100; // maximum number of iterations
        int maxPerturbations = 100; // maximum number of perturbations per iteration

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
        
      
        System.out.println("\033[1;31m---------------------------------STARTING THE ITERATED LOCAL SEARCH ALGORITHM---------------------------------\033[0m" );

        System.out.println();
        for(int i=0;i<datasetValues.dataSets.size();i++)
        {
            // System.out.println("ProblemSetName:  "+datasetValues.dataSets.get(i).ProblemSetName);
            // System.out.println("datasetValues.dataSets.get(i).ProblemSetName  SIZE : "+datasetValues.dataSets.get(i).ProblemInstances.size());
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
          
            for(int ii=0;ii<datasetValues.dataSets.get(i).ProblemInstances.size();ii++){
                long start = System.nanoTime();    
               
                elapsedTime += System.nanoTime() - start;
    
                items=new int[datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.size()];

                for(int iii=0;iii<datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.size();iii++){
                    // System.out.print(datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.get(iii).value+" , ");
                    items[iii]=datasetValues.dataSets.get(i).ProblemInstances.get(ii).ItemsList.get(iii).value;
                }
                binCapacity=datasetValues.dataSets.get(i).ProblemInstances.get(ii).bin_Size;
                int local_optimum = packItems(maxIterations, maxPerturbations); // pack items into bins
                int KnownOptimum=datasetValues.dataSets.get(i).ProblemInstances.get(ii).Known_Opt_binCount_Value;
                // System.out.println("Number of bins used: " + local_optimum);
                // System.out.println("Known Opt bins used Value : "+datasetValues.dataSets.get(i).ProblemInstances.get(ii).Known_Opt_binCount_Value);
            

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

            }
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

    static int packItems(int maxIterations, int maxPerturbations) {
        int numBins = Integer.MAX_VALUE;
        bins = new int[items.length];
        int[] bestBins = new int[items.length];
        int bestNumBins = Integer.MAX_VALUE;
        int numIterations = 0;
        while (numIterations < maxIterations) {
            numIterations++;
            perturbSolution(maxPerturbations);
            localSearch();
            int curNumBins = countBins();
            if (curNumBins < numBins) {
                numBins = curNumBins;
                System.arraycopy(bins, 0, bestBins, 0, bins.length);
            }
            if (curNumBins < bestNumBins) {
                bestNumBins = curNumBins;
            }
            if (curNumBins == 1) { // stop if a feasible solution is found
                break;
            }
        }
        System.arraycopy(bestBins, 0, bins, 0, bins.length);
        return bestNumBins;
    }

    static void perturbSolution(int maxPerturbations) {
        Random random = new Random();
        for (int i = 0; i < maxPerturbations; i++) {
            int idx1 = random.nextInt(items.length);
            int idx2 = random.nextInt(items.length);
            int temp = items[idx1];
            items[idx1] = items[idx2];
            items[idx2] = temp;
        }
    }
    //local search algorithm
    static void localSearch() {
        Arrays.fill(bins, 0);
        for (int i = 0; i < items.length; i++) {
            int j = findBestBin(items[i]);
            bins[j] += items[i];
        }
    }
//searches for the bin that has the most available space for a given item. 
//It takes as input the size of an item and returns the index of the bin with the most available space.
    static int findBestBin(int itemSize) {
        int minBin = -1;
        int minSpace = Integer.MAX_VALUE;
        for (int i = 0; i < bins.length; i++) {
            int space = binCapacity - bins[i];
            if (itemSize <= space && space < minSpace) {
                minBin = i;
                minSpace = space;
            }
        }
        if (minBin == -1) {//it means that there is no bin with enough available space for the item, thus call findLeastFullBin() to find the least full bin.
            minBin = findLeastFullBin();
        }
        return minBin;
    }
//find the least full bin (i.e., the bin with the smallest remaining capacity),
// and the item is assigned to this bin.
    static int findLeastFullBin() {
        int minBin = 0;
        int minSpace = Integer.MAX_VALUE;
        for (int i = 0; i < bins.length; i++) {
            int space   = binCapacity - bins[i];
            if (space < minSpace) {
                minBin = i;
                minSpace = space;
            }
        }
        return minBin;
    }
    //count the number of bins that are currently in use (i.e., bins that are greator than 0)
    static int countBins() {
        int numBins = 0;
        for (int i = 0; i < bins.length; i++) {
            if (bins[i] > 0) {
                numBins++;
            }
        }
        return numBins;
    }
    
    }

   