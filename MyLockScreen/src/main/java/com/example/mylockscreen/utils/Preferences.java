package com.example.mylockscreen.utils;

/**
 * Created by haihong.xiahh on 13-7-3.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences
{
    public static final String USER_PREFERENCE = "user_pref";

    public static boolean getUserPrefBoolean(Context paramContext, String paramString, boolean paramBoolean)
    {
        return paramContext.getSharedPreferences("user_pref", 0).getBoolean(paramString, paramBoolean);
    }

    public static int getUserPrefInt(Context paramContext, String paramString, int paramInt)
    {
        return paramContext.getSharedPreferences("user_pref", 0).getInt(paramString, paramInt);
    }

    public static long getUserPrefLong(Context paramContext, String paramString, long paramLong)
    {
        return paramContext.getSharedPreferences("user_pref", 0).getLong(paramString, paramLong);
    }

    public static String getUserPrefString(Context paramContext, String paramString1, String paramString2)
    {
        return paramContext.getSharedPreferences("user_pref", 0).getString(paramString1, paramString2);
    }

    public static void setUserPrefBoolean(Context paramContext, String paramString, boolean paramBoolean)
    {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("user_pref", 0).edit();
        localEditor.putBoolean(paramString, paramBoolean);
        localEditor.commit();
    }

    public static void setUserPrefInt(Context paramContext, String paramString, int paramInt)
    {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("user_pref", 0).edit();
        localEditor.putInt(paramString, paramInt);
        localEditor.commit();
    }

    public static void setUserPrefLong(Context paramContext, String paramString, long paramLong)
    {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("user_pref", 0).edit();
        localEditor.putLong(paramString, paramLong);
        localEditor.commit();
    }

    public static void setUserPrefString(Context paramContext, String paramString1, String paramString2)
    {
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("user_pref", 0).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }
}
