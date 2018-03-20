package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.adapters.CategoryAdapter;
import the_fangovvers.cookitout.model.Category;

public class CategoryActivity extends BaseActivity {

    /*private ListView listView;*/
    private ListView listView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        super.inflateToolbar(R.id.toolbar);

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
            bmb.addBuilder(new TextOutsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_add_circle_outline_black_24dp)
                    .normalText("Add new Category")
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textGravity(Gravity.CENTER)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(index == 0) addCategory();
                        }
                    })
            );
                            /*.shadowCornerRadius(Util.dp2px(15))
                            .buttonCornerRadius(Util.dp2px(15))
                            .isRound(false)*/
                    bmb.addBuilder(new TextOutsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_remove_circle_outline_black_24dp)
                    .normalText("Remove Category")
                            .typeface(Typeface.DEFAULT_BOLD)
                            .textGravity(Gravity.CENTER)
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    //remove category
                                }
                            })

                    );
        categories.add(new Category("Beef", R.drawable.beef));
        categories.add(new Category("Pork", R.drawable.pork));
        categories.add(new Category("Chicken", R.drawable.chicken));
        categories.add(new Category("Turkey", R.drawable.turkey));
        categories.add(new Category("Fish", R.drawable.fish));
        categories.add(new Category("Seafood", R.drawable.seafood));
        categories.add(new Category("Oriental Food", R.drawable.oriental));
        categories.add(new Category("Vegetarian", R.drawable.vegetarian));
        categories.add(new Category("Soup", R.drawable.soup));
        categories.add(new Category("Gluten Free", R.drawable.glutenfree));
        categories.add(new Category("Vegan", R.drawable.vegan));
        categories.add(new Category("Salad", R.drawable.salad));
        categories.add(new Category("Drink", R.drawable.drink));
        categories.add(new Category("Desserts", R.drawable.desserts));

        /*listView = (ListView) findViewById(R.id.categoryList);*/
        listView = (ListView) findViewById(R.id.gridList);
        categoryAdapter = new CategoryAdapter(this, categories);
        listView.setAdapter(categoryAdapter);
        /*listView.setAdapter(categoryAdapter);*/

    }

    public void addCategory() {
        final Context context = CategoryActivity.this;
        // custom dialog
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_category_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText categoryName = (EditText) promptsView.findViewById(R.id.categoryName);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String categoryNameString = categoryName.getText().toString();
                                if (!categoryNameString.equals("")) {
                                    // TODO: 11/24/2017 save categories
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


}
