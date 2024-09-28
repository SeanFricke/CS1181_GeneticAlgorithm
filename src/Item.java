public class Item {
    // Field Inits
    private final String name;
    private final double weight;
    private final int value;
    private boolean included;


    /**
     * Constructor for ITEM that takes in explicit values as args.
     * @param name WEIGHT of ITEM.
     * @param weight Weight of ITEM.
     * @param value Monetary value of ITEM.
     */
    public Item(String name, double weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.included = false;
    }

    /**
     * Constructor for ITEM that takes in another ITEM as template, and copies it.
     * @param other Template ITEM to copy.
     */
    public Item(Item other){
        this.name = other.name;
        this.weight = other.weight;
        this.value = other.value;
    }

    /**
     * Setter for the INCLUDED field to dictate whether the ITEM is taken in that scenario (Chromosome).
     * @param included Boolean to set for INCLUDED field.
     */
    public void setIncluded(boolean included) {
        this.included = included;
    }

    /**
     * Getter for ITEM WEIGHT
     * @return ITEM WEIGHT as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for ITEM WEIGHT.
     * @return WEIGHT of ITEM as Double.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Getter for ITEM VALUE.
     * @return VALUE of ITEMS as Int.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Getter for INCLUDED.
     * @return INCLUDED 'state' as Boolean.
     */
    public boolean isIncluded() {
        return this.included;
    }

    /**
     * toString for the ITEM info.
     * @return [NAME] ([WEIGHT] lbs, $[VALUE])
     */
    public String toString() {
        return this.name + " (" + this.weight + " lbs, $" + this.value + ")";
    }

}
