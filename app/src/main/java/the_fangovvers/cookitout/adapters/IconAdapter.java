package the_fangovvers.cookitout.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IconAdapter extends ArrayAdapter<Integer> {

    public IconAdapter(Context context,ArrayList<Integer> icons) {
        super(context, 0,  icons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.icon_item, parent, false);

        Integer currentIcon = getItem(position);

        ImageView icon = (ImageView) listItemView.findViewById(R.id.iconImage);
        icon.setImageResource(currentIcon);

        return listItemView;
    }
}
