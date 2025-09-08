package com.varunu28.scratchpad.reflection.jsonserializer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import com.varunu28.scratchpad.reflection.jsonserializer.data.Actor;
import com.varunu28.scratchpad.reflection.jsonserializer.data.Movie;

public class JsonSerializerRunner {

    public static void main(String[] args) throws IllegalAccessException {
        Actor actor1 = new Actor("Elijah Wood", new String[] {"Lord of the Rings", "The Good Son"});
        Actor actor2 = new Actor("Ian McKellen", new String[] {"X-Men", "The Hobbit"});
        Actor actor3 = new Actor("Orlando Bloom", new String[] {"Pirates of the Caribbean", "Kingdom of Heaven"});

        Movie movie = new Movie(
            "Lord of the Rings",
            8.8f,
            new String[] {"Fantasy", "Adventure"},
            new Actor[]{actor1, actor2, actor3});

        String json = objectToJson(movie, 0);
        System.out.println(json);
    }

    public static String objectToJson(Object instance, int size) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(indent(size));
        sb.append("{");
        sb.append("\n");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.isSynthetic()) {
                continue;
            }
            sb.append(indent(size + 1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");
            if (field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if (field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isArray()) {
                sb.append(arrayToJson(field.get(instance), size + 1));
            } else {
                sb.append(objectToJson(field.get(instance), size + 1));
            }
            if (i < fields.length - 1) {
                sb.append(",");
            }
            sb.append(indent(size + 1));
            sb.append("\n");
        }
        sb.append(indent(size));
        sb.append("}");
        return sb.toString();
    }

    private static String arrayToJson(Object arrayObject, int indentSize) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        int arrayLength = Array.getLength(arrayObject);
        Class<?> componentType = arrayObject.getClass().getComponentType();
        sb.append("[");
        sb.append("\n");
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayObject, i);
            if (componentType.isPrimitive()) {
                sb.append(indent(indentSize + 1));
                sb.append(formatPrimitiveValue(element, componentType));
            } else if (componentType.equals(String.class)) {
                sb.append(indent(indentSize + 1));
                sb.append(formatStringValue(element.toString()));
            } else {
                sb.append(objectToJson(element, indentSize + 1));
            }
            if (i < arrayLength - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(indent(indentSize + 1));
        sb.append("]");
        return sb.toString();
    }

    private static String indent(int size) {
        return "\t".repeat(Math.max(0, size));
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) throws IllegalAccessException {
        if (type.equals(boolean.class)
            || type.equals(int.class)
            || type.equals(long.class)
            || type.equals(short.class)) {
            return instance.toString();
        } else if (type.equals(float.class) || type.equals(double.class)) {
            return String.format("%.2f", instance);
        }

        throw new RuntimeException("Not a primitive type");
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
