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
        Random rng = new Random();
        Chromosome initialChromosome = new Chromosome(readData("test.csv"));

        // Generation 1 (Original gen)
        ArrayList<Chromosome> Generation_1 = initializePopulation(initialChromosome, 10);

        // Crossover
        ArrayList<Chromosome> pairOff = new ArrayList<>(Generation_1);
        ArrayList<Chromosome> Generation_2 = new ArrayList<>();
        while (!pairOff.isEmpty()) {
            int parent1_idx;
            int parent2_idx;

            parent1_idx = rng.nextInt(pairOff.size());

            do {
                parent2_idx = rng.nextInt(pairOff.size());
            } while (parent1_idx == parent2_idx);

            // Labeling parents
            Chromosome parent1 = pairOff.get(parent1_idx);
            Chromosome parent2 = pairOff.get(parent2_idx);

            Chromosome child = parent1.crossover(parent2); // Crossover
            Generation_2.add(child); // Add child to generation 2 (Crossover gen)

            // Remove from pairing array
            pairOff.remove(parent1_idx);
            pairOff.remove(parent2_idx);
        }

        // Gen 3 (Mutation gen)
        ArrayList<Chromosome> Generation_3 = new ArrayList<>();
        for (Chromosome c : Generation_2) {
            Generation_3.add(new Chromosome(c));
        }

        // Mutation of 10% population
        int mutationAmount = Generation_3.size() / 10; // Dividing by 10 and flooring it.
        int targetChromosome_idx; // Init for active target
        ArrayList<Integer> targetChromosome_idx_LAST = new ArrayList<>(); // Init for list of previous targets

        // Do n number of times. (N = number of chromosomes to expose)
        for (int i = 0; i < mutationAmount; i++) {
            // Randomly pick a target index that has NOT been previously chosen.
            do {
                targetChromosome_idx = rng.nextInt(Generation_3.size());
            } while (targetChromosome_idx_LAST.contains(targetChromosome_idx));

            Chromosome targetChromosome = Generation_3.get(targetChromosome_idx); // Get chromosome at target index
            targetChromosome.mutate(); // Mutate
            targetChromosome_idx_LAST.add(targetChromosome_idx); // Remember this index as already mutated.
        }

        Generation_3.sort(Chromosome::compareTo);
        System.out.println(Generation_3);
    }
}
