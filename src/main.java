import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException {
        ArrayList<Item> items;
        items = GeneticAlgorithm.readData("test.csv");
        for (Item item : items) {
            System.out.println(item.toString());
        }
    }
}
