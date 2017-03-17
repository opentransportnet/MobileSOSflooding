package eu.opentransportnet.sosflooding.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.models.BaseActivity;
import eu.opentransportnet.sosflooding.network.NetworkReceiver;

import java.util.Locale;

/**
 * @author Kristaps Krumins
 * @author Ilmars Svilsts
 */
public class SosActivity extends BaseActivity implements View.OnClickListener{
    private static final int TOOLBAR_TITLE = R.string.title_activity_sos;

    private static Context sContext;

    private NetworkReceiver mNetworkReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        sContext = this;
        setContentView(R.layout.activity_sos);
        setToolbarTitle(R.string.title_activity_sos);
        initToolbarBackBtn();

        ImageButton deleteButton = (ImageButton) findViewById(R.id.sos_button);
        deleteButton.setVisibility(View.INVISIBLE);

        Button sosButton = (Button) findViewById(R.id.buttonsos);
        sosButton.setOnClickListener(this);

        sosButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    TextView text = (TextView) findViewById(R.id.text_press_activity_sos);
                    text.setVisibility(View.GONE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    TextView text = (TextView) findViewById(R.id.text_press_activity_sos);
                    text.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });




        if (Locale.getDefault().getLanguage().equals("cs")) {
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixelsLeft = (int) (168*scale + 0.5f);
            int dpAsPixelsTop = (int) (82*scale + 0.5f);
            ((TextView) findViewById(R.id.text_press_activity_sos)).setPadding(dpAsPixelsLeft,dpAsPixelsTop,0,0);
        }
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
        // Refresh toolbar in case language has been changed
        setToolbarTitle(TOOLBAR_TITLE);
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
            case R.id.buttonsos:
                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:1188"));
                try{
                    startActivity(callIntent);
                }
                catch (Exception e){}
                break;
        }
    }

    public static Context getContext() {
        return sContext;
    }
}