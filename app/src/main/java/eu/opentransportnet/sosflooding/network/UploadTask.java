package eu.opentransportnet.sosflooding.network;

import android.content.Context;

import eu.opentransportnet.sosflooding.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Kristaps Krumins
 */
public class UploadTask {

    private static UploadTask mInstance;
    private static Context sAppCtx;

    private boolean mUploadStarted = false;

    private UploadTask(Context ctx) {
        sAppCtx = ctx.getApplicationContext();
    }

    public static synchronized UploadTask getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UploadTask(context);
        }
        return mInstance;
    }

    public void startScheduledUpload() {
        if (!mUploadStarted) {
            // Sets a new Timer
            Timer timer = new Timer();
            // Initializes the TimerTask's job
            TimerTask timerTask = initializeScheduledUpload();
            // Schedules the timer, after the first 1000ms theTimerTask will run every 3 minutes
            timer.schedule(timerTask, 1000, 180000);
            mUploadStarted = true;
        }
    }

    private TimerTask initializeScheduledUpload() {
        TimerTask timerTask = new TimerTask() {
            public void run() {
                if (Utils.isConnected(sAppCtx)) {
                    checkNews();
                }
            }
        };
        return timerTask;
    }

    public void checkNews() {
                Requests.getNEWNews(sAppCtx);
    }
}
