package eu.opentransportnet.sosflooding.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Kristaps Krumins
 * @author Ilmars Svilsts
 */
public class Utils {
    // Should be set on app start
    private static Context mContext;

    public static void showToastAtTop(Context ctx, String message) {
        CharSequence text = message;
        Toast toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 110);
        toast.show();
    }

    /**
     * The ConnectivityManager to query the active network and determine if it has Internet
     * connectivity
     *
     * @param ctx The context
     * @return {@code true} if there is Internet connectivity, otherwise {@code false}
     */
    public static boolean isConnected(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void saveLocale(Context ctx, String lang) {
        SharedPreferences langPref = ctx.getSharedPreferences("language", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = langPref.edit();
        editor.putString("languageToLoad", lang);
        editor.commit();
    }

    public static void loadLocale(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences("language", Activity.MODE_PRIVATE);
        String language = prefs.getString("languageToLoad", "");
        changeLanguage(ctx, language);
    }


    public static void changeLanguage(Context ctx, String lang) {
        if (lang.equalsIgnoreCase("")) {
            // Use default
            lang = "en";
        }
        Log.d("dsaddfgdg", lang);
        Locale locale = new Locale(lang);
        saveLocale(ctx, lang);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
    }

    public static void logD(String tag, String message) {
        if (Config.getInstance(mContext).logMessages()) {
            Log.d(tag, message);
        }
    }

    public static void setContext(Context ctx){
        mContext = ctx;
    }

    public static String getHostname() {
        if (Config.getInstance(mContext).isRelease()) {
            return Config.getInstance(mContext).getReleaseHostname();
        } else {
            return Config.getInstance(mContext).getDebugHostname();
        }
    }

    public static String getStringFromFile(String filename) {
        String data = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            data=br.readLine();
            br.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
