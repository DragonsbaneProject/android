package org.dragonsbaneproject.platform;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian on 10/25/17.
 */

public class DBApplication extends Application {

    private Map<String,Activity> activities = new HashMap<>();

    public Activity getActivity(Class clazz) {
        return activities.get(clazz.getName());
    }

    public void addActivity(Class clazz, Activity activity) {
        activities.put(clazz.getName(), activity);
    }

    public void removeActivity(Class clazz) {
        activities.remove(clazz.getName());
    }
}
