package the_fangovvers.cookitout.model;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

public class Recipe {

    String name;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    Category category;

    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public Category getCategory() {
        return category;
    }

    public static ArrayList<Recipe> getByCategory(ArrayList<Recipe> recipes, Category category) {
        ArrayList<Recipe> result = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getCategory() != null) {
                if (r.getCategory().equals(category)) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", category=" + category +
                '}';
    }
}
