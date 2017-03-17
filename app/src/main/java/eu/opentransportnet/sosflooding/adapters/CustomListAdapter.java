package eu.opentransportnet.sosflooding.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;

import java.util.List;

/**
 * @author Ilmars Svilsts
 */
public class CustomListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final List<String>  text;
    private final List<String>  date;

    public CustomListAdapter(Activity context,
                             List<String> text, List<String> date) {
        super(context, R.layout.list_news, text);
        this.context = context;
        this.text = text;
        this.date = date;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_news, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_list);
        txtTitle.setText(text.get(position));
        TextView txtTime = (TextView) rowView.findViewById(R.id.text_time);
        txtTime.setText(date.get(position));
        return rowView;
    }
}
