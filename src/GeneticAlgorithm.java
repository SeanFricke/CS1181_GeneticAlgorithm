import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {
    public static ArrayList<Item> readData(String filename) throws IOException {
        ArrayList<Item> items = new ArrayList<>();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] itemArr = line.split(",");
            items.add(new Item(
                    itemArr[0].strip(),
                    Double.parseDouble(itemArr[1].strip()),
                    Integer.parseInt(itemArr[2].strip())
                    )
            );
        }
        return items;
    }
}
