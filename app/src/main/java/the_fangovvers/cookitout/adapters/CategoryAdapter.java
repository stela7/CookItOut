package the_fangovvers.cookitout.adapters;

import android.app.Activity;
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
import the_fangovvers.cookitout.activities.CategoryInfoActivity;
import the_fangovvers.cookitout.activities.RecipeInfoActivity;
import the_fangovvers.cookitout.model.Category;

/**
 * Created by Karolina on 02/11/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;

    public CategoryAdapter(Activity context, ArrayList<Category> categories){
        super(context, 0, categories);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);

        final Category currentCategory = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.categoryName);
        name.setText(String.valueOf(currentCategory.getName()));

        ImageView icon = (ImageView) listItemView.findViewById(R.id.categoryIcon);
        icon.setImageResource(currentCategory.getResourceID());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String recipeJSON = gson.toJson(currentCategory);
                Intent intent = new Intent(context, CategoryInfoActivity.class);
                intent.putExtra("category", recipeJSON);
                context.startActivity(intent);
            }
        });


        return listItemView;
    }

}
