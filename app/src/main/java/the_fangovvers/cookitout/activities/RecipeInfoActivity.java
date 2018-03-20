package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import the_fangovvers.cookitout.Helper;
import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.adapters.IngredientAdapter;
import the_fangovvers.cookitout.adapters.RecipeListAdapter;
import the_fangovvers.cookitout.adapters.StepAdapter;
import the_fangovvers.cookitout.model.Recipe;

public class RecipeInfoActivity extends BaseActivity {

    private Recipe recipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        super.inflateToolbar(R.id.toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("recipe")) {
            String recipeJSON = (String) bundle.get("recipe");
            Gson gson = new Gson();
            this.recipe = gson.fromJson(recipeJSON, Recipe.class);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addToBasketFAB);
        fab.setImageBitmap(Helper.textAsBitmap("+", 72, Color.BLACK));
        // TODO: 11/15/2017 change plus sign to basket icon
        loadRecipe();
    }

    private void loadRecipe() {
        if (recipe == null) return;
        TextView recipeNameView = findViewById(R.id.recipeName);
        ListView ingredientsView = findViewById(R.id.ingredientListView);
        ListView stepsView = findViewById(R.id.stepListView);

        recipeNameView.setText(recipe.getName());
        IngredientAdapter ia = new IngredientAdapter(RecipeInfoActivity.this, recipe.getIngredients());
        StepAdapter sa = new StepAdapter(RecipeInfoActivity.this, recipe.getSteps());
        ingredientsView.setAdapter(ia);
        stepsView.setAdapter(sa);

    }

    public void addToBasket(View view) {
        if (FirebaseCONN.getInstance().getCurrentUser() == null) return;
        new MainAsync().execute("https://cook-it-out.firebaseio.com/USERS_TABLE/"
                + FirebaseCONN.getInstance().getCurrentUser().getUid() + "/basketRecipes.json");
    }

    @Override
    public void showAlert(Context context, String title, String text) {
        super.showAlert(context, title, text);
    }

    private class MainAsync extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            URL url = null;
            String urlString = (String) objects[0];
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return Helper.getRequest(url);
        }

        @Override
        protected void onPostExecute(Object o) {
            String recipeslistJSON = (String) o;
            String recipesJSON = recipeslistJSON;
            Log.d("JSON", recipeslistJSON);
            Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
            ArrayList<Recipe> recipeList = new Gson().fromJson(recipeslistJSON, listType);
            if (recipeList == null) recipeList = new ArrayList<>();
            recipeList.add(recipe);
            DatabaseReference rootRef = FirebaseCONN.getInstance().getmDB().getReference();
            DatabaseReference mRef = rootRef.child("USERS_TABLE")
                    .child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid())
                    .child("basketRecipes");
            mRef.setValue(recipeList);

            showAlert(RecipeInfoActivity.this, "Success", "Recipe was added to your basket.");
            super.onPostExecute(o);
        }


    }
}
