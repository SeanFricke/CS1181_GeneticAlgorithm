import java.util.ArrayList;
import java.util.Random;

public class Chromosome extends ArrayList<Item> implements Comparable<Chromosome> {
    private static Random rng = new Random();
    public Chromosome() {
    }

    public Chromosome(ArrayList<Item> items) {
        for (Item item : items) {
            Item itemCopy = new Item(item.getName(), item.getWeight(), item.getValue());
            itemCopy.setIncluded(rng.nextBoolean());
            add(itemCopy);
        }

    }

    public Chromosome crossover(Chromosome other) {
        Chromosome child = new Chromosome();

        for (int i = 0; i < this.size(); i++) {
            /* Use bool result instead of int because 5 and 5 chance split (50% chance) is the equivalent to a bool
            giving 1 or 0 equally */
          boolean traitParent = rng.nextBoolean();
          Item trait;
          if (traitParent) {
              trait = new Item(this.get(i).getName(), this.get(i).getWeight(), this.get(i).getValue());
          }
          else {
              trait = new Item(other.get(i).getName(), other.get(i).getWeight(), other.get(i).getValue());
          }
          child.add(trait);
        }

        return child;
    }

    public void mutate() {
        for (Item item : this) {
            int mutation = new Random().nextInt(9) + 1;
            if (mutation == 1) {
                item.setIncluded(!item.isIncluded());
            }
        }
    }

    public int getFitness() {
        double totalWeight = 0;
        int totalValue = 0;
        for (Item item : this) {
            totalWeight += item.getWeight();
            totalValue += item.getValue();
        }
        if (totalWeight > 10) {
            return 0;
        } else{
            return totalValue;
        }
    }

    @Override
    public int compareTo(Chromosome other) {
        if (this.getFitness() < other.getFitness()) {
            return -1;
        } else if (this.getFitness() > other.getFitness()) {
            return 1;
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
