package the_fangovvers.cookitout.model;

public class Category {
   private String name;
   private int resourceID; // TODO: 11/1/2017 image type

    public Category(String name, int resourceID) {
        this.name = name;
        this.resourceID = resourceID;
    }

    public void setIcon(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getName() {
        return name;
    }

    public int getResourceID() {
        return resourceID;
    }

    @Override
    public boolean equals(Object obj) {
        Category c = (Category) obj;
        return this.name.equals(c.getName());

    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", resourceID=" + resourceID +
                '}';
    }
}
