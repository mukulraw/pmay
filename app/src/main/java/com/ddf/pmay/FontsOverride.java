package com.ddf.pmay;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

class FontsOverride {

    static void setDefaultFont(Context context) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                "fonts/CaviarDreamsBold.ttf");
        replaceFont(regular);
    }

    private static void replaceFont(final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField("MONOSPACE");
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
