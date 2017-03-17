package eu.opentransportnet.sosflooding.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.adapters.CustomListAdapter;
import eu.opentransportnet.sosflooding.interfaces.VolleyRequestListener;
import eu.opentransportnet.sosflooding.models.BaseActivity;
import eu.opentransportnet.sosflooding.network.RequestQueueSingleton;
import eu.opentransportnet.sosflooding.network.Requests;
import eu.opentransportnet.sosflooding.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilmars Svilsts
 */
public class NewsListActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLanguageList;
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_CATEGORY = "category";
    public final String TAG_DOWNLOAD_NEWS = "downloadNews";
    private static Context sContext;
    private static final String LOG_TAG = "NewsListActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        setContentView(R.layout.activity_news_list);
        setToolbarTitle(R.string.title_activity_change_news_options);
        initToolbarBackBtn();
        final RelativeLayout progressBar = (RelativeLayout) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        Requests.getNews(this,
                new VolleyRequestListener<String>() {
                    @Override
                    public void onResult(String response) {
                        if(!response.equals(null) && !response.isEmpty() && !response.equals("null")){
                            JSONArray jArray=null;
                            try {
                                jArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            final List<String> time = new ArrayList<String>(),
                                    title = new ArrayList<String>(),content = new ArrayList<String>(),
                                    category = new ArrayList<String>();

                            for(int i=0; i<jArray.length(); i++){

                                JSONObject json_data;
                                try {
                                    json_data = jArray.getJSONObject(i);
                                    time.add(json_data.getString("the_time"));
                                    title.add(json_data.getString("title"));
                                    content.add(json_data.getString("content"));
                                    category.add(json_data.getString("category"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            CustomListAdapter adapter =
                                    new CustomListAdapter(NewsListActivity.this, title,time);

                            mLanguageList.setAdapter(adapter);
                            mLanguageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    Intent c = new Intent(NewsListActivity.this, NewsActivity.class);
                                    c.putExtra(EXTRA_TIME,time.get(position));
                                    c.putExtra(EXTRA_TITLE,title.get(position));
                                    c.putExtra(EXTRA_CONTENT,content.get(position));
                                    c.putExtra(EXTRA_CATEGORY,category.get(position));
                                    NewsListActivity.this.startActivity(c);
                                }

                            });

                            Utils.logD(LOG_TAG, response);
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String object) {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                TAG_DOWNLOAD_NEWS);
        sContext = this;
        // Gets ListView object from xml
        mLanguageList = (ListView) findViewById(R.id.list);

        ImageButton sos = (ImageButton) findViewById(R.id.sos_button);
        sos.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sos_button:
                Intent c = new Intent(this, SosActivity.class);
                startActivity(c);
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueueSingleton.getInstance(sContext).cancelAllPendingRequests(TAG_DOWNLOAD_NEWS);
    }
}
