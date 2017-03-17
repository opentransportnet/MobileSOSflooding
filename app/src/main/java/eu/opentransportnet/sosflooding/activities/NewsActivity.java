package eu.opentransportnet.sosflooding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.models.BaseActivity;

/**
 * @author Ilmars Svilsts
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        setContentView(R.layout.activity_news);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String title = extras.getString(NewsListActivity.EXTRA_TITLE);

        TextView titles = (TextView) findViewById(R.id.title);
        if (titles != null) {
            titles.setText((CharSequence) title);
        }
        initToolbarBackBtn();

        TextView timeTextView = (TextView) findViewById(R.id.date);
        String time = extras.getString(NewsListActivity.EXTRA_TIME);
        if (!time.equals(null) && !time.isEmpty() && time != "null") {
            timeTextView.setText(time);
        }

        TextView contentTextView = (TextView) findViewById(R.id.description);
        String content = extras.getString(NewsListActivity.EXTRA_CONTENT);
        if (!content.equals(null) && !content.isEmpty() && content != "null") {
            contentTextView.setText(content);
        }

        TextView categoryTextView = (TextView) findViewById(R.id.category);
        String category = extras.getString(NewsListActivity.EXTRA_CATEGORY);
        if (!category.equals(null) && !category.isEmpty() && category != "null") {
            categoryTextView.setText(category);
        }
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
