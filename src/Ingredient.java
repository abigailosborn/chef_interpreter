public class Ingredient {
    String name;
    int inital_value;
    Measure measure;
    boolean is_heaped_or_level;
    
    public Ingredient() {
        this.name = "";
        this.inital_value = -1;
        this.measure = Measure.UNDEFINED;
        this.is_heaped_or_level = false;
    }

    public String toString() {
        return (this.inital_value > 0 ? Integer.toString(this.inital_value) +
                (this.measure.name().equals("UNDEFINED") ? "" : this.measure.name()) + " of " : "")
               + this.name;
    }
}
