import java.io.IOException;

public class MainClass
{
    /**
     * @param args
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void main(String[] args) throws NumberFormatException, IOException {
        BinPackingForILS binPackingForILS = new BinPackingForILS();
        binPackingForILS.GoILS();

        TabuSearch tabuSearch = new TabuSearch();
        tabuSearch.GoTabu();

    }
}
