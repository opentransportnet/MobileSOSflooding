package eu.opentransportnet.sosflooding.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.models.Composition;
import eu.opentransportnet.sosflooding.models.WmsLayer;
import com.library.routerecorder.RouteRecorder;
import com.library.routerecorder.Utils;

import java.util.ArrayList;

/**
 * @author Kristaps Krumins
 */
public class CompositionListAdapter extends ArrayAdapter<Composition> {
    private ArrayList<Composition> compositionList;
    private Activity context;
    private RouteRecorder routeRecorder;

    public CompositionListAdapter(Activity context, int textViewResourceId,
                                  ArrayList<Composition> compositionList,
                                  RouteRecorder routeRecorder) {
        super(context, textViewResourceId, compositionList);

        this.context = context;
        this.routeRecorder = routeRecorder;
        this.compositionList = new ArrayList<>();
        this.compositionList.addAll(compositionList);
    }

    private class ViewHolder {
        TextView title;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // reuse views
        if (convertView == null) {
            LayoutInflater vi = context.getLayoutInflater();
            convertView = vi.inflate(R.layout.listview_layers, null);

            // configure view holder
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Composition composition = (Composition) cb.getTag();
                    boolean isSelected = cb.isChecked();
                    composition.setSelected(isSelected);

                    if (isSelected == false) {
                        routeRecorder.loadUrl("javascript:removeWmsLayersFromComposition(" +
                                composition.getId() + ")");
                    } else {
                        if (composition.isLayersLoaded()) {
                            ArrayList<WmsLayer> wmsLayers = composition.getWmsLayers();

                            for (WmsLayer layer : wmsLayers) {
                                routeRecorder.loadUrl("javascript:addWmsLayerForComposition(" +
                                        composition.getId() + ",'" +
                                        layer.getWmsUrl() + "','" + layer.getName() + "')");
                            }
                        } else {
                            composition.setSelected(!isSelected);
                            Utils.showToastAtTop(context, context.getString(R.string.layer_not_loaded));
                        }
                    }
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // fill data
        Composition composition = compositionList.get(position);
        holder.title.setText(composition.getTitle());
        holder.checkBox.setChecked(composition.isSelected());
        holder.checkBox.setTag(composition);

        return convertView;
    }

    @Override
    public void add(Composition composition) {
        compositionList.add(composition);
        super.add(composition);
    }
}
