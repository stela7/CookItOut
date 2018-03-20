package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

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
import the_fangovvers.cookitout.adapters.IconAdapter;
import the_fangovvers.cookitout.adapters.RecipeListAdapter;
import the_fangovvers.cookitout.model.Recipe;

public class RecipeListActivity extends BaseActivity {

    private ArrayList<Recipe> recipeList;
    private String recipesJSON;
    private RecipeListAdapter rAdapter;
    private ListView recipesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        super.inflateToolbar(R.id.toolbar);
        recipesListView = findViewById(R.id.recipeListMain);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_add_circle_outline_black_24dp)
                .normalText("Add a new recipe")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if (index == 0) addRecipe();
                    }
                })
        );
                            /*.shadowCornerRadius(Util.dp2px(15))
                            .buttonCornerRadius(Util.dp2px(15))
                            .isRound(false)*/
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_remove_circle_outline_black_24dp)
                .normalText("Remove recipe")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if (index == 1) removeRecipes();
                    }
                })

        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {
        if (FirebaseCONN.getInstance().getCurrentUser() == null) return;
        new MainAsync().execute("https://cook-it-out.firebaseio.com/USERS_TABLE/"
                + FirebaseCONN.getInstance().getCurrentUser().getUid() + "/mainRecipes.json");
    }


    public void addRecipe() {
        if (FirebaseCONN.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
            intent.putExtra("recipes", recipesJSON);
            startActivity(intent);
        }
    }

    public void removeRecipes() {
        if (recipeList == null) return;
        final CharSequence[] items = new CharSequence[recipeList.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = recipeList.get(i).getName();
        }
        // arraylist to keep the selected items
        final ArrayList seletedRecipes = new ArrayList();
        Log.d("Size", String.valueOf(recipeList.size()));
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
                            int removeIndex = (int) seletedRecipes.get(i);
                            recipeList.remove(removeIndex);
                        }
                        DatabaseReference dbRoot = FirebaseCONN.getInstance().getmDB().getReference();
                        DatabaseReference mRef = dbRoot.child("USERS_TABLE")
                                .child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid())
                                .child("mainRecipes");
                        mRef.setValue(new ArrayList<Recipe>());
                        mRef.setValue(recipeList);
                        rAdapter = new RecipeListAdapter(RecipeListActivity.this, recipeList);
                        recipesListView.setAdapter(rAdapter);
                        // TODO: 15/02/2018 fix stringjson not being updated

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    public void showIconDialog(View view) {
        final Context context = RecipeListActivity.this;
        IconAdapter iconAdapter = new IconAdapter(context, Helper.ICONS);
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.icon_popup, null);
        final GridView iconsGrid = promptsView.findViewById(R.id.iconGridView);
        iconsGrid.setAdapter(iconAdapter);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        // WORKING
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
            recipesJSON = (String) o;
            Type listType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();
            recipeList = new Gson().fromJson(recipesJSON, listType);
            if (recipeList == null) recipeList = new ArrayList<>();
            rAdapter = new RecipeListAdapter(RecipeListActivity.this, recipeList);
            recipesListView.setAdapter(rAdapter);
            super.onPostExecute(o);
        }


    }
}
