package eu.opentransportnet.sosflooding.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.utils.Const;

/**
 * @author Ilmars Svilsts
 */
public class DrawerList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] mTitles;
    private String[] mImages;
    private Typeface tf;

    public DrawerList(Activity context, String[] titles, String[] images) {
        super(context, R.layout.list_single, titles);
        this.context = context;
        this.mTitles = titles;
        mImages = images;
        tf = Typeface.createFromAsset(context.getAssets(), Const.FONTELLO_PATH);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText(mTitles[position]);
        txtTitle.setTypeface(tf);

        TextView txtImage = (TextView) rowView.findViewById(R.id.img);
        txtImage.setText(mImages[position]);
        txtImage.setTypeface(tf);
        return rowView;
    }
}
