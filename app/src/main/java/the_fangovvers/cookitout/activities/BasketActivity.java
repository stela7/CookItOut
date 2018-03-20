package the_fangovvers.cookitout.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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
import java.util.Map;

import the_fangovvers.cookitout.Helper;
import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.adapters.BasketAdapter;
import the_fangovvers.cookitout.model.Ingredient;
import the_fangovvers.cookitout.model.Recipe;

public class BasketActivity extends BaseActivity {

    private ArrayList<Ingredient> basketIngredients = new ArrayList<>();
    private BasketAdapter basket;
    private Table<String, String, Integer> basketTable;
    private ListView listView;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        super.inflateToolbar(R.id.toolbar);
        activity = this;
        listView = findViewById(R.id.basketList);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_remove_circle_outline_black_24dp)
                .normalText("Remove item")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                       if(index == 0) remove();
                    }
                })
        );
                            /*.shadowCornerRadius(Util.dp2px(15))
                            .buttonCornerRadius(Util.dp2px(15))
                            .isRound(false)*/
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_remove_shopping_cart_black_24dp)
                .normalText("Clean basket")
                .typeface(Typeface.DEFAULT_BOLD)
                .textGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        if(index == 1) cleanBasketFirebase();
                    }
                })

        );

       /* //remove from the firebase
        final FloatingActionButton actionButton1 = (FloatingActionButton) findViewById(R.id.floating_menu_button1);
        //remove from basket
        FloatingActionButton actionButton2 = (FloatingActionButton) findViewById(R.id.floating_menu_button2);

        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - Marek firebase?
            }
        });
*/
        loadBasket();
        /*basket = new BasketAdapter(this, basketIngredients);
        listView = (ListView) findViewById(R.id.basketList);
        listView.setAdapter(basket);*/

        /*basketIngredients.add(new Ingredient("flour", "one cup", 1));
        basketIngredients.add(new Ingredient("potatoes", "kg", 1));*/
    }

    private void loadBasket() {
        if (FirebaseCONN.getInstance().getCurrentUser() == null) return;
        new MainAsync().execute("https://cook-it-out.firebaseio.com/USERS_TABLE/"
                + FirebaseCONN.getInstance().getCurrentUser().getUid() + "/basketRecipes.json");
    }

    public void remove() {
        for(int i = 0; i < basket.getCount();i++){
            basket.getItem(i).setVisibilty();
            basket.notifyDataSetChanged();
        }
       /* CheckBox boxes = (CheckBox) findViewById(R.id.check);
        boxes.setVisibility(View.VISIBLE);*/
    }

    public void ifChecked(View view) {
        CheckBox boxes = (CheckBox) findViewById(R.id.check);
        boolean checked = ((CheckBox) view).isChecked();
        switch ((view.getId())) {
            case R.id.check:
                if (checked) {
                    basket.remove((Ingredient) boxes.getTag());
                    basket.notifyDataSetChanged();
                }

        }

    }

    public Table<String, String, Integer> toTable(ArrayList<Recipe> recipes) {
        Table<String, String, Integer> result = HashBasedTable.create();
        for (Recipe r : recipes) {
            if (r.getIngredients() != null) {
                ArrayList<Ingredient> ingredients = r.getIngredients();
                for (Ingredient i : ingredients) {
                    if (result.contains(i.getName(), i.getUnit())) {
                        Integer temp = result.get(i.getName(), i.getUnit());
                        temp += i.getValue();
                        result.put(i.getName(), i.getUnit(), temp);
                    } else {
                        result.put(i.getName(), i.getUnit(), i.getValue());
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Ingredient> toArrayList(Table<String, String, Integer> ingredients) {
        ArrayList<Ingredient> result = new ArrayList<>();
        Map<String, Map<String, Integer>> rowIngredients = ingredients.rowMap();
        for (String s : rowIngredients.keySet()) {
            String ingredientName = s;
            Map<String, Integer> values = rowIngredients.get(s);
            for (String s2 : values.keySet()) {
                String ingredientUnit = s2;
                Integer amount = values.get(s2);
                result.add(new Ingredient(ingredientName, ingredientUnit, amount));
            }
        }
        return result;
    }

    public void cleanBasketFirebase(){
        DatabaseReference dbRoot = FirebaseCONN.getInstance().getmDB().getReference();
        DatabaseReference mRef = dbRoot.child("USERS_TABLE")
                .child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid())
                .child("basketRecipes");
        mRef.setValue(new ArrayList<Recipe>());
        showAlert(BasketActivity.this, "Success", "Your basket has been cleaned");
        basket = new BasketAdapter(BasketActivity.this, new ArrayList<Ingredient>());
        listView.setAdapter(basket);
    }

    /*public void removePopup() {
        // TODO: 11/15/2017  not used yet
        final CharSequence[] items = new CharSequence[basketIngredients.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = basketIngredients.get(i).getName();
        }
        // arraylist to keep the selected items
        final ArrayList seletedItems = new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select The Difficulty Level")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }*/

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
            Log.d("JSON", recipeslistJSON);
            Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
            ArrayList<Recipe> recipeBasket = new Gson().fromJson(recipeslistJSON, listType);
            if (recipeBasket == null) recipeBasket = new ArrayList<>();
            Table<String, String, Integer> basketTable = toTable(recipeBasket);
            basketIngredients = toArrayList(basketTable);

            basket = new BasketAdapter(activity, basketIngredients);
            listView.setAdapter(basket);
            super.onPostExecute(o);
        }


    }
}

