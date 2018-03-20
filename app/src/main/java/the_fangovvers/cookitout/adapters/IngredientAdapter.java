package the_fangovvers.cookitout.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.model.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    public IngredientAdapter(Activity context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_item, parent, false);

        Ingredient currentIngredient = getItem(position);

        TextView amount = (TextView) listItemView.findViewById(R.id.amountField);
        amount.setText(String.valueOf(currentIngredient.getValue()));

        TextView unit = (TextView) listItemView.findViewById(R.id.unitField);
        unit.setText(String.valueOf(currentIngredient.getUnit()));

        TextView name = (TextView) listItemView.findViewById(R.id.nameField);
        name.setText(String.valueOf(currentIngredient.getName()));

        return listItemView;
    }
}
