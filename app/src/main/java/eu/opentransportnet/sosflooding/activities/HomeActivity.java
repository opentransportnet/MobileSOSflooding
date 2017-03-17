package eu.opentransportnet.sosflooding.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.BuildConfig;
import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.adapters.DrawerList;
import eu.opentransportnet.sosflooding.listeners.SlideMenuClickListener;
import eu.opentransportnet.sosflooding.models.BaseActivity;
import eu.opentransportnet.sosflooding.network.NetworkReceiver;
import eu.opentransportnet.sosflooding.network.RequestQueueSingleton;
import eu.opentransportnet.sosflooding.network.Requests;
import eu.opentransportnet.sosflooding.network.UploadTask;
import eu.opentransportnet.sosflooding.utils.Const;
import eu.opentransportnet.sosflooding.utils.Utils;

import java.io.File;

/**
 * @author Kristaps Krumins
 * @author Ilmars Svilsts
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static boolean sLanguageChanged = false;
    private static final int TOOLBAR_TITLE = R.string.title_activity_home;
    private static final String LOG_TAG = "HomeActivity";
    public final String TAG_DOWNLOAD_CONTENT_REQUEST = "downloadContent";

    private static Context sContext;
    private static Activity sActivity;

    private DrawerLayout mDrawer;
    private NetworkReceiver mNetworkReceiver;
    private ListView mDrawerList;
    public static boolean mHTMLCheck=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        sActivity=this;
        sContext = this;
        Utils.setContext(this);
        setContentView(R.layout.activity_home);
        setToolbarTitle(R.string.title_activity_home);
        initToolbarBackBtn();
        mNetworkReceiver = new NetworkReceiver(this);
        initHomeBtns();

        ImageButton sos = (ImageButton) findViewById(R.id.sos_button);
        sos.setVisibility(View.VISIBLE);
        sos.setOnClickListener(this);

        Button drawer = (Button) findViewById(R.id.back_button);
        drawer.setText(R.string.icon_menu);
        drawer.setTextSize(45);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.START);
            }
        });
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.drawerlist);
        setDrawerAdapter();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener(mDrawer, this));

        createFolders();

        TextView version = (TextView) findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        try {
            if(getString(R.string.svn_version).equals("null")){
                version.setText("v" + versionName);
            }
            else{
                version.setText("v" + versionName + " (" + getString(R.string.svn_version) + ")");
            }
        }
        catch (Exception e){
            Utils.logD(LOG_TAG, String.valueOf(e));
        }

        initFloodingHTML(false);
        UploadTask.getInstance(this).startScheduledUpload();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Registers BroadcastReceiver to track network connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh all text fields in case language has been changed
        setToolbarTitle(TOOLBAR_TITLE);
        setDrawerAdapter();
        if(sLanguageChanged) {
            setToolbarTitle(R.string.title_activity_home);
            setDrawerAdapter();
            ((TextView) findViewById(R.id.about_flooding_home_activity)).setText(R.string.about_flooding);
            ((TextView) findViewById(R.id.information_about_flooding_home_activity)).setText(R.string.information_about_flooding);
            ((TextView) findViewById(R.id.maps_home_activity)).setText(R.string.maps);
            ((TextView) findViewById(R.id.weather_floods_acidents_home_activity)).setText(R.string.maps_weather_floods_acidents);
            ((TextView) findViewById(R.id.news_home_activity)).setText(R.string.news);
            ((TextView) findViewById(R.id.police_etc_home_activity)).setText(R.string.police_fire_medic);
            sLanguageChanged = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {}
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {}
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueueSingleton.getInstance(sContext).
                cancelAllPendingRequests(TAG_DOWNLOAD_CONTENT_REQUEST);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maps:
                Intent mapsActivity = new Intent(this, MapActivity.class);
                startActivity(mapsActivity);
                break;
            case R.id.about:
                initFloodingHTML(true);
                break;
            case R.id.alerts:
                Intent NewsActivity = new Intent(this, NewsListActivity.class);
                startActivity(NewsActivity);
                break;
            case R.id.sos_button:
                Intent c = new Intent(this, SosActivity.class);
                startActivity(c);
                break;
        }
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     * Creates folders in internal storage
     */
    private void createFolders() {
        File filesDir = getFilesDir();
        File newFolder = new File(filesDir + "/routes");
        newFolder.mkdir();
        newFolder = new File(filesDir + "/" + Const.APP_ROUTES_JSON_PATH);
        newFolder.mkdir();
        newFolder = new File(filesDir + "/" + Const.APP_ROUTES_CSV_PATH);
        newFolder.mkdir();
    }

    private void setDrawerAdapter() {
        String[] drawerTitles = getResources().getStringArray(R.array.drawer_titles_array);
        String[] drawerImages = getResources().getStringArray(R.array.drawer_img_array);
        DrawerList adapter = new DrawerList(HomeActivity.this, drawerTitles, drawerImages);

        mDrawerList.setAdapter(adapter);
    }

    private void initHomeBtns() {
        LinearLayout button = (LinearLayout) findViewById(R.id.maps);
        button.setOnClickListener(this);
        button = (LinearLayout) findViewById(R.id.about);
        button.setOnClickListener(this);
        button = (LinearLayout) findViewById(R.id.alerts);
        button.setOnClickListener(this);
    }
    private void initFloodingHTML(final Boolean open) {
        FrameLayout spinner;
        spinner = (FrameLayout) findViewById(R.id.progress);
        if(open){
            spinner.setVisibility(View.VISIBLE);
        }
        if(mHTMLCheck) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if(!mHTMLCheck) {
                        if (!Requests.getContentDate(sContext, sActivity, open)) {
                            if (open) {
                                FrameLayout spinner;
                                spinner = (FrameLayout) findViewById(R.id.progress);
                                spinner.setVisibility(View.GONE);
                            }
                        }
                    }
                    else{
                        initFloodingHTML(open);
                    }
                }
            }, 1000);
        }
        else {
            if (!Requests.getContentDate(sContext, sActivity, open)) {
                if (open) {
                    spinner = (FrameLayout) findViewById(R.id.progress);
                    spinner.setVisibility(View.GONE);
                }
            }
        }
    }
    public static void languageChanged(){
        sLanguageChanged = true;
    }
}