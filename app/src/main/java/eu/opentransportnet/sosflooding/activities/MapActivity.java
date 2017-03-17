package eu.opentransportnet.sosflooding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;
import android.widget.ListView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.adapters.CompositionListAdapter;
import eu.opentransportnet.sosflooding.interfaces.VolleyRequestListener;
import eu.opentransportnet.sosflooding.models.BaseActivity;
import eu.opentransportnet.sosflooding.models.Composition;
import eu.opentransportnet.sosflooding.models.WmsLayer;
import eu.opentransportnet.sosflooding.network.Requests;
import eu.opentransportnet.sosflooding.utils.Const;
import eu.opentransportnet.sosflooding.utils.Utils;
import com.library.routerecorder.RouteRecorder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Kristaps Krumins
 */
public class MapActivity extends BaseActivity implements View.OnClickListener {
    public static final String MAP_URL = "file:///android_asset/www/map.html";

    private static final String LOG_TAG = "MapActivity";

    private RouteRecorder mRr;
    private SlidingUpPanelLayout mSelectLayersPanel;
    private CompositionListAdapter mCompositionListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        setContentView(R.layout.activity_map);
        setToolbarTitle(R.string.title_activity_maps);
        initToolbarBackBtn();
        initRouteRecorder();

        ImageButton sos = (ImageButton) findViewById(R.id.sos_button);
        sos.setOnClickListener(this);

        mSelectLayersPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSelectLayersPanel.setFadeOnClickListener(this);

        ArrayList<Composition> compositionList = new ArrayList<>();
        mCompositionListAdapter =
                new CompositionListAdapter(this, R.layout.listview_layers, compositionList, mRr);
        ListView listView = (ListView) findViewById(R.id.layer_list);
        listView.setAdapter(mCompositionListAdapter);
        // Downloads and adds compositions to adapter
        addCompositions();
    }

    private void initRouteRecorder() {
        mRr = (RouteRecorder) getFragmentManager().findFragmentById(R.id.route_recorder);
        mRr.setDefaultLocation(Const.DEFAULT_LATITUDE, Const.DEFAULT_LONGITUDE);
        mRr.setTracking(true);
        mRr.addJavascriptInterface(this, "MapActivity");
        mRr.loadWebView();
        mRr.loadUrl(MAP_URL);
    }

    @JavascriptInterface
    public static String getLegendNotAvailableMessage(){
        return getContext().getString(R.string.legend_is_not_available);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sos_button:
                Intent c = new Intent(this, SosActivity.class);
                startActivity(c);
                break;
            case R.id.sliding_layout:
                mSelectLayersPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
        }
    }

    private void addCompositions() {
        Requests.getCompositions(this,
                new VolleyRequestListener<String>() {
                    @Override
                    public void onResult(String response) {
                        try {
                            JSONArray compositions = new JSONArray(response);

                            if (compositions != null) {
                                for (int i = 0; i < compositions.length(); i++) {
                                    JSONObject compositionObj = compositions.getJSONObject(i);
                                    String name = compositionObj.getString("name");
                                    int compositionId = compositionObj.getInt("composition_id");
                                    Composition composition = new Composition(compositionId,
                                            name, false);
                                    mCompositionListAdapter.add(composition);
                                    addLayersToComposition(composition);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mCompositionListAdapter.notifyDataSetChanged();
                        findViewById(R.id.layers_loading_panel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String object) {
                        Utils.logD(LOG_TAG, "Compositions download error");
                        findViewById(R.id.layers_loading_panel).setVisibility(View.GONE);
                    }
                },
                "downloadCompositions");
    }

    private void addLayersToComposition(final Composition composition) {
        Requests.getLayers(this, composition.getId(),
                new VolleyRequestListener<String>() {
                    @Override
                    public void onResult(String response) {
                        try {
                            JSONArray layers = new JSONArray(response);

                            if (layers != null) {
                                for (int i = 0; i < layers.length(); i++) {
                                    JSONObject layerObj = layers.getJSONObject(i);
                                    String name = layerObj.getString("wms_layer_name");
                                    String url = layerObj.getString("wms_url");
                                    WmsLayer layer = new WmsLayer(name,url);
                                    composition.addWmsLayer(layer);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        composition.setLayersLoaded();
                    }

                    @Override
                    public void onError(String object) {
                        Utils.logD(LOG_TAG, "Layers download error");
                        composition.setLayersLoaded();
                    }
                },
                "downloadLayers");
    }
}
