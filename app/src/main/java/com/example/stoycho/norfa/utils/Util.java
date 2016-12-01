package com.example.stoycho.norfa.utils;

import android.content.Context;

/**
 * Created by stoycho on 11/18/16.
 */

public class Util {

    public static int getSize(Context context)
    {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) Math.ceil(density);
    }
}
