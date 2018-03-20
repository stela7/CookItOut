package the_fangovvers.cookitout.activities;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import the_fangovvers.cookitout.Helper;
import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.adapters.RecipeListAdapter;
import the_fangovvers.cookitout.model.Category;
import the_fangovvers.cookitout.model.Recipe;

public class CategoryInfoActivity extends BaseActivity {

    private Category category = null;
    private RecipeListAdapter rAdapter;
    private ListView recipesListView;
    private ArrayList<Recipe> allRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_info);
        super.inflateToolbar(R.id.toolbar);
        recipesListView = findViewById(R.id.CategoryList);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("category")) {
            String recipeJSON = (String) bundle.get("category");
            Gson gson = new Gson();
            this.category = gson.fromJson(recipeJSON, Category.class);
        }
        loadCategory();
        loadRecipes();

        //BOOM MENU BUTTON
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_add_circle_outline_black_24dp)
                .normalText("Add recipe to category")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if(index == 0) addRecipesToCategory();
                    }
                })
        );
                            /*.shadowCornerRadius(Util.dp2px(15))
                            .buttonCornerRadius(Util.dp2px(15))
                            .isRound(false)*/
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_remove_circle_outline_black_24dp)
                .normalText("Remove recipe from category")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if(index == 1) removeFromCategory();
                    }
                })

        );

    }

    private void loadCategory() {
        if (category == null) return;
        TextView categoryName = findViewById(R.id.name);
        ImageView categoryImage = findViewById(R.id.Icon);

        categoryName.setText(category.getName());
        Drawable d = getResources().getDrawable(category.getResourceID());
        categoryImage.setImageDrawable(d);
    }

    private void loadRecipes() {
        if (FirebaseCONN.getInstance().getCurrentUser() == null) return;
        new MainAsync().execute("https://cook-it-out.firebaseio.com/USERS_TABLE/"
                + FirebaseCONN.getInstance().getCurrentUser().getUid() + "/mainRecipes.json");
    }

    public void addRecipesToCategory() {
        if (allRecipes == null) return;
        final CharSequence[] items = new CharSequence[allRecipes.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = allRecipes.get(i).getName();
        }
        // arraylist to keep the selected items
        final ArrayList seletedRecipes = new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select recipes to add")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedRecipes.add(indexSelected);
                        } else if (seletedRecipes.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedRecipes.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < seletedRecipes.size(); i++) {
                            allRecipes.get((Integer) seletedRecipes.get(i)).setCategory(category);
                        }
                        DatabaseReference dbRoot = FirebaseCONN.getInstance().getmDB().getReference();
                        DatabaseReference mRef = dbRoot.child("USERS_TABLE")
                                .child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid())
                                .child("mainRecipes");
                        mRef.setValue(new ArrayList<Recipe>());
                        mRef.setValue(allRecipes);
                        ArrayList<Recipe> categorized = Recipe.getByCategory(allRecipes, category);
                        rAdapter = new RecipeListAdapter(CategoryInfoActivity.this, categorized);
                        recipesListView.setAdapter(rAdapter);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    public void removeFromCategory(){
        if (allRecipes == null) return;
        final CharSequence[] items = new CharSequence[allRecipes.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = allRecipes.get(i).getName();
        }
        // arraylist to keep the selected items
        final ArrayList seletedRecipes = new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select recipes to remove")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedRecipes.add(indexSelected);
                        } else if (seletedRecipes.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedRecipes.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < seletedRecipes.size(); i++) {
                            allRecipes.get((Integer) seletedRecipes.get(i)).setCategory(null);
                        }
                        DatabaseReference dbRoot = FirebaseCONN.getInstance().getmDB().getReference();
                        DatabaseReference mRef = dbRoot.child("USERS_TABLE")
                                .child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid())
                                .child("mainRecipes");
                        mRef.setValue(new ArrayList<Recipe>());
                        mRef.setValue(allRecipes);
                        ArrayList<Recipe> categorized = Recipe.getByCategory(allRecipes, category);
                        rAdapter = new RecipeListAdapter(CategoryInfoActivity.this, categorized);
                        recipesListView.setAdapter(rAdapter);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
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
            ArrayList<Recipe> all;
            if (recipeslistJSON != null) {
                Log.d("JSON", recipeslistJSON);
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                all = new Gson().fromJson(recipeslistJSON, listType);
                if (all == null) all = new ArrayList<>();
            } else
                all = new ArrayList<>();
            if (all.isEmpty()) {
                super.onPostExecute(o);
                return;
            }
            allRecipes = all;
            ArrayList<Recipe> categorized = Recipe.getByCategory(all, category);
            rAdapter = new RecipeListAdapter(CategoryInfoActivity.this, categorized);
            recipesListView.setAdapter(rAdapter);
            super.onPostExecute(o);
        }


    }
}
