import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadDataSetValues 
{
    ArrayList<ProblemSet> dataSets =  new ArrayList<>();

    public void CreateDataSet(String DataSetName, String dir,int index) throws NumberFormatException, IOException
    {


        ReadOptimumValues optimumValues = new ReadOptimumValues();
        optimumValues.ReadExcelFile();

        // System.out.p/rintln(optimumValues.Optimum_Values.get(0).ProblemInstances.get(0).Known_Opt_Value);


        File f = new File(dir);
        dataSets.add(new ProblemSet(DataSetName)); 


       
        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = f.listFiles(textFilter);
        int i = 0;
        for (File file : files) {
            File file1 = new File(dir+file.getName());
          
           dataSets.get(index).ProblemInstances.add(new ProblemInstance(file.getName()));
         
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) 
            {
                String st;
                int count = 0;
                dataSets.get(index).ProblemInstances.get(i).name = file1.getName();
                while ((st = br.readLine()) != null)
                {

                    // System.out.println(Integer.parseInt(st));
                    if (count == 0) 
                    {
                        dataSets.get(index).ProblemInstances.get(i).items_count = Integer.parseInt(st);
                    }
                    else if (count == 1) 
                    {
                        dataSets.get(index).ProblemInstances.get(i).bin_Size = Integer.parseInt(st);   
                    }
                    else 
                    {
                        dataSets.get(index).ProblemInstances.get(i).ItemsList.add(new items(Integer.parseInt(st)));
                    }
                    
                    count++;
            }
        }
        i++;
        }
       
      


                      
    }
    
    public void GetDataSets() throws NumberFormatException, IOException
    {

        for (int i = 0; i < 9; i++) 
        {
            switch(i)
            {
                case 0:
                    CreateDataSet("Falkenauer_T", "DataSets/Falkenauer/Falkenauer_T/", 0);
           

                break;
                case 1:
                    CreateDataSet("Falkenauer_U", "DataSets/Falkenauer/Falkenauer_U/", 1);
             

                break;
                case 2:
                    CreateDataSet("Hard28", "DataSets/Hard28/", 2);
    

                break;
                case 3:
                    CreateDataSet("Scholl_1", "DataSets/Scholl/Scholl_1/", 3);
         

                break;
                case 4:
                    CreateDataSet("Scholl_2", "DataSets/Scholl/Scholl_2/", 4);
                

                break;
                case 5:
                    CreateDataSet("Scholl_3", "DataSets/Scholl/Scholl_3/", 5);
                 

                break;
                case 6:
                    CreateDataSet("Schwerin_1", "DataSets/Schwerin/Schwerin_1/", 6);
                

                break;
                case 7:
                    CreateDataSet("Schwerin_2", "DataSets/Schwerin/Schwerin_2/", 7);
          

                break;
                case 8:
                    CreateDataSet("Waescher", "DataSets/Waescher/", 8);
                 

                break;
            }
            
               
        }
    }

    public void SetKnownOptimumValues()
    {
      
        ReadOptimumValues optimumValues = new ReadOptimumValues();
        optimumValues.ReadExcelFile();
        for (int i = 0; i < dataSets.size(); i++) 
        {
            for (int j = 0; j < dataSets.get(i).ProblemInstances.size(); j++) 
            {
                for (int k = 0; k < optimumValues.Optimum_Values.size(); k++) 
                {
                    for(int l=0; l<optimumValues.Optimum_Values.get(k).ProblemInstances.size(); l++)
                    {
                        
                        if (dataSets.get(i).ProblemInstances.get(j).name.equals(optimumValues.Optimum_Values.get(k).ProblemInstances.get(l).Problem_Instance+".txt")) 
                        {
                            dataSets.get(i).ProblemInstances.get(j).Known_Opt_binCount_Value = optimumValues.Optimum_Values.get(k).ProblemInstances.get(l).Known_Opt_Value;
                        }
                    }
                    
                }
            }
        }
    }
        
    

}

// for(int ii=0; ii<optimumValues.Optimum_Values.get(index).ProblemInstances.size(); ii++)
// {
//     System.out.println("Problem instatnce name: "+optimumValues.Optimum_Values.get(index).ProblemInstances.get(ii).Problem_Instance);
//     System.out.println("File name name: "+file.getName());
//     String concatinate=optimumValues.Optimum_Values.get(index).ProblemInstances.get(ii).Problem_Instance+".txt";
//     System.out.println("Concatinate: "+concatinate);
//     if(concatinate.equals(file.getName()))
//     { System.out.println("Inside if#####################");
//         dataSets.get(index).ProblemInstances.add(new ProblemInstance(file.getName(), optimumValues.Optimum_Values.get(index).ProblemInstances.get(ii).Known_Opt_Value));
//     }
// }