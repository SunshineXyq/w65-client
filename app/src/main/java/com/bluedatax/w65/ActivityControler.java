package com.bluedatax.w65;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdx108 on 15/12/25.
 */
public class ActivityControler {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {

        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {

        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
