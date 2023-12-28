package org.freedomfinancestack.razorpay.cas.acs.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TestUtil {
    public static <T> T replaceData(
            T currObject, Map<String, Object> mockData) {
        for (String key : mockData.keySet()) {
            updateFields(currObject, key, mockData.get(key));
        }
        return currObject;
    }

    public static <T> void updateFields(T object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Handle exceptions or log as needed
            e.printStackTrace();
        }
    }
}
