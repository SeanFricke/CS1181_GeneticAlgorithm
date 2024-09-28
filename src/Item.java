public class Item {
    // Field Inits
    private final String name;
    private final double weight;
    private final int value;
    private boolean included;


    /**
     * Constructor for Item that takes in explicit values as args.
     * @param name Name of Item.
     * @param weight Weight of Item.
     * @param value Monetary value of Item.
     */
    public Item(String name, double weight, int value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.included = false;
    }

    /**
     * Constructor for Item that takes in another Item as template, and copies it.
     * @param other Template Item to copy.
     */
    public Item(Item other){
        this.name = other.name;
        this.weight = other.weight;
        this.value = other.value;
    }

    /**
     * Setter for the Included field to dictate whether the Item is taken in that scenario (Chromosome).
     * @param included Boolean to set for Included field.
     */
    public void setIncluded(boolean included) {
        this.included = included;
    }

    /**
     * Getter for item Name
     * @return Item name as String.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for item Weight.
     * @return Weight of item as Double.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Getter for item Value.
     * @return Value of items as Int.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Getter for Included.
     * @return Included 'state' as Boolean.
     */
    public boolean isIncluded() {
        return this.included;
    }

    /**
     * To string for the item info.
     * @return <Name> ( <Weight> lbs, $<Value>
     */
    public String toString() {
        return this.name + " (" + this.weight + " lbs, $" + this.value + ")";
    }

}
