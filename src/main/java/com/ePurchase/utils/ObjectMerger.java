package com.ePurchase.utils;


import com.ePurchase.domain.NonEditableFromClient;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ObjectMerger {

    public static void mergeClientEditableFields(Object sourceObject, Object targetObject) {
        Set<Field> fieldSet = getAllFields(sourceObject);
        for (Field field : fieldSet) {
            boolean isClientEditableField = !field.isAnnotationPresent(NonEditableFromClient.class);
            if (shouldMerge(field)) {

                String fieldName = field.getName();
                Object sourceValue = null;
                try {
                    sourceValue = PropertyUtils.getSimpleProperty(sourceObject, field.getName());
                    PropertyUtils.setSimpleProperty(targetObject, fieldName, sourceValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }

            }

        }

    }

    private static boolean shouldMerge(Field field) {
        return !(field.isAnnotationPresent(NonEditableFromClient.class) || Modifier.isStatic(field.getModifiers()));
    }

    private static Set<Field> getAllFields(Object object) {
        Set<Field> fieldSet = new HashSet<>();
        Class clazz = object.getClass();
        do {
            populateFields(fieldSet, clazz);
            clazz = clazz.getSuperclass();
        }
        while (!Object.class.equals(clazz));
        return fieldSet;
    }

    private static void populateFields(Set<Field> fieldSet, Class clazz) {
        Collections.addAll(fieldSet, clazz.getDeclaredFields());
    }
}
