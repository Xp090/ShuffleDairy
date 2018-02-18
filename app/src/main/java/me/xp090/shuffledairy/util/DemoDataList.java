package me.xp090.shuffledairy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.davidea.flexibleadapter.items.IFlexible;
import me.xp090.shuffledairy.R;
import me.xp090.shuffledairy.ShuffleDairyApplication;
import me.xp090.shuffledairy.model.DairyNoteItem;
import me.xp090.shuffledairy.model.DateHeader;
import me.xp090.shuffledairy.model.NoteEntity;

/**
 * Created by Xp090 on 25/01/2018.
 */

public class DemoDataList {
    public static final String FIRST_TIME_DEMO_POPULATE_PREF = "FIRST_TIME_DEMO_POPULATE_PREF";
    public static final String PREF_KEY_IS_POPULATED = "isPopulated";
    private static Context context = ShuffleDairyApplication.getAppContext();
    private static SharedPreferences demoDataPref = context.getSharedPreferences(FIRST_TIME_DEMO_POPULATE_PREF, Context.MODE_PRIVATE);
    public static boolean isFirstRun() {
        boolean isFirstRun = demoDataPref.getBoolean(PREF_KEY_IS_POPULATED, true);
        return isFirstRun;
    }

    public static List<NoteEntity> getDemoDataList() {
        List<NoteEntity> demoItems = new ArrayList<>();
        String[] textArray = context.getResources().getStringArray(R.array.demoText);
        int[] colorArray = context.getResources().getIntArray(R.array.note_colors);
        String[] dateArray = context.getResources().getStringArray(R.array.demo_date_array);
        Random r = new Random();
        for (int i=0; i < textArray.length;i++) {
            int c = r.nextInt(colorArray.length);
            demoItems.add(new NoteEntity(Long.parseLong(dateArray[i]), textArray[i], colorArray[c]));
        }

        demoDataPref.edit()
                .putBoolean(PREF_KEY_IS_POPULATED,false)
                .apply();
        return demoItems;
    }
}
