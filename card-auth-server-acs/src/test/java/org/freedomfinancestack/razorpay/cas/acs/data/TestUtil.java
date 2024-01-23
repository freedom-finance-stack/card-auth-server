package org.freedomfinancestack.razorpay.cas.acs.data;

import java.lang.reflect.Field;
import java.util.Map;

public class TestUtil {
    public static <T> T replaceData(T currObject, Map<String, Object> mockData) {
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

    public static void setPrivateField(Object targetObject, String fieldName, Object newValue)
            throws NoSuchFieldException, IllegalAccessException {
        // Get the field from the target object's class
        Field field = targetObject.getClass().getDeclaredField(fieldName);

        // Make the field accessible, even if it's private or final
        field.setAccessible(true);

        // Set the new value for the field
        field.set(targetObject, newValue);
    }
}
