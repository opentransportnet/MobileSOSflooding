package eu.opentransportnet.sosflooding.network;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import eu.opentransportnet.sosflooding.R;
import eu.opentransportnet.sosflooding.activities.AboutActivity;
import eu.opentransportnet.sosflooding.activities.HomeActivity;
import eu.opentransportnet.sosflooding.activities.NewsListActivity;
import eu.opentransportnet.sosflooding.interfaces.VolleyRequestListener;
import eu.opentransportnet.sosflooding.utils.Const;
import eu.opentransportnet.sosflooding.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Kristaps Krumins
 * @author Ilmars Svilsts
 */
public class Requests {
    public static final String PATH_GET_COMPOSITIONS = "/get_compositions";
    public static final String PATH_GET_LAYERS = "/get_layers";
    public static final String PATH_GET_NEWS = Utils.getHostname() + "/get_news";
    public static final String PATH_GET_CONTENT_DATE = Utils.getHostname() + "/get_content_date/cs/";

    private static final String LOG_TAG = "Requests";

    private static Integer mNotificationId = Const.NOTIFICATION_BASE_FOR_UPLOAD;

    private Integer getNotificationId() {
        return mNotificationId;
    }

    static int count;

    /**
     * Downloads array of available compositions
     *
     * @param ctx      The context
     * @param listener The listener for request response
     * @param tag      The request tag, used for canceling request
     * @return If {@link StringRequest} has been added to request queue returns {@code true},
     * otherwise{@code false}
     */
    public static boolean getCompositions(Context ctx, final VolleyRequestListener<String> listener,
                                          String tag) {
        if (!Utils.isConnected(ctx)) {
            return false;
        }

        String url = "http://" + Utils.getHostname() + PATH_GET_COMPOSITIONS;
        url += "?language=" + Locale.getDefault().getLanguage();
        Utils.logD(LOG_TAG, "Request url:" + url);

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.logD(LOG_TAG, error.toString());
                        listener.onError(null);
                    }
                }
        );

        // Increase timeout, because their server is slow
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(req, tag);

        return true;
    }

    /**
     * Downloads array of available layers
     *
     * @param ctx      The context
     * @param id       The composition ID
     * @param listener The listener for request response
     * @param tag      The request tag, used for canceling request
     * @return If {@link StringRequest} has been added to request queue returns {@code true},
     * otherwise{@code false}
     */
    public static boolean getLayers(Context ctx, int id,
                                    final VolleyRequestListener<String> listener, String tag) {
        if (!Utils.isConnected(ctx)) {
            return false;
        }

        String parameters = "?language=" + Locale.getDefault().getLanguage();
        String url = "http://" + Utils.getHostname() + PATH_GET_LAYERS + "/" + id + parameters;
        Utils.logD(LOG_TAG, "Request url:" + url);

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.logD(LOG_TAG, error.toString());
                        listener.onError(null);
                    }
                }
        );

        // Increase timeout, because their server is slow
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(req, tag);

        return true;
    }

    public static boolean getNews(Context ctx,
                                    final VolleyRequestListener<String> listener, String tag) {
        if (!Utils.isConnected(ctx)) {
            return false;
        }

        String option="";
        try {
                option=Utils.getStringFromFile(HomeActivity.getContext().getFilesDir()+"/options.txt");
            }
        catch (Exception e){}
        String url = "http://" + PATH_GET_NEWS;
        if(option==""){
            url = url + "/24";
        }
        else {
            if (option.equals("1")) {
                url = url + "/170";
            } else if (option.equals("2")) {
                url = url + "/750";
            } else if (option.equals("3")) {
                url = url + "/8800";
            } else if (option.equals("4")) {
            } else {
                url = url + "/24";
            }
        }

        String lang = "eng";

        if (Locale.getDefault().getLanguage().equals("cs")) {
            lang = "cs";
        }

        Utils.logD(LOG_TAG, url);
        url=url+"?language=%1$s";
        url = String.format(url,
                lang);
        Utils.logD(LOG_TAG, url);
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.logD(LOG_TAG, error.toString());
                        listener.onError(null);
                    }
                }
        );

        // Increase timeout, because their server is slow
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(req, tag);

        return true;
    }

    public static boolean getNEWNews(final Context ctx) {
        if (!Utils.isConnected(ctx)) {
            return false;
        }
        String url = "http://" + PATH_GET_NEWS+"/0.03";

        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logD(LOG_TAG, response);
                        if(response!=null && !response.equals(null) && !response.equals("null")){
                            Utils.logD(LOG_TAG, response);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
                            mBuilder.setSmallIcon(R.drawable.home_alerts);

                            mBuilder.setContentTitle("New flooding news!");
                            mBuilder.setContentText("There is new news, please click to get details.");
                            mBuilder.setAutoCancel(true);
                            Intent resultIntent = new Intent(ctx, HomeActivity.class);
                            Intent resultIntents = new Intent(ctx, NewsListActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
                            stackBuilder.addParentStack(HomeActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            stackBuilder.addNextIntentWithParentStack(resultIntents);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(0,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(resultPendingIntent);

                            NotificationManager mNotificationManager = (NotificationManager)
                                    ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                            // notificationID allows you to update the notification later on.
                            mNotificationManager.notify(2, mBuilder.build());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.logD(LOG_TAG, error.toString());
                    }
                }
        );

        // Increase timeout, because their server is slow
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(req, "downloadNews");

        return true;
    }
    public static boolean getContentDate(final Context ctx,final Activity activity,final Boolean open) {
        HomeActivity.mHTMLCheck=true;
        final FrameLayout spinner;
        spinner = (FrameLayout) activity.findViewById(R.id.progress);
        if (!Utils.isConnected(ctx)) {
            if(open)
            {
                Utils.showToastAtTop(ctx, ctx.getString(R.string.network_unavailable));
            }
            HomeActivity.mHTMLCheck=false;
            return false;
        }
        String url = "http://" + PATH_GET_CONTENT_DATE;
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response!=null && !response.equals(null) && !response.equals("null")){
                            try {
                                JSONObject obj = new JSONObject(response);

                                String lang = "en";

                                if (Locale.getDefault().getLanguage().equals("cs")) {
                                    lang = "cs";
                                }

                                String option=HomeActivity.getContext().getFilesDir()+"/html" +lang;
                                File html = new File(option);
                                if(!html.isDirectory()) {
                                    getContentData(ctx,activity,open,lang);
                                }
                                else{
                                    HomeActivity.mHTMLCheck=false;
                                    File file = new File(option);

                                    Date lastModDate = new Date(file.lastModified());
                                    Date lastModDat = new Date(obj.getLong("time"));

                                    if(lastModDate.getTime()>lastModDat.getTime()){
                                        if(open)
                                        {
                                            spinner.setVisibility(View.GONE);
                                            Intent aboutActivity =
                                                    new Intent(activity, AboutActivity.class);
                                            activity.startActivity(aboutActivity);
                                        }
                                    }
                                    else{
                                        getContentData(ctx,activity,open,lang);
                                    }
                                }
                            } catch (Throwable t) {
                                HomeActivity.mHTMLCheck=false;
                                if(open) {
                                    Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                                    spinner.setVisibility(View.GONE);
                                }
                            }
                        }
                        else{
                            HomeActivity.mHTMLCheck=false;
                            if(open) {
                                Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                                spinner.setVisibility(View.GONE);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HomeActivity.mHTMLCheck=false;
                        if(open)
                        {
                            spinner.setVisibility(View.GONE);
                            Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                        }
                    }
                }
        );

        // Increase timeout, because their server is slow
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(req, "downloadContent");
        return true;
    }


    public static boolean getContentData(final Context ctx,
                                         final Activity activity, final Boolean open, final String lang){
        final FrameLayout spinner;
        spinner = (FrameLayout) activity.findViewById(R.id.progress);
        if (!Utils.isConnected(ctx)) {
            if(open)
            {
                Utils.showToastAtTop(ctx, ctx.getString(R.string.network_unavailable));
            }
            HomeActivity.mHTMLCheck=false;
            return false;
        }
        String url ="http://" + Utils.getHostname() + "/get_content/" + lang;
        InputStream request = new InputStream(Request.Method.GET, url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            if (response!=null) {
                                String filename = "content.zip";
                                try{
                                    //Response to input stream
                                    java.io.InputStream input = new ByteArrayInputStream(response);
                                    File path = ctx.getFilesDir();
                                    File file = new File(path, filename);
                                    BufferedOutputStream output =
                                            new BufferedOutputStream(new FileOutputStream(file));
                                    byte data[] = new byte[1024];

                                    while ((count = input.read(data)) != -1) {
                                        output.write(data, 0, count);
                                    }
                                    output.flush();
                                    output.close();
                                    input.close();

                                    String fileName =ctx.getFilesDir()+ "/content.zip";
                                    String locationToUnzip = ctx.getFilesDir() + "/html"+lang+"/";

                                    unzipFile(fileName, locationToUnzip);
                                    if(open)
                                    {
                                        spinner.setVisibility(View.GONE);
                                        Intent aboutActivity =
                                                new Intent(activity, AboutActivity.class);
                                        activity.startActivity(aboutActivity);
                                    }
                                }catch(IOException e){
                                    if(open)
                                    {
                                        Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                                        spinner.setVisibility(View.GONE);
                                    }
                                }
                            }
                            else{
                                if(open) {
                                    Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                                    spinner.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            if(open) {
                                Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                                spinner.setVisibility(View.GONE);
                            }
                        }
                        HomeActivity.mHTMLCheck=false;
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                HomeActivity.mHTMLCheck=false;
                if(open) {
                    Utils.showToastAtTop(ctx, ctx.getString(R.string.connection_issue));
                    spinner.setVisibility(View.GONE);
                }
            }
        }, null);


        // Increase timeout, because their server is slow
        request.setRetryPolicy(new DefaultRetryPolicy(
                RequestQueueSingleton.LONG_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adds request to request queue
        RequestQueueSingleton.getInstance(ctx.getApplicationContext())
                .addToRequestQueue(request, "downloadContent");
        return true;
    }

    public static void unzipFile(String fileName, String location) throws IOException {
        File file = new File(location);
        deleteRecursively(file);
        int size;
        byte[] buffer = new byte[1024];
        ZipEntry zipEntrys = null;
        ZipInputStream zipInput;

        try {
            File dirs = new File(location);
            if(!dirs.isDirectory()) {
                dirs.mkdirs();
            }
            zipInput =
                    new ZipInputStream(new BufferedInputStream(new FileInputStream(fileName), 1024));
            try {
                while ((zipEntrys = zipInput.getNextEntry()) != null) {
                    String path = location + zipEntrys.getName();
                    File unzipFile = new File(path);

                    if (zipEntrys.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    }
                    else {
                        File parentDirectories = unzipFile.getParentFile();
                        if ( null != parentDirectories ) {
                            if ( !parentDirectories.isDirectory() ) {
                                parentDirectories.mkdirs();
                            }
                        }

                        FileOutputStream fileOutput = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream output = new BufferedOutputStream(fileOutput, 1024);
                        try {
                            while ( (size = zipInput.read(buffer, 0, 1024)) != -1 ) {
                                output.write(buffer, 0, size);
                            }
                            zipInput.closeEntry();
                        }catch (Exception e){}
                        output.flush();
                        output.close();
                    }
                }
            }
            catch (Exception e) {}
            zipInput.close();
        }
        catch (Exception e) {}
    }
    public static void deleteRecursively(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursively(child);
            }
        }
        fileOrDirectory.delete();
    }
}
