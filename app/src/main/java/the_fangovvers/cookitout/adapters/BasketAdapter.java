package the_fangovvers.cookitout.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.model.Category;
import the_fangovvers.cookitout.model.Ingredient;

/**
 * Created by stela on 11/13/2017.
 */

public class BasketAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredients;

    public BasketAdapter(Activity context, ArrayList<Ingredient> ingredients){
        super(context, 0, ingredients);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.basket_item, parent, false);

        Ingredient currentIngredient = getItem(position);

        TextView amount = (TextView) listItemView.findViewById(R.id.amount);
        amount.setText(String.valueOf(currentIngredient.getValue()));

        TextView unit = (TextView) listItemView.findViewById(R.id.unit);
        unit.setText(String.valueOf(currentIngredient.getUnit()));

        TextView name = (TextView) listItemView.findViewById(R.id.name);
        name.setText(String.valueOf(currentIngredient.getName()));
        CheckBox checkbox = (CheckBox)listItemView.findViewById(R.id.check);
        checkbox.setTag(currentIngredient);

        if(currentIngredient.isVisible()){
            checkbox.setVisibility(View.VISIBLE);
        }


        return listItemView;
    }

}
