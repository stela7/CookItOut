package the_fangovvers.cookitout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.model.Step;

import java.util.ArrayList;

public class StepAdapter extends ArrayAdapter<Step> {


    public StepAdapter(Context context,  ArrayList<Step> steps) {
        super(context, 0, steps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.step_item, parent, false);

        Step currentStep = getItem(position);

        TextView step = (TextView) listItemView.findViewById(R.id.stepTextView);
        step.setText(position+1 + ": " +String.valueOf(currentStep.getStep()));

        return listItemView;
    }
}
