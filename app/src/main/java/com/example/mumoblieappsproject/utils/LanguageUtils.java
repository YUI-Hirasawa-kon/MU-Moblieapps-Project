package com.example.mumoblieappsproject.utils;

import android.content.Context;

import java.util.Locale;

public class LanguageUtils {
    public static boolean isCurrentLanguageEnglish(Context context) {
        // 方法一：从 Configuration 获取
        Locale currentLocale = context.getResources().getConfiguration().locale;
        return currentLocale.getLanguage().equals("en");
    }

    public static String getCurrentLanguageTag(Context context) {
        Locale currentLocale = context.getResources().getConfiguration().locale;
        return currentLocale.toLanguageTag();
    }
}
