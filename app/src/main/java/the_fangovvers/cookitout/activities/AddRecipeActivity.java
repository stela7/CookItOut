package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.adapters.IngredientAdapter;
import the_fangovvers.cookitout.adapters.StepAdapter;
import the_fangovvers.cookitout.model.Ingredient;
import the_fangovvers.cookitout.model.Recipe;
import the_fangovvers.cookitout.model.Step;

public class AddRecipeActivity extends BaseActivity {

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();
    private ListView ingredientListView;
    private ListView stepListView;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_recipe);
        super.inflateToolbar(R.id.toolbar);

       /* ingredients.add(new Ingredient("Cream", "ml", 250));
        ingredients.add(new Ingredient("Sugar", "g", 300));
        ingredients.add(new Ingredient("Egg", "psc.", 3));*/
        ingredientListView = (ListView) findViewById(R.id.ingredientListView);
        ingredientAdapter = new IngredientAdapter(this, ingredients);
        ingredientListView.setAdapter(ingredientAdapter);

        /*steps.add(new Step("Add some water and mix"));*/
        stepListView = (ListView) findViewById(R.id.stepListView);
        stepAdapter = new StepAdapter(this, steps);
        stepListView.setAdapter(stepAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("recipes")) {
            String recipesJSON = (String) bundle.get("recipes");
            if (recipesJSON == null || recipesJSON.equals(""))
                this.recipes = new ArrayList<>();
            else{
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                this.recipes = new Gson().fromJson(recipesJSON, listType);
            }
        }
        if (recipes == null) recipes = new ArrayList<>();
    }

    public void showIngredientDialog(View view) {
        final Context context = AddRecipeActivity.this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.ingredient_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);

        final EditText amount = (EditText) promptsView.findViewById(R.id.amountInputField);
        final EditText unit = (EditText) promptsView.findViewById(R.id.unitInputField);
        final EditText name = (EditText) promptsView.findViewById(R.id.nameInputField);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String amountstring = amount.getText().toString();
                                String unitstring = unit.getText().toString();
                                String namestring = name.getText().toString();
                                if (!(amountstring.equals("") && unitstring.equals("") && namestring.equals(""))) {
                                    ingredients.add(new Ingredient(namestring, unitstring, Integer.valueOf(amountstring)));
                                    ingredientAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showStepDialog(View view) {
        final Context context = AddRecipeActivity.this;
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.step_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);

        final EditText step = (EditText) promptsView.findViewById(R.id.stepEditText);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String stepstring = step.getText().toString();
                                if (!stepstring.equals("")) {
                                    steps.add(new Step(stepstring));
                                    ingredientAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void saveRecipe(View view) {
        final EditText recipename = (EditText) findViewById(R.id.recipeName);
        String recipeNameString = String.valueOf(recipename.getText());
        if (!recipeNameString.equals("")) {
            Recipe newRecipe = new Recipe(recipeNameString, ingredients, steps);
            recipes.add(newRecipe);
            DatabaseReference rootRef = FirebaseCONN.getInstance().getmDB().getReference();
            DatabaseReference cineIndustryRef = rootRef.child("USERS_TABLE")
                    .child(FirebaseCONN.getInstance().getCurrentUser().getUid())
                    .child("mainRecipes");
            cineIndustryRef.setValue(recipes);
            finish();
        }

    }
    public void cancel(View view) {
        finish();
    }
}
