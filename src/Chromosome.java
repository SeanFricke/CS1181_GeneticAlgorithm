import java.util.ArrayList;
import java.util.Random;

/**
 * CHROMOSOME class, for storing an ArrayList of ITEMS, and included training functions such as
 * crossover of two CHROMOSOMES, or mutation for a single one.
 */
public class Chromosome extends ArrayList<Item> implements Comparable<Chromosome> {
    private static Random rng = new Random(); // RNG init
    /**
     * Construct an empty CHROMOSOME.
     */
    public Chromosome() {
    }

    /**
     * Construct a CHROMOSOME using an ArrayList of ITEMS as its structure.
     * @param items Structure template for the new CHROMOSOME.
     */
    public Chromosome(ArrayList<Item> items) {
        for (Item gene : items) {
            // Copy values that are in constructor
            Item geneCopy = new Item(gene.getName(), gene.getWeight(), gene.getValue());
            geneCopy.setIncluded(rng.nextBoolean()); // Get random INCLUDED state
            add(geneCopy); // Add gene copy to CHROMOSOME
        }
    }

    /**
     * Create a 'child' CHROMOSOME between two parents (this CHROMOSOME, and one other)
     * @param other Second CHROMOSOME object - along with this CHROMOSOME - that will give some of their traits
     * (INCLUDED value) to the child.
     * @return Child CHROMOSOME with random traits (INCLUDED value) from each parent.
     */
    public Chromosome crossover(Chromosome other) {
        Chromosome child = new Chromosome();

        for (int i = 0; i < this.size(); i++) {
            /* Use bool result instead of int because 5 and 5 chance split (50% chance) is the equivalent to a bool
            giving 1 or 0 equally */
          boolean traitParent = rng.nextBoolean();

          Item gene; // Target gene
          if (traitParent) {
              // Make item with constructor needed fields, then sets INCLUDED separately.
              gene = new Item(this.get(i).getName(), this.get(i).getWeight(), this.get(i).getValue());
              gene.setIncluded(this.get(i).isIncluded());
          }
          else {
              // Make item with constructor needed fields, then sets INCLUDED separately.
              gene = new Item(other.get(i).getName(), other.get(i).getWeight(), other.get(i).getValue());
              gene.setIncluded(other.get(i).isIncluded());
          }
          child.add(gene); // Add gene to child CHROMOSOME.
        }
        return child;
    }

    /**
     * Give this CHROMOSOME random inverses of traits (INCLUDED value) at a rare chance.
     */
    public void mutate() {
        for (Item gene : this) {
            int mutation = new Random().nextInt(9) + 1; // Pick a random Int 1-10
            if (mutation == 1) {
                gene.setIncluded(!gene.isIncluded()); // Mutate if 1 was picked.
            }
        }
    }

    /**
     * Get the fitness of this CHROMOSOME, when evaluated in the bin packing problem.
     * This can be used to find the accuracy of the CHROMOSOME 'model'.
     * @return Value of all included ITEMS, zero if included ITEMS are over 10 lbs.
     */
    public int getFitness() {
        // Total var inits
        double totalWeight = 0;
        int totalValue = 0;

        for (Item gene : this) {
            if (gene.isIncluded()) {
                totalWeight += gene.getWeight(); // If INCLUDED, add on its WEIGHT to total.
                totalValue += gene.getValue(); // If INCLUDED, add on its VALUE to total.
            }
        }

        if (totalWeight > 10) {
            return 0; // If total WEIGHT is over 10 lbs, fitness is 0.
        } else{
            return totalValue; // If total WEIGHT is 10 lbs or less, fitness is the total VALUE.
        }
    }

    /**
     * Compare two CHROMOSOMES' fitness together.
     * @param other the CHROMOSOME to be compared.
     * @return -1 for less fit, 1 for more fit, 0 for same fitness.
     */
    @Override
    public int compareTo(Chromosome other) {
        if (this.getFitness() < other.getFitness()) {
            return 1; // If less than OTHER
        } else if (this.getFitness() > other.getFitness()) {
            return -1; // If greater than OTHER
        } else {
            return 0; // If equal to OTHER
        }
    }

    /**
     * toString for the CHROMOSOME info.
     * @return [ITEM toString for every ITEM] Fitness: [Fitness]
     */
    public String toString() {
        String output = ""; // Empty String init
        for (Item gene : this) {
            if (gene.isIncluded()) {
                output += gene + "\n"; // Add the toString of every ITEM that is INCLUDED.
            }
        }
        output += "Fitness: " + this.getFitness() + "\n"; // Add the fitness to the end of the String
        return output;
    }
}
