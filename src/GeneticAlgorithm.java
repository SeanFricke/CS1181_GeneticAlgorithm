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
        // Inits
        ArrayList<Item> items = new ArrayList<>();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        
        // Read File
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine(); // Get next line (record) of the csv format
            String[] itemArr = line.split(","); // Split it between the commas
            items.add(new Item(
                    itemArr[0].strip(), // Use the stripped first value as the NAME
                    Double.parseDouble(itemArr[1].strip()), // Use the stripped second value as the WEIGHT
                    Integer.parseInt(itemArr[2].strip()) // Use the stripped third value as the VALUE
                    )
            ); // Make new ITEM from csv record, and add it to ArrayList
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
        ArrayList<Chromosome> population = new ArrayList<>(); // Init
        
        // Construct a new CHROMOSOME from ITEMS template, add to population array, and repeat 'populationSize' times.
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(items));
        }
        return population;
    }

    /**
     * Driver for the algorithm that creates a population, randomly pairs them off for crossover, implements a
     * 10% chance for the child CHROMOSOME to be exposed to mutation (along with the 10% chance a given gene will
     * mutate), and finally filters by the top 10 fittest CHROMOSOMES of the generation, and clears the rest,
     * repeating for 19 more generations.
     *
     * NOTE: I feel as if this method of genetic evolution has the major flaw of getting stuck in the
     * 'false' or local minimum problem. This is sorta like gradient decent, to find a minimum error
     * (in this case max value, so think of it in reverse) or optimized solution for a set of arguments
     * (weight and value in this case). After a couple generations, the concept that we are 'inbreeding' the
     * populations over many generations, soon starts to affect the amount of change we get. This can slow it to
     * what can be seen as a local or 'false' minimum (maximum in this case due to needing the highest value) of the
     * problem. The mutations compensate for that somewhat, but may need tweaked or made more impactful to push the
     * 'species' out of the pit (local minimum) to find the true optimized solution. With the small data set we have,
     * it may not be a problem -especially seeing it's just a project-, but I had seen drastic varying top values
     * after my runs of 20 generations (2900-3400) and had that thought in mind.
     *
     * @throws FileNotFoundException Cannot find ITEM template data in csv form.
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Init
        Random rng = new Random();
        Chromosome initialChromosome = new Chromosome(readData("test.csv"));
        
        // Create initial population, as well as next generation
        ArrayList<Chromosome> currentGeneration = initializePopulation(initialChromosome, 10);
        ArrayList<Chromosome> nextGeneration = new ArrayList<>();
        
        // Do for 20 generations
        for (int j = 0; j < 20; j++) {
            
            // For all population in 'currentGeneration'
            for (Chromosome chromosome : currentGeneration) {
                Chromosome tempChromosome = new Chromosome(); // Init

                for (Item gene : chromosome) {
                    // Copy values that are in constructor
                    Item geneCopy = new Item(gene.getName(), gene.getWeight(), gene.getValue());
                    geneCopy.setIncluded(gene.isIncluded()); // Copy INCLUDED state
                    tempChromosome.add(geneCopy); // Add gene copy to tempChromosome
                }
                nextGeneration.add(tempChromosome);
            }

            // Crossover
            // Init
            int pairOffSize = nextGeneration.size();
            ArrayList<Integer> pairOff_LAST = new ArrayList<>();

            // For as many pairs as can be made in nextGeneration
            for (int i = 0; i < pairOffSize / 2; i++) {
                // Init
                int parent1_idx;
                int parent2_idx;

                // Find a random first parent that has yet to be paired
                do {
                    parent1_idx = rng.nextInt(pairOffSize);
                } while (pairOff_LAST.contains(parent1_idx));

                // Find a random second parent that has yet to be paired AND is not the same as first parent
                do {
                    parent2_idx = rng.nextInt(pairOffSize);
                } while (parent1_idx == parent2_idx || pairOff_LAST.contains(parent2_idx));

                // Labeling parents
                Chromosome parent1 = nextGeneration.get(parent1_idx);
                Chromosome parent2 = nextGeneration.get(parent2_idx);

                Chromosome child = parent1.crossover(parent2); // Crossover
                nextGeneration.add(child); // Add child to nextGeneration

                // Add both parents to list of already paired CHROMOSOMES.
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

            nextGeneration.sort(null); // Sort nextGeneration using CHROMOSOME compareTo method
            currentGeneration.clear(); // wipe currentGeneration

            int topContenders;
            if (nextGeneration.size() < 10) {
                /* IF somehow nextGeneration has a population of less than 10
                (Which shouldn't be possible given we always take 10 and only ADD to it),
                take the max amount possible to the next generation instead.
                 */
                topContenders = nextGeneration.size();
            } else {
                topContenders = 10; // Else, use the default top 10
            }

            /*  Iterator though the top 10 fittest CHROMOSOMES in the generation,
                making a deep copy to put into currentGeneration */
            for (Chromosome chromosome : nextGeneration.subList(0, topContenders - 1)) {
                Chromosome tempChromosome = new Chromosome();
                for (Item gene : chromosome) {
                    Item geneCopy = new Item(gene.getName(), gene.getWeight(), gene.getValue());
                    geneCopy.setIncluded(gene.isIncluded());
                    tempChromosome.add(geneCopy);
                }
                currentGeneration.add(tempChromosome);
            }
            nextGeneration.clear();
        }

        // Print the highest valued CHROMOSOME at the end of 20 generations.
        System.out.printf("\n ------ Fittest individual ------\n%s", currentGeneration.get(0).toString());
    }
}
