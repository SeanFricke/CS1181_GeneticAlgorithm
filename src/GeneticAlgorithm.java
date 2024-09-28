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

    /**
     * Driver for the algorith that creates a population, randomly pairs them off for crossover, implements a
     * 10% chance for the child chromosome to be exposed to mutation (along with the 10% chance a given gene will
     * mutate), and finally filters by the top 10 fittest Chromosomes of the generation, and clears the rest,
     * repeating for 19 more generations.
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Random rng = new Random();
        Chromosome initialChromosome = new Chromosome(readData("test.csv"));

        ArrayList<Chromosome> currentGeneration = initializePopulation(initialChromosome, 10);
        ArrayList<Chromosome> nextGeneration = new ArrayList<>();

        for (int j = 0; j < 20; j++) {
            for (Chromosome chromosome : currentGeneration) {
                Chromosome tempChromosome = new Chromosome();
                for (Item item : chromosome) {
                    Item itemCopy = new Item(item.getName(), item.getWeight(), item.getValue());
                    itemCopy.setIncluded(item.isIncluded());
                    tempChromosome.add(itemCopy);
                }
                nextGeneration.add(tempChromosome);
            }

            // Crossover
            int pairOffSize = nextGeneration.size();
            ArrayList<Integer> pairOff_LAST = new ArrayList<>();
            for (int i = 0; i < pairOffSize / 2; i++) {
                int parent1_idx;
                int parent2_idx;

                do {
                    parent1_idx = rng.nextInt(pairOffSize);
                } while (pairOff_LAST.contains(parent1_idx));

                do {
                    parent2_idx = rng.nextInt(pairOffSize);
                } while (parent1_idx == parent2_idx && pairOff_LAST.contains(parent2_idx));

                // Labeling parents
                Chromosome parent1 = nextGeneration.get(parent1_idx);
                Chromosome parent2 = nextGeneration.get(parent2_idx);

                Chromosome child = parent1.crossover(parent2); // Crossover
                nextGeneration.add(child); // Add child to nextGeneration

                pairOff_LAST.add(parent1_idx);
                pairOff_LAST.add(parent2_idx);
            }

            // Mutation of 10% population
            int mutationAmount = nextGeneration.size() / 10; // Dividing by 10 and flooring it.

            int targetChromosome_idx; // Init for active target
            ArrayList<Integer> targetChromosome_idx_LAST = new ArrayList<>(); // Init for list of previous targets

            // Do n number of times. (N = number of chromosomes to expose)
            for (int i = 0; i < mutationAmount; i++) {
                // Randomly pick a target index that has NOT been previously chosen.
                do {
                    targetChromosome_idx = rng.nextInt(nextGeneration.size());
                } while (targetChromosome_idx_LAST.contains(targetChromosome_idx));

                Chromosome targetChromosome = nextGeneration.get(targetChromosome_idx); // Get chromosome at target index
                targetChromosome.mutate(); // Mutate
                targetChromosome_idx_LAST.add(targetChromosome_idx); // Remember this index as already mutated.
            }

            nextGeneration.sort(null);
            currentGeneration.clear();
            int topContenders;
            if (nextGeneration.size() < 10) {
                topContenders = nextGeneration.size();
            } else {
                topContenders = 10;
            }

            for (Chromosome chromosome : nextGeneration.subList(0, topContenders - 1)) {
                Chromosome tempChromosome = new Chromosome();
                for (Item item : chromosome) {
                    Item itemCopy = new Item(item.getName(), item.getWeight(), item.getValue());
                    itemCopy.setIncluded(item.isIncluded());
                    tempChromosome.add(itemCopy);
                }
                currentGeneration.add(tempChromosome);
            }
            nextGeneration.clear();
        }

        System.out.printf("\n ------ Fittest individual ------\n%s", currentGeneration.get(0).toString());
    }
}
