import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {
    /**
     * Reads in a file in a CSV format, and parses it into an arrayList of items.
     * @param filename Filename to read data from
     * @return ArrayList of items, that can be turned into a Chromosome.
     * @throws FileNotFoundException File path of name 'filename' is not found or able to be read.
     */
    public static ArrayList<Item> readData(String filename) throws FileNotFoundException {
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

    /**
     * Initialize population using a starter chromosome, and create a population with it, of a certain size.
     * @param items Initial chromosome structure
     * @param populationSize The size of the population of chromosomes.
     * @return An arrayList of chromosomes of size 'populationSize'
     */
    public static ArrayList<Chromosome> initializePopulation(ArrayList<Item> items, int populationSize) {
        ArrayList<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(items));
        }
        return population;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Chromosome initialChromosome = new Chromosome(readData("test.csv"));
        ArrayList<Chromosome> Generation_1 = initializePopulation(initialChromosome, 10);

        ArrayList<Chromosome> Generation_2 = new ArrayList<>();
        for (Chromosome c : Generation_1) {
            Generation_2.add(new Chromosome(c));
        }

        ArrayList<Chromosome> pairOff = new ArrayList<>(Generation_2);
        while (!pairOff.isEmpty()) {
            Random rng = new Random();
            int parent1;
            int parent2;

            parent1 = rng.nextInt(pairOff.size());

            do {
                parent2 = rng.nextInt(pairOff.size());
            } while (parent1 == parent2);

        }
    }
}
