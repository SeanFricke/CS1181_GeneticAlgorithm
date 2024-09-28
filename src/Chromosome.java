import java.util.ArrayList;
import java.util.Random;

/**
 * Chromosome class, for storing a list of items, and included training functions such as
 * crossover of two Chromosomes, or mutation for a single one.
 */
public class Chromosome extends ArrayList<Item> implements Comparable<Chromosome> {
    private static Random rng = new Random();
    /**
     * Construct an empty chromosome.
     */
    public Chromosome() {
    }

    /**
     * Construct a chromosome using an ArrayList of items as its structure.
     * @param items Structure template for the new chromosome.
     */
    public Chromosome(ArrayList<Item> items) {
        for (Item item : items) {
            Item itemCopy = new Item(item.getName(), item.getWeight(), item.getValue());
            itemCopy.setIncluded(rng.nextBoolean());
            add(itemCopy);
        }
    }

    /**
     * Create a 'child' chromosome between two parents (this chromosome, and one other)
     * @param other Second chromosome object - along with this chromosome - that will give some of their traits
     *              to the child.
     * @return Child chromosome with random traits from each parent.
     */
    public Chromosome crossover(Chromosome other) {
        Chromosome child = new Chromosome();

        for (int i = 0; i < this.size(); i++) {
            /* Use bool result instead of int because 5 and 5 chance split (50% chance) is the equivalent to a bool
            giving 1 or 0 equally */
          boolean traitParent = rng.nextBoolean();
          Item trait;
          if (traitParent) {
              trait = new Item(this.get(i).getName(), this.get(i).getWeight(), this.get(i).getValue());
              trait.setIncluded(this.get(i).isIncluded());
          }
          else {
              trait = new Item(other.get(i).getName(), other.get(i).getWeight(), other.get(i).getValue());
              trait.setIncluded(other.get(i).isIncluded());
          }
          child.add(trait);
        }
        return child;
    }

    /**
     * Give this chromosome random inverses of traits at a rare chance.
     */
    public void mutate() {
        for (Item item : this) {
            int mutation = new Random().nextInt(9) + 1;
            if (mutation == 1) {
                item.setIncluded(!item.isIncluded());
            }
        }
    }

    /**
     * Get the fitness of this chromosome, when evaluated in the bin packing problem.
     * This can be used to find the accuracy of the chromosome 'model'.
     * @return Value of all included items, zero if included items are over 10 lbs.
     */
    public int getFitness() {
        double totalWeight = 0;
        int totalValue = 0;
        for (Item item : this) {
            if (item.isIncluded()) {
                totalWeight += item.getWeight();
                totalValue += item.getValue();
            }
        }
        if (totalWeight > 10) {
            return 0;
        } else{
            return totalValue;
        }
    }

    /**
     * Compare two Chromosomes' fitness together.
     * @param other the object to be compared.
     * @return -1 for less fit, 1 for more fit, 0 for same fitness.
     */
    @Override
    public int compareTo(Chromosome other) {
        if (this.getFitness() < other.getFitness()) {
            return 1;
        } else if (this.getFitness() > other.getFitness()) {
            return -1;
        } else {
            return 0;
        }
    }

    public String toString() {
        String output = "";
        for (Item item : this) {
            if (item.isIncluded()) {
                output += item + "\n";
            }
        }
        output += "Fitness: " + this.getFitness() + "\n";
        return output;
    }
}
