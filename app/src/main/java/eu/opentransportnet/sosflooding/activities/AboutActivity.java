package eu.opentransportnet.sosflooding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.models.BaseActivity;

import java.util.Locale;

/**
 * @author Ilmars Svilsts
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        setContentView(R.layout.activity_about);
        setToolbarTitle(R.string.title_activity_about);
        initToolbarBackBtn();

        WebView webView = (WebView) findViewById(R.id.web_view);

        String lang = "en";

        if (Locale.getDefault().getLanguage().equals("cs")) {
            lang = "cs";
        }

        String locationToUnzip = getFilesDir() + "/html"+lang+"/index.html";
        webView.loadUrl("file://"+locationToUnzip);
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
}
