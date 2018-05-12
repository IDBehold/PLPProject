package sample.Util;

import java.awt.*;
import java.lang.reflect.Field;

public class ColorUtils {
    public static Color parseColor(String colorName) {
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(colorName.toUpperCase());
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = Color.BLACK;
        }
        return color;
    }
}
