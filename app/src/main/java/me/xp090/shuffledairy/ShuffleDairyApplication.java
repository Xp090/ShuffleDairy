package me.xp090.shuffledairy;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Xp090 on 17/01/2018.
 */

public class ShuffleDairyApplication extends Application {
   private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

  public static Context getAppContext() {
        return context;
  }
}
