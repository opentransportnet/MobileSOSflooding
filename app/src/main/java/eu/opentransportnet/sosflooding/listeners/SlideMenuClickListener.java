package eu.opentransportnet.sosflooding.listeners;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import eu.opentransportnet.sosflooding.activities.ChangeLangActivity;
import eu.opentransportnet.sosflooding.activities.ChangeNewsOptionsActivity;
import eu.opentransportnet.sosflooding.activities.HomeActivity;

/**
 * @author Ilmars Svilsts
 */
public class SlideMenuClickListener implements ListView.OnItemClickListener {
    private DrawerLayout drawer;
    HomeActivity activity;

    public SlideMenuClickListener(DrawerLayout drawer, HomeActivity activity) {
        this.drawer = drawer;
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // display view for selected nav drawer item
        switch (position) {
            case 0:
                drawer.closeDrawers();
                Intent changeLang = new Intent(activity, ChangeLangActivity.class);
                activity.startActivity(changeLang);
                break;
            case 1:
                drawer.closeDrawers();
                 changeLang = new Intent(activity, ChangeNewsOptionsActivity.class);
                activity.startActivity(changeLang);
                break;
        }
    }
}