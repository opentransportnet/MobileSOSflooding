package eu.opentransportnet.sosflooding.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.models.BaseActivity;
import eu.opentransportnet.sosflooding.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Ilmars Svilsts
 */
public class ChangeNewsOptionsActivity extends BaseActivity implements View.OnClickListener {
    private ListView mLanguageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialization */
        setContentView(R.layout.activity_change_news_options);
        setToolbarTitle(R.string.title_activity_change_news_options);
        initToolbarBackBtn();

        TextView language = (TextView) findViewById(R.id.current_age);

        String option=
                Utils.getStringFromFile(HomeActivity.getContext().getFilesDir()+"/options.txt");



        // Gets ListView object from xml
        mLanguageList = (ListView) findViewById(R.id.age_list);

        Resources res = getResources();
        final String[] languages = res.getStringArray(R.array.news_options);

        if(option==""){
            language.setText(R.string.day);
        }
        else {
            try {
                language.setText(languages[Integer.parseInt(option)]);
            }catch (Exception e)
            {
                language.setText(R.string.day);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.black_list_item, languages);
        mLanguageList.setAdapter(adapter);
        mLanguageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                writeToFile(String.valueOf(position),
                        HomeActivity.getContext().getFilesDir()+"/options.txt");
                finish();
            }

        });

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

    public static boolean writeToFile(String object,String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(object);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
