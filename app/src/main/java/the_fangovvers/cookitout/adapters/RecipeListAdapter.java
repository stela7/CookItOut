package the_fangovvers.cookitout.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.activities.RecipeInfoActivity;
import the_fangovvers.cookitout.model.Recipe;

/**
 * Created by stela on 11/6/2017.
 */

public class RecipeListAdapter extends ArrayAdapter<Recipe> {

    private Context context;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View recipeItemView = convertView;
        if (recipeItemView == null)
            recipeItemView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item, parent, false);
        final Recipe currentRecipe = getItem(position);

        TextView recipe = (TextView) recipeItemView.findViewById((R.id.RecipeName));
        recipe.setText(/*position + 1 + ": " + */String.valueOf(currentRecipe.getName()));

        if (currentRecipe.getCategory() != null) {
            ImageView catIcon = recipeItemView.findViewById(R.id.categoryIcon);
            int catID = currentRecipe.getCategory().getResourceID();
            catIcon.setImageResource(catID);
        }

        recipeItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String recipeJSON = gson.toJson(currentRecipe);
                Intent intent = new Intent(context, RecipeInfoActivity.class);
                intent.putExtra("recipe", recipeJSON);
                context.startActivity(intent);
            }
        });

        return recipeItemView;
    }


}
