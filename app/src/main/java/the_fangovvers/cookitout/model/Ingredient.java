package the_fangovvers.cookitout.model;

public class Ingredient {
    boolean isVisible = false;
    String name;
    String unit;
    int value;

    public Ingredient(String name, String unit, int value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }
    public boolean isVisible(){
        return isVisible;
    }
    public void setVisibilty(){
        isVisible= !isVisible;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
